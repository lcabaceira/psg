package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * A DMSID for work items retrieved from Alfresco.
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
public class OwAlfrescoBPMDMSID
{
    public static final char DMSID_SEPARATOR_CHAR = ',';
    public static final String DMSID_PREFIX = "bpm";
    private String id;

    public static OwAlfrescoBPMDMSID fromNativeID(String nativeID) throws OwException
    {
        return new OwAlfrescoBPMDMSID(DMSID_PREFIX + DMSID_SEPARATOR_CHAR + nativeID);
    }

    public OwAlfrescoBPMDMSID(String dmsidStr) throws OwException
    {
        if (!dmsidStr.startsWith(DMSID_PREFIX + DMSID_SEPARATOR_CHAR))
        {
            throw new OwInvalidOperationException(new OwString1("adpalfrescobpm.OwAlfrescoBPMDMSID.invalid.dmsid.error", "Invalid DMSID %1.", dmsidStr));
        }

        this.id = dmsidStr.substring(DMSID_PREFIX.length() + 1);

    }

    public String getPrefix()
    {
        return DMSID_PREFIX;
    }

    public String getId()
    {
        return this.id;
    }

    String getDMSIDString()
    {
        return getPrefix() + DMSID_SEPARATOR_CHAR + getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getDMSIDString();
    }
}
