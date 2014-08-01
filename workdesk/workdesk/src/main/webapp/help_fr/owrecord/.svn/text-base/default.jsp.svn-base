 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Aide relative à la gestion des fichiers électroniques

</span>

<p class=OwHelpIntro>
	<br>Gérez aisément vos fichiers électroniques dans «&nbsp;Gestion des fichiers électroniques&nbsp;». Pour ouvrir un fichier électronique, cliquez sur son titre, soit dans la liste de résultats d'une recherche, soit dans une pièce jointe à un élément de travail, soit directement dans «&nbsp;Gestion des fichiers électroniques&nbsp;» à partir de la liste des derniers fichiers électroniques ouverts. Les quatre derniers fichiers électroniques que vous avez ouverts sont enregistrés ici et apparaissent généralement au-dessus de la vue des fichiers électroniques. Cette liste est également disponible après une reconnexion.  
	<br><br>
	<br clear="all">
</p>

<a name="#RecordTreeViewModule">
    <span class=OwHelpTopic2>
        Vue des fichiers électroniques et dossiers  
      
    </span>

</a>

<p class="OwHelpText">
    Lorsque vous sélectionnez un fichier électronique, des informations relatives à ce fichier, telles que le numéro de client, le nom du client, les dernières modifications, etc., apparaissent généralement au-dessus de la structure du fichier électronique.  <br>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/filefolderview.png" alt="" title=""/>
    <br><br>La structure d'un fichier électronique, avec ses dossiers et sous-dossiers, apparaît sous forme d'arborescence. Cliquez sur le signe plus-moins en regard d'un dossier pour afficher ou masquer les sous-dossiers qu'il contient. Pour afficher le contenu d'un (sous)-dossier, cliquez sur son nom. Le dossier affiché est indiqué. 
    <br><br>
	La vue du dossier montre le contenu du dossier sélectionné. Vous pouvez, par un clic de souris, ouvrir des documents dans une visionneuse ou, le cas échéant, dans un programme d'édition. Plusieurs fonctions applicables aux documents sont également disponibles (voir ci-dessous). Vous pouvez choisir parmi plusieurs types de listes de documents pour afficher le contenu d'un dossier&nbsp;: 
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewRow.png" alt="" title=""/> 
	Liste de documents simple

</span>
<p class="OwHelpText">
    Ce type de liste présente les documents avec leur nom et d'autres propriétés. Le dernier document ouvert est indiqué (en gras, par exemple). Les documents déjà édités sont différenciés des autres (en italique par exemple). Les documents récemment utilisés sont ainsi faciles à retrouver.    
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewThumbnail.png" alt="" title=""/> 
	Liste de vignettes

</span>
<p class="OwHelpText">
    Ce type de liste présente des vignettes des documents. Vous pouvez choisir la taille des vignettes (petite, moyenne ou grande). 
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewCombined.png" alt="" title=""/> 
	Liste de documents avec vignettes

</span>
<p class="OwHelpText">
    Ce type de liste contient les deux types précédemment décrits. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewEXTJSGrid.png" alt="" title=""/> 
	Liste AJAX

</span>
<p class="OwHelpText">
    Les listes AJAX présentent les documents de la même manière que les listes de documents simples. Les listes AJAX offrent toutefois beaucoup plus de flexibilité&nbsp;: par exemple, la vignette d'un document s'affiche lors de l'arrêt du curseur pendant un bref instant sur un élément de la liste. <br> 
    Vous pouvez également modifier l'ordre et la largeur des colonnes. Changez l'ordre des colonnes par glisser-déposer en cliquant sur l'en-tête de colonne, en la faisant glisser vers la position voulue et en la déposant. La largeur des colonnes peut aisément être modifiée en déplaçant simplement le séparateur vers la position voulue tout en maintenant appuyé le bouton gauche de la souris. Un double-clic sur le séparateur permet d'obtenir automatiquement un ajustement optimal de la colonne. <br>
    Les listes AJAX permettent de modifier directement les métadonnées affichées des documents. Double-cliquez simplement dans le champ respectif, puis modifiez, saisissez ou remplacez l'entrée précédente. Si un formatage spécial est nécessaire (pour les numéros de client, par exemple), les valeurs correctes sont affichées dans une zone de sélection déroulante. Vous pouvez y sélectionner rapidement la propriété voulue en saisissant les premières lettres de celle-ci. Cette fonction est disponible pour toutes les listes de choix dans Workdesk.
    <br><br>
</p>
	
