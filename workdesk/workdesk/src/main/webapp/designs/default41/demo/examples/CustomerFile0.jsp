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
                        //if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CF.owdcm:CustomerCountry}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        //}
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CF.owdcm:CustomerCountry}", m_View.getContext().localize("jsp.CustomerFile.Attribute.CustomerCountry", "Country"), m_View,null);%></td>
					<td class="DefaultInput">
					   <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CF.owdcm:CustomerCountry}"); %>
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
	     