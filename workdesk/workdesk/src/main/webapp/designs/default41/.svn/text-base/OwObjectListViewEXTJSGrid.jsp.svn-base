<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*,
            com.wewebu.ow.server.ecm.OwObjectCollection,
            com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewEXTJSGrid"
    autoFlush   ="true"
 %><%
    // get a reference to the calling view
    OwObjectListViewEXTJSGrid m_View = (OwObjectListViewEXTJSGrid)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwMainAppContext m_context = (OwMainAppContext) m_View.getContext();  
    boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();

    if(useHtml5DragAndDrop) 
    {
%>
                    <script type="text/javascript">
                        var dropArea = document.getElementById('OwSublayout_ContentContainer');
                        if(typeof addHtml5DropArea == 'function') {
                            addHtml5DropArea(dropArea, false);
                        }
                    </script>
<%
    }
%>
<script type="text/javascript">
function configExtjsGrid() {
	if (usesEXTJSGrid()) {
		OwObjectListViewEXTJSGrid.config = {
				initialized: true,
				main_url: "<%=m_View.getContext().getBaseURL()%>",
				list_id: "<%=m_View.getListViewID()%>",
			    url_persistSelection: "<%=m_View.getAjaxEventURL("PersistSelection",null)%>",
			    url_getColumnInfo : "<%=m_View.getAjaxEventURL("getColumnInfo",null)%>",
			    url_readlist : "<%=m_View.getAjaxEventURL("ReadList",null)%>", 
			    usefilters : false,
			    pluginurl : "<%=m_View.getEventURL("PluginEvent",null)%>",
			    editfilterurl : "<%=m_View.getEventURL("EditFilter",null)%>",
			    contextmenuurl : "<%=m_View.getAjaxEventURL("getContextmenu",null)%>",
			    setcolumninfourl : "<%=m_View.getAjaxEventURL("setColumnInfo",null)%>",
			    setcelldataurl : "<%=m_View.getAjaxEventURL("setCellData",null)%>",
			    dateformat : "<%=m_View.getDateFormat()%>",
			    decimalseparator : "<%=m_View.getDecimalSeparator()%>",
			    startrow : <%=m_View.getStartRow()%>,
			    pagesize : <%=m_View.getPageSize()%>,
			    iconfilter : "<%=m_View.getContext().getDesignURL()%>/images/OwObjectListView/filter.png",
			    iconfilteractive : "<%=m_View.getContext().getDesignURL()%>/images/OwObjectListView/filteractive.png",
			    strPagingDisplayMsg : <%
			     if(m_View.getCount() < 0) {
			    %>
			    "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingDisplayMsgUnbound","Displaying items {0} - {1}")%>"
			    <%
			     } else {
			    %>
		        "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingDisplayMsg","Displaying items {0} - {1} of {2}")%>"
		        <% 
			     }
			    %>,
			    strPagingEmptyMsg : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingEmptyMsg","No items to display")%>",
			    strPagingBeforePageText : "<label for='<%=m_View.getListViewID()+"numberField"%>'><%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingBeforePageText","Page")%></label>",
			    strPagingAfterPageText : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingAfterPageText","of {0}")%>",
			    strPagingFirstText : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingFirstText","First Page")%>",
			    strPagingPrevText : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingPrevText","Previous Page")%>",
			    strPagingNextText : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingNextText","Next Page")%>",
			    strPagingLastText : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingLastText","Last Page")%>",
			    strPagingRefreshText : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingRefreshText","Refresh")%>",
			    showPaging : <%=m_View.hasPaging()%>,
			    loadingMessage : "<%=m_View.getLoadingMessage()%>",
			    singleSelect : <%=Boolean.toString(!m_View.isMultiSelectionEnabled())%>
		};
	}
};
configExtjsGrid();
</script>
<%--

/*Ext.EventManager.onDocumentReady(OwObjectListViewEXTJSGrid.init, OwObjectListViewEXTJSGrid, true);*/
--%>
<div id="grid-panel"></div>

<script type="text/javascript">
    var gridPanelHeight = getWindowHeight();
    
    if(document.getElementById('OwResultListViewTitle'))
    {
    	gridPanelHeight -= 255;
    } else {
    	gridPanelHeight -= 220;
	}
<%if (!m_View.isCollectionComplete()) 
    {//The warning message will be rendered, so we have to substract the height of the%>
        gridPanelHeight -= 23;
<%
    }
%>
    document.getElementById('grid-panel').style.height = gridPanelHeight  + "px";
</script>
<%
	((OwMainAppContext)m_View.getContext()).addFinalScript(new OwScript("if (usesEXTJSGrid(true)) {OwObjectListViewEXTJSGrid.setHeight(document.getElementById('grid-panel').style.height);}",OwScript.DEFAULT_PRIORITY - 20));
%>