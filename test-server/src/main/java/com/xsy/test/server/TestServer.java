package com.xsy.test.server;

import com.xsy.core.transport.RpcService;
import com.xsy.rpc.api.HelloObject;
import com.xsy.rpc.api.HelloService;
import com.xsy.rpc.api.HelloServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloObject=new HelloServiceImpl();
        RpcService rpcService=new RpcService();
        rpcService.register(helloObject,8888);

    }
}
