<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@page import="com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.CustomerFile.Title", "Customer File Properties")%></h1>
        <!-- <img src="<%=m_View.getContext().getDesignURL()%>/micon/briefcase2.png"/> -->
    </div>
        
    <!-- Main area -->
    <div id="OwDemoForm_MAIN">
        <div class="block">
          <div class="OwFormTable_Column">
			<table class="OwFormTable">
				<tr>
                    <td class="OwRequired">
                        <%
                        //if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{cmis:folder.cmis:name}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        //}
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{cmis:folder.cmis:name}", m_View.getContext().localize("jsp.CustomerFile.Attribute.Name","Company Name"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{cmis:folder.cmis:name}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CompanyOwner}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>

					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CompanyOwner}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CompanyOwner","Company Owner"), m_View,null);%>:</td>
					<td class="DefaultInput">
					   <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CompanyOwner}"); %>
                       </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerStreet}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>

					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerStreet}", m_View.getContext().localize("jsp.CustomerFile.Attribute.Street", "Street"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerStreet}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerZIPCode}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>

					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerZIPCode}", m_View.getContext().localize("jsp.CustomerFile.Attribute.ZIPCode", "ZIP Code"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerZIPCode}"); %>
                        </div>
                    </td>
                            
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerCity}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>

					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerCity}", m_View.getContext().localize("jsp.CustomerFile.Attribute.City", "City"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerCity}"); %>
                        </div>
                    </td>
				</tr>
				
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerCountry}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>

					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerCountry}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerCountry", "Country"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerCountry}"); %>
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
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerType}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerType}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerType", "Type"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerType}"); %>
                        </div>
                    </td>
				</tr>				
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerStatus}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerStatus}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerStatus", "Status"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerStatus}"); %>
                        </div>
                    </td>
				</tr>		
				
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerSince}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerSince}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerSince","Customer since"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerSince}"); %>
                        </div>
                    </td>
				</tr>
				
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{P:owdcm:CustomerIDAspect.owdcm:CustomerID}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{P:owdcm:CustomerIDAspect.owdcm:CustomerID}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerID","Customer ID"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{P:owdcm:CustomerIDAspect.owdcm:CustomerID}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerCP}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerCP}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerCPImage","Contact Person"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerCP}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerLogo}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerLogo}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerLogo","Customer Logo"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerLogo}"); %>
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
	     