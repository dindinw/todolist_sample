package io.dindinw.todolist.log4j;

import javax.websocket.Session;

import org.apache.log4j.or.ObjectRenderer;

public class SessionRenderer implements ObjectRenderer{

    @Override
    public String doRender(Object o) {
        if (o instanceof Session){
            Session s = (Session)o;
            return String.format("I am Rendered : id is [%s], version is [%s] ",
                    s.getId(), s.getProtocolVersion());
        }
        return null;
    }

}
