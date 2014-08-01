package com.wewebu.ow.server.plug.owdocprops;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * A handler which will allow to control dynamically the views modifiability.  
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
 *@since 4.2.0.0
 */
public interface OwEditPropertiesModifiabilityHandler
{
    /**
     * Method to initialize the handler
     * @param appContext OwAppContext current application context
     * @param handlerConfigNode OwXMLUtil the node defined for this handler
     * @throws OwException
     */
    void init(OwAppContext appContext, OwXMLUtil handlerConfigNode) throws OwException;

    /**
     * Verify if current view should be modifiable for item or not.
     * @param viewMask int representation of OwEditPropertiesDialog, like: OwEditPropertiesDialog.VIEW_MASK_PROPERTIES
     * @param item OwObject which is used to retrieve data from for view
     * @return boolean true if should be modifiable
     */
    boolean isModifiable(int viewMask, OwObject item);
}
