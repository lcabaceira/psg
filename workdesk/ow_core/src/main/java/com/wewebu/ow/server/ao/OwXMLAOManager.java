package com.wewebu.ow.server.ao;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Manager for virtual folder XML application objects.<br/><br/>
 * Returns {@link Document} objects.<br/>
 * Retrieves single objects and collection of objects.<br/>
 * Does not support parameterized retrieval through {@link #getApplicationObject(String, Object, boolean, boolean)}.
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
 *@since 3.2.0.0
 */
public class OwXMLAOManager extends OwSupportBasedManager
{
    private static final Logger LOG = OwLogCore.getLogger(OwDefaultAOManager.class);

    public static final String DEFAULT_BASE_PATH = "other";

    public OwXMLAOManager()
    {

    }

    /**
     * Constructor - {@link #DEFAULT_BASE_PATH} will be used as base path 
     * @param aoSupport_p application {@link OwObject} persistence support
     * 
     */
    public OwXMLAOManager(OwAOSupport aoSupport_p)
    {
        this(aoSupport_p, DEFAULT_BASE_PATH);
    }

    /**
     * Constructor - an empty string base path will be used 
     * @param aoSupport_p application {@link OwObject} persistence support
     * @param basePath_p path relative to the persistence support root of 
     *                   the managed objects' container  
     */
    public OwXMLAOManager(OwAOSupport aoSupport_p, String basePath_p)
    {
        super(aoSupport_p, basePath_p);
    }

    /**
     * 
     * @param object_p
     * @return an XML {@link Document} created from the given objects' 
     *         first content element 
     * @throws OwException
     */
    private Document toXMLDocumet(OwObject object_p) throws OwException
    {
        InputStream in = null;
        try
        {
            OwContentCollection contentCollection = object_p.getContentCollection();
            OwContentElement firstContentElement = contentCollection.getContentElement(OwContentCollection.CONTENT_TYPE_DOCUMENT, 1);
            in = firstContentElement.getContentStream(null);
        }
        catch (OwException se)
        {
            throw se;
        }
        catch (Exception e)
        {
            String msg = "Error getting XML content collection from server.";
            LOG.fatal("OwXMLAOManager.toXMLDocumet():" + msg, e);
            throw new OwServerException(new OwString("ecmimpl.OwXMLAOManager.xml.stream.error", "Error getting XML content collection from server!"), e);
        }
        Document document = null;
        try
        {
            document = OwXMLDOMUtil.getDocumentFromInputStream(in);
        }
        catch (Exception e)
        {
            String msg = "XML file cannot be read, probably due to a syntax error!";
            LOG.fatal("OwXMLAOManager.toXMLDocumet():" + msg, e);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwXMLAOManager.xml.document.error", "XML file cannot be read due to a syntax error."), e);
        }
        return document;
    }

    public Document getApplicationObject(String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        if (param_p != null)
        {
            LOG.error("OwXMLAOManager.getApplicationObject():Could not process non null XML object parameter - not implemented !");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.invalid.application.object.request", "Invalid application object request!"));
        }
        else
        {
            OwObject object = getAOSupportObject(strName_p, forceUserSpecificObject_p, createIfNotExist_p);
            return toXMLDocumet(object);
        }
    }

    public Document getApplicationObject(String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getApplicationObject(strName_p, null, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public List<Document> getApplicationObjects(String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {
        OwObject[] objects = getAOSupportObjects(strName_p, forceUserSpecificObject_p, false);
        List<Document> documents = new LinkedList<Document>();
        for (int i = 0; i < objects.length; i++)
        {
            if (objects[i] != null)
            {

                Document document = toXMLDocumet(objects[i]);
                documents.add(document);
            }
        }
        return documents;
    }

    @Override
    public int getManagedType()
    {
        return OwNetwork.APPLICATION_OBJECT_TYPE_XML_DOCUMENT;
    }

    //    @Override  will be introduced together when AOW managers refactoring
    public final OwAOType<?> getType()
    {
        return OwAOConstants.AO_XML_DOCUMENT;
    }

    @Override
    public String toString()
    {
        return "OwXMLAOManager->" + super.toString();
    }
}