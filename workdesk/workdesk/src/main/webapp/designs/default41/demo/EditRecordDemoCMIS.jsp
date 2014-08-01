<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"
	language="java" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
	<div id="OwDemoForm_Header">
        <h1>Edit using form</h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

	<!-- Main area -->
	<div id="OwDemoForm_MAIN">
	   <div class="block">
            <div class="left"><h2><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firststep","1. Step")%></h2></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "{cmis:folder.cmis:name}", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.hrDossierName","Dossier name:"), m_View,null);
                 m_View.renderNamedRegion(out,"{cmis:folder.cmis:name}"); 
                %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.hrDossierName.info","Please type the HR Dossier name.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "{F:owd:dossier.owd:firstName}", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firstName","Firstname:"), m_View,null);
                 m_View.renderNamedRegion(out,"{F:owd:dossier.owd:firstName}"); 
                %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.firstName.info","Please type the firstname of employee.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "{F:owd:dossier.owd:lastName}", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.lastName","Lastname:"), m_View,null);
                 m_View.renderNamedRegion(out,"{F:owd:dossier.owd:lastName}"); 
                %>
            <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.lastName.info","Please type the lastname of employee.")%></div>
            </div>
        </div>
        <div class="block">
            <div class="left"></div>
            <div class="right">
                <%
                 OwInsertLabelHelper.insertLabel(out, "{F:owd:dossier.owd:dossierPersonnelNumber}", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.personnelNumber","Personnel Number:"), m_View,null);
                 m_View.renderNamedRegion(out,"{F:owd:dossier.owd:dossierPersonnelNumber}"); 
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