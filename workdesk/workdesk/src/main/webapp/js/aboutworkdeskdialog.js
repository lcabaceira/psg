/**
 * Alfresco Workdesk
 * Copyright (c) Alfresco Software, Inc. All Rights Reserved.
 * 
 * Requires ext-all.js
 */
Ext.namespace("aboutworkdeskdialog");

/**
 * Default localized messages. Use Ext.override({...}) to change the language.
 */
aboutworkdeskdialog.Messages = aboutworkdeskdialog.Messages
		|| {
			'aboutworkdeskdialog.Messages.btnOk' : 'Ok',
		    'aboutworkdeskdialog.Messages.title' : 'About',

			/**
			 * @param {String}
			 *            key A key from aboutworkdeskdialog.Messages
			 * @param {String}
			 *            value1 The value to replace token {0}
			 * @param {String}
			 *            value2 Etc...
			 * @return {String} The formatted string
			 */
			localize : function(key) {
				var txt = aboutworkdeskdialog.Messages[key];
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

aboutworkdeskdialog.AboutDialog = Ext
		.extend(
				Ext.Window,
				{
					constructor : function(message,copyright,config) {
						this.intervalID='';
						this.lblScroll = new Ext.Panel({
							border : false,
							hidden: false, 
						    contentEl : 'about-contributions',
						});
						this.lblMessage = new Ext.Panel({
							border : false,
							hidden: false,
							html : message,
						    bodyStyle : 'padding-bottom:10px;'
						});
						this.lblCopyright = new Ext.Panel(
							{
								layout : {
							  	   type : 'hbox',
								   align : 'left',
								   pack : 'start'
							    },
							height : 30,
							border : false,
						    region : 'south',
							html : copyright,
							bodyStyle : 'padding-left:5px;'
						});
						this.aboutPanel = new Ext.Panel({
							padding : 5,
							border : false,
							region : 'center',
							layout : {
								type : 'vbox',
								align : 'stretch',
								pack : 'start'
							},
							items : [this.lblMessage,
							         this.lblScroll],
							id : 'aboutPanel'
						});

						// Copy any passed in configs into a newly defined
						// config object
						
						this.logoPanel=new Ext.Panel(
							{
									layout : {
								  	   type : 'hbox',
									   align : 'left',
									   pack : 'start'
								    },
								height : 55,
								region : 'north',
								contentEl : 'about-logo'
						   } 
						);
						
						config = Ext
								.applyIf(
										{
											width : 520,
											height : 320,
											title : aboutworkdeskdialog.Messages['aboutworkdeskdialog.Messages.title'],
											modal : true,
											closable : true,
											hidden : true,
											resizable : false,
											layout : 'border',
											items : [
													this.aboutPanel,
													this.logoPanel,
													this.lblCopyright],
											listeners : {
											}
										}, config);

						aboutworkdeskdialog.AboutDialog.superclass.constructor.call(this,
								config);
						Ext.get('about-contributions').setVisibilityMode(Ext.Element.DISPLAY);
						Ext.get('about-contributions').setVisible(true);
					    Ext.get('about-logo').setVisibilityMode(Ext.Element.DISPLAY);
						Ext.get('about-logo').setVisible(true);
					},

					setInterval : function(){
						var contributionsDiv = document.getElementById('about-contributions');
						var contributioncontent = document.getElementById('about-contributions-content');
						contributioncontent.style.padding=contributionsDiv.clientHeight+'px 0px';
						this.intervalID = window.setInterval(function f() { 
							                              contributionsDiv.scrollTop++;
                                                          if (contributionsDiv.scrollTop >= (contributioncontent.clientHeight - contributionsDiv.clientHeight)) 
                                                            {
                                                              contributionsDiv.scrollTop = 0;
                                                            }
                                                         }, 80);
				 	},
				 	
				 	onShow: function(){
				 		aboutworkdeskdialog.AboutDialog.superclass.onShow.call(this);
				 		this.setInterval();	
				 	},
				 	close: function(){
				 		    window.clearInterval(this.intervalID);
							this.hide();
				 	}
				});

aboutworkdeskdialog.getDialog = function(message,copyright) {
	if (!aboutworkdeskdialog.AboutDialog.dlg) {
		aboutworkdeskdialog.AboutDialog.dlg = new aboutworkdeskdialog.AboutDialog(message,copyright);
	}
	return aboutworkdeskdialog.AboutDialog.dlg;
};

function showAbout(messages,copyright) {
	//for for construct message
	var msg = '';
	for ( var i = 0; i < messages.length; i++) {
		var line = messages[i];
		msg += '<br/>' + line;
	}

	var dialog = aboutworkdeskdialog.getDialog(msg, copyright);
	dialog.show();
}