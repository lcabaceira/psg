package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import junit.framework.Assert;

import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.OwAlfrescoBPMRepository;

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
public class TestAlfrescoBPMRepository extends AlfrescoBPMRepositoryFixture
{
    public TestAlfrescoBPMRepository(String name) throws Exception
    {
        super(name);
    }

    public void testGetWorkItemInterface() throws Exception
    {
        OwWorkitemRepository owWorkitemRepository = (OwWorkitemRepository) this.getNetwork().getInterface(OwWorkitemRepository.class.getName(), null);
        Assert.assertTrue(owWorkitemRepository instanceof OwAlfrescoBPMRepository);
    }

    public void testBaseURL() throws Exception
    {
        OwAlfrescoBPMRepository owWorkitemRepository = (OwAlfrescoBPMRepository) this.getNetwork().getInterface(OwWorkitemRepository.class.getName(), null);
        Assert.assertEquals("http://abs-alfone.alfresco.com:8080/alfresco", owWorkitemRepository.getBaseURL());
    }
}