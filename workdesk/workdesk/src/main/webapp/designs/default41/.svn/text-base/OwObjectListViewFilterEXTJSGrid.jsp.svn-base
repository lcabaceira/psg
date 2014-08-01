<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"
   import="java.util.*,
   com.wewebu.ow.server.ui.*,
   com.wewebu.ow.server.app.*,
   com.wewebu.ow.server.field.OwSearchCriteria,
   com.wewebu.ow.server.field.OwSearchOperator,
   com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewFilterEXTJSGrid"

%><%
    // get a reference to the calling view
    OwObjectListViewFilterEXTJSGrid m_View = (OwObjectListViewFilterEXTJSGrid) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwMainAppContext m_context = (OwMainAppContext) m_View.getContext();
    boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();

    if (useHtml5DragAndDrop)
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

OwObjectListViewEXTJSGrid.config = { initialized: true,
									 main_url: "<%=m_View.getContext().getBaseURL()%>",
									 list_id: "<%=m_View.getListViewID()%>",
									 url_persistSelection: "<%=m_View.getAjaxEventURL("PersistSelection", null)%>",
									 url_getColumnInfo : "<%=m_View.getAjaxEventURL("getColumnInfo", null)%>",
                                     url_readlist : "<%=m_View.getAjaxEventURL("ReadList", null)%>", 
                                     usefilters : true,
                                     pluginurl : "<%=m_View.getEventURL("PluginEvent", null)%>",
                                     editfilterurl : "<%=m_View.getEventURL("EditFilter", null)%>",
                                     contextmenuurl : "<%=m_View.getAjaxEventURL("getContextmenu", null)%>",
                                     setcolumninfourl : "<%=m_View.getAjaxEventURL("setColumnInfo", null)%>",
                                     setcelldataurl : "<%=m_View.getAjaxEventURL("setCellData", null)%>",
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
                                     strPagingEmptyMsg       : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingEmptyMsg", "No items to display")%>",
                                     strPagingBeforePageText : "<label for='<%=m_View.getListViewID()+"numberField"%>'><%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingBeforePageText", "Page")%></label>",
                                     strPagingAfterPageText  : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingAfterPageText", "of {0}")%>",
                                     strPagingFirstText      : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingFirstText", "First Page")%>",
                                     strPagingPrevText       : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingPrevText", "Previous Page")%>",
                                     strPagingNextText       : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingNextText", "Next Page")%>",
                                     strPagingLastText       : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingLastText", "Last Page")%>",
                                     strPagingRefreshText    : "<%=m_View.getContext().localize("dmsdialogs.views.OwObjectListViewEXTJSGrid.PagingRefreshText", "Refresh")%>",
                                     showPaging   : <%=m_View.hasPaging()%>,
                                     loadingMessage : "<%=m_View.getLoadingMessage()%>",
                                     singleSelect : <%=Boolean.toString(!m_View.isMultiSelectionEnabled())%>
                                   };

/*Ext.EventManager.onDocumentReady(OwObjectListViewEXTJSGrid.init, OwObjectListViewEXTJSGrid, true);*/

function ExtjsGrid_SelectAll()
{
    OwObjectListViewEXTJSGrid.selectAll();
}

function ExtjsGrid_PluginEvent(pluginid)
{
    OwObjectListViewEXTJSGrid.pluginEvent(pluginid);
}

