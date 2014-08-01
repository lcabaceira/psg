package com.wewebu.ow.server.ui.viewer;

/**
 *<p>
 * Enum for handling of OwInforProvider implementation. 
 * Represents just the objects which can be send from viewer
 * to request information of a document.
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
 *@since 3.2.0.0
 */
public enum OwDocInfoEnum
{
    /** Has the document external resources, specific for MO:DCA(AFP, IOCA) content type*/
    HAS_EXTERNAL_RESOURCES("HasExternalResources"),
    /** Has permission/Can current user to check-in the document */
    HAS_PERM_TO_CHECKIN("PermToCheckinDoc"),
    /** Has permission/Can user check-out document*/
    HAS_PERM_TO_CHECKOUT("PermToCheckoutDoc"),
    /**Can current user permission to cancel checkout*/
    HAS_PERM_TO_CANCEL_CHECKOUT("PermToCancelCheckout"),
    /**Can current user copy content*/
    HAS_PERM_TO_COPY("PermToCopyFromDoc"),
    /**Has current user permission to add the annotations on this document */
    HAS_PERM_TO_CREATE_ANNOTATIONS("PermToCreateAnnotations"),
    /**Has current user permission to save/export the document content to local system */
    HAS_PERM_TO_EXPORT("PermToExportDoc"),
    /**Has current user permission to print the document */
    HAS_PERM_TO_PRINT("PermToPrintDoc"),
    /**Has current user permission to update the annotations linked with this document */
    HAS_PERM_TO_UPDATE_ANNOTATIONS("PermToUpdateAnnotations"),
    /**Has current user permission to view the annotations linked with this document */
    HAS_PERM_TO_VIEW_ANNOTATIONS("PermToViewAnnotations"),
    /**Has current user permission to view the document content */
    HAS_PERM_TO_VIEW("PermToViewDoc"),
    /**Has current user permission to create instance of the document */
    HAS_PERM_TO_CREATE_DOCUMENT("PermToCreateDoc"),
    /** Number of pages in a segment, this constant has no meaning when number of segment is 1 */
    NUMBER_OF_PAGES_IN_SEGMENT("NumPagesInSegment"),
    /** Number of segments in the document, default is 1 */
    NUMBER_OF_SEGMENTS("NumSegments"),
    /** Number of parents the document has, default is 0*/
    NUMBER_OF_PARENTS("NumParents"),
    /** Is checkout*/
    IS_CHECKOUT("isCheckout"),
    /** Is versionable*/
    IS_VERSIONABLE("isVersionable"),
    /** Has permission to modify content (not Annotation)*/
    HAS_PERM_TO_MOD("PermToModify");

    String name;

    private OwDocInfoEnum(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Will search for an entry with given name. 
     * @param name String matching name of entry
     * @return OwDocInfoEnum or null if not found
     */
    public static OwDocInfoEnum getEnumFromName(String name)
    {
        for (OwDocInfoEnum entry : values())
        {
            if (entry.getName().equals(name))
            {
                return entry;
            }
        }

        return null;
    }
}
