package com.wewebu.ow.server.ecmimpl.owdummy;

import java.io.File;

import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Creates dummy objects based on their nature - file based, link, so.
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
public class OwDummyObjectFactory
{
    private static OwDummyObjectFactory instance = null;

    public static final String LINK_FILE_EXTENSION = ".lno";

    public static synchronized OwDummyObjectFactory getInstance()
    {
        if (instance == null)
        {
            instance = new OwDummyObjectFactory();
        }

        return instance;
    }

    private OwDummyObjectFactory()
    {

    }

    public OwFileObject create(OwNetwork network, File file) throws OwException
    {
        if (file.getName().endsWith(LINK_FILE_EXTENSION))
        {
            return null;
        }
        else
        {
            try
            {
                return new OwDummyFileObject(network, file);
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("", e);
            }
        }
    }

    public OwObjectLink create(OwNetwork network, OwObjectClass linkClass, String linkName, OwObject source, OwObject target)
    {
        //TODO: use link class
        return new OwDummyObjectLink(linkName, linkClass, source, target);
    }
}
