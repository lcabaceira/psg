package com.wewebu.ow.server.ecmimpl.alfresco.bpm.util;

import com.wewebu.ow.server.ecm.OwObject;

/**
 * 
 *<p>
 * Filter implementation doing nothing.
 * Will match with any object which may exist, this implementation is used
 * for OOP reasons.
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
 *@since 4.2.0.0
 */
public class NoClientSideFilter implements ClientSideFilter
{

    public static final NoClientSideFilter INSTANCE = new NoClientSideFilter();

    private NoClientSideFilter()
    {
        super();
    }

    @Override
    public boolean match(OwObject obj) throws Exception
    {
        return true;
    }

}
