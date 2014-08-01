package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import java.util.Calendar;
import java.util.Locale;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.alfresco.wd.ext.restlet.auth.OwRestletBasicAuthenticationHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoRESTFulFactory;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverterFactoryRest;

/**
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
public class AlfrescoBPMNativeFixture
{
    protected static final String BASE_URI = "http://abs-alfone.alfresco.com:8080/alfresco";

    protected static AlfrescoRESTFulFactory restFactory;

    @BeforeClass
    public static void classSetup() throws Exception
    {
        NativeValueConverterFactoryRest converterFactory = new NativeValueConverterFactoryRest(Calendar.getInstance().getTimeZone());
        OwRestletAuthenticationHandler authHandler = new OwRestletBasicAuthenticationHandler("admin", "admin");
        restFactory = new AlfrescoRESTFulFactory(BASE_URI, authHandler, new Locale("UTF-8"), converterFactory);
    }

    @AfterClass
    public static void classTearDown() throws Exception
    {
        restFactory.release();
    }
}
