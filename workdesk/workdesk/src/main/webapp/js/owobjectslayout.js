OwObjectLayoutEXTJS = function() {
	var navigationElementId;
	var mainPanel;
	var navigationPanelInitialWidth;
	var navigationPanelResizeURL;
	var previewPropPanelInitialHeight;
	var navPanel;
	var propPreviewPanel;
	var isIEHeightFixed = false;
	var treePanel;
	var treeComp;
	return {
		config : {},

		init : function() {
			// register window resize
			Ext.EventManager.onWindowResize(onSublayoutResize);
			navigationElementId = this.config.m_clientSideId;
			navigationPanelInitialWidth = this.config.m_navigationPanelInitialWidth;
			previewPropPanelInitialHeight = this.config.m_previewPropPanelInitialHeight;
			navigationPanelResizeURL = this.config.m_navigationPanelResizeURL;
			panelCollapsedStateChangedURL = this.config.m_PanelCollapsedStateChangedURL;
			isPropPreviewPanelCollapsed = this.config.m_propPreviewPanelCollapsed;
			isNavPanelCollapsed = this.config.m_navPanelCollapsed;
			arrangeRecordHeight(navigationElementId);
			var size = calculateSize();
			// arrangeRecordHeight(navigationElementId);
			var maxSplitterSize = size[1] * 2 / 3;
			propPreviewPanelVisible = false;
			propPreviewPanelElement = document.getElementById('OwRecordPreviewPropertyView');
			if (propPreviewPanelElement != null) {
				header = document.getElementById('OwMainColumnHeader_PreviewPropertyView');
				if (header) {
					header.style.display = "none";
				}
				propPreviewPanelVisible = true;
			} else {
				propPreviewPanelElement = document.getElementById('OwMainColumnHeader_PreviewPropertyView');
			}
			if (propPreviewPanelElement) {
				propPreviewPanelHeight = calculatePreviewPropsHeight(propPreviewPanelElement, size[0]);
				propPreviewPanel = new Ext.Panel({
					id : 'propPreviewContentPanel',
					contentEl : propPreviewPanelElement,
					region : 'north',
					split : true,
					collapsible : true,
					autoScroll : true,
					height : propPreviewPanelHeight,
					collapsed : isPropPreviewPanelCollapsed
				});
				propPreviewPanel.setVisible(propPreviewPanelVisible);
				var treeElement = document.getElementById(navigationElementId);
				var treeElementInPanel = document.getElementById('extjs-tree-panel');
				if (treeElementInPanel == null) {
					treeElementInPanel = treeElement;
				} else {
					treeComp = getTreeComponent();
				}
				if (treeComp != null) {
					treePanel = new Ext.Panel({
						region : 'center',
						layout : 'fit',
						minSize : 100,
						items : [ treeComp ]
					});

				} else {
					treePanel = new Ext.Panel({
						contentEl : treeElementInPanel,
						region : 'center',
						minSize : 100
					});
				}
				if (navigationPanelInitialWidth > size[1]) {
					navigationPanelInitialWidth = size[1] / 4;
				}
				navPanel = new Ext.Panel({
					id : 'navContentPanel',
					region : 'west',
					contentEl : 'OwSubLayout_NAVIGATION',
					split : true,
					width : navigationPanelInitialWidth,
					collapsible : true,
					layout : 'border',
					items : [ propPreviewPanel, treePanel ],
					minSize : 100,
					maxSize : maxSplitterSize,
					collapsed : isNavPanelCollapsed
				});
				var gridObjects = new Ext.Panel({
					region : 'center',
					contentEl : 'OwSubLayout_MAIN'
				});

				var mainLayout = document.getElementById('OwSubLayout_Div_Panel');
				mainPanel = new Ext.Panel({
					id : 'mainPanel',
					renderTo : 'OwSubLayout_Div_Panel',
					layout : 'border',
					items : [ navPanel, gridObjects ],
					height : mainLayout.clientHeight
					// ,width : size[1]
				});
				var mainId = document.getElementById('OwSubLayout_Div');
				if (mainId) {
					mainId.style.display = "none";
				}
				gridObjects.on('resize', onGridObjectsResize);
				mainPanel.show();
				navPanel.on('resize', onNavigationPanelResize);
				navPanel.on('expand', onPanelExpanded);
				navPanel.on('collapse', onPanelCollapsed);
				propPreviewPanel.on('resize', onNavigationPanelResize);
				propPreviewPanel.on('expand', onPanelExpanded);
				propPreviewPanel.on('collapse', onPanelCollapsed);
				calculateTreeHeightBasedOnPropertyPreviewHeight();
				// Ext.onReady(ensureVisibleSelectedNode);
			} else {
				navPanel = new Ext.Panel({
					id : 'navContentPanel',
					region : 'west',
					contentEl : 'OwSubLayout_NAVIGATION',
					split : true,
					width : navigationPanelInitialWidth,
					collapsible : true,
					minSize : 100,
					maxSize : maxSplitterSize,
					collapsed : isNavPanelCollapsed
				});
				var gridObjects = new Ext.Panel({
					region : 'center',
					contentEl : 'OwSubLayout_MAIN'
				});

				var mainLayout = document.getElementById('OwSubLayout_Div_Panel');
				mainPanel = new Ext.Panel({
					id : 'mainPanel',
					renderTo : 'OwSubLayout_Div_Panel',
					layout : 'border',
					items : [ navPanel, gridObjects ],
					height : mainLayout.clientHeight
					// ,width : size[1]
				});
				var mainId = document.getElementById('OwSubLayout_Div');
				if (mainId) {
					mainId.style.display = "none";
				}
				mainPanel.show();
				navPanel.on('expand', onPanelExpanded);
				navPanel.on('collapse', onPanelCollapsed);
			}
		}, // end init
		updateSize : function() {
			var mainLayout = document.getElementById('OwSubLayout_Div_Panel');
			var mainPanel = Ext.getCmp('mainPanel');
			mainPanel.setHeight(mainLayout.clientHeight);
			mainPanel.doLayout();
		},
		ensureVisibleSelectedNode : function() {
			Ext.onReady(makeSelectedNodeVisible = function() {
				treeComp = getTreeComponent();
				// ensure selected node is visible
				if (treeComp != null) {
					selectedNode = treeComp.getSelectionModel().getSelectedNode();
					if (selectedNode != null) {
						selectedNode.ensureVisible();
					}
				}
			});
		}
	};// end return
	// ------ calculate container size ---
	function calculateSize() {
		var subLayoutNavigation = document.getElementById('OwSubLayout_NAVIGATION');
		subLayoutNavigation.style.visibility = "visible";
		var subLayoutMain = document.getElementById('OwSubLayout_MAIN');
		subLayoutMain.style.visibility = "visible";
		var subLayoutTable = document.getElementById('OwSubLayout_Div_Panel');
		var mainLayout = document.getElementById('OwMainLayout_MAIN');
		var subLayoutMenu = document.getElementById('OwSubLayout_menu');
		if (mainLayout && subLayoutMenu) {
			height = mainLayout.offsetHeight - subLayoutMenu.offsetHeight;
		} else {
			height = subLayoutTable.offsetHeight;// mainLayout.offsetHeight-23;
		}
		var width = mainLayout.offsetWidth - 4;
		return [ height, width ];
	}
	function getTreeComponent() {
		componentId = navigationElementId + 'cmp';
		treeComp = Ext.getCmp(componentId);
		return treeComp;
	}
	function onGridObjectsResize(component, adjWidth, adjHeight, rawWidth, rawHeight) {
		// alert('resize!');
		var gridComponent = Ext.getCmp('owgrid');
		if (gridComponent) {
			gridComponent.setWidth(adjWidth);
		}
	}
	function onNavigationPanelResize(component, adjWidth, adjHeight, rawWidth, rawHeight) {
		// alert('New width: ' + adjWidth);
		var requestConfig = {
			timeout : 30000,
			// disableCaching : false,
			success : function(o) {

			},
			failure : function(o) {

			}
		};
		var shouldNotify = true;
		var postparam = null;
		if (component == navPanel) {

			postparam = Ext.urlEncode({
				width : adjWidth
			});
			// only when manually resized, not when the propPreviewPanel is
			// slideOut by clicking on it's toolbar when it is collapsed.
			if (adjHeight) {
				calculateTreeHeightBasedOnPropertyPreviewHeight();
			}
		} else if ((propPreviewPanel != null) && (component == propPreviewPanel)) {
			shouldNotify = true;
			postparam = Ext.urlEncode({
				height : adjHeight
			});
			// only when manually resized, not when the propPreviewPanel is
			// slideOut by clicking on it's toolbar when it is collapsed.
			if (adjHeight) {
				calculateTreeHeightBasedOnPropertyPreviewHeight();
			}
		} else {
			shouldNotify = true;
			postparam = Ext.urlEncode({
				height : adjHeight
			});
		}
		if (shouldNotify) {
			Ext.lib.Ajax.request("POST", navigationPanelResizeURL, requestConfig, postparam);
		}
	}
	function calculatePreviewPropsHeight(propPreviewPanelElement, parentHeight) {
		propPreviewPanelHeight = -1;
		if (propPreviewPanelElement) {
			propPreviewPanelHeight = propPreviewPanelElement.offsetHeight + 26;

			if (propPreviewPanelHeight > parentHeight / 2 - 26) {
				propPreviewPanelHeight = parentHeight / 2 - 26;
			}
			if (Ext.isIE) {
				propPreviewPanelHeight += 2;
			}
			// if (Ext.isIE) {
			// propPreviewPanelElement.style['overflowY'] = 'hidden';
			// if (!isIEHeightFixed) {
			// propPreviewPanelHeight += 17;
			// propPreviewPanelElement.style['height'] =
			// propPreviewPanelElement.offsetHeight + 15;
			// isIEHeightFixed = true;
			// }
			// }
			/* if restore should be made */
			if (previewPropPanelInitialHeight != -1 && previewPropPanelInitialHeight < parentHeight - 100) {
				propPreviewPanelHeight = previewPropPanelInitialHeight;
			}
		}
		return propPreviewPanelHeight;
	}
	function onSublayoutResize(viewWidth, viewHeight) {
		arrangeRecordHeight(navigationElementId, true);
		mainLayout = document.getElementById('OwSubLayout_Div_Panel');
		mainPanel = Ext.getCmp('mainPanel');
		// mainPanel.setWidth(mainLayout.offsetWidth);
		mainPanel.setHeight(mainLayout.clientHeight);
		mainPanel.doLayout();
		grid = Ext.getCmp('owgrid');
		if (grid) {
			gridPanel = document.getElementById('grid-panel');
			grid.setHeight(gridPanel.offsetHeight);
			grid.setWidth(gridPanel.offsetWidth);
		}
	}
	function calculateTreeHeightBasedOnPropertyPreviewHeight() {
		var nPanSize = navPanel.getHeight();
		var treeD = (!treeComp) ? document.getElementById(navigationElementId) : document.getElementById(navigationElementId + 'cmp');
		var propPreviewPanelCollapsed = propPreviewPanel.collapsed;
		if (propPreviewPanelCollapsed) {
			// 28 px---navigation panel toolbar, 22 px---the toolbar of the
			// preview properties when collapsed, 8 px---padding for the tree
			// inside tree panel
			treeD.style.height = (nPanSize - 28 - 22 - 8 - 5 - 1) + "px";
			treePanel.setHeight(nPanSize - 28 - 22 - 5 - 1);
		} else {
			var pPanSize = propPreviewPanel.getHeight();
			var diff = nPanSize - pPanSize;
			// 28 px---the toolbar of the preview properties, 5 px---the bar
			// between the preview properties view and tree view, 8 px---padding
			// for the tree inside tree panel
			treeD.style.height = (diff - 28 - 5 - 8) + "px";
			treePanel.setHeight(diff - 28 - 5);
		}
		treePanel.doLayout();
	}
	function onPanelCollapsed(p) {
		var requestConfig = {
			timeout : 30000,
			// disableCaching : false,
			success : function(o) {

			},
			failure : function(o) {

			}
		};
		var postparam = Ext.urlEncode({
			panelId : p.getId(),
			collapsedState : 'true'
		});
		Ext.lib.Ajax.request("POST", panelCollapsedStateChangedURL, requestConfig, postparam);
		if ((propPreviewPanel != null) && (p == propPreviewPanel)) {
			calculateTreeHeightBasedOnPropertyPreviewHeight();
		}
	}
	function onPanelExpanded(p) {
		var requestConfig = {
			timeout : 30000,
			// disableCaching : false,
			success : function(o) {

			},
			failure : function(o) {

			}
		};
		var postparam = Ext.urlEncode({
			panelId : p.getId(),
			collapsedState : 'false'
		});
		Ext.lib.Ajax.request("POST", panelCollapsedStateChangedURL, requestConfig, postparam);
	}
}();