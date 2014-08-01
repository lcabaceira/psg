<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*, com.wewebu.ow.server.util.*" 
    autoFlush   ="true"
    pageEncoding="utf-8"
    contentType="text/html; charset=utf-8"
    language="java"
%>

<%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
    <div class="OwStandardDialog" id="OwEditPropertiesFormularDialog">    
    <div id="OwStandardDialog_Header">
    <div id="OwStandardDialog_BTN">
        <span class="OwStandardDialog_CLOSE_BTN"><% m_View.renderRegion(out,OwStandardDialog.CLOSE_BTN_REGION); %></span>
             <% if ( m_View.isRegion(OwStandardDialog.HELP_BTN_REGION) ) { %>
            <span class="OwStandardDialog_CLOSE_BTN"><% m_View.renderRegion(out,OwStandardDialog.HELP_BTN_REGION); %></span>
         <% } %>
        </div>
    <div class="break"></div>
    </div>
   
        <div class="OwStandardDialog_MAIN"><% m_View.renderRegion(out,OwStandardDialog.MAIN_REGION); %></div>
        
    </div>
    

