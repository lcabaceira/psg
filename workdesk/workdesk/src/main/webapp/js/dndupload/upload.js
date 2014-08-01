/**
 * Alfresco Workdesk
 * Copyright (c) Alfresco Software, Inc. All Rights Reserved.
 * 
 * Requires ext-all.js Inspired from
 * http://robertnyman.com/html5/fileapi-upload/fileapi-upload.html
 */

Ext.namespace("upload");

var DND = DND || {};

(function() {
	var dropTargets = [];
	var uploader = null;
	DND.enabled = true;
	
	dropTargets.enable = function() {
		for(var i = 0; this[i]; i++) {
			this[i].enable();
		}
	};
	
	dropTargets.disable = function() {
		for(var i = 0; this[i]; i++) {
			this[i].disable();
		}
	};
	
	DND.images = 'js/dndupload/imgs';
	DND.setup = function(dContainer, dUploader) {
		uploader = dUploader;
		if(dContainer) {
			this.addDropTarget(dContainer);
		}
	};
	
	DND.addDropTarget = function(dContainer) {
		dContainer.addEventListener("transfer", DND.handleTransfer);
		if(this.enabled) {
			dContainer.enable();
		} else {
			dContainer.disable();
		}
		dropTargets.push(dContainer);
	};

	DND.uploadProgressXHR = function(event) {
	};

	DND.loadedXHR = function(event) {
		var currentImageItem = event.target.log;

		currentImageItem.className = "loaded";
		console.log("xhr upload of " + event.target.log.id + " complete");
	};

	DND.uploadError = function(error) {
		console.log("error: " + error);
	};

	//TODO: filesList and uploader.uploadFiles(filesList) has to be fixed for Mozilla and maybe Chrome too.
	//Mozilla: uploader.uploadFiles(filesList.toArray())
	//Chrome: new Packages.java.util.ArrayList() not working 
	DND.handleTransfer = function(dt) {
		if(!uploader) {
			alert('No uploader');
			return;
		}
		var files = dt.files;
		var nbFiles = dt.files.length;
		var uploadCfg = uploader.getUploadCfg();

		var invalidFilesSize = [];
		var invalidFilesExtension = [];
		var batchSize = 0;
		var filesList = [];
		var i = 0;

		var invalidFilesSizeStr = '';
		var invalidFilesExtensiontStr = '';
		var anInvalidFile;

		var errMessage;

		var msgBox = Ext.Msg
				.show({
					title : upload.Messages['upload.Messages.waitWileCheckingFile.title'],
					msg : upload.Messages['upload.Messages.waitWileCheckingFile.msg'],
					closable : false
				});

		// Validation checks
		if (!uploadCfg.isAcceptMultifile() && nbFiles > 1) {
			upload.ErrMessageBox
					.showError(upload.Messages['upload.Messages.errMultiFileNotSupported']);
			return;
		}

		if (uploadCfg.isAcceptMultifile() && uploadCfg.getMaxUploadCount() > 0
				&& uploadCfg.getMaxUploadCount() < nbFiles) {
			upload.ErrMessageBox.showError(upload.Messages.localize(
					'upload.Messages.errTooManyFiles', nbFiles, uploadCfg
							.getMaxUploadCount()));
			return;
		}

		var taskRun = function(count) {
			for (i = 0; i < nbFiles; i++) {
				var valid = true;
				var aFile = files[i];
				batchSize += aFile.size;

				if (!uploadCfg.isSizeAccepted(aFile.size)) {
					invalidFilesSize.push(aFile);
					valid = false;
				}

				if (!uploadCfg.isExtensionAccepted(aFile.name)) {
					invalidFilesExtension.push(aFile);
					valid = false;
				}

				if (valid) {
					filesList.push(aFile);
				}

				if (0 == (i % 10)) {
					msgBox.getDialog().doLayout(false, true);
				}
			}

			if (0 <= uploadCfg.getMaxBatchSize()
					&& batchSize > uploadCfg.getMaxBatchSize()) {
				upload.ErrMessageBox.showError(upload.Messages.localize(
						'upload.Messages.errInvalidBatchSize', Math
								.floor(uploadCfg.getMaxBatchSize()
										/ (1024 * 1024) * 100) / 100));
				return;
			}

			if (0 != invalidFilesSize.length) {
				invalidFilesSizeStr = '<hr/>'
						+ upload.Messages.localize(
								'upload.Messages.errInvalidFilesSize',
								uploadCfg.getMaxUploadSize() / (1024 * 1024));

				for (i = 0; i < invalidFilesSize.length; i++) {
					anInvalidFile = invalidFilesSize[i];
					invalidFilesSizeStr += '<p>- ' + anInvalidFile.name
							+ '</p>';
				}
			}

			if (0 != invalidFilesExtension.length) {
				invalidFilesExtensiontStr = '<hr/>'
						+ upload.Messages['upload.Messages.errInvalidFilesExtension'];

				for (i = 0; i < invalidFilesExtension.length; i++) {
					anInvalidFile = invalidFilesExtension[i];
					invalidFilesExtensiontStr += '<p>- ' + anInvalidFile.name
							+ '</p>';
				}
			}

			if (0 == filesList.length) {
				errMessage = upload.Messages.localize(
						'upload.Messages.errNoFiles',
						"default: No files to be uploaded.");
				errMessage += invalidFilesSizeStr + invalidFilesExtensiontStr;
				upload.ErrMessageBox.showError(errMessage);
				return;
			}

			msgBox.hide();

			if (0 != invalidFilesSizeStr.length
					|| 0 != invalidFilesExtensiontStr.length) {
				DND.dropTargets().disable();
				errMessage = '<p>'
						+ upload.Messages['upload.Messages.errInvalidFiles']
						+ '</p>'
						+ '<p>'
						+ upload.Messages['upload.Messages.questionSkipContinue']
						+ '</p>' + invalidFilesSizeStr
						+ invalidFilesExtensiontStr;

				upload.SkipCancelMessageBox.show(errMessage, function(btn) {
					var dlg;
					if (btn == 'yes') {
						dlg = DND.progressDialog;
						uploader.uploadFiles(filesList, dlg);
					}
					DND.dropTargets().enable();
				});

				return;
			}

			uploader.uploadFiles(filesList, DND.progressDialog);
		};

		taskRun(0);
	};

	DND.onSuccess = function() {
		// Replace this with your own handler
	};

	DND.dropTargets = function() {
		return dropTargets;
	};
	
	DND.enable = function () {
		DND.dropTargets().enable();
		this.enabled = true;
	};
	
	DND.disable = function () {
		DND.dropTargets().disable();
		this.enabled = false;
	};
}());

