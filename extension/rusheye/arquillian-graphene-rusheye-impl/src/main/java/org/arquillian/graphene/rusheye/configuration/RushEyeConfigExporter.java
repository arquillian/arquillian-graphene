package org.arquillian.graphene.rusheye.configuration;

public class RushEyeConfigExporter {
	private static RushEyeConfiguration rusheyeConfig;
	
	public static RushEyeConfiguration get(){
		return rusheyeConfig;
	}
	
	public static void set(RushEyeConfiguration rc){
		rusheyeConfig=rc;
	}
}
