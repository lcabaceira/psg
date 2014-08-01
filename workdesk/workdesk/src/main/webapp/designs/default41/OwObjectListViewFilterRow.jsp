<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	com.wewebu.ow.server.app.*,
	java.util.*,
	com.wewebu.ow.server.field.OwSearchCriteria,
	com.wewebu.ow.server.field.OwSearchOperator,
	com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewFilterRow"

%><%// get a reference to the calling view
            OwObjectListViewFilterRow m_View = (OwObjectListViewFilterRow) request.getAttribute(OwView.CURRENT_MODULE_KEY);
            OwMainAppContext m_context = (OwMainAppContext) m_View.getContext();
            boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();
            
            if(useHtml5DragAndDrop) {
%>
            <script type="text/javascript">
              var dropArea = document.getElementById('OwSublayout_ContentContainer');
              if(typeof addHtml5DropArea == 'function') {
                 addHtml5DropArea(dropArea, false);
              }
            </script>
<%
            }

            List pluginEntries = m_View.getPluginEntries();
            if (m_View.getIsListValid())
            {
                if (m_View.getCurrentFilter() != null)
                {
                    OwSearchCriteria crit = m_View.getCurrentFilter().getSearchNode().getCriteria();
%>
<div id="OwObjectListViewFilterRow_Filter">
	<div class="OwSmallSubMenuItem">
		<a class="OwSmallSubMenuItem" href="<%=m_View.getEnableFilterURL()%>">
<%
                    String imageUrl, toolTip;
                    if (m_View.getCurrentFilter().isActive())
                    {
                        imageUrl = m_View.getContext().getDesignURL() + "/images/checked.png";
                        toolTip = m_View.getContext().localize("image.checked", "Filter activated");
                    }
                    else
                    {
                        imageUrl = m_View.getContext().getDesignURL() + "/images/unchecked.png";
                        toolTip = m_View.getContext().localize("image.unchecked", "Filter not activated");
                    }
    %><img src="<%=imageUrl%>" alt="<%=toolTip%>" title="<%=toolTip%>" /></a>
		<a class="OwSmallSubMenuItem" href="<%=m_View.getSetFilterValueURL()%>"><%=m_View.getContext().localize("owsearch.OwObjectListViewFilterRow.setfilter", "Filter")%> "<%=crit.getDisplayName(m_View.getContext().getLocale())%>" </a>

		<script type="text/javascript">
			function changeFilterOperator() {
				<%=m_View.getChangeFilterOperatorURL()%>
			}
		</script><%
		            List items = new LinkedList();
		            Iterator it = OwObjectListViewFilterRow.getFilterOperators(crit).iterator();
		            while (it.hasNext())
		            {
		                Integer Op = (Integer) it.next();
		                String displayValue = OwSearchOperator.getOperatorDisplayString(m_View.getContext().getLocale(), Op.intValue());
		                OwComboItem item = new OwDefaultComboItem(Op.toString(), displayValue);
		                items.add(item);
		            }
		            OwComboModel model = new OwDefaultComboModel(false, false, "" + crit.getOperator(), items);
		            OwComboboxRenderer renderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(model, OwObjectListViewFilterRow.FILTER_OPERATOR_SELECT_ID, null, null, null);
		            renderer.addEvent("onchange", "changeFilterOperator()");
		            String filterOperatorDisplayName=m_View.getContext().localize("OwObjectListViewFilterRow.filterOperatordisplayName", "Filter Operator");
		            OwInsertLabelHelper.insertLabelValue(out, filterOperatorDisplayName, OwObjectListViewFilterRow.FILTER_OPERATOR_SELECT_ID);
		            renderer.renderCombo(out);
%>
		<div class="OwSmallSubMenuItemValue"><%
		            if (crit.isCriteriaOperatorRange())
                    {
		                String filterBetweenFirstDisplayName=m_View.getContext().localize("OwObjectListViewFilterRow.filterBetweenFirstDisplayName", "First");
		                String betweenFirstID=Integer.toString(crit.hashCode());
		                OwInsertLabelHelper.insertLabelValue(out, filterBetweenFirstDisplayName, betweenFirstID);
                        m_View.getFieldManager().insertEditField(out, crit);
%>
			<span>-</span>
<%
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
%>    </div>
	</div>
</div>
<%
                }
%>
<div id="wrap">
 <table class="OwGeneralList_table" id="<%=m_View.getListViewID()%>">
	<caption>
<%
                m_View.renderTableCaption(out);
%>
	</caption>
	<thead><%
	            m_View.renderSortHeader(out, pluginEntries);
	%></thead>
<%
                if (m_View.getCount() != 0)
                {
%>
	<tbody class="OwObjectListView_body">
<%
		            m_View.renderRows(out, pluginEntries);
%>
	</tbody>
 </table>
</div>
<%
                    if (m_View.hasPaging())
                    {
%>      <div id="footer">

<%
                    m_View.renderPageSelector(out);
%>      </div>
<%
                    }//paging if
               }
               else
               {
%>
 </table>
</div>
<p id="emptyList" class="OwEmptyTextMessage"><%=m_View.getContext().localize("app.OwObjectListView.emptylist", "No items to display")%></p>
<%
                }
            }
            else
            {
%>
<p id="emptyList" class="OwEmptyTextMessage"><%=m_View.getContext().localize("app.OwObjectListView.nolist", "No items to display")%></p>
<%
            }
%>