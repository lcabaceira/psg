package com.wewebu.ow.server.ecm;

import org.w3c.dom.Node;

/**
 *<p>
 * Base Class for virtual folder object factories.
 * The factory is created and cached in the ECM-System. You retrieve objects with {@link #getInstance(String strDmsIDPart_p)}<br/>
 * They are constructed using a XML root node given in the init method, which must be overridden in 
 * the implementation of your virtual folder object factory.
 * With the {@link #getInstance(String strDmsIDPart_p)} you can retrieve instances of virtual folders which can be treated as any other OwObject.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwVirtualFolderObjectFactory
{
    /** name of the root node tag */
    public static final String ROOT_NODE_TAG_NAME = "root";

    /** name of the classname node tag for the virtual folder class */
    public static final String CLASSNAME_TAG_NAME = "ClassName";

    /** init a virtual folder object
     * 
     * @param context_p OwNetworkContext
     * @param repository_p OwRepository
     * @param strBaseDMSID_p String name to identify the virtual folder by the DMSID, to be extended by instance part (see getInstance)
     * @param rootNode_p Node XML root node describing the virtual folder
     * @param virtualFolderName_p name of the virtual folder 
     */
    public abstract void init(OwNetworkContext context_p, OwRepository repository_p, String strBaseDMSID_p, String virtualFolderName_p, Node rootNode_p) throws Exception;

    /** get a folder instance from the factory with the given DMSID part. The complete DMSID is strBaseDMSID_p + strDmsIDPart_p (see init)
     * @param strDmsIDPart_p String DMSID part for the instance, or null to get a default virtual folder
     * @return  OwVirtualFolderObject
     * */
    public abstract OwVirtualFolderObject getInstance(String strDmsIDPart_p) throws Exception;
}
