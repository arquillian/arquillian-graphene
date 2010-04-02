Selenium.prototype.findTableRow = function(locator, searchText, searchCol){
	var table = this.browserbot.findElement(locator);
	
	var rowObj = table.rows;
	var minCol = 0;
	
	for (rowNo=0; rowNo<rowObj.length; rowNo++) {
        // No need to read too short rows
        var maxCol = rowObj[rowNo].cells.length;
        
        if (maxCol <= searchCol || maxCol <= minCol) 
        	continue;
        
        searchHtml = rowObj[rowNo].cells[searchCol].innerHTML;
        searchHtml = searchHtml.replace(/(<([^>]+)>)/ig,"");
        
        //alert(searchHtml);
        //if (searchHtml.replace(/\n/g, " ").match(/<TD.*>.*<\/TD>/i)) 
        //	continue;
                
        // Build the pattern to check search column
        var searchPat = new RegExp(".*"+searchText+".*", "m");   
        
        // No more processing for unmatched rows
        if (!searchHtml.match(searchPat)) 
        	continue; 
        
        // Got the row! Let's simply return it
        return rowNo;
    }
	return -1;
}

Selenium.prototype.countTableRows = function(locator){
	var table = this.browserbot.findElement(locator);
	
	return table.rows.length;
}

Selenium.prototype.fillInEditor = function(locator, content){
	var iframe = this.browserbot.findElement(locator);
	iframe.contentDocument.body.innerHTML= content;
}

Selenium.prototype.assertTextOrder = function(textValues) {

    var allText = this.page().bodyText();
    var expectedTextValues = textValues.split(";");

    for (var i = 0; i < expectedTextValues.length - 1; i++) {
                var index = allText.indexOf(expectedTextValues[i]);
                var nextIndex = allText.indexOf(expectedTextValues[i+1]);
                if (index == -1) {
                	return false;
                	//assert.fail("'" + expectedTextValues[i] + "' not found.");
        }
                if (nextIndex == -1) {
                	return false;
                //assert.fail("'" + expectedTextValues[i+1] + "' not found.");
        }

        if (index > nextIndex) {
        	return false;
                //assert.fail("'" + expectedTextValues[i+1] + "' found before '" + expectedTextValues[i] + "'");
        }
    }
    return true;
}

/********************************************************
 * Copyright (C) 2002-2003, CodeHouse.com. All rights reserved.
 * CodeHouse(TM) is a registered trademark.
 *
 * THIS SOURCE CODE MAY BE USED FREELY PROVIDED THAT
 * IT IS NOT MODIFIED OR DISTRIBUTED, AND IT IS USED
 * ON A PUBLICLY ACCESSIBLE INTERNET WEB SITE.
 * 
 * CodeHouse.com JavaScript Library Module: Get Current Style Method
 *
 * You can obtain this script at http://www.codehouse.com
 ********************************************************
 *
 * getStyle(String locator, String property)
 * 
 * Get current style property of element given by locator.
 * 
 * This methods of getting current style value haven't absolute browser compatibility.
 * 
 * Use CSS style notation instead of JavaScripts camel notation!
 * 
 * E.g.: use property "background-color" instead of "backgroundColor"
 */
Selenium.prototype.getStyle = function(locator, property) {
	var elem = this.browserbot.findElementOrNull(locator);
	
	if (elem == null) {
			throw new SeleniumError("null property value");
	}
	
	var currentDocument = this.browserbot.getCurrentWindow().document;
	
	if( elem.currentStyle ) {  
		var ar = property.match(/\w[^-]*/g);
		var s = ar[0];
		      
		for(var i = 1; i < ar.length; ++i) {
			s += ar[i].replace(/\w/, ar[i].charAt(0).toUpperCase());
		}
		
		var result = elem.currentStyle[s];
		if (result == undefined) {
			throw new SeleniumError("null property value");
		}
		return result;
	} else if( currentDocument.defaultView.getComputedStyle ) {
		var computedStyle = currentDocument.defaultView.getComputedStyle(elem, null);
		
		if (computedStyle.getPropertyCSSValue(property) == null) {
			throw new SeleniumError("null property value");
		}
		
		return computedStyle.getPropertyValue(property);
	}
	
	throw new SeleniumError("browser incompability");
}

/**
 * Aligns screen to top (resp. bottom) of element given by locator.
 * 
 * TODO should be reimplemented to use of JQuery.scrollTo
 * 
 * @param locator of element which should be screen aligned to
 * @param alignToTop should be top border of screen aligned to top border of element
 */
Selenium.prototype.scrollIntoView = function(locator, alignToTop) {
	var elem = this.browserbot.findElementOrNull(locator);
	
	if (elem == null) {
		throw new SeleniumError("null property value");
	}
	
	if (elem.scrollIntoView == undefined) {
		throw new SeleniumError("scrollIntoView isn't supported at this element");
	}
	
	elem.scrollIntoView(alignToTop);
}
 
/**
 * Simulates a user hovering a mouse over the specified element at specific
 * coordinates relative to element.
 * 
 * @param locator
 *            element's locator
 * @param coordString
 *            specifies the x,y position (i.e. - 10,20) of the mouse event
 *            relative to the element returned by the locator.
 */
Selenium.prototype.doMouseOverAt = function(locator, coordString) {
	var element = this.browserbot.findElement(locator);
	var clientXY = getClientXY(element, coordString)

	this.browserbot.triggerMouseEvent(element, 'mouseover', true, clientXY[0],
			clientXY[1]);
}

/**
 * Returns the count of elements for given jQuery selector
 * 
 * @param selector
 *            jQuery selector
 * @return count of elements matching given selector
 */
Selenium.prototype.getJQueryCount = function(selector) {
	var inDocument = this.browserbot.getDocument();
	var found = $(inDocument).find(selector);
	return found.length;
}

/**
 * Gets the text of an element. This works for any element that contains
 * text. This command uses either the textContent (Mozilla-like browsers) or
 * the innerText (IE-like browsers) of the element, which is the rendered
 * text shown to the user.
 * 
 * If no element with given locator is found, returns null.
 *
 * @param locator an element locator
 * @return string the text of the element or null if element's wasn't found
 */
Selenium.prototype.getTextOrNull = function(locator) {
	var element = this.browserbot.findElementOrNull(locator);
	if (element == null) {
		throw new SeleniumError("element is not found");
	}
	return getText(element).trim();
};

/**
 * Add required script to page.
 * 
 * Use a id of script (ideally based on hash of script) to avoid adding the same script twice.
 * 
 * (This code should refuse adding script to the page duplicitly due to usage
 * of hash-id, so you can add it thought you are not sure script is already
 * added or not - this is usefull for adding libraries directly to the
 * page).
 * 
 * Script will be appended to end of the body tag within the script
 * tag.
 * 
 * @param script what should be added to the page
 */
Selenium.prototype.addScriptLocally = function(id, script) {
	var inDocument = this.browserbot.getCurrentWindow().document;
	if (inDocument.getElementById(id)) {
		return;
	}
	var scriptTag = inDocument.createElement('script');
	scriptTag.id = id;
	scriptTag.innerHTML = script;
	inDocument.body.appendChild(scriptTag);
};
 