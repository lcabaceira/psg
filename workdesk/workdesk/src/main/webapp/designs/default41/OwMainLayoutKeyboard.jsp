<%@ page
	import="java.util.*,com.wewebu.ow.server.util.OwString,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.ui.ua.OwOSFamily,com.wewebu.ow.server.app.*"
	autoFlush="true" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8" language="java"%>
<%
// get a reference to the calling view
OwLayout m_View = (OwLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
OwMainAppContext context = (OwMainAppContext) m_View.getContext();

%>
   <h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.KEYBOARD_REGION", "Access keys for interaction.")%></h1>
<%
Collection<? extends OwEventDescription> keys = context.getEventDescriptions();
if (keys != null)
{
    Iterator<? extends OwEventDescription> it = keys.iterator();
    %>
<dl>
	<%
    while (it.hasNext())
    {
        OwEventDescription eventDescription =  it.next();
        
        String userAgentHeader = request.getHeader("user-agent");
        OwOSFamily osFamily=OwOSFamily.findFirst(userAgentHeader);

    %>
	<dt>
		[<%=eventDescription.getEventString(context.getLocale(),osFamily)%>]
	</dt>
	<dd><%=eventDescription.getDescription(context.getLocale(),osFamily)%></dd>
	<%
	 }
	%>
</dl>
<%
}
%>
