<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*,
            com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewThumbnails"
    autoFlush   ="true"
 %><%
    // get a reference to the calling view
    OwObjectListViewThumbnails m_View = (OwObjectListViewThumbnails)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwMainAppContext m_context = (OwMainAppContext) m_View.getContext();  
    boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();

    if(useHtml5DragAndDrop)
    {
%>
     <script type="text/javascript">
         var dropArea = document.getElementById('OwSublayout_ContentContainer');
         if(typeof addHtml5DropArea == 'function') {
            addHtml5DropArea(dropArea, false);
         }
     </script>
<%
   }
   if ( m_View.getIsList() )  
   { 
%>
<div id="wrap">
   <div class="OwObjectListViewThumbnails_area" id="<%=m_View.getListViewID()%>">
      <div class="OwObjectListViewThumbnails_nav">
<%	   		int thumbnailTypesCount = m_View.getThumbnailTypeCount();
	   		if (thumbnailTypesCount > 1)
	   		{
	   			for(int i = 0; i < thumbnailTypesCount; i++) 
       			{
%>          		<a href="<%=m_View.getThumbnailTypeUrl(i)%>"><%=m_View.getThumbnailTypeDisplayName(i)%></a>&nbsp;<%
       			}
	   		}
%>      </div>
<%     m_View.renderRegion(out, OwObjectListViewThumbnails.THUMBNAIL_LIST);%>
   </div>
</div>
<%     if (m_View.hasPaging() && m_View.isRegion(OwObjectListViewThumbnails.PAGE_BUTTONS) ) 
       {
%>
  <div id="footer">
<%       m_View.renderRegion(out,OwObjectListViewThumbnails.PAGE_BUTTONS); %>
  </div>
<%
       }
       m_View.renderRegion(out,OwObjectListViewThumbnails.CONTEXT_MENU);
   }
   else
   {
%>
	<span id="emptyList" class="OwEmptyTextMessage"><%=m_View.getContext().localize("app.OwObjectListView.emptylist", "No items to display")%></span>
<% } %>