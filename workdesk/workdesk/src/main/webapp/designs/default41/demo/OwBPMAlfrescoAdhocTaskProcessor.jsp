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
        <h1><%=m_View.getContext().localize("jsp.demo.OwBPMLaunchDemoAdhoc.pageref", "Form Launch-Step-Processor (Demo - for Adhoc tasks)")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
			<table class="OwFormTable OwNowrapLabel">
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{ow_ro_D:bpm:task.bpm:description}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.topic", "Topic"), m_View,null); %></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "{ow_ro_D:bpm:task.bpm:description}"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:comment}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.comment", "Processing Note"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:comment}"); %></td>
				</tr>
				<tr>
                    <td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:priority}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.priority", "Priority"), m_View,null);%></td>
                    <td class="DefaultInput"><% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:priority}"); %></td>
                </tr>
                <tr>
                    <td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{P:owdbpm:jspLayoutAspect.owdbpm:jspStepProcessor}", m_View.getContext().localize("jsp.demo.OwBPMLaunchDemo.jspStepProcessor", "Jsp Step Processor"), m_View,null);%></td>
                    <td class="DefaultInput"><% m_View.renderNamedRegion(out, "{P:owdbpm:jspLayoutAspect.owdbpm:jspStepProcessor}"); %></td>
                </tr>				
			</table>
        </div>
    <div id="OwDemoForm_Footer"><% m_View.renderNamedRegion(out, "ow_menu"); %></div>
</div>
		
			
		