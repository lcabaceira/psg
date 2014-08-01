package com.wewebu.ow.server.ecmimpl.opencmis;

import com.wewebu.ow.server.util.OwObjectIDCodeUtil;

/**
 *<p>
 * OwCMISSimpleDMSID.
 * This class simple creates from the given strings a semicolon separated
 * DMSID representation, starting always with the DMS-prefix.
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
public class OwCMISSimpleDMSID implements OwCMISDMSID
{
    //    private static final Logger LOG = OwLog.getLogger(OwCMISSimpleDMSID.class);

    private String dmsPrefix;
    private String cmisID;
    private String resourceID;

    public OwCMISSimpleDMSID(String dmsPrefix_p, String resourceID_p, String cmisID_p)
    {
        super();
        this.dmsPrefix = dmsPrefix_p;
        this.cmisID = OwObjectIDCodeUtil.decode(cmisID_p);
        this.resourceID = resourceID_p;
    }

    public String getCMISID()
    {
        return this.cmisID;
    }

    /**
     * This method concatenate the given String using comma &quot;,&quot; as separator,
     * DMSID syntax is:
     * <p>
     * <code>DMS-prefix + &quot;,&quot; + resourceID + &quot;,&quot; + cmisObjectID</code>
     * </p>
     * @return String representing DMSID
     */
    public String getDMSIDString()
    {
        return createDMSID(this.dmsPrefix, this.resourceID, this.cmisID);
    }

    public String getResourceID()
    {
        return this.resourceID;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + cmisID.hashCode();
        result = prime * result + ((dmsPrefix == null) ? 0 : dmsPrefix.hashCode());
        result = prime * result + ((resourceID == null) ? 0 : resourceID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        OwCMISSimpleDMSID other = (OwCMISSimpleDMSID) obj;

        if (!cmisID.equals(other.cmisID))
        {
            return false;
        }

        if (resourceID == null)
        {
            if (other.resourceID != null)
            {
                return false;
            }
        }
        else if (!resourceID.equals(other.resourceID))
        {
            return false;
        }

        if (dmsPrefix == null)
        {
            if (other.dmsPrefix != null)
            {
                return false;
            }
        }
        else if (!dmsPrefix.equals(other.dmsPrefix))
        {
            return false;
        }
        return true;

    }

    /**
     * Static method which is used for creation of DMSID without an instance of OwCMISSimpleDMSID.
     * <p>Create a DMSID which looks like: <br />
     * <code>DMS-prefix + &quot;,&quot; + resourceID + &quot;,&quot; + cmisObjectID</code><br />
     * the cmisID_p will be escaped using the {@link OwObjectIDCodeUtil#encode(String)} method.
     * </p>
     * @param dmsPrefix_p String prefix to be used
     * @param resourceID_p String resource ID (object store or repository)
     * @param cmisID_p String object id
     * @return String representing a DMSID
     */
    public static String createDMSID(String dmsPrefix_p, String resourceID_p, String cmisID_p)
    {
        /*StringBuilder will save up to 10 microseconds for DMSID creation
         * compared to the OwEscapedStringTokenizer.createDelimitedString(Collection)*/
        StringBuilder builder = new StringBuilder(dmsPrefix_p);
        builder.append(OwCMISDMSID.DMSID_SEPARATOR);
        builder.append(resourceID_p);
        builder.append(OwCMISDMSID.DMSID_SEPARATOR);
        builder.append(OwObjectIDCodeUtil.encode(cmisID_p));

        return builder.toString();
    }

}