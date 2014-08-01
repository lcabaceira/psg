/**
 * Alfresco Workdesk
 * Copyright (c) Alfresco Software, Inc. All Rights Reserved.
 * 
 * Requires ext-all.js Inspired from
 * http://robertnyman.com/html5/fileapi-upload/fileapi-upload.html
 */

upload.ProgressDialog_Simple = Ext
		.extend(
				Ext.Window,
				{
					constructor : function(config) {
						var myself = this;
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
						this.lblTimeEstimates = new Ext.Panel({
							padding : '10px 0px 5px 0px',
							border : false,
							html : '--'
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
							items : [ this.lblTimeEstimates, this.pBarOverall,
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

						upload.ProgressDialog.superclass.constructor.call(this,
								config);
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
						this.totalSizeUploaded = (progress * this.totalSize);
						this.updateEstimates();
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

						this.totalSize = 0;
						this.totalSizeUploaded = 0;

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

						this.lblTimeEstimates.update('');
						this.lblError.update('');

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

upload.ProgressDialog_Simple.getDialog = function() {
	if (!upload.ProgressDialog_Simple.dlg) {
		upload.ProgressDialog_Simple.dlg = new upload.ProgressDialog_Simple();
	}
	return upload.ProgressDialog_Simple.dlg;
};