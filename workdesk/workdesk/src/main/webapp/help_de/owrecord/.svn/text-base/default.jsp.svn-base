<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Bereich Aktenverwaltung
</span>

<p class=OwHelpIntro>
	<br>Im Bereich "Aktenverwaltung" verwalten Sie Ihre Akten &uuml;bersichtlich und komfortabel. 
    Um eine Akte zu &ouml;ffnen, klicken Sie einfach deren Titel an: entweder in der Trefferliste einer Suche, in der Anlage eines Vorgangs oder
    direkt im Bereich Aktenverwaltung aus der Liste der zuletzt ge&ouml;ffneten Akten. Die 
    letzten vier Akten, die Sie ge&ouml;ffnet hatten, werden dort gespeichert und meistens 
	oberhalb der Aktenansicht angezeigt. Diese Liste steht Ihnen auch zur Verf&uuml;gung, wenn Sie sich erneut anmelden. 
	<br><br>
	<br clear="all">
</p>

<a name="#RecordTreeViewModule">
    <span class=OwHelpTopic2>
        Akten- und Ordneransicht  
    </span>
</a>

<p class="OwHelpText">
    Haben Sie eine Akte augew&auml;hlt, sehen Sie in einem Bereich - meist oderhalb der Aktenstruktur 
    - die Akteninformationen wie z.B. Kundenummer, Kundenname, Letzte &uuml;nderung usw.  <br>
    <br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/filefolderview.png" alt="" title=""/>
    <br><br>Die Struktur der Akte mit ihren Ordnern und Unterordnern wird als Baum angezeigt. Klicken 
    Sie auf das Plus-Minus-Symbol vor einem Ordner, um Unterordner anzeigen oder ausblenden zu lassen. Um den Inhalt 
    eines (Unter-)Ordners zu sehen, klicken Sie auf den Ordnernamen. Der angezeigte Ordner wird hervorgehoben.
	<br><br>
	Die Ordneransicht zeigt den Inhalt des gew&auml;hlten Ordners. Die Dokumente lassen sich per Mausklick in einem
	Viewer oder ggf. in einem Bearbeitungprogramm &ouml;ffnen. Au&szlig;erdem stehen verschiedene Bearbeitungsfunktionen f&uuml;r Dokumente zur Verf&uuml;gung (siehe unten). 
	Sie k&ouml;nnen sich den Ordnerinhalt in verschiedenen Listen anzeigen lassen:<br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewRow.png" alt="" title=""/> 
	Einfache Dokumentenliste
</span>
<p class="OwHelpText">
    In dieser Ansicht werden Ihnen die Dokumente mit Namen und anderen Dokumentattributen aufgelistet. Das zuletzt ge&ouml;ffnete Dokument wird hervorgehoben (z.B. fett).  
    Dokumente, mit denen Sie bereits gearbeitet haben, werden anders (z.B. kursiv) dargestellt als noch nicht angezeigte. So finden Sie k&uuml;rzlich verwendete 
    Dokumente schnell wieder.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewThumbnail.png" alt="" title=""/> 
	Thumbnail-Liste
</span>
<p class="OwHelpText">
    In dieser Ansicht werden Ihnen Thumbnails der Dokumente angezeigt. Sie k&ouml;nnen dabei w&auml;hlen, ob die Thumbnails klein, mittel oder gro&szlig; dargestellt 
    werden sollen. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewCombined.png" alt="" title=""/> 
	Dokumentenliste mit Thumbnails
</span>
<p class="OwHelpText">
    Diese Ansicht vereint die beiden oben beschriebenen Listentypen. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewEXTJSGrid.png" alt="" title=""/> 
	AJAX-Liste
