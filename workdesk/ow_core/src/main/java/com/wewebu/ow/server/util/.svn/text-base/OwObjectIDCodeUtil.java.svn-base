package com.wewebu.ow.server.util;

/**
 *<p>
 * Utility class for ObjectID de- and encoding.<br/>
 * The ID (string) of an object can be used for path definitions, because the object
 * name must not be unique, therefore the object ID should not contain slashes.
 * This utility class will replace in the given ID all occurrence of slashes '/'
 * ([\u002f]) with a broken bar '|' ([\u007c]).
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
 *@since 3.1.0.0
 */
public class OwObjectIDCodeUtil
{
    /**Separator of the path, should not be contained in
     * objectID definition.*/
    public static final String PATH_SEPARATOR = "/";

    /**Placeholder which will be add to the ID instead of
     * the {@link #PATH_SEPARATOR}*/
    public static final String ENCODE_PLACEHOLDER = "|";

    /**char representation of the {@link #PATH_SEPARATOR}*/
    public static final char PATH_SEPARATOR_CHAR = '/';

    /**char representation of the {@link #ENCODE_PLACEHOLDER}*/
    public static final char ENCODE_PLACEHOLDER_CHAR = '|';

    /**
     * Encode the given ID, by replacing all existing slashes
     * in the objectID_p String with the {@link #ENCODE_PLACEHOLDER_CHAR}.
     * @param objectID_p String which may contain slashes
     * @return String without any occurrence of slashes
     */
    public static String encode(String objectID_p)
    {
        return objectID_p.replace(PATH_SEPARATOR_CHAR, ENCODE_PLACEHOLDER_CHAR);
    }

    /**
     * Decode the given ID, by replacing the {@link #ENCODE_PLACEHOLDER_CHAR}
     * with slashes ({@link #PATH_SEPARATOR_CHAR}).
     * @param objectID_p String which should be decoded
     * @return String where {@link #ENCODE_PLACEHOLDER_CHAR} are replaced with slashes
     */
    public static String decode(String objectID_p)
    {
        return objectID_p.replace(ENCODE_PLACEHOLDER_CHAR, PATH_SEPARATOR_CHAR);
    }

}
