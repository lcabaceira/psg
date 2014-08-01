/**
 * Inserts the code for Drag and Drop Applet.
 * @param {Object} parentId
 * @param {Object} cookiesData
 * @param {Object} baseUrl
 * @param {Object} messageHTML
 * @param {Object} propFile
 * @param {boolean} multifile
 * @param {boolean} useExtJs
 * @param {Map} messages - localized messages, all starting with the prefix 'upload.Messages'
 * @See com.wewebu.ow.client.upload.
 */
function insertDndApplet(parentId, cookiesData, baseUrl, messageHTML, propFile,
		 multifile, useExtJs, messages) {
	var parentElement = document.getElementById(parentId);
	var innerHtmlObject = '';
	var appletParams = '';
	var _info = navigator.userAgent;
	var ie = (_info.indexOf("MSIE") > 0);
	if (parentElement) {
		// Rendering for Internet Explorer

		innerHtmlObject += '<object type="application/x-java-applet"';
		if (ie) {
			// CLASS ID
			//Use latest java plugin
			innerHtmlObject += ' classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"';
			//Use version 1.5.0 of the java plugin
			//innerHtmlObject += ' classid="clsid:CAFEEFAC-0015-0000-0000-ABCDEFFEDCBA"';
			innerHtmlObject += ' codebase="http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab#version=1,5" ';
		}

		innerHtmlObject += ' width= "26" height= "26" name="dndupload" id="dndupload">';

		appletParams += '<param name="java_archive" value="applets/dndupload/ow_dndupload.jar"/>';
		appletParams += '<param name="java_code" value="com.wewebu.ow.client.upload.OwDNDApplet"/>';
		appletParams += '<param name="cache_archive" value="applets/dndupload/commons-logging-1.1.1.jar,applets/dndupload/gson-1.7.1.jar,applets/dndupload/httpclient-4.1.1.jar,applets/dndupload/httpcore-4.1.jar,applets/dndupload/httpmime-4.1.1.jar"/>';
		appletParams += '<param name="cache_archive_ex" value="applets/dndupload/ow_dndupload.jar;preload;4.2.0.0"/>';

		appletParams += '<param name="mayscript" value="yes"/>';

		cookiesParam = '<param name="cookie_data" value="' + cookiesData
				+ '"/>';
		appletParams += cookiesParam;

		urlParam = '<param name="upload_url" value="' + baseUrl
				+ '/dragdropcontent"/>';
		appletParams += urlParam;

		appletParams += "<param name=\"message\" value=\"" + messageHTML
				+ "\"/>";

		if (multifile) {
			appletParams += '<param name="multifile" value="yes"/>';
		}
		
		if(useExtJs) {
			appletParams += '<param name="use_extjs" value="yes"/>';
		} else {
			appletParams += '<param name="use_extjs" value="no"/>';
		}

		propFileParam = '<param name="props_file" value="' + baseUrl
				+ '/applets/dndupload/' + propFile + '"/>';
		appletParams += propFileParam;
		appletParams += '<param name="initial_focus" value="false"/>';
		
		//Localization messages to be used inside the applet.
		if (messages) {
			for ( var key in messages) {
				if(typeof messages[key] == 'string') {
					appletParams += '<param name="' + key + '" value="'
							+ messages[key] + '"/>';
				}
			}
		}
		
		innerHtmlObject += appletParams;
		//last line
		innerHtmlObject += '</object>';

		//alert(innerHtmlObject);
		parentElement.innerHTML = innerHtmlObject;
		
	}
}

//==============================================================================================================================================================
//=================================================================== Add some helper functions ================================================================
//==============================================================================================================================================================
/**
 * Stack all JS calls to the Applet into this array, 
 * until it (the applet) has finished loading.
 */
var DNDAppletCallQueue = new Array();
var DNDAppletReady = false;

/**
 * To be used whenever you want to perform a DIRECT call to one of the applet's methods from JS code.
 * It makes sure to defer the call until the applet has finished loading.
 * 
 * JS calls to the applet, that are the result of a round-trip from Java -> JS -> Java, do no need 
 * to be deferred.
 *  
 * @param aFunction
 */
function queueDNDAppletCall(aFunction) {
	extJsLog.debug('Pushing dnd applet call ' + aFunction.toString());
	DNDAppletCallQueue.push(aFunction);
	executeQueuedDNDAppletCalls();
};

function executeQueuedDNDAppletCalls() {
	if(!DNDAppletReady) {
		return;
	}
	var deferedCall = DNDAppletCallQueue.shift();
	while(null != deferedCall) {
		extJsLog.debug('Executing deffered call ' + deferedCall.toString());
		deferedCall.call();
		deferedCall = DNDAppletCallQueue.shift();
	}
};

/**
 * Called by the applet as the last step in its own init() method.
 */
function onDnDAppletInitialized() {
	extJsLog.debug('onDnDAppletInitialized called ...');
	this.DNDAppletReady = true;
	this.executeQueuedDNDAppletCalls();
};

DnDAppletEnableDrop = function(enabledValue) {
	this.queueDNDAppletCall(function() {
		document.dndupload.setEnabled(enabledValue);
	});
};