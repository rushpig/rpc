package com.xsy.rpc.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService{

    private static final Logger logger= LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject helloObject) {
        logger.info("服务端接受到:{}",helloObject.getMessage());
        return "这是调用的返回值,id="+helloObject.getId();
    }
}
