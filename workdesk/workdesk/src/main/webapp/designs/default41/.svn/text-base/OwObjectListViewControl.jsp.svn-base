<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*,
            com.wewebu.ow.server.ui.OwView,
            java.util.Collection,
			java.util.List,
            com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewControl"
    autoFlush="true"

%><%// get a reference to the calling view
    OwObjectListViewControl m_View = (OwObjectListViewControl)request.getAttribute(OwView.CURRENT_MODULE_KEY);

    List objectLists = m_View.getObjectLists();
    if (objectLists.size() > 1)
    {
%><h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwObjectListViewControl.accessibility.ButtonControl", "List of result list representation views")%></h3><%        

        m_View.renderButtons(out);
%>
   	<p class="accessibility"><a href="#owpaging" class="accessibility"><%=m_View.getContext().localize("jsp.OwObjectListViewControl.accessibility.PageControl", "Go to page control component")%></a></p>
<%     
   	}
%>