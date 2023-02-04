package com.xsy.rpc.common.exception;

import com.xsy.rpc.common.enumeration.RpcError;

public class RpcException extends Throwable {

    RpcError rpcError;

    public RpcException(RpcError serviceNotFound) {
        rpcError = serviceNotFound;
    }
}
