package com.playtube.controller.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/websocket/{token}")
@Slf4j
public class WebSocketService {

    /**
     * 1.当前连接数
     * 为了保证线程安全引入的实体类：AtomicInteger
     */
    public static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * 2.存储每个客户端连接的连接信息
     */
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 3.每个连接的session
     */
    private Session session;

    /**
     * 4.每个连接的唯一标识符
     */
    private String sessionId;

    /**
     * 5.连接者的userId
     */
    private Long userId;

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
    public void openConnection(Session session, @PathParam("token") String token) {
        log.info("打开连接");
        System.out.println("打开连接");
//        try{
//            this.userId = TokenUtil.verifyToken(token);
//        }catch (Exception e){}
//        this.sessionId = session.getId();
//        this.session = session;
//        if (WEBSOCKET_MAP.containsKey(sessionId)) {
//            WEBSOCKET_MAP.remove(sessionId);
//            WEBSOCKET_MAP.put(sessionId, this);
//        } else {
//            WEBSOCKET_MAP.put(sessionId, this);
//            ONLINE_COUNT.getAndIncrement();
//        }
        log.info("用户连接成功" + sessionId + "当前在线人数" + ONLINE_COUNT.get());
        //告诉前端连接成功
        try {
            this.sendMessage("连接成功");
        } catch (Exception e) {
            log.error("连接异常");
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
        log.info("用户退出" + sessionId + "当前在线人数" + ONLINE_COUNT.get());
    }

    /**
     * 有消息通讯时调用
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        log.info("用户信息：" + sessionId + ",报文：" + message);
//        if(!StringUtil.isNullOrEmpty(message)){
//            try{
//                //1.给连接用户群发消息
//                for(Map.Entry<String,WebSocketService> entry:WEBSOCKET_MAP.entrySet()){
//                    WebSocketService webSocketService = entry.getValue();
//                    //获取生产者
//                    DefaultMQProducer defaultMQProducer = (DefaultMQProducer) APPLICATION_CONTEXT.getBean("barrageProducer");
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("message",message);
//                    jsonObject.put("sessionId",webSocketService.getSessionId());
//                    Message msg = new Message(UserMomentsConstant.BARRAGE_TOPIC,jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
//                    RocketMQUtil.asyncSendMsg(defaultMQProducer,msg);
//                }
//                //2.如果不是访客，则保存到数据库
//                if(userId != null){
//                    Barrage barrage = JSONObject.parseObject(message,Barrage.class);
//                    barrage.setUserId(userId);
//                    BarrageService barrageService = (BarrageService) APPLICATION_CONTEXT.getBean("barrageService");
//                    //异步保存到mysql数据库中
//                    barrageService.asyncAddBarrage(barrage);
//                    //同步保存到Redis中
//                    barrageService.addBarrageToRedis(barrage);
//                }
//            }catch (Exception e){
//                log.error("弹幕接收出现问题");
//                e.printStackTrace();
//            }
        }
//    }

    /**
     * 发生错误时调用
     * @param error
     */
    @OnError
    public void onError(Throwable error){
        log.error("出现error：" + error);
    }

    /**
     * 给前端发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //定时向前端推送在线人数
    @Scheduled(fixedRate = 5000)
    public void noticeOnlineCount() throws IOException{
        for(Map.Entry<String,WebSocketService> entry:WebSocketService.WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            if(webSocketService.getSession().isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount:",ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    public Session getSession() {
        return session;
    }
    public String getSessionId() {
        return sessionId;
    }
}
