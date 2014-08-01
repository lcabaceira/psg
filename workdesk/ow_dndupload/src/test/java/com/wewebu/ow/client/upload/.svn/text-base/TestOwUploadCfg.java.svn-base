package com.wewebu.ow.client.upload;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 *<p>
 * TestOwUploadCfg.
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
public class TestOwUploadCfg
{
    @Test
    public void testConfigurationBuilde() throws Exception
    {
        OwAbstractCfg params_p = new OwAbstractCfg() {

            @Override
            public boolean has(String name_p)
            {
                return false;
            }

            @Override
            public String getString(String name_p)
            {
                return "";
            }
        };
        OwAbstractCfg cfgProperties_p = new OwAbstractCfg() {
            private Map<String, String> back = new HashMap<String, String>();
            {
                back.put(OwAppletParams.PARAM_UPLOAD_URL, "foo");
            }

            @Override
            public boolean has(String name_p)
            {
                return back.containsKey(name_p);
            }

            @Override
            public String getString(String name_p)
            {
                return back.get(name_p);
            }
        };
        try
        {
            new OwUploadCfg(params_p, cfgProperties_p);
            Assert.fail("An " + OwUploadCfgPropertyException.class + " should have been thrown!");
        }
        catch (OwUploadCfgPropertyException e)
        {
            //OK
        }
    }
}
