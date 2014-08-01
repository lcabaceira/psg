/**
 * Alfresco Workdesk
 * Copyright (c) Alfresco Software, Inc. All Rights Reserved.
 */

var uploadURL = "dragdropcontent";
/**
 * Dummy upload configuration.
 * settings - JSON wrapper for cfg.properties as used by the DnD applet too. 
 */
function UploadCfg(settings) {
	//defaults
	this.maxUploadSize = -1; // no limit
	this.fileExtFilter = []; // all types accepted
	this.maxBatchSize = -1; // no limit
	this.maxUploadCount = -1; // no limit
	this.acceptMultifile = false;

	this.numberFromCfg = function(settings, key, defaultValue) {
		if (settings[key]) {
			var val = Number(settings[key]);
			if (NaN !== val) {
				return val;
			}
		}
		
		return defaultValue;
	};

	if (settings) {
		this.maxUploadSize = this.numberFromCfg(settings, "max_upload_size", -1);
		if (settings["file_ext_filter"]) {
			this.fileExtFilter = settings["file_ext_filter"].split(",");
			for ( var i = 0; i < this.fileExtFilter.length; i++) {
				this.fileExtFilter[i] = this.fileExtFilter[i].trim();
			}
		}
		this.maxBatchSize = this.numberFromCfg(settings, "max_batch_upload_size", -1);
		this.maxUploadCount = this.numberFromCfg(settings, "max_upload_count", -1);
		if (settings["multifile"]) {
			var val = settings["multifile"].trim().toLowerCase();
			this.acceptMultifile = (val === "true" || val === "yes");
		}
	}

	this.isSizeAccepted = function(fileSize) {
		if (this.maxUploadSize > 0) {
			return fileSize <= this.maxUploadSize;
		} else {
			// Unlimited
			return true;
		}
		return true;
	};

	this.isExtensionAccepted = function(filename) {
		if (0 == this.fileExtFilter.length) {
			return true;
		}

		var extensionStart = filename.lastIndexOf('.');
		var extension = null;
		if (-1 != extensionStart) {
			extension = filename.substring(extensionStart + 1).toLowerCase();
		}
		if (null != extension) {
			return (-1 != this.fileExtFilter.indexOf(extension));
		} else {
			return false;
		}
	};

	this.getMaxBatchSize = function() {
		return this.maxBatchSize;
	};
	this.getMaxUploadSize = function() {
		return this.maxUploadSize;
	};
	this.getMaxUploadCount = function() {
		return this.maxUploadCount;
	};
	this.isAcceptMultifile = function() {
		return this.acceptMultifile;
	};
}

/**
 * XMLHttpRequest based implementation of Uploader.
 * @param cfg - JSON with settings for this Uploader;
 * @returns - void
 */
function XhrUploader(cfg) {
	this.cfg = cfg;
	this.uploadFiles = function(files, progressDialog) {
		var xhr, fileSizes = [];
		var fd = new FormData();

		for ( var i = 0, f; f = files[i]; i++) {
			fileSizes[i] = f.size;
			fd.append('myFile', f);
		}

		// Uploading - for Firefox, Google Chrome and Safari
		xhr = new XMLHttpRequest();

		// Update progress bar
		xhr.upload.addEventListener("progress", function(evt) {
			if (evt.lengthComputable) {
				progressDialog.fileUploadedProgress(evt.loaded / evt.total);
			} else {
				// No data to calculate on
			}
			if (progressDialog.isCanceled()) {
				xhr.abort();
			}
		}, false);

		// File uploaded
		xhr.addEventListener("loadend",	function() {
							if (xhr.readyState !== xhr.DONE	|| xhr.status !== 200) {
								if (xhr.status === 0) {
									progressDialog.error("Unknown error.");
								} else {
									progressDialog.error(xhr.statusText + " ("
											+ xhr.status + ")");
								}
							} else {
							}
							progressDialog.finish();
						}, false);

		xhr.open("post", uploadURL, true);

		progressDialog.startingUpload(fileSizes);
		try {
			xhr.send(fd);
		} catch (err) {
			progressDialog.error("Error description: " + err.message);
			progressDialog.finish();
		}
	};
	
	this.getUploadCfg = function() {
		return this.cfg;
	};
}

/**
 * Creates a <div> element with a class of 'protected' to show a grayed out rectangle over a disabled DnD area. 
 * @returns __protectionDiv() a new <div> element.
 */
function createProtectionDiv() {
	var protectionDiv = document.createElement('div');
	protectionDiv.setAttribute("class", "protected");
	protectionDiv.addEventListener("dragleave", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		return false;
	}, false);

	protectionDiv.addEventListener("dragenter", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		evt.dataTransfer.dropEffect = "none";
		return false;
	}, false);

	protectionDiv.addEventListener("dragover", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		evt.dataTransfer.dropEffect = "none";
		return false;
	}, false);

	protectionDiv.addEventListener("drop", function(evt) {
		evt.preventDefault();
		evt.stopPropagation();
		evt.dataTransfer.dropEffect = "none";
		return false;
	}, false);
	
	// default not visible
	protectionDiv.style.display = "none";
	return protectionDiv;
}

/**
 * Prepares and registers this element as drop target.
 * @param element - an HTML element that will be prepared as Drop Target
 * @param showProtectionOnDisable - if true, the element will be protected with a gray div when drops are disabled.
 */
