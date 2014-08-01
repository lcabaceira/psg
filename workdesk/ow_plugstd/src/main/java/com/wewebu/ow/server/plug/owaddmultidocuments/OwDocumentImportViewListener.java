package com.wewebu.ow.server.plug.owaddmultidocuments;

/**
 *<p>
 * Listener for import event triggered from {@link OwDocumentImportView}.
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
public interface OwDocumentImportViewListener
{
    /**Event triggered after a document was successfully imported from within {@link OwDocumentImportView}*/
    void onDocumentImported();
}