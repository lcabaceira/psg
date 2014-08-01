package com.wewebu.ow.client.upload;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.SingleClientConnManager;

/**
 *<p>
 * This is the configuration used by the {@link OwUploadProcess} implementation.
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
 *@see OwUploadProcess
 */
public class OwUploadCfg
{
    private static final Logger LOGGER = Logger.getLogger(OwUploadCfg.class.getName());

    private String cookiesData;
    private boolean translatePath = false;
    private boolean encodePath = false;
    private String uploadURL;
    private boolean sendFullPath;
    private boolean acceptMultifile;
    private boolean acceptInsecureCertificates = false;
    private boolean stripRootPath;

    private int maxUploadCount;
    private int maxUploadSize;
    private boolean useExtJs;
    private Set<String> fileExtFilter;

    private int maxBatchSize;

    /**
     * Builds a configuration from the applet's parameters and from the configuration properties file.
     * @param params_p
     * @param cfgProperties_p
     * @throws OwUploadCfgPropertyException If there are some errors in the configuration parameters/properties.
     */
    public OwUploadCfg(OwAbstractCfg params_p, OwAbstractCfg cfgProperties_p) throws OwUploadCfgPropertyException
    {
        String uploadUrl = params_p.getString(OwAppletParams.PARAM_UPLOAD_URL);
        if (null == uploadUrl)
        {
            throw new OwUploadCfgPropertyException(OwAppletParams.PARAM_UPLOAD_URL, "The " + OwAppletParams.PARAM_UPLOAD_URL + " configuration parameter is mandatory.");
        }
        try
        {
            new URL(uploadUrl);
        }
        catch (MalformedURLException e)
        {
            throw new OwUploadCfgPropertyException(OwAppletParams.PARAM_UPLOAD_URL, "The " + OwAppletParams.PARAM_UPLOAD_URL + " configuration must be a valid URL. Provided: " + uploadUrl);
        }
        this.setUploadURL(uploadUrl);

        this.setCookiesData(params_p.getString(OwAppletParams.PARAM_COOKIES_DATA));

        this.setAcceptMultifile(params_p.getBoolean(OwAppletParams.PARAM_MULTIFILE));
        this.setTranslatePath(cfgProperties_p.getBoolean(OwCfgProperties.PROP_TRANSLATE_PATH));
        this.setEncodePath(cfgProperties_p.getBoolean(OwCfgProperties.PROP_ENCODE_PATH));
        this.setSendFullPath(cfgProperties_p.getBoolean(OwCfgProperties.PROP_FULL_PATH));
        this.setStripRootPath(cfgProperties_p.getBoolean(OwCfgProperties.PROP_STRIP_ROOTPATH));
        this.setAcceptInsecureCertificates(cfgProperties_p.getBoolean(OwCfgProperties.ACCEPT_INSECURE_CERTIFICATES));
        this.setUseExtJs(params_p.getBoolean(OwAppletParams.PROP_USE_EXTJS));

        if (cfgProperties_p.has(OwCfgProperties.PROP_MAX_UPLOAD_COUNT))
        {
            this.setMaxUploadCount(cfgProperties_p.getInt(OwCfgProperties.PROP_MAX_UPLOAD_COUNT));
        }
        else
        {
            this.setMaxUploadCount(-1);
        }

        if (cfgProperties_p.has(OwCfgProperties.PROP_MAX_UPLOAD_SIZE))
        {
            this.setMaxUploadSize(cfgProperties_p.getInt(OwCfgProperties.PROP_MAX_UPLOAD_SIZE));
        }
        else
        {
            this.setMaxUploadSize(-1);
        }

        if (cfgProperties_p.has(OwCfgProperties.PROP_MAX_BATCH_UPLOAD_SIZE))
        {
            this.setMaxBatchUploadSize(cfgProperties_p.getInt(OwCfgProperties.PROP_MAX_BATCH_UPLOAD_SIZE));
        }
        else
        {
            this.setMaxBatchUploadSize(-1);
        }

        if (cfgProperties_p.has(OwCfgProperties.PROP_FILE_EXT_FILTER))
        {
            this.setFileExtFilter(cfgProperties_p.getString(OwCfgProperties.PROP_FILE_EXT_FILTER));
        }
        else
        {
            this.setFileExtFilter("");
        }
    }

    private void setMaxBatchUploadSize(int maxBatchSize_p)
    {
        this.maxBatchSize = maxBatchSize_p;
    }

    public int getMaxBatchSize()
    {
        return maxBatchSize;
    }

    private void setFileExtFilter(String filter_p)
    {
        this.fileExtFilter = new HashSet<String>();
        StringTokenizer tokenizer = new StringTokenizer(filter_p, ",");
        while (tokenizer.hasMoreElements())
        {
            String ext = (String) tokenizer.nextElement();
            this.fileExtFilter.add(ext.trim().toLowerCase());
        }
    }

