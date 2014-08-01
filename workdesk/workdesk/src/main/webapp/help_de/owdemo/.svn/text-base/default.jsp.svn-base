<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Hilfe zum Bereich Demo
</span>
<p class="OwHelpIntro">
<br><br>Das Plugin "Demo" soll Software-Entwicklern den Einstieg in die Programmierung mit dem Alfresco Wordesk Framework
vereinfachen. Das "Demo"-Plugin zeigt auf einfache Weise, den Einsatz f&uuml;r Views/Layouts und Documents mit dem 
Framework.<br><br>
</p>

<span class=OwHelpTopic2>
	Klasse: OwDemoView
</span>
<p class="OwHelpText">
<br>Die Klasse "OwDemoView" wird von der Klasse "OwMainView" abgeleitet. In ihrer 
"Init"-Methode werden folgende Views eingebunden:
</p><br>
<ul type="square" class="OwHelpText">
    <li>OwDemoLayoutView</li>
	<li>OwDemoMenuView</li>
	<li>OwDemoDisplayView</li>
	<li>OwDemoColorLabelView</li>
</ul>

<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/demo.gif" alt="" title=""/>
<br>
<br>
<span class=OwHelpTopic3>
	Klasse: OwDemoLayoutView
</span>
<p class="OwHelpText">
Die Klasse "OwDemoLayoutView" dient als Tabellenger&uuml;st, um die anderen Views in der 
generierten Site zu platzieren. Innerhalb der "onRender"-Methode wird eine Tabelle mit 
HTML-Tags konstruiert. Durch den Aufruf der "renderRegion"-Methode kann ein als 
Region definierter View gerendert werden.
</p>
<br>

<span class=OwHelpTopic3>
	Klasse: OwDemoMenuView
</span>
<p class="OwHelpText">
Die im Demo-Plugin verwendeten Klassen "OwDemoMenuView", "OwDemoDisplayView" und "OwDemoColorLabelView" 
verdeutlichen das Zusammenspiel der einzelnen Views. So kann durch das Anklicken der vertikal ausgerichteten 
Links ("Red", "Green" und "Blue"), die Farbeigenschaften einer Zelle des "DisplayViews" ge&auml;ndert werden.
Hierbei wird eine der entsprechenden "onClick"-Methoden ausgel&ouml;st und die "setColor"-Methode aus "OwDemoDocument"
aufgerufen.
</p>
<br>

<span class=OwHelpTopic3>
	Klasse: OwDemoDisplayView
</span>
<p class="OwHelpText">
Die Klasse "OwDemoDisplayView" rendert eine Tabelle, deren Hintergrundzellfarbe dynamisch generiert wird.
Die Farbinformation ist in einer Konstanten in der Klasse "OwDemoDocument" gespeichert. Sie wird &uuml;ber die 
Methode "getColor" aus "OwDemoDocument" abgefragt.
</p>
<br>

<span class=OwHelpTopic3>
	Klasse: OwDemoColorLabelView
</span>
<p class="OwHelpText">
Die Klasse "OwDemoColorLabelView" stellt in ihrer "onRender"-Methode eine Tabelle dar, die die Hintergrundfarbe
des Display-Views ausgibt. Der Wert der Hintergrundfrabe wird in diesem Fall nicht durch eine der "onClick"-Methoden
in "OwDemoMenuView" ge&auml;ndert, sondern durch das Update-Event, das die "setColor"-Methode in "OwDemoDocument" 
aufruft.
</p>
<br>

<span class=OwHelpTopic3>
	Klasse: OwSubMenuView
</span>
<p class="OwHelpText">
Mit der Klasse "OwSubMenuView" wird das horizontal ausgerichtete Men&uuml; erstellt. Einzelne Men&uuml;punkte 
werden mit der Methode "addMenuItem" angef&uuml;gt, die in der "Init"-Methode der Klasse "OwDemoMenuView" 
aufgerufen wird.
</p>
<br>

<span class=OwHelpTopic2>
	Klasse: OwDemoDocument
</span>
<p class="OwHelpText">
<br>Die Klasse "OwDemoDocument" ist von der Klasse "OwMainDocument" abgeleitet. Sie hat folgende 
zus&auml;tzliche Methoden implementiert:
</p>
<ul type="square" class="OwHelpText">
    <li>setColor()</li>
	<li>getColor()</li>
</ul>
