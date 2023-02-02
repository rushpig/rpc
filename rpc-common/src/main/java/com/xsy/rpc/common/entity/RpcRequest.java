package com.xsy.rpc.common.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户端发起一个调用请求我们服务端怎么知道它想要调用的是哪个接口哪个方法呢？
 * 首先需要知道接口的名字，方法的名字
 * 因为方法重载的缘故，我们还需要这个方法的所有参数的类型
 * 最后，客户端调用时，还要传递参数的实际值
 */
@Data
@Builder
public class RpcRequest implements Serializable {

    //待调用接口名称
    private String interfaceName;

    //待调用方法名称
    private String methodName;

    //调用方法的参数
    private  Object[] parameters;

    //调用方法的参数类型
    private Class<?>[] paramTypes;


}
