package com.wewebu.ow.unittest.util;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

/**
 *<p>
 * ResourceUtil.
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
public class ResourceUtil
{

    private static ResourceUtil instance = null;

    private static Logger LOG = Logger.getLogger("ResourceUtil");

    private ResourceUtil()
    {

    }

    public static ResourceUtil getInstance()
    {

        if (instance == null)
        {
            instance = new ResourceUtil();
        }
        return instance;
    }

    /**
     * Returns a class using ClassLoader awareness.
     * 
     * @param classPath_p
     *            The class path.
     * @return Class
     * @throws ClassNotFoundException
     */
    public Class getClass(String classPath_p) throws ClassNotFoundException
    {
        Class clazz = null;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null)
        {
            try
            {
                clazz = cl.loadClass(classPath_p);
            }
            catch (ClassNotFoundException cnfe)
            {
                clazz = null;
            }
        }

        if (clazz != null)
        {
            return clazz;
        }

        try
        {
            clazz = ResourceUtil.class.getClassLoader().loadClass(classPath_p);
        }
        catch (ClassNotFoundException cnfe)
        {
            clazz = null;
        }

        if (clazz != null)
        {
            return clazz;
        }

        clazz = ClassLoader.getSystemClassLoader().loadClass(classPath_p);

        return clazz;
    }

    /**
     * Returns the URL to a resource which may be a file or a classpath
     * resource.
     * 
     * @param resource_p
     * @return URL or <code>null</code> if not found.
     */
    public URL getUrl(String resource_p)
    {

        URL url = null;

        // Is it a file?

        File file = new File(resource_p);
        if (file.exists() && file.isFile())
        {
            try
            {
                url = file.toURL();
            }
            catch (MalformedURLException e)
            {
                url = null;
                LOG.error("MalformedURLException in ResourceUtil.getUrl", e);
            }
        }

        if (url != null)
        {
            return url;
        }

        // Is it a classpath resource?

        // Check the ClassLoader of the current Thread stack
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl != null)
        {
            url = cl.getResource(resource_p);
        }

        if (url != null)
        {
            return url;
        }

        // Check this classes ClassLoader
        url = ResourceUtil.class.getResource(resource_p);

        if (url != null)
        {
            return url;
        }

        // Finally, try the system ClassLoader
        url = ClassLoader.getSystemResource(resource_p);

        return url;
    }

    /**
     * Locates a file and returns the absolute path including the file name.
     * 
     * @param file_p String  the file to find
     * @return the absolute path to the file including the file name
     */
    public String getResourcePath(String file_p)
    {
        String path = "";
        if (null != file_p)
        {
            file_p = file_p.trim();
        }
        else
        {
            throw new IllegalArgumentException("Provided file name or path is null");
        }
        URL url = this.getUrl(file_p);
        if (url != null)
        {
            path = url.getPath();
            try
            {
                path = URLDecoder.decode(path, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                LOG.error("UnsupportedEncodingException in ResourceUtil.getResourcePath", e);
            }
        }
        return path;
    }

    /**
     * Locates a file and returns the absolute path including the file name.
     * 
     * @param file_p
     * 
     * @return the absolute path to the file including the file name
     */
    public String locateResourcePathUTF8(String file_p)
    {
        String path = "";
        if (null != file_p)
        {
            file_p = file_p.trim();
        }
        else
        {
            throw new IllegalArgumentException("Provided file name or path is null");
        }
        URL url = this.getUrl(file_p);
        if (url != null)
        {
            try
            {
                path = URLDecoder.decode(url.getPath(), "UTF-8").substring(1);
            }
            catch (UnsupportedEncodingException e)
            {
                path = "";
                LOG.error("UnsupportedEncodingException in ResourceUtil.locateResourcePathUTF8", e);
            }
        }
        return path;
    }

    /**
     * Return the resource as InputStream
     * 
     * @param path_p
     * @return InputStream
     */
    public InputStream getResourceAsStream(String path_p)
    {
        return ResourceUtil.class.getResourceAsStream(path_p);
    }

}
