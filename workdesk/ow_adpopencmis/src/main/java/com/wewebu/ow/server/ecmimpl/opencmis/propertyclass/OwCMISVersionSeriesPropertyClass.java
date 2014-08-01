package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.Arrays;
import java.util.List;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISStoredVirtualPropertyImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISVirtualProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * {@link OwResource#m_VersionSeriesPropertyClass} virtual property class.
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
public class OwCMISVersionSeriesPropertyClass extends OwCMISAbstractStoredPropertyClass<OwVersionSeries, OwCMISObjectClass>
{
    private static final List<Integer> OPERATORS = Arrays.asList(OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL);

    public OwCMISVersionSeriesPropertyClass(OwCMISObjectClass objectClass_p)
    {
        super(OwResource.m_VersionSeriesPropertyClass.getClassName(), OwResource.m_VersionSeriesPropertyClass, OPERATORS, objectClass_p);
    }

    @Override
    public OwCMISVirtualProperty<OwVersionSeries> from(OwCMISObject object_p) throws OwException
    {
        try
        {
            return new OwCMISStoredVirtualPropertyImpl<OwVersionSeries>(this, object_p, object_p.getVersionSeries());
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            OwString message = new OwString1("", "Could not get version series!");
            throw new OwServerException(message, e);
        }
    }
}
