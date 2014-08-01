package com.wewebu.ow.server.ecmimpl.owsimpleadp;

import java.io.File;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;
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
import com.wewebu.ow.server.field.OwSearchSQLOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * This is the main class of the adapter.<br/>
 * It is declared in the owbootstrap.xml EcmAdapter section and will be instantiated for the adapter by the Workdesk core.
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
public class OwSimpleNetwork implements OwNetwork<OwObject>
{
    // =============================================================================
    //
    //
    // MINIMAL MEMBERS
    //
    //
    // =============================================================================    

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "simple";

    /** the credentials of the authenticated user */
    private OwSimpleCredentials m_credentials;

    /** the context for callback and environment method's into the core */
    private OwNetworkContext m_context;

    /** the configuration node where this adapter is configured in owbootstrap.xml */
    private OwXMLUtil m_confignode;

    /** the event manager usage is optional, if event's should be stored in history */
    private OwEventManager m_eventmanager;

    /** Adapter has simply one object class definition. 
     *  Usually there are several object classes for an adapter stored in a HashMap
     *  for very complex systems the adapter might also have several resources with different object classes
     *  here in this simple implementation the adapter has only one resource that is always null
     */
    private OwSimpleObjectClass m_objectclass = new OwSimpleObjectClass();

    /** object category for the preferences, which can be user or application defined, like user settings, recent file list... */
    public static final String OBJECT_CATEGORY_PREFERENCES = "owpreferences";

    /** preferences folder - can be configured in <code>owbootstrap.xml</code> file*/
    protected String m_preferencesFolder = OBJECT_CATEGORY_PREFERENCES;

    // =============================================================================
    //
    //
    // MINIMAL IMPLEMENTATION
    //
    //
    // ============================================================================= 

    /** retrieve an object for the given path
     * 
     */
    public OwObject getObjectFromPath(String strPath_p, boolean refresh_p) throws Exception
    {
        return new OwSimpleObject(this, m_objectclass, "simple network root");
    }

