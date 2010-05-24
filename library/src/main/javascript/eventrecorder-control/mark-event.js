var doc = selenium.browserbot.getCurrentWindow().document;
var elm = doc.getElementsByTagName("ActivityEventRecorder")[0];
var evt = doc.createEvent("Events");
elm.setAttribute("mark", "{0}");
evt.initEvent("ActivityEventRecorderMarkEvent", true, false);
elm.dispatchEvent(evt);