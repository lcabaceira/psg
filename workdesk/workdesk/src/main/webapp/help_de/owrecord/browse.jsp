<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Bereich Durchsuchen
</span>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/browse.png" alt="" title=""/><br><br>    
    Im Bereich Durchsuchen k&ouml;nnen Sie manuell durch den Aktenbestand navigieren, wie Sie es z.B. vom Windows Explorer 
    gewohnt sind. Die Aktenstruktur wird Ihnen dabei als Baum angezeigt. Um Unterordner anzuzeigen oder auszublenden, klicken Sie einfach auf das 
    Plus-Minus-Symbol vor der Akte oder dem Ordner. Um den Inhalt 
    eines (Unter-)Ordners zu sehen, klicken Sie auf den Ordnernamen. Der angezeigte Ordner wird hervorgehoben.
	<br><br>
	Die Ordneransicht zeigt den Inhalt des gew&auml;hlten Ordners. Die Dokumente lassen sich per Mausklick in einem
	Viewer oder ggf. in einem Bearbeitungprogramm &ouml;ffnen. 
    Au&szlig;erdem stehen - wie im Bereich Aktenverwaltung - verschiedene 
    Bearbeitungsfunktionen f&uuml;r Akten und Dokumente und verschiedene Listentypen zur Verf&uuml;gung, 
    die auf der Hilfeseite zur <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","Aktenverwaltung","OwHelpLink")%> ausf&uuml;hrlich 
    beschrieben werden. <br clear="all">
<br><br> 
</p>
