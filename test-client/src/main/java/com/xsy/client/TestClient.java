package com.xsy.client;

import com.xsy.core.transport.RpcClientProxy;
import com.xsy.rpc.api.HelloObject;
import com.xsy.rpc.api.HelloService;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy=new RpcClientProxy("127.0.0.1",8888);
        HelloService helloService=proxy.getProxy(HelloService.class);
        HelloObject object=new HelloObject(888,"This is a message");
        String res=helloService.hello(object);
    }
}
