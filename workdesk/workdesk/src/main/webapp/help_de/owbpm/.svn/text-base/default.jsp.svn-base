<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Hilfe zum Bereich BPM
</span>

<p class="OwHelpIntro">
	<br>Im Bereich "P8 BPM" (f&uuml;r Case management mit IBM FileNet P8 BPM) bzw. "Alfresco BPM" (f&uuml;r Case Management mit Alfresco Workflow) k&uuml;nnen Sie Vorg&auml;nge komfortabel bearbeiten. Ihnen werden - je nach Ihrer Rolle - 
	unterschiedliche Inboxen und Sichten (nur mit IBM FileNet P8 BPM) sowie Funktionen zur Vorgangsbearbeitung angezeigt.
</p>
<br><br>


<span class="OwHelpTopic2">
    Inboxen und Sichten
</span>

<p class="OwHelpText">	
	<br>Im Navigationsmen&uuml; (meistens links) werden Ihnen verschiedene Inboxen und Sichten 
	angezeigt, die Sie per Mausklick ausw&auml;hlen k&ouml;nnen. In den meisten F&auml;llen stehen Ihnen 
	die pers&ouml;nliche Inbox, verschiedene Gruppeninboxen und &uuml;berblicksansichten zur Verf&uuml;gung.<br><br>
</p>


<p class="OwHelpText">
	<ul>
		<li><b>Inbox</b><br>
		Die Inbox ist Ihr pers&ouml;nlicher Postkorb. Hier finden Sie alle Vorg&auml;nge, die von Ihnen bearbeitet werden sollen.</li><br>
		<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bpm_navigation.png" alt="" title=""/>
		<li><b>Gruppeninboxen [Nur verf&uuml;gbar in IBM FileNet P8 BPM]</b><br>
		Jeder Benutzer hat je nach Berechtigung Zugriff auf bestimmte Gruppeninboxen. Hier finden Sie Vorg&auml;nge,
		die von Ihnen sowie mehreren anderen Personen bearbeitet werden k&ouml;nnen.</li><br>
		<li><b>&Uuml;berblick [Nur verf&uuml;gbar in IBM FileNet P8 BPM]</b><br>
		Die &Uuml;berblicksansichten zeigen Ihnen einen Querschnitt &uuml;ber verschiedene Inboxen. Hier k&ouml;nnen 
		z.B. alle Vorg&auml;nge angezeigt werden, die h&ouml;chste Priorit&auml;t haben, oder nur Vorg&auml;nge zu einem 
		bestimmten Kundenkreis.</li><br>
	</ul>
</p>

<p class="OwHelpText">	
	<br>Optional k&ouml;nnen folgende Sichten angezeigt werden:<br><br>
</p>
		
<p class="OwHelpText">
	<ul>		
		<li><b>Vertreter/Proxy [Nur verf&uuml;gbar in IBM FileNet P8 BPM]</b><br>
		Hier werden Ihnen ggf. alle Vorg&auml;nge angezeigt, f&uuml;r die Sie als Vertreter eingesetzt sind.</li><br>
		<li><b>Tracker [Nur verf&uuml;gbar in IBM FileNet P8 BPM]</b><br>
		Wenn Sie von Administrator als Tracker bestimmt sind, finden Sie in dieser optionalen Ansicht die Vorg&auml;nge,
		die Sie &uuml;berwachen und analysieren sollen.
		</li><br>
	</ul>
</p>

<p class="OwHelpText">	
	<br>&uuml;ber der Vorgangsliste finden Sie in allen Sichten die Wiedervorlageansicht und eine Filterfunktion.<br><br>
</p>
		
