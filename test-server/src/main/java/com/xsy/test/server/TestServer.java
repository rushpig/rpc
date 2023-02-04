package com.xsy.test.server;

import com.xsy.core.register.DefaultServiceRegistry;
import com.xsy.core.register.ServiceRegistry;
import com.xsy.core.transport.RpcServer;
import com.xsy.rpc.api.HelloObject;
import com.xsy.rpc.api.HelloService;
import com.xsy.rpc.api.HelloServiceImpl;
import com.xsy.rpc.common.exception.RpcException;

public class TestServer {
    public static void main(String[] args) throws RpcException {
        HelloService helloObject = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloObject);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(8888);

    }
}
