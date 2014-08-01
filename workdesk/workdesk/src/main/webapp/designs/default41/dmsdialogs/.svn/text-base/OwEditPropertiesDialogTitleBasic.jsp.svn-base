<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
	import="com.wewebu.ow.server.ecm.*,
	    com.wewebu.ow.server.ui.*,
	    com.wewebu.ow.server.plug.owdocprops.OwEditPropertiesDialog,
	    com.wewebu.ow.server.plug.owdocprops.OwEditPropertiesDialogSimple"

%><%// get a reference to the calling view
    OwEditPropertiesDialogSimple m_View = (OwEditPropertiesDialogSimple) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<span class="OwEditProperties_Versionheader"> <%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.doclinktitle", "Properties:")%>&nbsp;&nbsp;
<%
    m_View.getMimeManager().insertIconLink(out, m_View.getItem());
    m_View.getMimeManager().insertTextLink(out, m_View.getItem().getName(), m_View.getItem());
 %>
</span>
<span class="OwEditProperties_Versiontext">
<%
 out.write(m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.typ", "Type:&nbsp;"));
 out.write(m_View.getItem().getObjectClass().getDisplayName(m_View.getContext().getLocale()));
 if ( m_View.getItem().hasVersionSeries() )  { 
     out.write("&nbsp;&nbsp;");
     if ( m_View.getItem().getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) )
     { 
         String title = m_View.getContext().localize("app.Checked_out", "Checked out");
       %><img src="<%=m_View.getContext().getDesignURL()%>/images/checkedout.png" title="<%=title%>" alt="<%=title%>"><%
     }
     out.write(m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.version","Version"));
     out.write(m_View.getItem().getVersion().getVersionInfo());
 }
%></span>