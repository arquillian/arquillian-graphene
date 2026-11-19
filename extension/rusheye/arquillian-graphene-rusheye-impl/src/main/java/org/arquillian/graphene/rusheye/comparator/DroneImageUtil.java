package org.arquillian.graphene.rusheye.comparator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

final class DroneImageUtil {
	
	private static WebDriver driver = GrapheneContext.getContextFor(Default.class).getWebDriver();
	private final static AlphaComposite COMPOSITE = AlphaComposite.getInstance(AlphaComposite.CLEAR);
	private final static Color TRANSPARENT = new Color(0, 0, 0, 0); 
	
	public static BufferedImage getPageSnapshot(){
		File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage page = null;
		try {
			page = ImageIO.read(screen);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get page snapshot", e);
		}
		return page;
	}
	
	public static BufferedImage getElementSnapshot(WebElement element){
		Point p = element.getLocation();
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();
		return getPageSnapshot().getSubimage(p.getX(), p.getY(), width, height);
	}
	
	public static BufferedImage maskElement(WebElement element){
		return maskArea(getPageSnapshot(), element);
	}
	
	public static BufferedImage maskElement(BufferedImage img, WebElement element){
		return maskArea(img, element);
	}
	
	public static BufferedImage maskElements(BufferedImage img, List<WebElement> elements){
		for(WebElement element: elements){
			img = maskArea(img, element);
		}
		return img;
	}
	private static BufferedImage maskArea(BufferedImage img, WebElement element){
		  Graphics2D g2d = (Graphics2D) img.getGraphics();
		  g2d.setComposite(COMPOSITE);
		  g2d.setColor(TRANSPARENT);
		  
		  Point p = element.getLocation();
		  int width = element.getSize().getWidth();
		  int height = element.getSize().getHeight();
		  g2d.fillRect(p.getX(), p.getY(), width, height);
		  
		  return img;
	}
}