<p class="OwHelpText">
	<ul>			
		
		<li><b>Wiedervorlage</b><br>
		Vorg&auml;nge, deren Bearbeitung aufgeschoben werden muss, k&ouml;nnen auf Wiedervorlage gesetzt 
		werden. Sie werden dann bis zum Erreichen des Wiedervorlagedatums aus der jeweiligen 
		Ansicht ausgeblendet. Um diese Aufgaben vor dem Wiedervorlagedatum einsehen und 
		bearbeiten zu k&ouml;nnen, markieren Sie das Auswahlk&auml;stchen zur Wiedervorlage. Zum 
		Ausschalten der Wiedervorlageansicht heben Sie die Markierung einfach wieder auf.
		</li><br>
	    <li><b>Filter einstellen</b><br>
	    Sie k&ouml;nnen die verschiedenen Sichten zus&auml;tzlich einschr&auml;nken, indem Sie Filter 
	    erstellen. Klicken Sie auf das Trichter-Symbol neben dem jeweiligen Spaltentitel und 
	    geben Sie danach einfach die gew&uuml;nschten Filterkriterien an. Wenn Sie das 
	    Auswahlk&auml;sten markieren, wird die Ansicht gefiltert. Wird die Markierung aufgehoben, 
	    erscheint die Ansicht wieder ungefiltert.
	    </li>
	</ul>
</p>

<p class="OwHelpText">
	<br><br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/wv_filter.png"  style="vertical-align: middle;" alt="" title=""/><br>
</p>

<p class="OwHelpText">	
	<br><br>Vorg&auml;nge werden in allen Inboxen und Sichten in Listen angezeigt. Je nach Priorit&auml;t 
	k&ouml;nnen die Vorg&auml;nge unterschiedlich markiert sein - entweder durch farbige Hinterlegung 
	oder mit einem Priorit&uuml;tsflag. Sie erkennen so auf einen Blick, welche Vorg&auml;nge am 
	dringendsten bearbeitet werden sollen.<br>
	<br>
	Die Listen werden auf- bzw. absteigend sortiert, wenn Sie einen beliebigen Spaltentitel 
	anklicken. In Ajax-Listen k&ouml;nnen au&szlig;erdem  Spaltenbreiten und -reihenfolge wie aus dem 
	Windows Explorer gewohnt ver&auml;ndert werden.
</p>

<br><br><br>


<span class="OwHelpTopic2">
   Vorgangsbearbeitung<br><br>
   <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/wv_filter_context.png" alt="" title=""/>
</span>

<p class="OwHelpText">
    <br>Zum Bearbeiten markieren Sie einen oder mehrere Vorg&auml;nge. Die gew&auml;hlten Vorg&auml;nge sind 
    dann f&uuml;r andere Benutzer gesperrt, bis Sie sie wieder entsperren, sich ausloggen oder 
    die Sicht wechseln  (nur in IBM FileNet P8 BPM). Gesperrte Vorg&auml;nge werden mit dem Symbol 
    <img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/locked.png"
         alt="<%=m_View.getContext().localize("help.image.locked16.gif","Image: Locked item")%>" 
    	 title="<%=m_View.getContext().localize("help.image.locked16.gif","Image: Locked item")%>" 
    /> gekennzeichnet.
    <br><br>
    Zum Bearbeiten von Vorg&auml;ngen gibt es die folgenden Grundfunktionen:
    <ul>
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/jspprocessor.png" alt="" title=""/> / <img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/standardprocessor.png" alt="" title=""/>  Schritt bearbeiten</li>
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/returntosource.png" alt="" title=""/>  Zur&uuml;ckgeben</li>
		<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/reassign.png" alt="" title=""/>  Weiterleiten</li>
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/note.png" alt="" title=""/>  Notiz einf&uuml;gen</li>				
    	<li><img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/resubmit.png" alt="" title=""/>  Wiedervorlage</li>
	</ul>
	Au&szlig;erdem k&ouml;nnen Sie sich &uuml;ber die Symbole 
	<img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history.png" alt="" title=""/> (Historie anzeigen) 
	und <img style="vertical-align:middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history_search.png" alt="" title=""/> (Historie suchen) 
	die Historie des Vorgangs anzeigen lassen bzw. bestimmt Eintr&auml;ge darin suchen (nur verf&uuml;gbar in IBM FileNet P8 BPM). Falls Sie Fragen zu 
	weiteren Funktionen haben, die Ihnen zur Verf&uuml;gung stehen, wenden Sie sich bitte an Ihren Administrator. 
	<br><br>
	Um eine Funktion auf einen oder mehrere Vorg&auml;nge anzuwenden, k&ouml;nnen Sie diese entweder per Klick auf 
	das jeweilige Icon neben dem Vorgangstitel bzw. der Vorgangsbeschreibung oder aus einem Kontextmen&uuml; ausw&auml;hlen.<br><br>        
