<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Bereich Favoriten
</span>

<p class="OwHelpText">
	<br>Hier finden Sie Links auf Akten und Dokumente, die Sie im Bereich der <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","Aktenverwaltung","OwHelpLink")%> zu Ihren Favoirten hinzugef&uuml;gt haben.<br><br>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bookmark1.png" alt="" title=""/><br>
	Klicken Sie auf ein Dokument, &ouml;ffnet es sich im Viewer. Beim Klick auf eine Akte &ouml;ffnet sich diese in der Aktenverwaltung. 
</p>

<p class="OwHelpText">
	&Uuml;ber das Icon <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bookmark_delete.png" alt="<%=m_View.getContext().localize("help.images.bookmark_delete.gif","Delete bookmark image")%>" 
	title="<%=m_View.getContext().localize("help.images.bookmark_delete.gif","Delete bookmark image")%>"/> k&ouml;nnen Sie den Link auf das Dokument bzw. auf die Akte 
	wieder entfernen. 
</p>

<p class="OwHelpText">
    <br>Ausserdem finden Sie hier gespeicherte Suchen, die Sie im Bereich der <%=((OwInfoView)m_View).getLink("owsearch/default.jsp","Suche","OwHelpLink")%> gespeichert und zu Ihren Favoriten hinzugef&uuml;gt haben.<br><br>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bookmark2.png" alt="" title=""/><br>
    Klicken Sie auf eine gespeicherte Suche, wird die Suche erneut ausgef&uuml;hrt. 
</p>

<p class="OwHelpText">
    &Uuml;ber das Icon <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/storedsearch_delete.png" alt="" title=""/> k&ouml;nnen Sie die gespeicherte Suche l&ouml;schen.
</p>