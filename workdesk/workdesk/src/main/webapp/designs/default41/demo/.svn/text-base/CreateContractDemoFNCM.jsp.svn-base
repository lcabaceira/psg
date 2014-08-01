<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true" pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.createnew", "Create new eFile demo")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
       <div class="block">
            <div class="left"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.firststep","1. Step")%></div>
            <div class="right">
            	<div class="owdemoprop">
	                <%
	                  OwInsertLabelHelper.insertLabel(out, "FolderName", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.enterfoldername","Folder:"), m_View,null);
	                  m_View.renderNamedRegion(out,"FolderName"); 
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.foldername.hint","Please type in a name for the folder.")%></div>
                </div>
                
                <div class="owdemoprop">
	                <%
	                  OwInsertLabelHelper.insertLabel(out, "ow_ro_CMEFileID", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.efileid","CMEFileID:"), m_View,null);
	                  m_View.renderNamedRegion(out,"ow_ro_CMEFileID");
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.efileid.hint","The generated eFile id.")%></div>
                </div>
                
                <div class="owdemoprop">
	                <%
	                  OwInsertLabelHelper.insertLabel(out, "ow_ro_ContractNumber", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.contractnumber","ContractNumber:"), m_View,null);
	                  m_View.renderNamedRegion(out,"ow_ro_ContractNumber");
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.contractnumber.hint","The contract number filled in previous view.")%></div>
                </div>
                
                <div class="owdemoprop">
	                <%
	                   OwInsertLabelHelper.insertLabel(out, "ow_ro_DateSentOn", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.datesenton","DateSentOn:"), m_View,null);
	                   m_View.renderNamedRegion(out,"ow_ro_DateSentOn");
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.datesenton.hint","The date filled in previous view.")%></div>                
                </div>
                
                <div class="owdemoprop">
	                <%
	                   OwInsertLabelHelper.insertLabel(out, "ow_ro_Status", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.status","Status:"), m_View,null);
	                   m_View.renderNamedRegion(out,"ow_ro_Status");
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.status.hint","The status of eFile filled in previous view.")%></div>
                </div>
                
                <div class="owdemoprop">
	                <%
	                  OwInsertLabelHelper.insertLabel(out, "StatusDescription", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.statusdescription","Status Description:"), m_View,null);
	                  m_View.renderNamedRegion(out,"StatusDescription");
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.statusdescription.hint","Please type in a description for the status of this eFile.")%></div>
                </div>
                
                <div class="owdemoprop">
	                <%
	                  OwInsertLabelHelper.insertLabel(out, "ow_ro_SecondGeneratedKey", m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.secondgeneratedkey","SecondGeneratedKey:"), m_View,null);
	                  m_View.renderNamedRegion(out,"ow_ro_SecondGeneratedKey");
	                %>
	                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.secondgeneratedkey.hint","Another key generated using metadata filled in previous view.")%></div>
                </div>
            </div>
        </div>
        <div class="block">
            <div class="left"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.secondstep","2. Step")%></div>
            <div class="right">
                <% m_View.renderNamedRegion(out,"ow_menu"); %>
                <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractRecordForm.text2","When clicking save, the eFile will be created.")%></div>
            </div>
        </div>
    </div>
    <div id="OwDemoForm_Footer">&nbsp;</div>
</div>