package org.arquillian.graphene.rusheye.pages;

import org.arquillian.graphene.rusheye.annotation.RushEye;
import org.arquillian.graphene.rusheye.annotation.Snap;
import org.arquillian.graphene.rusheye.comparator.Ocular;
import org.arquillian.graphene.rusheye.comparator.OcularResult;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;

@Snap("RichFacesTheme-#{theme}.png")
public class RichFacesTheme {

    @Drone
    private WebDriver driver;

    @RushEye
    private Ocular ocular;

    public void goTo(String demo, String skin){
        driver.get("http://showcase.richfaces.org/richfaces/component-sample.jsf?demo=" + demo + "&skin=" + skin);
    }
    
    public OcularResult compare(String theme) {

        return this.ocular
                    .replaceParameter("theme", theme)
                    .compare();
    }
    
    
}
