package com.wewebu.ow.server.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility class for mimetype descriptions mappings.<br/>
 * If the client wants to add more MIME types, or overwrite the existing settings,
 * the <code>owdummy</code> configuration provide a file called e.g. <code>mimetypes.properties</code>, 
 * as an example.
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
public class OwMimeTypes
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwConfiguration.class);

    /** mimetypes singleton */
    protected static final OwFileObjectStatics m_MimeTypes = new OwFileObjectStatics();

    /**
     *<p>
     * Mimetypes singleton with mimetype file extension mappings.
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
    protected static class OwFileObjectStatics
    {
        /** maps file extensions to corresponding MIME types */
        public Map<String, String> m_ExtensionToMimeTypes;
        /** maps MIME types to corresponding file extensions */
        public Map<String, String> m_MimeTypesToExtension;

        /** create the static MIME table
         */
        private OwFileObjectStatics()
        {
            // === create MIME table of extension / MIME strings.
            m_ExtensionToMimeTypes = new HashMap<String, String>();

            // Test extension for dialog handlers
            m_ExtensionToMimeTypes.put("owc", "ow/customobject");

            m_ExtensionToMimeTypes.put("vst", "application/x-visio");
            m_ExtensionToMimeTypes.put("vsd", "application/visio");
            m_ExtensionToMimeTypes.put("vnd", "application/vnd.visio");

            m_ExtensionToMimeTypes.put("txt", "text/plain");

            m_ExtensionToMimeTypes.put("gif", "image/gif");

            m_ExtensionToMimeTypes.put("png", "image/png");

            m_ExtensionToMimeTypes.put("msg", "application/msoutlook");
            m_ExtensionToMimeTypes.put("xls", "application/msexcel");
            m_ExtensionToMimeTypes.put("doc", "application/msword");
            m_ExtensionToMimeTypes.put("ppt", "application/mspowerpoint");
            m_ExtensionToMimeTypes.put("mdb", "application/msaccess");

            m_ExtensionToMimeTypes.put("jpg", "image/jpeg");
            m_ExtensionToMimeTypes.put("jpeg", "image/jpeg");
            m_ExtensionToMimeTypes.put("jpe", "image/jpeg");

            m_ExtensionToMimeTypes.put("pdf", "application/pdf");

            m_ExtensionToMimeTypes.put("tif", "image/tif");
            m_ExtensionToMimeTypes.put("tiff", "image/tiff");

            m_ExtensionToMimeTypes.put("afp", "application/afp");

            m_ExtensionToMimeTypes.put("htm", "text/html");
            m_ExtensionToMimeTypes.put("html", "text/html");
            m_ExtensionToMimeTypes.put("shtml", "text/html");

            m_ExtensionToMimeTypes.put("rtf", "text/rtf");

            m_ExtensionToMimeTypes.put("zip", "application/zip");
            m_ExtensionToMimeTypes.put("css", "text/css");
            m_ExtensionToMimeTypes.put("rtx", "text/richtext");
            m_ExtensionToMimeTypes.put("js", "text/javascript");
            m_ExtensionToMimeTypes.put("pnm", "image/x-portable-anymap");
            m_ExtensionToMimeTypes.put("pbm", "image/x-portable-bitmap");
            m_ExtensionToMimeTypes.put("pgm", "image/x-portable-graymap");
            m_ExtensionToMimeTypes.put("ppm", "image/x-portable-pixmap");
            m_ExtensionToMimeTypes.put("bmp", "image/bmp");

            m_ExtensionToMimeTypes.put("url", "text/url");

            m_ExtensionToMimeTypes.put("swf", "application/x-shockwave-flash");

            m_ExtensionToMimeTypes.put("xml", "text/xml");

            //New Office Mime Types (OOXML) from Office 2007 on
            m_ExtensionToMimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            m_ExtensionToMimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            m_ExtensionToMimeTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

            createMime2TypeExtension();
        }

        /**
         * Creates the MIME 2 type  map.
         */
        private void createMime2TypeExtension()
        {
            // === create m_MimeTypesToExtension as well
            m_MimeTypesToExtension = new HashMap<String, String>();

            for (Entry<String, String> entry : m_ExtensionToMimeTypes.entrySet())
            {
                m_MimeTypesToExtension.put(entry.getValue(), entry.getKey());
            }
        }

        /**
         * Load the MIME types from a property file (<code>mimetypes.properties</code>).
         * If this file doesn't exist, the existing mimetypes are kept. 
         * <br>
         * Using this mechanism, more mimetypes can be added. The existing mimetypes (configured in the constructor of this class)
         * can be only overwritten, not deleted.
         * 
         * @param propsURL_p
         * @throws Exception
         */
        private void loadFromUrl(URL propsURL_p) throws Exception
        {
            if (propsURL_p != null)
            {
                Properties props = new Properties();
                File propsFile = new File(propsURL_p.getFile());
                if (propsFile.exists())
                {
                    InputStream propStream = null;
                    try
                    {
                        propStream = propsURL_p.openStream();
                        props.load(propStream);
                    }
                    finally
                    {
                        if (propStream != null)
                        {
                            propStream.close();
                        }
                    }
                    for (Entry<Object, Object> entry : props.entrySet())
                    {
                        m_ExtensionToMimeTypes.put(entry.getKey().toString(), entry.getValue().toString());
                    }
                    createMime2TypeExtension();
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwMimeTypes: MIME mapping is loaded and initialized from the " + propsFile.getAbsolutePath() + " file...");
                    }
                }
                else
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwMimeTypes: No custom '" + propsFile.getName() + "' file found. Using of default MIME mappings from OwMimeTypes class...");
                    }
                }
            }
            else
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwMimeTypes: the URL used to initialized the MIME mapping is null...");
                }
            }
        }
    }

    /** embedded extension field (IBM On Demand compatible) */
    public static final String EXTENSION_FIELD = "extension-field";

    /** get a file extension from MIME type
     * @param strMimeType_p String MIME type, can be null
     * @return String the file extension, or null if not found
     */
    public static String getExtensionFromMime(String strMimeType_p)
    {
        // look for a embedded extension field (IBM On Demand compatible)
        if (strMimeType_p == null)
        {
            return null;
        }

        int iEmbededExtension = strMimeType_p.indexOf(EXTENSION_FIELD);
        if (-1 != iEmbededExtension)
        {
            return strMimeType_p.substring(iEmbededExtension + EXTENSION_FIELD.length()).toLowerCase();
        }

        return m_MimeTypes.m_MimeTypesToExtension.get(strMimeType_p);
    }

    /** get MIME type from path
     * @param path_p String path, can be null
     * @return String the MIME type, or null if not found
     * @since 4.2.0.0
     */
    public static String getMimeTypeFromPath(String path_p)
    {
        int iLast = -1;

        if (path_p != null)
        {
            iLast = path_p.lastIndexOf('.');
        }

        // remove Extension from name
        if (-1 != iLast)
        {
            String extension = path_p.substring(iLast + 1, path_p.length());
            return OwMimeTypes.getMimeTypeFromExtension(extension);
        }
        else
        {
            return null;
        }
    }

    /** get MIME type from extension
     * @param strExtension_p String extension, can be null
     * @return String the MIME type, or null if not found
     */
    public static String getMimeTypeFromExtension(String strExtension_p)
    {
        if (strExtension_p != null)
        {
            return m_MimeTypes.m_ExtensionToMimeTypes.get(strExtension_p.toLowerCase());
        }
        else
        {
            return null;
        }
    }

    /** retrieve a parameter from a MIME parameter string 
     *  e.g. text/HTML;name=hallo.txt -> retrieve parameter name results in hallo.txt
     *
     * @param strParameterName_p String name of parameter to retrieve
     *
     * @return String parameter, or null if not found
     */
    public static String getMimeParameter(String strMimeParameter_p, String strParameterName_p)
    {
        if (strMimeParameter_p == null)
        {
            return null;
        }

        int iIndex = strMimeParameter_p.indexOf(strParameterName_p);
        if (iIndex == -1)
        {
            return null;
        }

        iIndex += strParameterName_p.length();

        StringBuffer ret = new StringBuffer();

        boolean fQuota = false;
        for (int i = iIndex; i < strMimeParameter_p.length(); i++)
        {
            char c = strMimeParameter_p.charAt(i);

            switch (c)
            {
                case '=':
                    if (fQuota)
                    {
                        ret.append(c);
                    }
                    break;

                case ';':
                    if (fQuota)
                    {
                        ret.append(c);
                    }
                    else
                    {
                        i = strMimeParameter_p.length();
                    }
                    break;

                case '\"':
                    fQuota = !fQuota;
                    break;

                default:
                    ret.append(c);
                    break;
            }
        }

        return ret.toString();
    }

    /** init the MimeType - loading data from URL
     *  
     * @param propsURL_p URL
     * @throws Exception
     */
    public static void loadFromUrl(URL propsURL_p) throws Exception
    {
        m_MimeTypes.loadFromUrl(propsURL_p);
    }
}