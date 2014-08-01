<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true" pageEncoding="utf-8" language="java" contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
	<div id="OwDemoForm_Header">
		<h1><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.title", "Properties for IDs generation")%></h1>
		<img
			alt="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			title="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
	</div>

	<!-- Main area -->
	<div id="OwDemoForm_MAIN">
		<div class="block">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.firststep", "1. Step")%></div>
			<div class="right">
				<div class="owdemoprop">
					<%
                        OwInsertLabelHelper.insertLabel(out, "ContractNumber", m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.keys.contractNumber", "Contract Number:"), m_View,null);					
					    m_View.renderNamedRegion(out, "ContractNumber");
					%>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.keys.contractNumber.hint", "Please type in a contract number.")%></div>
				</div>

				<div class="owdemoprop">
                    <%
                       OwInsertLabelHelper.insertLabel(out, "DateSentOn", m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.keys.dateSentOn", "Sent On Date:"), m_View,null);
                       m_View.renderNamedRegion(out,"DateSentOn");
                    %>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.keys.dateSentOn.hint", "Sent On Date.")%></div>
				</div>

				<div class="owdemoprop">
					<%
					    OwInsertLabelHelper.insertLabel(out, "Status", m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.keys.status", "Status:"), m_View,null);
					    m_View.renderNamedRegion(out, "Status");
					%>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.keys.status.hint", "The status of the contract.")%></div>
				</div>
			</div>
		</div>
		<div class="block">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.secondstep", "2. Step")%></div>
			<div class="right">
				<%
				    m_View.renderNamedRegion(out, "ow_menu");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateContractDemoKeyFormularFNCM.text2", "When clicking next, the ids will be generated.")%></div>
			</div>
		</div>
	</div>
	<div id="OwDemoForm_Footer">
		&nbsp;
	</div>
</div>