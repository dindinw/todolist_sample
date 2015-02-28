package test.io.dindinw.lang;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import io.dindinw.lang.Check;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void testCheckArg(){
        int age=28;
        thrown.expect(IllegalArgumentException.class);
        Check.checkArg(age>30, "Alex's age should not less than 30");
    }
    
    @Test
    public void testCheckArg2(){
        int age=28;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("[alex]'s age should not less than [30]");
        Check.checkArg(age>30, "[%s]'s age should not less than [%s]","alex",30);
    }
    
    @Test
    public void testCheckNull(){
        thrown.expect(NullPointerException.class);
        Check.checkNotNull(null);
    }
    @Test
    public void testCheckNull2(){
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Input should not be null");
        Check.checkNotNull(null,"Input should not be null");
    }
    
    @Test
    public void testCheckNull3(){
        Object input=null;
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("[input=null] should not be null");
        Check.checkNotNull(input,"[%s=%s] should not be null","input",input);
    }
    
    @Test
    public void testInputEmptyString(){
        String input="";
        String errorMsg="[%s] should not be empty";
        String errorArg="input";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("[input] should not be empty");
        Check.checkNotNullOrEmpty(input, errorMsg, errorArg);
    }
    @Test
    public void testInputNullString(){
        String input=null;
        String errorMsg="[%s] should not be null";
        String errorArg="input";
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("[input] should not be null");
        Check.checkNotNullOrEmpty(input, errorMsg, errorArg);
    }
    
    
}
