package com.wewebu.ow.server.ao;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwNetworkConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An file system application {@link OwObject} provider.
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
 *@since 3.2.0.0
 */
public class OwFileAOSupport implements OwAOSupport, OwConfigurableSupport
{
    private static final Logger LOG = OwLogCore.getLogger(OwFileAOSupport.class);

    private static final String DEFAULTLOCATION_PARAMETER = "defaultLocation";
    private static final String LOCATION_PARAMETER = "location";
    private static final String FILTER_CLASS = "fileFilterClass";

    private String defaultLocation;

    /**Separator used for the file system path, creating for example the default ow_app location*/
    public static final char PATH_SEPARATOR = '/';

    private String basePath;
    private String userID;
    private OwNetwork<?> network;

    private String location;

    private String fileFilterClass;

    public OwFileAOSupport()
    {

    }

    /**
     * Constructor
     * @param network_p current network
     * @param configuration_p bootstrap configuration wrapper
     * @throws OwException
     */
    public OwFileAOSupport(OwNetwork<?> network_p, OwNetworkConfiguration configuration_p, String defaultLocation_p) throws OwException
    {//backwards compatibility constructor in case no AO configuration is available
        initialize(network_p, configuration_p.getApplicationObjectBaseDir(defaultLocation), defaultLocation_p);
    }

    private void initialize(OwNetwork<?> network_p, String location_p, String defaultLocation_p) throws OwException
    {
        try
        {
            this.defaultLocation = defaultLocation_p;
            this.network = network_p;
            OwCredentials credentials = network_p.getCredentials();
            OwUserInfo userInfo = credentials.getUserInfo();
            this.userID = userInfo.getUserID();

            OwNetworkContext context = network_p.getContext();
            this.basePath = context.getBasePath();

            this.location = location_p;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve credentials information...", e);
            throw new OwInvalidOperationException(network_p.getContext().localize("ecmimpl.OwFileAOSupport.credentials.error", "Could not retrieve credentials information!"), e);
        }
    }

    @Override
    public void init(OwSupportConfiguration configuration, OwAOContext context) throws OwConfigurationException
    {
        try
        {
            OwXMLUtil networkConfiguration = context.getConfiguration().getNetworkAdaptorConfiguration();

            String confDefaultLocation = configuration.getParameterValue(DEFAULTLOCATION_PARAMETER, "");
            String confLocation = configuration.getParameterValue(LOCATION_PARAMETER, "");
            if (confLocation.isEmpty())
            {
                confLocation = networkConfiguration.getSafeTextValue(OwNetworkConfiguration.EL_OWAPPLICATIONOBJECTBASEDIR, confDefaultLocation);
            }
            fileFilterClass = configuration.getParameterValue(FILTER_CLASS, "com.wewebu.ow.server.ao.OwFileAOSupport$BasicFileFilter");
            initialize(context.getNetwork(), confLocation, confDefaultLocation);
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not initialize file AO support.", e);
        }
    }

    public OwObject[] getSupportObjects(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        File folder = getSupportFile(strName_p, forceUserspecificObject_p, createIfNotExist_p, false);
        File[] siteFiles = folder.listFiles(createFileFilter());
        if (siteFiles == null)
        {
            return new OwFileObject[0];
        }
        else
        {
            OwObject[] objects = new OwObject[siteFiles.length];
            if (siteFiles.length > 0)
            {
                for (int i = 0; i < siteFiles.length; i++)
                {
                    File file = siteFiles[i];
                    if (!file.isHidden())
                    {
                        try
                        {
                            objects[i] = new OwFileObject(this.network, file, file.getName());
                        }
                        catch (OwException e)
                        {
                            throw e;
                        }
                        catch (Exception e)
                        {
                            LOG.warn("Could not read application object file: " + file, e);
                        }
                    }
                }
            }
            return objects;
        }
    }

