<%@ page 
    import="com.wewebu.ow.server.ecm.*,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.plug.owdocprops.OwEditPropertiesDialog" 
    autoFlush   ="true"
    pageEncoding="utf-8"
    contentType="text/html; charset=utf-8"
    language="java"
%><%
    // get a reference to the calling view
    OwEditPropertiesDialog m_View = (OwEditPropertiesDialog)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
		<span class="OwEditProperties_Versionheader">
			<%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.doclinktitle","Properties:")%>&nbsp;&nbsp;
<%          m_View.getMimeManager().insertIconLink(out,m_View.getItem());
            m_View.getMimeManager().insertTextLink(out,m_View.getItem().getName(),m_View.getItem());%>
		</span>

        <span class="OwEditProperties_Versiontext">
        <%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.typ", "Type:&nbsp;") + m_View.getItem().getObjectClass().getDisplayName(m_View.getContext().getLocale())
%><% 
            if ( m_View.getItem().hasVersionSeries() )  { 
                out.write("&nbsp;&nbsp;");
                if ( m_View.getItem().getVersion().isCheckedOut(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) )  {
                    String title = m_View.getContext().localize("app.Checked_out", "Checked out");
%>
		        <img src="<%=m_View.getContext().getDesignURL()%>/images/checkedout.png" title="<%=title%>" alt="<%=title%>">
<%              } 
                out.write(m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.version","Version"));
                out.write(m_View.getItem().getVersion().getVersionInfo());
            }%>
        </span>
<%       if ( m_View.getItem().hasVersionSeries() )  { %>
         <span class="OwEditProperties_Versionlinks">
	        <a title="<%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.getlatest","Selects current version")%>" class="OwEditPropertiesDialog_VersionAccessLink" href="<%=m_View.getLatestVersionURL()%>"><%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.lastversion","Last Version")%></a>
	        <a title="<%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.getrelease","Selects released version")%>" class="OwEditPropertiesDialog_VersionAccessLink" href="<%=m_View.getReleasedVersionURL()%>"><%=m_View.getContext().localize("owdocprops.OwEditPropertiesDialog.releasedversion","Released Version")%></a>
         </span>
<%       }%>