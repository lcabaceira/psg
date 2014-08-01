<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Dialog Dokument hinzuf&uuml;gen 
</span>

<p class=OwHelpIntro>
    <br>Die Funktion "Dokument hinzuf&uuml;gen" erm&ouml;glicht es Ihnen, 
    neue Dokumente von Ihrem Dateisystem ins ECM-System einzustellen. Die hinzugef&uuml;gten Dokumente stehen Ihnen dann in der
    gew&auml;hlten Akte (und im gew&auml;hlten Ordner) zur Verf&uuml;gung.<br><br>
</p>

<span class=OwHelpTopic2>
    Schritte
</span>


<p class="OwHelpText">
    <br>Es gibt verschiedene M&ouml;glichkeiten, die gew&uuml;nschten Dokumente hochzuladen:<br><br>
    <ol><li>1. Per Drag & Drop auf das "Dokument Hinzuf&uuml;gen" Symbol: Sie markieren die gew&uuml;nschten Dokumente im Windows Explorer oder auf dem Windows Desktop, ziehen sie 
    mit gehaltener Maustaste &uuml;ber das "Dokument Hinzuf&uuml;gen" Symbol.</li></ol> 
    <ol><li>2. Per Drag & Drop auf die Trefferliste: Sie markieren die gew&uuml;nschten Dokumente im Windows Explorer oder auf dem Windows Desktop, ziehen sie 
    mit gehaltener Maustaste &uuml;ber auf die Trefferliste. Diese Funktion steht Ihnen nur zur Verfügung, wenn sie den HTML5 Dokumenten-Upload in den Benutzereinstellungen aktiviert haben und sie einen der unterst&uuml;tzten HTML5 Browser verwenden.</li></ol> 
    <ol><li>3. Per Copy & Paste: Sie markieren die gew&uuml;nschten Dokumente im Windows Explorer oder auf dem Windows Desktop, kopieren sie, 
    klicken mit der rechten Maustaste auf das "Dokument Hinzuf&uuml;gen" Symbol und w&auml;hlen "Einf&uuml;gen/Paste". Diese Funktion steht Ihnen nur zur Verfügung, wenn sie den HTML5 Dokumenten-Upload in den Benutzereinstellungen deaktiviert haben.</li></ol>
    <ol><li>4. Sie klicken direkt im Wordesk auf das "Dokument Hinzuf&uuml;gen" Symbol und laden die gew&uuml;nschten Dateien nacheinander hoch: 
    	<ul type="square" class="OwHelpText">        
    	<li>* Lokale Datei: W&auml;hlen Sie &uuml;ber "Durchsuchen" die Dateien auf Ihrer lokalen Festplatte aus. </li>
    	<li>* Ohne Inhalt: Setzen Sie hier eine Markierung, wenn es sich nur um eine Sammlung von Eigenschaften handelt, die nicht mit einem Dokument 
    	verkn&uuml;pft sind (z.B. Kundendaten).</li>
    	<li>* Klicken Sie dann auf "Hochladen". Die hochgeladenen Objekte erscheinen in einer Liste. Haben Sie alle Dateien hochgeladen, klicken Sie auf "Weiter". </li>
		</ul>
    </li></ol>
    
    Danach folgen Sie den Erkl&auml;rungen im Dialog "Neue Dokumente hinzuf&uuml;gen" Schritt f&uuml;r Schritt:<br><br>
</p>

<span class=OwHelpTopic3>
	1. Klasse ausw&auml;hlen (optional)
</span>

<p class="OwHelpText">
    W&auml;hlen Sie die gew&uuml;nschte Dokumentenklasse. Je nach dem, zu welcher Akte Sie ein Dokument hinzuf&uuml;gen wollen, ist standardm&auml;&szlig;ig 
    die Basisklasse f&uuml;r Ihr Dokument ausgew&auml;hlt oder eine Dokumentklasse vordefiniert. Wenn Sie eine Klasse angeklickt haben, 
    erscheinen rechts die Eigenschaften, die der Klasse zugeordnet sind.<br>
    Sollten Sie Fragen zu den Dokumentklassen haben, die Ihnen zur Verf&uuml;gung stehen, wenden Sie sich bitte an Ihren Administrator.
    <br>  
</p>
</ul>
<br>

<span class=OwHelpTopic3>
	2. Zugriffsrechte w&uuml;hlen (optional)
</span>

<p class="OwHelpText">
    Vergeben Sie ggf. Zugriffsrechte f&uuml;r die hinzugef&uuml;gten Dokumente. (Standardm&auml;&szlig;ig werden die
    	Zugriffsrechte des jeweiligen Ordners &uuml;bernommen.)<br><br>
</p>



<span class=OwHelpTopic3>
	3. Eigenschaften festlegen
</span>

<p class="OwHelpText">
    Wenn die Objektklasse zu Ihrem Dokument automatisch festgelegt wurde, starten Sie mit diesem Schritt.
    <br> Geben Sie hier die Eigenschaften des Dokuments ein. <br>
    Die mit Stern <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/required.png" alt="" title=""/>
    gekennzeichneten Eigenschaftsfelder sind Pflichtfelder, die ausgef&uuml;llt werden m&uuml;ssen, z.B. Kundennummer oder Dokumentenname.<br>
     
   	Falls Sie mehrere Dokumente auf einmal indizieren, werden die 
    Metadaten des ersten Dokuments f&uuml;r die n&auml;chsten Dokumente als Vorschlagswerte &uuml;bernommen, k&ouml;nnen anschlie&szlig;end 
    aber ge&auml;ndert werden. Um Ihnen die Indizierung zu erleichter, werden Ihnen die Dokumente neben der Eingabemaske im
    Viewer angezeigt.<br>
    
    Wenn Sie alle Angaben gemacht haben, klicken Sie auf "Speichern". <br><br>Die Dokumente werden ins ECM-System eingestellt.
    
</p>
<br>
<br>