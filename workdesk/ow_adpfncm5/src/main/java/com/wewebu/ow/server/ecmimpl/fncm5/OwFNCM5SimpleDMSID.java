package com.wewebu.ow.server.ecmimpl.fncm5;

import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;

/**
 *<p>
 * P8 5.0 DMSID basic implementation.
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
public class OwFNCM5SimpleDMSID implements OwFNCM5DMSID
{
    private String objID;
    private String resourceID;

    public OwFNCM5SimpleDMSID(String objID, String resourceID)
    {
        this.objID = objID;
        this.resourceID = resourceID;
    }

    public String getDMSID()
    {
        return getDMSID(getObjectID(), getResourceID());
    }

    public String getObjectID()
    {
        return this.objID;
    }

    public String getResourceID()
    {
        return this.resourceID;
    }

    public static String getDMSID(String objID, String resourceID)
    {
        StringBuilder dmsId = new StringBuilder(OwFNCM5Network.DMS_PREFIX);
        dmsId.append(OwFNCM5DMSID.DMSID_SEPARATOR);
        dmsId.append(resourceID);
        dmsId.append(OwFNCM5DMSID.DMSID_SEPARATOR);
        dmsId.append(objID);

        return dmsId.toString();
    }

    @Override
    public String toString()
    {
        return getDMSID();
    }

}
