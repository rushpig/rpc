package com.xsy.rpc.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 将成功的返回值写成一个枚举类型???
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200,"调用方法失败"),
    FAIL(500,"调用方法失败"),
    METHOD_NOT_FOUND(500,"未找到指定方法"),
    CLASS_NOT_FOUND(500,"未找到指定类");

    private  final int code;
    private final String message;

}
/*
为什么要用枚举类？
 */