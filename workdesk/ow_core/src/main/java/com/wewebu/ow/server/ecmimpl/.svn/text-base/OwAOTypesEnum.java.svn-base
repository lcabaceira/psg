package com.wewebu.ow.server.ecmimpl;

import com.wewebu.ow.server.ecm.OwNetwork;

/**
 *<p>
 * Application object types. 
 * Replaces deprecated <b>APPLICATION_OBJECT_TYPE_XXX</b> integer definitions from {@link OwNetwork} and
 * concrete network implementations.
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
public enum OwAOTypesEnum
{
    UNKNOWN(Integer.MIN_VALUE),

    /** object type for the virtual folders  (Type: OwVirtualFolderObjectFactory)*/
    VIRTUAL_FOLDER(1),

    /** object type for the preferences, which can be user or application defined, like user settings, recent file list... (Type: OwObject) */
    PREFERENCES(2),

    /** object type for the search templates (Type: OwSearchTemplate) */
    SEARCHTEMPLATE(3),

    /** object type for the XML streams (Type: org.w3c.dom.Node) */
    XML_DOCUMENT(4),

    /** object type for the attribute bags (Type: OwAttributeBag)*/
    ATTRIBUTE_BAG(5),

    /** object type for the attribute bags (Type: OwAttributeBagIterator)*/
    ATTRIBUTE_BAG_ITERATOR(6),

    /** object type for the writable attribute bags like databases (Type: OwAttributeBagWritable)*/
    ATTRIBUTE_BAG_WRITABLE(7),

    /** object type for the enumeration collections for choicelists (Type: OwEnumCollection) */
    ENUM_COLLECTION(8),

    /** object type for the read only attribute bags like databases (Type: OwAttributeBagWritable), i.e.: the attributenames of the bag represent the users */
    INVERTED_ATTRIBUTE_BAG(9),

    /** object type representing an entry template*/
    ENTRY_TEMPLATE(10),

    /** Reserved for BPM specific handling, range = [20-30[*/
    BPM_RESERVED(20),

    SEARCHTEMPLATE_BPM(21),

    /** object type for the search templates (Type: OwSearchTemplate) */
    BPM_VIRTUAL_QUEUES(23),

    /** object type for the enumeration collections (Type: OwEnumCollection) */
    BPM_ENUM_COLLECTION(20),

    /** user defined object types start here */
    USER_START(0x1000);

    public static OwAOTypesEnum fromType(int type)
    {

        OwAOTypesEnum[] all = values();
        for (OwAOTypesEnum aoType : all)
        {
            if (aoType.type == type)
            {
                return aoType;
            }
        }

        return UNKNOWN;
    }

    public final int type;

    private <T> OwAOTypesEnum(int type)
    {
        this.type = type;
    }

}
