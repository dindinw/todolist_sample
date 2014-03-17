package io.dindinw.todolist.websocket.client;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientEndpoint
public class ToDoListClientEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToDoListClientEndpoint.class);
    private static final org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger("session");
    
    @OnMessage
    public void processMessageFromServer(String msg, Session session) {
        LOGGER.info("Message came from the server ! " + msg);
    }
    
    /**
     * Method to be called when a new web socket session is open on client side.
     * @param session
     */
    @OnOpen
    public void onClientSideSessionOpen(final Session openSession){
        log4jLogger.info(openSession);
        LOGGER.info("The Session is open at the client side " + openSession);
    }
    
    /**
     * Method to be called when a web socket session is closing on client side.
     * @param closeSession
     */
    @OnClose
    public void onClientSideSessionClose(final Session closingSession){
        LOGGER.info("The Session is closing at the client side " + closingSession);
        
    }


}
