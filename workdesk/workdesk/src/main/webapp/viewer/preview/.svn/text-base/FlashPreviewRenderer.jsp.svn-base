<%@page import="com.wewebu.ow.server.ui.preview.OwFlashPreviewRenderer"%>
<%@page import="java.util.Map"%>
<%@page import="com.wewebu.ow.server.ui.OwView"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"%>
<%
    @SuppressWarnings("unchecked")
    Map<String, Object> cfgMap = (Map<String, Object>)request.getAttribute(OwFlashPreviewRenderer.REQ_ATT_CFG);
    
    String flashvars = (String)cfgMap.get("flashvars");
    String wmode = (String)cfgMap.get("wmode");
    String allowfullscreen = (String)cfgMap.get("allowFullScreen");
    String width = (String)cfgMap.get("width");
    String height = (String)cfgMap.get("height");
    
    String quality = (String)cfgMap.get("quality");
    String allowNetworking = (String)cfgMap.get("allowNetworking");
    String allowScriptAccess = (String)cfgMap.get("allowScriptAccess");
%>

<!-- the bottom padding is a hack for IE, which for some obscure reason, will place a vertical scroll bar along the flash object -->
<div style="width: <%=width%>; height: <%=height%>; padding-bottom: 3px;">
	<object  classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
	    codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"
	    PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer"
	    type="application/x-shockwave-flash"
	    width="<%=width%>"
        height="<%=height%>"
	    align="top"
	    id="flashPreview">
	    
	        <param name="movie" value="./swf/WebPreviewer.swf"/>
	        <param name="quality" value="<%=quality%>"/>
	        <param name="allowNetworking" value="<%=allowNetworking%>"/>
	        <param name="allowScriptAccess" value="<%=allowScriptAccess%>"/>
	        <param name="allowFullScreen" value="<%=allowfullscreen%>"/>
	        <param name="wmode" value="<%=wmode%>"/>
	        <param name="flashvars" value=<%=flashvars%>/>
	            
	        <!--[if !IE]>-->
	        <object  type="application/x-shockwave-flash" data="./swf/WebPreviewer.swf"
	            width="<%=width%>"
	            height="<%=height%>"
	            align="top"
	            id="flashPreview">
	        <!--<![endif]-->
	            <param name="movie" value="./swf/WebPreviewer.swf"/>
	            <param name="quality" value="<%=quality%>"/>
	            <param name="allowNetworking" value="<%=allowNetworking%>"/>
	            <param name="allowScriptAccess" value="<%=allowScriptAccess%>"/>
	            <param name="allowFullScreen" value="<%=allowfullscreen%>"/>
	            <param name="wmode" value="<%=wmode%>"/>
	            <param name="flashvars" value=<%=flashvars%>/>
	        <!--[if !IE]>-->
	        </object>
	        <!--<![endif]-->
	</object>
</div>