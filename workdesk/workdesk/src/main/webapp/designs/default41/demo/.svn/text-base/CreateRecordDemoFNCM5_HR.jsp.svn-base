<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true" pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
	<div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("plugin.com.wewebu.ow.NewDossier.usingForm.description", "Create New HR Dossier using form")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

	<!-- Main area -->
	<div id="OwDemoForm_MAIN">
	   <div class="block">
            <div class="left"><h2><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firststep","1. Step")%></h2></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "FolderName", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.hrDossierName","Dossier name:"), m_View,null);
                 m_View.renderNamedRegion(out,"FolderName"); 
                %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.hrDossierName.info","Please type the HR Dossier name.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "FirstName", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firstName","Firstname:"), m_View,null);
                 m_View.renderNamedRegion(out,"FirstName"); 
                %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firstName.info","Please type the firstname of employee.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "LastName", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.lastName","Lastname:"), m_View,null);
                 m_View.renderNamedRegion(out,"LastName"); 
                %>
            <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.lastName.info","Please type the lastname of employee.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "DossierPersonnelNumber}", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.personnelNumber","Personnel Number:"), m_View,null);
                 m_View.renderNamedRegion(out,"DossierPersonnelNumber}"); 
                %>
            <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.personnelNumber.info","Please type the personnel number.")%></div>
            </div>
        </div>
	    <div class="block">
            <div class="left"><h2><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.secondstep","2. Step")%></h2></div>
            <div class="right">
                <% m_View.renderNamedRegion(out,"ow_menu"); %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.text2","When clicking save, the file will be created with your specifications and opened for editing.")%></div>
	        </div>
	    </div>
	</div>
	<div id="OwDemoForm_Footer">&nbsp;</div>
</div>