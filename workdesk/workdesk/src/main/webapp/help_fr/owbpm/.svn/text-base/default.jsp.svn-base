 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Aide relative à BPM

</span>

<p class="OwHelpIntro">
	<br>Avec «&nbsp;P8 BPM&nbsp;» (pour la gestion de cas avec IBM FileNet P8 BPM) et «&nbsp;Alfresco BPM&nbsp;» (pour la gestion de cas avec Alfresco Workflow), vous pouvez aisément traiter les éléments de travail et les tâches. Selon votre rôle, vous avez accès à différentes boîtes de réception (uniquement dans FileNet P8 BPM), vues et fonctions pour le traitement des éléments de travail/tâches.  
</p>
<br><br>


<span class="OwHelpTopic2">
    Boîtes de réception et vues
</span>

<p class="OwHelpText">	
	<br>Vous pouvez sélectionner ces boîtes de réception et vues par un clic de souris dans la barre de navigation (souvent visible à gauche). Dans la plupart des cas, une boîte de réception personnelle, plusieurs boîtes de réception de groupe et des vues d'ensemble sont disponibles. <br><br>
</p>


<p class="OwHelpText">
	<ul>
		<li><b>Boîte de réception</b><br>
		Votre boîte de réception personnelle regroupe tous les éléments de travail/tâches que vous devez traiter personnellement. 
		<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/bpm_navigation.png"  alt="" title=""/>
		</li><br>
		<li><b>Boîtes de réception de groupe [uniquement disponibles dans IBM FileNet P8 BPM]</b><br>
		Selon votre rôle, vous avez accès à certaines boîtes de réception de groupe. Celles-ci contiennent les éléments de travail que vous pouvez traiter vous-même ou qui peuvent être traités par plusieurs autres personnes. </li><br>
		<li><b>Vue globale [uniquement disponible dans IBM FileNet P8 BPM]</b><br>
		Les vues globales présentent une section par le biais de différentes boîtes de réception (par exemple, tous les éléments de travail prioritaires ou simplement ceux qui concernent certains clients). </li><br>
	</ul>
</p>

<p class="OwHelpText">	
	<br>Les vues suivantes peuvent aussi être affichées&nbsp;:<br><br>
</p>
		
<p class="OwHelpText">
	<ul>		
		<li><b>Proxy [uniquement disponible dans IBM FileNet P8 BPM]</b><br>
		Cette vue vous permet d'accéder à tous les éléments de travail issus des boîtes de réception personnelles de vos collègues qui vous ont désigné comme proxy. </li><br>
		<li><b>Suivi [uniquement disponible dans IBM FileNet P8 BPM]</b><br>
		Si votre administrateur vous a attribué un rôle de suivi, vous trouverez dans cette vue tous les éléments de travail que vous devez suivre et analyser. </li><br>
	</ul>
</p>

<p class="OwHelpText">	
	<br>L'ensemble des boîtes de réception et des vues donnent accès à la vue Resoumission et aux fonctions de filtre, au-dessus de la liste d'éléments de travail et de tâches.<br><br>
</p>
		
<p class="OwHelpText">
	<ul>			
		
		<li><b>Resoumission</b><br>
		Les éléments de travail/tâches à traiter ultérieurement ou pouvant être resoumis. Ceux-ci n'apparaissent plus dans la boîte de réception/vue correspondante jusqu'à la date de resoumission. Pour afficher et modifier les éléments de travail/tâches resoumis, sélectionnez «&nbsp;Vue Resoumission&nbsp;». Pour revenir à la boîte de réception actuelle, il suffit d'annuler cette sélection. </li><br>
	    <li><b>Filtres</b><br>
	    Vous pouvez également limiter les vues en choisissant vos propres critères de filtre. Cliquez sur le symbole d'entonnoir en regard de l'en-tête de colonne et définissez vos critères de filtre. Cochez la case pour filtrer la vue. Désélectionnez cette case pour annuler le filtrage de la vue.</li>
	</ul>
</p>

<p class="OwHelpText">
	<br><br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/wv_filter.png"  alt="" title=""  style="vertical-align: middle;"/><br>
</p>

<p class="OwHelpText">	
	<br><br>Les éléments de travail/tâches s'affichent dans des listes dans toutes les boîtes de réceptions et les vues. Ils apparaissent dans différentes couleurs selon leur priorité de traitement. Des signes en couleur et/ou des indicateurs de priorité vous permettent de localiser plus rapidement les éléments de travail/tâches importants.<br>
	<br>
	Vous pouvez trier les listes d'éléments de travail/tâches en cliquant sur un en-tête de colonne. Vous pouvez également modifier la largeur et l'ordre des colonnes dans des listes AJAX telles qu'on les appelle dans l'Explorateur Windows. 
</p>

<br><br><br>


