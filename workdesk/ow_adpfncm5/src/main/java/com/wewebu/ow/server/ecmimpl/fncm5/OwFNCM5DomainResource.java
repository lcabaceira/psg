package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory.ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Domain;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * P8 5.0 content object model defines non {@link ObjectStore} entities (like the {@link Domain}s themselves).
 * The domain-resource models a FNCM-Domain level entities defining resource. 
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
public class OwFNCM5DomainResource extends OwFNCM5Resource implements OwFNCM5ContentObjectModel
{
    public static final String DOMAIN_RESOURCE_NAME = "P8 5.0 Domain Resource";

    public static final String DOMAIN_RESOURCE_ID = "DR";

    private OwFNCM5Domain domain;

    public OwFNCM5DomainResource(OwFNCM5Domain domain_p, OwFNCM5Network network_p) throws OwException
    {
        super(network_p);
        this.domain = domain_p;

    }

    public String getDisplayName(Locale locale_p)
    {
        // TODO localize
        return DOMAIN_RESOURCE_NAME;
    }

    public String getDescription(Locale locale_p)
    {
        // TODO localize
        return DOMAIN_RESOURCE_NAME;
    }

    public String getID()
    {
        return domain.getID();
    }

    public OwFNCM5DMSID createDMSIDObject(String objID_p, String... params_p)
    {
        return new OwFNCM5SimpleDMSID(objID_p, getID());
    }

    public OwFNCM5Domain getDomainObject()
    {
        return this.domain;
    }

    /**
     * 
     * @return list of {@link OwFNCM5ObjectStore}s defined by this domain
     * @throws OwException
     */
    public List<OwFNCM5ObjectStore> getObjectStores() throws OwException
    {
        return this.domain.getObjectStores();
    }

    @Override
    public OwFNCM5ContentObjectModel getObjectModel()
    {
        return this;
    }

    public OwFNCM5PropertyClass propertyClassForName(String propertyClassName_p) throws OwException
    {
        notSupported();
        return null;
    }

    public OwFNCM5Class<?, ?> objectClassForName(String objectClassName_p) throws OwException
    {
        notSupported();
        return null;
    }

    private void notSupported() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("Not supported by " + getClass());
    }

    public Map<String, String> objectClassNamesWith(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {
        notSupported();
        return null;
    }

    public <N> OwFNCM5Class<N, ?> classOf(N nativeObject_p) throws OwException
    {
        notSupported();
        return null;
    }

}
