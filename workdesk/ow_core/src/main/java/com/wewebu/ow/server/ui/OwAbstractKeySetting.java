package com.wewebu.ow.server.ui;

/**
 *<p>
 *Basic key setting implementation.
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
 *@since 4.1.1.0
 */
public abstract class OwAbstractKeySetting implements OwKeySetting
{
    private String id;

    public OwAbstractKeySetting(String id)
    {
        super();
        this.id = id;
    }

    @Override
    public String getId()
    {
        return id;
    }

}
