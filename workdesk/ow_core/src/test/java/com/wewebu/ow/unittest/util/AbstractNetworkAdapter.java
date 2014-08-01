package com.wewebu.ow.unittest.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIAccessRightsModul;
import com.wewebu.ow.server.ecm.ui.OwUILoginModul;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwNetworkConfiguration;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * This is a dummy class, use it if you need only some methods of
 * the Network to be implemented.
 * For example if only getUserFromID(String) is needed in the test case.
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
public abstract class AbstractNetworkAdapter<O extends OwObject> implements OwNetwork<O>
{

    OwNetworkContext networkContext;

    public boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canDo(OwObject obj_p, int iFunctionCode_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canEditAccessRights(OwObject object_p) throws Exception
    {
        return false;
    }

    public String createNewObject(OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p) throws Exception
    {
        return null;
    }

    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p) throws Exception
    {
        return null;
    }

    public String createNewObject(boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) throws Exception
    {
        return null;
    }

    public String createObjectCopy(OwObject obj_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwObject parent_p, int[] childTypes_p) throws Exception
    {
        return null;
    }

    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwResource resource_p) throws Exception
    {
        return null;
    }

    public Object getApplicationObject(int iTyp_p, String strName_p, Object param_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws Exception
    {
        return null;
    }

    public Object getApplicationObject(int iTyp_p, String strName_p, boolean fForceUserSpecificObject_p, boolean fCreateIfNotExist_p) throws Exception
    {
        return null;
    }

    public Collection getApplicationObjects(int iTyp_p, String strName_p, boolean fForceUserSpecificObject_p) throws Exception
    {
        return null;
    }

    public OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws Exception
    {
        return null;
    }

    public Object getInterface(String strInterfaceName_p, Object oObject_p) throws Exception
    {
        return null;
    }

    public Locale getLocale()
    {
        return null;
    }

    public boolean hasInterface(String strInterfaceName_p)
    {
        return false;
    }

    public void init(OwNetworkContext context_p, OwXMLUtil networkSettings_p) throws Exception
    {
        this.networkContext = context_p;
    }

    public void setEventManager(OwEventManager eventManager_p)
    {

    }

    public void setRoleManager(OwRoleManager roleManager_p)
    {

    }

    public boolean canBatch()
    {
        return false;
    }

    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return false;
    }

    public void closeBatch(OwBatch batch_p) throws OwException
    {

    }

    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        return null;
    }

    public String getDMSPrefix()
    {
        return null;
    }

    public OwEventManager getEventManager()
    {
        return null;
    }

    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        return null;
    }

    public Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        return null;
    }

    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        return null;
    }

    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        return null;
    }

    public OwResource getResource(String strID_p) throws Exception
    {
        return null;
    }

    public Iterator getResourceIDs() throws Exception
    {
        return null;
    }

    public OwBatch openBatch() throws OwException
    {
        return null;
    }

    public void refreshStaticClassdescriptions() throws Exception
    {

    }

    public void releaseResources() throws Exception
    {

    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        return null;
    }

    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        return null;
    }

    public boolean canUserSelect() throws Exception
    {
        return false;
    }

    public OwCredentials getCredentials() throws Exception
    {
        return null;
    }

    public OwUILoginModul getLoginSubModul() throws Exception
    {
        return null;
    }

    public String getRoleDisplayName(String strRoleName_p) throws Exception
    {
        return null;
    }

    public OwUserInfo getUserFromID(String strID_p) throws Exception
    {
        return null;
    }

    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws Exception
    {
        return null;
    }

    public void loginDefault(String strUser_p, String strPassword_p) throws Exception
    {

    }

    public void logout() throws Exception
    {

    }

    public OwRoleManager getRoleManager()
    {
        return null;
    }

    public OwNetworkConfiguration getNetworkConfig()
    {
        return null;
    }

    public OwNetworkContext getContext()
    {
        return this.networkContext;
    }

    @Override
    public OwIterable<O> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO :  Abstract network page search
        throw new OwNotSupportedException("Abstract networks  do not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }
}