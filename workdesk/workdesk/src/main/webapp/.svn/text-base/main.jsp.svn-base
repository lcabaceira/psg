<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.app.OwAboutMessageHelper,
    com.wewebu.ow.server.ui.OwWebApplication,
    com.wewebu.ow.server.ui.OwView,
    java.util.List"

%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<%@ include file="headinc.jsp" 
%><title><%=m_context.localize("app.OwMainAppContext.apptitle","Alfresco Workdesk")%><%
/* get a reference to the login view */
OwView m_mainview = (OwView) OwWebApplication.getMainView(session);
boolean useExtJsForDragAndDrop = m_context.getConfiguration().isUseExtJsForDragAndDrop();
boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();

if (m_context.isLogin())
{
    out.write(" - ");
    out.write(m_mainview.getBreadcrumbPart());
}
%></title>
</head>
<body id="<%=OwWebApplication.BODY_ID%>" onLoad="OwOnDocumentLoad();" class="owBody <%=m_context.getSafeCurrentMasterPluginStyleClassID() %> <%= !m_context.isLogin() ? "OwLogin" : ""%>">
   <script type="text/javascript">
var about_translated_messages = about_translated_messages
    || {
        "aboutworkdeskdialog.Messages.btnOk" : "<%=m_context.localize("aboutworkdeskdialog.Messages.btnOk", "OK")%>",
        "aboutworkdeskdialog.Messages.title" : "<%=m_context.localize("aboutworkdeskdialog.Messages.title", "About")%>"
    };
    Ext.apply(aboutworkdeskdialog.Messages, about_translated_messages);
    </script>
    <% 
    List<String> contributors=OwAboutMessageHelper.getContributors(m_context);
    %>
<%-- render the body of the webapplication --%><% 
			// render appropriate page according to logged in state
	        if ( ! m_context.isLogin() )  
	        {
	            /* get a reference to the main view */
	            OwView m_loginview = (OwView) OwWebApplication.getLoginView(session);
	        // === still no credentials available, display login page
	            // handle loginview response
	            m_loginview.render(out);
	 		}
	 		else
	 		{
	        // === user is logged in now
	            // handle main view response
	            m_mainview.render(out);
	        }
    	%><%-- render keyboard handling script --%>
    	<div id="registeredKeysScripts" class="hidden">
    		<jsp:include page="<%=m_context.getDesignDir()+\"/OwKeyboardScriptsRenderer.jsp\"%>"/>
   		</div>
<%-- on load handler scripts go here --%>
   <script type="text/javascript">
    function OwOnDocumentLoad()
    {
   	   self.focus();
<%--
       // the following line will regain the keyboard focus if the drag and drop 
       // applet catches the focus in IE6 and thus deactivates the keyboard commands
       //-----< start uncommenting here >-----
       // window.setTimeout(function(){if(!prevent_focus_regainment){document.body.focus();}},500);
       //-----< stop uncommenting here >-----
--%><%
       String script = m_context.getFinalScript();
       if ( null != script ) 
       { 
              out.println(script);
              m_context.clearFinalScript();
              
       }

       if ( m_context.getFocusControlID() != null )  
       {
%>
			setFocus('<%=m_context.getFocusControlID()%>');
<% 
       } 
%>
	}
<%
if(null != m_context.getFocusControlID()) {
%>
	if(Ext) {
		Ext.onReady(function(){
		    setFocus('<%=m_context.getFocusControlID()%>');
		});
	}
<%
}
%>
   </script>
<%    m_context.clearFocusControlID(); 
%><%-- enable a dump of the session data and the view system 
     m_context.dump(out); 
--%><%-- this frame is just used by the common.js:navigateHREF() --%>
<iframe id="serviceFrame" src="" style="visibility:hidden; height: 0px; width: 0px;">
</iframe>
    <div id="about-contributions" class="about-contributions" style="display: none;">
        <div id="about-contributions-content">
        <%=contributors.get(0)%>
        <br/>
        <% 
        for(int i=1;i<contributors.size();i++){
         %>
         <br/><%=contributors.get(i)%>
       <%  
        }
        %>
        </div>
    </div>
    <div id="about-logo" style="display: none;">
     <img src="<%=m_context.getDesignURL()%>/images/OwGuiLayout/<%=m_context.localize("image.workdesklogo","OW_Logo1.png")%>" id="OwHeaderView_Logo1" alt="<%=m_context.localize("image.ow_logo1.gif","Workdesk Logo")%>" title="<%=m_context.localize("image.ow_logo1.gif","Workdesk Logo")%>"/>
    </div>
</body>
</html>