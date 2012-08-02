package org.jboss.arquillian.graphene.page.interception;

import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;

@JavaScript("Graphene.formSubmissionInterception")
public abstract class FormSubmissionInterception {

    public abstract void bind();

    public abstract boolean unbind();

    public abstract boolean isBound();

    public abstract boolean isSubmissionPaused();
    
    public abstract void continueSubmission();
    
    public abstract WebElement getSubmittedForm();

    public void waitForSubmissionPaused() {
        // TODO waiting
    }
}