</span>
<p class="OwHelpText">
    In AJAX-Listen werden Ihnen die Dokumente &auml;hnlich wie in der einfachen Dokumentenliste angezeigt. Allerdings bieten AJAX-Listen mehr Flexibilit&auml;t: 
    Wenn Sie zum Beispiel mit der Maus &uuml;ber einen Listeneintrag fahren, wird nach kurzer Verz&ouml;gerung ein Thumbnail des Dokuments angezeigt. <br> 
    Sie k&ouml;nnen in AJAX-Listen auch die Reihenfolge der Spalten 
    &auml;ndern, indem Sie auf einen Spaltentitel klicken, ihn mit gehaltener Maustaste zur gew&uuml;nschten Position ziehen und ihn dort fallen lassen. Auch die 
    Spaltenbreite k&ouml;nnen Sie anpassen. Dazu ziehen Sie einfach den Spaltentrenner in die gew&uuml;nschte Position. Die Breite der Spalten passt sich an. Mit einem 
    Doppelklick auf den Spaltentrenner wird sie in optimaler Breite angezeigt. <br>
    Au&szlig;erdem k&ouml;nnen Sie hier die angezeigten Metadaten der Dokumente direkt in der Liste bearbeiten. Dazu klicken Sie 
    doppelt in das gew&uuml;nschte Feld und &auml;ndern, erg&auml;nzen oder &uuml;berschreiben den bisherigen Eintrag. Sind bestimmte Formatierungen notwending (wie z.B. bei Kundennummern), werden 
    Ihnen die passenden Werte in einer Auswahlliste angezeigt. W&auml;hrend Sie einen Wert eingeben, springt der Cursor zum n&auml;chsten zutreffenden Wert. Dabei 
    werden mindestens die ersten drei von Ihnen eingegebenen Zeichen ber&uuml;cksichtigt.
    <br><br>
</p>
	
<p class="OwHelpText">	
	Alle Listen au&szlig;er der Thumbnail-Liste k&ouml;nnen auf- und absteigend sortiert werden. Klicken Sie dazu einfach auf den jeweiligen Spaltentitel.<br clear="all"><br><br>
</p>
 

<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Funktionen f&uuml;r Akten und Dokumente 
    </p>
</a>

<p class="OwHelpText">
	<br>Um Akten und Dokumente zu bearbeiten, stehen Ihnen je nach Nutzerrolle und Aktenstruktur einige der
	folgenden Funktionen zur Verf&uuml;gung. Manche lassen sich direkt ausf&uuml;hren, bei manchen unterst&uuml;tzt Sie ein Dialog. 
	Alle Dialoge k&ouml;nnen abgebrochen oder geschlossen werden, in dem Sie auf das Schlie&szlig;en-Symbol 
	klicken (meist oben links oder rechts).<br>  
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/edit_properties.png" alt="" title=""/> 
	Eigenschaften bearbeiten
</span>
<p class="OwHelpText">
    ... &ouml;ffnet einen Dialog, in dem Sie die Eigenschaften der aktuellen Akte bzw. der gew&auml;hlten Dokumente
    einsehen und bearbeiten k&ouml;nnen. Weitere Informationen dazu finden Sie auf der Hilfeseite <%=((OwInfoView)m_View).getLink("owdocprops/default.jsp","Eigenschaften bearbeiten","OwHelpLink")%>. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history.png" alt="" title=""/> 
	Historie anzeigen
</span>
<p class="OwHelpText">
    ... &ouml;ffnet die Historie der aktuellen Akte bzw. der gew&auml;hlten Dokumente. Sie 
    k&ouml;nnen die einzelnen Eintr&auml;ge genau wie Dokumentenlisten per Klick auf den Spaltentitel auf- und absteigend
    sortieren. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history_search.png" alt="" title=""/> 
	Historie suchen
</span>
<p class="OwHelpText">
    Mit dieser Funktion k&ouml;nnen Sie gezielt nach Historieneintr&auml;gen suchen. Geben Sie einfach die 
    gew&uuml;nschten Suchkriterien ein und klicken Sie auf "Suchen". Es werden Ihnen dann nur die 
    Historieneintr&auml;ge angezeigt, die Ihren Suchkriterien entsprechen.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bookmark.png" alt="" title=""/> 
	Zu den Favoriten hinzuf&uuml;gen
</span>
<p class="OwHelpText">
    ... speichert einen Link auf die aktuelle Akte bzw. auf die gew&auml;hlten Dokumente zu Ihren Favoriten. 
    Im Bereich <%=((OwInfoView)m_View).getLink("owshortcut/default.jsp","Favoriten","OwHelpLink")%> haben Sie so oft ben&ouml;tigte Akten und Dokumente schnell im Zugriff, ohne danach suchen zu m&uuml;ssen. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/copy.png" alt="" title=""/> 
	Kopieren
