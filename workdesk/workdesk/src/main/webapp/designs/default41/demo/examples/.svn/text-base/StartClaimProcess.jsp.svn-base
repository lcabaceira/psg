<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@page import="com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.BPMStartClaimReview.pageref", "Start Claim Handling")%></h1>
        <img alt="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" title="<%=m_View.getContext().localize("image.alfresco.png","Alfresco Logo")%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco_flower.png" />
    </div>

    <!-- Main area -->
    <div id="OwDemoForm_MAIN"><br/><br/>
		<table class="OwFormTable OwNowrapLabel">
			<tr>				
				<td colspan="3" class="DefaultInput" style="color:#FF8400"><b><%=m_View.getContext().localize("owlabel.D:bpm:task.bpm:desc16", "&#8658; Describe the reason for the claim and set the priority:")%></b><br/><br/></td>
			</tr>
			<tr>
                <td class="OwRequired">
                    <%
                    if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{D:bpm:task.bpm:comment}")) {
                        %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                    }
                    %>
                </td>
				<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:comment}", m_View.getContext().localize("owlabel.D:bpm:task.bpm:comment", "Claim Description"), m_View,null);%>:</td>
				<td class="DefaultInput">
                    <div class="OwPropertyControl">
                        <% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:comment}"); %>
                    </div>
                </td>
			</tr>
			<tr>
                <td class="OwRequired">
                    <%
                    if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{D:bpm:task.bpm:priority}")) {
                        %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                    }
                    %>
                </td>
				<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{D:bpm:task.bpm:priority}", m_View.getContext().localize("owlabel.D:bpm:task.bpm:priority", "Priority"), m_View,null);%>:</td>
				<td class="DefaultInput">
                    <div class="OwPropertyControl">
                        <% m_View.renderNamedRegion(out, "{D:bpm:task.bpm:priority}"); %>
                    </div>				
				</td>
			</tr>
		</table>
	</div>
	
	<div id="OwDemoForm_Footer">
        <% m_View.renderNamedRegion(out, "ow_menu"); %>
	</div>		
</div>

