package io.dindinw.todolist.websocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class ToDoListClientEndpoint {
    
    @OnMessage
    public void processMessageFromServer(String msg, Session session) {
        System.out.println("Message came from the server ! " + msg);
    }
    
    /**
     * Method to be called when a new web socket session is open on client side.
     * @param session
     */
    @OnOpen
    public void onClientSideSessionOpen(final Session openSession){
        System.out.println("The Session is open at the client side " + openSession);
    }
    
    /**
     * Method to be called when a web socket session is closing on client side.
     * @param closeSession
     */
    @OnClose
    public void onClientSideSessionClose(final Session closingSession){
        System.out.println("The Session is closing at the client side " + closingSession);
        
    }


}
