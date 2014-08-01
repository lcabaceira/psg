<%@ page 
    import="com.wewebu.ow.server.plug.owbpm.plug.*,
            com.wewebu.ow.server.ui.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwBPMResubmitDialog m_dialog= (OwBPMResubmitDialog)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div class="OwPropertyBlockLayout" id="OwMainContent">
	<div class="OwPropertyBlock">
		<div class="OwPropertyLabel"><%=m_dialog.getContext().localize("plug.owbpm.OwResubmit.resubmitdate","Resubmission Date")%>:</div>
       
		<div class="OwPropertyValue"><% m_dialog.renderRegion(out,OwBPMResubmitDialog.KEY_RESUBMIT_DATE); %></div>
       
       	<% // if an error occurred render it 
		if (m_dialog.getErrorEditField()!= null && m_dialog.getErrorEditField().length()>0)
		{ %>
			<span class="OwPropertyError">
			<%= m_dialog.getErrorEditField() %>
			</span>
		<% } %>
		</div>

		<div class="OwPropertyBlock">
			<div class="OwPropertyLabel"><%=m_dialog.getContext().localize("plug.owbpm.OwResubmit.newNote","New Note")%>:</div>   
			<div class="OwPropertyValue"><% m_dialog.renderNote(out); %></div>
		</div>
</div>