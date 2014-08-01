package com.wewebu.ow.server.plug.owdms;

import java.io.IOException;
import java.util.List;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImportItemContentCollection;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Implementation of OwSaveDlgDialog to create a save only dialog for a given object.
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
public class OwSaveDialog extends OwSaveDlgDialog
{
    /** status of this dialog: nothing done yet */
    protected static final int DIALOG_STATUS_NONE = 0;
    /** status of this dialog: checkin successful*/
    protected static final int DIALOG_STATUS_OK = 1;
    /** status of this dialog: checkin failed */
    protected static final int DIALOG_STATUS_FAILED = 2;

    /** status of this dialog needed for historization */
    protected int m_dialogStatus = DIALOG_STATUS_NONE;

    /** construct a save dialog to save the given object
     *
     * @param saveObject_p OwObject to save
     * @param documentImporters_p List of document importers to use
     */
    public OwSaveDialog(OwObject saveObject_p, List documentImporters_p) throws Exception
    {
        super(saveObject_p.getResource(), true, documentImporters_p);
        ((OwSaveDlgDocument) getDocument()).setObjectTemplate(saveObject_p);
    }

    /**
     * get the current status of this dialog
     * @return the status
     */
    public int getStatus()
    {
        return (m_dialogStatus);
    }

    /** overridable to display the title of the dialog
      *
      * @return title
      */
    public String getTitle()
    {
        String strTitle = getContext().localize("owsavedialog.OwSaveDialog.title", "Save Document:");

        try
        {
            strTitle += OwHTMLHelper.encodeToSecureHTML(getCurrentDocument().getObjectTemplate().getName());
        }
        catch (IOException ex)
        {
            //fallback replace all existing < and > with #60 and #62 unicode
            strTitle += getCurrentDocument().getObjectTemplate().getName().replaceAll("[:<:]", "&#60;").replaceAll("[:>:]", "&#62;");
        }

        return strTitle;
    }

    /**
     * Implementation of the abstract method defined in <code>OwSaveDlgDialog</code> that
     * performs the actual save operation.
     */
    protected void performSave() throws Exception
    {
        try
        {
            // get the imported document and create the content collection
            OwDocumentImportItem documentImportItem = getCurrentDocument().getImportedDocument();
            if (documentImportItem == null)
            {
                // will be re-thrown. so no problem to throw within try/catch
                throw new IllegalStateException("OwSaveDialog.performSave() has been invoked without an OwDocumentImportItem set at the OwSaveDlgDocument");
            }
            OwContentCollection contentCollection = new OwDocumentImportItemContentCollection(documentImportItem);
            // save the imported document
            OwObject obj = getCurrentDocument().getObjectTemplate();
            if (obj.hasVersionSeries())
            {
                obj.getVersion().save(contentCollection, documentImportItem.getContentMimeType(0), documentImportItem.getContentMimeParameter(0));
            }
            else
            {
                obj.setContentCollection(contentCollection);
            }

            //check if postprocessing should be executed 
            openPostProcessingView(getCurrentDocument().getDocumentImporter(), obj);
            // release the imported document
            documentImportItem.release();
        }
        catch (Exception e)
        {
            // flag failure
            m_dialogStatus = DIALOG_STATUS_FAILED;
            // re-throw exception
            throw e;
        }
        close();
    }

    private void close() throws Exception
    {
        if (!isPostProcessDialogOpen())
        {
            // flag success
            m_dialogStatus = DIALOG_STATUS_OK;

            // cleanup
            cleanup();

            // close dialog
            closeDialog();
        }
    }

    protected void onPostProcessDialogClose(OwDialog dialog_p) throws Exception
    {
        close();
    }
}