/**
 * Default localized messages. Use Ext.override({...}) to change the language.
 */
upload.Messages = upload.Messages
		|| {
			'upload.Messages.btnOk' : 'Ok',
			'upload.Messages.btnCancel' : 'Cancel',
			'upload.Messages.btnYes' : 'Yes',
			'upload.Messages.btnNo' : 'No',
			'upload.Messages.btnClose' : 'Close',
			'upload.Messages.lblUploading' : 'Uploading',
			'upload.Messages.lblOverall' : 'Overall',
			'upload.Messages.lblTimeEstimates' : 'Elapsed time / Remaining time: {0} / {1} ({2} MB/s)',
			'upload.Messages.msgCloseOnCompletion' : 'Close this dialog when the upload completes.',
			'upload.Messages.waitWileCheckingFile.msg' : 'Please wait while the files are being checked ...',
			'upload.Messages.waitWileCheckingFile.title' : 'Wait',
			'upload.Messages.msgErrGeneral' : 'Error',
			'upload.Messages.tltUpload' : 'Upload progress',
			'upload.Messages.errInvalidFiles' : 'There are some invalid files!',
			'upload.Messages.errInvalidFilesExtension' : 'File extension is not supported:',
			'upload.Messages.errInvalidFilesSize' : 'File size is too big (configured maximum size of {0} MB):',
			'upload.Messages.errInvalidBatchSize' : 'Batch size is too big (configured maximum size of {0} MB).',
			'upload.Messages.questionSkipContinue' : 'Skip them and continue?',
			'upload.Messages.errMultiFileNotSupported' : 'Multifile upload not supported.',
			'upload.Messages.errTooManyFiles' : 'You have tried to upload {0} files but just a maximum of {1} files are allowed.',
			'upload.Messages.errUpload' : 'Upload error.',
			'upload.Messages.errNoFiles' : 'No files to be uploaded.',
			'upload.Messages.errFileNotFound' : 'File not found {0}.',

			/**
			 * @param {String}
			 *            key A key from upload.Messages
			 * @param {String}
			 *            value1 The value to replace token {0}
			 * @param {String}
			 *            value2 Etc...
			 * @return {String} The formatted string
			 */
			localize : function(key) {
				var txt = upload.Messages[key];
				if (!txt) {
					txt = 'Key \'' + key + '\' not found!';
					return txt;
				}

				var args = Array.prototype.slice.call(arguments);
				args = args.slice(1);
				args.splice(0, 0, txt);
				return String.format.apply(String, args);
			}
		};

