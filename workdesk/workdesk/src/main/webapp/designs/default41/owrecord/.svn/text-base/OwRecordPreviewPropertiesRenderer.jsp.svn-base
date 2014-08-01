<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	        com.wewebu.ow.server.ecm.*,
	        com.wewebu.ow.server.plug.owrecord.*"
%><%
    // get a reference to the calling view
    OwRecordPreviewPropertyView m_View = (OwRecordPreviewPropertyView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    if (m_View != null && m_View.getPropertyNames() != null)
    {
%>
<table>
<%	    for (int i = 0; i < m_View.getPropertyNames().size(); i++)
        {
	            String strPropertyName = (String) m_View.getPropertyNames().get(i);

	            // lookup first in rootfolder, then in selected folder
	            OwProperty property = m_View.getProperty(strPropertyName);

	            if (null != property)
	            {
%>
	<tr>
		<th scope="row">
			<%=property.getPropertyClass().getDisplayName(m_View.getContext().getLocale())%>:
		</th>
		<td>
<%			    m_View.renderProperty(property, out);%>
		</td>
	</tr>
<%
                }
         }
%>
</table>
<%
    }
%>