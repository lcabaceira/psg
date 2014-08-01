package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import org.junit.Assert;
import org.junit.Test;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;

/**
 *<p>
 * OwFNBPM5UserPropertyTest. 
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
public class OwFNBPM5UserPropertyTest
{
    @Test
    public void testEqualsNull() throws Exception
    {
        OwPropertyClass propclass = new OwStandardPropertyClass();
        long userid = 100;
        OwFNBPM5UserProperty property = new OwFNBPM5UserProperty(null, propclass, userid);

        Assert.assertFalse(property.equals(null));
    }
}
