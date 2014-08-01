package com.wewebu.ow.server.ecmimpl.opencmis.property;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * OwCMISProperty.
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
public interface OwCMISProperty<O> extends OwProperty, Cloneable
{

    OwFieldDefinition getFieldDefinition() throws OwException;

    Object getValue() throws OwException;

    void setValue(Object value_p) throws OwException;

    @Override
    Object getNativeObject() throws OwException;

}
