package com.wewebu.ow.server.app.id.viid;

/**
 *<p>
 * Interface for <b>V</b>ersion <b>I</b>ndependent <b>Id</b> representation.
 * An unique object Id is an independent representation of an object from it's current state or version,
 * which allow identifying/referencing the object.
 * Based on that Id, the newest/latest state of object can be resolved/queried.
 *</p>
 *<p>
 * Syntax of a generated VIId is:
 * <p>
 * <code>viid,{objectType},{object/versionseries Id},{resource Id}</code>
 * </p>
 * where objectType (= OwVIIdType) is encode like:
 * <ul>
 * <li>Folder = f</li>
 * <li>Document = d</li>
 * <li>Custom = c</li>
 * <li>WorkItem = w</li>
 * <li>Link = l</li>
 * </ul>
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
 *@see OwVIIdType
 */
public interface OwVIId
{
    public static final String VIID_PREFIX = "viid";

    public static final String SEPARATOR = ",";

    /**
     * Get the ObjectId. Which can be anything:
     * <ul>
     * <li>VersionSeries-Id</li>
     * <li>Folder-Id</li>
     * <li>WorkItem-Id</li>
     * <li>Custom Object-Id</li>
     * </ul> and so on.
     * @return String Id of the object
     */
    String getObjectId();

    /**
     * An enumeration representation of the object type.
     * Can be null if enumeration doesn't contain corresponding representation,
     * in such case only String representation is available {@link #getTypeAsString()}.
     * @return UniqueObjectIdType or null
     * @see #getTypeAsString()
     */
    OwVIIdType getType();

    /**
     * A string representation of the object type,
     * by default the prefix of the OwVIIdType.
     * If {@link #getType()} returns null, this value will be handled as representation of object type.
     * @return String
     */
    String getTypeAsString();

    /**
     * ResourceId of the object. The resource can define object store (a.k.a. repository),
     * which is used to resolve the object.
     * @return String
     */
    String getResourceId();

    /**
     * Get a string which represents this object id instance.
     * Resulting string can be used in getObjectFromDMSID calls.
     * @return String
     */
    String getViidAsString();
}
