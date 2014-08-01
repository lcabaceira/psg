 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Aide relative à Démo

</span>
<p class="OwHelpIntro">
	<br>Le module d'extension «&nbsp;Démo&nbsp;» doit aider les développeurs de logiciels à se lancer dans la programmation d'Workdesk Framework. Il montre comment utiliser les vues/dispositions et les documents avec Workdesk Framework.<br><br>
</p>

<span class=OwHelpTopic2>
	Classe&nbsp;: OwDemoView

</span>
<p class="OwHelpText">
<br>La classe «&nbsp;OwDemoView&nbsp;» est dérivée de la classe «&nbsp;OwMainView&nbsp;». La méthode «&nbsp;init&nbsp;» de «&nbsp;OwDemoView&nbsp;» comprend les vues suivantes&nbsp;:
<br><br></p>
<ul type="square" class="OwHelpText">
    <li>OwDemoLayoutView</li>
	<li>OwDemoMenuView</li>
	<li>OwDemoDisplayView</li>
	<li>OwDemoColorLabelView</li>
</ul>

<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/demo.gif" alt="" title=""/>
<br>
<br>
<span class=OwHelpTopic3>
	Classe&nbsp;: OwDemoLayoutView

</span>
<p class="OwHelpText">
La classe «&nbsp;OwDemoLayoutView&nbsp;» sert de grille de tableau et place les autres vues dans le site généré. La méthode «&nbsp;onRender&nbsp;» crée un tableau avec des tags HTML. La méthode «&nbsp;renderRegion&nbsp;» permet de rendre une vue définie en tant que région.
</p>
<br>

<span class=OwHelpTopic3>
	Classe&nbsp;: OwDemoMenuView

</span>
<p class="OwHelpText">
Les classes «&nbsp;OwDemoMenuView&nbsp;», «&nbsp;OwDemoDisplayView&nbsp;» et «&nbsp;OwDemoColorLabelView&nbsp;» utilisées pour le module d'extension démo expliquent comment les différentes vues interagissent. Vous pouvez changer les propriétés de couleur d'une cellule «&nbsp;DisplayView&nbsp;» en cliquant sur un des liens («&nbsp;Rouge&nbsp;», «&nbsp;Vert&nbsp;» et «&nbsp;Bleu&nbsp;»). La méthode «&nbsp;onClick&nbsp;» appelle la méthode «&nbsp;setColor&nbsp;» de «&nbsp;OwDemoDocument&nbsp;».
</p>
<br>

<span class=OwHelpTopic3>
	Classe&nbsp;: OwDemoDisplayView

</span>
<p class="OwHelpText">
La classe « OwDemoDisplayView&nbsp;» rend un tableau dont la couleur de fond des cellules est générée dynamiquement. Les informations relatives à la couleur sont enregistrées dans une variable de classe «&nbsp;OwDemoDocument&nbsp;». Vous pouvez les invoquer avec la méthode «&nbsp;getColor&nbsp;» à partir de «&nbsp;OwDemoDocument&nbsp;». 
</p>
<br>

<span class=OwHelpTopic3>
	Classe&nbsp;: OwDemoColorLabelView

</span>
<p class="OwHelpText">
Avec sa méthode «&nbsp;onRender&nbsp;», la classe «&nbsp;OwDemoColorLabelView&nbsp;» crée un tableau qui affiche la couleur de fond de la vue d'affichage. Dans ce cas, la valeur de la couleur de fond n'est pas modifiée par la méthode «&nbsp;onClick&nbsp;» de «&nbsp;OwDemoMenuView&nbsp;». Elle est en revanche modifiée par un événement de mise à jour appelant la méthode «&nbsp;setColor&nbsp;» de «&nbsp;OwDemoDocument&nbsp;». 
</p>
<br>

<span class=OwHelpTopic3>
	Classe&nbsp;: OwSubMenuView

</span>
<p class="OwHelpText">
La classe «&nbsp;OwSubMenuView&nbsp;» crée le menu aligné horizontalement. Chaque entrée de menu est ajoutée à l'aide de la méthode «&nbsp;addMenuItem&nbsp;» qui est appelée par la méthode «&nbsp;init&nbsp;» de la classe «&nbsp;OwDemoMenuView&nbsp;».
</p>
<br>

<span class=OwHelpTopic2>
	Classe&nbsp;: OwDemoDocument

</span>
<p class="OwHelpText">
<br>La classe «&nbsp;OwDemoDocument&nbsp;» est dérivée de la classe «&nbsp;OwMainDocument&nbsp;». Les autres méthodes suivantes sont mises en œuvre&nbsp;:
</p>
<ul type="square" class="OwHelpText">
    <li>setColor()</li>
	<li>getColor()</li>
</ul>
