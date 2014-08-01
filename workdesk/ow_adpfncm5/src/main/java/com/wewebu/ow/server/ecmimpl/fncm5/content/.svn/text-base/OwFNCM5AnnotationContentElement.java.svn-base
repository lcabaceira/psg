package com.wewebu.ow.server.ecmimpl.fncm5.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Annotation;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5NativeObjHelper;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.viewer.OwFNCM5Annotation;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwStreamUtil;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Implementation for Annotations.
 * Attention this class will create of all 
 * available Annotations (AnnotationSet)
 * as one XML stream representation.
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
public class OwFNCM5AnnotationContentElement implements OwContentElement
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5AnnotationContentElement.class);

    private AnnotationSet contentObject;

    protected final String DOCID = "DocID";
    protected final String FNDOCANNOLIST = "FnDocAnnoList";
    protected final String FNPAGEANNOLIST = "FnPageAnnoList";
    protected final String LIBNAME = "LibName";
    protected final String SYSTEM_TYPE = "SystemType";
    protected final String XMLNS_XSD = "xmlns:xsd";
    protected final String XMLNS_XSI = "xmlns:xsi";
    protected final String XSD_NAMESP = "http:www.w3.org/2001/XMLSchema";
    protected final String XSI_NAMESP = "http://www.w3.org/2001/XMLSchema-instance";
    protected final String SYS_TYPE = "0";
    protected final String PAGE = "Page";
    protected final String FIRST_PAGE = "1";

    public OwFNCM5AnnotationContentElement(AnnotationSet annot)
    {
        this.contentObject = annot;
    }

    public String getContentFilePath() throws OwException
    {
        return null;
    }

    public String getContentURL() throws OwException
    {
        return null;
    }

    public AnnotationSet getContentObject() throws OwException
    {
        return this.contentObject;
    }

    public int getContentRepresentation() throws OwException
    {
        return OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM;
    }

    public boolean isInputStreamNative()
    {
        return true;
    }

    @SuppressWarnings("rawtypes")
    public InputStream getContentStream(OutputStream out_p) throws OwException
    {
        org.w3c.dom.Document document;
        try
        {
            document = OwXMLDOMUtil.getNewDocument();
        }
        catch (ParserConfigurationException e1)
        {
            LOG.error("Cannot create parser, environmental problem", e1);
            throw new OwServerException("Cannot create Inputstream from Annotation", e1);
        }

        // build the XML "skeleton" (header)
        Node rootNode = document.getDocumentElement();
        rootNode = document.createElement(FNDOCANNOLIST);
        document.appendChild(rootNode);

        OwXMLDOMUtil.setNodeAttribute(document, rootNode, XMLNS_XSD, XSD_NAMESP);
        OwXMLDOMUtil.setNodeAttribute(document, rootNode, XMLNS_XSI, XSI_NAMESP);
        OwXMLDOMUtil.setNodeAttribute(document, rootNode, LIBNAME, "");
        OwXMLDOMUtil.setNodeAttribute(document, rootNode, DOCID, "");
        OwXMLDOMUtil.setNodeAttribute(document, rootNode, SYSTEM_TYPE, SYS_TYPE);

        rootNode.appendChild(document.createTextNode("\n"));

        // in this node we will put all the annotations
        Node annotationParentNode = document.createElement(FNPAGEANNOLIST);
        rootNode.appendChild(annotationParentNode);
        rootNode.appendChild(document.createTextNode("\n"));
        OwXMLDOMUtil.setNodeAttribute(document, annotationParentNode, PAGE, FIRST_PAGE);

        // now we append the annotations
        Iterator it = getContentObject().iterator();
        while (it.hasNext())
        {
            Annotation annotation = (Annotation) it.next();
            OwFNCM5NativeObjHelper.ensure(annotation, new String[] { PropertyNames.CONTENT_ELEMENTS, PropertyNames.MIME_TYPE });
            attachAnnotationContent(annotation, annotationParentNode);
        }

        InputStream stream;
        try
        {
            stream = OwXMLDOMUtil.getInputStream(document);
            if (out_p == null)
            {
                return stream;
            }
            else
            {
                OwStreamUtil.upload(stream, out_p, true);
                return null;
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Unknown exception during stream transfer", e);
            throw new OwServerException("Unknown problem occured during content handling", e);
        }
    }

    /**
     * Will process the Annotation structure and attach it to the provided parent node.
     * <p>If provided Annotation content is not parse-able, the element is skipped
     * and won't be attached to the parent node.</p> 
     * @param annotation com.filenet.api.core.Annotation to be attached
     * @param parentNode org.w3c.dom.Node parent node to attach to
     */
    protected void attachAnnotationContent(Annotation annotation, Node parentNode)
    {
        org.w3c.dom.Document anoDoc;
        InputStream annotStream = null;
        try
        {
            OwContentElement content = OwFNCM5ContentHelper.getContentElement(annotation);
            if (content != null)
            {
                annotStream = content.getContentStream(null);
                anoDoc = OwXMLDOMUtil.getDocumentFromInputStream(annotStream);
                String user = OwFNCM5Network.localNetwork().getCredentials().getUserInfo().getUserName();
                OwFNCM5Annotation wrap = new OwFNCM5Annotation(anoDoc, annotation.getAccessAllowed().intValue(), user, OwFNCM5Network.localNetwork().getContext());
                Node annotationNode = wrap.getRootElement();
                if (annotationNode != null)
                {
                    Node annotationChildNode = parentNode.getOwnerDocument().importNode(annotationNode.cloneNode(true), true);
                    parentNode.appendChild(annotationChildNode);
                }
            }
            else
            {
                LOG.warn("OwFNCM5AnnotationContentElement.attachAnnotationContent: Incorrect Annotation file found with no content, id = " + annotation.get_Id());
            }
        }
        catch (IOException e)
        {
            LOG.warn("Problems with stream handling Annotation content was not attached.", e);
        }
        catch (SAXException e)
        {
            LOG.warn("Problem Annotation content was not attached. Annotation-id = " + annotation.get_Id(), e);
        }
        catch (ParserConfigurationException e)
        {
            LOG.error("Enviroment problem could not create parser.", e);
        }
        catch (Exception e)
        {
            LOG.error("Unknown Problem while processing annotation content, will be ignored.", e);
        }
    }

    public int getPageNumber(int lPageIndex_p) throws OwException
    {
        return 0;
    }

    public String getMIMEType() throws OwException
    {
        return "text/xml";
    }

    public String getMIMEParameter() throws OwException
    {
        return null;
    }

    public void releaseResources()
    {

    }

}
