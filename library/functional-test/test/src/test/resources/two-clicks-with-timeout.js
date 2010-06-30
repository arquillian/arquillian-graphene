function doTwoClicksWithtimeout(part) {
	if (part == 0) {
		setTimeout(function() {
			doTwoClicksWithtimeout(1);
		}, 5000);
		selenium.doClick('{0}');
	} else if (part == 1) {
		selenium.doClick('{1}');
	}
}

doTwoClicksWithtimeout(0);