var doc = selenium.browserbot.getCurrentWindow().document;
var elm = doc.getElementsByTagName("ActivityEventRecorder")[0];
var data = elm.getAttribute("data");
elm.setAttribute("data", "");
data;