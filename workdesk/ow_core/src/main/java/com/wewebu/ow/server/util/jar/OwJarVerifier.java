package com.wewebu.ow.server.util.jar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedDataParser;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.util.Store;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Utility class for checking jar file integrity.
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
public class OwJarVerifier
{
    private static final Logger LOG = OwLogCore.getLogger(OwJarVerifier.class);
    private static final String ALFRESCO_CODESIGN_CER = "alfresco-code-sign-public.cer";

    private static X509Certificate weWebUCert;

    private URL jarURL = null;

    private JarFile jarFile = null;

    public OwJarVerifier(URL jarURL_p)
    {
        this.jarURL = jarURL_p;

        try
        {
            if (jarFile == null)
            {
                jarFile = retrieveJarFileFromURL(jarURL_p);
            }
        }
        catch (Exception ex)
        {
            SecurityException se = new SecurityException();
            se.initCause(ex);
            throw se;
        }
    }

    /**
     * Retrieve the jar file from the specified URL.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private JarFile retrieveJarFileFromURL(URL url_p) throws PrivilegedActionException, MalformedURLException
    {
        LOG.info(String.format("Trying to retrieve the jar content from %s.", url_p.toExternalForm()));

        JarFile jf = null;

        // Prepare the URL with the appropriate protocol.
        jarURL = url_p.getProtocol().equalsIgnoreCase("jar") ? url_p : new URL("jar:" + url_p.toExternalForm() + "!/");
        LOG.info(String.format("The URL prepared for creating the JarURLConnection is %s.", jarURL.toExternalForm()));

        // Retrieve the jar file using JarURLConnection
        jf = (JarFile) AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception
            {
                JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
                // Always get a fresh copy, so we don't have to
                // worry about the stale file handle when the
                // cached jar is closed by some other application.
                conn.setUseCaches(false);
                return conn.getJarFile();
            }
        });
        return jf;
    }

    /**
     * First, retrieve the jar file from the URL passed in constructor.
     * Then, compare it to the expected X509Certificate.
     * If everything went well and the certificates are the same, no
     * exception is thrown.
     * @param targetCert_p
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void verify(X509Certificate targetCert_p) throws IOException
    {
        // Sanity checking
        if (targetCert_p == null)
        {
            throw new SecurityException("Provider certificate is invalid");
        }

        Vector entriesVec = new Vector();

        // Ensure the jar file is signed.
        Manifest man = jarFile.getManifest();
        if (man == null)
        {
            throw new SecurityException("The provider is not signed");
        }

        // Ensure all the entries' signatures verify correctly
        byte[] buffer = new byte[8192];
        Enumeration entries = jarFile.entries();

        while (entries.hasMoreElements())
        {
            JarEntry je = (JarEntry) entries.nextElement();

            // Skip directories.
            if (je.isDirectory())
            {
                continue;
            }
            entriesVec.addElement(je);
            InputStream is = jarFile.getInputStream(je);

            // Read in each jar entry. A security exception will
            // be thrown if a signature/digest check fails.
            while (is.read(buffer, 0, buffer.length) != -1)
            {
                // Don't care
            }
            is.close();
        }

        // Get the list of signer certificates
        Enumeration e = entriesVec.elements();

        while (e.hasMoreElements())
        {
            JarEntry je = (JarEntry) e.nextElement();

            // Every file must be signed except files in META-INF.
            Certificate[] certs = je.getCertificates();
            if ((certs == null) || (certs.length == 0))
            {
                if (!je.getName().startsWith("META-INF"))
                {
                    throw new SecurityException("The file " + je.getName() + " is not signed!");
                }
            }
            else
            {
                // Check whether the file is signed by the expected
                // signer. The jar may be signed by multiple signers.
                // See if one of the signers is 'targetCert'.
                int startIndex = 0;
                X509Certificate[] certChain;
                boolean signedAsExpected = false;

                while ((certChain = getAChain(certs, startIndex)) != null)
                {
                    if (certChain[0].equals(targetCert_p))
                    {
                        // Stop since one trusted signer is found.
                        signedAsExpected = true;
                        break;
                    }
                    // Proceed to the next chain.
                    startIndex += certChain.length;
                }

                if (!signedAsExpected)
                {
                    throw new SecurityException("The provider " + "is not signed by a " + "trusted signer");
                }
            }
        }
    }

    /**
     * Extracts ONE certificate chain from the specified certificate array
     * which may contain multiple certificate chains, starting from index
     * 'startIndex'.
     */
    private static X509Certificate[] getAChain(Certificate[] certs_p, int startIndex_p)
    {
        if (startIndex_p > certs_p.length - 1)
        {
            return null;
        }

        int i;
        // Keep going until the next certificate is not the
        // issuer of this certificate.
        for (i = startIndex_p; i < certs_p.length - 1; i++)
        {
            if (!((X509Certificate) certs_p[i + 1]).getSubjectDN().equals(((X509Certificate) certs_p[i]).getIssuerDN()))
            {
                break;
            }
        }
        // Construct and return the found certificate chain.
        int certChainSize = (i - startIndex_p) + 1;
        X509Certificate[] ret = new X509Certificate[certChainSize];
        for (int j = 0; j < certChainSize; j++)
        {
            ret[j] = (X509Certificate) certs_p[startIndex_p + j];
        }
        return ret;
    }

