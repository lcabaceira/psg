<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Bereich Suche
</span>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/search.png" style="vertical-align:middle;" alt="" title=""/><br><br>    
    Im Bereich Suche k&ouml;nnen Sie nach Dokumenten und Akten suchen. 
    Es stehen Ihnen dazu verschiedene Suchmasken zur Verf&uuml;gung:
    Aktensuche, Dokumentensuche und m&ouml;glicherweise weitere 
    benutzerspezifische Suchen.<br><br> 
</p>
<p class="OwHelpText">
    W&auml;hlen Sie die gew&uuml;nschte Suche in der Navigationsleiste (meistens links) aus. 
    Zur Eingabe der Suchkriterien erscheint auf der rechten Seite die entsprechende Suchmaske.
    <br><br><br>
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        Suchkriterien<br>   
    </span>
</a>

<p class="OwHelpText">
    Je nach Art der Suchmaske k&ouml;nnen Sie nach verschiedenen Kriterien suchen 
    (z.B. Aktenname, Dokumententitel, Kundennummer, Datum der letzten &Auml;nderung, etc.). 
    Sie k&ouml;nnen auch Platzhalter verwenden, um einzelne oder beliebig viele Zeichen zu ersetzen. 
    Welche Zeichen Sie als Platzhalter nutzen k&ouml;nnen (z.B.: *, ?, %, ...), erfahren Sie von Ihrem Administrator.  
	<br><br>
    Geben Sie die gew&uuml;nschten Eigenschaften in die Suchmaske ein. 
    Wenn Sie die eingegebenen Suchkriterien l&ouml;schen wollen, klicken Sie auf "Zur&uuml;cksetzen". <br>
    <br>
    Wenn Sie auf "Suchen" klicken, erscheint die Trefferliste mit den Dokumenten oder Akten, 
    die Ihren Suchkriterien entsprechen. Wenn Sie keine Suchkriterien angegeben haben, werden alle 
    vorhandenen Akten bzw. Dokumente aufgelistet.<br clear="all"><br><br>
</p>

<a name="#DocListViewModule">
    <span class=OwHelpTopic2>
        Trefferliste
    </span>
</a>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/trefferliste.png" style="vertical-align:middle;" alt="" title=""/><br><br>
    Die Trefferliste zeigt alle Akten oder Dokumente an, die den angegebenen 
    Suchkriterien entsprechen. Sie k&ouml;nnen die Treffer per Klick auf den jeweiligen Spaltentitel 
    (z. B. nach Name, Kundennummer usw.) auf- bzw. absteigend sortieren.<br><br>
    Klicken Sie in der Trefferliste auf ein Dokument, wird es im Viewer ge&ouml;ffnet.  
    Sie k&ouml;nnen in der Trefferliste &auml;hnlich wie in der Dokumentenliste der Aktenverwaltung 
    die Dokumente bearbeiten. Welche Funktionen Ihnen bereits in der Trefferliste (als Icon oder im 
    Kontextmen&uuml;) zur Bearbeitung zur Verf&uuml;gung stehen, h&auml;ngt von Ihrer Nutzerrolle ab. 
    Mehr &uuml;ber einzelne Funktionen erfahren Sie in der Hilfe zur 
    
    <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","Aktenverwaltung","OwHelpLink")%>.
    
    <br><br>
    Klicken Sie in der Trefferliste auf eine Akte, wird diese in der Aktenverwaltung ge&ouml;ffnet. 
    Wenn Sie zur Suche zur&uuml;ckkehren, sehen Sie wieder die letzte Trefferliste. Klicken Sie 
    auf die zuvor genutze Suchmaske, werden Ihre Eingaben als Vorschlagswerte angezeigt.<br><br>   
</p>


<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        <br>Tipps zur Suche<br>   
    </span>
</a>

<p class="OwHelpText">
    Um die Suche zu beschleunigen, k&ouml;nnen Sie in den Suchmasken die "Maximale Trefferanzahl" angeben. 
    Wenn die von Ihnen gesuchten Objekte dann nicht in der Trefferliste erscheinen, k&ouml;nnen Sie die
    Suche verfeinern. <br><br> 
    Au&szlig;erdem haben Sie die M&ouml;glichkeit, oft ben&ouml;tigte Suchen zu speichern. Dazu tragen Sie in der 
    Trefferliste (siehe Abbildung oben) einen Namen f&uuml;r die neue Suche an und klicken auf "Speichern". Diese Suche wird 
    Ihnen dann in einer Auswahlbox der jeweiligen Suchmaske angezeigt. 
    <br clear="all"><br><br>
</p>
