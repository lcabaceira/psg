OwSubLayoutEXTJS = function() {
	var navigationPanelWidth = null;
	//var navigationPanelResizeURL = null;
	var panelCollapsedStateChangedURL = null;
	var navPanel = null;
	var mainPanel = null;
	var secondaryContent = null;
	return {
		secondaryPanel : secondaryContent,
		config : {},
		layoutSize : calculateSize,
		layoutMainPanel : mainPanel,
		init : function() {
			var subLayoutNavigation = document.getElementById('OwSubLayout_NAVIGATION');
			if (subLayoutNavigation != null) {
				subLayoutNavigation.style.visibility = "visible";
			}

			var subLayoutMain = document.getElementById('OwSubLayout_MAIN');
			if (subLayoutMain != null) {
				subLayoutMain.style.visibility = "visible";
			}

			var subLayoutSec = document.getElementById('OwSubLayout_SEC');
			if (subLayoutSec != null) {
				subLayoutSec.style.visibility = "visible";
			}
			if (subLayoutMain == null) {
				/*
				 * do nothing, no navigation pane or no OwSubLayout_Div_Panel no split!
				 * @return {TypeName} 
				 */
				return;
			}
			navigationPanelWidth = this.config.m_navigationPanelWidth;
			navigationPanelResizeURL = this.config.m_resizeURL;
			panelCollapsedStateChangedURL = this.config.m_PanelCollapsedStateChangedURL;
			var size = calculateSize();

			var mainContent = new Ext.Panel({
				id : 'mainContentPanel',
				region : 'center',
				contentEl : 'OwSubLayout_MAIN',
				autoscroll : true
			});
			var panelItems = [ mainContent ];

			if (subLayoutNavigation != null) {
				var isNavPanelCollapsed = subLayoutNavigation.getAttribute("data-collapsed");

				navPanel = new Ext.Panel({
					id : 'navContentPanel',
					region : 'west',
					contentEl : 'OwSubLayout_NAVIGATION',
					split : true,
					minSize : 150,
					maxSize : 500,
					width : navigationPanelWidth,
					collapsible : true,
					collapsed : (isNavPanelCollapsed == "true")
				});
				navPanel.on('expand', onPanelExpanded);
				navPanel.on('collapse', onPanelCollapsed);

				panelItems.push(navPanel);
			}

			if (subLayoutSec != null) {
				var isSecondaryCollapsed = document.getElementById('OwSubLayout_SEC').getAttribute("data-collapsed");

				secondaryContent = new Ext.Panel({
					id : 'secContentPanel',
					region : 'east',
					layout : 'fit',
					contentEl : 'OwSubLayout_SEC',
					split : true,
					collapsible : true,
					collapsed : (isSecondaryCollapsed == "true"),
					width : '40%',
					autoscroll : true
				});
				secondaryContent.on('expand', onPanelExpanded);
				secondaryContent.on('collapse', onPanelCollapsed);

				panelItems.push(secondaryContent);
			}

			//prevents flicker when properties are default-collapsed
			document.getElementById('OwSubLayout_Div_Panel').style.display = "none";
			
			mainPanel = new Ext.Panel({
				renderTo : 'OwSubLayout_Div_Panel',
				layout : 'border',
				items : panelItems,
				height : size[0],				
				width : size[1]
			});

			var mainId = document.getElementById('OwSubLayout_Div');
            if (mainId) {
                mainId.style.display = "none";
            }
            

            mainPanel.show();
			this.updateLayout();
			mainContent.on('resize', onMainContentPanelResize);
			if (navPanel != null) {
				navPanel.on('resize', onNavigationPanelResize);
			}
			
			if (navPanel) {
				var isNavPanelCollapsed = subLayoutNavigation
						.getAttribute("data-collapsed");
				if (isNavPanelCollapsed == "true") {
					navPanel.collapse();
				}
			}

			if (secondaryContent) {
				var isSecondaryCollapsed = document.getElementById(
						'OwSubLayout_SEC').getAttribute("data-collapsed");
				if (isSecondaryCollapsed == "true") {
					secondaryContent.collapse('right');
				}
			}
                
			// prevents flicker when properties are default-collapsed
			document.getElementById('OwSubLayout_Div_Panel').style.display = "block";
                
			Ext.EventManager.onWindowResize(onSublayoutResize);
			this.layoutMainPanel = mainPanel;
			this.secondaryPanel = secondaryContent;
		},
		updateLayout : function() {
			//resize once again because the height can be changed by the new content
			size = calculateSize();
			mainPanel.setHeight(size[0]);
			mainPanel.doLayout();
		}

	};//end return
	//------ calculate container size ---
	function calculateSize() {
		//var subLayoutTable = document.getElementById('OwSubLayout_Div_Panel');
		var subLayoutSec = document.getElementById('OwSubLayout_SEC');
		var subLayoutNavigation = document.getElementById('OwSubLayout_NAVIGATION');
		var subLayoutMain = document.getElementById('OwSubLayout_MAIN');
		var widthOrientation = document.getElementById('OwSubLayout_Div_Panel');

		var secHeight = 0;
		if (subLayoutSec != null) {
			secHeight = subLayoutSec.offsetHeight;
		}

		//Do not use height of OwSubLayout_Div_Panel -> Causes bug 4872
		//height = subLayoutTable.offsetHeight;//widthOrientation.offsetHeight-23;

		//calculate height by 'OwSubLayout_NAVIGATION' and 'OwSubLayout_MAIN'
		//height is the greater value of the offset height
		height = subLayoutNavigation == null ? 0
				: subLayoutNavigation.offsetHeight;
		if (secHeight > height) {
			height = secHeight;
		}
		if (subLayoutMain.offsetHeight > height) {
			height = subLayoutMain.offsetHeight;
		} else {
			//height = subLayoutNavigation.offsetHeight;
			height = height + 26; // add x-panel-header for roll in/out of navigation column
		}

		//handle problem of automatic height resizing by the extJS form elements
		//this problem occures, if standard html elements are rerendered by extJS to extJS form elements
		//this process sometimes starts after current size calculation
		//add 4px for every select element in subLayoutMain
		height = height	+ (subLayoutMain.getElementsByTagName('select').length * 4);

		if (height < 400) {
			height = 400;
		}

		var width = widthOrientation.offsetWidth - 4;
		return [ height, width ];
	}
	//---------on resize------------------
	function onNavigationPanelResize(component, adjWidth, adjHeight, rawWidth, rawHeight) {
		//alert('New width: ' + adjWidth);
		var requestConfig = {
			timeout : 30000,
			//disableCaching : false,
			success : function(o) {

			},
			failure : function(o) {

			}
		};
		shouldNotify = true;
		var postparam = null;
		if (component == navPanel) {

			postparam = Ext.urlEncode({
				width : adjWidth
			});
		} else {
			shouldNotify = true;
			postparam = Ext.urlEncode({
				height : adjHeight
			});
		}
		if (shouldNotify) {
			Ext.lib.Ajax.request("POST", navigationPanelResizeURL,
					requestConfig, postparam);
		}
		size = calculateSize();
		mainPanel.setHeight(size[0]);
		onSublayoutResize();
	}

	function onSublayoutResize() {
		var mainLayout = document.getElementById('OwSubLayout_Div_Panel');
		mainPanel.setWidth(mainLayout.offsetWidth - 4);
		
		mainPanel.doLayout();
		updateGridComponent();
	}
	function updateGridComponent() {
		grid = Ext.getCmp('owgrid');
		if (grid) {
			gridPanel = document.getElementById('grid-panel');
			grid.setHeight(gridPanel.offsetHeight);
			grid.setWidth(gridPanel.offsetWidth);
		}
	}
	function onMainContentPanelResize() {
		updateGridComponent();
	}
	function onPanelCollapsed(p) {
		var requestConfig = {
			timeout : 30000,
			//disableCaching : false,
			success : function(o) {

			},
			failure : function(o) {

			}
		};
		var postparam = Ext.urlEncode({
			panelId : p.getId(),
			collapsedState : 'true'
		});
		Ext.lib.Ajax.request("POST", panelCollapsedStateChangedURL,
				requestConfig, postparam);
	}
	function onPanelExpanded(p) {
		var requestConfig = {
			timeout : 30000,
			//disableCaching : false,
			success : function(o) {

			},
			failure : function(o) {

			}
		};
		var postparam = Ext.urlEncode({
			panelId : p.getId(),
			collapsedState : 'false'
		});
		Ext.lib.Ajax.request("POST", panelCollapsedStateChangedURL,
				requestConfig, postparam);
	}
}();

Ext.EventManager.on(window, 'load', function() {
	if (typeof (window['OwSubLayoutEXTJS']) != 'undefined'
			&& typeof (window['configLayout']) != 'undefined') {
		configLayout();
		OwSubLayoutEXTJS.init();
	}
});