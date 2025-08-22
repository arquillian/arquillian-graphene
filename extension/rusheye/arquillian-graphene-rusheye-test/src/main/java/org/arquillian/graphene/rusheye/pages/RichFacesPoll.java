package org.arquillian.graphene.rusheye.pages;

import org.arquillian.graphene.rusheye.annotation.RushEye;
import org.arquillian.graphene.rusheye.annotation.Snap;
import org.arquillian.graphene.rusheye.comparator.Ocular;
import org.arquillian.graphene.rusheye.comparator.OcularResult;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Snap("RichFacesPoll.png")
public class RichFacesPoll {

    @Drone
    private WebDriver driver;
    
    @FindBy(id="form:serverDate")
    private WebElement datetime;

    @RushEye
    private Ocular ocular;

    public void goTo(String demo, String skin){
        driver.get("http://showcase.richfaces.org/richfaces/component-sample.jsf?demo=" + demo + "&skin=" + skin);
    }

    public OcularResult compare() {
        return this.ocular.exclude(datetime)
                   .compare();
    }
}
