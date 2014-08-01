package com.wewebu.ow.server.plug.owremote;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class OwRemoteControlDocumentTest extends OwRemoteLinkTest
{

    private static final String VIEWCRYPTX = "viewcryptX";
    private static final String CTRLEVX = "ctrlevX";
    private static final String VIEWX = "viewX";

    public void testLegacyExternalCryptRequest() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "http://wewebu.com:8080/workdesk");
        request.setParameter(OwRemoteConstants.QUERY_KEY_EVENT_NAME, OwRemoteConstants.CONTROL_EVENT_VIEW_CRYPT);
        request.setParameter(OwRemoteConstants.QUERY_KEY_DMSID, encrypted_TEST_DMSID);
        MockHttpServletResponse response = new MockHttpServletResponse();

        legacyRemoteControlDocument.onExternalRequest(request, response);
        assertEquals(objectURL(), response.getRedirectedUrl());
    }

    public void testLegacyExternalRequest() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "http://wewebu.com:8080/workdesk");
        request.setParameter(OwRemoteConstants.QUERY_KEY_EVENT_NAME, OwRemoteConstants.CONTROL_EVENT_VIEW);
        request.setParameter(OwRemoteConstants.QUERY_KEY_DMSID, TEST_DMSID);
        MockHttpServletResponse response = new MockHttpServletResponse();

        legacyRemoteControlDocument.onExternalRequest(request, response);
        assertEquals(objectURL(), response.getRedirectedUrl());
    }

    public void testExternalRequest() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "http://wewebu.com:8080/workdesk");
        request.setParameter(CTRLEVX, VIEWX);
        request.setParameter(OwRemoteConstants.QUERY_KEY_DMSID, TEST_DMSID);
        MockHttpServletResponse response = new MockHttpServletResponse();

        remoteControlDocument.onExternalRequest(request, response);
        assertEquals(objectURL(), response.getRedirectedUrl());
    }

    public void testCryptExternalRequest() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "http://wewebu.com:8080/workdesk");
        request.setParameter(CTRLEVX, VIEWCRYPTX);
        request.setParameter(OwRemoteConstants.QUERY_KEY_DMSID, encrypted_TEST_DMSID);
        MockHttpServletResponse response = new MockHttpServletResponse();

        remoteControlDocument.onExternalRequest(request, response);
        assertEquals(objectURL(), response.getRedirectedUrl());
    }

}
