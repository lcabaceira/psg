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
        <h1><%=m_View.getContext().localize("jsp.demo.OwBPMDemo.pageref", "Form Step Processor Demo")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN">

			<table class="OwFormTable OwNowrapLabel">
				<%
			    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
			    { %>
			    <tr><td colspan="2">
			    <%
			    ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteAll(out);
			    %>
			    </td></tr>  
			    <%}
				%>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "F_Subject", m_View.getContext().localize("jsp.demo.OwBPMDemo.topic", "Topic"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "F_Subject"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "F_StepName", m_View.getContext().localize("jsp.demo.OwBPMDemo.step", "Step"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "F_StepName"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "F_EnqueueTime", m_View.getContext().localize("jsp.demo.OwBPMDemo.started", "Started"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "F_EnqueueTime"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName">
					<%OwInsertLabelHelper.insertLabel(out, "F_Comment", m_View.getContext().localize("jsp.demo.OwBPMDemo.comment", "Processing note"), m_View,null);%>
						<%
						    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
						    {
						        ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteProperty(out, "F_Comment");
						    }
						%>
					
					</td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "F_Comment"); %></td>
				</tr>
			</table>
        </div>
    <div id="OwDemoForm_Footer"><% m_View.renderNamedRegion(out, "ow_menu"); %></div>
</div>