</script>
<%
   	    if (m_View.getCurrentFilter() != null)
   	    {
		    OwSearchCriteria crit = m_View.getCurrentFilter().getSearchNode().getCriteria();
%>   		
		<div id="OwObjectListViewFilterRow_Filter" class="OwSmallSubMenuItem">
			<a class="OwSmallSubMenuItem" href="<%=m_View.getEnableFilterURL()%>">
<%
           String toolTip, imgSrc;
           if (m_View.getCurrentFilter().isActive())
           {
               toolTip = m_View.getContext().localize("image.checked", "Filter activated");
               imgSrc = m_View.getContext().getDesignURL() + "/images/checked.png";

           }
           else
           {
               toolTip = m_View.getContext().localize("image.unchecked", "Filter not activated");
               imgSrc = m_View.getContext().getDesignURL() + "/images/unchecked.png"; 
           }
%>        <img src="<%=imgSrc%>" alt="<%=toolTip%>"  title="<%=toolTip%>"></a>
		<a class="OwSmallSubMenuItem" href="<%=m_View.getSetFilterValueURL()%>">
		<%=m_View.getContext().localize("owsearch.OwObjectListViewFilterRow.setfilter", "Filter")%>
		"<%=crit.getDisplayName(m_View.getContext().getLocale())%>"
		</a>
		<script type="text/javascript">
			function changeFilterOperator() {
				<%=m_View.getChangeFilterOperatorURL()%>
			}
		</script>
<%
        List items = new LinkedList();
        Iterator it = OwObjectListViewFilterEXTJSGrid.getFilterOperators(crit).iterator();
        while (it.hasNext())
        {
            Integer Op = (Integer) it.next();
            String displayValue = OwSearchOperator.getOperatorDisplayString(m_View.getContext().getLocale(), Op.intValue());
            OwComboItem item = new OwDefaultComboItem(Op.toString(), displayValue);
            items.add(item);
        }
        OwComboModel model = new OwDefaultComboModel(false, false, "" + crit.getOperator(), items);
        OwComboboxRenderer renderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(model, OwObjectListViewFilterEXTJSGrid.FILTER_OPERATOR_SELECT_ID, null, null, null);
        renderer.addEvent("onchange", "changeFilterOperator()");
        String filterOperatorDisplayName=m_View.getContext().localize("OwObjectListViewFilterRow.filterOperatordisplayName", "Filter Operator");
        OwInsertLabelHelper.insertLabelValue(out, filterOperatorDisplayName, OwObjectListViewFilterEXTJSGrid.FILTER_OPERATOR_SELECT_ID);
        renderer.renderCombo(out);
%>
		<div class="OwSmallSubMenuItemValue">
<%
        if (crit.isCriteriaOperatorRange())
        {
            String filterBetweenFirstDisplayName=m_View.getContext().localize("OwObjectListViewFilterRow.filterBetweenFirstDisplayName", "First");
            String betweenFirstID=Integer.toString(crit.hashCode());
            OwInsertLabelHelper.insertLabelValue(out, filterBetweenFirstDisplayName, betweenFirstID);
            m_View.getFieldManager().insertEditField(out, crit);
            out.write(" <span>-</span> ");
            
            String filterBetweenLastDisplayName=m_View.getContext().localize("OwObjectListViewFilterRow.filterBetweenLastDisplayName", "Last");
            OwSearchCriteria secondRangeCrit=crit.getSecondRangeCriteria();
            String betweenLastID=Integer.toString(secondRangeCrit.hashCode());
            OwInsertLabelHelper.insertLabelValue(out, filterBetweenLastDisplayName, betweenLastID);
            m_View.getFieldManager().insertEditField(out, secondRangeCrit);
        }
        else
        {
            String filterCriteriaDisplayName=m_View.getContext().localize("OwObjectListViewFilterRow.filterCriteriadisplayName", "Criteria");
            String criteriaID=Integer.toString(crit.hashCode());
            OwInsertLabelHelper.insertLabelValue(out, filterCriteriaDisplayName, criteriaID);
            m_View.getFieldManager().insertEditField(out, crit);
        }
%>
		</div>
	</div>
<%
    }
%>
<div id="grid-panel" style="width:100%;height:100px;"></div>

<script type="text/javascript">

document.getElementById('grid-panel').style.height = (getWindowHeight() - 220) + "px";

</script>
<%
    ((OwMainAppContext) m_View.getContext()).addFinalScript(new OwScript("if (usesEXTJSGrid(true)) {OwObjectListViewEXTJSGrid.setHeight(document.getElementById('grid-panel').style.height);}", OwScript.DEFAULT_PRIORITY - 20));
%>