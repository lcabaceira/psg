<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true" pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.createnew", "Create new file demo")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
       <div class="block">
            <div class="left"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firststep","1. Step")%></div>
            <div class="right">
                <%
                  OwInsertLabelHelper.insertLabel(out, "FolderName", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.entername","Name:"), m_View,null);              
                  m_View.renderNamedRegion(out,"FolderName"); 
                %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.text1","Please type in a name for the file.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.secondstep","2. Step")%></div>
            <div class="right">
                <% m_View.renderNamedRegion(out,"ow_menu"); %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.text2","When clicking save, the file will be created with your specifications and opened for editing.")%></div>
            </div>
        </div>
    </div>
    <div id="OwDemoForm_Footer">&nbsp;</div>
</div>