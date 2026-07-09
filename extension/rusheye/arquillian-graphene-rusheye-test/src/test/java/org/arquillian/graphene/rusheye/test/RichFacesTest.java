package org.arquillian.graphene.rusheye.test;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.graphene.rusheye.comparator.OcularResult;
import org.arquillian.graphene.rusheye.pages.RichFaces;
import org.arquillian.graphene.rusheye.pages.RichFaces60PercentSimilarity;
import org.arquillian.graphene.rusheye.pages.RichFacesPoll;
import org.arquillian.graphene.rusheye.pages.RichFacesTheme;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.page.Page;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@RunAsClient
public class RichFacesTest extends RichFaceBaseTest {

    @Page
    private RichFaces richFacesPage;
    
    @Page
    private RichFaces60PercentSimilarity richFacesPage60;
    
    @Page
    private RichFacesPoll richFacesPoll;
    
    @Page
    private RichFacesTheme richFacesTheme;
    
    
    /*
     * 
     *                Page objects visual validation
     * 
     */

    @Test
    public void visual_validation_when_there_is_no_basleine() {
       
        richFacesPage.goTo("repeat", "ruby");

        Assert.assertFalse(isFilePresent("RichFaces.png"));

        // No baseline, so ocular compare will return true
        // and saves a snapshot for future compare
        Assert.assertTrue(richFacesPage.compare().isEqualsImages());
    }

    @Test(dependsOnMethods = { "visual_validation_when_there_is_no_basleine" })
    public void visual_validation_when_there_is_a_basleine() {
        richFacesPage.goTo("repeat", "ruby");

        Assert.assertTrue(isFilePresent("RichFaces.png"));

        // A snapshot has been saved already as part of previous test.
        // so ocular compare will return the result
        Assert.assertTrue(richFacesPage.compare().isEqualsImages());
    }

    @Test(dependsOnMethods = { "visual_validation_when_there_is_a_basleine" })
    public void visual_validation_when_theme_changes() {
        richFacesPage.goTo("repeat", "wine");
        OcularResult result = richFacesPage.compare();

        // A snapshot has been saved already as part of previous test.
        // But there is a different theme
        // so ocular compare will return the result as false
        Assert.assertFalse(result.isEqualsImages());
        Assert.assertTrue(result.getSimilarity() > 60);
    }
    
    
    /*
     * 
     *                Page fragemnts visual validation
     * 
     */

    @Test(dependsOnMethods = { "visual_validation_when_theme_changes" })
    public void visual_validation_when_there_is_no_basleine_for_fragment() {
        richFacesPage.goTo("repeat", "ruby");
        Assert.assertFalse(isFilePresent("RichFaceLeftMenu.png"));

        // Like a page object, ocular can also compare a fragment
        OcularResult result = richFacesPage.getLeftMenu().compare();
        Assert.assertTrue(result.isEqualsImages());
    }

    @Test(dependsOnMethods = { "visual_validation_when_there_is_no_basleine_for_fragment" })
    public void visual_validation_when_there_is_a_basleine_for_fragment() {
        richFacesPage.goTo("repeat", "wine");
        Assert.assertTrue(isFilePresent("RichFaceLeftMenu.png"));
        OcularResult result = richFacesPage.getLeftMenu().compare();

        // We load the page with different theme
        // But fragment does not affect
        // So the result is true
        Assert.assertTrue(result.isEqualsImages());
    }

    
    /*
     * 
     *                Similariy Test
     * 
     */
    
    @Test(dependsOnMethods = { "visual_validation_when_there_is_a_basleine_for_fragment" })
    public void visual_validation_for_similarity() {
        richFacesPage.goTo("repeat", "classic");
        OcularResult result = richFacesPage60.compare();

        // A snapshot has been saved already as part of previous test.
        // But there is a different theme
        // so ocular compare will return the result
        // the result is true because we want only 60% match. Not 100% match
        Assert.assertTrue(result.isEqualsImages());
    }
    
    
    /*
     * 
     *                Non-determinitic elements exclusion
     * 
     */
    
    @Test(dependsOnMethods = { "visual_validation_for_similarity" })
    public void check_for_excluding_element() throws InterruptedException {
        // There is a non-determinitic value in this page

        richFacesPage.goTo("poll", "wine");
        OcularResult beforeResult = richFacesPoll.compare();
        Assert.assertTrue(isFilePresent("RichFacesPoll.png"));
        Assert.assertTrue(beforeResult.isEqualsImages());

        // Wait for sometime to get the server date time changed
        Thread.sleep(4000);

        OcularResult afterResult = richFacesPoll.compare();

        // It should be equal because we exclude the date time element
        Assert.assertTrue(afterResult.isEqualsImages());
    }  
    
    /*
     * 
     *               Dynamic snapshot selection
     * 
     */    
    
    @Test(dependsOnMethods = { "check_for_excluding_element" })
    public void check_for_runtime_selection_of_snapshot() {
     
        for(String theme: getAllThemes()){
            richFacesTheme.goTo("ajax", theme);
            richFacesTheme.compare(theme);
            Assert.assertTrue(isFilePresent("RichFacesTheme-" + theme + ".png"));  
        }
    }    
    
    //TestNG Data provider does not work. So going for a temp solution
    private List<String> getAllThemes() {
       
       List<String> themes = new ArrayList<String>();
       themes.add("wine");
       themes.add("ruby");
       themes.add("japanCherry");
       themes.add("emeraldTown");
       themes.add("deepMarine");
       themes.add("classic");
       themes.add("blueSky");
       return themes;
       
    }
}