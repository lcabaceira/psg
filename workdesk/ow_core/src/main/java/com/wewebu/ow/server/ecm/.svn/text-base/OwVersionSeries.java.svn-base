package com.wewebu.ow.server.ecm;

import java.util.Collection;

import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Base interface for Versions Series. 
 * A Version Series is attached to a versionable object and switches between versions.<br/>
 * <b>NOTE:</b> a document is always working on a specific version. 
 * With the VersionSeries a document can select a different version.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwVersionSeries
{
    /** get a OwObject for a given OwVersion
     * 
     * @param version_p OwVersion object identifying the OwObject
     *
     * @return OwObject
     */
    public abstract OwObject getObject(OwVersion version_p) throws Exception;

    /** get the available version objects 
     *
     * @param properties_p Collection of property names to retrieve for cached fast access
     * @param sort_p OwSort
     * @param iMaxSize_p maximum number of items to retrieve
     *
     * @return  Collection of OwVersion objects, use getObject to obtain a OwObject
     */
    public abstract Collection getVersions(Collection properties_p, OwSort sort_p, int iMaxSize_p) throws Exception;

    /** retrieve the latest version in the series
     *
     * @return OwVersion, use getObject to obtain a OwObject
     */
    public abstract OwVersion getLatest() throws Exception;

    /** retrieve the released version in the series
     *
     * @return OwVersion, use getObject to obtain a OwObject
     */
    public abstract OwVersion getReleased() throws Exception;

    /** retrieve the reservation (intermediate after checkout) version in the series
     *
     * @return OwVersion or null if not checked out of ECM system does not support reservations, use getObject to obtain a OwObject)
     */
    public abstract OwVersion getReservation() throws Exception;

    /**
     * Return a String which represent the version series Id.
     * <p>Method is available since 3.2.0.0</p>
     * @return String version series id of this version series
     * @since 3.2.0.0
     */
    public abstract String getId();
}