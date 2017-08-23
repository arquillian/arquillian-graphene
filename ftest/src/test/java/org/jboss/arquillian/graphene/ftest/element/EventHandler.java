/*
package org.jboss.arquillian.graphene.ftest.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class EventHandler implements WebDriverEventListener {

    public void beforeClick(WebElement element, WebDriver browser){
        try {
            PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
            writer.println("before click " + element);
            writer.close();
        }
        catch (FileNotFoundException, UnsupportedEncodingException e){
            System.out.println("logs not here");
        }
    }

    public void afterClick(WebDriver browser, WebElement element){
        try {
            PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
            writer.println("after click " + element);
            writer.close();
        }
        catch (FileNotFoundException, UnsupportedEncodingException e){
            System.out.println("logs not here");
        }
    }
}
*/
