/**
 * 
 */
package com.wewebu.ow.server.app.impl.viid;

import com.wewebu.ow.server.app.id.OwIdDecoder;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Decoder for simple VIId representations.
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
public class OwSimpleVIIdDecoder implements OwIdDecoder<OwVIId>
{

    @Override
    public OwVIId decode(String idRepresentation) throws OwException
    {
        if (idRepresentation.startsWith(OwVIId.VIID_PREFIX))
        {
            String[] splitVIId = idRepresentation.split(OwVIId.SEPARATOR);
            if (splitVIId != null && splitVIId.length == 4)
            {
                return new OwSimpleVIId(splitVIId[2], splitVIId[3], splitVIId[1]);
            }
        }

        throw new OwInvalidOperationException(new OwString1("viid.OwSimpleVIIdDecoder.decode.invalid.prefix", "Invalid serialized Id %1", idRepresentation));
    }

}