<p class="OwHelpText">	
	Toutes les listes, à l'exception de la liste de vignettes, peuvent être triées dans un ordre ascendant ou descendant en cliquant sur l'en-tête de colonne respectif. <br clear="all"><br><br>
</p>

<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Fonctions applicables aux fichiers électroniques et aux documents 
    </p>

</a>

<p class="OwHelpText">
	<br>Selon votre rôle d'utilisateur et la structure du fichier électronique, certaines des fonctions suivantes permettent d'éditer les fichiers électroniques et les documents. Certaines peuvent être exécutées directement, d'autres affichent une boîte de dialogue. Toutes les boîtes de dialogue peuvent être annulées ou fermées en cliquant sur le symbole de fermeture (en général, en haut à droite ou en haut à gauche).    
	<br>  
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/edit_properties.png" alt="" title=""/> 
	Editer les propriétés

</span>
<p class="OwHelpText">
    ... ouvre une boîte de dialogue qui permet d'afficher et d'éditer les propriétés du fichier électronique actuel ou des documents sélectionnés. Pour de plus amples informations, veuillez consulter la page d'aide <%=((OwInfoView)m_View).getLink("owdocprops/default.jsp","Edit Properties","OwHelpLink")%>. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history.png" alt="" title=""/> 
	Afficher l'historique

</span>
<p class="OwHelpText">
    ... ouvre la piste d'audit du fichier électronique/dossier actuel ou les documents sélectionnés respectivement. Vous pouvez trier les éléments de la même manière que les documents, dans un ordre ascendant ou descendant, en cliquant sur l'en-tête de colonne correspondant. 
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history_search.png" alt="" title=""/> 
	Historique de recherche

</span>
<p class="OwHelpText">
    Cette fonction permet d'effectuer une recherche sélective des éléments de la piste d'audit. Saisissez simplement vos critères de recherche et cliquez sur «&nbsp;Recherche&nbsp;». Seuls les éléments correspondant à ces critères sont alors affichés. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bookmark.png" alt="" title=""/> 
	Ajouter aux favoris

</span>
<p class="OwHelpText">
    ... enregistre dans vos favoris un lien au fichier électronique/dossier actuel ou aux documents sélectionnés. «&nbsp;<%=((OwInfoView)m_View).getLink("owshortcut/default.jsp","Favorites","OwHelpLink")%>&nbsp;» permet donc d'accéder rapidement aux fichiers électroniques/dossiers et aux documents fréquemment utilisés sans les rechercher. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/copy.png" alt="" title=""/> 
	Copier

</span>
<p class="OwHelpText">
    ... copie dans le presse-papiers le fichier électronique/dossier actuel ou les documents sélectionnés. Vous pouvez ainsi les ajouter facilement en pièce jointe à des éléments de travail, par exemple. Lorsque vous collez un document depuis le presse-papiers, le document d'origine reste dans le dossier d'origine. Lorsque vous déplacez des documents, ceux-ci sont transférés de leur dossier d'origine vers le nouveau dossier.<br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/deletebtn.png" alt="" title=""/> 
	Supprimer

</span>
<p class="OwHelpText">
    Si votre rôle d'utilisateur vous y autorise, cette fonction vous permet de supprimer définitivement des documents, dossiers et fichiers électroniques. <br>
</p>



<br><br>

<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Fonctions applicables aux fichiers électroniques 
    </p>

</a>

<p class="OwHelpText">
	<br>Certaines des fonctions suivantes sont parfois également disponibles pour modifier les fichiers électroniques/dossiers&nbsp;:
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Créer un nouveau fichier électronique/dossier

</span>
<p class="OwHelpText">
    ... ouvre une boîte de dialogue permettant la création de nouveaux fichiers électroniques/dossiers. Saisissez le nom du nouveau fichier électronique/dossier et définissez ses propriétés. Puis cliquez sur «&nbsp;Enregistrer&nbsp;» pour créer le nouveau fichier électronique/dossier. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Créer un sous-dossier

</span>
<p class="OwHelpText">
    ... ouvre une boîte de dialogue permettant la création d'un nouveau sous-dossier dans le fichier électronique ou dossier actuel. Choisissez en premier lieu la classe du fichier électronique/dossier, puis cliquez sur «&nbsp;Suivant&nbsp;». Définissez ensuite les propriétés du sous-dossier, puis cliquez sur «&nbsp;Enregistrer&nbsp;». Le nouveau sous-dossier est créé. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_document.png" alt="" title=""/> 
	Ajouter un document

