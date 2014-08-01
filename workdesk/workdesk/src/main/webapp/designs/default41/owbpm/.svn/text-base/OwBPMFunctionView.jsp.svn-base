<%@ page 
    import="com.wewebu.ow.server.ui.*,
		    com.wewebu.ow.server.ecm.OwObject,
		    com.wewebu.ow.server.app.OwDocumentFunction,com.wewebu.ow.server.plug.owbpm.OwBPMFunctionView" 
    autoFlush   ="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"
%><%
    // get a reference to the calling view
    OwBPMFunctionView m_View = (OwBPMFunctionView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<ul class="OwRecordRecordFunctionView">
<%
	// === draw links for each document function
      OwObject containerobject = m_View.getContainerObject();

	    if ( (m_View.getDocumentFuntionPlugins() != null) && (containerobject != null) )
	    {
	        for (int i=0;i < m_View.getDocumentFuntionPlugins().size();i++)
	        {
	            OwDocumentFunction docFunctionPlugin = (OwDocumentFunction)m_View.getDocumentFuntionPlugins().get(i);
	            
	            if ( ! m_View.getIsPluginEnabled(docFunctionPlugin) )
	                continue;
	
               %>  <li><%

                    %><a title="<%=docFunctionPlugin.getTooltip() %>" class="OwMainMenuItem" href="<%=m_View.getDocumentFunctionEventURL(i)%>"><%=docFunctionPlugin.getBigIconHTML(containerobject,null)%><%

                    %><span><%=docFunctionPlugin.getLabel(containerobject,null)%></span></a><%

                %></li>
<%         }
      }
%>
</ul>