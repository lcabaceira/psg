package com.wewebu.ow.server.app.id.viid;

import com.wewebu.ow.server.app.id.OwIdDecoder;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwInaccessibleException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;

/**
 *<p>
 * Factory which will create an UniqueObjectId from a specific Object 
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
public interface OwVIIdFactory
{
    /**
     * Creates an OwVIId for provided object.
     * The generated id is not specific to a version, but represents a reference uniquely to that object.
     * 
     * @param obj OwObject
     * @return OwVIId
     * @throws OwNotSupportedException if provided OwObject cannot be represented as OwVIId
     * @throws OwInaccessibleException runtime exception if parts of the OwObject which are needed for OwVIId construction cannot be accessed.
     */
    OwVIId createVersionIndependentId(OwObject obj) throws OwNotSupportedException, OwInaccessibleException;

    /**
     * Create an instance which is capable of resolving Strings into OwVIId instances.
     * @return OwIdDecoder for String to OwVIId transformation
     */
    OwIdDecoder<OwVIId> createViidDecoder();
}
