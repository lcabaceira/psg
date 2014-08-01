package com.wewebu.ow.clientservices.utils;

/**
 *<p>
 * Utility class used to sanitize file names.
 * The implementation tries to do it's best to transform a {@link String} to a valid file name.
 * Most probably the algorithm will be OS dependent.
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
public class OwFileNameSanitizer
{
    /**
     * Factory method. In the future we could implement different version for different OSs.
     * @return An instance of {@link OwFileNameSanitizer} appropriated for this platform.
     */
    public static OwFileNameSanitizer getInstance()
    {
        return new OwFileNameSanitizer();
    }

    /**
     * Tries to transform the given string into a valid file name.
     * @param fileName_p
     * @return hopefully a valid file name as close as possible to the initial provided name.
     */
    public String sanitize(String fileName_p)
    {
        String sanitizedFileName = fileName_p.replaceAll("\"", "");
        return sanitizedFileName;
    }
}
