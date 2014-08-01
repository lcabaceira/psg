package com.wewebu.ow.server.collections;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * This interface denotes an object that is capable of providing navigation 
 * of its children through the {@link OwIterable} interface. 
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
 *@see OwIterable
 *@since 4.2.0.0
 */
public interface OwPageableObject<T extends OwObject> extends OwObject
{
    OwIterable<T> getChildren(OwLoadContext loadContext) throws OwException;
}
