package com.wewebu.ow.server.ecmimpl.opencmis.content;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString2;

/**
 *<p>
 * Simple content collection implementation, based on 
 * <code>org.apache.chemistry.opencmis.client.api.Document</code> object.
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
public class OwCMISContentCollection implements OwContentCollection
{
    private static final Logger LOG = OwLog.getLogger(OwCMISContentCollection.class);
    private static final List<Integer> CONTENT_TYPES = new LinkedList<Integer>();
    static
    {
        CONTENT_TYPES.add(Integer.valueOf(OwContentCollection.CONTENT_TYPE_DOCUMENT));
    }

    private TransientDocument doc;

    public OwCMISContentCollection(TransientDocument document)
    {
        this.doc = document;
    }

    @Override
    public OwContentElement getContentElement(int iContentType_p, int iPage_p) throws Exception
    {
        if (iPage_p > getPageCount())
        {
            throw new OwInvalidOperationException(new OwString2("opencmis.OwCMISContentCollection.err.invalidIdx", "The provided page index %1 is larger than current page count %2", Integer.toString(iPage_p), Integer.toString(getPageCount())));
        }
        if (getContentTypes().contains(Integer.valueOf(iContentType_p)))
        {
            return new OwCMISContentElement(doc.getContentStream());
        }
        else
        {
            LOG.warn("OwCMISContentCollection.getContentElement: Request of unsupported/non-contained content type, id = " + iContentType_p);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISContentCollection.err.invalidContentType", "The requested content type is not available."));
        }
    }

    @Override
    public int getPageCount() throws Exception
    {
        if (null == (doc.getContentStream()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public Collection<Integer> getContentTypes() throws Exception
    {
        return CONTENT_TYPES;
    }

    /**
     * Reference to current native object.
     * @return Document
     */
    protected TransientDocument getNativeObject()
    {
        return this.doc;
    }
}
