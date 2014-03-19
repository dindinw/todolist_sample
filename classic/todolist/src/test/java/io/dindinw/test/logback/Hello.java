package io.dindinw.test.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class Hello {
    // get logger, the logger context created in the background
    private final static Logger logger = LoggerFactory.getLogger(Hello.class);

    public static void main(String[] args) {
        
        // print internal context state
        printContextStatus();
        
        // only this info level log  is shown, because logback.xml set to 'root level="info"'
        // and 'logger name="io.dindinw.test.logback.Hello" level="info"'
        printInInfoLevel(); 
        printInDebugtLevel(); //not print
        printInTraceLevel(); //not print
        
        //after that, all log levels are printed!
        forceLog(); 
        printInDebugtLevel(); //print
        printInTraceLevel(); //print

    }
    private static void printContextStatus(){
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }
    private static void printInInfoLevel() {
        logger.info("Hello world in Info level");        
    }
    public static void printInDebugtLevel(){
        logger.debug("Hello world in Debug");
    }
    
    public static void printInTraceLevel(){
        logger.trace("Hello world in Trace level");        
    }
    
    /**
     * Try to set all logger's Level to ALL, so all level's log can be printed.
     * Note: Actually we don't need to do this in such stupid way. a smarter alternative may a 
     * Filter
     */
    public static void forceLog(){
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ALL);
        logger.info("root logger's level changed to ALL");
        /*
         *  also need to force to set the current logger to level ALL because 
         *  we already had defined the logger explicitly in logback.xml.
         */
        ((ch.qos.logback.classic.Logger)logger).setLevel(Level.ALL); 
        final String msg = "I am always log";
        logger.info(msg);
        logger.debug(msg);
        logger.trace(msg);
    }
    
}
