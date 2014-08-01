package com.wewebu.ow.server.ecmimpl.opencmis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.OwSQLEntitiesResolver;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwObjectIDCodeUtil;

/**
 *<p>
 * OwCMISSQLStandardEntitiesResolver.
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
public class OwCMISSQLStandardEntitiesResolver implements OwSQLEntitiesResolver
{
    private static final Logger LOG = OwLog.getLogger(OwCMISSQLStandardEntitiesResolver.class);

    private OwCMISNetwork network;

    private OwCMISExternalEntitiesResolver externalResolver = null;

    public OwCMISSQLStandardEntitiesResolver(OwCMISNetwork network_p)
    {
        this(network_p, null);
    }

    public OwCMISSQLStandardEntitiesResolver(OwCMISNetwork network_p, OwCMISExternalEntitiesResolver externalResolver_p)
    {
        super();
        this.network = network_p;
        this.externalResolver = externalResolver_p;
    }

    public Set<String> resolveQueryableColumnNames(String tableName_p, String resourceID_p) throws OwException
    {
        OwCMISResource resource = this.network.getResource(resourceID_p);
        OwCMISObjectClass objectClass = this.network.getObjectClass(tableName_p, resource);

        //only request query able properties from baseObjectType 
        Set<OwCMISPropertyClass<?>> queryableProperties = objectClass.getQueryablePropertyClasses();
        HashSet<String> queryName = new HashSet<String>();
        for (OwCMISPropertyClass<?> prop : queryableProperties)
        {
            queryName.add(prop.getQueryName());
        }
        //basically this properties must be queryable, if not still add them
        if (objectClass.getType() == OwObjectReference.OBJECT_TYPE_DOCUMENT)
        {
            if (!queryName.contains(PropertyIds.IS_LATEST_VERSION))
            {
                queryName.add(PropertyIds.IS_LATEST_VERSION);
            }
            if (!queryName.contains(PropertyIds.IS_MAJOR_VERSION))
            {
                queryName.add(PropertyIds.IS_MAJOR_VERSION);
            }
            if (!queryName.contains(PropertyIds.IS_VERSION_SERIES_CHECKED_OUT))
            {
                queryName.add(PropertyIds.IS_VERSION_SERIES_CHECKED_OUT);
            }

            if (!queryName.contains(PropertyIds.VERSION_SERIES_ID))
            {
                queryName.add(PropertyIds.VERSION_SERIES_ID);
            }

            if (!queryName.contains(PropertyIds.VERSION_SERIES_CHECKED_OUT_BY))
            {
                queryName.add(PropertyIds.VERSION_SERIES_CHECKED_OUT_BY);
            }

            if (!queryName.contains(PropertyIds.VERSION_LABEL))
            {
                queryName.add(PropertyIds.VERSION_LABEL);
            }

            if (!queryName.contains(PropertyIds.CONTENT_STREAM_MIME_TYPE))
            {
                queryName.add(PropertyIds.CONTENT_STREAM_MIME_TYPE);
            }

            if (!queryName.contains(PropertyIds.CONTENT_STREAM_ID))
            {
                queryName.add(PropertyIds.CONTENT_STREAM_ID);
            }
        }

        //the following properties must be request at least otherwise we cannot create OwObjects
        if (!queryName.contains(PropertyIds.OBJECT_ID))
        {
            queryName.add(PropertyIds.OBJECT_ID);
        }
        if (!queryName.contains(PropertyIds.OBJECT_TYPE_ID))
        {
            queryName.add(PropertyIds.OBJECT_TYPE_ID);
        }
        if (!queryName.contains(PropertyIds.OBJECT_TYPE_ID))
        {
            queryName.add(PropertyIds.BASE_TYPE_ID);
        }

        return queryName;
    }

    public String resolveQueryTableName(String tableName_p, String resourceID_p) throws OwException
    {
        OwCMISResource resource = this.network.getResource(resourceID_p);
        if (OwResource.m_ObjectNamePropertyClass.getClassName().equals(tableName_p))
        {
            return null;
        }
        else
        {
            try
            {
                OwCMISObjectClass objectClass = this.network.getObjectClass(tableName_p, resource);
                if (objectClass != null && objectClass.isQueryable())
                {
                    return objectClass.getQueryName();
                }
                else
                {
                    return null;
                }
            }
            catch (OwException e)
            {
                LOG.debug("Could not solve object class for table name " + tableName_p, e);
                return null;
            }
        }
    }

    public boolean canOrderBy(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        OwCMISPropertyClass<?> cmisPropertyClass = resolvePropertyClass(tableName_p, columnName_p, repositoryID_p);

        return cmisPropertyClass != null && cmisPropertyClass.isOrderable();
    }

    public String resolveDefaultRepositoryID() throws OwException
    {
        OwCMISResource defaultResource = this.network.getResource(null);
        return defaultResource.getID();
    }

    public String resolveQueryFolderId(String resourceID_p, String path_p) throws OwException
    {
        String path = path_p;

        if (path_p.startsWith("/" + resourceID_p))
        {
            path = path_p.substring(resourceID_p.length() + 1);
        }

        OwCMISSession session = this.network.getSession(resourceID_p);
        OwCMISObject folder = session.getObjectByPath(path, false);
        return OwObjectIDCodeUtil.decode(folder.getID());
    }

    public String resolveRepositoryID(String repositoryName_p) throws OwException
    {
        Iterator<String> resources = this.network.getResourceIDs();
        while (resources.hasNext())
        {
            String id = resources.next();
            OwCMISResource resource = this.network.getResource(id);
            String resourceRepositoryName = resource.getName();
            if (resourceRepositoryName.equals(repositoryName_p))
            {
                return id;
            }
        }
        if (this.externalResolver != null)
        {
            return this.externalResolver.resolveRepositoryID(repositoryName_p);
        }

        String prefix = this.network.getDMSPrefix() + "/";
        if (repositoryName_p.startsWith(prefix))
        {
            return resolveRepositoryID(repositoryName_p.substring(prefix.length()));
        }
        else
        {
            LOG.warn("OwCMISSQLStandardEntitiesResolver.resolveRepositoryID(): Unknown repository, name " + repositoryName_p);
            return null;
        }
    }

    public boolean isInternalRepositoryID(String repositoryID_p) throws OwException
    {
        if (repositoryID_p == null)
        {
            return false;
        }

        Iterator<String> iResources = this.network.getResourceIDs();
        while (iResources.hasNext())
        {
            String resource = iResources.next();
            if (resource.equals(repositoryID_p))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isSubtable(String parentTable_p, String childTable_p, String repositoryID_p) throws OwException
    {
        OwCMISObjectClass child = this.network.getObjectClass(childTable_p, this.network.getResource(repositoryID_p));
        OwCMISObjectClass parent = this.network.getObjectClass(parentTable_p, this.network.getResource(repositoryID_p));
        return parent.isAssignableFrom(child);
    }

    public boolean isVersionable(String tableName_p, String repositoryID_p) throws OwException
    {
        return false;
    }

    public OwNetworkContext getNetworkContext()
    {
        return network.getContext();
    }

    /**
     * 
     * @param tableName_p
     * @param columnName_p
     * @param repositoryID_p
     * @return a property class corresponding to the given parameters class model equivalents ( table - object class , column - property class ).
     *         Table definition takes precedence over the class designated by a fully qualified column name.
     *         For non fully qualified column names that are not defined by table object class null is returned.            
     * @throws OwException
     * @since 4.1.1.1
     */
    protected OwCMISPropertyClass<?> resolvePropertyClass(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        OwCMISSession session = this.network.getSession(repositoryID_p);
        OwCMISObjectClass tableObjectClass = session.getObjectClass(tableName_p);
        OwCMISQualifiedName qualifiedName = new OwCMISQualifiedName(columnName_p);

        OwCMISPropertyClass<?> cmisPropertyClass = tableObjectClass.findPropertyClass(qualifiedName.getName());

        if (cmisPropertyClass == null)
        {
            String className = qualifiedName.getNamespace();
            String propName = qualifiedName.getName();

            if (null == className)
            {
                return null;
            }

            OwCMISObjectClass columnObjectClass = session.getObjectClass(className);
            cmisPropertyClass = columnObjectClass.getPropertyClass(propName);
        }

        return cmisPropertyClass;
    }

    @Override
    public String resovleQueryColumnName(String tableName_p, String columnName_p, String repositoryID_p) throws OwException
    {
        if (isInternalRepositoryID(repositoryID_p))
        {
            OwCMISPropertyClass<?> cmisPropertyClass = resolvePropertyClass(tableName_p, columnName_p, repositoryID_p);

            if (cmisPropertyClass == null)
            {
                return columnName_p;
            }

            if (cmisPropertyClass.isQueryable())
            {
                String qName = cmisPropertyClass.getQueryName();
                if (qName == null)
                {
                    return cmisPropertyClass.getNonQualifiedName();
                }
                else
                {
                    return qName;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return columnName_p;
        }
    }

}