upload.ErrMessageBox = upload.MessageBox
		|| {
			showError : function(message) {
				Ext.MessageBox.buttonText.ok = upload.Messages['upload.Messages.btnOk'];
				Ext.MessageBox.show({
					title : upload.Messages['upload.Messages.msgErrGeneral'],
					msg : message,
					buttons : Ext.MessageBox.OK,
					animEl : 'mb3',
					width : 400,
					icon : Ext.MessageBox.ERROR
				});
			}
		};

upload.SkipCancelMessageBox = upload.SkipCancelMessageBox
		|| {
			show : function(message, callbackFunction) {
				Ext.MessageBox.buttonText.yes = upload.Messages['upload.Messages.btnYes'];
				Ext.MessageBox.buttonText.no = upload.Messages['upload.Messages.btnNo'];
				Ext.MessageBox.show({
					title : upload.Messages['upload.Messages.msgErrGeneral'],
					msg : message,
					buttons : Ext.MessageBox.YESNO,
					animEl : 'mb3',
					width : 400,
					fn : callbackFunction,
					icon : Ext.MessageBox.QUESTION
				});
			}
		};

/**
 * Implements an OwProgressMonitor interface. It is wrapped by the Java Class
 * OwJSprogressMonitor.
 */
upload.ProgressDialog = Ext
		.extend(
				Ext.Window,
				{
					constructor : function(config) {
						var myself = this;
						this.uploadedFiles = 0;

						this.canceled = false;
						this._hasErrors = false;

						this.cancelButton = new Ext.Button(
								{
									text : upload.Messages['upload.Messages.btnCancel'],
									handler : function(btn, event) {
										// myself.hide();
										myself.canceled = true;
										this.disable();
									},
									margins : '0 5'
								});
						this.closeButton = new Ext.Button({
							disabled : true,
							text : upload.Messages['upload.Messages.btnClose'],
							handler : function(btn, event) {
								myself.hide();
							},
							margins : '0 5'
						});
						this.lblError = new Ext.Panel({
							height : 60,
							padding : '5px 0px 5px 0px',
							border : false,
							html : '--',
							style : {
								color : 'red'
							}
						});
						this.lblCurrentFile = new Ext.Panel({
							padding : '10px 0px 5px 0px',
							border : false,
							html : '--'
						});
						this.lblTimeEstimates = new Ext.Panel({
							padding : '10px 0px 5px 0px',
							border : false,
							html : '--'
						});
						this.pBarFile = new Ext.ProgressBar({
							border : false,
							text : '0%',
							autoWidth : false
						});
						this.pBarOverall = new Ext.ProgressBar({
							border : false,
							text : '0%',
							autoWidth : false
						});
						this.progressBarsPanel = new Ext.Panel({
							padding : 5,
							border : false,
							region : 'center',
							layout : {
								type : 'vbox',
								align : 'stretch',
								pack : 'start'
							},
							items : [ this.lblCurrentFile, this.pBarFile,
									this.lblTimeEstimates, this.pBarOverall,
									this.lblError ],
							id : 'progressBarsPanel'
						});
						this.chkAutoClose = new Ext.form.Checkbox({
							hidden : true,
							checked : true
						});
						this.controlsPanel = new Ext.Panel(
								{
									bodyStyle : 'border-left-style: none; border-right-style: none; border-bottom-style: none;',
									padding : '5px 5px 5px 5px',
									layout : {
										type : 'hbox',
										align : 'middle',
										pack : 'start'
									},
									height : 40,
									region : 'south',
									items : [ this.chkAutoClose, {
										border : false,
										// html :
										// upload.Messages['upload.Messages.msgCloseOnCompletion'],
										flex : 1
									}, this.closeButton, this.cancelButton ]
								});

						// Copy any passed in configs into a newly defined
						// config object
						config = Ext
								.applyIf(
										{
											width : 500,
											height : 240,
											title : upload.Messages['upload.Messages.tltUpload'],
											modal : true,
											closable : false,
											hidden : true,
											resizable : false,
											layout : 'border',
											items : [
													this.progressBarsPanel,
													this.controlsPanel,
													{
														bodyStyle : 'border-top-style: none; border-right-style: none; border-bottom-style: none; vertical-align: middle; text-align: center;',
														html : '<br/><img src="'
																+ DND.images
																+ '/add_documents.png" />',
														width : 100,
														region : 'east',
														cls : 'upload-dlg-right'

													} ],
											listeners : {
												beforehide : this.beforeClose
											}
										}, config);
						if (!config.fileCount) {
							this.fileCount = 0;
						} else {
							this.fileCount = config.fileCount;
						}
						upload.ProgressDialog.superclass.constructor.call(this,
								config);
					},

					/**
					 * Notifies this dialog that a new file is being uploaded.
					 * 
					 * @param fileName
					 *            The name of the file that will be processed
					 *            next.
					 */
					startingNextFileUpload : function(fileName) {
						this.lblCurrentFile.update(fileName);
						this.pBarFile.reset();
						this.pBarFile.updateText('0%');
					},

					/**
					 * Notification that the current file was uploaded.
					 */
					fileUploaded : function() {
						this.uploadedFiles++;
						this.fullyUploadedFilesSize += this.fileSizes[this.uploadedFiles - 1];
						this.totalSizeUploaded = this.fullyUploadedFilesSize;
						this.updateEstimates();
					},

					/**
					 * Updates the estimated time values and updates the overall
					 * progress bar too.
					 */
					updateEstimates : function() {
						this.elapsedDeltaTime = new Date().getTime()
								- this.startTime;
						this.estimatedTotalDeltaTime = this.elapsedDeltaTime
								* this.totalSize / this.totalSizeUploaded;

						var elapsedTimeDbl = this.elapsedDeltaTime / 1000;
						var estimatedRemainingTimeDbl = (this.estimatedTotalDeltaTime - this.elapsedDeltaTime) / 1000;

						var elapsedTimeStr = ''
								+ Math.floor(elapsedTimeDbl / 3600)
								+ ':'
								+ String.leftPad(Math
										.floor((elapsedTimeDbl % 3600) / 60),
										2, '0')
								+ ':'
								+ String.leftPad(Math
										.floor(elapsedTimeDbl % 60), 2, '0');

						var estimatedRemainingTimeStr = ''
								+ Math.floor(estimatedRemainingTimeDbl / 3600)
								+ ':'
								+ String
										.leftPad(
												Math
														.floor((estimatedRemainingTimeDbl % 3600) / 60),
												2, '0')
								+ ':'
								+ String.leftPad(Math
										.floor(estimatedRemainingTimeDbl % 60),
										2, '0');

						var speed = (this.totalSizeUploaded / (1024 * 1024))
								/ elapsedTimeDbl;
						var timeEstimatesTxt = upload.Messages.localize(
								'upload.Messages.lblTimeEstimates',
								elapsedTimeStr, estimatedRemainingTimeStr, Math
										.round(speed));

						this.lblTimeEstimates.update(timeEstimatesTxt);
						this.pBarOverall.updateProgress(this.totalSizeUploaded
								/ this.totalSize);
						this.pBarOverall.updateText(''
								+ Math.round(this.totalSizeUploaded
										/ this.totalSize * 100) + '%');
					},

					/**
					 * Notification that all files were uploaded, cancel was
					 * pressed or there has been an error.
					 */
					finish : function() {
						if (this.isCanceled()) {
							this.hide();
						} else if (this.isAutoclose() && !this.hasErrors()) {
							this.hide();
						} else {
							this.cancelButton.disable();
							this.closeButton.enable();
						}
					},

					/**
					 * 
					 * @param progress
					 *            A floating point value between 0 and 1.
					 */
					fileUploadedProgress : function(progress) {
						this.totalSizeUploaded = this.fullyUploadedFilesSize
								+ (progress * this.fileSizes[this.uploadedFiles]);
						this.updateEstimates();

						this.pBarFile.updateProgress(progress);
						this.pBarFile.updateText(''
								+ Math.round(progress * 100) + '%');
					},

					/**
					 * Call this before starting a new upload session.
					 * 
					 * @param fileSizes
					 *            array of longs denoting the relative size
					 *            between files. Usually this would be the size
					 *            in bytes of each file.
					 */
					startingUpload : function(fileSizes) {
						DND.dropTargets().disable();

						this.fileSizes = fileSizes;
						this.fileCount = fileSizes.length;
						this.uploadedFiles = 0;

						this.totalSize = 0;
						this.totalSizeUploaded = 0;
						this.fullyUploadedFilesSize = 0;

						this.startTime = new Date().getTime();
						this.elapsedDeltaTime = 0;
						this.estimatedTotalDeltaTime = 0;

						var i;
						for (i = 0; i < fileSizes.length; i++) {
							this.totalSize += fileSizes[i];
						}

						this.canceled = false;
						this._hasErrors = false;

						this.show();

						this.lblCurrentFile.update('');
						this.lblTimeEstimates.update('');
						this.lblError.update('');

						this.pBarFile.reset();
						this.pBarFile.updateText('0%');
						this.pBarOverall.reset();
						this.pBarOverall.updateText('0%');

						this.cancelButton.enable();
						this.closeButton.disable();
					},

					error : function(message) {
						this._hasErrors = true;
						this.lblError.update(message);
						this.progressBarsPanel.doLayout();
					},

					isCanceled : function() {
						return this.canceled;
					},

					isAutoclose : function() {
						return this.chkAutoClose.getValue();
					},

					hasErrors : function() {
						return this._hasErrors;
					},

					/**
					 * This method makes sure to call any onSuccess handlers.
					 */
					beforeClose : function() {
						if (DND.onSuccess) {
							if (!this.hasErrors() && !this.isCanceled()) {
								DND.onSuccess();
							}
						}
						DND.dropTargets().enable();
						return true;
					}
				});

upload.ProgressDialog.getDialog = function() {
	if (!upload.ProgressDialog.dlg) {
		upload.ProgressDialog.dlg = new upload.ProgressDialog();
	}
	return upload.ProgressDialog.dlg;
};

DND.progressDialog = upload.ProgressDialog.getDialog();
//DND.progressDialog = upload.ProgressDialog_Simple.getDialog();	