</span>
<p class="OwHelpText">
    Cette fonction permet d'ajouter des documents dans le dossier actuel. Vous pouvez utiliser différentes méthodes (glisser-déposer, copier-coller ou importation). Vous trouverez une description détaillée <%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","here","OwHelpLink")%>. 
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/paste.png" alt="" title=""/> 
	Coller des documents à partir du presse-papiers

</span>
<p class="OwHelpText">
    Lorsque vous copiez des documents depuis un fichier électronique/dossier, une liste de résultats ou un élément de travail vers le presse-papiers, cette fonction vous permet de les coller (en copie) dans le dossier actuel. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/move.png" alt="" title=""/> 
	Déplacer un document à partir du presse-papiers

</span>
<p class="OwHelpText">
    Lorsque vous copiez des documents depuis un fichier électronique/dossier, une liste de résultats ou un élément de travail vers le presse-papiers, cette fonction vous permet de les déplacer vers le dossier actuel. Tout comme avec la fonction copier-coller, ces documents n'apparaissent plus dans leur emplacement d'origine. <br>
</p>



<br><br>
<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Fonctions applicables aux documents 
    </p>

</a>

<p class="OwHelpText">
	<br>Certaines des fonctions suivantes sont parfois disponibles pour éditer les documents&nbsp;:
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cut.png" alt="" title=""/> 
	Couper

</span>
<p class="OwHelpText">
    ... coupe les documents sélectionnés depuis leur dossier actuel pour les placer dans le presse-papiers. Vous pouvez ainsi les déplacer facilement vers d'autres dossiers ou fichiers électroniques. Si vous ne les collez pas dans un nouvel emplacement, ils sont conservés dans leur dossier d'origine. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkout.png" alt="" title=""/> 
	Réserver une copie de travail

</span>
<p class="OwHelpText">
     ... verrouille les documents aux fins d'édition. Vous seul avez ainsi accès à ces documents, notamment pour les télécharger et les éditer. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cancelcheckout.png" alt="" title=""/> 
	Annuler la réservation

</span>
<p class="OwHelpText">
    ... déverrouille les documents réservés sans enregistrer les modifications. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/download.png" alt="" title=""/> 
	Télécharger

</span>
<p class="OwHelpText">
    ... enregistre les documents en copie locale aux fins d'édition. Pour empêcher que d'autres utilisateurs n'éditent simultanément les mêmes documents, vous devez d'abord les réserver.   
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/save.png" alt="" title=""/> 
	Enregistrer

</span>
<p class="OwHelpText">
    ... enregistre un document en tant que nouvelle version mineure dans le système. Une boîte de dialogue permet de sélectionner et d'importer le document voulu. Celui-ci reste toutefois réservé jusqu'à ce que vous le libériez.  
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkin.png" alt="" title=""/> 
	Libérer la copie de travail

</span>
<p class="OwHelpText">
    ... enregistre un document en tant que nouvelle version majeure ou mineure dans le système et le déverrouille. Une boîte de dialogue permet de modifier les propriétés du document, de sélectionner un document dans votre système de fichiers et de l'importer. Cette procédure est 
    semblable à <%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","adding a document","OwHelpLink")%>.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/demoteversion.png" alt="" title=""/> 
	Rétrograder la version

</span>
<p class="OwHelpText">
    ... rétrograde la version majeure la plus récente à la dernière version mineure possible, par exemple de&nbsp;2.0 à&nbsp;1.1. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/promoteversion.png" alt="" title=""/> 
	Promouvoir la version

</span>
<p class="OwHelpText">
    ... promeut la version mineure la plus récente à la version majeure suivante la plus élevée, par exemple de&nbsp;3.1 à&nbsp;4.0.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/remove.png" alt="" title=""/> 
	Retirer

</span>
<p class="OwHelpText">
    ... retire le document d'un fichier électronique/dossier sans le supprimer. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/docview.png" alt="" title=""/> 
	Comparer les documents

</span>
<p class="OwHelpText">
    ... ouvre les documents sélectionnés dans la visionneuse, dans des fenêtres en mosaïque. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_note.png" alt="" title=""/> 
	Insérer une note

</span>
<p class="OwHelpText">
     ... ouvre une boîte de dialogue permettant l'insertion de notes, notamment pour donner à vos collègues des recommandations en matière d'édition. Les notes existantes ne sont pas écrasées, mais sont conservées dans la piste d'audit. Cliquez sur «&nbsp;Enregistrer&nbsp;» pour enregistrer votre note. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/sendlink.png" alt="" title=""/> 
	Lien e-mail

</span>
<p class="OwHelpText">
    ... ouvre un nouvel e-mail contenant des liens aux documents sélectionnés dans votre programme de messagerie électronique par défaut. <br>
</p>




<br>