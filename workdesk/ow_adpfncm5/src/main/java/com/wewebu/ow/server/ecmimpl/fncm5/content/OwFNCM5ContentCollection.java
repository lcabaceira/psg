package com.wewebu.ow.server.ecmimpl.fncm5.content;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Document;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Content collection based on P8 5.x API.
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
public class OwFNCM5ContentCollection implements OwContentCollection
{
    private Document nativeObject;
    protected static final List<Integer> ONLY_CONTENT_TYPES = new LinkedList<Integer>();
    protected static final List<Integer> ANNOT_CONTENT_TYPES = new LinkedList<Integer>();

    static
    {
        ONLY_CONTENT_TYPES.add(Integer.valueOf(OwContentCollection.CONTENT_TYPE_DOCUMENT));

        ANNOT_CONTENT_TYPES.addAll(ONLY_CONTENT_TYPES);
        ANNOT_CONTENT_TYPES.add(Integer.valueOf(OwContentCollection.CONTENT_TYPE_ANNOTATION));
    }

    public OwFNCM5ContentCollection(Document document)
    {
        this.nativeObject = document;
    }

    public OwContentElement getContentElement(int iContentType_p, int iPage_p) throws OwException
    {
        if (iContentType_p == OwContentCollection.CONTENT_TYPE_ANNOTATION)
        {
            OwFNCM5NativeObjHelper.retrieveProperties(getNativeObject(), PropertyNames.ANNOTATIONS, true);
            return OwFNCM5ContentHelper.createAnnotationElement(getNativeObject(), iPage_p - 1);
        }

        if (getContentTypes().contains(Integer.valueOf(iContentType_p)))
        {
            OwContentElement elem = null;
            if (iContentType_p == OwContentCollection.CONTENT_TYPE_DOCUMENT)
            {
                OwFNCM5NativeObjHelper.retrieveProperties(getNativeObject(), PropertyNames.CONTENT_ELEMENTS, true);
                elem = OwFNCM5ContentHelper.getContentElement(getNativeObject(), iPage_p - 1);
            }
            if (elem == null)
            {
                throw new OwServerException("Cannot retrieve content element with type " + iContentType_p + " page = " + iPage_p);
            }

            return elem;
        }
        else
        {
            throw new OwInvalidOperationException("Invalid content type requested (" + iContentType_p + ", page = " + iPage_p + ")");
        }
    }

    public int getPageCount() throws OwException
    {
        OwFNCM5NativeObjHelper.ensure(getNativeObject(), PropertyNames.CONTENT_ELEMENTS);
        return getNativeObject().get_ContentElements().size();
    }

    @SuppressWarnings("rawtypes")
    public Collection getContentTypes() throws OwException
    {
        OwFNCM5NativeObjHelper.ensure(getNativeObject(), PropertyNames.ANNOTATIONS);
        if (getNativeObject().get_Annotations().isEmpty())
        {
            return ONLY_CONTENT_TYPES;
        }
        else
        {
            return ANNOT_CONTENT_TYPES;
        }
    }

    /**
     * Return the Document this Collection is based on.
     * @return Document
     */
    protected Document getNativeObject()
    {
        return nativeObject;
    }

}
