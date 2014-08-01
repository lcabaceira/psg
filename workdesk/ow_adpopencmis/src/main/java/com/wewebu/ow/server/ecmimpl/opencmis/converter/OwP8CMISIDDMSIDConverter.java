package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;

/**
 *<p>
 * OwCMISIdDMSIDConverter extension for P8 CMIS ID formats.
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
public class OwP8CMISIDDMSIDConverter extends OwCMISIDDMSIDConverter
{
    private static final Logger LOG = OwLog.getLogger(OwP8CMISIDDMSIDConverter.class);

    private static final String ID_PARTS_SEPARATOR = ":";

    public OwP8CMISIDDMSIDConverter(OwCMISDMSIDDecoder dmsidDecoder_p, String defaultResourceID_p)
    {
        super(dmsidDecoder_p, defaultResourceID_p);
    }

    @Override
    protected OwCMISDMSID fromID(String id_p)
    {
        if (id_p.contains(ID_PARTS_SEPARATOR))
        {
            String[] idParts = id_p.split(ID_PARTS_SEPARATOR);
            if (idParts.length == 3)
            {
                try
                {
                    //TODO : P8 reports SymbolicNames as repository IDs but in reference properties the ID is used to determine repositories
                    //       repository decoding is postponed until SymbolicName to ID  mapping is done or repository ID is correctly reported   
                    //                    idParts[0]="{"+idParts[0].substring(4)+"}";
                    idParts[2] = idParts[2].substring(4);

                    //TODO: see the above TODO
                    //                    return new OwCMISSimpleDMSID(this.dmsidDecoder.getDMSIDPrefix(), idParts[0], idParts[2]);

                    return new OwCMISSimpleDMSID(this.dmsidDecoder.getDMSIDPrefix(), this.resourceID, idParts[2]);
                }
                catch (Exception e)
                {
                    LOG.error("Invalid resource ID.", e);
                }

            }
        }

        // by default 
        return super.fromID(id_p);

    }
}
