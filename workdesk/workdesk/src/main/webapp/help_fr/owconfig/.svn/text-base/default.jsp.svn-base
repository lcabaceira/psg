 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Aide relative à l'administration

</span>

<p class="OwHelpIntro">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/administration.png" alt="" title=""/><br><br> 
	<em>Administration</em> fournit une interface utilisateur au Gestionnaire de rôles DB d'Workdesk et héberge des liens aux outils de modélisation d'infrastructure provenant du fournisseur ECM correspondant, si ceux-ci sont configurés. 
</p>
<br>


<span class="OwHelpTopic2"> 
    Gestionnaire de rôles DB 
 
</span>

<p class="OwHelpText">	 
	<br> 
	Pour assigner des ressources à un rôle, sélectionnez un rôle en cliquant sur le bouton doté de trois points. Si vous avez préalablement choisi un rôle, vous pouvez sélectionner celui-ci dans la zone déroulante. <br><br> 
</p>

<span class="OwHelpTopic3"> 
    Classe d'objet 
 
</span>

<p class="OwHelpText"> 
	Assignez des classes d'objets à un rôle. Important&nbsp;: par défaut, aucune classe d'objet n'est assignée aux rôles. Nous recommandons, dans un premier temps, d'autoriser toutes les classes d'objets pour tous les rôles puis, dans un deuxième temps, de supprimer les classes d'objets inutiles.  
	<br><br>
</p> 
 
<span class="OwHelpTopic3">Fonction supplémentaire 
 
</span>

<p class="OwHelpText">
	Dans <%=((OwInfoView)m_View).getLink("owsettings/default.jsp","Settings","OwHelpLink")%>, un Administrateur assigne à un rôle des fonctionnalités supplémentaires. Il peut, par exemple, accorder aux utilisateurs d'un rôle des options de configuration supplémentaires dans leurs <em>Paramètres</em>.  
	<br><br>
</p>

<span class="OwHelpTopic3">
    Module d'extension

</span>

<p class="OwHelpText"> 
	Assignez des modules d'extension à un rôle. <br><br>
</p>

<span class="OwHelpTopic3"> 
    File d'attente virtuelle 
 
</span>

<p class="OwHelpText"> 
	Assignez des files d'attente virtuelles préconfigurées à un rôle. <br><br>
</p>

<span class="OwHelpTopic3">
    Design

</span>

<p class="OwHelpText"> 
	Assignez un design à un rôle. <br><br>
</p>

<span class="OwHelpTopic3">
    Champs d'index

</span>

<p class="OwHelpText"> 
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/indexfields.png" alt="" title=""/><br><br>Définissez le traitement des champs d'index dans Workdesk. Ceux-ci sont affichés par classe d'objet. Chaque champ d'index comporte trois entrées correspondant au contexte d'utilisation de ce champ d'index dans Workdesk. Ces contextes sont <em>Créer</em>, <em>Libérer</em> et <em>Visualiser</em>.</p><p class="OwHelpText">Pour chaque contexte, un Administrateur peut spécifier si le champ d'index correspondant est censé être présenté à l'utilisateur, en cochant ou en désélectionnant les options <em>Autoriser </em> ou <em>Refuser</em>. Dans chaque contexte, la valeur par défaut pour chaque champ d'index est <em>Autoriser</em>. Si toutefois un rôle hérite de paramètres Autoriser pour des champs d'index ou des contextes qui ne sont pas censés être visibles par les utilisateurs, un paramètre explicite <em>Refuser </em> peut être défini. 
	<br><br> 
</p>

<span class="OwHelpTopic3">
	Modèle de recherche

</span>

<p class="OwHelpText"> 
	Assignez des modèles de recherche à un rôle. <br><br>
</p>
	
<p class="OwHelpText">	
	Vous pouvez autoriser ou explicitement refuser l'accès à tous les éléments de la catégorie sélectionnée. Cliquez sur «&nbsp;Enregistrer&nbsp;» pour enregistrer les modifications. Celles-ci seront validées lors de la prochaine ouverture de session. <br>
	Les cases cochées indiquent les paramètres actuellement enregistrés. Si plusieurs rôles sont assignés à un seul utilisateur, les éléments refusés s'appliquent en premier.<br><br>
</p>


<span class="OwHelpTopic2">
    Outils

</span>

<p class="OwHelpText">	
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/tools.png" alt="" title=""/><br><br>
	<br>L'onglet «&nbsp;Outils&nbsp;» contient des liens à des outils externes tels que Search Designer ou Process Designer d'IBM FileNet P8. Cliquez sur un lien pour lancer l'outil correspondant. <br><br>
</p>