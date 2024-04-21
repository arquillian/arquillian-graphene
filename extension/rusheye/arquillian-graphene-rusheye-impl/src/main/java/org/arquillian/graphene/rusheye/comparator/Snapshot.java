package org.arquillian.graphene.rusheye.comparator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;

import org.arquillian.graphene.rusheye.configuration.RushEyeConfigExporter;
import org.arquillian.graphene.rusheye.configuration.RushEyeConfiguration;
import org.arquillian.graphene.rusheye.exception.NoSuchPatternException;
import org.arquillian.rusheye.oneoff.ImageUtils;
import org.openqa.selenium.WebElement;

class Snapshot {

    private final RushEyeConfiguration rusheyeConfiguration;
    private final SnapshotAttributes snapshotAttributes;
    private BufferedImage pattern;

    public Snapshot(SnapshotAttributes snapshotAttributes) {
        this.rusheyeConfiguration = RushEyeConfigExporter.get();
        this.snapshotAttributes = snapshotAttributes;
    }

    public BufferedImage getPattern(List<WebElement> excludedElements) {
        this.setPattern(null, excludedElements);
        return this.pattern;
    }

    public BufferedImage getPattern(WebElement element, List<WebElement> excludedElements) {
        this.setPattern(element, excludedElements);
        return this.pattern;
    }

    public SnapshotAttributes getAttributes() {
        return this.snapshotAttributes;
    }

    private void setPattern(WebElement element, List<WebElement> excludedElements) {
        Path patternPath = null;
        try {
            patternPath = this.rusheyeConfiguration.getPatternDefaultPath().resolve(this.snapshotAttributes.getFileName());
            File patternImg = patternPath.toFile();
            if (patternImg.exists())
                this.pattern = ImageIO.read(patternImg);
            else {
                if (null != element)
                    this.pattern = DroneImageUtil.getElementSnapshot(element);
                else
                    this.pattern = DroneImageUtil.getPageSnapshot();
                if (this.rusheyeConfiguration.getIfPatternCanBeSaved())
                    ImageUtils.writeImage(this.pattern, patternImg.getParentFile(), patternImg.getName());
            }
            this.pattern = DroneImageUtil.maskElements(this.pattern, excludedElements);
        } catch (IOException e) {
            throw new NoSuchPatternException("Unable to locate pattern @ " + patternPath, e);
        }
    }
}