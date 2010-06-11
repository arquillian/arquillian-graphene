package org.jboss.test.selenium.utils.text.simplifiedFormat;

import junit.framework.Assert;

import org.testng.annotations.Test;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

public class SimpleFormatting {

    @Test
    public void testSimpleWithoutNumbers() {
        Assert.assertEquals("123", format("{}{}{}", 1, "2", '3'));
    }
    
    @Test
    public void testSimpleWitNumbers() {
        Assert.assertEquals("123", format("{0}{1}{2}", 1, "2", '3'));
    }
    
    @Test
    public void testSimpleWithNumbersNotInOrder() {
        Assert.assertEquals("321", format("{2}{1}{0}", 1, "2", '3'));
    }
    
    
}
