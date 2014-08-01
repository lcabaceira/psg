<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Hilfe zum Dialog "Arbeitsauftrag bearbeiten"
</span>

<p class="OwHelpIntro">
	<br>Wenn Sie Vorg&auml;nge im Bereich "BPM" bzw. "Case Management" ausgew&auml;hlt und auf "Schritt bearbeiten" 
	geklickt haben, &ouml;ffnen sich die gew&auml;hlten Vorg&auml;nge in einem Bearbeitungsformular. 
	In den meisten Bearbeitungsformularen werden Ihnen Vorgangseigenschaften 
	und deren Wert angezeigt. Werte, die Sie &auml;ndern k&ouml;nnen oder sollen, erscheinen 
	in Auswahlboxen oder als Eingabefeld. 
	Falls sie mehrere Vorg&auml;nge auf einmal bearbeiten, k&ouml;nnen Sie mit 
	Hilfe der Pfeilsymbole zwischen den einzelnen Vorg&auml;ngen wechseln.
	<br><br>
</p>
<br>

<p class="OwHelpText">  
    <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/processstep.png" alt="" title=""/>
</p>

<span class="OwHelpTopic2">
   Anlagen
</span>

<p class="OwHelpText">
    <br>Zum Vorgang geh&ouml;rende Anlagen (Ordner oder Dokumente) werden Ihnen 
    direkt im Bearbeitungsformular aufgelistet. 
</p>

<span class="OwHelpTopic3">
	<br>Dokumente als Anlage
</span>

<p class="OwHelpText">
    <br>Durch Klick auf den Namen des Dokuments &ouml;ffnet es sich im Viewer. <br>
    &auml;hnlich wie in der Aktenverwaltung k&ouml;nnen Sie auch direkt im Bearbeitungsformular 
    die Anlagen bearbeiten. Welche Funktionen Ihnen hier zum Bearbeiten von Anlagen 
    zur Verf&uuml;gung stehen, h&auml;ngt von Ihrer Nutzerrolle ab. Hier einige Beispiele:<br><br>
    <ul>
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/edit_properties.png" alt="" title=""/>  Eigenschaften bearbeiten</li>
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/copy.png" alt="" title=""/>  Kopieren</li>
		<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/download.png" alt="" title=""/>  Lokale Kopie speichern</li>
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/deletebtn.png" alt="" title=""/>  Element l&ouml;schen (entfernt das Element aus der Anlagenliste)</li>				
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_paste.png" alt="" title=""/>  Objekt aus der Zwischenablage einf&uuml;gen</li>
	</ul>
	<br>Sie k&ouml;nnen flexibel zwischen Aktenverwaltung und dem Bearbeitungsformular wechseln, um z.B. ein Dokument 
	aus der Aktenverwaltung zu kopieren und dieses dem Vorgang als Anlage hinzuzuf&uuml;gen. Wenn Sie wieder zum Bereich 
	AWD BPM zur&uuml;ckkehren, ist ihr Bearbeitungsformular weiterhin ge&ouml;ffnet. <br>      
</p>

<span class="OwHelpTopic3">
	<br>Ordner als Anlage
</span>

<p class="OwHelpText">
    <br>Durch Klick auf den Namen des Ordners &ouml;ffnet sich die entsprechende Akte in der Aktenverwaltung. Sie k&ouml;nnen dort wie gewohnt 
    Dokumente ansehen und bearbeiten und flexibel zwischen Aktenverwaltung und dem Bearbeitungsformular wechseln. Wenn Sie wieder zum Bereich 
	AWD BPM zur&uuml;ckkehren, ist Ihr Bearbeitungsformular weiterhin ge&ouml;ffnet. <br><br>      
</p>

<span class="OwHelpTopic2">
    Bearbeitungsschritt abschlie&szlig;en
</span>

<p class="OwHelpText">	
	<br>Am Ende des Formular haben Sie die Auswahl zwischen mehreren Optionen:  
</p>

<span class="OwHelpTopic3">
	<br>Speichern
</span>

<p class="OwHelpText">	
	Wenn Sie auf "Speichern" klicken, werden Ihre &auml;nderungen &uuml;bernommen. 
	Der Vorgang wird jedoch noch nicht abgeschlossen und bleibt weiterhin zur 
	Bearbeitung ge&ouml;ffnet.
</p>

<span class="OwHelpTopic3">
	<br>Zur&uuml;ckgeben
</span>

<p class="OwHelpText">	
	"Zur&uuml;ckgeben" w&auml;hlen Sie, wenn der vorherige Bearbeiter des Vorgangs 
	seine Angaben noch einmal &uuml;berpr&uuml;fen soll. Der Vorgang erscheint (ggf. mit 
	den von Ihnen gespeicherten &auml;nderungen) dann wieder in der vorherigen Inbox oder Sicht. 
	Das Bearbeitungsformular schlie&szlig;t sich und Sie sehen die zuletzt gew&auml;hlte Sicht.  
</p>

<span class="OwHelpTopic3">
	<br>Abschlie&szlig;en
</span>

<p class="OwHelpText">	
	Um den Bearbeitungsschritt abzuschlie&szlig;en, w&auml;hlen Sie eine der gegebenen Antworten und klicken 
	auf "Abschlie&szlig;en". Der Vorgang wird dann automatisch an den n&auml;chsten Bearbeiter weitergeleitet. 
	Das Bearbeitungsformular schlie&szlig;t sich und Sie sehen die zuletzt gew&auml;hlte Sicht.  
</p>

<span class="OwHelpTopic3">
	<br>Abbrechen
</span>

<p class="OwHelpText">	
	Sie k&ouml;nnen die Bearbeitung jederzeit abbrechen, indem Sie auf das Schlie&uuml;en-Symbol (meistens oben links) klicken. 
	Gespeicherte &auml;nderungen bleiben erhalten. Das Bearbeitungsformular schlie&szlig;t sich und Sie sehen die zuletzt gew&auml;hlte Sicht.  
</p>