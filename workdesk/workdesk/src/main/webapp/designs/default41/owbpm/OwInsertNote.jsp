<%@ page
	import="com.wewebu.ow.server.plug.owbpm.plug.*,com.wewebu.ow.server.ui.OwView"
	autoFlush="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%
    // get a reference to the calling view
    OwBPMInsertNoteDialog m_dialog= (OwBPMInsertNoteDialog)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div id="OwMainContent">
	<div class="OwPropertyBlockLayout">
		<div class="OwPropertyBlock">
			<div class="OwPropertyLabel"><%=m_dialog.getContext().localize("plug.owbpm.OwBPMInsertNote.newNote","New Note")%>:</div>
			<div class="OwPropertyValue"><%m_dialog.renderNote(out); %></div>
		</div>
	</div>
</div>