package com.wewebu.ow.server.ecmimpl.opencmis.junit.helper;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativePropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResourceInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.collections.OwCMISQueryIterable;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.unittest.mock.OwTestNetworkContext;

/**
 *<p>
 * JUnitOwCMISSession.
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
public class JUnitOwCMISSession implements OwCMISNativeSession
{
    OwCMISNetwork network;

    @Override
    public OwCMISResourceInfo getResourceInfo()
    {
        return null;
    }

    @Override
    public OwCMISObject getObject(String objId_p, boolean refresh_p) throws OwException
    {
        return null;
    }

    @Override
    public OwCMISObject getObjectByPath(String path, boolean refresh) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISObjectClass getObjectClass(String objectClassName_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String createObject(boolean promote_p, Object mode_p, String objectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwCMISObject parent_p, String mimeType_p,
            String mimeParameter_p, boolean keepCheckedOut_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    @Override
    public OwCMISDMSIDDecoder getDMSIDDecoder()
    {
        return new OwCMISSimpleDMSIDDecoder();
    }

    @Override
    public TimeZone getTimeZone()
    {
        return TimeZone.getDefault();
    }

    @Override
    public OwObjectCollection query(OwQueryStatement statement, boolean searchAllVersions, boolean includeAllowableActions, IncludeRelationships includeRelationships, String renditionFilter, BigInteger maxItems, BigInteger skipCount)
            throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISObject getRootFolder() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISResource getResource()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<OwCMISObjectClass> getObjectClasses(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Session getOpenCMISSession()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperationContext createOperationContext(Collection<String> filterPropertyNames, int maxItemsPerPage, OwCMISNativeObjectClass<?, ?>... classContext)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISNativePropertyClassFactory getNativePropertyClassFactory()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends ObjectType> OwCMISNativeObjectClass<T, ?> from(T type) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <O extends TransientCmisObject> OwCMISNativeObjectClass<?, O> classOf(O object) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <O extends TransientCmisObject> OwCMISNativeObject<O> from(O cmisObject, Map<String, ?> conversionParameters) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISNativeObjectClass<?, ?> getNativeObjectClass(String className) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISNativeObject<TransientCmisObject> getNativeObject(String objectNativeId, Collection<String> propertyNames, Map<String, ?> conversionParameters) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwNetwork network) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <P> P getParameterValue(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISNetwork getNetwork()
    {
        if (network == null)
        {
            network = new OwCMISNetwork();
            Document doc;
            try
            {
                doc = OwXMLDOMUtil.getDocumentFromInputStream(System.class.getResourceAsStream("/resources/cmis/bootstraps_wwu/cmis_owbootstrap_def.xml"));
                NodeList nodeLst = doc.getElementsByTagName("EcmAdapter");
                nodeLst.getLength();//Fetch elements
                network.init(new OwTestNetworkContext(), new OwStandardXMLUtil(nodeLst.item(0)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException("Cannot initialize network", e);
            }
        }
        return network;
    }

    @Override
    public OwCMISObject getObject(OwVIId viid) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OperationContext createOperationContext(Collection<String> filterPropertyNames, OwSort sorting, int maxItemsPerPage, OwCMISNativeObjectClass<?, ?>... classContext)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwCMISQueryIterable query(OwQueryStatement statement, OwLoadContext loadContext) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
