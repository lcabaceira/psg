package com.wewebu.ow.server.ecmimpl.fncm5;

import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSIDDecoder;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * P8 5.0 basic decoder of {@link OwFNCM5SimpleDMSID}.
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
public class OwFNCM5SimpleDMSIDDecoder implements OwFNCM5DMSIDDecoder
{

    public String[] getParts(OwFNCM5DMSID dmsid)
    {
        return getParts(dmsid.getDMSID());
    }

    public String[] getParts(String objDmsid)
    {
        return objDmsid.split(OwFNCM5DMSID.DMSID_SEPARATOR);
    }

    public OwFNCM5DMSID getDmsidObject(String dmsid) throws OwInvalidOperationException
    {
        String[] parts = getParts(dmsid);

        if (parts.length != 3 || !OwFNCM5Network.DMS_PREFIX.equals(parts[0]))
        {
            throw new OwInvalidOperationException("Provided DMSID is invalid, provided DMSID = " + dmsid);
        }

        return new OwFNCM5SimpleDMSID(parts[2], parts[1]);
    }

}
