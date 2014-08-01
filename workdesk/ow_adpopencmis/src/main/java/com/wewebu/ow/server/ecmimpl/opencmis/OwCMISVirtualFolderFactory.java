package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ao.OwVirtualFolderFactory;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwVirtualFolderObjectFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Default virtual factory implementation for CMIS
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
public class OwCMISVirtualFolderFactory implements OwVirtualFolderFactory
{
    private static final Logger LOG = OwLog.getLogger(OwCMISVirtualFolderFactory.class);
    public static final String DEFAULT_PREFIX = "vf";

    private OwNetwork network;

    public OwCMISVirtualFolderFactory(OwNetwork network)
    {
        super();
        this.network = network;
    }

    public String getDMSPrefix()
    {
        return network.getDMSPrefix() + "," + DEFAULT_PREFIX;
    }

    public OwObject createVirtualFolder(Node xmlVirtualFolderDescriptionNode_p, String strName_p, String strDmsIDPart_p) throws OwException
    {
        // read the classname to instantiate from XML, default is OwStandardVirtualFolderObject
        OwXMLUtil description;
        try
        {
            description = new OwStandardXMLUtil(xmlVirtualFolderDescriptionNode_p);
            String strVirtualFolderClassName = description.getSafeTextValue(OwVirtualFolderObjectFactory.CLASSNAME_TAG_NAME, "com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory");
            Class<?> virtualFolderClass = Class.forName(strVirtualFolderClassName);

            OwVirtualFolderObjectFactory retObject = (OwVirtualFolderObjectFactory) virtualFolderClass.newInstance();
            Node rootNode = description.getSubNode(OwVirtualFolderObjectFactory.ROOT_NODE_TAG_NAME);
            retObject.init(network.getContext(), network, getDMSPrefix() + "," + strName_p, strName_p, rootNode);

            return retObject.getInstance(strDmsIDPart_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISVirtualFolderFactory.createVirtualFolder():Could not create virtual folder named " + strName_p + " and dmisd part " + strDmsIDPart_p);
            throw new OwInvalidOperationException(network.getContext().localize("opencmis.OwCMISVirtualFolderFactory.err.createVirtualFolder", "Could not create virtual folder!"), e);
        }
    }

}
