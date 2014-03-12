package io.dindinw.todolist.websocket.server;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

@ServerEndpoint(value="/todolist")
public class ToDoListServerEndpoint {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ToDoListServerEndpoint.class);
    /**
     * Method to receive incoming web socket messages
     * Each websocket endpoint only have one message handling method
     * @param msg
     */
    @OnMessage
    public void processMessageFromClient(final String msg, final Session session){
        LOGGER.info("Message '{}' came from the client ! ", msg);        
    }
    
    /**
     * Method to be called when a new web socket session is open on server side.
     * @param session
     */
    @OnOpen
    public void onServerSideSessionOpen(final Session openSession){
        LOGGER.info("The Session is open at the server side " + openSession);
    }
    
    /**
     * Method to be called when a web socket session is closing on server side.
     * @param closeSession
     */
    @OnClose
    public void onServerSideSessionClose(final Session closingSession){
        LOGGER.info("The Session is closing at the server side " + closingSession);
        
    }
    
    
}
