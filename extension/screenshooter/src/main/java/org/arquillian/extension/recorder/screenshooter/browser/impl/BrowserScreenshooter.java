/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.arquillian.extension.recorder.screenshooter.browser.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.arquillian.extension.recorder.RecorderFileUtils;
import org.arquillian.extension.recorder.screenshooter.Screenshooter;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.Screenshot;
import org.arquillian.extension.recorder.screenshooter.ScreenshotMetaData;
import org.arquillian.extension.recorder.screenshooter.ScreenshotType;
import org.arquillian.recorder.reporter.impl.TakenResourceRegister;
import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.drone.webdriver.factory.remote.reusable.ReusableRemoteWebDriver;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class BrowserScreenshooter implements Screenshooter {

    private File screenshotTargetDir = null;
    private ScreenshotType screenshotType = null;
    private ScreenshooterConfiguration configuration;
    private TakenResourceRegister takenResourceRegister;
    private String message;

    private Logger log = Logger.getLogger(BrowserScreenshooter.class.getName());

    /**
     *
     * @param takenResourceRegister
     * @throws IllegalArgumentException if {@code TakenResourceRegister} is a null object
     */
    public BrowserScreenshooter(TakenResourceRegister takenResourceRegister) {
        Validate.notNull(takenResourceRegister, "Taken resource register can not be a null object!");
        this.takenResourceRegister = takenResourceRegister;
    }

    @Override
    public Screenshot takeScreenshot() {
        return takeScreenshot(screenshotType);
    }

    @Override
    public Screenshot takeScreenshot(ScreenshotType type) {
        Validate.notNull(type, "Screenshot type is a null object!");
        ScreenshotMetaData metaData = new ScreenshotMetaData();
        metaData.setResourceType(type);
        return takeScreenshot(
            new File(ResourceIdentifierFactory.getResoruceIdentifier(metaData, null).getIdentifier(type)),
            type);
    }

    @Override
    public Screenshot takeScreenshot(String fileName) {
        Validate.notNullOrEmpty(fileName, "File name is a null object or an empty string!");
        return takeScreenshot(new File(fileName));
    }

    @Override
    public Screenshot takeScreenshot(File file) {
        Validate.notNull(file, "File is a null object!");
        return takeScreenshot(file, screenshotType);
    }

    private File getJustTargetDir(File screenshotToTake) {
        return new File(screenshotToTake.getPath().substring(0,
            screenshotToTake.getPath().lastIndexOf(System.getProperty("file.separator"))));
    }

    @Override
    public Screenshot takeScreenshot(String fileName, ScreenshotType type) {
        Validate.notNullOrEmpty(fileName, "File name is a null object or an empty string!");
        Validate.notNull(type, "Type of screenshot is a null object!");
        return takeScreenshot(new File(fileName), type);
    }

    @Override
    public Screenshot takeScreenshot(File screenshotToTake, ScreenshotType type) {
        WebDriver browser = getTakingScreenshotsBrowser(screenshotToTake);

        screenshotToTake = new File(screenshotTargetDir, screenshotToTake.getPath());
        File targetDir = getJustTargetDir(screenshotToTake);
        RecorderFileUtils.createDirectory(targetDir);

        if (browser == null) {
            return null;
        }

        try {
            FileUtils.copyFile(((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE), screenshotToTake);
        } catch (Exception e) {
            throw new RuntimeException("Error during saving the screenshot!", e);
        }

        Screenshot screenshoot = new BrowserScreenshot();
        screenshoot.setResource(screenshotToTake);
        screenshoot.setResourceType(screenshotType);
        screenshoot.setMessage(message);

        try {
            BufferedImage bimg = ImageIO.read(screenshotToTake);
            screenshoot.setWidth(bimg.getWidth());
            screenshoot.setHeight(bimg.getHeight());
        } catch (IOException e) {
            throw new RuntimeException("Unable to get width and height of taken image located at "
                + screenshoot.getResource().getAbsolutePath());
        }

        takenResourceRegister.addTaken(screenshoot);

        return screenshoot;
    }

    private WebDriver getTakingScreenshotsBrowser(File screenshotToTake) {
        WebDriver result = null;

        try {
            GrapheneContext context = GrapheneContext.getContextFor(Default.class);
            result = ((GrapheneProxyInstance) context.getWebDriver(TakesScreenshot.class)).unwrap();
            //FIXME remove this try-catch block and bring new solution
        } catch (IllegalStateException ex) {
            log.info("The screenshot " + screenshotToTake.getPath() + " hasn't been taken."
                         + " The reason: " + ex.getMessage());
            return null;
        }

        if (result instanceof ReusableRemoteWebDriver) {
            result = new Augmenter().augment(result);
        }
        return result;
    }

    @Override
    public Screenshooter setScreenshotTargetDir(String screenshotTargetDir) {
        Validate.notNullOrEmpty(screenshotTargetDir,
                                "Screenshot target directory can not be a null object or an empty string");
        return setScreenshotTargetDir(new File(screenshotTargetDir));
    }

    @Override
    public Screenshooter setScreenshotTargetDir(File screenshotTargetDir) {
        Validate.notNull(screenshotTargetDir, "File is a null object!");
        RecorderFileUtils.createDirectory(screenshotTargetDir);
        this.screenshotTargetDir = screenshotTargetDir;
        return this;
    }

    @Override
    public Screenshooter setScreenshotType(ScreenshotType screenshotType) {
        Validate.notNull(screenshotType, "Screenshot type is a null object!");
        this.screenshotType = screenshotType;
        return this;
    }

    @Override
    public void init(ScreenshooterConfiguration configuration) {
        if (this.configuration == null) {
            if (configuration != null) {
                this.configuration = configuration;
                setScreenshotTargetDir(configuration.getRootDir());
                setScreenshotType(ScreenshotType.valueOf(this.configuration.getScreenshotType()));
            }
        }
    }

    @Override
    public ScreenshotType getScreenshotType() {
        return screenshotType;
    }

    @Override
    public Screenshooter setMessage(String message) {
        this.message = message;
        return this;
    }
}