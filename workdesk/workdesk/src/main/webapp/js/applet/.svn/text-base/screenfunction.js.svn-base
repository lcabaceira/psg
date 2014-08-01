function pageWidth() {
	return window.innerWidth != null ? window.innerWidth
			: document.documentElement && document.documentElement.clientWidth ? document.documentElement.clientWidth
					: document.body != null ? document.body.clientWidth : null;
}
function pageHeight() {
	return window.innerHeight != null ? window.innerHeight
			: document.documentElement && document.documentElement.clientHeight ? document.documentElement.clientHeight
					: document.body != null ? document.body.clientHeight : null;
}
function posLeft() {
	return typeof window.pageXOffset != 'undefined' ? window.pageXOffset
			: document.documentElement && document.documentElement.scrollLeft ? document.documentElement.scrollLeft
					: document.body.scrollLeft ? document.body.scrollLeft : 0;
}
function posTop() {
	return typeof window.pageYOffset != 'undefined' ? window.pageYOffset
			: document.documentElement && document.documentElement.scrollTop ? document.documentElement.scrollTop
					: document.body.scrollTop ? document.body.scrollTop : 0;
}
function posRight() {
	return posLeft() + pageWidth();
}
function posBottom() {
	return posTop() + pageHeight();
}
function getMouseXY(e) {
	if (event.clientX) { // Grab the x-y pos.s if browser is IE.
		CurrentLeft = event.clientX + document.body.scrollLeft;
		CurrentTop = event.clientY + document.body.scrollTop;
	} else { // Grab the x-y pos.s if browser isn't IE.
		CurrentLeft = e.pageX;
		CurrentTop = e.pageY;
	}
	if (CurrentLeft < 0) {
		CurrentLeft = 0;
	}
	;
	if (CurrentTop < 0) {
		CurrentTop = 0;
	}
	;

	return true;
}
function showLocation() {
	var x = self.pageXOffset;
	var y = self.pageYOffset
	var currX = "X-coordinate: " + x + "\n";
	var currY = "Y-coordinate: " + y;
	//window.alert(currX + currY);
}
//dual monitor is enabled 
var m_dualMonitorSupport = false;
//default fullscreen disabled
var m_fullScreenMode = false;
//id from Alfresco Workdesk
var strID = "0";
//monitors are reversed
var monitorReverse = true;
/**
 * dual monitors
 */
function setDualMonitor() {

	var de = document.DesktopSettings.getDisplayDevices();

	if (de > 1) {
		document.getElementById('Dual').checked = false;
		m_fullScreenMode = false;
		m_dualMonitorSupport = true;

	} else {
		document.getElementById('Dual').disabled = true;
		document.getElementById('Dual').checked = false;
		m_fullScreenMode = false;
		m_dualMonitorSupport = false;
	}

}
/**
 * full screen mode changed 
 */
function update() {
	if (document.getElementById('Dual').checked) {
		m_fullScreenMode = true;
		//alert("checked::" + m_fullScreenMode);
	} else {
		m_fullScreenMode = false;
	}
	updatePositions();

}
function init() {

	var de = document.DesktopSettings.getDisplayDevices();
	m_fullScreenMode = false;
	m_dualMonitorSupport = false;
	updatePositions();
}

function openNewWindow(startX, startY, width, height) {
	var params = ',width=' + width + ',height=' + height
			+ ',toolbar=1,resizable=0';
	window.open("Html1.html", 'mywin', 'left=' + startX + ',top=' + startY
			+ params);

}
function resizeWindow(startX, startY, width, height) {
	window.moveTo(startX, startY);
	window.resizeTo(width, height);

}

