package com.wewebu.ow.server.ecmimpl.fncm5.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwSearchObjectStore;
import com.wewebu.ow.server.ecm.OwSearchPath;
import com.wewebu.ow.server.ecm.OwSearchPathField;
import com.wewebu.ow.server.ecm.OwStandardClassSelectObject;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * FileNet P8 5 Content Manager Searchtemplate implementation. <br/>
 * Extends the standard implementation, for other resource handling.
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
public class OwFNCM5SearchTemplate extends OwStandardSearchTemplate
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5SearchTemplate.class);
    protected static final String ATTRIBUTE_INSTRUCTION = "instruction";

    public OwFNCM5SearchTemplate(OwNetworkContext context_p, Node xmlSearchTemplateNode_p, String strName_p, String strResourceName_p) throws Exception
    {
        super(context_p, xmlSearchTemplateNode_p, strName_p, strResourceName_p);
    }

    @Deprecated
    /**
     * This constructor is deprecated and can lead to an exception, please use
     * the {@link #OwFNCM5SearchTemplate(OwNetworkContext, OwObject)} constructor.
     * @param context_p
     * @param obj_p
     * @param useSearchPaths_p
     * @throws Exception
     * @deprecated since 4.0.0.0 use {@link #OwFNCM5SearchTemplate(OwNetworkContext, OwObject)} constructor
     */
    public OwFNCM5SearchTemplate(OwNetworkContext context_p, OwObject obj_p, boolean useSearchPaths_p) throws Exception
    {
        super(context_p, obj_p, useSearchPaths_p);
    }

    /**
     * Constructor for search template. 
     * @param context_p
     * @param obj_p
     * @throws Exception
     * @since 4.0.0.0
     */
    public OwFNCM5SearchTemplate(OwNetworkContext context_p, OwObject obj_p) throws Exception
    {
        super(context_p, obj_p);
    }

    protected void scanResourceNodeEx(OwSearchNode search_p, Node foldersNode_p, Node objectstoresNode_p) throws Exception
    {

        Map osByIds = new HashMap();
        Map osByNames = new HashMap();

        final String pathNodeName = "searchPath";
        int mergeOperator = OwSearchOperator.MERGE_NONE;

        // === scan ObjectStores node
        // NOTE:    this is a FileNet P8 specialty, actually a folders node would be enough to describe the resources to search for,
        //          but for FileNet P8 compatibility we need to mind the ObjectStores in addition to the folders node
        //          we will also collect the meregeOperator (only one merge option will be used)
        if (objectstoresNode_p != null)
        {
            String strMergeOption = OwXMLDOMUtil.getSafeStringAttributeValue(objectstoresNode_p, "mergeoption", "");

            if (strMergeOption.equals("union"))
            {
                mergeOperator = OwSearchOperator.MERGE_UNION;
            }

            if (strMergeOption.equals("intersection"))
            {
                mergeOperator = OwSearchOperator.MERGE_INTERSECT;
            }

            for (Node n = objectstoresNode_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (!n.getNodeName().equals("objectstore"))
                {
                    continue;
                }

                //retrieve the correct id and name
                String objectStoreId = OwXMLDOMUtil.getSafeStringAttributeValue(n, "id", null);
                String objectStoreName = OwXMLDOMUtil.getSafeStringAttributeValue(n, "name", null);

                //the OwSearchObjectStore constructor will throw an exception if both name and id are null
                OwSearchObjectStore objectStore = new OwSearchObjectStore(objectStoreId, objectStoreName);
                //map the new object store accordingly by name and id 
                if (objectStoreId != null)
                {
                    osByIds.put(objectStoreId, objectStore);
                }
                if (objectStoreName != null)
                {
                    osByNames.put(objectStoreName, objectStore);
                }
            }
        }

        //the pathDefinedStores will hold all ObjectStores referred through folder elements 
        List pathDefinedStores = new LinkedList();
        final String defaultInstruction = OwString.localize(getContext().getLocale(), "ecm.OwStandardSearchTemplate.resourcepathinstr", "File path with object store, .../* also browses subfolders.");
        if (foldersNode_p != null)
        {
            for (Node n = foldersNode_p.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if (n.getNodeType() != Node.ELEMENT_NODE || !n.getNodeName().equals("folder"))
                {
                    continue;
                }

                int attributes = getSearchAttributeFromNode(n, OwSearchCriteria.ATTRIBUTE_HIDDEN);

                String instruction = OwXMLDOMUtil.getSafeStringAttributeValue(n, ATTRIBUTE_INSTRUCTION, null);
                if (null == instruction)
                {
                    instruction = defaultInstruction;
                }

                String objectStoreId = null;
                String objectStoreName = null;
                for (Node objectStoreNode = n.getFirstChild(); objectStoreNode != null; objectStoreNode = objectStoreNode.getNextSibling())
                {
                    if (objectStoreNode.getNodeName().equals("objectstore"))
                    {
                        objectStoreId = OwXMLDOMUtil.getSafeStringAttributeValue(objectStoreNode, "id", null);
                        objectStoreName = OwXMLDOMUtil.getSafeStringAttributeValue(objectStoreNode, "name", null);
                        break;
                    }
                }

                //the OwSearchObjectStore constructor will throw an exception if both name and id are null
                OwSearchObjectStore objectStore = new OwSearchObjectStore(objectStoreId, objectStoreName);

                if (objectStoreId != null)
                {
                    //an id defined ObjectStore must be mapped or unified (they should have the same name) 
                    //with an already existing ObjectStore with the same id 
                    OwSearchObjectStore osById = (OwSearchObjectStore) osByIds.get(objectStoreId);
                    if (osById != null)
                    {
                        objectStore = osById.unify(objectStore);
                        //we will replace the already existing ObjectStore
                        osByIds.put(objectStoreId, objectStore);
                    }
                    else
                    {
                        osByIds.put(objectStoreId, objectStore);
                    }
                }

                //object store name might have been unified by id we must refresh the name 
                objectStoreName = objectStore.getName();

                if (objectStoreName != null)
                {
                    //an name defined ObjectStore must be mapped or unified (they should have the same id) 
                    //with an already existing ObjectStore with the same name 
                    OwSearchObjectStore osByName = (OwSearchObjectStore) osByNames.get(objectStoreName);
                    if (osByName != null)
                    {
                        objectStore = osByName.unify(objectStore);
                        // we will replace the already existing ObjectStore 
                        // both in the name and id map if necessary 
                        if (objectStore.getId() != null)
                        {
                            osByIds.put(objectStore.getId(), objectStore);
                        }

                        osByNames.put(objectStoreName, objectStore);
                    }
                    else
                    {
                        osByNames.put(objectStoreName, objectStore);
                    }
                }

                pathDefinedStores.add(objectStore);
                String pathName = OwXMLDOMUtil.getSafeStringAttributeValue(n, "pathname", null);
                String pathId = null;

                boolean searchSubfolders = OwXMLDOMUtil.getSafeBooleanAttributeValue(n, "searchsubfolders", false);

                OwSearchPath searchPath = new OwSearchPath(pathId, pathName, searchSubfolders, objectStore);
                OwSearchPathField field = new OwSearchPathField(searchPath);

                // add resource path field to search tree
                search_p.add(createSearchNode(field.getFieldDefinition(), mergeOperator, searchPath, attributes, pathNodeName, instruction, null));

            }
        }
        //all referred resource names (ObjectStore names) should be filled in 
        m_resourceNames = new LinkedList();
        for (Iterator i = pathDefinedStores.iterator(); i.hasNext();)
        {
            OwSearchObjectStore pathStore = (OwSearchObjectStore) i.next();
            String id = pathStore.getId();
            String name = pathStore.getName();
            if (id == null && name == null)
            {
                String msg = "All objectstores must have an ID or at least a name! Found incorrect objecstore that has neither ID nor a name!";
                LOG.error("OwFNCMSearchTemplate.scanResourceNodeEx() : " + msg);
                throw new OwInvalidOperationException(msg);
            }
            if (id != null)
            {
                m_resourceNames.add(id);
            }
            else
            {
                m_resourceNames.add(name);
            }
            //we clear the maps so that they will only hold non folder referred ObjectStores
            if (id != null)
            {
                osByIds.remove(id);
            }
            if (name != null)
            {
                osByNames.remove(name);
            }
        }

        //this list will hold non folder referred 
        List objectStoreRootPaths = new LinkedList();
        objectStoreRootPaths.addAll(osByIds.values());
        //next we remove name based duplicates  of the id based ObjectStores 
        for (Iterator i = objectStoreRootPaths.iterator(); i.hasNext();)
        {
            OwSearchObjectStore store = (OwSearchObjectStore) i.next();
            String name = store.getName();
            if (name != null)
            {
                osByNames.remove(name);
            }
        }

        //the remaining name mapped objectstores are non folder referred name defined objecstores
        objectStoreRootPaths.addAll(osByNames.values());

        //we create objectstore reference paths if any
        for (Iterator i = objectStoreRootPaths.iterator(); i.hasNext();)
        {
            OwSearchObjectStore store = (OwSearchObjectStore) i.next();
            OwSearchPath osReference = new OwSearchPath(store);
            String name = store.getName();
            if (name == null)
            {
                String msg = "All objectstores must have names! Found objecstore with id=" + store.getId() + " that has no name!";
                LOG.error("OwFNCMSearchTemplate.scanResourceNodeEx(): " + msg + " The current OwStandardSearchTemplate implementation does not support nameless objectstores!");
                throw new OwInvalidOperationException(msg);
            }
            m_resourceNames.add(name);

            OwSearchPathField field = new OwSearchPathField(osReference);
            search_p.add(createSearchNode(field.getFieldDefinition(), mergeOperator, osReference, OwSearchCriteria.ATTRIBUTE_HIDDEN, pathNodeName, defaultInstruction, null));
        }
    }

    protected OwSearchNode createSubclassNode(OwFieldDefinition fieldDefinition_p, int iOp_p, Object oInitialAndDefaultValue_p, int iAttributes_p, String strUniqueName_p, String strInstruction_p, Collection wildcarddefinitions_p, Object minValue_p,
            Object maxValue_p, Object defaultValue_p, String sJavaClassName_p) throws Exception
    {
        if (OwStandardClassSelectObject.CLASS_CLASS_NAME.getClassName().equals(fieldDefinition_p.getClassName()))
        {
            iAttributes_p = OwSearchCriteria.ATTRIBUTE_HIDDEN;
        }
        return createSearchNode(fieldDefinition_p, iOp_p, oInitialAndDefaultValue_p, iAttributes_p, strUniqueName_p, strInstruction_p, wildcarddefinitions_p, minValue_p, maxValue_p, defaultValue_p, sJavaClassName_p);
    }

}
