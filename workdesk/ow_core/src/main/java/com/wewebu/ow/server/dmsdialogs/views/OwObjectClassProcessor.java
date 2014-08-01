package com.wewebu.ow.server.dmsdialogs.views;

import com.wewebu.ow.server.ecm.OwLocation;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for ObjectClass handling in create process.
 * Will be called when before the skeleton object is
 * created from ObjectClass.
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
 *@since 4.1.1.0
 */
public interface OwObjectClassProcessor
{

    /**
     * Processing of object class based on configuration or type of current processor.
     * @param objCls OwObjectClass used for Skeleton creation
     * @param location OwLocation current location
     * @param ctx OwAppContext
     * @return OwObjectClass
     */
    OwObjectClass process(OwObjectClass objCls, OwLocation location, OwAppContext ctx);

    /**
     * Init method to for current processor.
     * Provided node is the node where the processor is defined,
     * and can be used to read sub nodes for specific configuration. 
     * @param configNode OwXMLUtil
     * @throws OwException
     */
    void init(OwXMLUtil configNode) throws OwException;

}
