var ZIDI = new OfficeLauncherUtility();

function OfficeLauncherUtility()
{
    this.officelauncher = null;
    this.officelauncher_debug = false;
    this.officelauncher_overwrite_rules = null;
    // @deprecated since 4.2.0.0
    this.officelauncher_fallback_applet = false;
    // @deprecated since 4.2.0.0
    this.zidilauncherapplet_debug = false;

    this.getOfficeLauncher = function()
    {
    	if (this.officelauncher == null) {
    		this.officelauncher = new OfficeLauncher();
    		this.officelauncher.setConsoleLoggingEnabled(this.officelauncher_debug);
	        if (this.officelauncher_overwrite_rules) {
	        	this.officelauncher.setRules(this.officelauncher_overwrite_rules);
	        }
    	}
        return this.officelauncher;
    };
}

OfficeLauncherUtility.prototype.openOfficeDocument = function(deploymentRoot, dmsid, documentTitle, readOnly, requiredFileExt)
{
	var decDmsid = decodeURIComponent(dmsid);//Workdesk by default does an URL encoding, which leads to issues
	if (this.getOfficeLauncher().isSafari() || (!this.getOfficeLauncher().isWin() && !this.getOfficeLauncher().isMac())) {
		// detect if we are on a supported operating system
		// alert("Unsupported Operating System: This feature requires Microsoft Office on Windows or OS X.");
		Ext.Msg.show({
  		   title: "Unsupported Environment",
 		   msg: "Your current environment is not supported by Office Integration!",
 		   buttons: Ext.Msg.OK,
 		   icon: Ext.MessageBox.WARNING,
 		});
	} else {
		// detect if we can edit this file type and get associated protocol
		// handler
		var protocolHandler = this.getProtocolForFileExtension(requiredFileExt !== undefined ? requiredFileExt : this.getFileExtension(documentTitle));
		if (protocolHandler === undefined) {
			return false;
		}
		// special route for IOS
		if (this.getOfficeLauncher().isIOS()) {
			this.launchOfficeIOS(protocolHandler, this.buildOfficeUrl(deploymentRoot, decDmsid, documentTitle, readOnly, requiredFileExt));
			return;
		}
		// if we have a working PlugIn (ActiveX or NPAPI), use it. Otherwise we
		// use the protocol handler (e.g. Chrome w/o PlugIn)
		var url = this.buildOfficeUrl(deploymentRoot, decDmsid, documentTitle, readOnly, requiredFileExt);
		if (this.getOfficeLauncher().isAvailable()) {
			this.launchOfficeByPlugin(url);
		} else {
			// Launch protocol handler
			this.tryToLaunchOfficeByMsProtocolHandler(protocolHandler, url, readOnly);
		}
	}
};

OfficeLauncherUtility.prototype.launchOfficeByPlugin = function(url)
{
    var checker, dlg, ctx = this;
    var isNotIE = (this.getOfficeLauncher().isFirefox() || this.getOfficeLauncher().isChrome() || this.getOfficeLauncher().isSafari());
    if (!this.getOfficeLauncher().EditDocument(url)) {
        // check if the Plug-In has been blocked
        if (this.getOfficeLauncher().isControlNotActivated() && isNotIE) {
            checker = window.setInterval(function()
            {
                if (ctx.getOfficeLauncher().isControlActivated()) {
                    if (checker)
                    window.clearInterval(checker);

                    dlg.getDialog().close();
                    window.setTimeout(function()
                    {
                        if (!ctx.getOfficeLauncher().EditDocument(url)) {
                            if (ctx.getOfficeLauncher().getLastControlResult() !== -2) {
                                var errorDetails = ctx.getOfficeLauncher().getLastControlResult() !== false ? ' (Error code: ' + ctx.getOfficeLauncher().getLastControlResult() + ')' : '';
                                Ext.Msg.show({
                         		   title: "Plugin Launch Error",
                        		   msg: "Error: Starting Microsoft Office failed. " + errorDetails,
                        		   buttons: Ext.Msg.OK,
                        		   icon: Ext.MessageBox.WARNING,
                        		});
                            }
                        }
                    }, 50);
                }
            }, 250);
            
        	dlg = Ext.Msg.show({
        		title: "Permission Required",
        		msg: "<p>The Plug-In has been blocked.</p><p>Please allow the Plug-In to run.</p>",
        		icon: Ext.MessageBox.INFO,
        		fn: function()
       		    {
       			   window.clearInterval(checker);
       		    }
       		});

        } else {
            if (this.getOfficeLauncher().getLastControlResult() !== -2) {
                // error message only required if user did not cancel (result === -2)
                var errorDetails = this.getOfficeLauncher().getLastControlResult() !== false ? ' (Error code: ' + this.getOfficeLauncher().getLastControlResult() + ')' : '';
            	Ext.Msg.show({
           		   title: "Plugin Launch Failed",
          		   msg: "Problem: Starting Microsoft Office failed." + errorDetails,
          		   buttons: Ext.Msg.OK,
          		   icon: Ext.MessageBox.WARNING
          		});
            }
        }
    }
};

