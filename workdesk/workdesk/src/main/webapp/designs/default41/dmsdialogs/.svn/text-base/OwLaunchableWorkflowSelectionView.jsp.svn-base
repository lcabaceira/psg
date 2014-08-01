<%@ page
	import="java.util.Iterator,java.util.List,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.dmsdialogs.views.OwLaunchableWorkflowSelectionView"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
<%@page import="com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription"%>

<%
    // get a reference to the calling view
    OwLaunchableWorkflowSelectionView m_View = (OwLaunchableWorkflowSelectionView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
	String design = m_View.getContext().getDesignURL();
	
%><div style="padding-top:10px;" class="OwLaunchableWorkflowSelection"><%

    List descriptions = m_View.getWorkflowDescriptions();
    if (m_View.hasWorkflowDescrptions())
    {
        int rowCount = descriptions.size();
%>
<table class="OwGeneralList_table">
	<caption>
			<% m_View.renderTableCaption(out);%>
	</caption>
	<tbody>
<%
        for (int rowIndex=0; rowIndex<m_View.getWorkflowDescriptionCount(); rowIndex++)
        {
            String rowClass = (rowIndex % 2 == 0) ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd";
            String rowClickURL=m_View.createRowLinkURL(rowIndex);
%>
		<tr class="<%=rowClass%>">
		    <td align="center" width="10"></td>
			<td align="center" width="20">
				<a title="<%=m_View.getWorkflowDescriptionName(rowIndex)%>" href="<%=rowClickURL%>"> 
				<img alt="<%=m_View.getWorkflowDescriptionName(rowIndex)%>" title="<%=m_View.getWorkflowDescriptionName(rowIndex)%>" src="<%=design%>/images/plug/owbpm/gear_new.png" /></a>
			</td>
			<td align="left">
				<a title="<%=m_View.getWorkflowDescriptionName(rowIndex)%>" href="<%=rowClickURL%>"><%=m_View.getWorkflowDescriptionName(rowIndex)%></a>
			</td>
		</tr>
<%
	    }
%>
	</tbody>
</table>
<% } else { %>
    <span class="OwEmptyTextMessage"><%= m_View.getContext().localize("app.OwLaunchableWorkflowSelectionView.nolist","No workflows available.")%></span>
<% } %>
</div>
