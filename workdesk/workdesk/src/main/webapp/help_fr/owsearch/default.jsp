 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<span class=OwHelpTopic1>
	Aide relative à la recherche

</span>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/search.png"  style="vertical-align: middle;" alt="" title=""/><br><br>
	«&nbsp;Recherche&nbsp;» permet de rechercher des documents et des fichiers électroniques/dossiers. Plusieurs modèles de recherche peuvent être proposés&nbsp;: recherche de fichiers électroniques/dossiers, recherche de documents et parfois, plusieurs modèles de recherche propres aux utilisateurs.<br>     
	<br>
    Vous pouvez sélectionner un modèle de recherche dans la barre de navigation (en général à gauche). Le modèle de recherche correspondant, qui présente des champs de saisie pour vos critères de recherche, s'ouvre alors à droite. 
  <br><br><br>
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        Critères de recherche<br>   
       
    </span>

</a>

<p class="OwHelpText">
    Les critères (nom, numéro de client, date de dernière modification, etc.) varient selon le modèle de recherche. De plus, il est possible d'utiliser des jokers pour remplacer un ou plusieurs caractères. Votre administrateur vous informera des symboles (par exemple&nbsp;: *, ?, %) que vous pouvez utiliser en tant que jokers. 
	<br><br>
    Saisissez vos critères de recherche dans le modèle de recherche. Vous pouvez supprimer les critères de recherche saisis en cliquant sur «&nbsp;Remise à zéro&nbsp;».  
	<br><br>
    Lorsque vous cliquez sur «&nbsp;Recherche&nbsp;», une liste de résultats apparaît avec les documents ou fichiers électroniques/dossiers correspondant à vos critères. Si vous n'entrez aucun critère de recherche, tous les fichiers électroniques/dossiers et documents existants apparaissent dans la liste.
    <br clear="all"><br><br>
</p>

<a name="#DocListViewModule">
    <span class=OwHelpTopic2>
        Liste de résultats
    
    </span>

</a>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/resultlist.png"  style="vertical-align: middle;" alt="" title=""/><br><br>

    La liste de résultats affiche tous les fichiers électroniques/dossiers et documents correspondant à vos critères de recherche. Vous pouvez trier la liste de résultats dans un ordre ascendant ou descendant en cliquant sur l'en-tête de colonne (nom, numéro de client, etc.). <br><br>
    
    Si vous cliquez sur un titre de document dans la liste de résultats, celui-ci s'ouvre dans la visionneuse. De la même manière que dans «&nbsp;Gestion des fichiers électroniques&nbsp;», vous pouvez également modifier les documents figurant dans la liste de résultats. Les fonctions disponibles ici dépendent de votre rôle d'utilisateur. Pour en savoir plus sur chaque fonction individuelle, consultez la rubrique d'aide <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%>.
    
    <br><br>
    Si vous cliquez sur un fichier électronique dans la liste de résultats, celui-ci s'ouvre dans «&nbsp;Gestion des fichiers électroniques&nbsp;». Retournez dans «&nbsp;Recherche&nbsp;» pour afficher de nouveau la dernière liste de résultats. Un clic sur le modèle de recherche précédemment utilisé fait apparaître vos entrées en tant que valeurs initiales.<br><br>    
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        <br>Astuces de recherche<br>   
       
    </span>

</a>

<p class="OwHelpText">
    Pour accélérer votre recherche, vous pouvez définir un «&nbsp;Nombre maximum de résultats&nbsp;» dans les modèles de recherche. Si les objets que vous recherchez ne figurent pas dans la liste de résultats, vous pouvez affiner votre recherche. 
    <br><br> 
    Vous avez également la possibilité d'enregistrer vos recherches fréquentes. Pour ce faire, nommez la nouvelle recherche au-dessus de la liste de résultats et cliquez sur «&nbsp;Enregistrer la recherche&nbsp;». Vous pourrez ensuite sélectionner cette recherche à l'aide d'une case à cocher incluse dans le modèle de recherche. <br clear="all"><br><br>
</p>
