package org.arquillian.graphene.rusheye.comparator;

import org.arquillian.rusheye.suite.ComparisonResult;

public class OcularResult extends ComparisonResult{
    
    private final int similarityCutOff;
    
    public OcularResult(ComparisonResult result, int similarityCutOff) {

        super.setArea(result.getArea());
        super.setEqualsImages(result.isEqualsImages());
        super.setDiffImage(result.getDiffImage());
        super.setTotalPixels(result.getTotalPixels());
        super.setMaskedPixels(result.getMaskedPixels());
        super.setPerceptibleDiffs(result.getPerceptibleDiffs());
        super.setDifferentPixels(result.getDifferentPixels());
        super.setSmallDifferences(result.getSmallDifferences());
        super.setEqualPixels(result.getEqualPixels());
        
        this.similarityCutOff = similarityCutOff;
        
    }
     
    @Override
    public boolean isEqualsImages(){
        
        if(super.isEqualsImages())
            return true;
        else if(getPerceptibleDiffs()==0)
            return true;
        else if(getSimilarity() >= this.similarityCutOff)
            return true;
        return false;
    }
    
    public int getSimilarity(){
        return getEqualPixels()*100 / getTotalPixels();
    }
    
    @Override
    public String toString() {
        return "OcularResult [equalsImages=" + isEqualsImages() + 
                ", totalPixels=" + getTotalPixels() + 
                ", maskedPixels=" + getMaskedPixels() + 
                ", perceptibleDiffs=" + getPerceptibleDiffs() +
                ", differentPixels=" + getDifferentPixels() +
                ", smallDifferences=" + getSmallDifferences() + 
                ", equalPixels=" + getEqualPixels() + 
                ", similarity=" + getSimilarity() + "]";
    }

}
