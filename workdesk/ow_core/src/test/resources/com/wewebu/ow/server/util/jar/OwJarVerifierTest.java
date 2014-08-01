package com.wewebu.ow.server.util.jar;

import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

public class OwJarVerifierTest extends TestCase
{
    private X509Certificate weWebUCert;

    protected void setUp() throws Exception
    {
        super.setUp();
        URL certURL = OwJarVerifierTest.class.getResource("wewebu_codesign_pk.bin.cert");

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream inStream = certURL.openStream();
        weWebUCert = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();
    }

    public void testSignedJarTemperedClass() throws Exception
    {
        try
        {
            URL jarURL = OwJarVerifierTest.class.getResource("clientcombridge_tempered_class.jar");
            OwJarVerifier jarVerifier = new OwJarVerifier(jarURL);
            jarVerifier.verify(weWebUCert);
            fail("A tempered class should be detected!");
        }
        catch (SecurityException securityException)
        {
            // ok
            assertTrue("A tempered class should be detected!", -1 != securityException.getMessage().indexOf("OfficeControlApplet.class"));
        }
    }

    //    public void testSignedJarTemperedManifest() throws Exception
    //    {
    //        try
    //        {
    //            URL jarURL = JarVerifierTest.class.getResource("clientcombridge_tempered_manifest.jar");
    //            JarVerifier jarVerifier = new JarVerifier(jarURL);
    //            jarVerifier.verify(weWebUCert);
    //            fail("A tempered manifest should be detected!");
    //        }
    //        catch (SecurityException securityException)
    //        {
    //            // ok
    //            assertTrue("A tempered manifest should be detected!", -1 != securityException.getMessage().indexOf("OfficeControlApplet.class"));
    //        }
    //    }

    public void testSignedJarTemperedNewClass() throws Exception
    {
        try
        {
            URL jarURL = OwJarVerifierTest.class.getResource("clientcombridge_tempered_newClass.jar");
            OwJarVerifier jarVerifier = new OwJarVerifier(jarURL);
            jarVerifier.verify(weWebUCert);
            fail("An unsigned class should be detected!");
        }
        catch (SecurityException securityException)
        {
            // ok
            assertTrue("An unsigned class should be detected!", -1 != securityException.getMessage().indexOf("OfficeControlApplet1.class is not signed!"));
        }
    }

    public void testUnSigned() throws Exception
    {
        URL jarURL = OwJarVerifierTest.class.getResource("clientcombridge.jar");
        OwJarVerifier jarVerifier = new OwJarVerifier(jarURL);
        assertTrue(jarVerifier.isSigned());

        jarURL = OwJarVerifierTest.class.getResource("clientcombridge_unsigned.jar");
        jarVerifier = new OwJarVerifier(jarURL);
        assertFalse(jarVerifier.isSigned());
    }

    public void testGetSignatureCertificates() throws Exception
    {
        URL jarURL = OwJarVerifierTest.class.getResource("clientcombridge.jar");
        OwJarVerifier jarVerifier = new OwJarVerifier(jarURL);
        X509Certificate[] signers = jarVerifier.getSignatureCertificates();
        assertFalse(0 == signers.length);

        System.err.println(signers.length);
        for (int i = 0; i < signers.length; i++)
        {
            System.err.println("==========================================");
            System.err.println(signers[i].getSubjectDN());
        }
    }
}
