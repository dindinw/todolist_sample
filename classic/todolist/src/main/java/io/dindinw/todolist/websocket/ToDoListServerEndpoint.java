package io.dindinw.todolist.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/todolist")
public class ToDoListServerEndpoint {

    /**
     * Method to receive incoming web socket messages
     * Each websocket endpoint only have one message handling method
     * @param msg
     */
    @OnMessage
    public void onMessageComing(String msg, Session session){
        
    }
    
    /**
     * Method to be called when a new web socket session is open.
     * @param session
     */
    @OnOpen
    public void onSessionOpen(Session openSession){
        
    }
    
    /**
     * Method to be called when a web socket session is closing
     * @param closeSession
     */
    @OnClose
    public void onSessionClose(Session closingSession){
        
    }
    
    
}
