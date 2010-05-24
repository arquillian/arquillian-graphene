var doc = selenium.browserbot.getCurrentWindow().document;
var elm = doc.getElementsByTagName("ActivityEventRecorder")[0];
var evt = doc.createEvent("Events");
elm.setAttribute("data", "");
evt.initEvent("ActivityEventRecorderFlushEvent", true, false);
elm.dispatchEvent(evt);