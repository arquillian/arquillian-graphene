var doc = selenium.browserbot.getCurrentWindow().document;
var elm = doc.getElementsByTagName("ActivityEventRecorder")[0];
var evt = doc.createEvent("Events");
evt.initEvent("ActivityEventRecorderOpenFirebugEvent", true, false);
elm.dispatchEvent(evt);