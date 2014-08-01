package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Dynamic {@link OwFNCM5Resource} accessing interface. 
 * AWD resource sources can vary depending on the implementation (stored as object properties, 
 * cached on the HTTP sessions or application contexts).
 * In order to facilitate multiple storing strategies it is recommended to use 
 * <i>OwFNCM5ResourceAccessor</i>s as object properties instead of the actual {@link OwFNCM5Resource} objects.
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
public interface OwFNCM5ResourceAccessor<R extends OwFNCM5Resource>
{
    R get() throws OwException;
}
