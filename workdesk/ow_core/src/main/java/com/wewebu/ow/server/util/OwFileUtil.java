package com.wewebu.ow.server.util;

/**
 *<p>
 * Utility class for file access and manipulation.
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
public class OwFileUtil
{
    /** replace extension of the given filepath
     * 
     *  e.g.: c:\temp\myfile.txt,properties
     *  
     *  => c:\temp\myfile.properties
     * 
     * @param filepath_p
     * @return String title
     */
    public static String replaceExtension(String filepath_p, String newextension_p)
    {
        int i = filepath_p.lastIndexOf('.');
        if (-1 != i)
        {
            filepath_p = filepath_p.substring(0, i);
        }

        filepath_p += newextension_p;

        return filepath_p;
    }

    /** get the file title without extension of the given path
     * 
     *  e.g.: c:\temp\myfile.txt
     *  
     *  => myfile
     * 
     * @param filepath_p
     * @return String title
     */
    public static String getFileTitle(String filepath_p)
    {
        filepath_p = getFileName(filepath_p);

        int i = filepath_p.lastIndexOf('.');
        if (-1 != i)
        {
            filepath_p = filepath_p.substring(0, i);
        }

        return filepath_p;
    }

    /** get the file title with extension of the given path
     * 
     *  e.g.: c:\temp\myfile.txt
     *  
     *  => myfile.txt
     * 
     * @param filepath_p
     * @return String name
     */
    public static String getFileName(String filepath_p)
    {
        int i = filepath_p.lastIndexOf('/');
        if (-1 == i)
        {
            i = filepath_p.lastIndexOf('\\');
        }

        if (-1 != i)
        {
            filepath_p = filepath_p.substring(i + 1, filepath_p.length());
        }

        return filepath_p;
    }

    /** get the file extension of the given path
     * 
     *  e.g.: c:\temp\myfile.txt
     *  
     *  => myfile.txt
     * 
     * @param filepath_p
     * @return String extension of "" if file has no extension
     */
    public static String getFileExt(String filepath_p)
    {
        int i = filepath_p.lastIndexOf('.');
        if (-1 != i)
        {
            return filepath_p.substring(i, filepath_p.length());
        }
        else
        {
            return "";
        }
    }

    /** get the file directory without name of the given path
     * 
     *  e.g.: c:\temp\myfile.txt
     *  
     *  => c:\temp\
     * 
     * @param filepath_p
     * @return String directory with ending backslash
     */
    public static String getFileDir(String filepath_p)
    {
        int i = filepath_p.lastIndexOf('/');
        if (-1 == i)
        {
            i = filepath_p.lastIndexOf('\\');
        }

        if (-1 != i)
        {
            filepath_p = filepath_p.substring(0, i + 1);
        }

        return filepath_p;
    }
}