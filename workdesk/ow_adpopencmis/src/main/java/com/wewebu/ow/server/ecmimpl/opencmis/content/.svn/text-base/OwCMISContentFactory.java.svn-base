package com.wewebu.ow.server.ecmimpl.opencmis.content;

import java.io.InputStream;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientDocument;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Knows how to build {@link OwContentCollection} and {@link ContentStream} from various sources.
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
public class OwCMISContentFactory
{
    private static final Logger LOG = OwLog.getLogger(OwCMISContentFactory.class);
    private OwCMISNativeSession nativeSession;

    public OwCMISContentFactory(OwCMISNativeSession nativeSession)
    {
        this.nativeSession = nativeSession;
    }

    /**
     * @param contentCollection_p
     * @return a {@link ContentStream}
     */
    public ContentStream createContentStream(OwContentCollection contentCollection_p) throws OwException
    {
        OwContentElement elem = null;
        try
        {
            if (contentCollection_p == null || contentCollection_p.getPageCount() == 0)
            {
                return null;
            }
            elem = contentCollection_p.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);

            Session session = this.nativeSession.getOpenCMISSession();

            InputStream stream = elem.getContentStream(null);
            String mimetype = elem.getMIMEType();
            long length = -1; //unknown
            String filename = getElementName(elem);
            ContentStream contentStream = session.getObjectFactory().createContentStream(filename, length, mimetype, stream);
            return contentStream;
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (Exception e)
        {
            LOG.fatal("Invalid content collection! Could not retrieve content page count!", e);
            throw new OwInvalidOperationException(new OwString("ecmimpl.opencmis.ContentFactory.invalid.content.collection.error", "Invalid content collection! "), e);
        }
    }

    private String getElementName(OwContentElement contentElement) throws Exception
    {
        String name = null;
        String mimeParameter = contentElement.getMIMEParameter();
        name = mimeParameter;//getContentFilePath();
        if (name != null)
        {
            name = name.replace('\\', '/');
            name = name.substring(name.indexOf('=') + 1);
        }
        if (null == name)
        {
            throw new OwInvalidOperationException(new OwString1("ecmimpl.opencmis.ContentFactory.invalid.mimeparameter.error", "Invalid mime parameter: {0}!", mimeParameter));
        }
        return name;

    }

    public OwContentCollection createContentCollection(TransientDocument document)
    {
        return new OwCMISContentCollection(document);
    }
}
