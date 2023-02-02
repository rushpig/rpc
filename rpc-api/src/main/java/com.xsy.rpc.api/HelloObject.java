package com.xsy.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 测试api调用的实体：因为这个对象从客户端传到服务端，所以需要序列化
 @NoArgsConstructor：生成无参的构造方法。
 @AllArgsConstructor：生成该类下全部属性的构造方法。
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
