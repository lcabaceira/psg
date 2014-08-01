package com.wewebu.ow.server.field;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Priority rule definition.<br/>
 * Rules Engine for Highlighting in Hit List.
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
public interface OwPriorityRule
{

    /** Gets the container ID the rule applies to
     * 
     * @return String
     */
    public abstract String getContainer();

    /** Gets the style class the item should be highlighted if rule applies
     * 
     * @return String style class string e.g. myStyle
     */
    public abstract String getStylClass();

    /**
     * Checks if this rule applies to an {@link OwObject}
     * @param object_p
     * @return <code>true</code> if this rule applies to the specified object, <code>false</code> otherwise.
     * @throws OwInvalidOperationException if the rule checking process failed
     */
    public boolean appliesTo(OwObject object_p) throws OwInvalidOperationException;
}