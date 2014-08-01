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
    OwBPMDisplayWorkflowDialog m_View = (OwBPMDisplayWorkflowDialog)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
        <span class="OwEditProperties_Versionheader">
            <%=m_View.getContext().localize("owbpm.OwBPMDisplayWorkflowDialog.title","Workflow Name")%>:&nbsp;
<%          m_View.getMimeManager().insertIconLink(out,m_View.getItem());
            m_View.getMimeManager().insertTextLink(out,m_View.getItem().getName(),m_View.getItem());%>
        </span>
        
         <span class="OwEditProperties_Versiontext">
        <%
        List<String> tooltipLines = m_View.tooltipLinesFor(m_View.getItem());
        Iterator<String> linesIt = tooltipLines.iterator();
        while(linesIt.hasNext()) {
            %><%=linesIt.next()%>&nbsp;&nbsp;<%
        }
        %>
        </span>

        