</p>

<span class="OwHelpTopic3">
	Schritt bearbeiten
</span>

<p class="OwHelpText">
    W&auml;hlen Sie "Schritt bearbeiten" aus, &ouml;ffnen sich die markierten Vorg&auml;nge in einem Bearbeitungsformular. Dort k&ouml;nnen Sie Eingaben machen 
    und &uuml;berpr&uuml;fen sowie den Bearbeitungsschritt abschlie&szlig;en. Schritt f&uuml;r Schritt unterst&uuml;tzen Sie 
    Hinweise bei Bearbeitung. Weiter Informationen finden Sie auf der Hilfeseite <%=((OwInfoView)m_View).getLink("owbpm/processstep.jsp","Schritt bearbeiten","OwHelpLink")%>.<br>
    In der Men&uuml;leiste finden Sie die gleichen Symbole zum Bearbeiten des n&auml;chsten Vorgangs. Ihnen wird 
    damit automatisch der als n&auml;chstes zu bearbeitende Vorgang zugewiesen.
	<br><br>
</p>

<span class="OwHelpTopic3">
	Zur&uuml;ckgeben  [Nur verf&uuml;gbar in IBM FileNet P8 BPM]
</span>

<p class="OwHelpText">
    Wenn Sie Vorg&auml;nge aus einer Gruppeninbox in Ihre pers&ouml;nliche Inbox weitergeleitet haben, 
    k&ouml;nnen Sie diesen Schritt mit "Zur&uuml;ckgeben" r&uuml;ckg&auml;ngig machen.<br><br>
</p> 


<span class="OwHelpTopic3">
	Weiterleiten
</span>

<p class="OwHelpText">
    Mit "Weiterleiten" k&ouml;nnen Sie Vorg&auml;nge in andere Gruppeninboxen (nur in IBM FileNet P8 BPM) oder in Ihren pers&ouml;nlichen Postkorb 
    verschieben. Im Weiterleiten-Dialog w&auml;hlen Sie einfach die gew&uuml;nschte Zielbox aus und klicken "OK". 
    Die gew&auml;hlten Vorg&auml;nge werden weitergeleitet.<br><br>
</p>


<span class="OwHelpTopic3">
	Wiedervorlage setzen
</span>

<p class="OwHelpText">
    Sie k&ouml;nnen Vorg&auml;nge zu einem bestimmten Datum auf Wiedervorlage setzen. Diese werden bis dahin 
    aus der Sicht ausgeblendet. Um die sp&auml;tere Bearbeitung zu erleichtern, kann dabei eine Notiz 
    erstellt werden.<br>
	Auf Wiedervorlage gesetzte Vorg&auml;nge k&ouml;nnen in der Ansicht "Wiedervorlage" eingesehen und 
	bearbeitet werden. Wird das Wiedervorlagedatum auf das aktuelle Datum gesetzt, so erscheinen 
	die Vorg&auml;nge sofort wieder in der jeweiligen Ansicht. <br><br>
</p>


<span class="OwHelpTopic3">
	Notiz einf&uuml;gen
</span>

<p class="OwHelpText">
    Zu jedem Vorgang k&ouml;nnen Sie eine oder mehrere Notizen hinzuf&uuml;gen, um z.B. Ihren Kollegen 
    wichtige Bearbeitungshinweise zu geben. Alte Notizen werden in der Historie weiterhin angezeigt. 
    Notizen lassen sich auch in den einzelnen Ansichten als Spalte einblenden.<br><br>
</p>