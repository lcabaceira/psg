package com.wewebu.ow.server.util.paramcodec;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwUserOperationException;

/**
 *<p>
 * Encodes and decodes {@link OwParameterMap}s.<br/>
 * The result of the encoding and decoding of the parameter maps are also {@link OwParameterMap}s.<br/>
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
 *@since 3.0.0.0
 */
public interface OwParameterMapCodec
{
    /**
     * Encodes the given {@link OwParameterMap}
     * @param parameterMap_p
     * @return the encoded {@link OwParameterMap}
     * @throws OwException if the encoding process fails
     */
    OwParameterMap encode(OwParameterMap parameterMap_p) throws OwException;

    /**
     * Decodes the given {@link OwParameterMap}
     * @param parameterMap_p
     * @param preserveEncoding_p if <code>true</code> the decoded {@link OwParameterMap} will also contain the encoded parameters
     * @return the decoded {@link OwParameterMap}
     * @throws OwException if the decoding process fails
     * @throws OwUserOperationException if the decoding process fails but recovery is possible (non-system-errors)<br>
     *                                  egg. expired cookies in cookie based encoding scheme 
     */
    OwParameterMap decode(OwParameterMap parameterMap_p, boolean preserveEncoding_p) throws OwException, OwUserOperationException;

    /**
     * Verifies that this codec can decode the given {@link OwParameterMap} without system-errors. 
     * @param parameterMap_p
     * @return <code>true</code> if the given {@link OwParameterMap} can be decoded or <code>false</code> otherwise 
     * @throws OwException
     */
    boolean canDecode(OwParameterMap parameterMap_p) throws OwException;

    /**
     * Get the next available unique  index. 
     * @return a unique long index 
     * @throws OwException
     */
    long getNextUnqiueNameIndex() throws OwException;

}
