package com.wewebu.ow.server.ecmimpl.opencmis;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.collections.OwCMISQueryIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstraction of a CMIS adapter interaction session (a user and resource based adapter connection). 
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
public interface OwCMISSession
{
    /**
     * Access to parameter map of session
     * @param name_p String specific parameter
     * @return Object value of the given parameter or null if no such parameter is found
     */
    <P> P getParameterValue(String name_p);

    /**
     * Get the associated Resource/Repository for this session. 
     * @return OwCMISResourceInfo
     */
    OwCMISResourceInfo getResourceInfo();

    /**
     * Retrieve object from specific DMSID, may be retrieved from cache.
     * @param objId_p String Id of object
     * @param refresh_p boolean
     * @return OwCMISObject
     * @throws OwException 
     */
    OwCMISObject getObject(String objId_p, boolean refresh_p) throws OwException;

    /**
     * Retrieve object from specified path, may be retrieved from cache.
     * @param path String The path of the object, starting from the resource's root
     * @param refresh boolean if true, the cache will be ignored
     * @return OwCMISObject
     * @throws OwException 
     */
    OwCMISObject getObjectByPath(String path, boolean refresh) throws OwException;

    OwCMISObjectClass getObjectClass(String objectClassName_p) throws OwException;

    /**
     * 
     * @param promote_p promote_p
     * @param mode_p Object
     * @param objectClassName_p String
     * @param properties_p OwPropertyCollection
     * @param permissions_p OwPermissionCollection
     * @param content_p OwContentCollection
     * @param parent_p OwCMISObject
     * @param mimeType_p String
     * @param mimeParameter_p String
     * @param keepCheckedOut_p boolean
     * @return String DMSID of the newly created object
     * @throws OwException
     */
    String createObject(boolean promote_p, Object mode_p, String objectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwCMISObject parent_p, String mimeType_p,
            String mimeParameter_p, boolean keepCheckedOut_p) throws OwException;

    /**
     * Return current Locale
     * @return Locale
     */
    Locale getLocale();

    /**
     * 
     * @return OwCMISDMSIDDecoder
     */
    OwCMISDMSIDDecoder getDMSIDDecoder();

    /**
     * Return current time zone 
     * @return TimeZone
     */
    TimeZone getTimeZone();

    OwCMISQueryIterable query(OwQueryStatement statement, OwLoadContext loadContext) throws OwException;

    OwObjectCollection query(OwQueryStatement statement, boolean searchAllVersions, boolean includeAllowableActions, IncludeRelationships includeRelationships, String renditionFilter, BigInteger maxItems, BigInteger skipCount) throws OwException;

    OwCMISObject getRootFolder() throws OwException;

    OwCMISResource getResource();

    OwCMISNetwork getNetwork();

    /**
     * @param iTypes_p
     * @param fExcludeHiddenAndNonInstantiable_p
     * @param fRootOnly_p
     * @return a list of {@link OwCMISObjectClass}es.
     * @throws OwException 
     */
    Set<OwCMISObjectClass> getObjectClasses(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException;

    OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwNetwork network) throws OwException;

    /**
     * Delegation method from Network to corresponding Session for VIId handling. 
     * @param viid OwVIId
     * @return OwCMISNetwork
     * @throws OwException
     * @since 4.2.0.0
     */
    OwCMISObject getObject(OwVIId viid) throws OwException;
}