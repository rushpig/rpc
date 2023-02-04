package com.xsy.core.handle;

import com.xsy.core.register.ServiceRegistry;
import com.xsy.rpc.common.entity.RpcRequest;
import com.xsy.rpc.common.entity.RpcResponse;
import com.xsy.rpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 这个类是请求处理线程：每一个请求处理线程中就需要传入ServiceRegistry
 * 这里把处理线程和处理逻辑分成了两个类：
 *      RequestHandlerThread:只是一个线程,从ServiceRegistry获取到提供服务的对象后
 *          就会把RpcRequest和服务对象交给RequestHandler来处理
 *      RequestHandler:反射等过程被放到了RequestHandler里
 */
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用或发送时有错误发生：", e);
        } catch (RpcException e) {
            e.printStackTrace();
        }
    }
}
