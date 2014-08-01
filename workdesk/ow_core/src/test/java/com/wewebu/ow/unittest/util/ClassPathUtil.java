package com.wewebu.ow.unittest.util;

import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

/**
 *<p>
 * ClassPathUtil.
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
public class ClassPathUtil
{

    public ClassPathUtil()
    {
    }

    /**
     * TEST
     */
    public static void test()
    {
        ClassPathUtil cp = new ClassPathUtil();
        cp.printClasspath();
        cp.validate();
    }

    public static void main(String[] args_p)
    {
        ClassPathUtil cp = new ClassPathUtil();
        cp.printClasspath();
        cp.validate();
    }

    public void printClasspath()
    {
        log("\nClasspath:");
        StringTokenizer tokenizer = new StringTokenizer(getClasspath(), File.pathSeparator);
        while (tokenizer.hasMoreTokens())
        {
            log(tokenizer.nextToken());
        }
    }

    private String getClasspath()
    {
        return System.getProperty("java.class.path");
    }

    private void log(String message_p)
    {
        System.out.println(message_p);
    }

    public void validate()
    {
        StringTokenizer tokenizer = new StringTokenizer(getClasspath(), File.pathSeparator);
        while (tokenizer.hasMoreTokens())
        {
            String element = tokenizer.nextToken();
            File f = new File(element);
            if (!f.exists())
            {
                log("\n'" + element + "' " + "does not exist.");
            }
            else if ((!f.isDirectory()) && (!element.toLowerCase().endsWith(".jar")) && (!element.toLowerCase().endsWith(".zip")))
            {
                log("\n'" + element + "' is not a directory, .jar file, or .zip file.");
            }
        }
    }

    public void testClass(String resource_p)
    {

        // String resource = "/org/apache/commons/configuration/PropertiesConfiguration.class";
        URL classUrl = getResourceURL(resource_p);
        if (classUrl == null)
        {
            log("\n" + resource_p + " - Class not found!");
        }
        else
        {
            log("\nClass found in: '" + classUrl.getFile() + "'");
        }
    }

    private URL getResourceURL(String resource_p)
    {
        return ClassPathUtil.class.getResource(resource_p);
    }

}
