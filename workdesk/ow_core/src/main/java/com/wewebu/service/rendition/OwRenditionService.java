package com.wewebu.service.rendition;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Interface for Rendition capability handling.
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
public interface OwRenditionService
{
    /**
     * Retrieve the stream for specific type and object.
     * @param obj OwObject from whom a rendition is provided
     * @param type String MIME or Rendition type
     * @return InputStream of rendition representation
     * @throws IOException if could not access/retrieve stream
     * @throws OwException in other cases not matching IO problems
     */
    public InputStream getRenditionStream(OwObject obj, String type) throws IOException, OwException;

    /**
     * Return the MIME type of rendition, for specific type.
     * @param obj OwObject from which rendition are requested
     * @param type String of Rendition
     * @return List of possible MIME types
     * @throws OwException
     */
    public List<String> getRenditionMimeType(OwObject obj, String type) throws OwException;

    /**
     * Has Rendition representation for such object.
     * @param obj OwObject
     * @param type String
     * @return boolean true if renditions are available for object
     */
    public boolean hasRendition(OwObject obj, String type);

    /**
     * Trigger a generation of a Rendition for specific object.<br />
     * Before calling this method, verify that capability is available
     * through {@link #canCreateRendition(OwObject, String)}.
     * @param obj OwObject
     * @param type String
     * @throws OwException
     */
    public void createRendition(OwObject obj, String type) throws OwException;

    /**
     * Capability check for creation of rendition.
     * @param obj OwObject
     * @param type String rendition type or MIME
     * @return true if a rendition can be created for the given object 
     * @throws OwException
     */
    public boolean canCreateRendition(OwObject obj, String type) throws OwException;
}
