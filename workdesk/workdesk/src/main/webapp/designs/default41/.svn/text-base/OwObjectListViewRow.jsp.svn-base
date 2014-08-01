<%@page autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
	import="com.wewebu.ow.server.ui.*,
	com.wewebu.ow.server.app.*,
	java.util.List,
	com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewRow"
%><%
    // get a reference to the calling view
    OwObjectListViewRow m_View = (OwObjectListViewRow) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwMainAppContext m_context = (OwMainAppContext) m_View.getContext();  
    boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();

    if (m_View.getIsListValid())
    {
%>
       <div id="sticky_footer_in_use" class="<% if(m_View.getCount() == 0) out.println("OwGeneralList_empty"); %>">
       <div id="wrap">
<%
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
%>
			<table class="OwGeneralList_table" id="<%=m_View.getListViewID()%>">
				<caption>
<%
        m_View.renderTableCaption(out);
%>
				</caption>
				<thead>
<%
        m_View.renderSortHeader(out, m_View.getPluginEntries());
%>
				</thead>
<%
		if (m_View.getCount() != 0)
		{
%>
				<tbody class="OwObjectListView_body">
<%
            m_View.renderRows(out, m_View.getPluginEntries());
%>
				</tbody>
			</table>
      </div>
<%          if (m_View.hasPaging())
            {
%>      <div id="footer">
<%
		        m_View.renderPageSelector(out);
%>      </div>
<%
            }//paging if
       }
       else
       {
%>          </table>
      </div>
         <p id="emptyList" class="OwEmptyTextMessage"><%=m_View.getContext().localize("app.OwObjectListView.emptylist", "No items to display")%></p>
<%
       }
%>
    </div>
<%

    }
    else
    {
%>
     <p id="emptyList" class="OwEmptyTextMessage"><%=m_View.getContext().localize("app.OwObjectListView.nolist", "No items to display")%></p>
<%
    }
%>