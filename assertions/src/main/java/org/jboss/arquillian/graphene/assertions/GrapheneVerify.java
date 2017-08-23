/*
package org.jboss.arquillian.graphene.assertions;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.graphene.request.RequestGuard;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.jboss.arquillian.graphene.guard.RequestGuardFactory;

import java.io.*;
import java.util.Scanner;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class GrapheneVerify extends Actions {

    private final RequestGuard guard;
    private final Document document;
    private final FluentWait<WebDriver, Void> waitGuard;
    private final GrapheneContext context;
    private final DocumentReady documentReady = new DocumentReady(); //method from other class
    private final RequestIsDone requestIsDone = new RequestIsDone(); // both from requestGuardFactory
    private final RequestChange requestChange = new RequestChange();

    public Actions Verify(WebDriver driver){
        return new Actions(driver);
    }

    public static WebElement verify(WebElement element) {

    }

}
*/

/*try{
            ReversedLinesFileReader n = new ReversedLinesFileReader(new File("./log.txt"));
            String line;
            line = n.readLine();
            if(line.equals("after click " + element))
                line = n.readLine();
                if(line.equals("before click " + element))
                    return element;
            else
                throw new CouldNotVerifyException("Cannot verify the action was performed");



        }
        catch(FileNotFoundException e){
            System.out.println("no file");
        }
        catch(IOException e){
            System.out.println("io issues");
        }
    } //what can i do here?*/