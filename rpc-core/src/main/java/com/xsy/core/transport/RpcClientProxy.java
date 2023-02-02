package com.xsy.core.transport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.xsy.rpc.common.entity.RpcRequest;
import com.xsy.rpc.common.entity.RpcResponse;

/**
 * rpc客户端代理
 */
public class RpcClientProxy implements InvocationHandler {

    private String host;
    private int port;

    public RpcClientProxy(String host,int port){
        this.host=host;
        this.port=port;
    }


    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里使用builder()方法来生成这个对象（RpcRequest类里有Builder注解）
        RpcRequest rpcRequest=RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient=new RpcClient();
        return ((RpcResponse)rpcClient.sendRequest(rpcRequest,host,port)).getData();
    }
}
