package com.cc.kingdeestart;

import com.cc.kingdeestart.core.IRequestClient;
import com.cc.kingdeestart.core.KingDeeProperty;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

@SpringBootTest
class KingdeeStartApplicationTests {

    @Resource
    IRequestClient iRequestClient;

    @Resource
    ApplicationContext applicationContext;

    @Resource
    KingDeeProperty kingDeeProperty;

    @Test
    void contextLoads() {
//        System.out.println("11");
//        System.out.println(kingDeeProperty.getServerUrl());
//        IRequestClient bean = applicationContext.getBean(IRequestClient.class);
        System.out.println("1111");
        iRequestClient.save("");
    }

}
