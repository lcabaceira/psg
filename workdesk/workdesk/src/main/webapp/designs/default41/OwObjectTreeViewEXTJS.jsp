<%@ page
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*,
            com.wewebu.ow.server.dmsdialogs.views.OwObjectTreeViewEXTJS"
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%
    // get a reference to the calling view
    OwObjectTreeViewEXTJS m_View = (OwObjectTreeViewEXTJS)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<script type="text/javascript">

	OwObjectTreeViewEXTJS.config = {
		m_mainURL: "<%=m_View.getContext().getBaseURL()%>",
		m_treeDataURL : "<%=m_View.getAjaxEventURL("GetTreeData",null)%>",
		m_treeCollapseURL : "<%=m_View.getAjaxEventURL("NodeCollapsed",null)%>",
		m_rootText : "<%=m_View.getRootText()%>",
		m_rootId : "<%=m_View.getRootId()%>",
		m_listViewURL: "<%=m_View.getExternalUpdateURL()%>",
		m_clientSideId: "<%=m_View.getClientSideId()%>",
		m_selectedId: "<%=m_View.getSelectedId()%>",
		m_selectedPathIds: <%=m_View.getSelectedPathIds()%>,
		m_rootIconClass: "<%=m_View.getRootIconClass()%>",
		m_rootIcon: "<%=m_View.getRootIcon()%>",
		m_useDynamicSplit: <%=m_View.isDynamicSplitUsed()%>,
		m_toBeUpdatedIds: <%=m_View.getUpdateComponentIds()%>,
		m_toBeUpdatedURLs: <%=m_View.getUpdateComponentsURLs()%>,
		m_useIconClass:<%=m_View.isIconClassInUse()%>,
		m_rootClosedIcon: "<%=m_View.getRootClosedIcon()%>",
		m_rootOpenedIcon: "<%=m_View.getRootOpenedIcon()%>",
		m_loadingMessage:"<%=m_View.getLoadingMessage()%> ",
		m_errorTitleText:"<%=m_View.getErrorTitleText()%>",
		m_updateErrorText:"<%=m_View.getUpdateErrorText()%>",
		m_requestTimeOutText:"<%=m_View.getRequestTimeOutText()%>",
		m_cannotLoadNodeText:"<%=m_View.getCannotLoadNodeText()%>",
		m_showStacktraceMessage: "<%=m_View.getShowStacktraceMessage()%>",
		m_collapseStacktraceMessage:"<%=m_View.getCollapseStacktraceMessage()%>",
		m_expandOnSelect:<%=m_View.isExpandOnSelect()%>,
		m_isSelectedNodeExpanded:<%=m_View.isSelectedNodeExpanded()%>,
		m_DNDappletUpdateURL:
		<%if (m_View.getDnDAppletURL()!=null) {%>"<%=m_View.getDnDAppletURL()%>"
		<%} else {%>
		null
		<%}%>
	};
	
</script>
<div id="extjs-tree-panel"></div>