    // Close the jar file once this object is no longer needed.
    protected void finalize() throws Throwable
    {
        jarFile.close();
    }

    /**
     * We'll search for a file named META-INF/*.DSA or META-INF/*.RSA 
     * @return true if this jar is signed
     */
    public boolean isSigned()
    {
        try
        {
            Manifest manifest = jarFile.getManifest();
            if (null == manifest)
            {
                return false;
            }

            JarEntry signatureBlockEntry = getSignatureBlockEntry();
            return null != signatureBlockEntry;
        }
        catch (IOException e)
        {
            SecurityException securityException = new SecurityException();
            securityException.initCause(e);
            throw securityException;
        }
    }

    @SuppressWarnings("rawtypes")
    private JarEntry getSignatureBlockEntry()
    {
        JarEntry signatureBlockEntry = null;
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements())
        {
            JarEntry entry = (JarEntry) entries.nextElement();
            String entryName = entry.getName();
            if (entryName.startsWith("META-INF"))
            {
                if (entryName.endsWith("RSA") || entryName.endsWith("DSA"))
                {
                    signatureBlockEntry = entry;
                    break;
                }
            }
        }
        return signatureBlockEntry;
    }

    /**
     * Get Signature Certificates
     * @return {@link X509Certificate}[]
     * @throws IOException
     * @throws CMSException 
     */
    @SuppressWarnings("rawtypes")
    public X509CertificateHolder[] getSignatureCertificates() throws IOException, CMSException
    {
        JarEntry signatureBlockEntry = getSignatureBlockEntry();
        if (null != signatureBlockEntry)
        {
            InputStream inputStream = null;
            try
            {
                inputStream = jarFile.getInputStream(signatureBlockEntry);
                CMSSignedDataParser sp = new CMSSignedDataParser(new BcDigestCalculatorProvider(), new BufferedInputStream(inputStream, 1024));
                Store certStore = sp.getCertificates();
                SignerInformationStore signers = sp.getSignerInfos();

                Collection c = signers.getSigners();
                Iterator it = c.iterator();

                List<X509CertificateHolder> certificates = new ArrayList<X509CertificateHolder>();
                while (it.hasNext())
                {
                    SignerInformation signer = (SignerInformation) it.next();
                    Collection certCollection = certStore.getMatches(signer.getSID());

                    Iterator certIt = certCollection.iterator();
                    X509CertificateHolder cert = (X509CertificateHolder) certIt.next();

                    certificates.add(cert);
                }

                return certificates.toArray(new X509CertificateHolder[certificates.size()]);
            }
            finally
            {
                if (inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (IOException ex)
                    {
                    }
                }
                inputStream = null;
            }
        }
        return new X509CertificateHolder[] {};
    }

    /**
     * Get Workdesk Certificate
     * @return {@link X509Certificate}
     * @throws IOException
     * @throws CertificateException
     */
    public static X509Certificate getWeWebUCertificate() throws IOException, CertificateException
    {
        if (null == weWebUCert)
        {
            InputStream inStream = null;
            try
            {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                URL url = OwJarVerifier.class.getResource("/" + ALFRESCO_CODESIGN_CER);
                LOG.info(String.format("Loading certificate from %s.", url));
                inStream = url.openStream();
                weWebUCert = (X509Certificate) cf.generateCertificate(inStream);
            }
            finally
            {
                if (inStream != null)
                {
                    try
                    {
                        inStream.close();
                    }
                    catch (IOException ex)
                    {
                    }
                    inStream = null;
                }
            }
        }

        return weWebUCert;
    }
}