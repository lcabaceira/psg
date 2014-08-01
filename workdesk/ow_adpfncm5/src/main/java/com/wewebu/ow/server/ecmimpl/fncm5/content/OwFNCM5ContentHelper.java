package com.wewebu.ow.server.ecmimpl.fncm5.content;

import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentReference;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwMimeTypes;

/**
 *<p>
 * Helper for conversion between native and OW-API.
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
public class OwFNCM5ContentHelper
{

    /**
     * Get the content element for a defined index.
     * Will return null if index is less than zero, or
     * greater as content collection.
     * @param document Document native representation
     * @param idxElement integer representing the index of requested element
     * @return OwContentCollection or null
     */
    public static OwContentElement getContentElement(Document document, int idxElement)
    {
        ContentElementList lst = document.get_ContentElements();
        if (idxElement < 0 || lst.size() <= idxElement)
        {
            return null;
        }
        else
        {
            return createContentElement((ContentElement) lst.get(idxElement));
        }
    }

    /**
     * Get the content element for a defined index.
     * Will return null if index is less than zero, or
     * greater as content collection.
     * @param annot Annotation native representation
     * @return OwContentCollection or null
     */
    public static OwContentElement getContentElement(Annotation annot)
    {
        ContentElementList lst = annot.get_ContentElements();
        if (lst.isEmpty())
        {
            return null;
        }
        else
        {
            return createContentElement((ContentElement) lst.get(0));
        }
    }

    /**
     * Retrieve Annotations as content element.
     * @param document Document from where to retrieve the annotation objects
     * @param idxElement integer representing the index of object to retrieve.
     * @return OwFNCM5ContentElement representing Annotation, or null if index is less than 0 (zero)
     */
    public static OwContentElement createAnnotationElement(Document document, int idxElement)
    {
        AnnotationSet set = document.get_Annotations();
        if (idxElement < 0)
        {
            return null;
        }
        //        Iterator<?> it = set.iterator();
        //        while (it.hasNext())
        //        {
        //            Annotation annot = (Annotation) it.next();
        //            if (idxElement-- == 0)
        //            {
        //                return new OwFNCM5AnnotationContentElement(annot);
        //            }
        //        }
        return new OwFNCM5AnnotationContentElement(set);
    }

    /**
     * Create wrapper corresponding to type of native object.
     * Will return null if the provided element is from unknown type.
     * @param elem ContentElement to be wrapped
     * @return OwFNCM5ContentElement, or null if type is unknown/unsupported
     */
    protected static OwFNCM5ContentElement<?> createContentElement(ContentElement elem)
    {
        if (elem instanceof ContentTransfer)
        {
            return new OwFNCM5TransferContentElement((ContentTransfer) elem);
        }
        else if (elem instanceof ContentReference)
        {
            return new OwFNCM5ReferenceContentElement((ContentReference) elem);
        }
        else
        {
            return null;
        }
    }

    /**
     * Conversion to native representation. Will create a specific native object, 
     * depending on provided content element wrapper.
     * <p>An OwInvalidOperationException if given OwContentElement type is not supported.</p>
     * @param elem OwContentElement to be converted to native representation
     * @param mimeParameter String representing the MIME parameter string, can be null
     * @param mimeType String specific MIME type which should be used for content element, can be null
     * @return ContentElement
     * @throws Exception if fails to retrieve information from provided content element, or cannot convert content element
     */
    public static ContentElement convertToNativeElement(OwContentElement elem, String mimeParameter, String mimeType) throws Exception
    {
        String type = null;
        if (mimeType == null || mimeType.length() == 0)
        {
            type = elem.getMIMEType();
        }
        else
        {
            type = mimeType;
        }

        String name = null;
        if (mimeParameter == null || mimeParameter.length() == 0)
        {
            name = OwMimeTypes.getMimeParameter(elem.getMIMEParameter(), "name");
        }
        else
        {
            name = OwMimeTypes.getMimeParameter(mimeParameter, "name");
        }

        if (elem.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM)
        {
            ContentTransfer nativElem = com.filenet.api.core.Factory.ContentTransfer.createInstance();
            nativElem.set_ContentType(type);
            if (name != null)
            {
                nativElem.set_RetrievalName(name);
            }

            nativElem.setCaptureSource(elem.getContentStream(null));
            return nativElem;

        }
        if (elem.getContentRepresentation() == OwContentCollection.CONTENT_REPRESENTATION_TYPE_URL)
        {
            ContentReference nativeElem = com.filenet.api.core.Factory.ContentReference.createInstance();
            nativeElem.set_ContentLocation(elem.getContentURL());
            nativeElem.set_ContentType(type);

            return nativeElem;
        }
        throw new OwInvalidOperationException("Given content element type is not supported! type = " + elem.getContentRepresentation());
    }

    /**
     * Will convert a OwContentCollection to a native API representation.
     * @param collection OwContentCollection to convert
     * @return ContentElementList based on provided OwContentCollection
     * @throws OwException If cannot transform content element or retrieve information from content elements
     */
    @SuppressWarnings("unchecked")
    public static ContentElementList convertToNativeCollection(OwContentCollection collection) throws OwException
    {
        ContentElementList lst = com.filenet.api.core.Factory.ContentElement.createList();
        int pageCount = -1;
        try
        {
            pageCount = collection.getPageCount();
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (Exception ex)
        {
            throw new OwServerException("Could not retrieve page count of content collection", ex);
        }
        try
        {
            for (int i = 0; i < pageCount; i++)
            {
                lst.add(convertToNativeElement(collection.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, i + 1), null, null));
            }
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (Exception ex)
        {
            throw new OwServerException("Could not convert content element into native representation", ex);
        }
        return lst;
    }

    /**
     * Will convert a OwContentCollection to a native API representation.
     * @param col_p OwContentCollection to convert
     * @param mimeParam_p String MIME parameter to be used, can be null
     * @param mimeType_p String specific MIME type to be set, can be null
     * @return ContentElementList
     * @throws OwException
     */
    @SuppressWarnings("unchecked")
    public static ContentElementList convertToNativeCollection(OwContentCollection col_p, String mimeParam_p, String mimeType_p) throws OwException
    {
        ContentElementList lst = com.filenet.api.core.Factory.ContentElement.createList();
        int pageCount = -1;
        try
        {
            if (null != col_p)
            {
                pageCount = col_p.getPageCount();
            }
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (Exception ex)
        {
            throw new OwServerException("Could not retrieve page count of content collection", ex);
        }
        try
        {
            for (int i = 0; i < pageCount; i++)
            {
                lst.add(convertToNativeElement(col_p.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, i + 1), mimeParam_p, mimeType_p));
            }
        }
        catch (OwException owEx)
        {
            throw owEx;
        }
        catch (Exception ex)
        {
            throw new OwServerException("Could not convert content element into native representation", ex);
        }
        return lst;
    }

}
