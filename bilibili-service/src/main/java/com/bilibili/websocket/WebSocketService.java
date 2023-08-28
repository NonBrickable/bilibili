package com.bilibili.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/websocket")
public class WebSocketService {
    private final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    /**
     * 当前连接数
     * 为了保证线程安全引入的实体类：AtomicInteger
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * 存储每个客户端连接的连接信息
     */
    private static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 每个连接的session
     */
    private Session session;

    /**
     * 每个连接的唯一标识符
     */
    private String sessionId;

    /**
     * 公用的上下文
     */
    private static ApplicationContext APPLICATION_CONTEXT;
    public static void setApplicationContext(ApplicationContext applicationContext){
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 打开连接
     * session:后端存储的session
     */
    @OnOpen
    public void openConnection(Session session) {
        this.sessionId = session.getId();
        this.session = session;
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        } else {
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功" + sessionId + "当前在线人数" + ONLINE_COUNT.get());
        try {
            this.sendMessage("0");
        } catch (Exception e) {
            logger.error("连接异常");
        }
    }

    /**
     * 关闭连接（比如服务端断了，或者客户端关闭了页面）之后调用@OnClose对应的方法
     */
    @OnClose
    public void closeConnection(){
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出" + sessionId + "当前在线人数" + ONLINE_COUNT.get());
    }

    /**
     * 有消息通讯时调用
     * @param message
     */
    @OnMessage
    public void onMessage(String message){

    }

    /**
     * 发生错误时调用
     * @param error
     */
    @OnError
    public void onError(Throwable error){

    }

    /**
     * 给前端发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
