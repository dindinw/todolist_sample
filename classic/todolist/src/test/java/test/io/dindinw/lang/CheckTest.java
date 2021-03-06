package test.io.dindinw.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void test_CheckArg_1SimpleErrorMessage(){
        int age=28;
        thrown.expect(IllegalArgumentException.class);
        Check.checkArg(age<30, "Alex's age should not less than 30");
    }
    
    @Test
    public void test_CheckArg_2FormattedErrorMessage(){
        int age=28;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("[alex]'s age should not less than [30]");
        Check.checkArg(age<30, "[%s]'s age should not less than [%s]","alex",30);
    }
    
    
    @Test
    public void test_CheckNull_1null(){
        thrown.expect(IllegalArgumentException.class);
        Check.checkNotNull(null);
    }
    
    @Test
    public void test_CheckNull_1nullMsg(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("null");  //notice the output
        Check.checkNotNull(null,null);
    }
    
    @Test
    public void test_CheckNull_1nullMsg_varargs(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("some thing null, some thing not_null");
        Check.checkNotNull(null,"some thing %s, some thing %s",null,"not_null"); 
    }
    @Test
    public void test_CheckNull_2SimpleErrorMessage(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Input should not be null");
        Check.checkNotNull(null,"Input should not be null");
    }
    
    @Test
    public void test_CheckNull_3FormattedErrorMessage(){
        Object input=null;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("[input=null] should not be null");
        Check.checkNotNull(input,"[%s=%s] should not be null","input",input);
    }
    
    @Test
    public void test_checkNotEmpty_EmptyString(){
        String input="";
        String errorMsg="[%s] should not be empty";
        String errorArg="input";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("[input] should not be empty");
        Check.checkNotEmpty(input, errorMsg, errorArg);
    }
    @Test
    public void test_checkNotEmpty_NullString(){
        String input=null;
        String errorMsg="[%s] should not be null";
        String errorArg="input";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("[input] should not be null");
        Check.checkNotEmpty(input, errorMsg, errorArg);
    }
    
    @Test
    public void test_checkNotEmpty_EmptyArray(){
        thrown.expect(IllegalArgumentException.class);
        Check.checkNotEmpty(new String[]{});
    }
    @Test
    public void test_checkNotEmpty_EmptyList(){
        thrown.expect(IllegalArgumentException.class);
        Check.checkNotEmpty(new ArrayList<>());
    }
    
    
    @Test
    public void test_checkElementsNotNull_1array_1(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("at index [1]"); 
        Check.checkNotContainNullElement(new Object[]{"alex",null,"wu"}); //the 2nd is null
    }
    
    @Test
    public void test_checkElementsNotNull_1array_2(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("at index [1]"); 
        Check.checkNotContainNullElement(new Object[]{"",null}); //the 2nd is null
    }
    
    @Test
    public void test_checkElementsNotNull_2list(){
        thrown.expect(IllegalArgumentException.class);
        List<String> myList = Arrays.asList("alex",null,"wu");
        Check.checkNotContainNullElement(myList); //the 2nd is null
    }
    
    @Test
    public void test_checkElementsNotNull_3Set(){
        thrown.expect(IllegalArgumentException.class);
        Set<String> mySet = new HashSet<>();
        mySet.addAll(Arrays.asList("alex",null,"wu"));
        Check.checkNotContainNullElement(mySet); //the 2nd is null
    }
    
    
    @Test
    public void test_checkNullElement(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("at index [0]"); 
        Check.checkNotContainNullElement(new Object[]{null,"alex","wu"}, 0); //the 1nd is null
    }
    
    @Test
    public void test_checkElementsNotEmpty_normal(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("empty string at index [0]");
        Check.checkNotContainEmptyElement(new String[]{"","alex","wu"}); //the 1nd is empty
    }
    
    @Test
    public void test_checkElementsNotEmpty_emptyArray(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("input String[] should not be empty");
        Check.checkNotContainEmptyElement(new String[]{}); // the 2nd is empty
        
    }

    @Test
    public void testStringChecker(){
        Check.StringChecker checker = Check.getChecker(Check.StringChecker.class, "A");
        assertTrue(checker instanceof Check.StringChecker);
        assertTrue(checker.check("A").isLetter());
        assertFalse(checker.check("124").isSingleChar());
        assertTrue(checker.check("B").isLetter()&&checker.isSingleChar());

    }

    @Test
    public void testNumberChecker(){
        assertTrue(Check.getChecker(Check.NumberChecker.class, 2) instanceof Check.NumberChecker);
    }

   
}
