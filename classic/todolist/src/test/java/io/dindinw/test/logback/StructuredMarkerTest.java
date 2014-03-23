package io.dindinw.test.logback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class StructuredMarkerTest {
    
    private final static Logger logger = LoggerFactory.getLogger("io.dindinw.test.logback.StructuredMarker");

    /**
     * We can implement a structured maker by using the %marker% pattern to print out the marker.
     * see https://github.com/qos-ch/logback/blob/master/logback-classic/src/main/java/ch/qos/logback/classic/pattern/MarkerConverter.java
     * and https://github.com/qos-ch/logback/blob/master/logback-classic/src/test/java/ch/qos/logback/classic/pattern/MarkerConverterTest.java
     * for more details
     * 
     * @author yidwu
     *
     */
    public static class StructuredMarker implements Marker{
        
        private static final long serialVersionUID = 1L;
        
        //In slf4j idea, the marker should be 1:1 with name and marker instance
        //it's actually just different with the idea about the marker per transaction.
        //see www.slf4j.org/api/org/slf4j/helpers/BasicMarker.html‎ for details
        private final Marker structuredMarker = org.slf4j.MarkerFactory.getMarker("StructuredMarker"+System.currentTimeMillis());
        
        private final Map<String,String> propertyMap;
        
        public StructuredMarker(){
            this.propertyMap = new HashMap<String,String>();
        }
        
        @Override
        public String getName() {
            return structuredMarker.getName();
        }

        @Override
        public void add(Marker reference) {
            structuredMarker.add(reference);
        }

        @Override
        public boolean remove(Marker reference) {
            return structuredMarker.remove(reference);
        }

        @Override
        public boolean hasChildren() {
            return structuredMarker.hasChildren();
        }

        @Override
        public boolean hasReferences() {
            return structuredMarker.hasReferences();
        }

        @Override
        public Iterator iterator() {
            return structuredMarker.iterator();
        }

        @Override
        public boolean contains(Marker other) {
            return structuredMarker.contains(other);
        }

        @Override
        public boolean contains(String name) {
            return structuredMarker.contains(name);
        }
        public void put(final String key, final String value){
            propertyMap.put(key, value);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getName());
            sb.append(propertyMap.toString());
            Iterator it = this.iterator();
            while (it.hasNext()){
                sb.append("{");
                sb.append(it.next().toString());
                sb.append("}");
            }
            return sb.toString();
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        final String msg = "FOO BAR";
        final StructuredMarker sMarker = new StructuredMarker();
        sMarker.put("id","1");
        sMarker.put("name","foo");
        sMarker.put("user","bar");
        
        logger.info(sMarker,msg);
        logger.info(sMarker,msg);
        logger.info(sMarker,msg);
        
        sMarker.put("id","4");
        sMarker.put("name","foo4");
        sMarker.put("user","bar4");
        logger.info(sMarker,msg);
        sMarker.put("id","5");
        sMarker.put("name","foo5");
        sMarker.put("user","bar5");
        logger.info(sMarker,msg);
        
        //Test for a children marker, although don't the use case.
        StructuredMarker child = new StructuredMarker();
        child.put("name", "child");
        sMarker.add(child);
        StructuredMarker grandson = new StructuredMarker();
        grandson.put("name", "grandson");
        child.add(grandson);
        logger.info(sMarker,msg);
        logger.info(child,msg);
        logger.info(grandson,msg);
        
    }

}