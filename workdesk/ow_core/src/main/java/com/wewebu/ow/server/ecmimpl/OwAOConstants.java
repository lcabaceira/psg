package com.wewebu.ow.server.ecmimpl;

import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.ATTRIBUTE_BAG;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.ATTRIBUTE_BAG_ITERATOR;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.ATTRIBUTE_BAG_WRITABLE;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.BPM_VIRTUAL_QUEUES;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.INVERTED_ATTRIBUTE_BAG;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.PREFERENCES;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.SEARCHTEMPLATE;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.SEARCHTEMPLATE_BPM;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.VIRTUAL_FOLDER;
import static com.wewebu.ow.server.ecmimpl.OwAOTypesEnum.XML_DOCUMENT;

import java.util.HashMap;
import java.util.Map;

import com.wewebu.ow.server.ao.OwAOType;
import com.wewebu.ow.server.ao.OwAOTypeImpl;
import com.wewebu.ow.server.ao.OwRoleManagedAOImpl;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwVirtualFolderObject;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagIterator;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * Class of constants for Application Object type specification.
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
 *@since 4.0.0.0
 */
public class OwAOConstants
{

    public static final String CONFIGNODE_SUPPORT = "Support";
    public static final String CONFIGATTRIBUTE_CLASS = "class";
    public static final String CONFIGATTRIBUTE_ID = "id";
    public static final String CONFIGATTRIBUTE_TYPE = "type";
    public static final String CONFIGATTRIBUTE_NAME = "name";
    public static final String CONFIGATTRIBUTE_VALUE = "value";
    public static final String CONFIGNODE_MANAGERS = "Managers";
    public static final String CONFIGNODE_MANAGER = "Manager";
    public static final String CONFIGNODE_SUPPORTS = "Supports";
    public static final String CONFIGNODE_PARAMS = "params";
    public static final String CONFIGNODE_PARAM = "param";

    public static final OwAOType<OwVirtualFolderObject> AO_VIRTUAL_FOLDER = new OwRoleManagedAOImpl<OwVirtualFolderObject>(VIRTUAL_FOLDER, OwVirtualFolderObject.class, OwRoleManager.ROLE_CATEGORY_VIRTUAL_FOLDER) {

        @Override
        protected String resourceFromObject(OwVirtualFolderObject object)
        {
            return object.getVirtualFolderName();
        }

    };

    public static final OwAOType<OwObject> AO_PREFERENCES = new OwAOTypeImpl<OwObject>(PREFERENCES, OwObject.class);

    public static final OwAOType<OwSearchTemplate> AO_SEARCHTEMPLATE = new OwRoleManagedAOImpl<OwSearchTemplate>(SEARCHTEMPLATE, OwSearchTemplate.class, OwRoleManager.ROLE_CATEGORY_SEARCH_TEMPLATE) {

        @Override
        protected String resourceFromObject(OwSearchTemplate object)
        {
            return object.getName();
        }
    };

    public static final OwAOType<org.w3c.dom.Node> AO_XML_DOCUMENT = new OwAOTypeImpl<org.w3c.dom.Node>(XML_DOCUMENT, org.w3c.dom.Node.class);

    public static final OwAOType<OwAttributeBag> AO_ATTRIBUTE_BAG = new OwAOTypeImpl<OwAttributeBag>(ATTRIBUTE_BAG, OwAttributeBag.class);

    public static final OwAOType<OwAttributeBagIterator> AO_ATTRIBUTE_BAG_ITERATOR = new OwAOTypeImpl<OwAttributeBagIterator>(ATTRIBUTE_BAG_ITERATOR, OwAttributeBagIterator.class);

    public static final OwAOType<OwAttributeBagWriteable> AO_ATTRIBUTE_BAG_WRITABLE = new OwAOTypeImpl<OwAttributeBagWriteable>(ATTRIBUTE_BAG_WRITABLE, OwAttributeBagWriteable.class);

    public static final OwAOType<OwAttributeBagWriteable> AO_INVERTED_ATTRIBUTE_BAG = new OwAOTypeImpl<OwAttributeBagWriteable>(INVERTED_ATTRIBUTE_BAG, OwAttributeBagWriteable.class);

    public static final OwAOType<OwSearchTemplate> AO_BPM_VIRTUAL_QUEUES = new OwAOTypeImpl<OwSearchTemplate>(BPM_VIRTUAL_QUEUES, OwSearchTemplate.class);

    public static final OwAOType<OwSearchTemplate> AO_SEARCHTEMPLATE_BPM = new OwAOTypeImpl<OwSearchTemplate>(SEARCHTEMPLATE_BPM, OwSearchTemplate.class);

    private static Map<Integer, OwAOType<?>> allTypes = null;

    /**
     * 
     * @param type as defined by {@link OwAOTypesEnum}
     * @return an {@link OwAOType} corresponding to the given type
     * @throws OwInvalidOperationException if no {@link OwAOType} corresponding to the given type could be found
     */
    public static synchronized OwAOType<?> fromType(OwAOTypesEnum type) throws OwInvalidOperationException
    {
        return fromType(type.type);
    }

    /**
     * 
     * @param type integer type as defined by {@link OwAOTypesEnum} 
     * @return an {@link OwAOType} corresponding to the given integer type
     * @throws OwInvalidOperationException if no {@link OwAOType} corresponding to the given integer type could be found
     */
    public static synchronized OwAOType<?> fromType(int type) throws OwInvalidOperationException
    {
        if (allTypes == null)
        {
            allTypes = new HashMap<Integer, OwAOType<?>>();

            allTypes.put(AO_VIRTUAL_FOLDER.getType(), AO_VIRTUAL_FOLDER);
            allTypes.put(AO_PREFERENCES.getType(), AO_PREFERENCES);
            allTypes.put(AO_SEARCHTEMPLATE.getType(), AO_SEARCHTEMPLATE);
            allTypes.put(AO_XML_DOCUMENT.getType(), AO_XML_DOCUMENT);
            allTypes.put(AO_ATTRIBUTE_BAG.getType(), AO_ATTRIBUTE_BAG);
            allTypes.put(AO_ATTRIBUTE_BAG_ITERATOR.getType(), AO_ATTRIBUTE_BAG_ITERATOR);
            allTypes.put(AO_ATTRIBUTE_BAG_WRITABLE.getType(), AO_ATTRIBUTE_BAG_WRITABLE);
            allTypes.put(AO_INVERTED_ATTRIBUTE_BAG.getType(), AO_INVERTED_ATTRIBUTE_BAG);
            allTypes.put(AO_BPM_VIRTUAL_QUEUES.getType(), AO_BPM_VIRTUAL_QUEUES);
            allTypes.put(AO_SEARCHTEMPLATE_BPM.getType(), AO_SEARCHTEMPLATE_BPM);
        }

        OwAOType<?> theType = allTypes.get(type);

        if (theType == null)
        {
            throw new OwInvalidOperationException("No such AO type defined id=" + type + ". Is this " + OwAOTypesEnum.fromType(type) + " ?");
        }
        else
        {
            return theType;
        }

    }

}
