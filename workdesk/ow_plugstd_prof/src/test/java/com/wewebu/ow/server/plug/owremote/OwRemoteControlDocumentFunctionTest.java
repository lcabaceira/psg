package com.wewebu.ow.server.plug.owremote;

import static org.mockito.Mockito.verify;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.paramcodec.OwParameterMap;
import com.wewebu.ow.server.util.paramcodec.OwTransientCodec;

public class OwRemoteControlDocumentFunctionTest extends OwRemoteLinkTest
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwRemoteControlDocumentFunctionTest.class);

    private static final String VIEWCRYPTX = "viewcryptX";
    private static final String CTRLEVX = "ctrlevX";
    private static final String VIEWX = "viewX";

    public void testCreateViewEventURL() throws Exception
    {
        OwTransientCodec codec = OwTransientCodec.createConfiguredCodec(appContext);
        final String codecParameter = codec.peekAtNextURLParameter();

        remoteControlDocumentFunction.onClickEvent(testObject, null, null);

        final String expectedEncodedURL = OwAppContext.encodeURL("\nhttp://wewebu.com:8080/workdesk?owappeid=com.wewebu.ow.RemoteControl.Doc&" + TEST_URL_PARAMTER + "=" + codecParameter + "\n\n");
        //first comment is size of result
        final String expectedFinalScript = "//222\nself.location.href = 'mailto:?subject=null&body=null" + OwHTMLHelper.encodeJavascriptString(expectedEncodedURL) + "';";
        LOG.debug("Checking final script:" + expectedFinalScript);
        verify(appContext).addFinalScript(expectedFinalScript);

        OwParameterMap parameterMap = new OwParameterMap();

        parameterMap.setParameter(TEST_URL_PARAMTER, codecParameter);
        OwParameterMap decodedParameterMap = codec.decode(parameterMap, false);
        assertNotNull(decodedParameterMap.getParameter(CTRLEVX));

    }

}
