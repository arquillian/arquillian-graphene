var Ajocado = Ajocado || {};

/**
 * Syntactic shortcut for accessing Ajocado.Page on the page.
 * 
 * @return the Ajocado.Page object defined in the current page.
 */
Ajocado.getPage = function() {
	if (selenium.browserbot.getCurrentWindow().Ajocado == undefined) {
		return undefined;
	}
	return selenium.browserbot.getCurrentWindow().Ajocado.Page;
}