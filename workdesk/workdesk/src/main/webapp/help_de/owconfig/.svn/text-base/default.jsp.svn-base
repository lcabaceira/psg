<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>


<span class="OwHelpTopic1">
	Hilfe zum Bereich Verwaltung
</span>

<p class="OwHelpIntro">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/administration.png" alt="" title=""/><br><br>Der Bereich <em>Verwaltung</em> beinhaltet ein User Interface f&uuml;r Wordesk's DB Rollenmanager sowie Links zu Modellierungswerkzeugen des ECM Herstellers, falls verf&uuml;gbar. 
</p>
<br>


<span class="OwHelpTopic2"> 
    DB Rollenmanager 
</span>

<p class="OwHelpText">	
	<br> 
	Um Ressourcen einer Rolle zuzuweisen, w&auml;hlen Sie zun&auml;chst die gew&uuml;nschte Rolle aus, indem sie auf die Schaltfl&auml;che mit den drei Punkten dr&uuml;cken. Eine so ausgew&auml;hlte Rolle steht dann &uuml;ber Auswahlbox zur Verf&uuml;gung. <br><br>
</p>

<span class="OwHelpTopic3"> 
    Objektklasse 
</span>

<p class="OwHelpText">
	Hier k&ouml;nnen Sie festlegen, auf welche Objektklassen die ausgew&auml;hlte Rolle Zugriff hat. Um die Einstellungen zu verfeinern, nutzen Sie bitte die 
	Kategorie "Indexfelder".<br><br>
</p>

<span class="OwHelpTopic3"> 
    Zus&auml;tzliche Funktion 
</span>

<p class="OwHelpText">
	Administratoren haben im Bereich <%=((OwInfoView)m_View).getLink("owsettings/default.jsp","Einstellungen","OwHelpLink")%> 
	neben den Benutzereinstellungen auch die M&ouml;glichkeit, Seiteneinstellungen zu ver&auml;ndern. Dort 
	k&ouml;nnen sie zum Beispiel festlegen, welche Shortcuts f&uuml;r welche Funktion genutzt werden k&ouml;nnen oder welches Datumsformat verwendet werden soll. 
	Hier bestimmen Sie, welche Seiteneinstellungen Administratoren dort zur Verf&uuml;gung stehen. <br><br>
</p>

<span class="OwHelpTopic3">
    Plugin
</span>

<p class="OwHelpText">
	Hier bestimmen Sie, welche Bereiche der ausgew&auml;hlten Rolle angezeigt und welche ihr verwehrt werden.<br><br>
</p>

<span class="OwHelpTopic3"> 
    Virtueller Postkorb 
</span>

<p class="OwHelpText"> 
	Hier legen sie fest, welche virtuellen Postk&ouml;rbe Mitglieder der jeweiligen Rolle nutzen k&ouml;nnen.<br><br>
</p>

<span class="OwHelpTopic3">
    Design
</span>

<p class="OwHelpText"> 
	Unterschiedliche Abteilungen haben h&auml;ufig unterschiedliche Designs. Hier k&ouml;nnen Sie ausw&auml;hlen, welches Design einer Rolle zugeordnet sein soll.<br><br>
</p>

<span class="OwHelpTopic3"> 
    Indexfeld 
</span>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/indexfields.png" alt="" title=""/><br><br>Diese Kategorie zeigt Ihnen zun&uuml;chst alle Objektklassen. Wenn Sie auf das Plussymbol vor der Objektklasse klicken, erscheinen alle Indexfelder dieser
	Klasse. Indem Sie die H&auml;kchen setzen oder entfernen, k&ouml;nnen Sie hier einstellen, ob und wann die ausgew&auml;hlte Rolle berechtigt ist, die Indexfelder 
	zu sehen und ggf. zu &auml;ndern: Beim Erzeugen, beim Checkin und/oder beim Betrachten. <br><br>
</p>

<span class="OwHelpTopic3"> 
	Suchvorlage 
</span>

<p class="OwHelpText">
	Mitglieder einzelner Rollen ben&ouml;tigen unterschiedliche Suchtemplates. Hier legen Sie fest, welche sie nutzen k&ouml;nnen.<br><br>
</p>
	
<p class="OwHelpText">	
	Sie k&ouml;nnen den Zugriff auf alle Items der jeweiligen Kategorie nun explizit zulassen oder verwehren. 
	Klicken Sie auf "&uuml;bernehmen", werden die &Auml;nderungen gespeichert. 
	Diese greifen jedoch erst nach der n&auml;chsten Anmeldung.<br>
	Die H&auml;kchen neben den Auswahlboxen zeigen Ihnen, welche Einstellungen derzeit gespeichert sind. 
	Hat ein Nutzer mehrere Rollen gleichzeitig, gelten verwehrte Items mehr als zugelassene.<br><br>
</p>


<span class="OwHelpTopic2">
    Werkzeuge
</span>

<p class="OwHelpText">	
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/tools.png" alt="" title=""/><br><br>
	<br>Unter <em>Werkzeuge</em> k&ouml;nnen Ihnen Links zu externen Tools zur Verf&uuml;gung stehen, beispielsweise zum Search Designer oder Process Designer f&uuml;r IBM  
	FileNet P8. Klicken Sie einfach auf den Link, um das gew&uuml;nschte Tool zu starten. <br><br>
</p>