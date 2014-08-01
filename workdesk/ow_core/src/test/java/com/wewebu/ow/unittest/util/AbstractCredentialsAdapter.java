package com.wewebu.ow.unittest.util;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.OwAbstractCredentials;

/**
 *<p>
 * Simple adapter class to for creation of tests.
 * Returning valid values by default, but still
 * the derived class should implement the needed methods.
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
public abstract class AbstractCredentialsAdapter extends OwAbstractCredentials
{

    public OwUserInfo getUserInfo() throws Exception
    {
        return new OwTestUserInfo(this.getClass().toString());
    }

    public void invalidate() throws Exception
    {

    }

}
