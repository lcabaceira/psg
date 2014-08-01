<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
    Hilfe zum Dialog Eigenschaften bearbeiten
</span>

<p class="OwHelpIntro">
    <br><br>Der Dialog "Eigenschaften bearbeiten" dient zur Ansicht und &Auml;nderung der Eigenschaften 
    von Dokumenten. Zudem k&ouml;nnen Sie hier die Historie und Zugriffsrechte einsehen und feststellen,
    welche Versionen vorliegen und in welchen Akten das Dokument zu finden ist.<br>
    Dokumente werden parallel zum Eigenschaften bearbeiten Dialog im Viewer angezeigt. Bearbeiten Sie die Eigenschaften mehrerer Dokument auf einmal, 
    k&ouml;nnen Sie mit Hilfe der Pfeilsymbole zwischen ihnen wechseln. Auch die Ansicht im Viewer passt sich an.<br><br>
</p>


<span class="OwHelpTopic2">
    Funktionen
</span>

<br>
<br>
<p class="OwHelpText">
	 <!--  img src='<%=m_View.getContext().getBaseURL()%>/help_de/images/documentFunctions.png'><br><br-->
	Im Eigenschaften bearbeiten Dialog steht eine komplette Versionsverwaltung f&uuml;r Dokumente zur Verf&uuml;gung: 
	<br>
     <ul type="square" class="OwHelpText">
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkout.png" alt="" title=""/> 
        Auschecken: Sperrt das Dokument zum Bearbeiten. Dadurch k&ouml;nnen nur Sie auf das Dokument
        zugreifen und es z.B. downloaden und bearbeiten.
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cancelcheckout.png" alt="" title=""/> 
        Auschecken abbrechen: Entsperrt ein ausgechecktes Dokument, ohne dass &Auml;nderungen &uuml;bernommen werden. 
   	 	</li>
     	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/demoteversion.png" alt="" title=""/> 
        Version herunterstufen: Stuft die letzte Hauptversion zur letztm&ouml;glichen Nebenversion herab, z.B. von 2.0 auf 1.1. 
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/promoteversion.png" alt="" title=""/> 
        Version h&uuml;herstufen: Vergibt der letzten eingecheckten Nebenversion die n&auml;chstm&ouml;gliche Hauptversionsnummer, z.B. von 3.1 auf 4.0 (nur m&uuml;glich, 
        wenn das Dokument vorher herabgestuft wurde). 
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/save.png" alt="" title=""/> 
        Speichern: Speichert ein Dokument als neue Nebenversion ins System. Das Dokument bleibt jedoch gesperrt, bis Sie es einchecken.
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkin.png" alt="" title=""/> 
        Einchecken: Stellt ein Dokment als neue Neben- oder Hauptversion ins System ein (<%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","Hilfe zur Vorgehensweise","OwHelpLink")%>).
    	</li>
	</ul>	
</p>
<br>

<span class="OwHelpTopic2">
    Ansichten  
</span>
<br>
<br>

<span class="OwHelpTopic3">
	Eigenschaften
</span>

<p class="OwHelpText">	
    Hier k&uuml;nnen Sie die Eigenschaften eines Dokumentes &auml;ndern. F&uuml;gen Sie einfach die neuen Werte
    in die jeweiligen Felder ein. Um &Auml;nderungen zu &uuml;bernehmen, klicken Sie auf "Speichern".
    <br>
    Hinweis: Die mit einem Stern gekennzeichneten Eigenschaftsfelder sind Pflichtfelder und m&uuml;ssen ausgef&uuml;llt werden.
    Automatisch erstellte Eigenschaften wie Eingangsdatum oder Kundennummer k&ouml;nnen nicht ver&auml;ndert werden und erscheinen ausgegraut. <br>
    Um in langen Auswahllisten schnell zum gew&uuml;nschten Eintrag zu gelangen, k&ouml;nnen Sie die ersten Zeichen des Wertes angeben. Der Cursor springt 
    dann automatisch zum n&auml;chsten passenden Wert und die Auswahlliste verkleinert sich.<br><br>
</p>


<span class="OwHelpTopic3">
	Systemeigenschaften
</span>

<p class="OwHelpText">
    Systemeigenschaften sind vom System automatisch erzeugte Informationen zu Dokumenten und Akten. Sie k&ouml;nnen
    lediglich angesehen, aber nicht bearbeitet werden.<br><br>
</p>

<span class="OwHelpTopic3">
	Zugriffsrechte (optional)
</span>

<p class="OwHelpText">
     Hier wird aufgelistet, welcher Nutzer welche Zugriffsrechte auf das Dokument hat. Nur der Administrator
     kann Zugriffsrechte erteilen oder explizit verweigern. Sollten Sie nicht gen&uuml;gend Rechte besitzen,
     wenden Sie sich bitte an Ihren Administrator.<br><br>
</p>

<span class="OwHelpTopic3">
	Akten 
</span>

<p class="OwHelpText">
    In der Ansicht "Akten" wird aufgelistet, in welchen verschiedenen Akten (und welchen Unterordnern)
    das aktuell angezeigte Dokument zu finden ist. Der Link &ouml;ffnet dann die entsprechende 
    Akte sowie den Unterordner, in dem sich das Dokument befindet.<br><br>
</p>

<span class="OwHelpTopic3">
	Versionen 
</span>

<p class="OwHelpText">
     Hier finden Sie alle Versionen, die vom ausgew&auml;hlten Dokument verf&uuml;gbar sind, und k&ouml;nnen diese verwalten. 
     Wenn Sie eine bestimmte Version markieren, werden Ihnen die Eigenschaften dieser Version angezeigt.<br><br>
</p>



<span class="OwHelpTopic3">
	Historie 
</span>

<p class="OwHelpText">
     Diese Ansicht zeigt die Historie des Dokuments. Sie k&ouml;nnen die Liste der Eintr&auml;ge wie jede Liste im Wordesk sortieren, indem Sie 
     auf den jeweiligen Spaltentitel klicken. Welche Bearbeitungsschritte historisiert werden, legt der Administrator fest.<br><br>
</p>

<br>
<br>