function addHtml5DropArea(element, showProtectionOnDisable) {
    if (new XMLHttpRequest().upload)//simple test for HTML5 upload
    {
    	if(element.classList != null && element.classList.contains("dropZone")) {
    		return; // Already a drop zone
    	}
    	var protectionDiv = createProtectionDiv();
    	var dropOverlay = document.createElement("div");

    	element.classList.add("dropZone");
    	element.appendChild(protectionDiv);
    	element.appendChild(dropOverlay);

    	element.addEventListener("dragenter", function(evt) {
    		if(!dropOverlay.active || !isValidDrag(evt)) {
    			return;
    		}
    		evt.preventDefault();
    		evt.stopPropagation();
    		dropOverlay.style.display = "block";
    	}, false);

    	dropOverlay.protectionDiv = protectionDiv;
    	dropOverlay.classList.add("dropOverlay");
    	dropOverlay.active = true;
    	dropOverlay.style.display = "none";

    	dropOverlay.disable = function() {
    		this.active = false;
    		if(showProtectionOnDisable) {
    			this.protectionDiv.style.display = "block";
    		}
    	};

    	dropOverlay.enable = function() {
    		this.protectionDiv.style.display = "none";
    		this.active = true;		
    	};

    	dropOverlay.setEnabled = function(enabled) {
    		if (true === enabled) {
    			this.enable();
    		} else {
    			this.disable();
    		}
    	};

    	dropOverlay.addEventListener("dragleave", function(evt) {
    		if(!this.active) {
    			return;
    		}
    		logDebug("dragleave", evt);
    		evt.preventDefault();
    		evt.stopPropagation();
    		element.classList.remove("over");
    		this.style.display = "none";
    	}, false);

    	dropOverlay.addEventListener("dragenter", function(evt) {
    		if(!this.active || !isValidDrag(evt)) {
    			return;
    		}
    		 
    		logDebug("dragenter", evt);
    		evt.preventDefault();
    		evt.stopPropagation();	
    		element.classList.add("over");
    		//var dt = evt.dataTransfer;
    		//dt.dropEffect = "copy";
    		//dt.effectAllowed = "all";
    	}, false);

    	dropOverlay.addEventListener("dragover", function(evt) {
    		if(!this.active) {
    			return;
    		}
    		logDebug("dragover", evt);
    		evt.preventDefault();
    		evt.stopPropagation();
    		//var dt = evt.dataTransfer;
    		//dt.dropEffect = "copy";
    		//dt.effectAllowed = dt.dropEffect == "copy";;
    	}, false);

    	dropOverlay.addEventListener("drop", function(evt) {
    		if(!this.active ||  !isValidDrag(evt)) {
    			return;
    		}
    		evt.preventDefault();
    		evt.stopPropagation();
    		logDebug("drop", evt);
    		var dataTransfer = evt.dataTransfer;
    		var event, items, item;
    		var len, i;

    		event = document.createEvent("Event");
    		event.initEvent("transfer",true,true);
    		event.files = [];

    		// Try to use "Directories and System" API if available
    		if(dataTransfer && dataTransfer.items) {
    			items = dataTransfer.items;
    			len = items.length;
    			try {
    				for (i = 0; i < len; i++) {
    					item = items[i];
    					readFilesFromTransferItem(item, event.files);					
    				}
    			} catch (e) {
    				// try the default
    				event.files = evt.dataTransfer.files;
    			}
    		} else {
    			event.files = evt.dataTransfer.files;
    		}

    		this.dispatchEvent(event);
    		element.classList.remove("over");
    		this.style.display = 'none';
    	}, false);

    	DND.addDropTarget(dropOverlay);
    }
}

/**
 * If this DataTransferItem represents a file, then add it to the fileList.
 * @see http://www.whatwg.org/specs/web-apps/current-work/multipage/dnd.html#the-datatransferitem-interface
 */
function readFilesFromTransferItem(item, fileList) {
	var entry = {};
	
	if (item.getAsEntry) { //Standard HTML5 API
		entry = item.getAsEntry();
	} else if (item.webkitGetAsEntry) { //WebKit implementation of HTML5 API.
		entry = item.webkitGetAsEntry();
	}

	if (entry.isFile) {
		var aFile = item.getAsFile();
		fileList.push(aFile);
	} else if (entry.isDirectory) {
		//skip folders
	}
}

/**
 * Initializes the DnD handler to use an instance of XMLHttpRequest for uploading. 
 * It needs HTML5 support in the browser. 
 * This functions should be called only once from the initialization of the AddMultipleDocuments plug-in.
 * 
 * @param settings
 * @param enabled - initial state of the DnD handler.
 */
function setupHtml5Dnd(settings, enabled) {
	var cfg = new UploadCfg(settings);
	var uploader = new XhrUploader(cfg);
	DND.progressDialog = upload.ProgressDialog_Simple.getDialog();
	DND.setup(null, uploader);
	if(enabled) {
		DND.enable();
	} else {
		DND.disable();
	}	
}
/**
 * Check if Drag is a valid element, only files 
 * from FileSystem should trigger Drag event.
 * @param evt
 * @returns {Boolean}
 */
function isValidDrag(evt) {
	var dt = evt.dataTransfer; 
	for (var i = 0; i < dt.types.length; i++) {
		if (debug) {
			console.log("Type of Drag element in event is = " + dt.types[i]);
		}
		if (dt.types[i] == "Files") { return true; }//allow drop from file system only
	}
	return false;
}

function logDebug(strDragType, evt) {
  if (debug) {
	var firedOn = evt.target ? evt.target : evt.srcElement;
	var allowed;
	try {
		allowed = evt.dataTransfer.effectAllowed;//IE10 throws exception
	} catch (e) { allowed = "[exception reading \"effectAllowed\"]";}
	if (console){
		console.log(strDragType + " " + firedOn.nodeName + " event = " + evt.dataTransfer.dropEffect + " allowed = " + allowed + " items = " + evt.dataTransfer.items);
	}
  }
}