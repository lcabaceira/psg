<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@page import="com.wewebu.ow.server.ui.*" autoFlush="true"
    pageEncoding="utf-8"
    contentType="text/html; charset=utf-8"
    language="java"%>
<%@page import="com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>


<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.questionnaire", "Personnel Questionnaire")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
    </div>
        
    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
    	<div>
          	<%
          	if ( m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView)m_View).isPasteMetadataActivated()) {
          	  ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView)m_View).renderPasteAll(out); 
          	}
          	%>
    	</div>
        <div class="block">
          <div class="left">
			<% m_View.renderNamedRegion(out,"Image"); %><br />
			<%
			 OwInsertLabelHelper.insertLabel(out, "DateOfBirth", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.dateofbirth","Date of birth:"), m_View,"info");
			 m_View.renderNamedRegion(out,"DateOfBirth");m_View.renderNamedRegion(out,"ow_err_DateOfBirth");
			 
			 OwInsertLabelHelper.insertLabel(out, "BirthPlace", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.placeofbirth","in:"), m_View,"info");
			 out.write(" ");
			 m_View.renderNamedRegion(out,"BirthPlace");
			 
			 OwInsertLabelHelper.insertLabel(out, "Nationality", m_View.getContext().localize("","Nationality:"), m_View,"info");
			 m_View.renderNamedRegion(out,"Nationality");
			 
			 OwInsertLabelHelper.insertLabel(out, "Gender", m_View.getContext().localize("","Gender:"), m_View,"info");
			 m_View.renderNamedRegion(out,"Gender");
			%>
			
          </div>
          <div class="right">
			<table class="OwFormTable">
				<tr>
					<td class=OwPropertyName><%OwInsertLabelHelper.insertLabel(out, "ow_Filename", m_View.getContext().localize("","File name:"), m_View,null);%></td>
					<td class="DefaultInput">
					<% m_View.renderNamedRegion(out,"ow_Filename"); %>
					</td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "FirstName", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.firstname", "First name:"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "FirstName"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "LastName", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.lastname", "Last name:"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "LastName"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "Department", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.department", "Department:"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "Department"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "Addres", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.address", "Address:"), m_View,null);%>
			          	<%if ( m_View instanceof com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView && ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView)m_View).isPasteMetadataActivated()) {
							 ((com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView)m_View).renderPasteProperty(out,"Address");
						 }
						 %>
					</td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "Address"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "TelNumber", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.phone", "Phone:"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "TelNumber"); %></td>
				</tr>
				<tr>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "FaxNumber", m_View.getContext().localize("jsp.demo.OwDocumentPropertiesForm.fax", "Fax:"), m_View,null);%></td>
					<td class="DefaultInput"><% m_View.renderNamedRegion(out, "FaxNumber"); %></td>
				</tr>
			</table>
	       </div>
	   </div>
	</div>
    <div id="OwDemoForm_Footer"><% m_View.renderNamedRegion(out, "ow_menu"); %></div>
</div>
	                

