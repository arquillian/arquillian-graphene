package org.arquillian.graphene.rusheye.pages;

import org.arquillian.graphene.rusheye.annotation.RushEye;
import org.arquillian.graphene.rusheye.annotation.Snap;
import org.arquillian.graphene.rusheye.comparator.Ocular;
import org.arquillian.graphene.rusheye.comparator.OcularResult;
import org.arquillian.graphene.rusheye.fragments.RichFaceLeftMenu;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Snap("RichFaces.png")
public class RichFaces {

    @Drone
    private WebDriver driver;
    
    @FindBy(css="div.left-menu")
    private RichFaceLeftMenu leftmenu;

    @RushEye
    private Ocular ocular;

    public void goTo(String demo, String skin){
        driver.get("http://showcase.richfaces.org/richfaces/component-sample.jsf?demo=" + demo + "&skin=" + skin);
    }
       
    public RichFaceLeftMenu getLeftMenu(){
        return leftmenu;
    }
    
    public OcularResult compare() {
        return this.ocular.compare();
    }
}
