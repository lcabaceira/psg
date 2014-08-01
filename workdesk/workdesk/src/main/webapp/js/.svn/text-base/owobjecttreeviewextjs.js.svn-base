OwObjectTreeViewEXTJS = function() {
	var m_listViewURL;
	var clientSideId;
	var isDynamicSplit;
	var toBeUpdatedIds;
	var toBeUpdatedURLs;
	var m_mainURL;
	var m_useIconClass;
	var treeId;
	var loadingMessage;
	var errorTitleText;
	var updateErrorText;
	var requestTimeOutText;
	var cannotLoadNodeText;
	var errorWindow;
	var lastSelectedNode;
	var errorWindow;
	var errorHeight = 161;
	var showStacktraceMessage;
	var collapseStacktraceMessage;
	var expandOnSelect;
	var currentNodeExpanded;
	var loadingMask;
	var treeCollapseURL;
	var DNDappletUpdateURL;
	var ajaxRequestsMonitor = {
		interval : 500,
		numberOfRequests : 0,
		init : function(numberOfRequests) {
			this.numberOfRequests = numberOfRequests;
			showMask();
		},
		requestDone : function() {
			if (this.numberOfRequests > 0) {
				this.numberOfRequests = this.numberOfRequests - 1;
			}
			this.checkRun();
		},
		checkRun : function() {
			if (this.numberOfRequests <= 0) {
				this.stop();
			}
		},
		run : function() {
			this.checkRun();
		},
		stop : function() {
			grid = document.getElementById('grid-panel');
			if (!grid) {
				hideMask();
			}
			Ext.TaskMgr.stop(this);
		}
	};
	return {
		config : {},

		init : function() {
			m_mainURL = this.config.m_mainURL;
			m_listViewURL = this.config.m_listViewURL;
			treeCollapseURL = this.config.m_treeCollapseURL;
			clientSideId = this.config.m_clientSideId;
			treeId = clientSideId + 'cmp';
			selectedId = this.config.m_selectedId;
			selectedPathIds = this.config.m_selectedPathIds;
			rootIconCls = this.config.m_rootIconClass;
			rootIcon = this.config.m_rootIcon;
			isDynamicSplit = this.config.m_useDynamicSplit;
			toBeUpdatedIds = this.config.m_toBeUpdatedIds;
			toBeUpdatedURLs = this.config.m_toBeUpdatedURLs;
			m_useIconClass = this.config.m_useIconClass;
			loadingMessage = this.config.m_loadingMessage;
			errorTitleText = this.config.m_errorTitleText;
			updateErrorText = this.config.m_updateErrorText;
			requestTimeOutText = this.config.m_requestTimeOutText;
			cannotLoadNodeText = this.config.m_cannotLoadNodeText;
			showStacktraceMessage = this.config.m_showStacktraceMessage;
			collapseStacktraceMessage = this.config.m_collapseStacktraceMessage;
			expandOnSelect = this.config.m_expandOnSelect;
			currentNodeExpanded = this.config.m_isSelectedNodeExpanded;
			DNDappletUpdateURL = this.config.m_DNDappletUpdateURL;
			var treeLoader = new Ext.tree.TreeLoader({
				dataUrl : this.config.m_treeDataURL
			});
			if (!m_useIconClass) {
				baseAttrs = {
					openedIcon : 'openedIcon',
					closedIcon : 'closedIcon'
				};
				treeLoader.baseAttrs = baseAttrs;
			}
			treeLoader.addListener("loadexception", onTreeLoadException);
			var tree = new Ext.tree.TreePanel({
				useArrows : false,
				autoScroll : true,
				animate : true,
				enableDD : false,
				border : false,
				id : treeId,
				keys : {
					key : 13,
					stopEvent : true,
					ctrl : false,
					shift : false,
					alt : false,
					scope : this,
					handler : onTreeEnterPressed
				},

				//autoHeight : isDynamicSplit,
				// auto create TreeLoader
				loader : treeLoader,

				root : {
					nodeType : 'async',
					text : this.config.m_rootText,
					draggable : false,
					id : this.config.m_rootId,
					singleClickExpand : expandOnSelect
				}
			});
			if (m_useIconClass) {
				tree.getRootNode().attributes['iconCls'] = rootIconCls;
			} else {
				tree.getRootNode().attributes['openedIcon'] = this.config.m_rootOpenedIcon;
				tree.getRootNode().attributes['closedIcon'] = this.config.m_rootClosedIcon;
				tree.getRootNode().attributes['icon'] = rootIcon;
			}
			// render the tree
			tree.render('extjs-tree-panel');

			setHeight(tree);

			tree.addListener('load', onNodeLoaded);
			tree.addListener('click', onNodeClick);
			tree.addListener('beforeclick', onBeforeNodeClick);
			tree.addListener('expandnode', onExpandNode);
			tree.addListener('collapsenode', onCollapseNode);
			tree.getSelectionModel().addListener('beforeselect',
					onBeforeSelectNode);
			selectNode(tree);

			//key navigation
			Ext
					.onReady(function() {
						var keyMap = new Ext.KeyMap(
								document,
								[
										{
											stopEvent : true,
											key : "T",
											ctrl : true,
											fn : function(key, event) {
												event.stopEvent();
												theTree = Ext.getCmp(treeId);
												if (theTree) {
													selectedNode = theTree
															.getSelectionModel()
															.getSelectedNode();
													if (selectedNode) {
														selectedNode.select();
													} else {
														theTree.getRoot()
																.select();
													}
												}
											}
										},
										{
											stopEvent : true,
											key : "D",
											ctrl : true,
											fn : function() {
												grid = Ext.getCmp('owgrid');
												if (grid) {
													grid.getView().focusEl
															.focus();
													//select the row too
													selectionModel = grid
															.getSelectionModel();
													if (selectionModel) {
														if (selectionModel
																.getSelections().length == 0) {
															selectionModel
																	.selectFirstRow();
														}
													}
												}
											}
										} ]);
						keyMap.stopEvent = true;
					});

		}// end init
	};// end of return
	// private functions
	// -----< onTreeEnterPressed >---------------------------------------
	function onTreeEnterPressed(keyCode, event) {

		event.stopEvent();
		target = event.getTarget();
		target.click();

	}

	// -----< onExpandNode >---------------------------------------------
	function onExpandNode(node) {
		var postparam = Ext.urlEncode({
			owNodeId : "" + node.id + "",
			collapsed : false
		});

		Ext.lib.Ajax.request("POST", treeCollapseURL, null, postparam);
	}
	// -----< onCollapseNode >-------------------------------------------
	function onCollapseNode(node) {
		var postparam = Ext.urlEncode({
			owNodeId : "" + node.id + "",
			collapsed : true
		});

		Ext.lib.Ajax.request("POST", treeCollapseURL, null, postparam);
	}
	// -----< setHeight >------------------------------------------------
	function setHeight(tree) {
		if (!isDynamicSplit) {
			var treeDiv = document.getElementById('extjs-tree-panel');
			tree.setHeight(parseInt(treeDiv.style.height));
		}
	}
	// -----< selectNodeOnReady >-----------------------------------------
	function selectNodeOnReady() {
		theTree = Ext.getCmp(treeId);
		selectNode(theTree);
	}
	// -----< selectNode >------------------------------------------------
	function selectNode(tree, index) {
		// var selectedNode = tree.getNodeById(selectedId);
		var node;
		i = index || 0;
		if (i == selectedPathIds.length - 1) {
			node = tree.getNodeById(selectedPathIds[i]);
			if (node != null) {
				node.ensureVisible();
				node.select();
				if (currentNodeExpanded) {
					node.expand();
				} else {
					node.collapse();
				}
			} else {
				tree.getRootNode().select();
			}
		} else {
			node = tree.getNodeById(selectedPathIds[i]);
			if (node) {
				node.expand(false, false, selectNode.createDelegate(this, [
						tree, ++i ]));
			}
		}
	}

	// -----< onNodeLoaded >------------------------------------------------
	function onNodeLoaded(node) {
		if (node) {
			node.ensureVisible();
		}
	}
	// -----< onTreeLoadException >-----------------------------------------
	function onTreeLoadException(treeLoader, node, response) {
		handleError(response, cannotLoadNodeText);
	}
	// -----< onBeforeSelectNode >------------------------------------------
	function onBeforeSelectNode(selModel, newNode, oldNode) {
		if (m_useIconClass) {
			changeNodeIconCls(oldNode, 'folder-open', 'folder-close');
			changeNodeIconCls(newNode, 'folder-close', 'folder-open');
		} else {
			changeNodeIcon(oldNode, false);
			changeNodeIcon(newNode, true);
		}
	}
	// -----< onBeforNodeClick >------------------------------------------------
	function onBeforeNodeClick(node, e) {
		theTree = Ext.getCmp(treeId);
		selectionModel = theTree.getSelectionModel();
		if (selectionModel) {
			lastSelectedNode = selectionModel.getSelectedNode();
		}
	}
	// -----< onNodeClick >------------------------------------------------
	function onNodeClick(node) {
		var mainId = "OwSubLayout_MAIN";

		if (m_listViewURL != null) {
			hideMenu();
			// hideErrorMessage();
			var treeElement = this.getEl();
			var theTree = this;
			var theTreeNode = node;
			var oldSelectedNode = theTree.getSelectionModel().getSelectedNode();
			// treeElement.mask();
			showMask();

			var ajaxUpdLogName = "Ajax Request - UPDATE region " + mainId;
			var updDomLogName = "Update DOM for region " + mainId;
			var updLayoutLogName = "Update Layout for region " + mainId;

			var requestConfig = {
				timeout : 300000,
				// disableCaching : false,
				success : function(o) {
					extJsLog.timeEnd(ajaxUpdLogName);
					extJsLog.time(updDomLogName);
					var contentDiv = document.getElementById(mainId);
					Ext.DomHelper.useDom = true;
					Ext.DomHelper.overwrite(contentDiv, o.responseText);
					extJsLog.timeEnd(updDomLogName);
					updateScripts(contentDiv);

					executeUpdateCalls(clientSideId);
					updateDnDApplet();

					extJsLog.time(updLayoutLogName);
					arrangeRecordHeight(clientSideId, true);
					if (typeof (window['OwObjectLayoutEXTJS']) != 'undefined') {
						OwObjectLayoutEXTJS.updateSize();
					}
					if (usesEXTJSGrid()) {
						gridPanelDiv = document.getElementById('grid-panel');
						if (gridPanelDiv) {
							OwObjectListViewEXTJSGrid.init();
							OwObjectListViewEXTJSGrid
									.setHeight(gridPanelDiv.style.height);
						}
					}
					setHeight(theTree);
					/* IE7 fix: empty div in IE7 has a certain height */
					fixMessageHeight(false);
					extJsLog.timeEnd(updLayoutLogName);

					//hideMask();
					return true;
				},
				failure : function(o) {
					extJsLog.timeEnd(ajaxUpdLogName);
					hideMask();
					handleError(o);
				}
			};
			var postparam = Ext.urlEncode({
				owNodeId : "" + node.id + ""
			});
			extJsLog.time(ajaxUpdLogName);
			Ext.lib.Ajax.request("POST", m_listViewURL, requestConfig,
					postparam);
		}
	}
	function updateDnDApplet() {
		var ajaxUpdLogName = "Ajax Request - UPDATE region DNDApplet";
		var updDomLogName = "Update DOM for region DNDApplet";

		if (null != DNDappletUpdateURL) {
			var requestConfig = {
				timeout : 30000,
				// disableCaching : false,
				success : function(o) {
					extJsLog.timeEnd(ajaxUpdLogName);
					extJsLog.time(updDomLogName);
					if (o.responseText) {
						var value = trim(o.responseText);
						var enabledValue = (/^true$/i).test(value);
						var dndupload = document.getElementById('dndupload');
						if(dndupload) {
							DnDAppletEnableDrop(enabledValue);
						} else {
							if(enabledValue) {
								DND.enable();
							} else {
								DND.disable();
							}
						}
					}
					extJsLog.timeEnd(updDomLogName);
				},
				failure : function(o) {
				}
			};

			extJsLog.time(ajaxUpdLogName);
			Ext.lib.Ajax.request("POST", DNDappletUpdateURL, requestConfig,
					null);
		}
	}
	function showMask() {
		var theMask = getMask();
		var isMasked = theMask.el.isMasked();
		if (!isMasked) {
			extJsLog.time('theMask.show');
			theMask.show();
			extJsLog.timeEnd('theMask.show');
			extJsLog.time('theMask');
		}
	}
	function hideMask() {
		var theMask = getMask();
		var isMasked = theMask.el.isMasked();
		if (isMasked) {
			extJsLog.timeEnd('theMask');
			extJsLog.time('theMask.hide');
			theMask.hide();
			extJsLog.timeEnd('theMask.hide');
		}
	}
	function getMask() {
		if (generalLoadingMask == null) {
			generalLoadingMask = new Ext.LoadMask(Ext.getBody(), {
				id : 'generalMask',
				msg : loadingMessage
			});
		}
		return generalLoadingMask;
	}
	// -----< handle error > -------------------
	function handleError(o, errorMsg) {
		if (trim(o.responseText) == 'owsessiontimeout') {
			document.location = m_mainURL;
		} else {
			if (errorWindow == null || !errorWindow.isVisible()) {
				if (errorMsg == null) {
					theMessage = requestTimeOutText;
				} else {
					theMessage = errorMsg;
				}
				if (o.responseText) {
					theMessage = eval(o.responseText);
				}
				myArray = null;
				if (Ext.isArray(theMessage)) {
					myArray = theMessage;
				}
				errorDisplay = null;
				if (myArray) {
					messagePanel = new Ext.Panel({
						region : 'north',
						autoScroll : true,
						layout : 'fit',
						height : 70,
						bodyCssClass : 'OwErrorStack',
						bodyStyle : 'font-weight:bold',
						html : myArray[0]
					});
					stackTracePanel = new Ext.Panel({
						region : 'center',
						autoScroll : true,
						title : showStacktraceMessage,
						collapsible : true,
						collapsed : true,
						bodyCssClass : 'OwErrorStack',
						html : '<pre>' + myArray[1] + '</pre>'
					});

					stackTracePanel.on('expand', onStacktraceExpand);
					stackTracePanel.on('collapse', onStacktraceCollapse);
					errorDisplay = new Ext.Panel({
						region : 'center',
						layout : 'border',
						items : [ messagePanel, stackTracePanel ]
					});

				} else {
					errorDisplay = new Ext.Panel({
						region : 'center',
						autoScroll : true,
						bodyCssClass : 'OwErrorStack',
						bodyStyle : 'font-weight:bold',
						html : theMessage
					});
				}

				panel1 = new Ext.Panel({
					border : false,
					id : 'p1',
					flex : 0.2
				});
				buttonOk = new Ext.Button({
					width : 50
				});
				panel2 = new Ext.Panel({
					border : false,
					id : 'p2',
					flex : 0.2
				});

				buttonOk.setText("Ok");
				buttonOk.on('click', closeErrorWindow);

				panelOk = new Ext.Panel({
					layout : new Ext.layout.HBoxLayout({
						align : 'middle'
					}),
					height : 30,
					region : 'south',
					items : [ panel1, buttonOk, panel2 ]
				});

				errorPanel = new Ext.Panel({
					region : 'center',
					layout : 'border',
					items : [ errorDisplay, panelOk ]
				});

				errorWindow = new Ext.Window({
					title : errorTitleText,
					closable : 'true',
					closeAction : 'hide',
					layout : 'border',
					width : 400,
					height : errorHeight,
					minWidth : 200,
					minHeight : errorHeight,
					modal : true,
					iconCls : 'ow-ext-error',
					items : [ errorPanel ]
				});
				errorWindow.show();

				if (lastSelectedNode) {
					theTree = Ext.getCmp(treeId);
					theTree.render();

					theTree.getSelectionModel().select(lastSelectedNode);
					lastSelectedNode.ensureVisible();
				}
			}
		}
	}

	function onStacktraceExpand() {
		if (errorWindow) {
			errorWindow.setHeight(400);
			this.setTitle(collapseStacktraceMessage);
		}
	}
	function onStacktraceCollapse() {
		if (errorWindow) {
			errorWindow.setHeight(errorHeight);
			this.setTitle(showStacktraceMessage);
		}
	}

	// -----< executeUpdateCalls > -------------------
	function executeUpdateCalls(clientSideId) {
		if (toBeUpdatedIds != null) {

			var numberOfUpdates = 0;
			for (i = 0; i < toBeUpdatedIds.length; i++) {
				var element = document.getElementById(toBeUpdatedIds[i]);
				if (element) {
					numberOfUpdates++;
				}
			}
			ajaxRequestsMonitor.init(numberOfUpdates);
			Ext.TaskMgr.start(ajaxRequestsMonitor);
			for (i = 0; i < toBeUpdatedIds.length; i++) {
				var element = document.getElementById(toBeUpdatedIds[i]);
				if (element) {
					delegate = executeUpdateCall.createDelegate(this, [
							clientSideId, element, toBeUpdatedURLs[i] ]);
					delegate.call();
				}
			}
		} else {
			hideMask();
		}
	}
	function executeUpdateCall(clientSideId, element, url) {
		showMask();
		var ajaxUpdLogName = "Ajax Request - UPDATE region " + element.id;
		var updDomLogName = "Update DOM for region " + element.id;
		var requestConfig = {
			success : function(responseObject) {
				extJsLog.timeEnd(ajaxUpdLogName);
				extJsLog.time(updDomLogName);
				if (element) {
					extEl = Ext.get(element.id);
					if (extEl && Ext.isIE) {
						extEl.update(responseObject.responseText, true);
					} else {
						Ext.DomHelper.useDom = true;
						Ext.DomHelper.overwrite(element,
								responseObject.responseText);
						updateScripts(element);
					}

					if (Ext.isIE7) {
						fixIE7EmptyDivs(extEl);
					}

					arrangeRecordHeight(clientSideId, true);
					if (typeof (window['OwObjectLayoutEXTJS']) != 'undefined') {
						OwObjectLayoutEXTJS.updateSize();
					}

				}
				ajaxRequestsMonitor.requestDone();
				extJsLog.timeEnd(updDomLogName);
			},
			error : function(responseObject) {
				extJsLog.timeEnd(ajaxUpdLogName);
				ajaxRequestsMonitor.requestDone();
				Ext.Msg.alert(errorTitleText, updateErrorText + "\n"
						+ o.responseText);
			}
		};
		extJsLog.time(ajaxUpdLogName);
		Ext.lib.Ajax.request("POST", url, requestConfig);
	}

	//============ The great hack of all times =================
	/**
	 * Fix for IE7 "empty DIVs" bug. 
	 * Basically we are trying to detect empty DIVs and hide them.
	 * 
	 * @param extEl An instance of Ext.Element representing the element that must be fixed. 
	 * 	Usually this will be a wrapper over a DIV.
	 */
	function fixIE7EmptyDivs(extEl) {
		//extEl.clean(true);
		var newDom = extEl.dom;
		var i = 0;
		var node;
		for (i = 0; i < newDom.childNodes.length; i++) {
			node = newDom.childNodes[i];
			// Type 8 means "Comment node" see: <http://www.w3schools.com/dom/dom_nodetype.asp>
			if (!node.hasChildNodes() || node.nodeType == 8) {
				newDom.removeChild(node);
			}
		}
		if (0 == newDom.children.length) {
			extEl.addClass("OwContainerInvisible");
		} else {
			extEl.removeClass("OwContainerInvisible");
		}
	}
	//============ The great hack of all times ends here =======		

	// -----< updateScripts > -------------------
	/**
	 * relink the scripts to the DOM
	 * 
	 * @param {Object}
	 *            contentDiv
	 */
	function updateScripts(contentDiv) {
		var theScripts = contentDiv.getElementsByTagName('script');
		for ( var i = 0; i < theScripts.length; i++) {
			eval(theScripts[i].innerHTML);
		}
	}
	// -----< changeNodeIconCls >----------------------------------------------
	function changeNodeIconCls(theTreeNode, oldClass, newClass) {
		if (theTreeNode) {
			iconElement = theTreeNode.ui.getIconEl();
			if (iconElement) {
				var el = Ext.get(iconElement);
				if (el) {
					el.removeClass(oldClass);
					el.addClass(newClass);
				}
			}
		}
	}
	// -----< changeNodeIcon >----------------------------------------------
	function changeNodeIcon(theTreeNode, useOpenIcon) {
		if (theTreeNode) {
			iconElement = theTreeNode.ui.getIconEl();
			if (useOpenIcon) {
				theTreeNode.attributes['icon'] = theTreeNode.attributes['openedIcon'];
			} else {
				theTreeNode.attributes['icon'] = theTreeNode.attributes['closedIcon'];
			}
			iconElement.src = theTreeNode.attributes['icon'];
		}
	}
	function closeErrorWindow() {
		if (errorWindow) {
			errorWindow.close();
		}
	}
}();
OwObjectTreeViewEXTJS = function() {
	var m_listViewURL;
	var clientSideId;
	var isDynamicSplit;
	var toBeUpdatedIds;
	var toBeUpdatedURLs;
	var m_mainURL;
	var m_useIconClass;
	var treeId;
	var loadingMessage;
	var errorTitleText;
	var updateErrorText;
	var requestTimeOutText;
	var cannotLoadNodeText;
	var errorWindow;
	var lastSelectedNode;
	var errorWindow;
	var errorHeight = 161;
	var showStacktraceMessage;
	var collapseStacktraceMessage;
	var expandOnSelect;
	var currentNodeExpanded;
	var loadingMask;
	var treeCollapseURL;
	var DNDappletUpdateURL;
	var ajaxRequestsMonitor = {
		interval : 500,
		numberOfRequests : 0,
		init : function(numberOfRequests) {
			this.numberOfRequests = numberOfRequests;
			showMask();
		},
		requestDone : function() {
			if (this.numberOfRequests > 0) {
				this.numberOfRequests = this.numberOfRequests - 1;
			}
			this.checkRun();
		},
		checkRun : function() {
			if (this.numberOfRequests <= 0) {
				this.stop();
			}
		},
		run : function() {
			this.checkRun();
		},
		stop : function() {
			grid = document.getElementById('grid-panel');
			if (!grid) {
				hideMask();
			}
			Ext.TaskMgr.stop(this);
		}
	};
	return {
		config : {},

		init : function() {
			m_mainURL = this.config.m_mainURL;
			m_listViewURL = this.config.m_listViewURL;
			treeCollapseURL = this.config.m_treeCollapseURL;
			clientSideId = this.config.m_clientSideId;
			treeId = clientSideId + 'cmp';
			selectedId = this.config.m_selectedId;
			selectedPathIds = this.config.m_selectedPathIds;
			rootIconCls = this.config.m_rootIconClass;
			rootIcon = this.config.m_rootIcon;
			isDynamicSplit = this.config.m_useDynamicSplit;
			toBeUpdatedIds = this.config.m_toBeUpdatedIds;
			toBeUpdatedURLs = this.config.m_toBeUpdatedURLs;
			m_useIconClass = this.config.m_useIconClass;
			loadingMessage = this.config.m_loadingMessage;
			errorTitleText = this.config.m_errorTitleText;
			updateErrorText = this.config.m_updateErrorText;
			requestTimeOutText = this.config.m_requestTimeOutText;
			cannotLoadNodeText = this.config.m_cannotLoadNodeText;
			showStacktraceMessage = this.config.m_showStacktraceMessage;
			collapseStacktraceMessage = this.config.m_collapseStacktraceMessage;
			expandOnSelect = this.config.m_expandOnSelect;
			currentNodeExpanded = this.config.m_isSelectedNodeExpanded;
			DNDappletUpdateURL = this.config.m_DNDappletUpdateURL;
			var treeLoader = new Ext.tree.TreeLoader({
				dataUrl : this.config.m_treeDataURL
			});
			if (!m_useIconClass) {
				baseAttrs = {
					openedIcon : 'openedIcon',
					closedIcon : 'closedIcon'
				};
				treeLoader.baseAttrs = baseAttrs;
			}
			treeLoader.addListener("loadexception", onTreeLoadException);
			var tree = new Ext.tree.TreePanel({
				useArrows : false,
				autoScroll : true,
				animate : true,
				enableDD : false,
				border : false,
				id : treeId,
				keys : {
					key : 13,
					stopEvent : true,
					ctrl : false,
					shift : false,
					alt : false,
					scope : this,
					handler : onTreeEnterPressed
				},

				//autoHeight : isDynamicSplit,
				// auto create TreeLoader
				loader : treeLoader,

				root : {
					nodeType : 'async',
					text : this.config.m_rootText,
					draggable : false,
					id : this.config.m_rootId,
					singleClickExpand : expandOnSelect
				}
			});
			if (m_useIconClass) {
				tree.getRootNode().attributes['iconCls'] = rootIconCls;
			} else {
				tree.getRootNode().attributes['openedIcon'] = this.config.m_rootOpenedIcon;
				tree.getRootNode().attributes['closedIcon'] = this.config.m_rootClosedIcon;
				tree.getRootNode().attributes['icon'] = rootIcon;
			}
			// render the tree
			tree.render('extjs-tree-panel');

			setHeight(tree);

			tree.addListener('load', onNodeLoaded);
			tree.addListener('click', onNodeClick);
			tree.addListener('beforeclick', onBeforeNodeClick);
			tree.addListener('expandnode', onExpandNode);
			tree.addListener('collapsenode', onCollapseNode);
			tree.getSelectionModel().addListener('beforeselect',
					onBeforeSelectNode);
			selectNode(tree);

			//key navigation
			Ext
					.onReady(function() {
						var keyMap = new Ext.KeyMap(
								document,
								[
										{
											stopEvent : true,
											key : "T",
											ctrl : true,
											fn : function(key, event) {
												event.stopEvent();
												theTree = Ext.getCmp(treeId);
												if (theTree) {
													selectedNode = theTree
															.getSelectionModel()
															.getSelectedNode();
													if (selectedNode) {
														selectedNode.select();
													} else {
														theTree.getRoot()
																.select();
													}
												}
											}
										},
										{
											stopEvent : true,
											key : "D",
											ctrl : true,
											fn : function() {
												grid = Ext.getCmp('owgrid');
												if (grid) {
													grid.getView().focusEl
															.focus();
													//select the row too
													selectionModel = grid
															.getSelectionModel();
													if (selectionModel) {
														if (selectionModel
																.getSelections().length == 0) {
															selectionModel
																	.selectFirstRow();
														}
													}
												}
											}
										} ]);
						keyMap.stopEvent = true;
					});

		}// end init
	};// end of return
	// private functions
	// -----< onTreeEnterPressed >---------------------------------------
	function onTreeEnterPressed(keyCode, event) {

		event.stopEvent();
		target = event.getTarget();
		target.click();

	}

	// -----< onExpandNode >---------------------------------------------
	function onExpandNode(node) {
		var postparam = Ext.urlEncode({
			owNodeId : "" + node.id + "",
			collapsed : false
		});

		Ext.lib.Ajax.request("POST", treeCollapseURL, null, postparam);
	}
	// -----< onCollapseNode >-------------------------------------------
	function onCollapseNode(node) {
		var postparam = Ext.urlEncode({
			owNodeId : "" + node.id + "",
			collapsed : true
		});

		Ext.lib.Ajax.request("POST", treeCollapseURL, null, postparam);
	}
	// -----< setHeight >------------------------------------------------
	function setHeight(tree) {
		if (!isDynamicSplit) {
			var treeDiv = document.getElementById('extjs-tree-panel');
			tree.setHeight(parseInt(treeDiv.style.height));
		}
	}
	// -----< selectNodeOnReady >-----------------------------------------
	function selectNodeOnReady() {
		theTree = Ext.getCmp(treeId);
		selectNode(theTree);
	}
	// -----< selectNode >------------------------------------------------
	function selectNode(tree, index) {
		// var selectedNode = tree.getNodeById(selectedId);
		var node;
		i = index || 0;
		if (i == selectedPathIds.length - 1) {
			node = tree.getNodeById(selectedPathIds[i]);
			if (node != null) {
				node.ensureVisible();
				node.select();
				if (currentNodeExpanded) {
					node.expand();
				} else {
					node.collapse();
				}
			} else {
				tree.getRootNode().select();
			}
		} else {
			node = tree.getNodeById(selectedPathIds[i]);
			if (node) {
				node.expand(false, false, selectNode.createDelegate(this, [
						tree, ++i ]));
			}
		}
	}

	// -----< onNodeLoaded >------------------------------------------------
	function onNodeLoaded(node) {
		if (node) {
			node.ensureVisible();
		}
	}
	// -----< onTreeLoadException >-----------------------------------------
	function onTreeLoadException(treeLoader, node, response) {
		handleError(response, cannotLoadNodeText);
	}
	// -----< onBeforeSelectNode >------------------------------------------
	function onBeforeSelectNode(selModel, newNode, oldNode) {
		if (m_useIconClass) {
			changeNodeIconCls(oldNode, 'folder-open', 'folder-close');
			changeNodeIconCls(newNode, 'folder-close', 'folder-open');
		} else {
			changeNodeIcon(oldNode, false);
			changeNodeIcon(newNode, true);
		}
	}
	// -----< onBeforNodeClick >------------------------------------------------
	function onBeforeNodeClick(node, e) {
		theTree = Ext.getCmp(treeId);
		selectionModel = theTree.getSelectionModel();
		if (selectionModel) {
			lastSelectedNode = selectionModel.getSelectedNode();
		}
	}
	// -----< onNodeClick >------------------------------------------------
	function onNodeClick(node) {
		var mainId = "OwSubLayout_MAIN";

		if (m_listViewURL != null) {
			hideMenu();
			// hideErrorMessage();
			var treeElement = this.getEl();
			var theTree = this;
			var theTreeNode = node;
			var oldSelectedNode = theTree.getSelectionModel().getSelectedNode();
			// treeElement.mask();
			showMask();

			var ajaxUpdLogName = "Ajax Request - UPDATE region " + mainId;
			var updDomLogName = "Update DOM for region " + mainId;
			var updLayoutLogName = "Update Layout for region " + mainId;

			var requestConfig = {
				timeout : 300000,
				// disableCaching : false,
				success : function(o) {
					extJsLog.timeEnd(ajaxUpdLogName);
					extJsLog.time(updDomLogName);
					var contentDiv = document.getElementById(mainId);
					Ext.DomHelper.useDom = true;
					Ext.DomHelper.overwrite(contentDiv, o.responseText);
					extJsLog.timeEnd(updDomLogName);
					updateScripts(contentDiv);

					executeUpdateCalls(clientSideId);
					updateDnDApplet();

					extJsLog.time(updLayoutLogName);
					arrangeRecordHeight(clientSideId, true);
					if (typeof (window['OwObjectLayoutEXTJS']) != 'undefined') {
						OwObjectLayoutEXTJS.updateSize();
					}
					if (usesEXTJSGrid()) {
						gridPanelDiv = document.getElementById('grid-panel');
						if (gridPanelDiv) {
							OwObjectListViewEXTJSGrid.init();
							OwObjectListViewEXTJSGrid
									.setHeight(gridPanelDiv.style.height);
						}
					}
					setHeight(theTree);
					/* IE7 fix: empty div in IE7 has a certain height */
					fixMessageHeight(false);
					extJsLog.timeEnd(updLayoutLogName);

					//hideMask();
					return true;
				},
				failure : function(o) {
					extJsLog.timeEnd(ajaxUpdLogName);
					hideMask();
					handleError(o);
				}
			};
			var postparam = Ext.urlEncode({
				owNodeId : "" + node.id + ""
			});
			extJsLog.time(ajaxUpdLogName);
			Ext.lib.Ajax.request("POST", m_listViewURL, requestConfig,
					postparam);
		}
	}
	function updateDnDApplet() {
		var ajaxUpdLogName = "Ajax Request - UPDATE region DNDApplet";
		var updDomLogName = "Update DOM for region DNDApplet";

		if (null != DNDappletUpdateURL) {
			var requestConfig = {
				timeout : 30000,
				// disableCaching : false,
				success : function(o) {
					extJsLog.timeEnd(ajaxUpdLogName);
					extJsLog.time(updDomLogName);
					if (o.responseText) {
						var value = trim(o.responseText);
						var enabledValue = (/^true$/i).test(value);
						if(typeof DND != 'undefined') {
							var dndupload = document.getElementById('dndupload');
							if(dndupload) {
								DnDAppletEnableDrop(enabledValue);
							} else {
								if(enabledValue) {
									DND.enable();
								} else {
									DND.disable();
								}
							}
						}
					}
					extJsLog.timeEnd(updDomLogName);
				},
				failure : function(o) {
				}
			};

			extJsLog.time(ajaxUpdLogName);
			Ext.lib.Ajax.request("POST", DNDappletUpdateURL, requestConfig,
					null);
		}
	}
	function showMask() {
		var theMask = getMask();
		var isMasked = theMask.el.isMasked();
		if (!isMasked) {
			extJsLog.time('theMask.show');
			theMask.show();
			extJsLog.timeEnd('theMask.show');
			extJsLog.time('theMask');
		}
	}
	function hideMask() {
		var theMask = getMask();
		var isMasked = theMask.el.isMasked();
		if (isMasked) {
			extJsLog.timeEnd('theMask');
			extJsLog.time('theMask.hide');
			theMask.hide();
			extJsLog.timeEnd('theMask.hide');
		}
	}
	function getMask() {
		if (generalLoadingMask == null) {
			generalLoadingMask = new Ext.LoadMask(Ext.getBody(), {
				id : 'generalMask',
				msg : loadingMessage
			});
		}
		return generalLoadingMask;
	}
	// -----< handle error > -------------------
	function handleError(o, errorMsg) {
		if (trim(o.responseText) == 'owsessiontimeout') {
			document.location = m_mainURL;
		} else {
			if (errorWindow == null || !errorWindow.isVisible()) {
				if (errorMsg == null) {
					theMessage = requestTimeOutText;
				} else {
					theMessage = errorMsg;
				}
				if (o.responseText) {
					theMessage = eval(o.responseText);
				}
				myArray = null;
				if (Ext.isArray(theMessage)) {
					myArray = theMessage;
				}
				errorDisplay = null;
				if (myArray) {
					messagePanel = new Ext.Panel({
						region : 'north',
						autoScroll : true,
						layout : 'fit',
						height : 70,
						bodyCssClass : 'OwErrorStack',
						bodyStyle : 'font-weight:bold',
						html : myArray[0]
					});
					stackTracePanel = new Ext.Panel({
						region : 'center',
						autoScroll : true,
						title : showStacktraceMessage,
						collapsible : true,
						collapsed : true,
						bodyCssClass : 'OwErrorStack',
						html : '<pre>' + myArray[1] + '</pre>'
					});

					stackTracePanel.on('expand', onStacktraceExpand);
					stackTracePanel.on('collapse', onStacktraceCollapse);
					errorDisplay = new Ext.Panel({
						region : 'center',
						layout : 'border',
						items : [ messagePanel, stackTracePanel ]
					});

				} else {
					errorDisplay = new Ext.Panel({
						region : 'center',
						autoScroll : true,
						bodyCssClass : 'OwErrorStack',
						bodyStyle : 'font-weight:bold',
						html : theMessage
					});
				}

				panel1 = new Ext.Panel({
					border : false,
					id : 'p1',
					flex : 0.2
				});
				buttonOk = new Ext.Button({
					width : 50
				});
				panel2 = new Ext.Panel({
					border : false,
					id : 'p2',
					flex : 0.2
				});

				buttonOk.setText("Ok");
				buttonOk.on('click', closeErrorWindow);

				panelOk = new Ext.Panel({
					layout : new Ext.layout.HBoxLayout({
						align : 'middle'
					}),
					height : 30,
					region : 'south',
					items : [ panel1, buttonOk, panel2 ]
				});

				errorPanel = new Ext.Panel({
					region : 'center',
					layout : 'border',
					items : [ errorDisplay, panelOk ]
				});

				errorWindow = new Ext.Window({
					title : errorTitleText,
					closable : 'true',
					closeAction : 'hide',
					layout : 'border',
					width : 400,
					height : errorHeight,
					minWidth : 200,
					minHeight : errorHeight,
					modal : true,
					iconCls : 'ow-ext-error',
					items : [ errorPanel ]
				});
				errorWindow.show();

				if (lastSelectedNode) {
					theTree = Ext.getCmp(treeId);
					theTree.render();

					theTree.getSelectionModel().select(lastSelectedNode);
					lastSelectedNode.ensureVisible();
				}
			}
		}
	}

	function onStacktraceExpand() {
		if (errorWindow) {
			errorWindow.setHeight(400);
			this.setTitle(collapseStacktraceMessage);
		}
	}
	function onStacktraceCollapse() {
		if (errorWindow) {
			errorWindow.setHeight(errorHeight);
			this.setTitle(showStacktraceMessage);
		}
	}

	// -----< executeUpdateCalls > -------------------
	function executeUpdateCalls(clientSideId) {
		if (toBeUpdatedIds != null) {

			var numberOfUpdates = 0;
			for (i = 0; i < toBeUpdatedIds.length; i++) {
				var element = document.getElementById(toBeUpdatedIds[i]);
				if (element) {
					numberOfUpdates++;
				}
			}
			ajaxRequestsMonitor.init(numberOfUpdates);
			Ext.TaskMgr.start(ajaxRequestsMonitor);
			for (i = 0; i < toBeUpdatedIds.length; i++) {
				var element = document.getElementById(toBeUpdatedIds[i]);
				if (element) {
					delegate = executeUpdateCall.createDelegate(this, [
							clientSideId, element, toBeUpdatedURLs[i] ]);
					delegate.call();
				}
			}
		} else {
			hideMask();
		}
	}
	function executeUpdateCall(clientSideId, element, url) {
		showMask();
		var ajaxUpdLogName = "Ajax Request - UPDATE region " + element.id;
		var updDomLogName = "Update DOM for region " + element.id;
		var requestConfig = {
			success : function(responseObject) {
				extJsLog.timeEnd(ajaxUpdLogName);
				extJsLog.time(updDomLogName);
				if (element) {
					extEl = Ext.get(element.id);
					if (extEl && Ext.isIE) {
						extEl.update(responseObject.responseText, true);
					} else {
						Ext.DomHelper.useDom = true;
						Ext.DomHelper.overwrite(element,
								responseObject.responseText);
						updateScripts(element);
					}

					if (Ext.isIE7) {
						fixIE7EmptyDivs(extEl);
					}

					arrangeRecordHeight(clientSideId, true);
					if (typeof (window['OwObjectLayoutEXTJS']) != 'undefined') {
						OwObjectLayoutEXTJS.updateSize();
					}

				}
				ajaxRequestsMonitor.requestDone();
				extJsLog.timeEnd(updDomLogName);
			},
			error : function(responseObject) {
				extJsLog.timeEnd(ajaxUpdLogName);
				ajaxRequestsMonitor.requestDone();
				Ext.Msg.alert(errorTitleText, updateErrorText + "\n"
						+ o.responseText);
			}
		};
		extJsLog.time(ajaxUpdLogName);
		Ext.lib.Ajax.request("POST", url, requestConfig);
	}

	//============ The great hack of all times =================
	/**
	 * Fix for IE7 "empty DIVs" bug. 
	 * Basically we are trying to detect empty DIVs and hide them.
	 * 
	 * @param extEl An instance of Ext.Element representing the element that must be fixed. 
	 * 	Usually this will be a wrapper over a DIV.
	 */
	function fixIE7EmptyDivs(extEl) {
		//extEl.clean(true);
		var newDom = extEl.dom;
		var i = 0;
		var node;
		for (i = 0; i < newDom.childNodes.length; i++) {
			node = newDom.childNodes[i];
			// Type 8 means "Comment node" see: <http://www.w3schools.com/dom/dom_nodetype.asp>
			if (!node.hasChildNodes() || node.nodeType == 8) {
				newDom.removeChild(node);
			}
		}
		if (0 == newDom.children.length) {
			extEl.addClass("OwContainerInvisible");
		} else {
			extEl.removeClass("OwContainerInvisible");
		}
	}
	//============ The great hack of all times ends here =======		

	// -----< updateScripts > -------------------
	/**
	 * relink the scripts to the DOM
	 * 
	 * @param {Object}
	 *            contentDiv
	 */
	function updateScripts(contentDiv) {
		var theScripts = contentDiv.getElementsByTagName('script');
		for ( var i = 0; i < theScripts.length; i++) {
			eval(theScripts[i].innerHTML);
		}
	}
	// -----< changeNodeIconCls >----------------------------------------------
	function changeNodeIconCls(theTreeNode, oldClass, newClass) {
		if (theTreeNode) {
			iconElement = theTreeNode.ui.getIconEl();
			if (iconElement) {
				var el = Ext.get(iconElement);
				if (el) {
					el.removeClass(oldClass);
					el.addClass(newClass);
				}
			}
		}
	}
	// -----< changeNodeIcon >----------------------------------------------
	function changeNodeIcon(theTreeNode, useOpenIcon) {
		if (theTreeNode) {
			iconElement = theTreeNode.ui.getIconEl();
			if (useOpenIcon) {
				theTreeNode.attributes['icon'] = theTreeNode.attributes['openedIcon'];
			} else {
				theTreeNode.attributes['icon'] = theTreeNode.attributes['closedIcon'];
			}
			iconElement.src = theTreeNode.attributes['icon'];
		}
	}
	function closeErrorWindow() {
		if (errorWindow) {
			errorWindow.close();
		}
	}
}();