    /** get the object class for given name and resource
     * 
     */
    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        // just one simple object class by this adapter
        return m_objectclass;
    }

    /** get all available class names for the given resource
     * 
     */
    public Map getObjectClassNames(int[] types_p, boolean excludeHiddenAndNonInstantiable_p, boolean rootOnly_p, OwResource resource_p) throws Exception
    {
        Map ret = new HashMap();

        // just one simple object class by this adapter
        ret.put(m_objectclass.getClassName(), m_objectclass);

        return ret;
    }

    /** perform a search
     * 
     */
    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int maxSize_p, int versionSelection_p) throws Exception
    {
        OwStandardObjectCollection ret = new OwStandardObjectCollection();

        // create an SQL statement from the given search node
        StringWriter sql = new StringWriter();
        OwSearchSQLOperator op = new OwSearchSQLOperator(OwSearchSQLOperator.DATE_MODE_ORACLE);
        op.createSQLSearchCriteria(searchCriteria_p.findSearchNode(OwSearchNode.NODE_TYPE_PROPERTY), sql);

        // TODO: perform search against back-end system

        // return dummy object's
        ret.add(new OwSimpleObject(this, m_objectclass, "Implement search in OwSimpleNetwork.doSearch."));
        ret.add(new OwSimpleObject(this, m_objectclass, "You will find an example for SQL generation too."));
        ret.add(new OwSimpleObject(this, m_objectclass, "This is the SQL statement of this search SQL: " + sql.toString()));

        return ret;
    }

    /** recreate an object from it's DMSID 
     *  see OwSimpleObject.getDMSID
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean refresh_p) throws Exception
    {
        // for the simple adapter the ID is just the name of the object
        // for a real system you need to define a unique ID and query the object from the back-end
        return new OwSimpleObject(this, m_objectclass, strDMSID_p);
    }

    /** retrieve a field or property definition
     * 
     */
    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        return m_objectclass.getPropertyClass(strFieldDefinitionName_p);
    }

    /** get application specific object's like attribute bag's or search templates
     * 
     */
    public Object getApplicationObject(int typ_p, String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws Exception
    {
        throw new OwObjectNotFoundException("");
    }

    /** get application specific object's like attribute bag's or search templates
     * 
     */
    public Collection getApplicationObjects(int typ_p, String strName_p, boolean forceUserSpecificObject_p) throws Exception
    {
        switch (typ_p)
        {
            case OwNetwork.APPLICATION_OBJECT_TYPE_SEARCHTEMPLATE:
            {
                // return a searchtemplate from the resources in the jar
                OwStandardObjectCollection ret = new OwStandardObjectCollection();

                String searchtemplateXmlResource = getClass().getResource("/com/wewebu/ow/server/ecmimpl/owsimpleadp/testsearchtemplate.xml").getPath();

                OwString.replaceAll(searchtemplateXmlResource, "20%", " ");

                // use a file object from core
                OwObject obj = new OwFileObject(this, new File(searchtemplateXmlResource));

                OwStandardSearchTemplate searchTemplate = new OwStandardSearchTemplate(getContext(), obj);

                ret.add(searchTemplate);

                return ret;
            }

            default:
                String msg = "OwSimpleNetwork.getApplicationObjects: Unsupported ApplicationObject, Name = " + strName_p + ", Type = " + typ_p;
                throw new OwObjectNotFoundException(msg);
        }
    }

    /** get the credentials of the authenticated user
     * 
     */
    public OwCredentials getCredentials() throws Exception
    {
        return m_credentials;
    }

    /** logout user
     * 
     */
    public void logout() throws Exception
    {
        m_credentials = null;

    }

    /** get a unique prefix for this adapter type
     * 
     */
    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    /** perform login using user password combination
     * 
     */
    public void loginDefault(String user_p, String password_p) throws Exception
    {
        m_credentials = new OwSimpleCredentials(user_p, password_p);
    }

    /** get a UI for the login process that will be embedded in the login page
     * 
     */
    public OwUILoginModul getLoginSubModul() throws Exception
    {
        OwSimpleLoginSubModul loginSubModul = new OwSimpleLoginSubModul();

        loginSubModul.init(this);

        return loginSubModul;
    }

    /** get a reference to the event manager that record's events
     * 
     */
    public OwEventManager getEventManager()
    {
        return m_eventmanager;
    }

    /** init the adapter
     * 
     */
    public void init(OwNetworkContext context_p, OwXMLUtil confignode_p) throws Exception
    {
        m_context = context_p;
        m_confignode = confignode_p;
        m_preferencesFolder = confignode_p.getSafeTextValue("UserDefinedPreferencesFolder", OBJECT_CATEGORY_PREFERENCES);
    }

    /** set the event-manager called from core
     * 
     */
    public void setEventManager(OwEventManager eventManager_p)
    {
        m_eventmanager = eventManager_p;
    }

    /** the context for callback and environment method's into the core
     * 
     * @return OwNetworkContext
     */
    public OwNetworkContext getContext()
    {
        return m_context;
    }

    /** the configuration node where this adapter is configured in owbootstrap.xml
     * 
     * @return OwXMLUtil
     */
    public OwXMLUtil getConfigNode()
    {
        return m_confignode;
    }

    // =============================================================================
    //
    //
    // OPTIONAL IMPLEMENTATION
    //
    //
    // =============================================================================    
    public boolean canCreateNewObject(OwResource resource_p, OwObject parent_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canCreateObjectCopy(OwObject parent_p, int[] childTypes_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canDo(OwObject obj_p, int functionCode_p, int context_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canEditAccessRights(OwObject object_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String createNewObject(OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p,
            String strMimeParameter_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String createNewObject(boolean promote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String createNewObject(boolean promote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean keepCheckedOut_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String createObjectCopy(OwObject obj_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwObject parent_p, int[] childTypes_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwObjectSkeleton createObjectSkeleton(OwObjectClass objectclass_p, OwResource resource_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getApplicationObject(int typ_p, String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwUIAccessRightsModul getEditAccessRightsSubModul(OwObject object_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getInterface(String strInterfaceName_p, Object object_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Locale getLocale()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasInterface(String strInterfaceName_p)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setRoleManager(OwRoleManager roleManager_p)
    {
        // TODO Auto-generated method stub

    }

    public boolean canBatch()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void closeBatch(OwBatch batch_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    public OwResource getResource(String strID_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Iterator getResourceIDs() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwBatch openBatch() throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void refreshStaticClassdescriptions() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void releaseResources() throws Exception
    {
        // TODO Auto-generated method stub

    }

    public Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int op_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean canUserSelect() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getRoleDisplayName(String strRoleName_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwUserInfo getUserFromID(String strID_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwUIUserSelectModul getUserSelectSubModul(String strID_p, int[] types_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwRoleManager getRoleManager()
    {
        return null;
    }

    @Override
    public OwIterable<OwObject> doSearch(OwSearchNode searchClause, OwLoadContext loadContext) throws OwException
    {
        // TODO :  Simple network page search
        throw new OwNotSupportedException("The Simple network core-adapter does not suppport PageSearch.");
    }

    @Override
    public boolean canPageSearch()
    {
        return false;
    }

}