</span>
<p class="OwHelpText">
    ... kopiert die aktuelle Akte bzw. die gew&auml;hlten Dokumente in die Zwischenablage. Von dort aus k&ouml;nnen Sie sie 
    zum Beispiel einfach als Anlage zu Vorg&auml;ngen hinzuf&uuml;gen. Wenn Sie ein Dokument einf&uuml;gen, verbleibt das Originaldokument im
    urspr&uuml;nglichen Ordner. Beim Verschieben wird es vom urspr&uuml;nglichen in den neuen Ordner verschoben.<br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/deletebtn.png" alt="" title=""/> 
	L&ouml;schen
</span>
<p class="OwHelpText">
    Falls Ihre Nutzerrolle es zul&auml;sst, k&ouml;nnen Sie mit Hilfe dieser Funktion Dokumente, Ordner und Akten unwiederbringlich l&ouml;schen. <br>
</p>



<br><br>

<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Aktenfunktionen 
    </p>
</a>

<p class="OwHelpText">
	<br>F&uuml;r die Bearbeitung von Akten, stehen Ihnen au&szlig;erdem einige der
	folgenden Funktionen zur Verf&uuml;gung:
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Neue Akte anlegen
</span>
<p class="OwHelpText">
    ... &ouml;ffnet einen Dialog zum Anlegen neuer Akten. Geben Sie den Namen der neuen Akte an und legen Sie ggf. ihre Eigenschaften fest. 
    Danach klicken Sie auf Speichern und eine neue Akte wird angelegt. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Unterordner anlegen
</span>
<p class="OwHelpText">
    ... &ouml;ffnet einen Dialog zum Anlegen eines neuen Unterordners in dem Ordner oder der Akte, in der Sie sich gerade befinden. 
    W&auml;hlen Sie die gew&ouml;nschte Ordnerklasse aus und klicken Sie auf "Weiter". Danach geben Sie die Eigenschaften des 
    Unterordners an und klicken auf Speichern. Der neue Unterordner wird angelegt. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_document.png" alt="" title=""/> 
	Dokument hinzuf&uuml;gen
</span>
<p class="OwHelpText">
    Mit dieser Funktion k&ouml;nnen Sie zum aktuellen Ordner Dokumente hinzuf&uuml;gen. Dazu gibt es verschiedene M&ouml;glichkeiten (Drag & Drop, Copy & Paste, 
    Upload). Eine ausf&uuml;hrliche Beschreibung finden Sie <%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","hier","OwHelpLink")%>. 
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/paste.png" alt="" title=""/> 
	Dokument aus der Zwischenablage einf&uuml;gen
</span>
<p class="OwHelpText">
    Wenn Sie Dokumente aus einer Akte, einer Trefferliste oder einem Vorgang in die Zwischenablage kopiert haben, 
    k&ouml;nnen Sie sie mit dieser Funktion in den aktuellen Order einf&uuml;gen (als Kopie). <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/move.png" alt="" title=""/> 
	Dokument aus der Zwischenablage verschieben
</span>
<p class="OwHelpText">
    Wenn Sie Dokumente aus einer Akte, einer Trefferliste oder einem Vorgang in die Zwischenablage kopiert haben, 
    k&ouml;nnen Sie sie mit dieser Funktion in den aktuellen Order verschieben. Sie erscheinen - wie beim Ausschneiden und Einf&uuml;gen - 
    nicht mehr am vorherigen Ort. <br>
</p>



<br><br>
<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Funktionen f&uuml;r Dokumente 
    </p>
</a>

<p class="OwHelpText">
	<br>Um Dokumente zu bearbeiten, stehen Ihnen au&szlig;erdem einige der
	folgenden Funktionen zur Verf&uuml;gung:
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cut.png" alt="" title=""/> 
	Ausschneiden
</span>
<p class="OwHelpText">
    ... schneidet die gew&auml;hlten Dokumente aus dem derzeitigen Ordner aus und f&uuml;gt sie in die Zwischenablage ein. 
    Von dort aus k&ouml;nnen Sie sie bequem in andere Ordner oder Akten verschieben. Falls Sie sie nirgends
    einf&uuml;gen, bleiben sie im urspr&uuml;nglichen Ordner erhalten. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkout.png" alt="" title=""/> 
	Auschecken