    public OwObject getSupportObject(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        File file = getSupportFile(strName_p, forceUserspecificObject_p, createIfNotExist_p, true);
        try
        {
            return new OwFileObject(this.network, file, file.getName());
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("Could not create OwFileObject for " + strName_p + ", forceUsersSpecificObject=" + forceUserspecificObject_p + " , createIfNotExist=" + createIfNotExist_p, e);
            throw new OwInvalidOperationException(this.network.getContext().localize1("ecmimpl.OwFileAOSupport.getSupportObject.error", "Could not create file application object for %1.", strName_p), e);
        }
    }

    public File getSupportFile(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p, boolean isFile_p) throws OwException
    {
        // === look in the folder .../WEB-INF/site/strCategory/... for files

        StringBuffer strFilePath;
        if (this.location.equals(defaultLocation))
        {
            //if default location get BasePath to deployment
            strFilePath = new StringBuffer(this.basePath.replace('\\', PATH_SEPARATOR));
            if (strFilePath.charAt(strFilePath.length() - 1) != PATH_SEPARATOR)
            {
                strFilePath.append(PATH_SEPARATOR);
            }
            strFilePath.append(this.location);
        }
        else
        {
            if (this.location.indexOf("/WEB-INF/") == 0 || this.location.indexOf("WEB-INF/") == 0)
            {
                strFilePath = new StringBuffer(this.basePath.replace('\\', PATH_SEPARATOR));
                if (strFilePath.charAt(strFilePath.length() - 1) != PATH_SEPARATOR)
                {
                    strFilePath.append(PATH_SEPARATOR);
                }
                strFilePath.append(this.location);
            }
            else
            {
                //otherwise we have a location definition in bootstrap.xml
                strFilePath = new StringBuffer(this.location.replace('\\', PATH_SEPARATOR));
            }
        }

        // === look in the folder <basedir>/strCategory/... for files
        if (strFilePath.charAt(strFilePath.length() - 1) == PATH_SEPARATOR)
        {
            strFilePath.append(strName_p);
        }
        else
        {
            strFilePath.append(PATH_SEPARATOR);
            strFilePath.append(strName_p);
        }

        if (forceUserspecificObject_p)
        {
            // === look in the folder <basedir>/strCategory/<userid>/... for files
            strFilePath.append("_");
            String filePath;
            try
            {
                filePath = URLEncoder.encode(this.userID, "UTF-8");
                strFilePath.append(filePath);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new OwServerException("Could not encode file path.");
            }
        }

        File file = new File(strFilePath.toString());
        if (!file.exists() && createIfNotExist_p)
        {
            if (isFile_p)
            {
                try
                {
                    LOG.info("OwFileAOSupport.getSupportFile: The file was successfully created: " + file.createNewFile());
                }
                catch (IOException e)
                {
                    LOG.fatal("Could not create file: " + strFilePath, e);
                    throw new OwServerException(this.network.getContext().localize1("ecmimpl.OwFileAOSupport.create.file.error", "Error creating file %1", strFilePath.toString()), e);
                }
            }
            else
            {
                LOG.info("OwFileAOSupport.getSupportFile: File structure successfully created: " + file.mkdirs());
            }
        }
        return file;
    }

    @Override
    public String toString()
    {
        return "OwFileAOSupport(basePath=" + this.basePath + ";location=" + this.location + ";userID=" + this.userID + ")";
    }

    /**
     * Factory for FileFilter which will return only specific files.
     * @return FileFilter
     * @since 4.2.0.0
     */
    @SuppressWarnings("unchecked")
    protected FileFilter createFileFilter()
    {
        try
        {
            if (this.fileFilterClass != null)
            {
                Class<FileFilter> filter = (Class<FileFilter>) Class.forName(this.fileFilterClass);
                return filter.newInstance();
            }
        }
        catch (Exception ex)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.warn("Unable to create instance of " + this.fileFilterClass + ", creating BasicFileFitler", ex);
            }
            else
            {
                LOG.warn("Unable to create instance of " + this.fileFilterClass + ", creating BasicFileFitler");
            }
        }
        return new BasicFileFilter();
    }

    protected static class BasicFileFilter implements FileFilter
    {
        @Override
        public boolean accept(File pathname)
        {

            return !pathname.isHidden() && pathname.isFile();
        }
    }
}
