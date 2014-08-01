<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true" 
   import="com.wewebu.ow.server.ui.*,
           com.wewebu.ow.server.plug.owrecord.*" 
%><%
    // get a reference to the calling view
    OwRecordSubLayout m_View = (OwRecordSubLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
	com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext)com.wewebu.ow.server.ui.OwWebApplication.getContext(session);
%>
   <jsp:include page="OwRecordMenu.jsp" flush="true"/>
   <div id="OwSubLayout_Div">
		<div id="OwRecordSubLayoutRegister_NAVIGATION">
            <%-- render navigation as tree view 
            m_View.renderRegion(out,OwRecordSubLayout.NAVIGATION_REGION);
            render navigation as register view --%>
            <% m_View.renderRegion(out,OwRecordSubLayout.NAVIGATION_REGION_REGISTER_MODE); %>
		</div>
		<div id="OwSubLayout_MAIN" class="OwSubLayoutRegister_MAIN">
		    <% m_View.renderRegion(out,OwRecordSubLayout.MAIN_REGION); %>
		</div>
    </div>