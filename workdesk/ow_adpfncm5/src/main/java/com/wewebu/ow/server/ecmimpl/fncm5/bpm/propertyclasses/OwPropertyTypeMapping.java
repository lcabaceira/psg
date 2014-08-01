package com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses;

import java.util.TreeMap;

import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5ParticipantProperty;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

import filenet.vw.api.VWFieldType;

/**
 *<p>
 * FileNet PE Property mapping.
 * Helper class support for property type mapping,
 * and also a representation of the java class.
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
public class OwPropertyTypeMapping
{

    /**Type integer representation, for support of VWFieldType.FIELD_TYPE_INT*/
    public static final OwPropertyTypeMapping INT = new OwPropertyTypeMapping(1, "java.lang.Integer");
    /**Type string representation, for support of VWFieldType.FIELD_TYPE_STRING*/
    public static final OwPropertyTypeMapping STRING = new OwPropertyTypeMapping(2, "java.lang.String");
    /**Type boolean representation, for support of VWFieldType.FIELD_TYPE_BOOLEAN*/
    public static final OwPropertyTypeMapping BOOLEAN = new OwPropertyTypeMapping(4, "java.lang.Boolean");
    /**Type float (Double) representation, for support of VWFieldType.FIELD_TYPE_FLOAT*/
    public static final OwPropertyTypeMapping FLOAT = new OwPropertyTypeMapping(8, "java.lang.Double");
    /**Type time (Date) representation, for support of VWFieldType.FIELD_TYPE_TIME*/
    public static final OwPropertyTypeMapping TIME = new OwPropertyTypeMapping(16, "java.util.Date");
    /**Type attachment representation, for support of VWFieldType.FIELD_TYPE_ATTACHMENT*/
    public static final OwPropertyTypeMapping ATTACHMENT = new OwPropertyTypeMapping(32, "com.wewebu.ow.server.ecm.OwObject");
    /**Type participant representation, for support of VWFieldType.FIELD_TYPE_PARTICIPANT*/
    public static final OwPropertyTypeMapping PARTICIPANT = new OwPropertyTypeMapping(VWFieldType.FIELD_TYPE_PARTICIPANT, OwFNBPM5ParticipantProperty.class.getName());
    /**Type XML (String) representation, for support of VWFieldType.FIELD_TYPE_XML*/
    public static final OwPropertyTypeMapping XML = new OwPropertyTypeMapping(128, "java.lang.String");
    /**Type exist since p8 4.x, for support of VWFieldType.FIELD_TYPE_TIME_64*/
    public static final OwPropertyTypeMapping TIME64 = new OwPropertyTypeMapping(256, "java.util.Date");

    private static final TreeMap<Integer, OwPropertyTypeMapping> ENUM_COLLECTION = new TreeMap<Integer, OwPropertyTypeMapping>();

    static
    {
        ENUM_COLLECTION.put(INT.getType(), INT);
        ENUM_COLLECTION.put(STRING.getType(), STRING);
        ENUM_COLLECTION.put(BOOLEAN.getType(), BOOLEAN);
        ENUM_COLLECTION.put(FLOAT.getType(), FLOAT);
        ENUM_COLLECTION.put(TIME.getType(), TIME);
        ENUM_COLLECTION.put(ATTACHMENT.getType(), ATTACHMENT);
        ENUM_COLLECTION.put(PARTICIPANT.getType(), PARTICIPANT);
        ENUM_COLLECTION.put(XML.getType(), XML);
        ENUM_COLLECTION.put(TIME64.getType(), TIME64);
    }

    private String javaClassName;
    private Integer type;

    public OwPropertyTypeMapping(int vwPropertyType_p, String javaClassName_p)
    {
        javaClassName = javaClassName_p;
        type = Integer.valueOf(vwPropertyType_p);
    }

    /**
     * Get the java class name for the current
     * mapping.
     * @return String representing the java class name.
     */
    public String getJavaClassName()
    {
        return this.javaClassName;
    }

    /**
     * Return the type as Integer instance.
     * @return Integer
     */
    public Integer getType()
    {
        return this.type;
    }

    /**
     * Return the java class name for the given VWFieldType definition.
     * Can throw an exception if the given type is unknown or unsupported. 
     * @param iVWFieldType_p
     * @return String representing the 
     * @throws OwInvalidOperationException
     */
    public static String getJavaClassName(int iVWFieldType_p) throws OwInvalidOperationException
    {
        OwPropertyTypeMapping type = ENUM_COLLECTION.get(Integer.valueOf(iVWFieldType_p));

        if (type == null)
        {
            throw new OwInvalidOperationException("OwEnumPropertyType.getJavaClassName: Invalid or unsupported field type = " + String.valueOf(iVWFieldType_p));
        }
        return type.getJavaClassName();
    }

    /**
     * Return the representation type form an internal collection.
     * If the type is unknown or unsupported this method can return null. 
     * @param iVWFieldType_p VWFieldType for which to retrieve a mapping
     * @return OwEnumPropertyType or null
     */
    public static OwPropertyTypeMapping getRepresentationType(int iVWFieldType_p)
    {
        return ENUM_COLLECTION.get(Integer.valueOf(iVWFieldType_p));
    }

    /**
     * Helper to analyze if the given mapping type is an attachment type.
     * @param type_p OwProeprtyTypemapping to check
     * @return boolean true only if {@link #ATTACHMENT}.getType() is equals given type
     */
    public static boolean isAttachment(OwPropertyTypeMapping type_p)
    {
        return ATTACHMENT.getType().equals(type_p.getType());
    }

    /**
     * Helper to analyze if the given mapping type is an attachment type.
     * @param type_p int to check
     * @return boolean true only if {@link #ATTACHMENT}.getType().intValue() is equals given type
     */
    public static boolean isAttachment(int type_p)
    {
        return ATTACHMENT.getType().intValue() == type_p;
    }

    /**
     * Helper to analyze if the given mapping type is from type participant.
     * @param type_p OwProeprtyTypemapping to check
     * @return boolean true only if {@link #PARTICIPANT}.getType() is equals given type
     */
    public static boolean isParticipant(OwPropertyTypeMapping type_p)
    {
        return PARTICIPANT.getType().equals(type_p.getType());
    }

    /**
     * Helper to analyze if the given mapping type is an attachment type.
     * @param type_p int to check
     * @return boolean true only if {@link #PARTICIPANT}.getType().intValue() is equals given type
     */
    public static boolean isParticipant(int type_p)
    {
        return PARTICIPANT.getType().intValue() == type_p;
    }
}