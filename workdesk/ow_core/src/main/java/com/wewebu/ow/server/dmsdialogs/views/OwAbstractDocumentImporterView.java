package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Abstract default DocumentImporterView, implementing the most functionality by default.
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
 *@since 2.5.2.0
 */
public abstract class OwAbstractDocumentImporterView extends OwLayout
{
    /**OwDocumentImporter to which this view belongs to*/
    private OwDocumentImporter importer;
    /**OwDocumentImporterCallback reference, which handles onDocumentImported events*/
    private OwDocumentImporterCallback documentImporterCallback;

    /**
     * Constructor for view, referencing the most important dependencies.<br />
     * Will throw an IllegalArgumentException if one of the parameter is null. 
     * @param importer_p OwDocumentImporter (non-null)
     * @param callback_p OwDocumentImporterCallback (non-null)
     */
    public OwAbstractDocumentImporterView(OwDocumentImporter importer_p, OwDocumentImporterCallback callback_p)
    {
        super();
        if (importer_p == null)
        {
            throw new IllegalArgumentException("Instantiation of DocumentImporterView faild: importer_p is null");
        }
        setDocumentImporterCallback(callback_p);
        this.importer = importer_p;
    }

    /**
     * Set the reference for callback handler.
     * <p>Will throw an IllegalArgumentException if <b>callback_p</b> is null</p>
     * @param callback_p OwDocumentImporterCallback (non-null)
     */
    public void setDocumentImporterCallback(OwDocumentImporterCallback callback_p)
    {
        if (callback_p == null)
        {
            throw new IllegalArgumentException("Setting DocumentImporterCallback failed: callback_p is null");
        }
        this.documentImporterCallback = callback_p;
    }

    protected void init() throws Exception
    {
        super.init();
    }

    protected abstract void onRender(Writer w_p) throws Exception;

    /**
     * Return the OwDocumentImporter which is current set.
     * @return OwDocumentImporter
     */
    protected OwDocumentImporter getDocumentImporter()
    {
        return this.importer;
    }

    /**
     * Return the current set OwDocumentImporterCallback handler.
     * @return OwDocumentImporterCallback 
     */
    protected OwDocumentImporterCallback getDocumentImporterCallback()
    {
        return this.documentImporterCallback;
    }

    /**
     * Helper method which should be used after a 
     * document was imported to notify the callback handler. 
     * @param item_p OwDocumentImportItem (non-null value)
     * @throws Exception if <code>item_p == null</code> or onDocumentImported-process failed
     * @since 2.5.2.0
     */
    protected void fireOnDocumentImportEvent(OwDocumentImportItem item_p) throws Exception
    {
        if (item_p == null)
        {
            throw new IllegalArgumentException("Call of DocumentImporterCallback failed: OwDoucmentImportItem is null");
        }
        getDocumentImporterCallback().onDocumentImported(getDocumentImporter(), item_p);
    }
}