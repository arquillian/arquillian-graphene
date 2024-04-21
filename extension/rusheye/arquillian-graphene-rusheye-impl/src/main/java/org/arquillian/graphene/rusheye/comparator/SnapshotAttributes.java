package org.arquillian.graphene.rusheye.comparator;

import java.util.HashSet;
import java.util.Set;

import org.arquillian.graphene.rusheye.configuration.RushEyeConfigExporter;
import org.arquillian.graphene.rusheye.configuration.RushEyeConfiguration;
import org.arquillian.rusheye.suite.Mask;
import org.arquillian.rusheye.suite.Perception;

public class SnapshotAttributes {
    
    private final String originalParameter;
    private String fileName;
    private int similarityCutOff;
    private Set<Mask> masks;
    private Perception perception;
    private final RushEyeConfiguration rusheyeConfiguration;
    
    public SnapshotAttributes(String fileName, float onePixelThresold, int similarityCutOff, String[] masks) {
        this.rusheyeConfiguration = RushEyeConfigExporter.get();
        this.fileName = fileName;
        this.originalParameter = fileName;
        this.setSimilarityCutOff(similarityCutOff);
        this.setMasks(masks); 
        this.setPerception(onePixelThresold);
    }
    
    public Perception getPerception() {
        return perception;
    }
    
    public int getSimilarityCutOff() {
        return similarityCutOff;
    }
       
    public Set<Mask> getMasks() {
        return masks;
    }
    
    public void replaceParameter(String param, String value) {
        this.fileName = this.originalParameter.replaceAll("\\#\\{" + param + "}", value);
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String name) {
        this.fileName = name;
    }
    
    private void setPerception(float onePixelThresold) {
        this.perception = new Perception();
        if (onePixelThresold == -1f)
            onePixelThresold = this.rusheyeConfiguration.getPerceptionOnePixelTreshold();
        this.perception.setOnePixelTreshold(onePixelThresold);
        this.perception.setGlobalDifferenceTreshold(this.rusheyeConfiguration.getPerceptionGlobalDifferenceTreshold());
    }
    
    private void setMasks(String[] maskFiles) {
        this.masks = new HashSet < Mask > ();
        //TBD
        /*try {            
            for (String maskFile: maskFiles) {
                Path mask = this.rusheyeConfiguration.getMaskDefaultPath().resolve(maskFile);
                masks.add(ImageUtils.readMaskImage(mask.toFile()));
            }
        } catch (Exception e) {
            throw new NoSuchMaskException(maskFiles.toString(), e);
        }*/
    }
    
    private void setSimilarityCutOff(int similarityCutOff){
        if(similarityCutOff==-1)
            this.similarityCutOff = this.rusheyeConfiguration.getSimilarityCutOffPercentage();
        else 
            this.similarityCutOff=similarityCutOff;
    }
}
