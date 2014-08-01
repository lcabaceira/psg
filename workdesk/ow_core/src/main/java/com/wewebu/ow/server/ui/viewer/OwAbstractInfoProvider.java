package com.wewebu.ow.server.ui.viewer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * Abstract Information Provider.
 * This implementation is based on a XML request and response.
 * <p>
 *  In the handleRequest will be try to parse the request body as a
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
 *@since 3.1.0.0
 */
public abstract class OwAbstractInfoProvider implements OwInfoProvider
{
    public void handleRequest(OwInfoRequest request_p, OutputStream answer_p) throws OwException, IOException
    {
        try
        {
            Document doc = OwXMLDOMUtil.getDocumentFromInputStream(request_p.getRequestBody());
            Node root = doc.getFirstChild();
            NodeList children = root.getChildNodes();
            StringBuilder answer = new StringBuilder("<?xml version=\"1.0\"?>");
            answer.append("<").append(root.getNodeName()).append(">");
            for (int i = 0; i < children.getLength(); i++)
            {
                Node item = children.item(i);
                String tagName = item.getNodeName();
                if (item.getFirstChild().getNodeType() == Node.TEXT_NODE)
                {
                    tagName = item.getFirstChild().getNodeValue();
                }
                String ret = process(item);
                if (ret != null)
                {
                    answer.append("<").append(tagName).append(">");
                    answer.append(ret);
                    answer.append("</").append(tagName).append(">");
                }
                else
                {
                    answer.append("<").append(tagName).append(" />");
                }
            }
            answer.append("</").append(root.getNodeName()).append(">");
            sendAnswer(answer.toString(), answer_p);
        }
        catch (SAXException e)
        {
            throw new OwInvalidOperationException("Invalid XML structure", e);
        }
        catch (ParserConfigurationException e)
        {
            throw new OwServerException("Could not load or instantiate parser from JVM configuration!", e);
        }
    }

    /**
     * Method transform the given answer into a byte array (UTF-8) 
     * and sending through given OutputStream back.
     * @param answer_p String answer to send
     * @param sendStream_p OutputStream used for answering
     * @throws UnsupportedEncodingException if conversion of String to UTF-8 byte array fails
     * @throws IOException if sending failed using the given OutputStream
     */
    protected void sendAnswer(String answer_p, OutputStream sendStream_p) throws UnsupportedEncodingException, IOException
    {
        sendStream_p.write(answer_p.getBytes("UTF-8"));
    }

    /**
     * Process XML node (item) and return the
     * String representation for the node to set.
     * <p>
     * A node could be:
     * <code>&lt;prop&gt;NumOfSegments&lt;/prop&gt;</code>
     * <p>Attention returning null will create an empty node
     * as response. (in this example <code>&lt;NumOfSegments /&gt;</code>)
     * </p>
     * @param item_p Node root of single item request
     * @return null or String representing the value
     */
    protected abstract String process(Node item_p);
}