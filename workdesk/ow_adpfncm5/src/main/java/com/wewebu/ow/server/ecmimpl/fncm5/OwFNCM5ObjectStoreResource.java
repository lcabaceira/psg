package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverter;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5StoredAccessor;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * An OwResource implementation based on P8 object store.
 * Implementation which work with the native P8 object store API
 * to retrieve and handle class definitions.
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
public class OwFNCM5ObjectStoreResource extends OwFNCM5Resource
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5ObjectStoreResource.class);

    private static final String CONTENTOBJECTMODEL_CONFIG_ELEMENT = "ContentObjectModel";
    private static final String MODELBUILDER_CONFIG_ELEMENT = "ModelBuilder";

    private OwFNCM5ObjectStore objectStoreObject;

    private OwFNCM5ContentObjectModel objectModel;

    private HashMap<Integer, OwFNCM5ValueConverter> converterCache;

    public OwFNCM5ObjectStoreResource(OwFNCM5ObjectStore objectStoreObject_p, OwFNCM5Network network_p)
    {
        super(network_p);
        this.objectStoreObject = objectStoreObject_p;

        this.objectModel = createObjectModel(getNetwork());
        converterCache = new HashMap<Integer, OwFNCM5ValueConverter>();
    }

    protected OwFNCM5ContentObjectModel createObjectModel(OwFNCM5Network network_p)
    {
        OwFNCM5StoredAccessor<OwFNCM5ObjectStoreResource> resourceAccessor = new OwFNCM5StoredAccessor<OwFNCM5ObjectStoreResource>(this);
        try
        {
            OwXMLUtil config = network_p.getConfiguration();
            OwXMLUtil omConfigUtil = config.getSubUtil(CONTENTOBJECTMODEL_CONFIG_ELEMENT);
            if (omConfigUtil != null)
            {
                String modelFactoryClassName = omConfigUtil.getSafeTextValue(MODELBUILDER_CONFIG_ELEMENT, null);
                if (modelFactoryClassName != null)
                {
                    Class<?> modelFactoryClass = Class.forName(modelFactoryClassName);
                    OwFNCM5ObjectModelBuilder factory = (OwFNCM5ObjectModelBuilder) modelFactoryClass.newInstance();
                    OwFNCM5ContentObjectModel model = factory.build(network_p, resourceAccessor, omConfigUtil);
                    LOG.debug("OwFNCM5ObjectStoreResource.createObjectModel : built object model with " + modelFactoryClassName + " -> [ " + model.toString() + " ]");
                    return model;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Could not read content model configuration.", e);
        }

        LOG.debug("No or wrong object model configuration. Using default object model.");

        return new OwFNCM5CachedObjectModel(resourceAccessor);
    }

    public String getDisplayName(Locale locale_p)
    {
        //TODO localize
        return this.objectStoreObject.getName();
    }

    public String getDescription(Locale locale_p)
    {
        //TODO localize
        ObjectStore nativeObjectStore = this.objectStoreObject.getNativeObject();
        return nativeObjectStore.get_DescriptiveText();
    }

    public String getID()
    {
        ObjectStore nativeObjectStore = this.objectStoreObject.getNativeObject();
        Id nativeId = nativeObjectStore.get_Id();
        return nativeId.toString();
    }

    public OwFNCM5DMSID createDMSIDObject(String objID, String... params)
    {
        return new OwFNCM5SimpleDMSID(objID, getID());
    }

    public OwFNCM5ObjectStore getObjectStore()
    {
        return this.objectStoreObject;
    }

    public ObjectStore getNativeObjectStore()
    {
        return this.objectStoreObject.getNativeObject();
    }

    public String getName()
    {
        return this.objectStoreObject.getName();
    }

    /**
     * 
     * @return P8 Symbolic Name of the underlying object store 
     * @since 3.2.0.1
     */
    public String getSymbolicName()
    {
        return this.objectStoreObject.getSymbolicName();
    }

    @Override
    public OwFNCM5ContentObjectModel getObjectModel()
    {
        return this.objectModel;
    }

}
