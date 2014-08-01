package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.ecmimpl.OwAOTypesEnum;

/**
 *<p>
 * Simple implementation of OwAOType.
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
 *@since 4.0.0.0
 */
public class OwAOTypeImpl<T> implements OwAOType<T>
{
    private Class<T> classType;
    private OwAOTypesEnum type;

    public OwAOTypeImpl(OwAOTypesEnum type, Class<T> classType)
    {
        this.type = type;
        this.classType = classType;
    }

    public int getType()
    {
        return type.type;
    }

    public Class<T> getClassType()
    {
        return classType;
    }

}
