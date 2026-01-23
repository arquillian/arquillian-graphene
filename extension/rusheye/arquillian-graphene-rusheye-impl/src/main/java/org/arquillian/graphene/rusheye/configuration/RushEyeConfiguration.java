package org.arquillian.graphene.rusheye.configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.arquillian.graphene.rusheye.exception.RushEyeConfigurationException;

public class RushEyeConfiguration extends Configuration{
	
	//private String basePath = "src/main/resources";
	private String snapshotPath = "/snapshot";
	//private String maskPath =  "/mask";
	private String resultPath =  "/result";
	private final float onePixelTrehold = 0f;
	private final float globalTreshold = 0f;
	private final int similarityCutOff = 100;
	private final boolean createSnapshot = true;  //if not exists
    private final boolean updateSnapshot = false; //if the result is false
	
	/*public Path getDefaultPath(){
		return Paths.get(getProperty("basePath", this.basePath));
	}
	
	public void setDefaultPath(String path){
		setProperty("basePath", path);
	}*/
	
	public Path getPatternDefaultPath(){
		return Paths.get(getProperty("snapshotPath", this.snapshotPath));
	}
	
	public void setPatternDefaultPath(String path){
		setProperty("snapshotPath", path);
	}
	
	/*public Path getMaskDefaultPath(){
		return this.getDefaultPath().resolve(getProperty("maskPath", this.maskPath));
	}	
	
	public void setMaskDefaultPath(String path){
		setProperty("maskPath", path);
	}*/	
		
	public Path getResultDefaultPath(){
		return Paths.get(getProperty("resultPath", this.resultPath));
	}

	public void setResultDefaultPath(String path){
		setProperty("resultPath", path);
	}
	
	public int getSimilarityCutOffPercentage(){
		return Integer.parseInt(getProperty("similarityCutOff", Integer.toString(this.similarityCutOff)));
	}
	
	public void setSimilarityCutOffPercentage(int similarityCutOff){
		setProperty("similarityCutOff", Integer.toString(similarityCutOff));
	}
	
	public float getPerceptionOnePixelTreshold(){
		return Float.parseFloat(getProperty("perceptionOnePixelTreshold", Float.toString(this.onePixelTrehold)));
	}
	
	public void setPerceptionOnePixelTreshold(float treshold){
		setProperty("perceptionOnePixelTreshold", Float.toString(treshold));
	}
	
	public float getPerceptionGlobalDifferenceTreshold(){
		return Float.parseFloat(getProperty("perceptionGlobalDifferenceTreshold", Float.toString(this.globalTreshold)));
	}
	
	public void setPerceptionGlobalDifferenceTreshold(float treshold){
		setProperty("perceptionGlobalDifferenceTreshold", Float.toString(treshold));
	}
	
	public boolean getIfPatternCanBeSaved(){
		return Boolean.valueOf(getProperty("createSnapshot", Boolean.toString(this.createSnapshot)));
	}
	
	public void setIfPatternCanBeSaved(boolean createBaseline){
		setProperty("createSnapshot", Boolean.toString(createBaseline));
	}

	public boolean getIfPatternCanBeUpdated(){
		return Boolean.valueOf(getProperty("updateSnapshot", Boolean.toString(this.updateSnapshot)));
	}
	
	public void setIfPatternCanBeUpdated(boolean updateBaseline){
		setProperty("updateSnapshot", Boolean.toString(updateBaseline));
	}	
	
	@Override
	public void validate() throws RushEyeConfigurationException {
	    
	    if(!(this.getSimilarityCutOffPercentage()>=0 && this.getSimilarityCutOffPercentage()<=100))
	        throw new IllegalArgumentException("similarityCutOff should be between 0 and 100");
	       
		if( !(Files.exists(this.getPatternDefaultPath())) &&
		    Files.exists(this.getResultDefaultPath()) )
			throw new RushEyeConfigurationException("RushEye pattern/mask/result path not found");		
	}

}