</span>
<p class="OwHelpText">
     ... sperrt Dokumente zum Bearbeiten. Dadurch k&ouml;nnen nur Sie auf diese Dokumente zugreifen und 
     sie z.B. herunterladen und bearbeiten.<br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cancelcheckout.png" alt="" title=""/> 
	Auschecken abbrechen
</span>
<p class="OwHelpText">
    ... entsperrt ausgecheckte Dokumente, ohne dass &Auml;nderungen &uuml;bernommen werden. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/download.png" alt="" title=""/> 
	Download
</span>
<p class="OwHelpText">
    ... speichert Dokumente zum Bearbeiten als lokale Kopie. Um zu vermeiden, dass andere Nutzer 
    die gleichen Dokumente zur gleichen Zeit bearbeiten, sollten Sie sie zun&auml;chst auschecken. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/save.png" alt="" title=""/> 
	
	Speichern
</span>
<p class="OwHelpText">
    ... speichert ein Dokument als neue Nebenversion ins System. Es &ouml;ffnet sich ein 
    Dialog, mit dessen Hilfe Sie das gew&uuml;nschte Dokument ausw&auml;hlen und hochladen k&ouml;nnen. 
    Das Dokument bleibt jedoch ausgecheckt, bis Sie es einchecken. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkin.png" alt="" title=""/> 
	Einchecken
</span>
<p class="OwHelpText">
    ... speichert ein Dokument als neue Haupt- oder Nebenversion ins System und hebt die Bearbeitungssperre auf. Es &ouml;ffnet sich ein 
    Dialog, mit dessen Hilfe Sie die Eigenschaften des Dokuments bearbeiten, das Dokument aus Ihrem Dateisystem ausw&auml;hlen und
    hochladen k&ouml;nnen. Dabei verfahren Sie &auml;hnlich wie beim <%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","Hinzuf&uuml;gen eines Dokuments","OwHelpLink")%>.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/demoteversion.png" alt="" title=""/> 
	Version herabstufen
</span>
<p class="OwHelpText">
    ... stuft die letzte Hauptversion zur letztm&ouml;glichen Nebenversion herab, z.B. von 2.0 auf 1.1. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/promoteversion.png" alt="" title=""/> 
	Version h&ouml;her stufen
</span>
<p class="OwHelpText">
    ... stuft die zuletzt gespeicherte Nebenversion zur n&auml;chstm&ouml;glichen Hauptversion herauf, z.B. von 3.1 auf 4.0.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/remove.png" alt="" title=""/> 
	Entfernen
</span>
<p class="OwHelpText">
    ... entfernt das Dokument aus der Akte, ohne es zu l&ouml;schen. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/docview.png" alt="" title=""/> 
	Dokumente vergleichen
</span>
<p class="OwHelpText">
    ... &ouml;ffnet die gew&auml;hlten Dokumente nebeneinander im Viewer. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_note.png" alt="" title=""/> 
	Notiz eingeben
</span>
<p class="OwHelpText">
     ... &ouml;ffnet einen Dialog, in dem Sie Notizen - z.B. Bearbeitungshinweise an Ihre Kollegen - 
     eingeben k&ouml;nnen. Bereits bestehende Notizen werden dabei nicht &uuml;berschrieben, sondern bleiben in 
     der Historie erhalten. Wenn Sie auf "Speichern" klicken, wird die Notiz gespeichert. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/sendlink.png" alt="" title=""/> 
	
	Email-Link
</span>
<p class="OwHelpText">
    ... &ouml;ffnet eine neue E-Mail in Ihrem Standard E-Mail-Programm, die Links auf die gew&auml;hlten Dokumente 
    enth&auml;lt.  <br>
</p>


<p class="OwHelpText">
    <br><br> Sollten Ihnen weitere Funtkionen zur Verf&uuml;gung stehen, die hier nicht beschrieben sind, wenden Sie sich bei Fragen dazu bitte 
    an Ihren Administrator. <br>
</p>

<br>