OfficeLauncherUtility.prototype.tryToLaunchOfficeByMsProtocolHandler = function(protocolHandler, url, readOnly)
{
    var protocolUrl = protocolHandler + ':ofe%7Cu%7C' + this.getOfficeLauncher().encodeUrl(url);
    var protocolHandlerPresent = false;
    var input = Ext.DomHelper.append('OwMainLayout_HEADER', {
        tag : 'input',
        id : 'tryToLaunchOfficeByMsProtocolHandler-input',
        style: "z-index: 1000; background-color: #FFF; border: none; outline: none"
    });
    input.focus();
    input.onblur = function()
    {
        protocolHandlerPresent = true;
    };
    location.href = protocolUrl;
    var scope = this;
    setTimeout(function()
    {
        input.onblur = null;
        input.remove();
        if (!protocolHandlerPresent) {
        	if (scope.officelauncher_fallback_applet) {
        		scope.openOfficeDocumentURL_Applet(url, readOnly);
        	} else {
            	Ext.Msg.show({
          		   title: "MS Office Version",
         		   msg: "This feature requires a supported version of Microsoft Office.",
         		   buttons: Ext.Msg.OK,
         		   icon: Ext.MessageBox.INFO
         		});
        	}
        }
    }, 500);
};

OfficeLauncherUtility.prototype.buildOfficeUrl = function(deploymentRoot, dmsid, documentTitle, readOnly, requiredFileExt)
{
    var url = deploymentRoot + "/AllObjects/" + this.encodeDmsid(dmsid) + "/";
    var cleanedFilename = this.cleanupDocumentTitle(documentTitle);

    if (requiredFileExt) {
        var endsWithExt = (cleanedFilename.length >= requiredFileExt.length) && (cleanedFilename.substr(cleanedFilename.length - requiredFileExt.length, requiredFileExt.length).toLowerCase() == requiredFileExt.toLowerCase());
        if (!endsWithExt) {
            cleanedFilename = cleanedFilename + requiredFileExt;
        }
    }

    var remainingLength = 250 - encodeURIComponent(url).length;
    cleanedFilename = encodeURIComponent(cleanedFilename);
    var cleanedLength = cleanedFilename.length;

    if (cleanedLength > remainingLength) {
        var dotPos = cleanedFilename.lastIndexOf(".");
        var extension = "";
        if (dotPos >= 0) {
            extension = cleanedFilename.substring(dotPos, cleanedFilename.length);
        }

        var maxLen = remainingLength - extension.length;
        cleanedFilename = cleanedFilename.substring(0, maxLen) + extension;
        if (encodeURIComponent(cleanedFilename).length > remainingLength) {
            while ((maxLen > 1) && (encodeURIComponent(cleanedFilename).length > remainingLength)) {
                maxLen--;
                cleanedFilename = cleanedFilename.substring(0, maxLen) + extension;
            }
        }
    }

    url = url + cleanedFilename;

    return url;
};

OfficeLauncherUtility.prototype.getFileExtension = function(fileName)
{
    var lastDotPos = fileName.lastIndexOf('.');
    if (lastDotPos < 0) {
        return '';
    } else {
        return fileName.substr(lastDotPos, fileName.length);
    }
};

OfficeLauncherUtility.prototype.getFilePureName = function(fileName)
{
    var lastDotPos = fileName.lastIndexOf('.');
    if (lastDotPos < 0) {
        return fileName;
    } else {
        return fileName.substr(0, lastDotPos);
    }
};

OfficeLauncherUtility.prototype.getProtocolForFileExtension = function(fileExtension)
{
    if (this.msProtocolNames) {
        return this.msProtocolNames[fileExtension];
    } else {
        return OfficeLauncherUtility.msProtocolNames[fileExtension];
    }
};

