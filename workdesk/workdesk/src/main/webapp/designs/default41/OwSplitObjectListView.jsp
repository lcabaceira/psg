<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.ui.*, 
    com.wewebu.ow.server.app.*, 
    com.wewebu.ow.server.util.*,
    com.wewebu.ow.server.dmsdialogs.views.OwSplitObjectListView" 
    autoFlush="true"
%><%
OwSplitObjectListView view = (OwSplitObjectListView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="OwSplitObjectListView">
<%
	int count = view.getSplitCount();
	for(int i=0;i<count;i++)
	{
	    out.write("<fieldset>");
		if (view.displaySplitName(i))
		{
%>
			<legend><span class="OwSplitObjectListViewName"><%=view.getSplitName(i)%></span></legend>
<%
		}
	    view.renderRegion(out,i);
	    out.write("</fieldset>");
	}	
%>
</div>