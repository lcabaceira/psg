<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"
    pageEncoding="utf-8"
    contentType="text/html; charset=utf-8"
    language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
	<div id="OwDemoForm_Header">
	   	<h1>Invoices Documents</h1>
		<h1><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.editproperties", "Edit document properties demo")%></h1>
		<img
			alt="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			title="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
	</div>

	<!-- Main area -->
	<div id="OwDemoForm_MAIN">
		<div>
			<%
			    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
			    {
			        ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteAll(out);
			    }
			%>
		</div>
		<div class="block">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.firststep", "1. Step")%></div>
			<div class="right">
				<div class="owdemoprop">
						<%
						    OwInsertLabelHelper.insertLabel(out, "DocumentTitle", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.documenttitle", "Document title:"), m_View,null);
						    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
						    {
						        ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteProperty(out, "DocumentTitle");
						    }
					         m_View.renderNamedRegion(out, "DocumentTitle");
					%>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.documenttitle.hint", "Please type in the document title.")%></div>
				</div>
				<div class="owdemoprop">
						<%
						    OwInsertLabelHelper.insertLabel(out, "TestNote", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.note", "Note:"), m_View,null);
						    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
						    {
						        ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteProperty(out, "TestNote");
						    }
					        m_View.renderNamedRegion(out, "TestNote");
					%>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.note.hint", "Please type in the document notes.")%></div>
				</div>

			</div>
		</div>
		<div class="block">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.secondstep", "2. Step")%></div>
			<div class="right">
				<%
				    m_View.renderNamedRegion(out, "ow_menu");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesFNCM.text2", "When clicking save, the document properties will be updated.")%></div>
			</div>
		</div>
	</div>
	<div id="OwDemoForm_Footer">
		&nbsp;
	</div>
</div>