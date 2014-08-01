package com.wewebu.ow.server.ecm.eaop;

import java.util.List;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 * An advice collection.
 *    
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 4.0.0.0
 */
public interface OwAdviser
{
    void add(Object... advices_p) throws OwInvalidOperationException;

    void remove(Object... advices_p) throws OwInvalidOperationException;

    <A> List<A> get(Class<A> advicerClass_p) throws OwInvalidOperationException;
}
