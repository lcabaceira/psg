package com.wewebu.ow.server.field;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * A property collection based scope for Expression Language expressions.<br/>
 * Rules Engine for Highlighting in Hit List.
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
 *@since 3.1.0
 */
public class OwPropertiesCollectionScope extends OwPropertiesContainerScope
{

    private OwPropertyCollection propertyCollection;

    public OwPropertiesCollectionScope(String name_p, OwPropertyCollection propertyCollection_p)
    {
        super(name_p);
        this.propertyCollection = propertyCollection_p;
    }

    @Override
    protected OwProperty getProperty(String propertyName_p) throws OwException
    {
        return (OwProperty) this.propertyCollection.get(propertyName_p);
    }

}
