package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.filenet.api.core.Folder;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolder;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwSemiVirtualRecordClass;
import com.wewebu.ow.server.ecm.OwStandardSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5FolderClass;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * The P8 5.0 semi-virtual folder implementation.
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
public class OwFNCM5SemiVirtualFolder extends OwFNCM5Folder implements OwSemiVirtualFolder
{

    private static final Logger LOG = OwLog.getLogger(OwFNCM5SemiVirtualFolder.class);

    /**
     *<p>
     * OwFNCM5SemiVirtualFolderEntry.
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
     *@deprecated since 4.0.0.0 use {@link OwSemiVirtualRecordClass}
     */
    public static class OwFNCM5SemiVirtualFolderEntry
    {
        private String m_sVirtualFolder;
        private String m_sFolderClassName;
        private boolean m_fIncludePhysicalChilds;
        private Map m_propertyMapping;

        public OwFNCM5SemiVirtualFolderEntry(Node n_p) throws OwConfigurationException
        {
            m_sVirtualFolder = OwXMLDOMUtil.getSafeStringAttributeValue(n_p, "virtualfolder", null);
            if (null == m_sVirtualFolder)
            {
                String msg = "OwFNCM5SemiVirtualFolder$OwFNCM5SemiVirtualFolderEntry: Please define \"virtualfolder\" attribute for SemiVirtualRecordClassName node in the owbootstrap.xml.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            m_sFolderClassName = OwXMLDOMUtil.getSafeStringAttributeValue(n_p, "folderclassname", null);
            if (null == m_sFolderClassName)
            {
                String msg = "OwFNCM5SemiVirtualFolder$OwFNCM5SemiVirtualFolderEntry: Please define \"folderclassname\" attribute for SemiVirtualRecordClassName node in the owbootstrap.xml.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            m_fIncludePhysicalChilds = OwXMLDOMUtil.getSafeBooleanAttributeValue(n_p, "includephysicalchilds", false);

            // create map of property mappings keyed by the virtual folder property
            m_propertyMapping = new HashMap();

            Collection propertymappings = OwXMLDOMUtil.getSafeNodeList(n_p);

            Iterator it = propertymappings.iterator();
            while (it.hasNext())
            {
                Node vfProperty = (Node) it.next();

                // add to map
                if (null != vfProperty.getFirstChild())
                {
                    m_propertyMapping.put(vfProperty.getFirstChild().getNodeValue(), OwXMLDOMUtil.getSafeStringAttributeValue(vfProperty, "folderproperty", null));
                }
            }

        }

        public String getFolderClassName()
        {
            return m_sFolderClassName;
        }

        public String getVirtualFolder()
        {
            return m_sVirtualFolder;
        }

        public boolean getIsIncludePhysicalChilds()
        {
            return m_fIncludePhysicalChilds;
        }

        public Map getPropertyMapping()
        {
            return m_propertyMapping;
        }
    }

    /**
     * The semi virtual folder entry configured in owbootstrap.xml
     */
    protected OwSemiVirtualRecordClass semiVirtualEntry;

    private OwSemiVirtualFolderAdapter adapter;

    public OwFNCM5SemiVirtualFolder(Folder nativeObject_p, OwFNCM5FolderClass clazz_p, OwSemiVirtualRecordClass semiVirtualEntry_p) throws OwException
    {
        super(nativeObject_p, clazz_p);

        this.semiVirtualEntry = semiVirtualEntry_p;
        OwFNCM5ObjectStoreResource resource = clazz_p.getResource();
        OwFNCM5Network network = resource.getNetwork();
        this.adapter = new OwStandardSemiVirtualFolderAdapter(network);
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        return this.adapter.getChildren(this, iObjectTypes_p, propertyNames_p, sort_p, iMaxSize_p, iVersionSelection_p, filterCriteria_p);
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        boolean childrenPresent = false;

        if (iObjectTypes_p != null)
        {
            for (int i = 0; i < iObjectTypes_p.length; i++)
            {
                if (OwObjectReference.OBJECT_TYPE_FOLDER == iObjectTypes_p[i] || OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS == iObjectTypes_p[i])
                {
                    childrenPresent = true;
                    break;
                }
            }
        }

        if (this.semiVirtualEntry.isIncludePhysicalChilds())
        {
            childrenPresent = childrenPresent || super.hasChilds(iObjectTypes_p, iContext_p);
        }

        return childrenPresent;
    }

    public OwSearchTemplate getSearchTemplate() throws OwException
    {
        OwVirtualFolderObject virtualFolder = this.adapter.getVirtualFolder(this);
        try
        {
            return virtualFolder.getSearchTemplate();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve virtual folder search template!", e);
        }
    }

    public OwObjectCollection getPhysicalChildren(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        try
        {
            return super.getChilds(objectTypes_p, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwFNCMSemiVirtualFolderObject.getPhysicalChildren():Could not retrieve the semi-virtual-folder physical children!", e);
            throw new OwInvalidOperationException(new OwString("fncm.OwFNCMSemiVirtualFolderObject.physical.error", "Could not retrieve the semi-virtual-physical children!"), e);
        }
    }

    public Map getPropertyMap()
    {
        return this.semiVirtualEntry.getPropertyMapping();
    }

    public String getVirtualFolderName()
    {
        return this.semiVirtualEntry.getVirtualFolder();
    }

    public boolean includesPhysicalChildren()
    {
        return this.semiVirtualEntry.isIncludePhysicalChilds();
    }

    public boolean searchSubstructure()
    {
        return semiVirtualEntry.isSearchSubstructure();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwSemiVirtualFolder#getPhysicalChildren(com.wewebu.ow.server.collections.OwLoadContext)
     */
    @Override
    public OwIterable<OwObject> getPhysicalChildren(OwLoadContext loadContext) throws OwException
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("Not implemented yet!");
    }

}
