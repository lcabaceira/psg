package com.wewebu.ow.server.ecm;

/**
 *<p>
 * OwObjectLink representation of association/relationship.
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
 *@since 4.1.1.0
 */
public interface OwObjectLink extends OwObject
{
    /**Abstraction of property name for target object*/
    public static final String OW_LINK_TARGET = "OW_LINK_TARGET";

    /**Abstraction of property name for the source object*/
    public static final String OW_LINK_SOURCE = "OW_LINK_SOURCE";

    /**Virtual property name which is used to query Link relation/direction*/
    public static final String OW_LINK_RELATION = "OW_LINK_RELATION";

    /**Virtual property name which is used to query specific Link types*/
    public static final String OW_LINK_TYPE_FILTER = "OW_LINK_TYPE_FILTER";

    /**
     * Get object which is the target.
     * @return OwObjectReference (can also return OwUnresolvedReference in exception case)
     */
    OwObjectReference getTarget();

    /**
     * Get object which is the source.
     * @return OwObjectReference (can also return OwUnresolvedReference in exception case)
     */
    OwObjectReference getSource();

    /**
     * Get information about the provided OwObject, if it is part of the OwObjectLink relation.
     * @param obj OwObject
     * @return OwObjectLinkRelation
     */
    OwObjectLinkRelation getRelation(OwObject obj);
}
