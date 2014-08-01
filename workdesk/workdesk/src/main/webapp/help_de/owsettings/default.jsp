<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>Hilfe zum Bereich Einstellungen</span>

<p class=OwHelpIntro>
    <br>Jeder Nutzer kann den Alfresco Workdesk an seine pers&ouml;nlichen Bed&uuml;rfnisse anpassen.
    <br> F&uuml;r jeden verf&uuml;gbaren Bereich (z.B. Aktenverwaltung, Suche, ...) und f&uuml;r den Alfresco Workdesk allgemein k&ouml;nnen unterschiedliche Einstellungen
    vorgenommen werden. 
</p>

<p class=OwHelpText>
    Die allgemeinen Einstellung, die Sie hier vornehmen, gelten f&uuml;r den gesamten Alfresco Workdesk. Sie k&ouml;nnen hier zum Beispiel festlegen, in welchem Bereich der
    Wordesk nach dem Anmelden startet (Suche, Vorgangsbearbeitung, Aktenverwaltung, ...). <br> <br>
    Standardm&auml;&szlig;ig wird der Inhalt der Zwischenablage im Kopfbereich des Wordesk eingeblendet. Diese Anzeige k&ouml;nnen Sie hier ein- oder ausschalten.<br><br>
    Die Seitengr&ouml;&szlig;e bestimmt die Anzahl der Eintr&auml;ge in einer Dokumenten-, Vorgangs- oder Trefferliste. Existieren mehr Eintr&auml;ge als hier eingestellt, 
    erscheinen unterhalb der Liste Symbole, um zu den weiteren Eintr&uuml;gen zu gelangen (z.B. 1-20, 21-40, ...). 
    Die hier gew&auml;hlte Einstellung gilt f&uuml;r alle Listentypen gleicherma&szlig;en. <br><br>
    Eventuell stehen Ihnen hier weitere Einstellungsm&ouml;glichkeiten zur Verf&uuml;gung. Bei Fragen dazu wenden Sie sich bitte an Ihren Administrator. <br>
</p>

<span class=OwHelpTopic2>
    <br>Allgemeine Einstellungen<br>  
    <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/settings.png" alt="" title=""/><br>
</span>
<br>

<span class=OwHelpTopic2>
    <br>Einstellungen f&uuml;r unterschiedliche Bereiche
</span>
<br>
<br>

<p class=OwHelpText>
    Aus einer Liste verf&uuml;gbarer Attribute k&ouml;nnen Sie sich f&uuml;r jeden Bereich diejenigen Attribute ausw&auml;hlen, die dort als Spaltentitel
    erscheinen sollen. Mit den Pfeilsymbolen nach rechts und links f&uuml;gen Sie markierte Attribute hinzu oder entfernen sie. Mit den Pfeilsymbolen nach oben und 
    unten k&ouml;nnen Sie die Reihenfolge &auml;ndern. Der Spaltentitel, der in der Liste gew&auml;hlter Attribute ganz oben steht, erscheint sp&auml;ter als erste 
    Spalte links in der Ansicht.
	<br clear="all"><br>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/columns.png" alt="" title=""/>
</p>
<br>

<p class=OwHelpText>
    F&uuml;r den Bereich Vorgangsbearbeitung haben Sie au&szlig;erdem die M&ouml;glichkeit, einen Vertreter einzustellen. Dazu w&auml;hlen Sie einen Kollegen aus und 
    legen einen Zeitraum f&uuml;r die Vertretung fest. Wenn Sie die Vertretung aktivieren, werden alle Vorg&auml;nge aus Ihrer pers&ouml;nlichen Inbox auch 
    Ihrem Vertreter anzeigt.  
	<br clear="all"><br>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/proxy.png" alt="" title=""/>
</p>


<br>

<span class=OwHelpTopic2>
	Pers&ouml;nliche Einstellungen vornehmen
</span>
<br>
<p class=OwHelpText>
    <br>Wenn Sie die gew&uuml;nschten Einstellungen ver&auml;ndert haben, klicken Sie auf "Speichern". Damit werden Ihre Einstellungen &uuml;bernommen und stehen
    Ihnen sofort zur Verf&uuml;gung.
</p>
<br>

<span class=OwHelpTopic2>
	Zur&uuml;cksetzten auf Standardeinstellungen
</span>

<p class=OwHelpText>
    <br>Alle Einstellungen lassen sich auf die Standardwerte zur&uuml;cksetzen, indem Sie "Standardwerte" anklicken.
    Die Eingabefelder werden dann automatisch wieder mit den Standardwerten belegt.
</p>






