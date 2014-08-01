package com.wewebu.ow.server.docimport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwStandardDocumentImportItem;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.dmsdialogs.views.OwAbstractDocumentImporterView;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.upload.OwMultiPart;
import com.wewebu.ow.server.util.upload.OwUpStreamParser;

/**
 *<p>
 * View class for OwLocalFileDocumentImporter.
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
public class OwLocalFileDocumentImporterView extends OwAbstractDocumentImporterView
{

    /** region for the optional menu */
    public static final int MENU_REGION = 1;

    /** flag to indicate that at most one file per document is allowed */
    protected boolean m_singleFileImports;

    /** Menu for buttons like "Add" */
    protected OwSubMenuView m_MenuView;

    /**
     * Create a new OwNoContentDocumentImporterView for a given callback.
     * 
     * @param callback_p the callback to invoke after a document has been imported
     */
    public OwLocalFileDocumentImporterView(OwLocalFileDocumentImporter importer_p, boolean singleFileImports_p, OwDocumentImporterCallback callback_p)
    {
        super(importer_p, callback_p);
        if (importer_p == null)
        {
            throw new IllegalArgumentException("OwLocalFileDocumentImporterView must not be instantiated without importer_p");
        }
        m_singleFileImports = singleFileImports_p;
    }

    /**
     * Init the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

    }

    /**
     * to get additional form attributes used for the form
     * override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        return "ENCTYPE='multipart/form-data'";
    }

    /**
     * Render this OwDocumentImportView by including a JSP page
     * 
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("docimport/OwLocalFileDocumentImporterView.jsp", w_p);
    }

    /**
     * performs an upload from the HTML input type file element.
     * saves the files in a temp dir using temp names which are stored in m_uploadTempFileNames.
     * the real fine names are stored in m_uploadedFileList
     * content type must be "multipart/form-data"
     * @param request_p
     * @param reason_p
     * @return true if upload was successful
     * @throws Exception
     */
    public boolean onUpload(HttpServletRequest request_p, Object reason_p) throws Exception
    {
        HashMap requestParameters = new HashMap();
        OwStandardDocumentImportItem importedDocument = null;
        try
        {
            // parse the upload stream and write the files of the MultiPart content to tempdir
            OwUpStreamParser mp = new OwUpStreamParser(request_p);
            OwMultiPart filePart;
            String fileName = null;
            OwLocalFileDocumentImporter importer = (OwLocalFileDocumentImporter) getDocumentImporter();
            while ((filePart = mp.readNextPart()) != null)
            {
                // name is either the filename or the name of the request parameter
                String name = filePart.getName();
                if (filePart.isFile())
                {
                    fileName = filePart.getFileName();
                    if (fileName != null)
                    {
                        // check if we already found a file in the POST stream
                        if (importedDocument != null)
                        {
                            throw new OwServerException("The POST data stream must contain at most one file.");
                        }
                        // write the file to temp dir
                        int importID = importer.getTempFileID();
                        fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
                        long importSize = filePart.writeTo(new File(importer.getTempDir()), Integer.toString(importID));
                        if (importSize == 0)
                        {
                            throw new OwServerException(getContext().localize("plug.docimport.OwNoContentDocumentImporterView.ErrorEmptyFile", "The specified file contains no content. You cannot create empty files."));
                        }
                        importedDocument = new OwStandardDocumentImportItem(importer.getTempDir(), importID, fileName);
                    }
                }
                // part is not a file but a request parameter
                else
                {
                    String paramValue = handleReqestParameterPart(filePart);
                    requestParameters.put(name, paramValue);
                }
            }
            // tidy up.
            mp.tidyUp();

        }
        catch (IOException ie)
        {
            throw ie;
        }

        if (importedDocument == null)
        {
            throw new OwServerException(getContext().localize("plug.docimport.OwNoContentDocumentImporterView.ErrorNoFile", "Please enter a file."));
        }

        fireOnDocumentImportEvent(importedDocument);

        return true;
    }

    /**
     * performs an upload from the HTML input type file element.
     * saves the files in a temp dir using temp names which are stored in m_uploadTempFileNames.
     * the real fine names are stored in m_uploadedFileList
     * content type must be "multipart/form-data"
     * @param request_p
     * @return true if upload was successful
     * @throws Exception
     * @since 4.2.0.0
     */
    public boolean onUpload(HttpServletRequest request_p) throws Exception
    {
        return onUpload(request_p, null);
    }

    /**
     * fetch the request parameter value
     * @param filePart_p
     * @return the request parameter value
     * @throws IOException
     */
    private String handleReqestParameterPart(OwMultiPart filePart_p) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        filePart_p.writeTo(outputStream);
        String paramValue = outputStream.toString();
        outputStream.close();
        return paramValue;
    }
}