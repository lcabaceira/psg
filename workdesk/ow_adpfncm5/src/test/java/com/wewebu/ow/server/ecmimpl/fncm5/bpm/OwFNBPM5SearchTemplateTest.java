package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.util.OwTimeZoneInfo;
import com.wewebu.ow.unittest.util.AbstractNetworkAdapter;
import com.wewebu.ow.unittest.util.AbstractNetworkContextAdapter;

/**
 *<p>
 * FNBPM Test Case: Test BPM SearchTemplates which are used also for 
 * CrossQueue querying.
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
public class OwFNBPM5SearchTemplateTest
{

    File refHelper;
    private static final Logger LOG = Logger.getLogger(OwFNBPM5SearchTemplateTest.class);

    @Before
    public void setUp() throws Exception
    {
        if (refHelper == null)
        {
            URL url = this.getClass().getResource("/resources/fncm/bpm/CrossA.xml");
            refHelper = new File(url.getFile());
            LOG.debug("OwFNBPM5SearchTemplateTest.setUp(): Can read file " + refHelper.canRead());
            LOG.debug("OwFNBPM5SearchTemplateTest.setUp(): " + refHelper.exists());
        }
    }

    @Test
    public void testInstantiation()
    {
        EmptyNetworkContext network = new EmptyNetworkContext();

        OwFileObject crossDef;
        try
        {
            crossDef = new OwFileObject(new EmptyNetwork(), refHelper, "Cross A.xml", "text/xml");
        }
        catch (Exception e1)
        {
            LOG.error("Could not create OwFileObject from resource!", e1);
            fail("Could not create OwFileObject from given resource! " + e1.getMessage());
            return;
        }

        try
        {
            new OwFNBPM5SearchTemplate(network, crossDef);
        }
        catch (Exception e)
        {
            LOG.error("Could not instantiate OwFNBPM5SearchTemplate!", e);
            fail("Could not parse searchtemplate for BPM Cross Queue! " + e.getMessage());
        }
    }

    @Test
    public void testGetColumInfoBeforeInit() throws Exception
    {
        EmptyNetworkContext network = new EmptyNetworkContext();

        OwFileObject crossDef = new OwFileObject(new EmptyNetwork(), refHelper, "Cross A.xml", "text/xml");
        OwFNBPM5SearchTemplate template = new OwFNBPM5SearchTemplate(network, crossDef);

        template.getColumnInfoList();
    }

    @Test
    public void testInit() throws Exception
    {
        EmptyNetwork network = new EmptyNetwork();

        OwFileObject crossDef = new OwFileObject(network, refHelper, "Cross A.xml", "text/xml");
        OwFNBPM5SearchTemplate template = new OwFNBPM5SearchTemplate(new EmptyNetworkContext(), crossDef);

        template.init(network);
    }

    @Test
    public void testGetColumnInfoAfterInit() throws Exception
    {
        EmptyNetwork network = new EmptyNetwork();

        OwFileObject crossDef = new OwFileObject(network, refHelper, "Cross A.xml", "text/xml");
        OwFNBPM5SearchTemplate template = new OwFNBPM5SearchTemplate(new EmptyNetworkContext(), crossDef);

        template.init(network);
        template.getColumnInfoList();
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetReassignContainerNames() throws Exception
    {
        EmptyNetwork network = new EmptyNetwork();

        OwFileObject crossDef = new OwFileObject(network, refHelper, "Cross A.xml", "text/xml");
        OwFNBPM5SearchTemplate template = new OwFNBPM5SearchTemplate(new EmptyNetworkContext(), crossDef);

        template.init(network);
        Collection col = template.getReassignContainerNames();

        assertTrue("Containers names are not empty", col == null);
    }

    private static class EmptyNetworkContext extends AbstractNetworkContextAdapter
    {

        public OwTimeZoneInfo getClientTimeZoneInfo()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public TimeZone getClientTimeZone()
        {
            // TODO Auto-generated method stub
            return TimeZone.getDefault();
        }

    }

    private static class EmptyNetwork extends AbstractNetworkAdapter<OwObject>
    {
    }
}