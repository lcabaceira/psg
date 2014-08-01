package com.wewebu.ow.server.app.impl.viid;

import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.app.id.viid.OwVIIdType;

/**
 *<p>
 * Simple implementation of the {@link OwVIId} interface. 
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
public class OwSimpleVIId implements OwVIId
{
    private String objectId;
    private String resourceId;
    private String typeString;
    private OwVIIdType type;

    public OwSimpleVIId(String objectId, String resourceId, String type)
    {
        this(objectId, resourceId, OwVIIdType.getFromPrefix(type));
        if (getType() == null)
        {
            this.typeString = type;
        }
    }

    public OwSimpleVIId(String objectId, String resourceId, OwVIIdType type)
    {
        this.objectId = objectId;
        this.resourceId = resourceId;
        this.type = type;
    }

    @Override
    public String getObjectId()
    {
        return objectId;
    }

    @Override
    public OwVIIdType getType()
    {
        return type;
    }

    @Override
    public String getTypeAsString()
    {
        if (getType() != null)
        {
            return getType().getPrefix();
        }
        else
        {
            return typeString;
        }
    }

    @Override
    public String getResourceId()
    {
        return resourceId;
    }

    @Override
    public String getViidAsString()
    {
        return getViidString(getResourceId(), getObjectId(), getTypeAsString());
    }

    public static String getViidString(String resourceId, String objectId, String type)
    {
        StringBuilder vidmsid = new StringBuilder(OwVIId.VIID_PREFIX);
        vidmsid.append(OwVIId.SEPARATOR);
        vidmsid.append(type);
        vidmsid.append(OwVIId.SEPARATOR);
        vidmsid.append(objectId);
        vidmsid.append(OwVIId.SEPARATOR);
        vidmsid.append(resourceId);
        return vidmsid.toString();
    }

    public static String getUoidString(String resourceId, String objectId, OwVIIdType type)
    {
        return getViidString(resourceId, objectId, type.getPrefix());
    }
}