function updatePositions() {

	var vBounds = document.DesktopSettings.getVirtualBounds();
	var origin = vBounds.getLocation();

	var xOrigin = 0;
	var yOrigin = 0;

	//starts from negative
	xOrigin = origin.x;
	yOrigin = origin.y;

	var dim = document.DesktopSettings.getDesktopResolutions();

	var xbr = 0;
	var ybr = 0;

	var _app = navigator.appName;
	if ((_app == 'Netscape')) {
		//works for Firefox
		xbr = window.screenX;
		ybr = window.screenY;
	} else {
		//works for Chrome and IE
		xbr = window.screenLeft;
		ybr = window.screenTop;

	}
	var padding = 5;
	xbr = xbr + 5;
	var y = 0;
	var monitorReverse;
	if (xOrigin < 0)
		monitorReverse = true;
	//open window position 
	var wStartX = 0;
	var wStartY = 0;
	var wWidth = 0;
	var wHeight = 0;
	var startpreviewX = 0;
	var startmainX = 0;
	//main window position 
	var mainWidth = 0;
	var mainHeigth = 0;

	if (monitorReverse) { //virtual size starts from -
		if (xbr < 0) { //open in positive area 
			if (m_fullScreenMode) {
				wStartX = 0;
				startmainX = xOrigin;
				wStartY = 0;
				wWidth = dim[0].width;
				wHeight = dim[0].height;
				mainWidth = dim[1].width;
				mainHeigth = dim[1].height;
			} else {//same screen
				startmainX = xOrigin;
				wStartX = xOrigin;
				wStartY = yOrigin;
				wWidth = dim[1].width;
				wHeight = dim[1].height;
				mainWidth = dim[0].width;
				mainHeigth = dim[0].height;
				startpreviewX = wStartX / 2;
			}
		} else { //opens in negative area
			if (m_fullScreenMode) {
				startmainX = 0;
				wStartX = xOrigin;
				wStartY = yOrigin;
				wWidth = dim[1].width;
				wHeight = dim[1].height;
				mainWidth = dim[0].width;
				mainHeigth = dim[0].height;
			} else {

				startmainX = 0;
				wStartX = 0;
				wStartY = 0;
				wWidth = dim[0].width;
				wHeight = dim[0].height;
				startpreviewX = dim[0].width / 2;
				mainWidth = dim[0].width;
				mainHeigth = dim[0].height;
			}
		}
	} else {
		// start with 0
		var dime = dim[0].width;
		if (xbr < dime) {
			if (m_fullScreenMode) {
				// open monitor 2
				wStartX = dim[0].width + 1;
				startmainX = 0;
				wStartY = 0;
				wWidth = dim[1].width;
				wHeight = dim[1].height;
				mainWidth = dim[0].width;
				mainHeigth = dim[0].height;
				startpreviewX = dim[0].width + 1;
			} else {
				wStartX = 0;
				startmainX = wStartX;
				wStartY = 0;
				wWidth = dim[0].width;
				wHeight = dim[0].height;
				startpreviewX = dim[0].width / 2;
				mainWidth = dim[0].width;
				mainHeigth = dim[0].height;
			}
		} else {//open in monitor 1
			if (m_fullScreenMode) {
				wStartX = xOrigin;
				startmainX = dim[0].width + 1;
				wStartY = yOrigin;
				wWidth = dim[0].width;
				wHeight = dim[0].height;
				mainWidth = dim[1].width;
				mainHeigth = dim[1].height;
			} else {
				wStartX = dim[0].width + 1;
				startmainX = wStartX;
				wStartY = 0;
				wWidth = dim[1].width;
				wHeight = dim[1].height;
				mainWidth = dim[0].width;
				mainHeigth = dim[0].height;
				startpreviewX = wStartX + dim[1].width / 2;
			}
		}
	}

	//set input fields
	if (m_fullScreenMode) {
		setInputFields(startmainX, yOrigin, mainWidth, mainHeigth, wStartX,
				wStartY, wWidth, wHeight);
	} else {

		setInputFields(startmainX, yOrigin, mainWidth / 2, wHeight,
				startpreviewX, wStartY, wWidth / 2, wHeight);
	}
}

