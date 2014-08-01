package com.wewebu.ow.server.ecmimpl.fncm5.helper;

import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * Helper map for P8 API to OwObjectRerence.OBJECT_TYPE_xxx.
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
public enum OwToP8Type
{
    Document(OwObjectReference.OBJECT_TYPE_DOCUMENT, "Document"), Folder(OwObjectReference.OBJECT_TYPE_FOLDER, "Folder"), CustomObject(OwObjectReference.OBJECT_TYPE_CUSTOM, "CustomObject");

    private int type;
    private String p8Type;

    private OwToP8Type(int type, String p8Type)
    {
        this.type = type;
        this.p8Type = p8Type;
    }

    public int getType()
    {
        return type;
    }

    /**
     * The P8 API type which will represent
     * the OwObjectReference type.
     * @return String name of the P8 API type
     */
    public String getP8Type()
    {
        return p8Type;
    }

    /**
     * This method will return the respective enumeration entry or null.
     * No exception will be thrown if the provided type is unknown.
     * @param type int representing {@link OwObjectReference}.OBJECT_TYPE_xxx
     * @return OwToP8Type or null
     */
    public static OwToP8Type getFromType(int type)
    {
        for (OwToP8Type entry : OwToP8Type.values())
        {
            if (entry.getType() == type)
            {
                return entry;
            }
        }
        return null;
    }

}
