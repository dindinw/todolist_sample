package io.dindinw.todolist.websocket;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

public class ToDoClient {
    
    public void sendMsg(Object Msg){
        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();   
        final String uri = "ws://localhost:8080/todolist";  
        container.connectToServer();
    }

}
