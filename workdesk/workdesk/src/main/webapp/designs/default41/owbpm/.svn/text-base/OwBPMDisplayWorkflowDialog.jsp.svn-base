<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.wewebu.ow.server.ecm.OwContentCollection"%>
<%@page
	import="com.wewebu.ow.server.plug.owbpm.plug.OwBPMDisplayWorkflowDialog"%>
<%@ page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.ecm.OwObject,com.wewebu.ow.server.app.OwDocumentFunction,com.wewebu.ow.server.plug.owbpm.OwBPMFunctionView"
	autoFlush="true" pageEncoding="utf-8" language="java"
	contentType="text/html; charset=utf-8"%>
<%
    // get a reference to the calling view
    OwBPMDisplayWorkflowDialog m_View = (OwBPMDisplayWorkflowDialog) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<%
    String eventURL = m_View.getEventURL("Cancel", null);
    String closeLabel = m_View.getContext().localize("owbpm.OwBPMDisplayWorkflowDialog.closeBtn", "Close");
%>
<div
	style="margin-left: 10px; padding-bottom: 10px; margin-top: 15px; clear: left">
	<%
 	        
	        String did = m_View.getItem().getDMSID();
	        String contentURL = String.format("%s/getContent?dmsid=%s&cttyp=%s&page=0", m_View.getContext().getBaseURL(), OwAppContext.encodeURL(did), OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM);
	%>
	<div>
		<img src="<%=contentURL%>" alt="<%="Task id: " + did%>" id="<%= did%>"/>
	</div>
 
</div>
<div
	style="margin-left: 10px; padding-bottom: 10px; margin-top: 15px; clear: left">
	<input type="button" value="<%=closeLabel%>" name="Close"
		onclick="document.location='<%=eventURL%>'" />
</div>

<script type="text/javascript">
  Ext.onReady(function() {	
<%
    StringBuffer tooltip = new StringBuffer("<div>");
    List<String> tooltipLines = m_View.tooltipLinesFor(m_View.getItem());
    Iterator<String> linesIt = tooltipLines.iterator();
    while(linesIt.hasNext()) {
        String line = linesIt.next();
        tooltip.append(line);
        if(linesIt.hasNext()) {
            tooltip.append("<hr/>");
        }
    }
    tooltip.append("</div>");
    String id =  m_View.getItem().getDMSID();
%>
		new Ext.ToolTip (
				{
					target : '<%=id%>',
					html : '<%=tooltip%>'
				});
<%

%>
	}); // onReady()
	
</script>