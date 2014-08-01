package com.wewebu.ow.client.upload;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 *<p>
 * Configuration properties supported by the applet.
 * Usually these properties are read from a properties file.
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
 *@see OwAppletParams#PARAM_PROPS_FILE
 */
public class OwCfgProperties extends OwAbstractCfg
{
    public static final String PROP_ENCODE_PATH = "encode_path";
    public static final String PROP_TRANSLATE_PATH = "translate_path";
    public static final String PROP_FULL_PATH = "full_path";
    public static final String PROP_STRIP_ROOTPATH = "strip_rootPath";
    public static final String ACCEPT_INSECURE_CERTIFICATES = "accept_insecure_certificates";

    /**
     *  Integer which specifies the maximum number of files allowed.
     */
    public static final String PROP_MAX_UPLOAD_COUNT = "max_upload_count";
    /**
     *  Integer which should specify the maximum upload size in bytes per file.
     *  <p>Use -1 or leave empty for unlimited size</p>
     */
    public static final String PROP_MAX_UPLOAD_SIZE = "max_upload_size";
    /**
     *  Integer which should specify the maximum upload size, in bytes, of the entire batch.
     *  <p>Use -1 or leave empty for unlimited size</p>
     */
    public static final String PROP_MAX_BATCH_UPLOAD_SIZE = "max_batch_upload_size";
    /**
     * Comma separated file extensions to be accepted by the upload applet.
     * <p>Ex. tif,tiff,jpg,jpeg</p>
     * <p>To accept all file type leave this configuration empty.</p>
     */
    public static final String PROP_FILE_EXT_FILTER = "file_ext_filter";

    private Properties properties;

    private OwCfgProperties(Properties properties_p)
    {
        this.properties = properties_p;
    }

    public static OwCfgProperties loadFrom(URL url_p) throws OwUploadCfgException
    {
        try
        {
            Properties properties = new Properties();
            properties.load(url_p.openStream());
            return new OwCfgProperties(properties);
        }
        catch (IOException ioe_p)
        {
            throw new OwUploadCfgException("An error occured while reading the configuration.", ioe_p);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwAbstractCfg#getString(java.lang.String)
     */
    @Override
    public String getString(String propName_p)
    {
        return this.properties.getProperty(propName_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwAbstractCfg#has(java.lang.String)
     */
    @Override
    public boolean has(String propName_p)
    {
        String value = this.properties.getProperty(propName_p);
        return null != value && 0 != value.trim().length();
    }

    public static OwCfgProperties empty()
    {
        return new OwCfgProperties(new Properties());
    }
}
