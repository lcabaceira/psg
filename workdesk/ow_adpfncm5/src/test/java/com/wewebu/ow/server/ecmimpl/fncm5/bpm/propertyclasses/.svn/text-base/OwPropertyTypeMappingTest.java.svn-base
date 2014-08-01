package com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5ParticipantProperty;

import filenet.vw.api.VWFieldType;

/**
 *<p>
 * OwPropertyTypeMappingTest.
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
public class OwPropertyTypeMappingTest
{
    @Test
    public void testMapping() throws Exception
    {
        String participantFieldClass = OwPropertyTypeMapping.getJavaClassName(VWFieldType.FIELD_TYPE_PARTICIPANT);
        assertEquals(OwFNBPM5ParticipantProperty.class.getName(), participantFieldClass);
    }
}
