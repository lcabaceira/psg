package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.definitions.PropertyIdDefinition;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISPreferredPropertyTypeCfg.PropertyType;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Helper for Aspects handling during creation process.
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
public class OwCMISAlfrescoSkeletonObjectClass implements OwCMISObjectClass
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAlfrescoSkeletonObjectClass.class);

    private OwCMISAbstractAlfrescoClass<ObjectType, TransientCmisObject> objectClass;

    public OwCMISAlfrescoSkeletonObjectClass(OwCMISAbstractAlfrescoClass<ObjectType, TransientCmisObject> objectClass)
    {
        super();
        this.objectClass = objectClass;
    }

    @Override
    public int getType()
    {
        return this.objectClass.getType();
    }

    @Override
    public List getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        return this.objectClass.getChilds(network_p, fExcludeHiddenAndNonInstantiable_p);
    }

    @Override
    public Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        return this.objectClass.getChildNames(network_p, fExcludeHiddenAndNonInstantiable_p);
    }

    @Override
    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception
    {
        return this.objectClass.hasChilds(network_p, fExcludeHiddenAndNonInstantiable_p, context_p);
    }

    @Override
    public String getClassName()
    {
        try
        {
            return getCopyClassName();
        }
        catch (Exception ex)
        {
            return this.objectClass.getClassName();
        }
    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return this.objectClass.getDisplayName(locale_p);
    }

    @Override
    public OwCMISPropertyClass<?> getPropertyClass(String strClassName_p) throws OwException
    {
        return this.objectClass.getPropertyClass(strClassName_p);
    }

    @Override
    public Collection<String> getPropertyClassNames() throws OwException
    {
        return this.objectClass.getPropertyClassNames();
    }

    @Override
    public String getNamePropertyName() throws OwException
    {
        return this.objectClass.getNamePropertyName();
    }

    @Override
    public boolean canCreateNewObject() throws OwException
    {
        return this.objectClass.canCreateNewObject();
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return this.objectClass.hasVersionSeries();
    }

    @Override
    public List<?> getModes(int operation_p) throws Exception
    {
        return this.objectClass.getModes(operation_p);
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return this.objectClass.getDescription(locale_p);
    }

    @Override
    public boolean isHidden() throws Exception
    {
        return this.objectClass.isHidden();
    }

    @Override
    public OwCMISObjectClass getParent()
    {
        return this.objectClass.getParent();
    }

    @Override
    public String getMimetype()
    {
        return this.objectClass.getMimetype();
    }

    @Override
    public Map<String, OwCMISVirtualPropertyClass<?>> getVirtualPropertyClasses(boolean localOnly_p)
    {
        return this.objectClass.getVirtualPropertyClasses(localOnly_p);
    }

    @Override
    public OwCMISVirtualPropertyClass<?> getVirtualPropertyClass(String strClassName_p)
    {
        return this.objectClass.getVirtualPropertyClass(strClassName_p);
    }

    @Override
    public OwCMISPropertyClass<?> findPropertyClass(String className_p)
    {
        return this.objectClass.findPropertyClass(className_p);
    }

    @Override
    public Map<String, OwCMISPropertyClass<?>> getPropertyClasses() throws OwException
    {
        return this.objectClass.getPropertyClasses();
    }

    @Override
    public boolean isContentRequired() throws OwException
    {
        return this.objectClass.isContentRequired();
    }

    @Override
    public boolean isQueryable()
    {
        return this.objectClass.isQueryable();
    }

    @Override
    public String getQueryName()
    {
        return this.objectClass.getQueryName();
    }

    @Override
    public String createNewObject(boolean promote_p, Object mode_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p, String strMimeParameter_p,
            boolean keepCheckedOut_p) throws OwException
    {
        return this.objectClass.createNewObject(promote_p, mode_p, properties_p, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, keepCheckedOut_p);
    }

    @Override
    public Set<OwCMISPropertyClass<?>> getQueryablePropertyClasses() throws OwException
    {
        return this.objectClass.getQueryablePropertyClasses();
    }

    @Override
    public void subclassedBy(OwCMISObjectClass subclass_p) throws OwInvalidOperationException
    {
        this.objectClass.subclassedBy(subclass_p);
    }

    @Override
    public boolean isAssignableFrom(OwCMISObjectClass class_p) throws OwException
    {
        return this.objectClass.isAssignableFrom(class_p);
    }

    @Override
    public PropertyType getPreferredPropertyType(OwCMISPropertyClass<?> propertyClass) throws OwException
    {
        return this.objectClass.getPreferredPropertyType(propertyClass);
    }

    @Override
    public OwObjectSkeleton createSkeletonObject(OwNetwork network_p, OwCMISResource res_p, OwCMISNativeSession session_p, OwXMLUtil initValues_p) throws Exception
    {
        return this.objectClass.createSkeletonObject(network_p, res_p, session_p, initValues_p);
    }

    @Override
    public String getCopyClassName()
    {
        StringBuilder build = new StringBuilder(this.objectClass.getClassName());
        ObjectType nativeObject = this.objectClass.getNativeObject();
        if (nativeObject.getPropertyDefinitions() != null)
        {
            PropertyIdDefinition prop = (PropertyIdDefinition) nativeObject.getPropertyDefinitions().get(OwCMISAspectsPropertyDefinition.ID);
            List<String> lst = prop.getDefaultValue();
            if (lst != null)
            {
                for (String aspectId : lst)
                {
                    build.append(",");
                    build.append(aspectId);
                }
            }
        }

        return build.toString();
    }

}