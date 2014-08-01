package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Enumeration of direction/relation types for an Link.
 * <ul>
 * <li><b>NONE</b>: Not part of Link/Relation/Association</li>
 * <li><b>INBOUND</b>: Object is the target of the Link/Relation/Association</li>
 * <li><b>OUTBOUND</b>: Object is the source of the Link/Relation/Association</li>
 * <li><b>BOTH</b>: May appear in rare cases, mostly used for retrieval</li>
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
 *@since 4.1.1.0
 */
public enum OwObjectLinkRelation
{
    /**Not part of link (relation)*/
    NONE(new OwString("links.OwObjectLinkRelation.NONE", "NONE")),
    /**object is target of link (relation)*/
    INBOUND(new OwString("links.OwObjectLinkRelation.INBOUND", "INBOUND")),
    /**object is source of link (relation)*/
    OUTBOUND(new OwString("links.OwObjectLinkRelation.OUTBOUND", "OUTBOUND")),
    /** object is both [rare cases]*/
    BOTH(new OwString("links.OwObjectLinkRelation.BOTH", "BOTH"));

    private OwString displayName;

    private OwObjectLinkRelation(OwString displayName)
    {
        this.displayName = displayName;
    }

    public OwString getDisplayName()
    {
        return displayName;
    }

    public boolean match(OwObjectLink link, OwObject object)
    {
        return link.getRelation(object) == this;
    }

    public boolean sameDirection(OwObjectLink link, OwObject object)
    {
        OwObjectLinkRelation relation = link.getRelation(object);
        return (this == NONE && relation == NONE) || (this != NONE && (relation == this || relation == BOTH));
    }
}
