<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"
	pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm" style="border: none">
	<div id="OwDemoForm_Header" style="float: left; width: 100%;">
		<h1><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.createnew", "Create new file demo")%></h1>
		<img
			alt="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			title="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
	</div>

	<!-- Main area -->
	<div id="OwDemoForm_MAIN"
		style="clear: left; float: left; width: 100%;">
		<div class="block">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.firststep", "1. Step")%></div>
			<div class="right">
				<%
				    OwInsertLabelHelper.insertLabel(out, "DocumentTitle", m_View.getContext().localize("jsp.demo.OwAddDocumentForm.taskTitle", "Task title:"), m_View,null);
				    m_View.renderNamedRegion(out, "DocumentTitle");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.text1", "Please type in a name for this task.")%></div>
			</div>
			<div style="clear: left;"></div>
			<div class="left">
				&nbsp;
			</div>
			<div class="right">
				<%
				    OwInsertLabelHelper.insertLabel(out, "Status", m_View.getContext().localize("jsp.demo.OwAddDocumentForm.taskStatus", "Status:"), m_View,null);
				    m_View.renderNamedRegion(out, "Status");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.taskStatusInfo", "Please type in the status of this task.")%></div>
			</div>
			<div style="clear: left;"></div>
			<div class="left">
				&nbsp;
			</div>
			<div class="right">
				<%
				    OwInsertLabelHelper.insertLabel(out, "StatusDescription", m_View.getContext().localize("jsp.demo.OwAddDocumentForm.taskStatusDescription", "Status Description:"), m_View,null);
				    m_View.renderNamedRegion(out, "StatusDescription");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.taskStatusDescriptionInfo", "Please type in the status description of this task.")%></div>
			</div>

		</div>

		<div class="block" style="float: left; width: 100%;">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.secondstep", "2. Step")%></div>
			<div class="right">
				<%
				    m_View.renderNamedRegion(out, "ow_menu");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwAddDocumentForm.text2", "When clicking save, the file will be created with your specifications and opened for editing.")%></div>
			</div>
		</div>
	</div>
	<div id="OwDemoForm_Footer"
		style="clear: left; float: left; width: 100%;">
		&nbsp;
	</div>
</div>