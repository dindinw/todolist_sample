package io.dindinw.test.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ForceLogFilterTest {
    public static class ForceLogFilter extends Filter<ILoggingEvent> {

        @Override
        public FilterReply decide(ILoggingEvent event) {
            
            return FilterReply.ACCEPT; //always accept
        }
        
    }
    private final static Logger logger = LoggerFactory.getLogger("io.dindinw.test.logback.ForceLog");
    
    public static void main(String[] args) {
        String msg = "FOO BAR";
        logger.error(msg);
        logger.warn(msg);
        logger.info(msg);
        logger.debug(msg);
        logger.trace(msg);
    }

}
