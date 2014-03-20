package io.dindinw.test.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * A Test to show how to force the logback to do log by ignoring the log level.
 * The solution try to use the TurboFilter feature of logback.
 * see details from : http://logback.qos.ch/manual/filters.html#TurboFilter
 * 
 * @author yidwu
 *
 */
public class ForceLogFilterTest {
    
    /**
     * The normal filter is scope is just the appender which is attached to.
     * So The force log feature CAN NOT implemented by using a normal filter.
     */
    public static class ForceLogFilter extends Filter<ILoggingEvent> {
        /**
         * it's not work because the scope is limited to a appender, 
         * appender can only see the effective level of a logger.
         */
        @Override
        public FilterReply decide(ILoggingEvent event) {
            return FilterReply.ACCEPT; //NOT WORK
        }
        
    }
    
    /**
     * Differences between normal Filter and TurboFilter objects. 
     * 
     * 1. TurboFilter objects are tied to the logging context. Hence, they are called not only 
     * when a given appender is used, but each and every time a logging request is issued. 
     * Their scope is wider than appender-attached filters. 
     * 2. TurboFilter called before the LoggingEvent object creation. TurboFilter objects do not require the 
     * instantiation of a logging event to filter a logging request. 
     * 
     * see http://logback.qos.ch/manual/filters.html#TurboFilter
     * 
     * The two customized tag here:
     * 1. targetLogger: the filter only works for specified logger name.
     * 2. doFilter: false flag to stop it working. the default is true.
     * 
     * @author yidwu
     *
     */
    public static class ForceLogTurboFilter extends TurboFilter  {
        private String targetLogger;
        private boolean doFilter=true;
        public boolean isDoFilter() {
            return doFilter;
        }
        public void setDoFilter(boolean doFilter) {
            this.doFilter = doFilter;
        }
        public String getTargetLogger() {
            return targetLogger;
        }
        public void setTargetLogger(String targetLogger) {
            this.targetLogger = targetLogger;
        }
        @Override
        public FilterReply decide(Marker marker,
                ch.qos.logback.classic.Logger logger, Level level,
                String format, Object[] params, Throwable t) {
            if (!isStarted()) {
                return FilterReply.NEUTRAL;
            }
            if (logger.getName().equals(targetLogger)) {
                return FilterReply.ACCEPT; //force log, ignore level
            }else{
                return FilterReply.NEUTRAL;
            }
        }
        @Override
        public void start() {
            if (isDoFilter()) {
                super.start();
            }
        }
        
    }
    /**
     * Here we use a fixed name instead of class name for a bitter name in our testing case. the fixed name will
     * be referred by our logback.xml
     */
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
