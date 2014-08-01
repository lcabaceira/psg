package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.FolderType;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.PolicyType;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISAbstractObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDocumentClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISFolderClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISFolderClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISPolicyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISPolicyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISRelationshipClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISRelationshipClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISObjectClassFactory;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;

/**
 *<p>
 * Simple/default implementation of class factory interface.
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
public class OwCMISSimpleObjectClassFactory implements OwCMISObjectClassFactory
{
    private static Logger LOG = OwLog.getLogger(OwCMISSimpleObjectClassFactory.class);

    private OwCMISNativeSession session;

    public OwCMISSimpleObjectClassFactory(OwCMISNativeSession session)
    {
        this.session = session;
    }

    public <T extends ObjectType> OwCMISNativeObjectClass<T, ?> createObjectClass(T objectType) throws OwException
    {
        BaseTypeId baseType = objectType.getBaseTypeId();
        OwCMISObjectClass clazz = null;
        switch (baseType)
        {
            case CMIS_DOCUMENT:
                clazz = createDocumentClass((DocumentType) objectType);
                break;
            case CMIS_FOLDER:
                clazz = createFolderClass((FolderType) objectType);
                break;
            case CMIS_POLICY:
                clazz = createPolicyClass((PolicyType) objectType);
                break;
            case CMIS_RELATIONSHIP:
                clazz = createRelationshipClass((RelationshipType) objectType);
                break;
            default:
                throw new OwNotSupportedException("The type " + objectType.getBaseTypeId() + " is not supported, name = " + objectType.getDisplayName());
        }

        return (OwCMISNativeObjectClass<T, ?>) clazz;
    }

    protected void baseInitialize(OwCMISAbstractObjectClass objectClass)
    {

    }

    protected void initialize(OwCMISDocumentClassImpl objectClass)
    {
        baseInitialize(objectClass);
    }

    protected void initialize(OwCMISFolderClassImpl objectClass)
    {
        baseInitialize(objectClass);
    }

    protected void initialize(OwCMISPolicyClassImpl objectClass)
    {
        baseInitialize(objectClass);
    }

    protected void initialize(OwCMISRelationshipClassImpl objectClass)
    {
        baseInitialize(objectClass);
    }

    /**(overridable)<br />
     * Create a Document class/definition object.
     * @param documentType {@link DocumentType} 
     * @return OwCMISObjectClass
     * @throws OwException
     */
    protected OwCMISDocumentClass createDocumentClass(DocumentType documentType) throws OwException
    {
        OwCMISDocumentClassImpl clazz = new OwCMISDocumentClassImpl(documentType, session);
        initialize(clazz);
        return clazz;
    }

    /**(overridable)<br />
    * Create a folder class/definition object.
    * @param folderType {@link FolderType}
    * @return OwCMISObjectClass
    */
    protected OwCMISFolderClass createFolderClass(FolderType folderType) throws OwException
    {
        OwCMISFolderClassImpl clazz = new OwCMISFolderClassImpl(folderType, session);
        initialize(clazz);
        return clazz;
    }

    /**(overridable)<br />
     * Create a policy class/definition object.
     * @param policyType PolicyType
     * @return OwCMISObjectClass
     */
    protected OwCMISPolicyClass createPolicyClass(PolicyType policyType) throws OwException
    {
        OwCMISPolicyClassImpl clazz = new OwCMISPolicyClassImpl(policyType, session);
        initialize(clazz);
        return clazz;

    }

    /**(overridable)<br />
     * Create a relationship class/definition object.
     * @param relationshipType {@link RelationshipType}
     * @return OwCMISObjectClass
     */
    protected OwCMISRelationshipClass createRelationshipClass(RelationshipType relationshipType) throws OwException
    {
        OwCMISRelationshipClassImpl clazz = new OwCMISRelationshipClassImpl(relationshipType, session);
        initialize(clazz);
        return clazz;
    }

    @Override
    public <O extends TransientCmisObject> OwCMISNativeObjectClass<?, O> createObjectClassOf(O object) throws OwException
    {
        ObjectType type = object.getType();
        return (OwCMISNativeObjectClass<?, O>) createObjectClass(type);
    }

    @Override
    public ObjectType retrieveObjectType(String id, Session session) throws OwException
    {
        return session.getTypeDefinition(id);
    }

}