OfficeLauncherUtility.prototype.launchOfficeIOS = function(protocolHandler, url)
{
    var protocolUrl = protocolHandler + ':ofe%7Cu%7C' + this.getOfficeLauncher().encodeUrl(url);
    var iframe = Ext.DomHelper.append('OwMainLayout_HEADER', {
        tag : 'iframe',
        id : 'launchOfficeIOS-iframe',
        style: "display: none; height: 0; width: 0"
    });
    iframe.attr('src', protocolUrl);
};

OfficeLauncherUtility.msProtocolNames = {
    '.doc'  : 'ms-word',
    '.docx' : 'ms-word',
    '.docm' : 'ms-word',
    '.dot'  : 'ms-word',
    '.dotx' : 'ms-word',
    '.dotm' : 'ms-word',
    '.xls'  : 'ms-excel',
    '.xlsx' : 'ms-excel',
    '.xlsb' : 'ms-excel',
    '.xlsm' : 'ms-excel',
    '.xlt'  : 'ms-excel',
    '.xltx' : 'ms-excel',
    '.xltm' : 'ms-excel',
    '.xlsm' : 'ms-excel',
    '.ppt'  : 'ms-powerpoint',
    '.pptx' : 'ms-powerpoint',
    '.pot'  : 'ms-powerpoint',
    '.potx' : 'ms-powerpoint',
    '.potm' : 'ms-powerpoint',
    '.pptm' : 'ms-powerpoint',
    '.potm' : 'ms-powerpoint',
    '.pps'  : 'ms-powerpoint',
    '.ppsx' : 'ms-powerpoint',
    '.ppam' : 'ms-powerpoint',
    '.ppsm' : 'ms-powerpoint',
    '.sldx' : 'ms-powerpoint',
    '.sldm' : 'ms-powerpoint',
};

OfficeLauncherUtility.prototype.encodeDmsid = function(dmsid)
{
    var encodedDmsid = "";
    for (var i = 0; i < dmsid.length; i++) {
        var c = dmsid.charAt(i);
        switch (c) {
            case '#':
                encodedDmsid = encodedDmsid + "$23";
                break;
            case '_':
                encodedDmsid = encodedDmsid + "$5F";
                break;
            case '\\':
                encodedDmsid = encodedDmsid + "$5C";
                break;
            case '/':
                encodedDmsid = encodedDmsid + "$2F";
                break;
            case ':':
                encodedDmsid = encodedDmsid + "$3A";
                break;
            case ';':
                encodedDmsid = encodedDmsid + "$3B";
                break;
            case '$':
                encodedDmsid = encodedDmsid + "$24";
                break;
            case '*':
                encodedDmsid = encodedDmsid + "$2A";
                break;
            case '?':
                encodedDmsid = encodedDmsid + "$3F";
                break;
            case '"':
                encodedDmsid = encodedDmsid + "$22";
                break;
            case 39:
                encodedDmsid = encodedDmsid + "$27";
                break;
            case '<':
                encodedDmsid = encodedDmsid + "$3C";
                break;
            case '>':
                encodedDmsid = encodedDmsid + "$3E";
                break;
            case '{':
                encodedDmsid = encodedDmsid + "$7B";
                break;
            case '|':
                encodedDmsid = encodedDmsid + "$7C";
                break;
            case '}':
                encodedDmsid = encodedDmsid + "$7D";
                break;
            case '~':
                encodedDmsid = encodedDmsid + "$7E";
                break;
            default:
                encodedDmsid = encodedDmsid + c;
                break;
        }
    }
    return (encodedDmsid);
};

OfficeLauncherUtility.prototype.cleanupDocumentTitle = function(documentTitle)
{
    var cleanedDocumentTitle = "";
    for (var i = 0; i < documentTitle.length; i++) {
        var c = documentTitle.charAt(i);
        switch (c) {
            case '#':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '\\':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '/':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case ':':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case ';':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '*':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '?':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '"':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case 39:
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '<':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '>':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '{':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '|':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '}':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            case '~':
                cleanedDocumentTitle = cleanedDocumentTitle + "_";
                break;
            default:
                cleanedDocumentTitle = cleanedDocumentTitle + c;
                break;
        }
    }
    return (cleanedDocumentTitle);
};

