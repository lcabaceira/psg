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
        <h1><%=m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.pageref", "Form Launch-Step-Processor (Demo)")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
			<table class="OwFormTable OwNowrapLabel">
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:startTask.bpm:workflowDescription}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.topic", "Topic"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "{D:bpm:startTask.bpm:workflowDescription}"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><% OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:comment}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.comment", "Processing note"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:comment}"); %></td>
				</tr>
				<tr>
                    <td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:percentComplete}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.percentComplete", "Percent completed"), m_View,null);%></td>
                    <td class="DefaultInput"><% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:percentComplete}"); %></td>
                </tr>				
			</table>
        </div>
    <div id="OwDemoForm_Footer"><% m_View.renderNamedRegion(out, "ow_menu"); %></div>
</div>
		
			
		