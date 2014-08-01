<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true" 
    import="java.util.Iterator,
			com.wewebu.ow.server.ui.OwView,
			com.wewebu.ow.server.ui.OwAppContext,
			com.wewebu.ow.server.util.OwHTMLHelper,
			com.wewebu.ow.server.plug.owrecord.OwRecordSetView,
			com.wewebu.ow.server.plug.owrecord.OwRecordSetView.OwObjectSetEntry"
 %><%
    // get a reference to the calling view
    OwRecordSetView m_View = (OwRecordSetView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    Iterator it = m_View.getRecentObjectList().iterator();
%>
   <p class="OwRecordSetView">
      <span class="OwHotKeyBar_Label"><%= m_View.getContext().localize("owrecord.OwRecordSetView.lastopendrecords", "Recently opened files:") %>&nbsp;&nbsp;</span>
<%
            int i=0;
            while ( it.hasNext() )
            {
                OwObjectSetEntry entry = (OwObjectSetEntry)it.next();
                i++;
%>          <span class="OwHotKeyBar_Items"><%
                    String strEventURL = m_View.getEventURL(OwRecordSetView.QUERY_KEY_OPEN_OBJECT, OwRecordSetView.QUERY_KEY_OBJECT_HASHCODE + "=" + String.valueOf(entry.hashCode()));
                    String strTitle = OwHTMLHelper.encodeToSecureHTML(entry.getName());
                    strTitle += " (CTRL + " + String.valueOf(i) + ")";

                  %><a title="<%= strTitle %>" href="<%= strEventURL %>"><%=OwHTMLHelper.encodeToSecureHTML(entry.getName())%></a><%
                  // register key event
                    m_View.getContext().registerKeyEvent('0' + i, OwAppContext.KEYBOARD_CTRLKEY_CTRL, strEventURL, entry.getName() );
              %></span><%
                if ( it.hasNext() )
                {
                    // write delimiter
            %><span class="OwHotKeyBar_delimiter"></span>
<%
                }
            }
%>
   </p>