<span class="OwHelpTopic2">
   Traitement des éléments de travail/tâches<br><br>
   <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/wv_filter_context.png" alt="" title=""/>
</span>

<p class="OwHelpText">
    <br>Sélectionnez un ou plusieurs éléments de travail/tâches à traiter. Les éléments de travail sélectionnés sont verrouillés pour d'autres utilisateurs jusqu'à ce que vous les déverrouilliez, que vous vous déconnectiez ou changiez la vue (dans FileNet P8 BPM uniquement). Les éléments de travail verrouillés sont signalés par le symbole 
    <img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/locked.png"
     alt="<%=m_View.getContext().localize("help.image.locked16.gif","Locked image")%>" 
     title="<%=m_View.getContext().localize("help.image.locked16.gif","Locked image")%>" />.
    <br><br>
    Pour traiter les éléments de travail/tâches, vous pouvez utiliser les fonctions de base suivantes&nbsp;:
    <ul>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/jspprocessor.png" alt="" title=""/> / <img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/standardprocessor.png" alt="" title=""/>  Etape de traitement</li>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/returntosource.png" alt="" title=""/>  Retour</li>
		<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/reassign.png" alt="" title=""/>  Transférer</li>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/note.png" alt="" title=""/>  Insérer une note</li>				
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/resubmit.png" alt="" title=""/>  Resoumission</li>
	</ul>
	Vous pouvez ensuite afficher la piste d'audit d'un élément de travail ou rechercher certains éléments dans la piste d'audit 
	en cliquant sur 
	<img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history.png" alt="" title=""/> (Afficher l'historique) et sur <img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/history_search.png" alt="" title=""/> (Historique de recherche) [uniquement disponible dans IBM FileNet P8 BPM]. Pour toute question concernant les autres fonctions disponibles, veuillez contacter votre administrateur. 
	<br><br>
	Pour appliquer une fonction à un ou plusieurs éléments de travail/tâches, vous pouvez soit cliquer sur l'icône correspondante en regard du titre/de la description de l'élément de travail/la tâche, soit faire une sélection dans un menu contextuel.  
	<br><br>        
</p>

<span class="OwHelpTopic3">
	Etape de traitement

</span>

<p class="OwHelpText">
    Lorsque vous sélectionnez «&nbsp;Etape de traitement&nbsp;», les éléments de travail/tâches désignés s'ouvrent dans un formulaire de traitement. Vous pouvez y insérer des données et en vérifier et réaliser l'étape de traitement. Des conseils accompagnent chaque étape pour vous aider. Vous trouverez des informations supplémentaires à la page d'aide «&nbsp;Etape de traitement&nbsp;». <br>
    La barre de menus présente les mêmes symboles pour le traitement de l'élément de travail/la tâche suivant(e). Ceux-ci permettent d'ouvrir automatiquement l'élément de travail/la tâche suivant(e) à traiter. 
    <br><br>
</p>

<span class="OwHelpTopic3">
	Retour

</span>

<p class="OwHelpText">
    Si vous avez transféré des éléments de travail d'une boîte de réception de groupe vers votre boîte de réception personnelle, vous pouvez annuler cette étape à l'aide de «&nbsp;Retour&nbsp;». <br><br>
</p> 


<span class="OwHelpTopic3">
	Transférer

</span>

<p class="OwHelpText">
    La commande «&nbsp;Transférer&nbsp;» permet de déplacer des éléments de travail/tâches vers d'autres boîtes de réception de groupe (uniquement dans IBM FileNet P8 BPM), les boîtes de réception d'autres utilisateurs ou votre boîte de réception personnelle. Choisissez une boîte de réception dans la boîte de dialogue et cliquez sur «&nbsp;OK&nbsp;». L'ensemble des éléments de travail/tâches sélectionnés sont alors transférés. <br><br>
</p>


<span class="OwHelpTopic3">
	Resoumission

</span>

<p class="OwHelpText">
    Vous pouvez resoumettre des éléments de travail/tâches à une certaine date. Ceux-ci n'apparaissent plus dans leur boîte de dialogue respective. Afin d'en faciliter le traitement ultérieur, une note peut être ajoutée. <br>
	Vous pouvez afficher et modifier les éléments de travail/tâches resoumis dans la vue Resoumission. Si vous fixez la date de resoumission à aujourd'hui, les éléments de travail/tâches réapparaîtront dans leur boîte de dialogue respective. <br><br>
</p>


<span class="OwHelpTopic3">
	Insérer une note

</span>

<p class="OwHelpText">
    Une ou plusieurs notes peuvent être ajoutées à chaque élément de travail/tâche, notamment pour donner à vos collègues d'importants conseils de traitement. Les notes ajoutées auparavant restent affichées dans la piste d'audit (uniquement dans IBM FileNet P8 BPM). Les notes peuvent aussi être affichées sous forme de colonne dans certaines boîtes de réception et vues.<br><br>
</p>