package org.arquillian.graphene.rusheye.comparator;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;

public interface Ocular {

	Ocular element(WebElement element);
	Ocular replaceParameter(String param, String value);
	Ocular useSnapshot(String snapshot);
	Ocular exclude(WebElement element);
	Ocular exclude(List<WebElement> elements);
	Ocular sleep(long time, TimeUnit timeUnit);
	OcularResult compare();
	
}
