(function(resourceName) {
	var scripts = document.getElementsByTagName('script');
	for ( var i = 0; i < scripts.length; i++) {
		var script = scripts[i];
		var rn = script.getAttribute('resourceName');
		if (rn === resourceName) {
			return script;
		}
	}
	return null;
})('{0}')
