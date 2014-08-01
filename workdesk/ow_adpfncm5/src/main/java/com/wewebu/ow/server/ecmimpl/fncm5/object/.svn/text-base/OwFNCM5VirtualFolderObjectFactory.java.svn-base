package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwSemiVirtualFolderAdapter;
import com.wewebu.ow.server.ecm.OwStandardVirtualFolderObjectFactory;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;

/**
 *<p>
 * OwFNCM5VirtualFolderObjectFactory.
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
public class OwFNCM5VirtualFolderObjectFactory extends OwStandardVirtualFolderObjectFactory
{

    protected OwObject getSearchPathObject() throws Exception
    {
        //try to retrieve the search path ...
        OwSearchTemplate template = getSearchTemplate();
        OwSearchNode theSearchNode = template.getSearch(false);

        OwSearchNode specialNode = theSearchNode.findSearchNode(OwSearchNode.NODE_TYPE_SPECIAL);

        if (specialNode != null)
        {

            List specialChildren = specialNode.getChilds();
            OwSearchNode virtualFolderPathNode = null;
            for (Iterator k = specialChildren.iterator(); k.hasNext();)
            {
                OwSearchNode specialChild = (OwSearchNode) k.next();
                OwSearchCriteria specialCriteria = specialChild.getCriteria();
                String criteriaUName = specialCriteria.getUniqueName();
                String criteriaClassName = specialCriteria.getClassName();
                if (OwSemiVirtualFolderAdapter.VIRTUAL_FOLDER_SEARCH_PATH_PROPERTY.equals(criteriaUName) && OwSearchPathField.CLASS_NAME.equals(criteriaClassName))
                {
                    virtualFolderPathNode = specialChild;
                    break;
                }
            }
            if (virtualFolderPathNode != null)
            {
                OwSearchCriteria virtualFolderPathCriteria = virtualFolderPathNode.getCriteria();
                OwSearchPath virtualFolderPath = (OwSearchPath) virtualFolderPathCriteria.getValue();
                OwSearchObjectStore virtualFolderSearchStore = virtualFolderPath.getObjectStore();

                String osIdentification = virtualFolderSearchStore.getId();
                if (osIdentification == null)
                {
                    osIdentification = virtualFolderSearchStore.getName();
                }
                //                OwResource resource = m_repository.getResource(osIdentification);
                String pathName = virtualFolderPath.getPathName();
                String path = "/" + osIdentification + pathName;
                OwObject pathObject = m_repository.getObjectFromPath(path, true);
                return pathObject;
            }
        }

        return null;
    }

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public Object getNativeObject() throws Exception
    {
        OwObject pathObject = getSearchPathObject();
        if (pathObject != null)
        {
            return pathObject.getNativeObject();
        }
        else
        {
            return null;
        }
    }

    @Override
    protected OwStandardVirtualFolderObjectFactory createNewSubfolderInstance() throws Exception
    {
        return new OwFNCM5VirtualFolderObjectFactory();
    }

}
