package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwObjectIDCodeUtil;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Standard CMIS DMSID encoder/decoder implementation.
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
public class OwCMISSimpleDMSIDDecoder implements OwCMISDMSIDDecoder
{
    private static final Logger LOG = OwLog.getLogger(OwCMISSimpleDMSIDDecoder.class);

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "ocmis";

    public static final String COMPATIBILITY_PREFIX = "cmis";

    public String getDMSIDPrefix()
    {
        return DMS_PREFIX;
    }

    public OwCMISDMSID createDMSID(String dmsid_p) throws OwException
    {
        String[] split = dmsid_p.split(OwCMISDMSID.DMSID_SEPARATOR);

        if (split.length != 3 || (!getDMSIDPrefix().equals(split[0]) && !COMPATIBILITY_PREFIX.equals(split[0])))
        {
            LOG.error("OwCMISSimpleDMSIDDecoder.createDMSID(): Invalid DMSID = " + dmsid_p);
            throw new OwInvalidOperationException(new OwString1("opencmis.OwCMISSimpleDMSIDDecoder.invalid.dmsid.error", "Invalid DMSID %1 !", dmsid_p));
        }

        return new OwCMISSimpleDMSID(getDMSIDPrefix(), split[1], OwObjectIDCodeUtil.decode(split[2]));
    }

}
