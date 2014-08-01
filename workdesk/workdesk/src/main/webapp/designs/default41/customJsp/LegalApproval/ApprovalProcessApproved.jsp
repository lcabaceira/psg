<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.cmg.legalapproval.LegalApproval", "Legal Approval - Your request was approved by lawyer")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco_flower.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
		<table class="OwFormTable OwNowrapLabel">
			<tr>
				<td class="OwPropertyName"><b><%=m_View.getContext().localize("owlabel.D:bpm:task.bpm:description", "Description:")%></b></td>
				<td class="DefaultInput"><b><%=m_View.getContext().localize("jsp.cmg.legalapproval.descApproval", "Request was approved by the lawyer.")%></b></td>
			</tr>
			<tr>
				<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "ow_ro_{P:cmgbpm:initiator.cmgbpm:initiatorName}", m_View.getContext().localize("jsp.cmg.RequestedBy", "Requested by:"), m_View,null);%></td>
				<td class="DefaultInput"><% m_View.renderNamedRegion(out, "ow_ro_{P:cmgbpm:initiator.cmgbpm:initiatorName}"); %></td>
			</tr>	
			<tr><td>&nbsp;</td></tr>
			
			<tr>
				<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:comment}", m_View.getContext().localize("owlabel.D:bpm:task.bpm:comment", "Comment:"), m_View,null);%></td>
				<td class="DefaultInput"><% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:comment}"); %></td>
			</tr>		

			<tr>
				<td class="OwPropertyName"><%=m_View.getContext().localize("jsp.cmg.Status", "Status:")%></td>
				<td class="DefaultInput"><%=m_View.getContext().localize("jsp.cmg.legalapproval.statusApproval1", "Approved by lawyer")%></td>
			</tr>	
			<tr>
				<td class="OwPropertyName"><%=m_View.getContext().localize("owlabel.D:bpm:task.bpm:percentComplete", "Percent complete:")%></td>
				<td class="DefaultInput">100 %</td>
			</tr>
			<tr>
				<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "OW_ATTACHMENTS", m_View.getContext().localize("jsp.cmg.Attachment", "Attachment:"), m_View,null);%></td>
				<td class="DefaultInput"><% m_View.renderNamedRegion(out, "OW_ATTACHMENTS"); %></td>
			</tr>
		</table>
	</div>
		
    <div id="OwDemoForm_Footer"><% m_View.renderNamedRegion(out, "ow_menu"); %></div>
</div>
