package com.wewebu.ow.server.command;

import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Check if the specific object is can be processed. <br/>
 * A {@link OwCommand} can be configured with different processableObjectStrategy instance, in different situations.
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
public interface OwProcessableObjectStrategy
{
    /**
     * Default strategy: all objects can be processed.
     */
    public static final OwProcessableObjectStrategy ALWAYS_PROCESSABLE_STRATEGY = new OwProcessableObjectStrategy() {

        public boolean canBeProcessed(OwObject object_p)
        {
            return true;
        }
    };

    /**
     * Check if the given object can be processed
     * @param oObject_p - the object to be verified
     * @return -<code>true</code> if the object can be processed
     * @throws Exception - thrown in case that verification ends with an exception.
     */
    boolean canBeProcessed(OwObject oObject_p) throws Exception;
}