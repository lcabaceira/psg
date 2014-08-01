package test.com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.representation.Representation;

import test.com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.AlfrescoBPMNativeFixture;

import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old.TaskInstanceDataOld;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.old.TaskInstanceResourceOld;

public class TaskInstancesREST extends AlfrescoBPMNativeFixture
{
    @Ignore
    @SuppressWarnings("deprecation")
    @Test
    public void testGetTaskInstanceFromId() throws Exception
    {
        TaskInstanceResourceOld resource = restFactory.getOldAPIFactory().taskInstanceResource("264023");
        TaskInstanceDataOld result = null;
        try
        {
            result = resource.get();
        }
        finally
        {
            Representation responseEntity = resource.getClientResource().getResponseEntity();
            responseEntity.exhaust();
            responseEntity.release();
        }

        Assert.assertTrue(result.getData().isPooled());
        Assert.assertTrue(result.getData().isEditable());
        Assert.assertFalse(result.getData().isReassignable());
        Assert.assertFalse(result.getData().isClaimable());
        Assert.assertTrue(result.getData().isReleasable());
    }
}