package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Document;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.content.OwFNCM5ContentHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * A {@link Document} state.
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
public class OwFNCM5DocumentState<D extends Document> extends OwFNCM5PersistentState<D>
{
    private static final String[] CONTENT_ELEMENTS_PROPERTIES = new String[] { PropertyNames.CONTENT_ELEMENTS };

    //    private static final Logger LOG = OwLog.getLogger(OwFNCM5DocumentState.class);

    public OwFNCM5DocumentState(D engineObject_p, OwFNCM5EngineObjectClass<?, ?> engineObjectClass_p)
    {
        super(engineObject_p, engineObjectClass_p);
    }

    @Override
    public synchronized boolean upload(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        Document document = getEngineObject();

        ContentElementList contentList = OwFNCM5ContentHelper.convertToNativeCollection(content_p, strMimeParameter_p, strMimeType_p);

        if (contentList != null && !contentList.isEmpty())
        {
            document.set_ContentElements(contentList);

            if (strMimeType_p != null && strMimeType_p.length() > 0)
            {
                document.set_MimeType(strMimeType_p);
            }

            clearCache(CONTENT_ELEMENTS_PROPERTIES);

            return true;
        }
        else
        {
            return false;
        }
    }
}
