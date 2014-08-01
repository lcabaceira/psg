package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISPreferredPropertyTypeCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISPreferredPropertyTypeCfg.PropertyType;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstract session based OwObjectClass implementation.
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
public abstract class OwCMISAbstractSessionObjectClass<S extends OwCMISSession> extends OwCMISAbstractObjectClass implements OwCMISSessionObjectClass<S>
{

    private S session;

    public OwCMISAbstractSessionObjectClass(S session)
    {
        super();
        this.session = session;
    }

    public S getSession()
    {
        return session;
    }

    @Override
    public Map<String, OwCMISObjectClass> getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        List<OwCMISObjectClass> lst = getChilds(network_p, fExcludeHiddenAndNonInstantiable_p);
        if (lst != null)
        {
            Map<String, OwCMISObjectClass> map = new HashMap<String, OwCMISObjectClass>();
            for (OwCMISObjectClass child : lst)
            {
                map.put(child.getClassName(), child);
            }
            return map;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception
    {
        List<OwCMISObjectClass> lst = getChilds(network_p, fExcludeHiddenAndNonInstantiable_p);
        return lst != null && !lst.isEmpty();
    }

    @Override
    public PropertyType getPreferredPropertyType(OwCMISPropertyClass<?> propertyClass) throws OwException
    {
        OwCMISNetwork netowrk = session.getNetwork();
        OwCMISNetworkCfg configuration = netowrk.getNetworkConfiguration();
        OwCMISPreferredPropertyTypeCfg preferredPropertyTypeConfiguration = configuration.getPreferredPropertyTypeCfg();
        return preferredPropertyTypeConfiguration.getPreferredType(propertyClass);
    }
}
