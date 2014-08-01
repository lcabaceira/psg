package com.wewebu.ow.server.ecmimpl.fncm5.conf;

import com.wewebu.ow.server.ecm.OwUserInfo;

/**
 *<p>
 * P8 5.0 base objects factory based UserInfo.  
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
public abstract class OwFNCM5UserInfo implements OwUserInfo
{
    private OwFNCM5UserInfoFactory infoFactory;

    public OwFNCM5UserInfo(OwFNCM5UserInfoFactory infoFactory_p)
    {
        super();
        this.infoFactory = infoFactory_p;
    }

    protected final OwFNCM5UserInfoFactory getInfoFactory()
    {
        return infoFactory;
    }
}
