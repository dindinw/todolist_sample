package io.dindinw.todolist.websocket.server;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

/**
 * It's a Jetty server 
 * @author yidwu
 *
 */
public class ToDoListServer {
    public static void main(String[] args) throws Exception {
        Server jetty = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        jetty.setHandler(context);
        
        //set up websocket server container by Jetty server
        ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
        container.addEndpoint(ToDoListServerEndpoint.class);
        
        jetty.start();
        jetty.join(); //keep running until interrupt.
    }

}
