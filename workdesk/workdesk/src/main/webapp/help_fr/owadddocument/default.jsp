 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Ajouter des documents

</span>

<p class=OwHelpIntro>
    <br>La commande «&nbsp;Ajouter des documents&nbsp;» permet d'ajouter de nouveaux documents de votre système de fichiers local vers le système ECM. Les documents ajoutés sont alors affichés dans le fichier électronique et le dossier sélectionnés.    <br><br>
</p>

<span class=OwHelpTopic2>
    Etapes

</span>

<p class="OwHelpText">
    <br>Il existe différentes méthodes possibles pour importer des documents&nbsp;: <br><br>
    <ol><li>1. Utilisation de la fonction glisser-déposer sur l'icône « Ajouter des documents » : sélectionnez les documents dans l'Explorateur Windows ou sur le bureau Windows, faites-les glisser vers l'icône « Ajouter des documents », puis déposez-les.</li></ol>
    <ol><li>2. Utilisation de la fonction glisser-déposer sur la liste de résultats : sélectionnez les documents dans l'Explorateur Windows ou sur le bureau Windows, faites-les glisser vers la liste de résultats, puis déposez-les. Cette fonctionnalité est disponible uniquement si vous avez activé l'importation de documents HTML5 dans les paramètres utilisateur et que vous utilisez un navigateur HTML5 pris en charge.</li></ol> 
    <ol><li>3. Utilisation de la fonction copier-coller : sélectionnez les documents dans l'Explorateur Windows ou sur le bureau Windows, copiez-les, faites un clic droit sur l'icône « Ajouter des documents » et sélectionnez « Coller ». Cette fonctionnalité est disponible uniquement si vous avez désactivé l'importation de documents HTML5 dans les paramètres utilisateur.</li></ol>
    <ol><li>4. Cliquez directement sur l'icône d'ajout de documents dans Workdesk et importez successivement les documents voulus&nbsp;:
    	<ul type="square" class="OwHelpText">        
    	<li>* Fichier local&nbsp;: sélectionnez un document dans votre système de fichiers local à l'aide de la commande «&nbsp;Parcourir&nbsp;». </li>
    	<li>* Aucun contenu&nbsp;: choisissez cette option, s'il s'agit simplement d'un ensemble de propriétés sans aucun document (par exemple, des données client).</li>
    	<li>* Puis cliquez sur «&nbsp;Importer&nbsp;». Une liste des objets importés apparaît. Une fois tous les fichiers voulus importés, cliquez sur «&nbsp;Suivant&nbsp;». </li>
		</ul>
    </li></ol>
    
    Suivez ensuite les instructions fournies dans la boîte de dialogue «&nbsp;Ajouter des documents&nbsp;»&nbsp;:<br><br>
</p>

<span class=OwHelpTopic3>
	1. Sélectionner une classe

</span>

<p class="OwHelpText">
    Sélectionnez une classe de document. Selon le fichier électronique auquel vous souhaitez ajouter un nouveau document, une classe standard est choisie par défaut ou une classe de document est prédéfinie. Une fois la classe sélectionnée, une description et les propriétés de celle-ci apparaissent à droite. <br>
    Pour toute question concernant les classes de documents disponibles, veuillez contacter votre administrateur. <br><br>
</p>

<span class=OwHelpTopic3>
	2. Définir des droits d'accès (facultatif)

</span>

<p class="OwHelpText">
  	S'il y a lieu, définissez les droits d'accès aux documents ajoutés. (Par défaut, les droits d'accès du dossier correspondant sont définis.)
  	<br><br>
</p>

<span class=OwHelpTopic3>
	3. Définir les propriétés

</span>

<p class="OwHelpText">
    Lorsque la classe de document est définie par défaut, vous commencez par cette étape. Vous définissez ici les propriétés du document.<br>
    Les champs signalés par un astérisque <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/required.png" alt="" title=""/>
    sont obligatoires. Lors de l'indexation de plusieurs documents, les métadonnées du premier document sont transmises aux suivants mais ne sont pas modifiables. Les documents sont affichés dans la visionneuse correspondante afin d'en faciliter l'indexation. <br>
    Cliquez sur «&nbsp;Suivant&nbsp;» pour continuer.
</p>
<br>

<p class="OwHelpText">                     
    Les documents sont ajoutés au système ECM.   
</p>
<br>