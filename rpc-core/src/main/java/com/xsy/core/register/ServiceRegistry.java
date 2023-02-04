package com.xsy.core.register;

import com.xsy.rpc.common.exception.RpcException;

/**
 * 容器：保存一些本地服务的信息
 *      并且在获取一个服务名字的时候能够返回这个服务的信息
 */
public interface ServiceRegistry {
    //注册服务信息
    <T> void register(T service) throws RpcException;
    //获取服务信息
    Object getService(String serviceName) throws RpcException;
}
