package com.wewebu.ow.clientservices.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;

import com.wewebu.ow.clientservices.exception.OwDecodeException;

/**
 *<p>
 * Utility class for the Workdesk client services applet.
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
public class OwHttpFileDownload
{
    private static final Logger LOGGER = Logger.getLogger(OwHttpFileDownload.class.getName());

    private static final String HTTP_EL_ATTACHMENT = "attachment";
    private static final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    private static final String HTTP_PARAM_FILENAME = "filename";

    public static final String ACCEPT_INSECURE_CERTIFICATES = "accept_insecure_certificates";
    private static final int BUFFER_SIZE = 1024;
    private String url = null;
    private String session = null;
    private OwProgressMonitor progressMonitor;
    private boolean m_isAcceptInsecureCertificates = false;

    public OwHttpFileDownload(String url_p, String session_p)
    {
        this.url = url_p;
        this.session = session_p;
    }

    public void setProgressMonitor(OwProgressMonitor progressMonitor_p)
    {
        this.progressMonitor = progressMonitor_p;
    }

    public boolean isAcceptInsecureCertificates()
    {
        return m_isAcceptInsecureCertificates;
    }

    public void setIsAcceptInsecureCertificates(boolean isAcceptInsecureCertificates_p)
    {
        m_isAcceptInsecureCertificates = isAcceptInsecureCertificates_p;
    }

    public File downloadFile(String dmsID_p)
    {
        HttpGet get = getGetMethod(dmsID_p);
        addHeaders(get);

        File fileObject = null;
        DefaultHttpClient client = null;

        try
        {
            client = createHttpClient();
            HttpResponse response = client.execute(get);

            HttpEntity respEntity = response.getEntity();
            StatusLine statusLine = response.getStatusLine();
            // EntityUtils.consume(respEntity);
            int statusCode = statusLine.getStatusCode();
            if (statusCode < 200 || statusCode >= 300)
            {
                String statusMessage = statusLine.getStatusCode() + " " + statusLine.getReasonPhrase();
                LOGGER.log(Level.SEVERE, "HTTP error status " + statusMessage);
                return null;
            }
            else if (HttpStatus.SC_OK != statusCode)
            {
                LOGGER.log(Level.INFO, "HTTP success status " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
            }

            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP_HEADER_CONTENT_DISPOSITION));
            HeaderElement attachmentElement = null;
            while (it.hasNext())
            {
                HeaderElement headerElement = it.nextElement();
                if (headerElement.getName().equals(HTTP_EL_ATTACHMENT))
                {
                    attachmentElement = headerElement;
                    break;
                }
            }

            if (null != attachmentElement)
            {
                NameValuePair fileNameParam = attachmentElement.getParameterByName(HTTP_PARAM_FILENAME);
                if (null != fileNameParam)
                {
                    String fileName = fileNameParam.getValue().trim();
                    if (fileName.startsWith("=?") && fileName.endsWith("?="))
                    {
                        fileName = OwRfc2047Decoder.decode(fileName);
                    }
                    fileName = OwFileNameSanitizer.getInstance().sanitize(fileName);

                    StringBuffer buffer = new StringBuffer();
                    buffer.append(System.getProperty("java.io.tmpdir"));
                    buffer.append(fileName);

                    fileObject = new File(buffer.toString());

                    if (!fileObject.createNewFile())
                    {
                        // just log it & override the content
                        LOGGER.log(Level.WARNING, "FileDownloadApplet: A file with same name exists on the specified path: " + buffer.toString());
                    }

                    // === delete the file in case of any error
                    if (!writeToFile(respEntity, fileObject) && fileObject.exists())
                    {
                        fileObject.delete();
                        fileObject = null;
                    }
                }
            }
            else
            {
                HeaderIterator itr = response.headerIterator(null);
                String msg = "No Content-Disposition header found, existing headers are:";
                while (itr.hasNext())
                {
                    Header header = (Header) itr.next();
                    msg += header.getName() + " : " + header.getValue();
                }
                LOGGER.log(Level.WARNING, msg);
            }
        }
        catch (HttpException e)
        {
            LOGGER.log(Level.SEVERE, "Fatal protocol violation!", e);
            fileObject = null;
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Fatal transport error!", e);
            fileObject = null;
        }
        catch (OwDecodeException e)
        {
            LOGGER.log(Level.SEVERE, "Fatal transport error!", e);
            fileObject = null;
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, "Fatal transport error!", e);
            fileObject = null;
        }
        finally
        {
            if (null != client)
            {
                client.getConnectionManager().shutdown();
            }
        }

        return fileObject;
    }

    /**
     * @return DefaultHttpClient
     * @throws Exception
     */
    private DefaultHttpClient createHttpClient() throws Exception
    {
        LOGGER.log(Level.INFO, "Creating a new HTTP Client");
        DefaultHttpClient client = new DefaultHttpClient(this.getClientConnectionManager());
        client.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception_p, int executionCount_p, HttpContext context_p)
            {
                // no retries, fail fast
                return false;
            }
        });

        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

        return client;
    }

    /**
     * @return the clientConnectionManager
     * @throws Exception
     */
    private ClientConnectionManager getClientConnectionManager() throws Exception
    {
        try
        {
            LOGGER.log(Level.INFO, "isAcceptInsecureCertificates=" + isAcceptInsecureCertificates());
            ClientConnectionManager ccm = new SingleClientConnManager();
            URL uploadUrl = new URL(this.url);
            if ("https".equalsIgnoreCase(uploadUrl.getProtocol()) && isAcceptInsecureCertificates())
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
            throw new Exception("Could not create Connection Manager", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("Could not create Connection Manager", e);
        }
        catch (KeyManagementException e)
        {
            throw new Exception("Could not create Connection Manager", e);
        }
    }

    private HttpGet getGetMethod(String dmsID_p)
    {
        HttpGet fileGet = null;

        // check if there is only JSESSIONID or also an LTPA-Token
        String url = this.url;
        String jsessionID = "";
        String[] cookies = this.session.split(";");
        if (cookies.length == 1)
        {
            jsessionID = this.session;
            url += ";" + jsessionID;
        }

        if (dmsID_p != null)
        {
            fileGet = new HttpGet(url + "?dmsid=" + dmsID_p);
        }
        else
        {
            fileGet = new HttpGet(url);
        }

        return fileGet;
    }

    private String getSessionCookieData()
    {
        return this.session;
    }

    private void addHeaders(HttpGet fileGet_p)
    {
        /* add session-cookies to header for WebSphere */
        if (this.getSessionCookieData() != null && 0 != this.getSessionCookieData().trim().length())
        {
            fileGet_p.setHeader("Cookie", this.getSessionCookieData());
        }
    }

    private boolean writeToFile(HttpEntity respEntity_p, File file_p)
    {
        InputStream in = null;
        BufferedOutputStream out = null;

        try
        {
            long contentLength = respEntity_p.getContentLength();
            if (null != progressMonitor)
            {
                int size = (int) (contentLength / BUFFER_SIZE) + 1;
                if (0 == size)
                {
                    size = 100;
                }
                progressMonitor.start(size);
            }

            in = respEntity_p.getContent();

            out = new BufferedOutputStream(new FileOutputStream(file_p));
            byte[] buffer = new byte[BUFFER_SIZE];
            int readBytes = 0;
            while ((readBytes = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, readBytes);

                if (progressMonitor != null)
                {
                    progressMonitor.increment(1);
                }
            }

            out.flush();

            return true;
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Fatal transport error!", e);
        }
        finally
        {
            // Release the connection.
            if (null != in)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
            if (null != out)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.SEVERE, "Coul not close output stream.", e);
                }
            }

            if (progressMonitor != null)
            {
                progressMonitor.end();
            }
        }

        return false;
    }

}