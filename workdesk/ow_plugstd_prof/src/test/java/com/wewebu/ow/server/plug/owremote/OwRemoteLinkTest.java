package com.wewebu.ow.server.plug.owremote;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import junit.framework.TestCase;

import com.wewebu.ow.server.app.OwConfiguration;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSettings;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwSecretEncryption;
import com.wewebu.ow.server.util.OwStandardOptionXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

public abstract class OwRemoteLinkTest extends TestCase
{

    protected static final String TEST_DMSID = "testDMSID";

    protected static final String TEST_MIME = "testMIME";
    protected static final String TEST_URL_PARAMTER = "testCP";
    protected static final String TEST_MIME_HOST = "http://someHost:1111";

    protected OwRemoteControlDocument legacyRemoteControlDocument;
    protected OwRemoteControlDocument remoteControlDocument;
    protected OwRemoteControlDocumentFunction remoteControlDocumentFunction;
    protected OwMainAppContext appContext;
    protected OwObject testObject;
    protected OwObjectClass testObjectClass;
    protected OwConfiguration configuration;
    protected String encrypted_TEST_DMSID;

    @Override
    protected void setUp() throws Exception
    {
        encrypted_TEST_DMSID = OwSecretEncryption.bytesToString(OwSecretEncryption.encrypt(TEST_DMSID));

        configuration = mock(OwConfiguration.class);

        InputStream xmlBootConfiguration = OwRemoteControlDocumentFunctionTest.class.getResourceAsStream("OwRemoteControlDocumentTest_boot.xml");
        InputStream xmlPluginsLegacyDocumentConfiguration = OwRemoteControlDocumentFunctionTest.class.getResourceAsStream("OwRemoteControlDocumentTest_legacy_plugins.xml");
        InputStream xmlPluginsDocumentConfiguration = OwRemoteControlDocumentFunctionTest.class.getResourceAsStream("OwRemoteControlDocumentTest_plugins.xml");
        InputStream xmlPluginsFunctionConfigConfiguration = OwRemoteControlDocumentFunctionTest.class.getResourceAsStream("OwRemoteControlDocumentFunctionTest_plugins.xml");
        InputStream xmlMIMEDoc = OwRemoteControlDocumentFunctionTest.class.getResourceAsStream("OwRemoteControlDocumentTest_mime.xml");
        OwStandardOptionXMLUtil bootConfiguration = new OwStandardOptionXMLUtil(xmlBootConfiguration, "bootstrap");
        final OwStandardOptionXMLUtil documentPluginsLegacyConfiguration = new OwStandardOptionXMLUtil(xmlPluginsLegacyDocumentConfiguration, "PlugIn");
        final OwStandardOptionXMLUtil documentPluginsConfiguration = new OwStandardOptionXMLUtil(xmlPluginsDocumentConfiguration, "PlugIn");
        OwStandardOptionXMLUtil functionPluginsConfiguration = new OwStandardOptionXMLUtil(xmlPluginsFunctionConfigConfiguration, "PlugIn");
        OwStandardOptionXMLUtil mimeConfiguration = new OwStandardOptionXMLUtil(xmlMIMEDoc, "mime");

        when(configuration.getBootstrapConfiguration()).thenReturn(bootConfiguration);
        when(configuration.getMIMENode(TEST_MIME)).thenReturn(mimeConfiguration);

        OwSettings settings = mock(OwSettings.class);
        OwHistoryManager historyManager = mock(OwHistoryManager.class);

        appContext = mock(OwMainAppContext.class);

        when(appContext.getHistoryManager()).thenReturn(historyManager);
        when(appContext.getSettings()).thenReturn(settings);
        when(appContext.getConfiguration()).thenReturn(configuration);
        when(appContext.getBaseURL()).thenReturn("http://wewebu.com:8080/workdesk");
        when(appContext.getApplicationAttribute("OwConfiguration.m_bootstrapConfiguration")).thenReturn(bootConfiguration);

        OwNetwork network = mock(OwNetwork.class);
        when(network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, "testBag", "testUser", true, true)).thenThrow(OwInvalidOperationException.class);
        when(appContext.getNetwork()).thenReturn(network);

        testObjectClass = mock(OwObjectClass.class);

        testObject = mock(OwObject.class);
        when(testObject.getObjectClass()).thenReturn(testObjectClass);
        when(testObject.getDMSID()).thenReturn(TEST_DMSID);
        when(testObject.getMIMEType()).thenReturn(TEST_MIME);
        when(testObject.getType()).thenReturn(OwObjectReference.OBJECT_TYPE_DOCUMENT);

        when(network.getObjectFromDMSID(TEST_DMSID, true)).thenReturn(testObject);

        legacyRemoteControlDocument = new OwRemoteControlDocument() {
            public OwAppContext getContext()
            {
                return appContext;
            };

            @Override
            public OwXMLUtil getConfigNode()
            {
                return documentPluginsLegacyConfiguration;
            }

        };

        remoteControlDocument = new OwRemoteControlDocument() {
            public OwAppContext getContext()
            {
                return appContext;
            };

            @Override
            public OwXMLUtil getConfigNode()
            {
                return documentPluginsConfiguration;
            }

        };

        remoteControlDocumentFunction = new OwRemoteControlDocumentFunction();
        remoteControlDocumentFunction.init(functionPluginsConfiguration, appContext);

    }

    protected String objectURL()
    {
        return TEST_MIME_HOST + "/" + OwRemoteConstants.QUERY_KEY_DMSID + "=" + TEST_DMSID;
    }
}
