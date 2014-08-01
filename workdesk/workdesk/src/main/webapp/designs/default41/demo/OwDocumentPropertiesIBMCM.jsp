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
		<h1><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.editproperties", "Edit document properties demo")%></h1>
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
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.firststep", "1. Step")%></div>
			<div class="right">
				<div class="owdemoprop">
						<%
						    OwInsertLabelHelper.insertLabel(out, "name", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.documenttitle", "Document title:"), m_View,null);
						    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
						    {
						        ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteProperty(out, "SC_Name");
						    }
					        m_View.renderNamedRegion(out, "name");
					%>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.documenttitle.hint", "Please type in the name.")%></div>
				</div>
                <div class="owdemoprop">
                        <%
                            OwInsertLabelHelper.insertLabel(out, "documentPersonnelNumber", m_View.getContext().localize("jsp.demo.OwCreateRecordForm.personnelNumber", "Personnel Number:"), m_View,null);
                            if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
                            {
                                ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteProperty(out, "{S_address.S_street}");
                            }
                     
                             //m_View.renderNamedRegion(out, "{S_address.S_street}"); //for Attribute Groups use {<>.<>}
                             m_View.renderNamedRegion(out, "documentPersonnelNumber");
                    %>
                    <div class="info"><%=m_View.getContext().localize("jsp.demo.OwCreateRecordForm.personnelNumber.info", "Please type the personnel number.")%></div>
                </div>
				<div class="owdemoprop">
						<%
						    OwInsertLabelHelper.insertLabel(out, "NoteLog", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.note", "Note:"), m_View,null);
						    if (m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).isPasteMetadataActivated())
						    {
						        ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView) m_View).renderPasteProperty(out, "NoteLog");
						    }
					        m_View.renderNamedRegion(out, "NoteLog");
					%>
					<div class="info"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.note.hint", "Please type in the document notes.")%></div>
				</div>
			</div>
		</div>
		<div class="block">
			<div class="left"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.secondstep", "2. Step")%></div>
			<div class="right">
				<%
				    m_View.renderNamedRegion(out, "ow_menu");
				%>
				<div class="info"><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesIBMCM.text2", "When clicking save, the document properties will be updated.")%></div>
			</div>
		</div>
	</div>
	<div id="OwDemoForm_Footer">
		&nbsp;
	</div>
</div>