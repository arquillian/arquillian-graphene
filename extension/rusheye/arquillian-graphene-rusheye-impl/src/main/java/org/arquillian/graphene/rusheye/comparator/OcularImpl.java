package org.arquillian.graphene.rusheye.comparator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.arquillian.graphene.rusheye.configuration.RushEyeConfigExporter;
import org.arquillian.graphene.rusheye.configuration.RushEyeConfiguration;
import org.arquillian.graphene.rusheye.exception.RushEyeVisualComparisonException;
import org.arquillian.rusheye.comparison.ImageComparator;
import org.arquillian.rusheye.core.DefaultImageComparator;
import org.arquillian.rusheye.oneoff.ImageUtils;
import org.arquillian.rusheye.suite.ComparisonResult;
import org.openqa.selenium.WebElement;

public class OcularImpl implements Ocular {

    private final boolean isPageFragment;
    private final ImageComparator comparator;
    private final Snapshot snapshot;
    private final RushEyeConfiguration rusheyeConfiguration;
    private WebElement element;
    private final List<WebElement> excludedElements;

    public OcularImpl(boolean isPageFragment, SnapshotAttributes snapshotAttributes) {
        this.isPageFragment = isPageFragment;
        this.rusheyeConfiguration = RushEyeConfigExporter.get();
        this.comparator = new DefaultImageComparator();
        this.excludedElements = new LinkedList<WebElement>();
        this.snapshot = new Snapshot(snapshotAttributes);
    }

    public Ocular element(WebElement element) {
        this.element = element;
        return this;
    }

    public Ocular exclude(WebElement element) {
        this.excludedElements.add(element);
        return this;
    }

    public Ocular exclude(List<WebElement> elements) {
        this.excludedElements.addAll(elements);
        return this;
    }

    public OcularResult compare() {
        ComparisonResult result = comparator.compare(this.getPattern(), 
                                                     this.getSample(),
                                                     this.snapshot.getAttributes().getPerception(), 
                                                     this.snapshot.getAttributes().getMasks());
        saveDiffImage(result);
        return new OcularResult(result, this.snapshot.getAttributes().getSimilarityCutOff());
    }

    public Ocular useSnapshot(String snapshot) {
        this.snapshot.getAttributes().setFileName(snapshot);
        return this;
    }

    public Ocular replaceParameter(String param, String value) {
        this.snapshot.getAttributes().replaceParameter(param, value);
        return this;
    }

    public Ocular sleep(long time, TimeUnit timeUnit) {
        try {
            Thread.sleep(timeUnit.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    private BufferedImage getPattern() {
        if (this.isPageFragment)
            return this.snapshot.getPattern(this.element, this.excludedElements);
        else
            return this.snapshot.getPattern(this.excludedElements);
    }

    private BufferedImage getSample() {
        BufferedImage sample;
        if (this.isPageFragment)
            sample = DroneImageUtil.getElementSnapshot(this.element);
        else
            sample = DroneImageUtil.getPageSnapshot();
        return DroneImageUtil.maskElements(sample, excludedElements);
    }

    private void saveDiffImage(ComparisonResult result) {
        try {
            File outputfile = this.rusheyeConfiguration.getResultDefaultPath()
                .resolve(this.snapshot.getAttributes().getFileName()).toFile();
            ImageUtils.writeImage(result.getDiffImage(), outputfile.getParentFile(), outputfile.getName());
        } catch (IOException e) {
            throw new RushEyeVisualComparisonException("Unable to write the difference", e);
        }
    }
}