// Create extensions namespace
Ext.namespace('Ext.alfresco.owd.ux');

Ext.alfresco.owd.ux.InfinitePagingToolbar = function(config) {
    // call parent constructor
	Ext.alfresco.owd.ux.InfinitePagingToolbar.superclass.constructor.call(this, config);
};
 
// extend
Ext.extend(Ext.alfresco.owd.ux.InfinitePagingToolbar, Ext.PagingToolbar, {
	onPagingKeyDown : function(field, e){
        var k = e.getKey(), d = this.getPageData(), pageNum;
        if (k == e.RETURN) {
            e.stopEvent();
            pageNum = this.readPage(d);
            if(pageNum !== false){
            	// We might not know the size of the result set.
           		pageNum = Math.max(1, pageNum) - 1;
           		this.doLoad(pageNum * this.pageSize);
            }
        }else if (k == e.HOME || k == e.END){
            e.stopEvent();
            pageNum = k == e.HOME ? 1 : d.pages;
            field.setValue(pageNum);
        }else if (k == e.UP || k == e.PAGEUP || k == e.DOWN || k == e.PAGEDOWN){
            e.stopEvent();
            if((pageNum = this.readPage(d))){
                var increment = e.shiftKey ? 10 : 1;
                if(k == e.DOWN || k == e.PAGEDOWN){
                    increment *= -1;
                }
                pageNum += increment;
                if(pageNum >= 1 & pageNum <= d.pages){
                    field.setValue(pageNum);
                }
            }
        }
    },

	doRefresh : function() {
		var start = this.cursor;
		var o = {}, pn = this.getParams();
        o[pn.start] = start;
        o[pn.limit] = this.pageSize;
        o['refresh'] = true;
        if(this.fireEvent('beforechange', this, o) !== false){
            this.store.load({params:o});
        }
	}
}); // end of extend  

