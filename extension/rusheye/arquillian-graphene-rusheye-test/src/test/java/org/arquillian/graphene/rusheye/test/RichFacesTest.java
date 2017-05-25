package org.arquillian.graphene.rusheye.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.arquillian.graphene.rusheye.comparator.OcularResult;
import org.arquillian.graphene.rusheye.pages.RichFaces;
import org.arquillian.graphene.rusheye.pages.RichFaces60PercentSimilarity;
import org.arquillian.graphene.rusheye.pages.RichFacesPoll;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.testng.Arquillian;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@RunAsClient
public class RichFacesTest extends Arquillian {

    private final static String SNAPSHOT_PATH = "src/test/resources/snapshot/";
    private final static String RESULT_PATH = "src/test/resources/result/";

    @Page
    private RichFaces richFacesPage;

    @Page
    private RichFaces60PercentSimilarity richFacesPage60;

    @Page
    private RichFacesPoll richFacesPoll;

    @Test
    public void richFaceCompareWithNoBaseline() {
        richFacesPage.goTo("repeat", "ruby");

        Assert.assertFalse(isFilePresent(SNAPSHOT_PATH + "RichFaces.png"));

        // No baseline, so ocular compare will return true
        // and saves a snapshot for future compare
        Assert.assertTrue(richFacesPage.compare().isEqualsImages());
    }

    @Test(dependsOnMethods = { "richFaceCompareWithNoBaseline" })
    public void richFaceCompareWithBaseline() {
        richFacesPage.goTo("repeat", "ruby");

        Assert.assertTrue(isFilePresent(SNAPSHOT_PATH + "RichFaces.png"));

        // A snapshot has been saved already as part of previous test.
        // so ocular compare will return the result
        Assert.assertTrue(richFacesPage.compare().isEqualsImages());
    }

    @Test(dependsOnMethods = { "richFaceCompareWithBaseline" })
    public void richFaceCompareWithDifferentTheme() {
        richFacesPage.goTo("repeat", "wine");
        OcularResult result = richFacesPage.compare();

        // A snapshot has been saved already as part of previous test.
        // But there is a different theme
        // so ocular compare will return the result as false
        Assert.assertFalse(result.isEqualsImages());
        Assert.assertTrue(result.getSimilarity() > 60);
    }

    @Test(dependsOnMethods = { "richFaceCompareWithDifferentTheme" })
    public void richFaceSimilarityTest() {
        richFacesPage.goTo("repeat", "classic");
        OcularResult result = richFacesPage60.compare();

        // A snapshot has been saved already as part of previous test.
        // But there is a different theme
        // so ocular compare will return the result
        // the result is true because we want only 60% match. Not 100% match
        Assert.assertTrue(result.isEqualsImages());
    }

    @Test
    public void richFaceFragmentTestWithNoBaseline() {
        richFacesPage.goTo("repeat", "ruby");
        Assert.assertFalse(isFilePresent(SNAPSHOT_PATH + "RichFaceLeftMenu.png"));

        // Like a page object, ocular can also compare a fragment
        OcularResult result = richFacesPage.getLeftMenu().compare();
        Assert.assertTrue(result.isEqualsImages());
    }

    @Test(dependsOnMethods = { "richFaceFragmentTestWithNoBaseline" })
    public void richFaceFragmentTestWithBaseline() {
        richFacesPage.goTo("repeat", "wine");
        Assert.assertTrue(isFilePresent(SNAPSHOT_PATH + "RichFaceLeftMenu.png"));
        OcularResult result = richFacesPage.getLeftMenu().compare();

        // We load the page with different theme
        // But fragment does not affect
        // So the result is true
        Assert.assertTrue(result.isEqualsImages());
    }

    @Test(dependsOnMethods = { "richFaceFragmentTestWithBaseline" })
    public void richFaceNonDeterministicValueTest() throws InterruptedException {

        // There is a non-determinitic value in this page

        richFacesPage.goTo("poll", "wine");
        OcularResult beforeResult = richFacesPoll.compare();
        Assert.assertTrue(isFilePresent(SNAPSHOT_PATH + "RichFacesPoll.png"));
        Assert.assertTrue(beforeResult.isEqualsImages());

        // Wait for sometime to get the server date time changed
        Thread.sleep(4000);

        OcularResult afterResult = richFacesPoll.compare();

        // It should be equal because we exclude the date time element
        Assert.assertTrue(afterResult.isEqualsImages());
    }

    @BeforeTest
    public void cleanUpBaselines() throws IOException {
        deleteFile(SNAPSHOT_PATH + "RichFaces.png");
        deleteFile(SNAPSHOT_PATH + "RichFacesPoll.png");
        deleteFile(SNAPSHOT_PATH + "RichFaceLeftMenu.png");
        deleteFile(RESULT_PATH + "RichFaces.png");
        deleteFile(RESULT_PATH + "RichFacesPoll.png");
        deleteFile(RESULT_PATH + "RichFaceLeftMenu.png");
    }

    private boolean isFilePresent(String path) {
        return new File(path).exists();
    }

    private void deleteFile(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }
}