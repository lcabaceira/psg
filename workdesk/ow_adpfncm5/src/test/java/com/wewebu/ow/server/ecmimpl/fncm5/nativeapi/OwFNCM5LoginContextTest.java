package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.security.auth.login.LoginException;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;

/**
 *<p>
 * OwFNCM5LoginContextTest.
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
public abstract class OwFNCM5LoginContextTest extends OwFNCM5NativeTest
{
    private static final String JAAS_CONF_WEB_SPHERE = "/jaas.conf.WebSphere";
    private static final String JAAS_CONF_WSI = "/jaas.conf.WSI";
    private static final String USER = "Administrator";
    private static final String PASSWORD = "wewebu2011";
    private static final String URI = "http://abs-fncm52.alfresco.com:9080/wsi/FNCEWS40MTOM/";
    private static final String JAAS_CONTEXT = "FileNetP8WSI";

    public void testLoginContext() throws LoginException
    {
        URL jaasConfURL = getClass().getResource(JAAS_CONF_WSI);
        Connection connection = loginContextConnection(URI, JAAS_CONTEXT, jaasConfURL, USER, PASSWORD);
        Domain defaultDomain = Factory.Domain.fetchInstance(connection, null, null);
        assertNotNull(defaultDomain);
        ObjectStoreSet objectStores = defaultDomain.get_ObjectStores();
        assertNotNull(objectStores);
        assertFalse(objectStores.isEmpty());
    }
}
