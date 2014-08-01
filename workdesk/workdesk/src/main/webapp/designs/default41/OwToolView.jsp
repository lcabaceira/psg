<%@ page
	import="java.util.Iterator,java.util.*,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.dmsdialogs.views.OwToolView,com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem"
	autoFlush="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwToolView m_View = (OwToolView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<!-- <div class="OwMainColumnHeader">&nbsp;</div> -->
<div class="OwToolView">
<%
	    Collection items = m_View.getToolItems();
	    List indexedCollection = new ArrayList(items);
	    int itemsCount = items.size();
	    if (itemsCount > 0)
	    {
	        int columnItemsCount = m_View.getColumnItemsCount();
	        int noOfColumns = (int) Math.ceil(((double) itemsCount) / ((double) columnItemsCount));

	        for (int i = 0; i < columnItemsCount; i++)
	        {
	            OwToolViewItem[] rowItems = new OwToolViewItem[noOfColumns];
	            for (int j = 0; j < rowItems.length; j++)
	            {
	                int itemIndex = j * columnItemsCount + i;
	                if (itemIndex < itemsCount)
	                {
	                    rowItems[j] = (OwToolViewItem) indexedCollection.get(itemIndex);
	                }
	            }
%>
	<div class="OwToolRow">
<%
			    for (int j = 0; j < rowItems.length; j++)
				{
			        if (rowItems[j] != null)
			        {
%>
        <a href="<%=m_View.createToolItemClickURL(rowItems[j])%>" title="<%=rowItems[j].getTitle()%>">
		<div class="OwToolItem">
			<div class="OwToolItem_title">
<%
				    if (rowItems[j].getBigIcon() != null)
					{
%>
				<div class="OwToolItem_title_icon" style="background-image: url(<%=rowItems[j].getBigIcon()%>); height: 24px; width: 24px;">
				</div>
<%
				    }
%>
				<div class="OwToolItem_title_text">
					<%=rowItems[j].getTitle()%>
				</div>
			</div>
			<div class="OwToolItem_description">
				<p><%=rowItems[j].getDescription()%></p>
			</div>
		</div>
        </a>
<%
			    	}
				}
%>
	</div>
<%
	    	}
	    }
%>
</div>