OwObjectListViewEXTJSGrid = function()
{

    /***************************************************************************
    ** P R I V A T E   V A R I A B L E S                                      **
    ***************************************************************************/
    var m_DataStore;
    var m_Grid;
    var m_gridContainer;
    var m_containerHeight;
    var m_Contextmenu;
    var m_FilterContextItem;
    var m_fields_enumDataStores = [];
    var m_DelayedEditorTask;
    var m_url_getColumnInfo;
    var m_url_readlist;
    var m_usefilters;
    var m_pluginurl;
    var m_editfilterurl;
    var m_contextmenuurl;
    var m_setcolumninfourl;
    var m_setcelldataurl;
    var m_dateformat;
    var m_decimalseparator;
    var m_startrow;
    var m_pagesize;
    var m_iconfilter;
    var m_iconfilteractive;
    var m_strPagingDisplayMsg;
    var m_strPagingEmptyMsg;
    var m_strPagingBeforePageText;
    var m_strPagingAfterPageText;
    var m_strPagingFirstText;
    var m_strPagingPrevText;
    var m_strPagingNextText;
    var m_strPagingLastText;
    var m_strPagingRefreshText;
    var m_showPaging;
    var m_loadingMessage;
    var m_gridMask;
    var currentColumnModel;
    var sortInfo;
    var oldColumns;
    var shouldReconfigure;
    
    var m_owIconColumnId; // the ID of the column with the plugins icons. Could be null.
    var m_dataJsonReader;
    //var m_mainUrl;
    
    //-----< return the OwObjectListViewEXTJSGrid class >-----------------------
    return {
    
        config : {initialized:false},

        init : function()
        {
            //-----< copy config into member vars >-----
        	m_mainUrl=this.config.main_url;
        	m_listID=this.config.list_id;
        	m_url_persistSelection=this.config.url_persistSelection;
            m_url_getColumnInfo = this.config.url_getColumnInfo;
            m_url_readlist = this.config.url_readlist;
            m_usefilters = this.config.usefilters;
            m_pluginurl = this.config.pluginurl;
            m_editfilterurl = this.config.editfilterurl;
            m_contextmenuurl = this.config.contextmenuurl;
            m_setcolumninfourl = this.config.setcolumninfourl;
            m_setcelldataurl = this.config.setcelldataurl;
            m_dateformat = this.config.dateformat;
            m_decimalseparator = this.config.decimalseparator;
            m_startrow = this.config.startrow;
            m_pagesize = this.config.pagesize;
            m_iconfilter = this.config.iconfilter;
            m_iconfilteractive = this.config.iconfilteractive;
            m_strPagingDisplayMsg = this.config.strPagingDisplayMsg;
            m_strPagingEmptyMsg = this.config.strPagingEmptyMsg;
            m_strPagingBeforePageText = this.config.strPagingBeforePageText;
            m_strPagingAfterPageText = this.config.strPagingAfterPageText;
            m_strPagingFirstText = this.config.strPagingFirstText;
            m_strPagingPrevText = this.config.strPagingPrevText;
            m_strPagingNextText = this.config.strPagingNextText;
            m_strPagingLastText = this.config.strPagingLastText;
            m_strPagingRefreshText = this.config.strPagingRefreshText;
            m_showPaging = this.config.showPaging;
            m_loadingMessage = this.config.loadingMessage;
            m_singleSelection = this.config.singleSelect;
			//-----< init tooltips >-----
            Ext.QuickTips.init();		
            Ext.apply(Ext.QuickTips.getQuickTip(), {
            	dismissDelay : 4000,
            	showDelay:1000
            });


            //-----< create new HttpProxy >-----
            var Fields_HttpProxy = new Ext.data.HttpProxy({url:m_url_getColumnInfo});

            // what we are doing here:
            // This grid loads its configuration dynamically from the server.
            // So we create a DataStore for the list of fields with an Json reader.
            // We set a function for the 'load' event which gets called after the
            // field list arrived.
            // In that function, we create the main data store and the grid depending
            // on the field list.

            //-----< define records of fields data >-----
            var Fields_RecordDefinition = [
                                            {name:'displayName',   mapping: 'display_name'},
                                            {name:'proposedWidth', mapping: 'proposed_width'},
                                            {name:'canEdit',       mapping: 'can_edit'},
                                            {name:'canFilter',     mapping: 'can_filter'},
                                            {name:'filterActive',  mapping: 'filter_active'},
                                            {name:'editDatatype',  mapping: 'edit_datatype'},
                                            {name:'editRequired',  mapping: 'edit_required'},
                                            {name:'editMaxvalue',  mapping: 'edit_maxvalue'},
                                            {name:'editMinvalue',  mapping: 'edit_minvalue'},
                                            {name:'enumInfoUrl',     mapping: 'enum_info_url'},
                                            {name:'sortDir',     mapping: 'sort_dir'},
                                            {name:'sortable',      mapping: 'sortable'},
                                            {name:'filterTooltip',mapping: 'filter_tooltip'}
                                          ];

            //-----< create new JsonReader >-----
            var Fields_JsonReader = new Ext.data.JsonReader( {
                                                               root          : 'field_defs',
                                                               totalProperty : 'count',
                                                               id            : 'id'
                                                             },
                                                             Fields_RecordDefinition
                                                           );

            //-----< create a new DataStore >-----
            var Fields_DataStore = new Ext.data.Store( {
                                                         proxy      : Fields_HttpProxy,
                                                         reader     : Fields_JsonReader,
                                                         remoteSort : true
                                                       }
                                                     );

            //-----< add listeners to datastore >-----
            Fields_DataStore.addListener('load',onFieldsStoreLoad);
            Fields_DataStore.addListener('loadexception',function()
            {
            	 //Ext.Msg.alert("Error","Error loading column and field definitions from server.");
            });

            //-----< initiate async data loading >-----
            // add a 'fakeparam'. This will force ExtJS to use the POST method instead of the GET method.
            // Due to a bug in IE, GET method calls are sometimes spuriously cached. Sigh!
            Fields_DataStore.load({params:{fakeparam:0}});
            
        }, // end of function 'init'
        setHeight:function(height) {
        	if (m_Grid) {
        		m_Grid.setHeight(parseInt(height));//just for a better look
        	}
        },
        selectAll : function()
        {
            // sanity check
            if(m_Grid==null)
            {
                return;
            }
            // get the selection model
            var selmod = m_Grid.getSelectionModel();
            // sanity check
            if(selmod==null)
            {
                return;
            }
            if( m_DataStore.getCount() == selmod.getCount() )
            {
                selmod.clearSelections();
            }
            else
            {
                selmod.selectAll();
            }
        }, // end of function 'selectAll'
        
        nextPage : function()
        {
            grid = Ext.getCmp('owgrid');
			if (grid) {
				bb = grid.getBottomToolbar(); 
				if (bb && bb.isVisible()) {
					if (grid.getStore().getTotalCount() > 0)
					{
						activePage = Math.ceil((bb.cursor + bb.pageSize) / bb.pageSize);
						maxNumberOfPages = Math.ceil(grid.getStore().getTotalCount()/bb.pageSize);
						if (activePage<maxNumberOfPages) {
							bb.moveNext();
						}
					}
					else
					{
						if (grid.getStore().reader.jsonData.canPageNext) {
							bb.moveNext();
						}
					}
				}
			}
        }, // end of function 'nextPage'
        
        previousPage : function()
        {
    		grid = Ext.getCmp('owgrid');
			if (grid) {
				bb = grid.getBottomToolbar(); 
				if (bb && bb.isVisible()) {
					bb.movePrevious();
				}
			}
        }, // end of function 'previousPage'
        
        firstPage : function(key, event)
        {
        	event.preventDefault();
    		grid = Ext.getCmp('owgrid');
			if (grid) {
				bb = grid.getBottomToolbar(); 
				if (bb && bb.isVisible()) {
					bb.moveFirst();
				}
			}
        }, // end of function 'firstPage'
        
        lastPage : function(key, event)
        {
        	event.preventDefault();
    		grid = Ext.getCmp('owgrid');
			if (grid) {
				bb = grid.getBottomToolbar(); 
				if (bb && bb.isVisible()) {
					bb.moveLast();
				}
			}
        }, // end of function 'lastPage'
        
        firstRow : function()
        {
        	grid = Ext.getCmp('owgrid');
			if (grid) {
				selectionModel = grid.getSelectionModel();
				if (selectionModel) {
					grid.stopEditing();
					selectionModel.clearSelections();
					selectionModel.selectFirstRow();
				}
			}
        }, // end of function 'firstRow'
        
        lastRow : function()
        {
        	grid = Ext.getCmp('owgrid');
			if (grid) {
				selectionModel = grid.getSelectionModel();
				if (selectionModel) {
					grid.stopEditing();
					selectionModel.clearSelections();
					selectionModel.selectLastRow();
				}
			}
        }, // end of function 'lastRow'
        
        pluginEvent : function(pluginid)
        {
            var selection = m_Grid.getSelectionModel().getSelections();
            if(pluginid && selection && (selection.length > 0))
            {
                var selectedlist = "";
                for(var i = 0; i < selection.length; i++)
                {
                    if(i > 0)
                    {
                        selectedlist = selectedlist + ",";
                    }
                    selectedlist = selectedlist + selection[i].id;
                }
                window.location.href = appendToUrl(m_pluginurl,"&object=" + selectedlist + "&plugin=" + pluginid);
            }
        }// end of function 'pluginEvent'	
    }; // end of return

    /***************************************************************************
    ** P R I V A T E   F U N C T I O N S                                      **
    ***************************************************************************/

    //-----< onFieldsStoreLoad >------------------------------------------------
    function onFieldsStoreLoad(store,records,options)
    {
        // get count
        var cnt = store.getCount();
        // array for column model
        var ColumnModel_Cols = [];
        // array for record definition
        var RecordDefinition = [];
        // static record definition
        RecordDefinition[0] = { name : "icon",             mapping : "icon_link" };
        RecordDefinition[1] = { name : "version",          mapping : "version_str" };
        RecordDefinition[2] = { name : "coicon",           mapping : "checkout_icon" };
        RecordDefinition[3] = { name : "plugins",          mapping : "plugin_links" };
        RecordDefinition[4] = { name : "tooltip",          mapping : "tooltip" };
        RecordDefinition[5] = { name : "defaultAction",    mapping : "default_action" };
        RecordDefinition[6] = { name : "defaultNewWindow", mapping : "default_newwindow" };
        RecordDefinition[7] = { name : "rowClassName",     mapping : "row_class_name" };
        RecordDefinition[8] = { name : "lockedicon",     mapping : "locked_icon" };
        RecordDefinition[9] = { name : "rowSelected",     mapping : "row_selected" };

        var iconwidth = 19;
        
        // append columns
        for(var i = 0; i < cnt; i++)
        {
            var col = store.getAt(i);
            // create icon column
            if(col.get("displayName") == "ow_icon_column")
            {
            	if (col.json.calculate)
            	{
            		iconwidth = iconwidth * col.get("proposedWidth");
            		iconwidth += 2 * 16; // icon and checked out/not checked out icon
            		iconwidth += 75; // version String, CMIS (pwc)
            		iconwidth += 16; // MIME type icon
            		//iconwidth += 16; //space for padding
            	}
            	else
                {
            		iconwidth = col.get("proposedWidth");
            	}
            	m_owIconColumnId = col.id;
            	ColumnModel_Cols[i] = { id  : col.id,
                                header      : "", // don't show display name
                                dataIndex   : "icon",
                                width       : iconwidth,
                                sortable    : false,
                                menuDisabled: true,
                                renderer    : fieldRendererIcon };
            }
            else
            {
            	var result = processColumn(col);

                ColumnModel_Cols[ColumnModel_Cols.length] = result.coldata;
                RecordDefinition[RecordDefinition.length] = result.recdata;
            }
        }
        
        // ColumnModel
        var ColumnModel = new Ext.grid.ColumnModel(ColumnModel_Cols);
        shouldReconfigure = true;
       /* clonedCopies = new Array(ColumnModel_Cols.length);
        for (idx=0;idx<ColumnModel_Cols.length;idx++) {
        	clonedCopies[idx] = ColumnModel_Cols[idx];
        }
        if (oldColumns != null) {
        	if (oldColumns.length!=clonedCopies.length) {
        		shouldReconfigure = true;
        	} else {
        		for (idx=0;idx<oldColumns.length;idx++) {
        			if (oldColumns[idx].header!=clonedCopies[idx].header) {
        				shouldReconfigure = true;
        				break;
        			}
        		}
        	}
        }
        //save a fresh copy
        oldColumns = clonedCopies;
        
        currentColumnModel = new Ext.grid.ColumnModel(clonedCopies);*/
        currentColumnModel = ColumnModel;
        // create datasource
        m_dataJsonReader = new Ext.data.JsonReader( {root: 'items', totalProperty: 'count', id: 'id'}, RecordDefinition);
        m_DataStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: m_url_readlist}),
            // create reader that reads the Topic records
            reader: m_dataJsonReader,
            // turn on remote sorting
            remoteSort: true
        });
        if (sortInfo != null) {
        	m_DataStore.setDefaultSort(sortInfo[0],sortInfo[1]);
        }
        
        m_gridMask = new Ext.LoadMask(Ext.getBody(), {msg:m_loadingMessage});
        
        if (m_Grid!=null)
        {
           var id = m_Grid.getStateId();
           Ext.state.Manager.set(id,null);
        }

        //create tool-bar at bottom
        var pagingBar = createPagingbar();
        pagingBar.addListener('change', onPageChange);
        // create the grid
        m_Grid = createGrid(ColumnModel, pagingBar);
        //define selection model
        m_Grid.selModel = createSelectionModel(m_singleSelection);
        m_Grid.selModel.setMainUrl(m_mainUrl);

        m_Grid.keys = getKeyEventHandler();
       
        m_Grid.keys.stopEvent = true;
        
        m_Grid.render('grid-panel');
        m_Grid.addListener('sortchange',onSortChange);
        m_Grid.addListener('columnmove',onGridColumnMove);
        m_Grid.addListener('columnresize',onGridColumnResize);
        m_Grid.addListener('beforeedit',onGridBeforeEdit);
        m_Grid.addListener('afteredit',onGridAfterEdit);
        m_Grid.removeListener('cellclick',m_Grid.onCellDblClick);
        m_Grid.removeListener('celldblclick',m_Grid.onCellDblClick);
        m_Grid.addListener('cellclick',onGridCellClick);
        m_Grid.addListener('celldblclick',onGridCellDblClick);
        m_Grid.addListener('viewready',onGridViewReady);
        var panelDiv = document.getElementById('grid-panel');
        m_Grid.setHeight(parseInt(panelDiv.style.height));
        // get the column menu object from the grid and append our own filter menu item
        if(m_usefilters)
        {
            var gridview = m_Grid.getView();
            if(gridview)
            {
                var headermenu = gridview.hmenu;
                m_FilterContextItem = new Ext.menu.Item({id:"filter",text:"Filter",handler:onFilterContextmenuClick});
                if(headermenu)
                {
                    headermenu.add("separator",m_FilterContextItem); 
                }
                headermenu.addListener('beforeshow',onFilterContextmenuBeforeShow);
            }
        }
        m_DataStore.on('exception',onDataStoreException);
        m_DataStore.load({params:{start:m_startrow, limit:m_pagesize},callback:onDataLoaded});
    }
    //-----< reconfigureGrid >--------------------------------------------------
    function reconfigureGrid(cm) {
    	if (sortInfo!=null) {
         	m_DataStore.setDefaultSort(sortInfo[0],sortInfo[1]);
    	}
    	m_Grid.reconfigure(m_DataStore, cm);
    	var bottomToolbar = m_Grid.getBottomToolbar();
    	if (bottomToolbar!=null) {
    		bottomToolbar.bindStore(m_DataStore,true);
    		if(m_showPaging) {
    			bottomToolbar.show();
    		} else {
    			bottomToolbar.hide();
    		}
    	}
    }
    //-----< onFilterContextmenuBeforeShow >-------------------------------------
    function onFilterContextmenuBeforeShow(menu)
    {
        var gridview = m_Grid.getView();
        var cm = m_Grid.getColumnModel();
        if(gridview)
        {
            var index = gridview.hdCtxIndex;
            var filterable = cm.config[index].can_filter;
            m_FilterContextItem.disabled = !filterable;
        }
    }
    
    //-----< onFilterContextmenuClick >-----------------------------------------
    function onFilterContextmenuClick(menuitem)
    {
        var gridview = m_Grid.getView();
        var cm = m_Grid.getColumnModel();
        if(gridview)
        {
            var index = gridview.hdCtxIndex;
            var filterable = cm.config[index].can_filter;
            if(filterable)
            {
                var colid = gridview.getColumnId(index);
                window.location.href = appendToUrl(m_editfilterurl,"&fprop=" + colid);
            }
        }
    }
    
    //-----< onSortChange >-------------------------------------------------
    function onSortChange(grid,sortInfo)
    {
    	if (m_Grid==null)
    	{
    		return;
    	}
    	
    	var selmod = m_Grid.getSelectionModel();
        // sanity check
        if(selmod==null)
        {
        	return;
        }
        
		selmod.clearSelections();
    }
    
    //-----< onGridColumnMove >-------------------------------------------------
    function onGridColumnMove(oldindex,newindex)
    {
        var paramstr = "";
        var cm = m_Grid.getColumnModel();
        for(var i=0; i < cm.getColumnCount(); i++)
        {
            if(paramstr.length > 0)
            {
                paramstr = paramstr + ",";
            }
            paramstr = paramstr + cm.getColumnId(i) + "=" + cm.getColumnWidth(i);
        }
        var postparam = Ext.urlEncode({columninfo:paramstr});
        Ext.lib.Ajax.request("POST",m_setcolumninfourl,createSessionTimeoutCallback(),postparam);
    }
    
    //-----< onGridColumnResize >-----------------------------------------------
    function onGridColumnResize(colindex,newwidth)
    {
        var paramstr = "";
        var cm = m_Grid.getColumnModel();
        for(var i=0; i < cm.getColumnCount(); i++)
        {
            if(paramstr.length > 0)
            {
                paramstr = paramstr + ",";
            }
            paramstr = paramstr + cm.getColumnId(i) + "=" + cm.getColumnWidth(i);
        }
        var postparam = Ext.urlEncode({columninfo:paramstr});
        Ext.lib.Ajax.request("POST",m_setcolumninfourl,createSessionTimeoutCallback(),postparam);
    }

    //-----< onGridCellClick >--------------------------------------------------
    function onGridCellClick(g,row,col)
    {
    	g.getView().focusEl.focus();
        var record = m_DataStore.getAt(row);
        if(record)
        {
            var selmod = g.getSelectionModel();
            isSelected = selmod.isSelected(row);
         //   selmod.persistCurrentSelection(row,isSelected);

            if(!record.get("selectionChanging"))
            {
                if(isSelected)
                {
                    if(m_DelayedEditorTask)
                    {
                        m_DelayedEditorTask.cancel();
                        m_DelayedEditorTask = null;
                    }
                    if (g.tooltip!=null) {
                    	g.tooltip.hide();
                    }
                    m_DelayedEditorTask = new Ext.util.DelayedTask(function(){
                                                                   g.startEditing(row,col);
                                                                   });
                    m_DelayedEditorTask.delay(200);
                }
            }
            record.set("selectionChanging",false);
        }
    }

    //-----< onGridCellDblClick >-----------------------------------------------
    function onGridCellDblClick(g,row,col)
    {
        if(m_DelayedEditorTask)
        {
            m_DelayedEditorTask.cancel();
            m_DelayedEditorTask = null;
        }
        var record = m_DataStore.getAt(row);
        if(record)
        {
            var action = record.get("defaultAction");
            var newwindow = record.get("defaultNewWindow");
            if(action)
            {
                if(newwindow)
                {
                    window.open(action.replace(/&amp;/g,"&"));
                }
                else
                {
                    window.location.href = action.replace(/&amp;/g,"&");
                }
            }
        }
    }
    //------< onGridViewReady >--------------------------------------------------
    function onGridViewReady(grid){
    	if (null!=generalLoadingMask){
    		var isMasked = generalLoadingMask.el.isMasked();
    		if(isMasked) {
    			extJsLog.timeEnd('theMask');
    			generalLoadingMask.hide();
    		}
    	}
    	if (shouldReconfigure) {
    		reconfigureGrid(currentColumnModel);
    	}
    	
    }
    //-----< onGridBeforeEdit > ------------------------------------------------
    function onGridBeforeEdit() {
    	Ext.QuickTips.disable();
    }
    //-----< onGridAfterEdit >--------------------------------------------------
    function onGridAfterEdit(e)
    {
    	Ext.QuickTips.enable();
        var sValue = "";
        if (m_fields_enumDataStores[e.field])
        {
        	//mind the enumerartion value packing [[value,text,tooltip],text]
        	var enumValueStore= new Ext.data.ArrayStore({fields:['value','text'],data:eval(e.value)});
        	
        	if (enumValueStore.data.length>0)
        	{
        		var valueElements=enumValueStore.getAt(0);
        		sValue=valueElements.data.value?valueElements.data.value:'';
        	}
        } 
        else if(e.value instanceof Date)
        {
        	sValue = e.value.format('Y-m-d H:i'); // transport date format. This IS NOT the date format the user sees
        }
        else
        {
        	sValue = ""+e.value;
     	}
        
        var postparam = Ext.urlEncode({row:e.record.id,col:e.field,value:sValue});
        var callback = {
            success: function(o){
        			receivedValue = o.responseText; 
        			if (receivedValue!=null && trim(receivedValue).length>0){
        				e.record.data[e.field]=receivedValue;
        			}
        			e.record.commit();
        	},
            failure: function(o){
        		if (trim(o.responseText)=='owsessiontimeout') {
            		document.location=m_mainUrl;
            	} else {
	        		Ext.Msg.alert("Error","Update error:\n"+o.responseText);
	        		e.record.reject();
            	}
        	}
        };
        Ext.lib.Ajax.request("POST",m_setcelldataurl,callback,postparam);
    }

    //-----< startContextmenuLoading >------------------------------------------
    function startContextmenuLoading()
    {
        // HTTP Proxy
        var Contextmenu_HttpProxy = new Ext.data.HttpProxy({url:m_contextmenuurl});
        // record definition
        var Contextmenu_RecordDefinition = [ {name:'displayName', mapping: 'display_name'},
                                             {name:'iconUrl',     mapping: 'icon_url'} ];
        // create new JsonReader
        var Contextmenu_JsonReader = new Ext.data.JsonReader( { root          : 'menu_items',
                                                                totalProperty : 'count',
                                                                id            : 'id' },
                                                              Contextmenu_RecordDefinition );
        // create a new DataStore
        var Contextmenu_DataStore = new Ext.data.Store( { proxy      : Contextmenu_HttpProxy,
                                                          reader     : Contextmenu_JsonReader,
                                                          remoteSort : false } );
        // add listeners to datastore
        Contextmenu_DataStore.on('load',onContextmenuStoreLoad);
        Contextmenu_DataStore.on('exception',onDataStoreException);
        // init async request
        // add a 'fakeparam'. This will force ExtJS to use the POST method instead of the GET method.
        // Due to a bug in IE, GET method calls are sometimes spuriously cached. Sigh!
        Contextmenu_DataStore.load({params:{fakeparam:0}});
    }

    //-----< onContextmenuStoreLoad >-------------------------------------------
    function onContextmenuStoreLoad(store,records,options)
    {
        var cnt = store.getCount();
        //don't render context menu if it doesn't contain any items
        if(store.getCount() > 0)
        {
	        var ContextmenuDefinition = [];
	        for(var i = 0; i < cnt; i++)
	        {
	            var col = store.getAt(i);
	            var menuitemdata = {};
	            menuitemdata["text"] = col.get("displayName");
	            menuitemdata["icon"] = col.get("iconUrl");
	            menuitemdata["handler"] = onContextMenuItemClick;
	            menuitemdata["pluginid"] = col.id;
	            ContextmenuDefinition[ContextmenuDefinition.length] = menuitemdata;
	        }
	        m_Contextmenu = new Ext.menu.Menu({items:ContextmenuDefinition});
	        m_Contextmenu.on('beforehide',onContextMenuHide); 
	        m_Grid.addListener('rowcontextmenu',onRowContextMenu);
        } 
    }
	//-----< onContextMenuHide >------------------------------------------------
    function onContextMenuHide() {
    	Ext.QuickTips.enable();
    }
    //-----< onRowContextMenu >-------------------------------------------------
    function onRowContextMenu(grid,rowIndex,e)
    {
        // do not show browsers context menu
        e.stopEvent();
        // sanity check
        if(!m_Contextmenu)
        {
            return;
        }
        // display context menu
        Ext.QuickTips.disable();
        m_Contextmenu.showAt(e.getXY());
        
    }

    //-----< onRowContextMenuItemClick >----------------------------------------
    function onContextMenuItemClick(menuitem)
    {
        var selection = m_Grid.getSelectionModel().getSelections();
        if(selection && (selection.length > 0))
        {
            var selectedlist = "";
            for(var i = 0; i < selection.length; i++)
            {
                if(i > 0)
                {
                    selectedlist = selectedlist + ",";
                }
                selectedlist = selectedlist + selection[i].id;
            }
            window.location.href = appendToUrl(m_pluginurl,"&object=" + selectedlist + "&plugin=" + menuitem.pluginid);
        }
    }
    
    //-----< fieldRendererIcon >------------------------------------------------
    function fieldRendererIcon(value,p,record)
    {
        var versionstr = "";
        if( record.data['version'] && (record.data['version'] != "") )
        {
            versionstr = "(" + record.data['version'] + ")";
        }
        return String.format('<div style="float:left;">{0}</div><div style="float:left;">{1}&nbsp;</div><div style="float:left;">{2}&nbsp;</div><div style="float:left;">{3}&nbsp;</div><div style="float:left;">{4}</div>',(record.data['lockedicon']?record.data['lockedicon']:''), (record.data['plugins']?record.data['plugins']:''), (record.data['coicon']?record.data['coicon']:''), versionstr, (value?value:''));
    }

    //-----< fieldRendererDefault >---------------------------------------------
    function showTooltip(value, metadata, record, rowindex, colindex, store) {
    	 if(record!=null && record.data['tooltip']!=null)
         {
    		 var preparedtooltip = record.data['tooltip'].replace(/\&#60;/g,"<").replace(/\&#62;/g,">").replace(/\&#39;/g,"'").replace(/\&amp;nbsp;/g," ").replace(/&quot;/g,"\"");
    		 metadata.attr = "ext:qtip='" + preparedtooltip + "'";
         }
    }
    //-----< fieldRendererDefault >---------------------------------------------
    function fieldRendererDefault(value, metadata, record, rowindex, colindex, store)
    {
    	showTooltip(value,metadata,record,rowindex,colindex,store);
        return(value?value:'');
    }

    //-----< fieldRendererBoolean >---------------------------------------------
    function fieldRendererBoolean(value,p,record)
    {
    	showTooltip(value,p,record);
        var result = value ? "<input disabled type='checkbox' checked>" : "<input disabled type='checkbox'>";
        return(result);
    }

    //-----< fieldRendererDate >------------------------------------------------
    function fieldRendererDate(value,p,record)
    {
    	showTooltip(value,p,record);
        var result = value ? value.format(m_dateformat) : '';
        return(result);
    }

    //-----< fieldRendererFloat >-----------------------------------------------
    function fieldRendererFloat(value,p,record)
    {
    	showTooltip(value,p,record);
        var result =  value ? String(value).replace(".",m_decimalseparator) : '';
        return(result);
    }

    //-----< fieldRendererEnum >------------------------------------------------
    function fieldRendererEnum(value,p,record)
    {
    	showTooltip(value,p,record);
    	var evaluatedValue = eval(value);
    	var enumValueStore= new Ext.data.ArrayStore({fields:['value','text','tooltip'],data:evaluatedValue!=null?evaluatedValue:value});
    	
    	var theText='';
    	
    	if (enumValueStore.data.length>0)
    	{
    		var valueElements=enumValueStore.getAt(0);
    		theText=valueElements.data.text?valueElements.data.text:'';
    	}
       
        return theText;
    } 
    //-----< onRowSelect >-------------------------------------------------
    function onRowSelect(rowSelectionModel,index) {
    	if (rowSelectionModel!=null) {
    		if (rowSelectionModel.getCount()>0) {
    			//alert('Selected row is: ' + index);
    			var postparam = Ext.urlEncode({objid:index,status:"true"});
    	        var callback = {
    	                success: function(o){
    	            		return true;
    	            	},
    	                failure: function(o){ Ext.Msg.alert("Error","Update error:\n"+o.responseText);e.record.reject();}
    	            };
    			Ext.lib.Ajax.request("POST",m_url_persistSelection,callback,postparam);
    		}
    	}
    }
    //-----< onRowDeselect >-------------------------------------------------
    function onRowDeselect(rowSelectionModel,index) {
    	if (rowSelectionModel!=null) {
    		if (rowSelectionModel.getCount()>0) {
    			//alert('Deselected row is: ' + index);
    			var postparam = Ext.urlEncode({objid:index,status:"false"});
    	        var callback = {
    	                success: function(o){
    	            		return true;
    	            	},
    	                failure: function(o){ Ext.Msg.alert("Error","Update error:\n"+o.responseText);e.record.reject();}
    	            };
    			Ext.lib.Ajax.request("POST",m_url_persistSelection,callback,postparam);
    		}
    	}
    }
    //-----< onDataLoaded >----------------------------------------------------
    function onDataLoaded(r,options,success) {
		if (success) {
	        // start asynchronous contextmenu loading
	        startContextmenuLoading();
			m_Grid.getSelectionModel().selectPersistentItems();
			if(m_owIconColumnId) {
				var columnM = m_Grid.getColumnModel();
				var index = columnM.getIndexById(m_owIconColumnId);
				if(-1 != index) {
					if (m_DataStore.getTotalCount() > 0)
					{
						if(m_dataJsonReader.jsonData.isComplete) {
							columnM.setColumnHeader(index, "" + m_DataStore.getTotalCount());
						} else {
							columnM.setColumnHeader(index, ">" + m_DataStore.getTotalCount());
						}
					}
				}
			}
		} 
    }
    //-----< onDataStoreException >----------------------------------------------------
    function onDataStoreException( proxy ,  type,  action,  options,  response,  arg ) {
    	if (trim(response.responseText)=='owsessiontimeout') {
    		document.location=m_mainUrl;
    	} else { 
    		errMsg=".";
    		if (response!=null){
    			if(response.responseText!=null) {
    				errMsg = ": <br>" + response.responseText;
    				Ext.Msg.alert("Error","Cannot load datastore" + errMsg);
    			} 
    		}
    	}
    }

    //-----< onPageChange >----------------------------------------------------
    function onPageChange(pagingToolbar, pageData) {
		if (m_Grid!=null)
		{
			if (m_Grid.getSelectionModel()!=null) 
			{
				m_Grid.getSelectionModel().selectPersistentItems();
			}
			if (m_DataStore.getTotalCount() < 0)
			{
				pagingToolbar.next.setDisabled(!m_DataStore.reader.jsonData.canPageNext);
				pagingToolbar.prev.setDisabled(!m_DataStore.reader.jsonData.canPagePrev);
				pagingToolbar.last.setDisabled(true);
				//pagingToolbar.inputItem.setDisabled(true);
			}
		}
    }
    //-----< createSessionTimeoutCallback >------------------------------------
    function createSessionTimeoutCallback() {
        var callback = {
                success: function(o){
            		return true;
            	},
                failure: function(o){
            		if (trim(o.responseText)=='owsessiontimeout') {
                		document.location=m_mainUrl;
                	} else { 
                		Ext.Msg.alert("Update error:\n"+o.responseText);
                	}
            		return false;
            	}
            };
        return callback;
    }

    //-----< process data column >------------------------------------    
    function processColumn(col)
    {
        var coldata = {};
        var recdata = {};
        var canFilter = col.get("canFilter") ? true : false;
        var filterActive = col.get("filterActive") ? true : false;
        var sortable = col.get("sortable") ? true : false;
        var filterTooltip = col.get("filterTooltip");
        coldata["id"] = col.id;
        coldata["menuDisabled"] = true;
        var headerhtml = "";
        if(m_usefilters && canFilter)
        {
            headerhtml += "<a href='" + appendToUrl(m_editfilterurl,"&fprop=" + col.id) + "'>";
            headerhtml += "<img alt='"+filterTooltip+"' title='"+filterTooltip+"' class='OwGeneralList_sortimg' border='0' src='";
            headerhtml += filterActive ? m_iconfilteractive : m_iconfilter;
            headerhtml += "'>";
            headerhtml += "</a>";
        }
        headerhtml += col.get("displayName");
        coldata["header"] = headerhtml;
        coldata["dataIndex"] = col.id;
        coldata["width"] = col.get("proposedWidth");
        coldata["sortable"] = true;
        coldata["can_filter"] = canFilter;
        coldata["filter_active"] = filterActive;
        coldata["sortDir"] = col.get("sortDir");
        coldata["sortable"] = sortable;
        if (coldata["sortDir"]!=null&&coldata["sortDir"]!="") {
        	sortInfo = new Array(2);
        	sortInfo[0]=col.id;
        	sortInfo[1]=col.get("sortDir");
        }
        if(col.get("canEdit"))
        {
            var editRequired = col.get("editRequired");
            switch(col.get("editDatatype"))
            {
            case 0: // String
                coldata["editor"] = new Ext.grid.GridEditor(new Ext.form.TextField({allowBlank:(!editRequired)}));
                // data rendered with default renderer
                recdata["type"] = "string";
                coldata["renderer"] = fieldRendererDefault;
                break;
            case 1: // Integer
                cfg = { allowBlank       : (!editRequired),
                        allowDecimals    : false,
                        decimalSeparator : m_decimalseparator };
                if(col.get("editMaxvalue"))
                {
                    cfg["maxValue"] = col.get("editMaxvalue");
                }        
                if(col.get("editMinvalue"))
                {
                    cfg["minValue"] = col.get("editMinvalue");
                }        
                coldata["editor"] = new Ext.grid.GridEditor(new Ext.form.NumberField(cfg));
                // data rendered with default renderer
                recdata["type"] = "int";
                coldata["renderer"] = fieldRendererDefault;
                break;
            case 2: // Boolean
                coldata["editor"] = new Ext.grid.GridEditor(new Ext.form.Checkbox());
                // data rendered with boolean field renderer
                recdata["type"] = "boolean";
                coldata["renderer"] = fieldRendererBoolean;
                break;
            case 3: // Date
                coldata["editor"] = new Ext.grid.GridEditor(new Ext.form.DateField({allowBlank:(!editRequired), format:m_dateformat}));
                // data rendered with default renderer
                recdata["type"] = "date";
                recdata["dateFormat"] = 'Y-m-d H:i'; // transport date format. This IS NOT the date format the user sees
                coldata["renderer"] = fieldRendererDate;
                break;
            case 4: // Double
                cfg = { allowBlank       : (!editRequired),
                        allowDecimals    : true,
                        decimalSeparator : m_decimalseparator };
                if(col.get("editMaxvalue"))
                {
                    cfg["maxValue"] = col.get("editMaxvalue");
                }        
                if(col.get("editMinvalue"))
                {
                    cfg["minValue"] = col.get("editMinvalue");
                }        
                coldata["editor"] = new Ext.grid.GridEditor(new Ext.form.NumberField(cfg));
                // data rendered with default renderer
                recdata["type"] = "float";
                coldata["renderer"] = fieldRendererFloat;
                break;
            case 5: // Enum
            	enumInfoUrl=col.get("enumInfoUrl");
                
            	//default enum proxy 
                enumInfo_HttpProxy = new Ext.data.HttpProxy( {
        			url : enumInfoUrl + '&row=0&col=0'
        		});

        		enumInfo_JsonReader = new Ext.data.JsonReader( {
        			totalProperty : 'valuesCount',
        			root : 'fields',
        			id : 'id'}, 
        			[ {	name : 'value'}, 
        			  {	name : 'text'} ]);

        		enumInfo_DataStore = new Ext.data.Store( {
        			proxy : enumInfo_HttpProxy,
        			reader : enumInfo_JsonReader,
        			remoteSort : true
        		});

        		enumInfo_DataStore.addListener('loadexception', function() {
        			 Ext.Msg.alert("Error","Error loading choice list from server!");
        		});
        		
        		m_fields_enumDataStores[col.id]=enumInfo_DataStore;
        		
                comboCfg= { mode		 :'remote',
                			typeAhead        : true,
                			lazyRender       : true,
                            triggerAction    : 'all',
                            decimalSeparator : m_decimalseparator,
                            store            : m_fields_enumDataStores[col.id],
                            valueField       : 'value',
                            displayField     : 'text',
                            listeners: {
                            	beforequery: function(qe){
                					delete qe.combo.lastQuery;
                            	}
                			}
                        };
                coldata["editor"] = new Ext.form.EnumEditor(new Ext.form.ComboBox(comboCfg),null,enumInfoUrl);
                // data rendered with enum renderer
                coldata["renderer"] = fieldRendererEnum;
                break;
            case 6: // NOTE
                coldata["editor"] = new Ext.form.NoteEditor(new Ext.form.TextField({allowBlank:(!editRequired)}));
                // data rendered with default renderer
                recdata["type"] = "string";
                coldata["renderer"] = fieldRendererDefault;
                break;
            default: 
                // no editor
                // data rendered with default renderer
                coldata["renderer"] = fieldRendererDefault;
                break;
            }
        }
        else
        {
            coldata["renderer"] = fieldRendererDefault;
        }
        recdata["name"] = col.id;
        recdata["mapping"] = col.id;
        recdata["rowSelected"]=col.get("rowSelected");

        return {recdata:recdata, coldata:coldata};
    }
    
    function createPagingbar()
    {
    	var textAfterPageInput = "";
    	if (m_DataStore.getTotalCount() > 0)
    	{
    		textAfterPageInput = m_strPagingAfterPageText;
    	}

    	var toolbar = new Ext.alfresco.owd.ux.InfinitePagingToolbar({
         	pageSize: m_pagesize,
         	store:m_DataStore,
            displayInfo: true,
            hideParent:true,
            hidden:!m_showPaging,
            displayMsg     : m_strPagingDisplayMsg,
            emptyMsg       : m_strPagingEmptyMsg,
            beforePageText : m_strPagingBeforePageText,
            afterPageText  : textAfterPageInput,
            firstText      : m_strPagingFirstText,
            prevText       : m_strPagingPrevText,
            nextText       : m_strPagingNextText,
            lastText       : m_strPagingLastText,
            refreshText    : m_strPagingRefreshText
             });
    	toolbar.inputItem.id=m_listID+'numberField';
         return toolbar;
    }
    
    function createSelectionModel(singleSelection)
    {
    	return new Ext.grid.WewebuRowSelectionModel({singleSelect:singleSelection});
    }
    
    function createGrid(columnModel, bottomBar)
    {
    	return new Ext.grid.EditorGridPanel({
        	id:'owgrid',
        	plugins:['autosizecolumns'],
            store: m_DataStore,
            colModel: columnModel,
            view: new Ext.grid.WewebuGridView(),
            enableColumnHide:false,
            stripeRows:false,
            loadMask: m_gridMask,
        	region:'center',
        	margins: '3 3 3 3',
        	bbar: bottomBar
        });
    }
    
    function getKeyEventHandler()
    {
    	return [
    	        {
    	        	stopEvent : true,
    	        	key : 13,
    	        	fn : function() {
    	        		grid = Ext.getCmp('owgrid');
    					if (grid) {
    						selectionModel = grid.getSelectionModel();
    						if (selectionModel) {
    							if (selectionModel.getSelections().length!=0) {
    								selectedRecord = selectionModel.getSelected();
    								store = grid.getStore();
    								row = store.indexOfId(selectedRecord.id);
    								executeDefaultAction(grid, row, 0);
    							}
    						}
    					}
    	        	}
    	        },
    	        {
    	        	stopEvent : true,
    	        	key : 36,//HOME
    	        	fn : function() {
    	        		OwObjectListViewEXTJSGrid.firstRow();	        				
    	        	}
    	        },
    	        {
    	        	stopEvent : true,
    	        	key : 35,//END
    	        	fn : function() {
    	        		OwObjectListViewEXTJSGrid.lastRow();            				
        			}
    	        },
    	        {
    	        	stopEvent:true,
    	        	key:33,//PG UP
    	        	ctrl:false,
    	        	fn : function() {
    	        		OwObjectListViewEXTJSGrid.previousPage();
    	        	}
    	        },   
    	        {
    	        	stopEvent:true,
                	key:34,//PG DOWN
                	ctrl:false,
                	fn : function(){
                		OwObjectListViewEXTJSGrid.nextPage();
                	}
            	},
    	        {
    	        	stopEvent:true,
    	        	key:33,//PG UP
    	        	ctrl:true,
    	        	fn : function(key,event) {
    	        		OwObjectListViewEXTJSGrid.firstPage(key, event);   		
    	        	}
    	        },   
    	        {
    	        	stopEvent:true,
    	        	key:34,//PG DOWN
    	        	ctrl:true,
    	        	fn : function(key,event) {
    	        		OwObjectListViewEXTJSGrid.lastPage(key, event);
    	        	}
    	        }   
            	];
    }
    
}(); // end of function 'OwObjectListViewEXTJSGrid'    
