package com.wewebu.ow.server.app;

/**
 *<p>
 * Callback interface invoked by the OwDocumentImporter.<br>
 * A document importer can be used by plugins that gather content like the add document,
 * the save or the checkin plugins to receive the content from the user.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
public interface OwDocumentImporterCallback
{

    /**
     * Invoked by a document importer after it has finished importing one document.
     * An importer is able to import multiple documents one by one and hold some in
     * a List. This method is invoked after each import to import the plugin that
     * this List now contains at least one element.
     * 
     * @param importer_p the importer that has just imported a document
     * @param item_p the imported document
     */
    public void onDocumentImported(OwDocumentImporter importer_p, OwDocumentImportItem item_p) throws Exception;

}