function setInputFields(owWindowTopX_p, owWindowTopY_p, owWindowWidth_p,
		owWindowHeight_p, owViewerTopX_p, owViewerTopY_p, owViewerWidth_p,
		owViewerHeight_p) {

	document.getElementById(strID + 'OwWindowTopX').value = owWindowTopX_p;
	document.getElementById(strID + 'OwWindowTopY').value = owWindowTopY_p;
	document.getElementById(strID + 'OwWindowWidth').value = owWindowWidth_p;
	document.getElementById(strID + 'OwWindowHeight').value = owWindowHeight_p;

	document.getElementById(strID + 'OwViewerTopX').value = owViewerTopX_p;
	document.getElementById(strID + 'OwViewerTopY').value = owViewerTopY_p;
	document.getElementById(strID + 'OwViewerWidth').value = owViewerWidth_p;
	document.getElementById(strID + 'OwViewerHeight').value = owViewerHeight_p;

}
function setInputFieldsFullModeMon1() {
	var vBounds = document.DesktopSettings.getVirtualBounds();
	var origin = vBounds.getLocation();
	var dim = document.DesktopSettings.getDesktopResolutions();
	

	var monitorReverse;
	var startViewerX=0;
	if (origin.x < 0)
	{
		monitorReverse = -1;
		startViewerX=origin.x
	}
	else
	{
		startViewerX=dim[0].width;
	}
	
	
	setInputFields(0, 0, dim[0].width, dim[0].height,
			startViewerX, 0, dim[1].width, dim[1].height);
}
function setInputFieldsFullModeMon2() {

	var vBounds = document.DesktopSettings.getVirtualBounds();
	var origin = vBounds.getLocation();
	var dim = document.DesktopSettings.getDesktopResolutions();
	

	var monitorReverse;
	var startMainX=0;
	var startViewerX=0;
	if (origin.x < 0)
	{
		monitorReverse = -1;
		startMainX=origin.x
		startViewerX=0;
	}
	else
	{
		startMainX=dim[0].width;
	}
	

	setInputFields(startMainX, origin.y, dim[1].width, dim[1].height,
			0, origin.y, dim[0].width, dim[0].height);

}
function setInputFieldsSplitModeMon1() {

	var vBounds = document.DesktopSettings.getVirtualBounds();
	var origin = vBounds.getLocation();
	var dim = document.DesktopSettings.getDesktopResolutions();
	
	setInputFields(0, 0, dim[0].width / 2, dim[0].height,
			dim[0].width / 2, origin.y, dim[0].width / 2, dim[0].height);
}
function setInputFieldsSplitModeMon2() {

	var vBounds = document.DesktopSettings.getVirtualBounds();
	var origin = vBounds.getLocation();
	var dim = document.DesktopSettings.getDesktopResolutions();
	var monitorReverse;
	var startViewerX=0;
	var startMainX=0;
	if (origin.x < 0)
	{
		monitorReverse = -1;
		startViewerX=-(dim[1].width/2);
		startMainX=origin.x;
	}
	else
	{
		startViewerX=dim[0].width + dim[1].width / 2;
		startMainX=dim[0].width;
	}
	setInputFields(startMainX, origin.y, dim[1].width / 2, dim[1].height,
			startViewerX, origin.y, dim[1].width / 2, dim[1].height);

}

function openWindows(full, startpreviewX, wStartX, wStartY, wWidth, wHeight) {

	wStartX = document.getElementById('startx').value;
	wStartY = document.getElementById('starty').value;
	wWidth = document.getElementById('width').value;
	wHeight = document.getElementById('height').value;
	startpreviewX = document.getElementById('startxV').value;
	var full = document.getElementById('Dual').checked;
	//Full mode 
	if (full) {
		openNewWindow(startpreviewX, wStartY, wWidth, wHeight);
	} else {
		resizeWindow(wStartX, wStartY, wWidth, wHeight);
		openNewWindow(startpreviewX, wStartY, wWidth, wHeight);
	}

}
function onDispSelection(combo) {
	
	var comboValue = combo.value;
	if (comboValue == "SplitMode") {
		m_dualMonitorSupport = false;
		m_fullScreenMode = false;
		updatePositions();
	} else if (comboValue == "FullScreenMode") {
		m_fullScreenMode = true;
		m_dualMonitorSupport = true;
		updatePositions();
	} else if (comboValue == "FullModeOnMon1") {
		setInputFieldsFullModeMon1();
	} else if (comboValue == "FullModeOnMon2") {
		setInputFieldsFullModeMon2();
	} else if (comboValue == "SplitModeOnMon1") {
		setInputFieldsSplitModeMon1()
	} else if (comboValue == "SplitModeOnMon2") {
		setInputFieldsSplitModeMon2()
	}

}
function onModeSelection(combo) {
	var myindex  = combo.selectedIndex;
	if ( myindex  == 1) {
		m_dualMonitorSupport = false;
		m_fullScreenMode = false;
		updatePositions();
	} else if (myindex == 2) {
		m_fullScreenMode = true;
		m_dualMonitorSupport = true;
		updatePositions();
	}
}
function onDispSelectionChecked() {
	if (getDisplayMode() == "automatic") {
		document.getElementById('Dual').disabled = false;
		updatePositions();
	} else {
		document.getElementById('Dual').disabled = true;
	}
}
function getDisplayMode() {
	return value = document.getElementById("displayMode").value;
}
