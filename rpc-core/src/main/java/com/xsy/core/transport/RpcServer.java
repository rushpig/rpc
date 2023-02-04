package com.xsy.core.transport;


import com.xsy.core.handle.RequestHandler;
import com.xsy.core.handle.RequestHandlerThread;
import com.xsy.core.register.ServiceRegistry;
import com.xsy.rpc.common.entity.RpcRequest;
import com.xsy.rpc.common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 版本二：降低耦合度：不会将ServiceRegistry和RpcService绑定在一起
 *       而是在创建RpcServer对象时， 传入一个ServiceRegistry作为这个服务的注册表
 *
 *       解耦二：此时服务的注册不由RpcServer处理
 *              Rpc只需要启动RequestHandlerThread就可以了
 */

public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);//阻塞队列
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器启动……");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {//这里不是很懂
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);
        }
    }

}












///**
// * 服务端：使用ServerSocket监听某个端口，循环接受连接信息，
// * 如果发来了请求就创建一个线程(BIO模式，后面会修改成NIO)
// * 在新线程中处理调用，这里创建线程采用线程池
// */
//public class RpcService {
//
//    private final ExecutorService threadPool;//java提供的线程池
//    private static final Logger logger= LoggerFactory.getLogger(RpcService.class);
//
//    public RpcService(){
//        int corePoolSize=5;
//        int maximumPoolSize=50;
//        long keepAliveTime=60;
//        BlockingQueue<Runnable> workingQueue=new ArrayBlockingQueue<>(100);
//        ThreadFactory threadFactory= Executors.defaultThreadFactory();
//        threadPool=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
//    }
//
//    /**
//     * 这里简化了一下，RpcServer暂时只能注册一个接口
//     * 既对外提供一个接口的调用服务，添加register方法
//     * 在注册完一个服务后立刻开始监听
//     */
//    public void register(Object service,int port){
//        try (ServerSocket serverSocket=new ServerSocket(port)){
//            logger.info("服务器正在启动...");
//            Socket socket;
//            while((socket=serverSocket.accept())!=null){
//                logger.info("客户端连接！Ip为:"+socket.getInetAddress());
//                threadPool.execute(new WorkerThread(socket,service));
//            }
//        }catch (IOException e){
//            logger.error("连接时有错误发生:",e);
//        }
//    }
//
//    /**
//     * 这是个内部类，是我们的工作线程
//     * 这里向工作线程WorkerThread传入socket和用于服务端实例service
//     * workerThread实现了Runable接口，用于接受RpcRequest对象
//     * 解析并且调用，生成RpcResponse对象并传输回去
//     */
//    private class WorkerThread implements Runnable{
//        Socket socket;
//        Object service;
//        public WorkerThread(Socket socket,Object service){
//            this.socket=socket;
//            this.service=service;
//        }
//
//        @Override
//        public void run() {
//            try (ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
//                 ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream())){
//                RpcRequest rpcRequest=(RpcRequest)objectInputStream.readObject();
//                Method method=service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
//                Object returnObject=method.invoke(service,rpcRequest.getParameters());
//                objectOutputStream.writeObject(RpcResponse.success(returnObject));
//                objectOutputStream.flush();
//            } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException|IOException |ClassNotFoundException e) {
//                logger.error("调用或发送时有错误发生");
//            }
//        }
//    }
//}
