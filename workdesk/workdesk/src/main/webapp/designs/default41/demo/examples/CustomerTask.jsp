<%@page import="com.wewebu.ow.server.app.OwInsertLabelHelper"%>
<%@page import="com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView"%>
<%@ page import="com.wewebu.ow.server.ui.*" autoFlush="true"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div id="OwDemoForm">
    <div id="OwDemoForm_Header">
        <h1><%=m_View.getContext().localize("jsp.CMTaskProperties.Title", "Customer Management Task Properties")%></h1>
        <!-- <img src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdocprops/edit_task_24.png"/> -->
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
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{cmis:folder.cmis:name}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.TaskName","Task Name"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{cmis:folder.cmis:name}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CT.owdcm:TaskType}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CT.owdcm:TaskType}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.TaskType","Task Type"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CT.owdcm:TaskType}"); %>
                        </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CT.owdcm:TaskPriority}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CT.owdcm:TaskPriority}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.Priority", "Priority"), m_View,null);%>:</td>
					<td class="DefaultInput">
					   <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CT.owdcm:TaskPriority}"); %>
					   </div>
                    </td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CT.owdcm:TaskDescription}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CT.owdcm:TaskDescription}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.TaskDescription","Task Description"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{F:owdcm:CT.owdcm:TaskDescription}"); %>
					    </div>
					</td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{P:owdcm:CDAssigneeAspect.owdcm:CDAssignee}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{P:owdcm:CDAssigneeAspect.owdcm:CDAssignee}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.Assignee", "Assignee"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{P:owdcm:CDAssigneeAspect.owdcm:CDAssignee}"); %>
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
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{P:owdcm:ProcessingStatusAspect.owdcm:ProcessingStatus}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{P:owdcm:ProcessingStatusAspect.owdcm:ProcessingStatus}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.Status", "Status"), m_View,null);%>:</td>
					<td class="DefaultInput">
					   <div class="OwPropertyControl">
					       <% m_View.renderNamedRegion(out, "{P:owdcm:ProcessingStatusAspect.owdcm:ProcessingStatus}"); %>
					   </div>
					</td>
				</tr>
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{P:owdcm:ResubmissionDateAspect.owdcm:ResubmissionDate}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{P:owdcm:ResubmissionDateAspect.owdcm:ResubmissionDate}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.ResubmissionDate", "Resubmission Date"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{P:owdcm:ResubmissionDateAspect.owdcm:ResubmissionDate}"); %>
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
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{P:owdcm:CustomerIDAspect.owdcm:CustomerID}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.CustomerID", "Customer ID"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">					
                            <% m_View.renderNamedRegion(out, "{P:owdcm:CustomerIDAspect.owdcm:CustomerID}"); %>
                        </div>
                    </td>
				</tr>	
				<tr>
                    <td class="OwRequired">
                        <%
                        if(((OwObjectPropertyFormularView)m_View).isFieldMandatory("{F:owdcm:CT.owdcm:AssociatedDocument}")) {
                            %><img src="<%=m_View.getContext().getDesignURL()%>/images/OwObjectPropertyView/required.png"/><%
                        }
                        %>
                    </td>
					<td class="OwPropertyName"><%OwInsertLabelHelper.insertLabel(out, "{F:owdcm:CT.owdcm:AssociatedDocument}", m_View.getContext().localize("jsp.CMTaskProperties.Attribute.AssociatedDocument", "Associated Document"), m_View,null);%>:</td>
					<td class="DefaultInput">
                        <div class="OwPropertyControl">
                            <% m_View.renderNamedRegion(out, "{F:owdcm:CT.owdcm:AssociatedDocument}"); %>
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