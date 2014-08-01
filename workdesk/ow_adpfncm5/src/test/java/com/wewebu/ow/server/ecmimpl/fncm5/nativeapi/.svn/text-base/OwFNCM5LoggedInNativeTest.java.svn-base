package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.wewebu.ow.server.ecmimpl.fncm5.unittest.log.JUnitLogger;

/**
 *<p>
 * Common Test class which will log into the test system and 
 * assign/reference some default objects.
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
@Ignore
public abstract class OwFNCM5LoggedInNativeTest extends OwFNCM5NativeTest
{
    private static final Logger LOG = JUnitLogger.getLogger(OwFNCM5LoggedInNativeTest.class);

    private static final String USER = "P8Admin";
    private static final String PASSWORD = "IBMFileNetP8";
    private static final String URI = "http://abs-fncm52.alfresco.com:9080/wsi/FNCEWS40MTOM/";
    private static final String JAAS_CONTEXT = "FileNetP8WSI";

    protected Connection connection;
    protected Domain defaultDomain;
    protected ObjectStoreSet objectStores;
    protected ObjectStore defaultObjectStore;

    @Before
    public void setUp() throws Exception
    {
        this.connection = loginConnection(URI, JAAS_CONTEXT, USER, PASSWORD);
        this.defaultDomain = Factory.Domain.fetchInstance(connection, null, null);
        LOG.info("OwFNCM5NativeAPITest.setUp : got default domain " + defaultDomain.get_Name());
        this.objectStores = defaultDomain.get_ObjectStores();

        this.defaultObjectStore = (ObjectStore) this.objectStores.iterator().next();
        LOG.info("OwFNCM5NativeAPITest.setUp : got default[#0] object store " + defaultObjectStore.get_Name());
    }

    @After
    public void tearDown() throws Exception
    {
        if (connection != null)
        {
            UserContext.get().popSubject();
        }
    }
}
