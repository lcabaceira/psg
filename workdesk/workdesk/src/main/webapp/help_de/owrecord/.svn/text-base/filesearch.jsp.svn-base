<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Bereich Aktensuche
</span>

<p class="OwHelpText">
	<br>    
    Im Bereich Aktensuche sehen Sie Dokumente und Daten, die mindestens eine gemeinsame Eigenschaft besitzen (z.B. Kundennummer, 
    Dokumentklasse, Priorit&äuml;t, Datum der letzten &Auml;nderung, ...). 
    Diese Eigenschaften werden als Suchkriterien zusammengefasst und als Ordner in einer Baumstruktur dargestellt. Der einzige Unterschied zu einer normalen Aktenstruktur: 
    Die Ordner existieren nicht tats&auml;chlich im ECM-System, sondern nur virtuell.  
    <br><br> 
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        Virtuelle Akten- und Ordneransicht<br>   
    </span>
</a>

<p class="OwHelpText">
    Sie sehen in der Aktensuche Akten und Ordner genauso wie in der Aktenverwaltung oder im Bereich Durchsuchen. Nur sind diese Ordner nicht tats&auml;chlich 
    im ECM-System vorhanden, sondern nur eine Kombination aus verschiedenen Suchkriterien. Ein Beispiel:
    Den obersten Ordner bilden alle Dokumente, die eine bestimmte Kundennummer besitzen. Ein Unterordner darunter w&auml;ren dann Dokumente, die 
    diese Kundennummer besitzen und eine bestimmte Dokumentklasse haben. Eine weitere Ebene darunter werden Dokumente angezeigt, die diese bestimmte 
    Kundennummer besitzen, zu der bestimmten Dokumentklasse geh&ouml;ren und in einem bestimmten Jahr ins System eingestellt worden sind. <br> 
    Sie klicken hier - genau wie in der Aktenverwaltung oder im Bereich Durchsuchen - einfach auf das 
    Plus-Minus-Symbol vor der Akte oder dem Ordner, um Unterordner anzuzeigen oder auszublenden. Um den Inhalt 
    eines (Unter-)Ordners zu sehen, klicken Sie auf den Ordnernamen. Der angezeigte Ordner wird hervorgehoben.
	<br><br>
	Die Ordneransicht zeigt den Inhalt des gew&auml;hlten Ordners. Die Dokumente lassen sich per Mausklick in einem
	Viewer oder ggf. in einem Bearbeitungprogramm &ouml;ffnen. Au&szlig;erdem stehen - wie im Bereich Aktenverwaltung - verschiedene 
    Bearbeitungsfunktionen f&uuml;r Akten und Dokumente und verschiedene Listentypen zur Verf&uuml;gung, 
    die auf der Hilfeseite zur <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","Aktenverwaltung","OwHelpLink")%> ausf&uuml;hrlich 
    beschrieben werden. Aufgrund der virtuellen Aktenstruktur gibt es jedoch ein paar Unterschiede bei folgenden Funktionen:<br clear="all">
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Neue Akte anlegen & <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Unterordner anlegen
</span>
<p class="OwHelpText">
    Da keine tats&auml;chlichen Akten und Ordner existieren, k&ouml;nnen Sie in der Aktensuche keine neuen Akten oder Unterordner anlegen. 
    Sollten Sie regelm&auml;&szlig;ig eine bestimmte Kombination aus Suchkriterien nutzen, die Ihnen fest als virtuelle Akte oder virtueller Ordner angezeigt werden sollen, 
    wenden Sie sich bitte an Ihren Administrator. <br>
</p>


<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/paste.png" alt="" title=""/> 
	Dokument aus der Zwischenablage einf&uuml;gen & <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/move.png" alt="" title=""/> 
	Dokument aus der Zwischenablage verschieben
</span>
<p class="OwHelpText">
    In der Aktensuche k&ouml;nnen Sie zwar Dokumente in die Zwischenablage kopieren, sie aber nicht in eine virtuelle Akte einf&uuml;gen oder verschieben. 
    In virtuellen Akten werden Ihnen Dokumente angezeigt, die bestimmte Eigenschaften besitzen. Um ein Dokument in eine andere virtuelle Akte zu verschieben, 
    m&uuml;ssen Sie die Eigenschaften der jeweiligen Akte besitzten. Wenn Sie also die Eigenschaften eines Dokuments ver&auml;ndern (<%=((OwInfoView)m_View).getLink("owdocprops/default.jsp","Eigenschaften bearbeiten","OwHelpLink")%>)
    erscheint es automatisch in der passenden virtuellen Akte. <br>
</p>


<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        <br><br>Verfeinerung der Anzeige<br>   
    </span>
</a>

<p class="OwHelpText">
    Das von Ihnen gesuchte Objekt ist nicht unter den Treffern? Dann verfeinern Sie die Ansicht. Geben Sie 
    weiter Suchkriterien an und klicken Sie auf "Suchen". <br>
    Um die Suche zu beschleunigen, k&ouml;nnen Sie au&szlig;erdem die "Maximale Trefferanzahl" angeben. 
    <br><br clear="all"><br><br>
</p>
