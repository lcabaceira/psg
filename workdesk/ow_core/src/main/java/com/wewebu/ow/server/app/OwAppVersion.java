/**
 * 
 */
package com.wewebu.ow.server.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 * <p>
 * This class models a version string.
 * </p>
 * 
 * <p>
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 * </font>
 * </p>
 * 
 * @since 3.2.0.0
 */
public class OwAppVersion
{
    private static final Logger LOG = OwLogCore.getLogger(OwAppVersion.class);

    /**
     * The name of the property holding the version string.
     */
    private static final String PROP_VERSION = "version";

    /**
     * The name of the property holding the build number string.
     */
    private static final String PROP_BUILDNUMBER = "buildNumber";
    /**
     * The name of the property holding the edition string.
     */
    private static final String PROP_EDITION = "edition";

    private String versionString;
    private String buildNumber;
    private String editionString;

    private OwAppVersion(String versionString, String buildNumber, String edition)
    {
        this.versionString = versionString;
        this.buildNumber = buildNumber;
        this.editionString = edition;
    }

    public static OwAppVersion unknownVersion()
    {
        return new OwAppVersion("default.OwMainLayout.jsp.footer.versionString", "", "default.OwMainLayout.jsp.footer.development");

    }

    /**
     * Creates a {@link OwAppVersion} from a properties file.
     * 
     * @param properties
     * @return OwAppVersion
     */
    public static OwAppVersion fromProperties(Properties properties)
    {
        String versionString = properties.getProperty(PROP_VERSION);
        String buildNumberString = properties.getProperty(PROP_BUILDNUMBER);
        String editionString = properties.getProperty(PROP_EDITION);
        return new OwAppVersion(versionString, buildNumberString, editionString);
    }

    public static OwAppVersion fromProperties(URL versionPropertiesURL)
    {
        if (null == versionPropertiesURL)
        {
            throw new IllegalArgumentException("versionPropertiesURL can not be null !");
        }

        Properties versionProperties = new Properties();
        InputStream inputStream = null;
        try
        {
            inputStream = versionPropertiesURL.openStream();
            versionProperties.load(inputStream);
            return OwAppVersion.fromProperties(versionProperties);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read version information !", e);
        }
        finally
        {
            if (null != inputStream)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    LOG.warn("Could not close stream !", e);
                }
            }
        }

    }

    public String getVersionString()
    {
        return versionString;
    }

    public String getBuildNumber()
    {
        return buildNumber;
    }

    public String getEditionString()
    {
        return editionString;
    }
}
