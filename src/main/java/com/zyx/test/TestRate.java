package com.zyx.test;


import com.zyx.DemoApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
public class TestRate implements CommandLineRunner {


    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationConnect;

    String expectedJson;
    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();

    }

    @Test
    public void show() throws Exception
    {
        String responseString = mvc.perform
                (
                        get("/ratelimit")          //请求的url,请求的方法是get

                )
                .andExpect(status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);


    }
    @Test
    public void onApplicationEvent() {
        ///设置线程池最大执行20个线程并发执行任务
        int threadSize = 20;
        //AtomicInteger通过CAS操作能保证统计数量的原子性
        AtomicInteger successCount = new AtomicInteger(0);
        CountDownLatch downLatch = new CountDownLatch(20);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            System.out.println("开始请求");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fixedThreadPool.execute(() -> {
                RestTemplate restTemplate = new RestTemplate();
                String str = null;
                try {
                    str = mvc.perform(get("/ratelimit")).andReturn().getModelAndView().getViewName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
                if ("success".equals(str)) {
                    successCount.incrementAndGet();
                }
                System.out.println(str);
                downLatch.countDown();
            });
        }
        //等待所有线程都执行完任务
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fixedThreadPool.shutdown();
        System.out.println("总共有" + successCount.get() + "个线程获得到了令牌!");
    }




    @Override
    public void run(String... args) throws Exception {
        System.out.println("通过实现CommandLineRunner接口，在spring boot项目启动后打印参数");

        this.onApplicationEvent();
    }
}
