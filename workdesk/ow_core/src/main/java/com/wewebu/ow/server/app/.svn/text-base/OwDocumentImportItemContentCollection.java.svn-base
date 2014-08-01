package com.wewebu.ow.server.app;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Standard <code>{@link OwContentCollection}</code> implementation that is wrapped around
 * an <code>{@link OwDocumentImportItem}</code>.<br>
 * Can and should be used to create an <code>{@link OwContentCollection}</code> for new
 * object creation, save or checkin from an <code>{@link OwDocumentImportItem}</code>.
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
 *@see OwDocumentImportItemContentElement
 */
public class OwDocumentImportItemContentCollection implements OwContentCollection
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwDocumentImportItemContentCollection.class);

    /** the document imported by the document importer */
    protected OwDocumentImportItem m_importedDocument;

    /**
     * Create a new <code>OwDocumentImportItemContentCollection</code> from the given <code>OwDocumentImportItem</code>.
     * 
     * @param importedDocument_p the imported document this content collection is based on
     */
    public OwDocumentImportItemContentCollection(OwDocumentImportItem importedDocument_p)
    {
        m_importedDocument = importedDocument_p;
    }

    /**
     * Returns the content element for the given type and page
     * 
     * @param iContentType_p the content type to receive the content element for
     * @param iPage_p the page to receive the content element for
     * 
     * @return the content element for the given type and page
     * 
     * @throws Exception
     */
    public OwContentElement getContentElement(int iContentType_p, int iPage_p) throws Exception
    {
        // do we support the content type? - OBJECT_TYPE_DOCUMENT
        if (iContentType_p != OwContentCollection.CONTENT_TYPE_DOCUMENT)
        {
            String msg = "OwDocumentImportItemContentCollection.getContentElement: Content Type is not from type document, type = " + iContentType_p + ", page = " + iPage_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return createDocumentImportItemContentElement(iContentType_p, iPage_p);
    }

    /**
     * Factory method tor createion of <code>{@link OwDocumentImportItemContentElement}</code> objects
     * 
     * @return created <code>{@link OwDocumentImportItemContentElement}</code> objects
     * 
     * @throws Exception 
     */
    protected OwDocumentImportItemContentElement createDocumentImportItemContentElement(int contentType_p, int page_p) throws Exception
    {
        return new OwDocumentImportItemContentElement(contentType_p, page_p, m_importedDocument);
    }

    public Collection getContentTypes() throws Exception
    {
        List contentTypes = new LinkedList();
        contentTypes.add(Integer.valueOf(OwContentCollection.CONTENT_TYPE_DOCUMENT));
        return contentTypes;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwContentCollection#getPageCount()
     */
    public int getPageCount() throws Exception
    {
        return m_importedDocument.getContentStreamCount();
    }

}