package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ContentObjectModel;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Object retrieved by referential containment relationships. 
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
public class OwFNCM5ReferentialContainee extends OwFNCM5DelegateObject<IndependentObject> implements OwFNCM5Containee
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5ReferentialContainee.class);

    private OwFNCM5Object<IndependentObject> head;
    private OwFNCM5Object<ReferentialContainmentRelationship> referentialContainmentRelationship;

    public OwFNCM5ReferentialContainee(OwFNCM5Object<ReferentialContainmentRelationship> referentialContainmentRelationship_p) throws OwException
    {
        this(referentialContainmentRelationship_p, OwFNCM5DefaultObjectFactory.INSTANCE);
    }

    public OwFNCM5ReferentialContainee(OwFNCM5Object<ReferentialContainmentRelationship> referentialContainmentRelationship_p, OwFNCM5ObjectFactory objectFactory_p) throws OwException
    {
        super();
        this.referentialContainmentRelationship = referentialContainmentRelationship_p;
        ReferentialContainmentRelationship nativeRelationship = this.referentialContainmentRelationship.getNativeObject();

        IndependentObject nativeHead = nativeRelationship.get_Head();

        OwFNCM5Resource resource = this.referentialContainmentRelationship.getResource();
        OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();

        OwFNCM5Class<IndependentObject, ?> headClass = objectModel.classOf(nativeHead);

        this.head = headClass.from(nativeHead, objectFactory_p);

    }

    @Override
    protected OwFNCM5Object<?> get()
    {
        return head;
    }

    @Override
    public OwProperty getProperty(String strPropertyName_p) throws OwException
    {
        String objectNamePropertyName = OwResource.m_ObjectNamePropertyClass.getClassName();
        if (strPropertyName_p.equals(objectNamePropertyName))
        {
            return referentialContainmentRelationship.getProperty(objectNamePropertyName);
        }
        else
        {
            return super.getProperty(strPropertyName_p);
        }
    }

    @Override
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        Collection names = propertyNames_p;
        String objectNamePropertyName = OwResource.m_ObjectNamePropertyClass.getClassName();

        OwProperty objectNameProperty = null;

        if (names.contains(objectNamePropertyName))
        {
            names = new LinkedList(propertyNames_p);
            names.remove(objectNamePropertyName);

            objectNameProperty = referentialContainmentRelationship.getProperty(objectNamePropertyName);
        }

        OwPropertyCollection properties = super.getProperties(names);
        if (objectNameProperty != null)
        {
            properties.put(objectNamePropertyName, objectNameProperty);
        }

        return properties;

    }

    @Override
    public String getName()
    {
        ReferentialContainmentRelationship nativeRelationship;
        try
        {
            nativeRelationship = referentialContainmentRelationship.getNativeObject();
            return nativeRelationship.get_ContainmentName();
        }
        catch (OwException e)
        {
            LOG.error("Could not retrieve native reference object", e);
            return "N/A";
        }

    }

    public IndependentObject getNativeObject() throws OwException
    {
        return this.head.getNativeObject();
    }

    public void removeReferenceFrom(OwFNCM5Folder folder_p) throws OwException
    {
        Folder folderNative = folder_p.getNativeObject();
        ReferentialContainmentRelationship nativeRelationship = referentialContainmentRelationship.getNativeObject();
        ReferentialContainmentRelationship re = folderNative.unfile(nativeRelationship.get_ContainmentName());
        re.save(RefreshMode.NO_REFRESH);
    }

}
