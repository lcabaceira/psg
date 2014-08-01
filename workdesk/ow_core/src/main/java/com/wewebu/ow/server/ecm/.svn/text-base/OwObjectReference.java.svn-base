package com.wewebu.ow.server.ecm;

/**
 *<p>
 * Base interface for all ECM Objects. Used to serialize objects.<br/>
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
public interface OwObjectReference
{
    /** object type base definitions */
    public static final int OBJECT_TYPE_UNDEFINED = 0x0000;

    /** object type objects with content */
    public static final int OBJECT_TYPE_ALL_CONTENT_OBJECTS = 0x1000;
    /** object type Document enumerator */
    public static final int OBJECT_TYPE_DOCUMENT = 0x1001;

    /** start value for user defined object types */
    public static final int OBJECT_TYPE_CONTENT_USER_START = 0x1080;

    /** object type objects with child objects */
    public static final int OBJECT_TYPE_ALL_CONTAINER_OBJECTS = 0x2000;
    /** object type physical folder enumerator */
    public static final int OBJECT_TYPE_FOLDER = 0x2001;
    /** object type for the ECM root folder */
    public static final int OBJECT_TYPE_ECM_ROOT_FOLDER = 0x2002;
    /** object type for the BPM root folder */
    public static final int OBJECT_TYPE_BPM_ROOT_FOLDER = 0x2003;
    /** object type stored searches  */
    public static final int OBJECT_TYPE_STORED_SEARCH = 0x2004;

    /** an object which is create out of a search*/
    public static final int OBJECT_TYPE_DYNAMIC_VIRTUAL_FOLDER = 0x200E;
    /** an object described in a search node, represented as folder*/
    public static final int OBJECT_TYPE_VIRTUAL_FOLDER = 0x200F;

    /** object type roster object enumerator */
    public static final int OBJECT_TYPE_ROSTER_FOLDER = 0x2020;
    /** object type public queue object enumerator */
    public static final int OBJECT_TYPE_PUBLIC_QUEUE_FOLDER = 0x2021;
    /** object type user queue object enumerator */
    public static final int OBJECT_TYPE_USER_QUEUE_FOLDER = 0x2022;
    /** object type system queue object enumerator */
    public static final int OBJECT_TYPE_SYS_QUEUE_FOLDER = 0x2023;
    /** object type cross queue object enumerator */
    public static final int OBJECT_TYPE_CROSS_QUEUE_FOLDER = 0x2024;
    /** object type proxy queue object enumerator */
    public static final int OBJECT_TYPE_PROXY_QUEUE_FOLDER = 0x2025;
    /** object type tracker queue object enumerator */
    public static final int OBJECT_TYPE_TRACKER_QUEUE_FOLDER = 0x2026;

    /** start value for user defined object types */
    public static final int OBJECT_TYPE_CONTAINER_USER_START = 0x2080;

    /** object type objects with only metadata */
    public static final int OBJECT_TYPE_ALL_TUPLE_OBJECTS = 0x3000;
    /** object type Custom object enumerator */
    public static final int OBJECT_TYPE_CUSTOM = 0x3001;
    /** object type History object enumerator */
    public static final int OBJECT_TYPE_HISTORY = 0x3002;
    /** object type for Link/Relationship/Association objects*/
    public static final int OBJECT_TYPE_LINK = 0x3003;

    /** start value for user defined object types */
    public static final int OBJECT_TYPE_TUPLE_USER_START = 0x3080;

    /** object type objects for workflows */
    public static final int OBJECT_TYPE_ALL_WORKFLOW_OBJECTS = 0x4000;
    /** object type work item object enumerator */
    public static final int OBJECT_TYPE_WORKITEM = 0x4001;
    /** object type roster item object enumerator */
    public static final int OBJECT_TYPE_ROSTERITEM = 0x4002;
    /** object type proxy object enumerator */
    public static final int OBJECT_TYPE_WORKITEM_PROXY = 0x4003;
    /** object type tracker object enumerator */
    public static final int OBJECT_TYPE_WORKITEM_TRACKER = 0x4004;

    /** start value for user defined object types */
    public static final int OBJECT_TYPE_WORKFLOW_USER_START = 0x4080;

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public abstract String getResourceID() throws Exception;

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public abstract OwObject getInstance() throws Exception;

    /** get Object name property string
     * @return the name property string of the object
     */
    public abstract String getName();

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public abstract String getID();

    /** get Object type
     * @return the type of the object
     */
    public abstract int getType();

    /** get the ECM specific ID of the Object. 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     *  The syntax of the ID is up to the ECM Adapter,
     *  but would usually be made up like the following:
     *
     */
    public abstract String getDMSID() throws Exception;

    /** retrieve the number of pages in the objects
     * @return number of pages
     */
    public abstract int getPageCount() throws Exception;

    /** get the MIME Type of the Object
     * @return MIME Type as String
     */
    public abstract String getMIMEType() throws Exception;

    /** get the additional MIME Parameter of the Object
     * @return MIME Parameter as String
     */
    public abstract String getMIMEParameter() throws Exception;

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p OwStatusContextDefinitions
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public abstract boolean hasContent(int iContext_p) throws Exception;
}