OfficeLauncherUtility.prototype.encodeDafPath = function(path)
{
    var encodedPath = "";
    for (var i = 0; i < path.length; i++) {
        var c = path.charAt(i);
        switch (c) {
            case '\\':
                encodedPath = encodedPath + "$$5C";
                break;
            case ':':
                encodedPath = encodedPath + "$$3A";
                break;
            case '*':
                encodedPath = encodedPath + "$$2A";
                break;
            case '?':
                encodedPath = encodedPath + "$$3F";
                break;
            case '"':
                encodedPath = encodedPath + "$$22";
                break;
            case '#':
                encodedPath = encodedPath + "$$23";
                break;
            case 39:
                encodedPath = encodedPath + "$$27";
                break;
            case '<':
                encodedPath = encodedPath + "$$3C";
                break;
            case '>':
                encodedPath = encodedPath + "$$3E";
                break;
            case '{':
                encodedPath = encodedPath + "$$7B";
                break;
            case '|':
                encodedPath = encodedPath + "$$7C";
                break;
            case '}':
                encodedPath = encodedPath + "$$7D";
                break;
            case '%':
                encodedPath = encodedPath + "$$25";
                break;
            case '~':
                encodedPath = encodedPath + "$$7E";
                break;
            case ';':
                encodedPath = encodedPath + "$$3B";
                break;
            case '&':
                encodedPath = encodedPath + "$$26";
                break;
            default:
                encodedPath = encodedPath + c;
                break;
        }
    }
    return (encodedPath);
};
// @Deprecated since 4.2.0
OfficeLauncherUtility.prototype.openOfficeDocumentURL_Applet = function(url, readOnly)
{
    // test if Applet is available. If not, load it dynamically
    if (document.applets["zidilauncherapplet"]) {
        if (!(document.applets["zidilauncherapplet"].viewDocument)) {
        	Ext.Msg.show({
       		   title: "Launch Applet Error",
      		   msg: "Failed loading the Office Workdesk launcher applet.",
      		   buttons: Ext.Msg.OK,
      		   icon: Ext.MessageBox.WARNING
      		});
        } else {
            this.openOfficeDocumentURL_Applet_Run(url, readOnly);
        }
    } else {
        var appletDiv = document.createElement("div");
        appletDiv.id = "zidilauncherapplet_div";
        appletDiv.innerHTML = "<applet id='zidilauncherapplet' style='visibility: hidden' name='zidilauncherapplet' archive='applets/zidilauncher/ow_zidilauncher.jar' code='com.wewebu.ow.client.zidilauncher.ZidiLauncherApplet' width=0 height=0 mayscript='true'><param name='debug' value='"
                + (zidilauncherapplet_debug ? "true" : "false") + "'><param name='cfgPath' value='applets/zidilauncher/cfg.properties'><param name='cache_archive_ex' value='applets/zidilauncher/ow_zidilauncher.jar;preload;4.2.0.0'>Sorry, you need to enable Java</applet>";
        document.body.appendChild(appletDiv);
        var scope = this;
        window.setTimeout(function()
        {
            scope.openOfficeDocumentURL_Applet_TestAndRun(url, readOnly, 8);
        }, 50);
    }
};
// @Deprecated since 4.2.0
OfficeLauncherUtility.prototype.openOfficeDocumentURL_Applet_TestAndRun = function(url, readOnly, maxTest)
{
    if (!(document.applets["zidilauncherapplet"].viewDocument)) {
        if (maxTest > 0) {
        	var scope = this;
            window.setTimeout(function()
            {
                scope.openOfficeDocumentURL_Applet_TestAndRun(url, readOnly, maxTest - 1);
            }, 100);
        } else {
        	Ext.Msg.show({
      		   title: "Launch Applet (Test&Run) Error",
     		   msg: "Failed loading the Office Workdesk launcher applet.",
     		   buttons: Ext.Msg.OK,
     		   icon: Ext.MessageBox.WARNING
     		});
        }
    } else {
        this.openOfficeDocumentURL_Applet_Run(url, readOnly);
    }
};
// @Deprecated since 4.2.0
OfficeLauncherUtility.prototype.openOfficeDocumentURL_Applet_Run = function(url, readOnly)
{
    var result = 0;
    if (readOnly) {
        result = document.applets["zidilauncherapplet"].viewDocument(url);
    } else {
        result = document.applets["zidilauncherapplet"].editDocument(url);
    }
    if (this.zidilauncherapplet_debug) {
        if (result != 0) {
        	Ext.Msg.show({
     		   title: "Applet Launch Error",
    		   msg: "Error launching Office by Applet: error code " + result,
    		   buttons: Ext.Msg.OK,
    		   icon: Ext.MessageBox.WARNING
    		});
        }
    }
};
