package io.dindinw.todolist.websocket.client;

import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class ToDoClient {
    
    final public String todoListWsUri = "ws://localhost:8080/todolist"; 
    
    private WebSocketContainer container;
    
    public ToDoClient(){
        
    }
    
    public void sendMsg(Object Msg) throws Exception{
        
        // Depends on the ServiceLoader (ask for META-INF/services/javax.websocket.ContainerProvider file contains impl classes)
        
        // jetty : org.eclipse.jetty.websocket.jsr356.JettyClientContainerProvider
        // https://github.com/eclipse/jetty.project/blob/master/jetty-websocket/javax-websocket-client-impl/src/main/resources/META-INF/services/javax.websocket.ContainerProvider
        
        // glassfish (tyrus) : org.glassfish.tyrus.container.inmemory.InMemoryContainerProvider
        // https://github.com/tyrus-project/tyrus/blob/master/containers/inmemory/src/main/resources/META-INF/services/javax.websocket.ContainerProvider        
        
        // or with the help from spring to inject a container? instead of fixed to a single impl?
        
        container = ContainerProvider.getWebSocketContainer();   
         
        // In Jetty's impl:
        //    org.eclipse.jetty.websocket.jsr356.ClientContainer
        //    is started, and return.
        // Client create 
        
        // Connect the supplied annotated endpoint instance to its server.
        // 1.) the websocket client endpoint instance. if don't provide a object instance, 
        // need a class with endpoint annotaion, and let container to do newInstace of the class.
        // 2.) path the complete path to the server endpoint.
        Session session = container.connectToServer(ToDoListClientEndpoint.class,URI.create(todoListWsUri));
        // 1 session to 1 physical connection (a socket connection)
        // see org.eclipse.jetty.websocket.client.io.ConnectionManager for details
        // Jettey don't support websocket-multiplex ( allow multiple WebSocket channels to run over the same TCP/IP connection. )
    
        // 1.) BasicRemote is the synchronous type of RemoteEndpoint (the peer side of connection ) 
        // 2.) sendText is the simplest method, 
        // 3.) sendObject() method is Encoder/Decoder required, otherwise error thrown.
        session.getBasicRemote().sendText("Hello");
        
    }

}