    public boolean isExtensionAccepted(String fileName_p)
    {
        if (this.fileExtFilter.isEmpty())
        {
            return true;
        }

        int extensionStart = fileName_p.lastIndexOf('.');
        String extension = null;
        if (-1 != extensionStart)
        {
            extension = fileName_p.substring(extensionStart + 1).toLowerCase();
        }
        if (null != extension)
        {
            return this.fileExtFilter.contains(extension);
        }
        else
        {
            return false;
        }
    }

    public int getMaxUploadSize()
    {
        return maxUploadSize;
    }

    private void setMaxUploadSize(int maxSize_p)
    {
        this.maxUploadSize = maxSize_p;
    }

    public boolean isSizeAccepted(long size_p)
    {
        if (this.maxUploadSize > 0)
        {
            return size_p <= this.maxUploadSize;
        }
        else
        {
            // Unlimited
            return true;
        }
    }

    private void setCookiesData(String extraHeader_p)
    {
        this.cookiesData = extraHeader_p;
    }

    public String getCookiesData()
    {
        return cookiesData;
    }

    private void setTranslatePath(boolean translatePath_p)
    {
        this.translatePath = translatePath_p;
    }

    public boolean isTranslatePath()
    {
        return translatePath;
    }

    private void setEncodePath(boolean encodePath_p)
    {
        this.encodePath = encodePath_p;
    }

    public boolean isEncodePath()
    {
        return encodePath;
    }

    private void setUploadURL(String uploadURL_p)
    {
        this.uploadURL = uploadURL_p;
    }

    public String getUploadURL()
    {
        return uploadURL;
    }

    private void setSendFullPath(boolean fullPath_p)
    {
        this.sendFullPath = fullPath_p;
    }

    public boolean isSendFullPath()
    {
        return sendFullPath;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("cookiesData: " + cookiesData + "\n");
        buffer.append("translatePath: " + translatePath + "\n");
        buffer.append("encodePath: " + encodePath + "\n");
        buffer.append("uploadURL: " + uploadURL + "\n");
        buffer.append("sendFullPath: " + sendFullPath + "\n");
        buffer.append("acceptMultifile: " + acceptMultifile + "\n");
        buffer.append("fileExtFilter: ");
        for (String ext : fileExtFilter)
        {
            buffer.append("'" + ext + "' ");
        }
        buffer.append("\n");

        return buffer.toString();
    }

    private void setAcceptMultifile(boolean acceptMultifile_p)
    {
        this.acceptMultifile = acceptMultifile_p;
    }

    public boolean isAcceptMultifile()
    {
        return acceptMultifile;
    }

    private void setMaxUploadCount(int maxUploadCount_p)
    {
        this.maxUploadCount = maxUploadCount_p;
    }

    public int getMaxUploadCount()
    {
        return maxUploadCount;
    }

    public void setUseExtJs(boolean useExtJs_p)
    {
        this.useExtJs = useExtJs_p;
    }

    public boolean isUseExtJs()
    {
        return useExtJs;
    }

    public void setAcceptInsecureCertificates(boolean acceptBadCertificates)
    {
        this.acceptInsecureCertificates = acceptBadCertificates;
    }

    public boolean isAcceptInsecureCertificates()
    {
        return acceptInsecureCertificates;
    }

    /**
     * @return the clientConnectionManager
     * @throws OwDNDException 
     */
    public ClientConnectionManager getClientConnectionManager() throws OwDNDException
    {
        try
        {
            ClientConnectionManager ccm = new SingleClientConnManager();
            URL uploadUrl = new URL(this.getUploadURL());
            if ("https".equalsIgnoreCase(uploadUrl.getProtocol()) && this.isAcceptInsecureCertificates())
            {
                LOGGER.log(Level.WARNING, "This instance will trust all certificates being they valid or invalid.");
                LOGGER.log(Level.WARNING, "In production mode accept_bad_certificates should always be turned off.");

                SSLContext ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm = new X509TrustManager() {

                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                    {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                    {
                    }

                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                };
                ctx.init(null, new TrustManager[] { tm }, null);
                SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                Scheme myHttpsScheme = new Scheme("https", uploadUrl.getPort(), ssf);
                SchemeRegistry sr = ccm.getSchemeRegistry();
                sr.register(myHttpsScheme);
            }

            return ccm;
        }
        catch (MalformedURLException e)
        {
            throw new OwDNDException("Could not create Connection Manager", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new OwDNDException("Could not create Connection Manager", e);
        }
        catch (KeyManagementException e)
        {
            throw new OwDNDException("Could not create Connection Manager", e);
        }
    }

    /**
     * @param stripRootPath the stripRootPath to set
     */
    public void setStripRootPath(boolean stripRootPath)
    {
        this.stripRootPath = stripRootPath;
    }

    /**
     * @return the stripRootPath
     */
    public boolean isStripRootPath()
    {
        return stripRootPath;
    }
}
