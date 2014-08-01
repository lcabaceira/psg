<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@page import="com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.ContactPersonProperties.Title", "Contact Person Properties")%></h1>
        <!-- <img src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdocprops/edit_customer_24.png"/> -->
    </div>
        
    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
        <div class="block">
          <div class="OwFormTable_Column">
			<table class="OwFormTable">
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CP.owdcm:CustomerCPPicture}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CP.owdcm:CustomerCPPicture}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.ContactPersonImage","Picture"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CP.owdcm:CustomerCPPicture}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{cmis:folder.cmis:name}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{cmis:folder.cmis:name}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.FullName","Full Name"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{cmis:folder.cmis:name}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CP.owdcm:CustomerCPFirstName}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>				
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CP.owdcm:CustomerCPFirstName}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.FirstName","First Name"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CP.owdcm:CustomerCPFirstName}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CP.owdcm:CustomerCPLastName}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CP.owdcm:CustomerCPLastName}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.LastName", "Last Name"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CP.owdcm:CustomerCPLastName}"); %>
                        </div>
                    </td>
				</tr>
			</table>
			</div>
			<div class="OwFormTable_Column">
			<table class="OwFormTable">
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CP.owdcm:CustomerCPPhone}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CP.owdcm:CustomerCPPhone}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.Phone", "Phone"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CP.owdcm:CustomerCPPhone}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CP.owdcm:CustomerCPMobile}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CP.owdcm:CustomerCPMobile}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.Mobile", "Mobile"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CP.owdcm:CustomerCPMobile}"); %>
                        </div>
                    </td>
				</tr>	
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CP.owdcm:CustomerCPMail}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CP.owdcm:CustomerCPMail}", m_View.getContext().localize("jsp.ContactPersonProperties.Attribute.EMail", "E-Mail"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CP.owdcm:CustomerCPMail}"); %>
                        </div>
                    </td>
				</tr>			
			</table>
	       </div>
	   </div>
	</div>

    <div id="OwDemoForm_Footer">
        <% m_View.renderNamedRegion(out, "ow_menu"); %>
    </div>
</div>
	     