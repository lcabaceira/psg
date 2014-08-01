package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Provide support for batch operations.
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
 *@since 3.1.0.0
 */
public interface OwBatchPropertiesHandler
{
    /** 
     * Set names for properties that are copied from one document to another.
     * @param batchIndexProperties_p
     */
    void setBatchIndexProperties(Collection batchIndexProperties_p);

    /** 
     * Save current properties for next object. 
     * @throws Exception 
     */
    void saveBatchIndexProperties() throws Exception;

    /** 
     * Clear stored batch index data
     * @throws OwInvalidOperationException 
     */
    void clearBatchIndex() throws OwInvalidOperationException;

    /** 
     * called by the client when the indexes should be set from the previous values as defined with setBatchProperties 
     * @throws Exception 
     */
    void onBatchIndex() throws Exception;

    /**
     * Set the current properties.
     * @param properties_p - the properties.
     */
    public void setProperties(OwPropertyCollection properties_p);

}