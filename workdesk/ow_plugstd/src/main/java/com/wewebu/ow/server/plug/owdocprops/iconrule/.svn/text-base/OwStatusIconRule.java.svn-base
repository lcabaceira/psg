package com.wewebu.ow.server.plug.owdocprops.iconrule;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface for rules used by StatusIconDocumentFunction. 
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
public interface OwStatusIconRule
{
    /**Does the current rule match the provided object,
     * @param obj OwObject current object to process
     * @param parentObj OwObject parent of object (can be null)
     * 
     * @return boolean true or false
     * @throws OwException 
     */
    boolean canApply(OwObject obj, OwObject parentObj) throws OwException;

    /**
     * Return an Icon representation, 
     * which could be an URI, CSS or anything else. 
     * @return String
     */
    String getIcon();

    /**
     * Return a text which should be used as readable
     * information of {@link #getIcon()} representation.
     * @return String
     */
    String getDescription();
}
