package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPolicy;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Displays and edits the policies of a given {@link OwPermissionCollection}.
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
public class OwPolicyLevelView extends OwView
{
    private static final String EFFECTIVE_LIST_ID = "effective_id";
    private static final String AVAILABLE_LIST_ID = "available_id";
    private static final String ALL_LIST_ID = "all_id";

    public static final int SELECT_ALL_POLICIES_REGION = 1;
    public static final int MESSAGE_REGION = 2;

    private OwUserInfo m_userInfo;

    /**Unique policy ITEMID index*/
    private int m_lastItemId = 0;

    private Map m_ownedViewItems = new HashMap();

    private boolean m_readonly = false;

    private boolean m_forceLiveUpdate = false;
    private String message;

    /**List box item policy item.Used to provide unique IDs for {@link OwPolicy} objects*/
    public class OwPolicyViewItem
    {
        private OwPolicy m_policy;
        private int m_id;
        private boolean m_selected;

        /**
         * Constructor
         * @param m_policy the policy this item represents 
         */
        private OwPolicyViewItem(OwPolicy m_policy, boolean selected_p)
        {
            super();
            this.m_policy = m_policy;
            m_lastItemId++;
            this.m_id = m_lastItemId;
            this.m_selected = selected_p;
        }

        public boolean isSelected()
        {
            return m_selected;
        }

        public int hashCode()
        {
            return m_id % 10;
        }

        public boolean equals(Object obj_p)
        {
            if (obj_p instanceof OwPolicyViewItem)
            {
                OwPolicyViewItem item = (OwPolicyViewItem) obj_p;
                return this.m_id == item.m_id;
            }
            else
            {
                return false;
            }
        }

        public final OwPolicy getPolicy()
        {
            return m_policy;
        }

        public final int getId()
        {
            return m_id;
        }

        public final Integer getIntegerId()
        {
            return Integer.valueOf(getId());
        }

        public final String getName()
        {
            return m_policy.getName();
        }
    }

    /**
     * Constructor
     * 
     * @param accessRightsView_p
     * @param userInfo_p
     * @param readonly_p <code>true</code> if this policies can't be edited
     * @since 4.0.0.0
     */
    public OwPolicyLevelView(OwUIGenericAccessRightsModul accessRightsView_p, OwUserInfo userInfo_p, boolean readonly_p)
    {
        this(accessRightsView_p, userInfo_p, readonly_p, false);
    }

    /**
     * Constructor
     * 
     * @param accessRightsView_p
     * @param userInfo_p
     * @param forceLiveUpdate_p if true the policy view will update the server data using its 
     *        own triggers (not through save menu buttons)    
     * @param readonly_p <code>true</code> if this policies can't be edited
     * @since 4.0.0.0
     */
    public OwPolicyLevelView(OwUIGenericAccessRightsModul accessRightsView_p, OwUserInfo userInfo_p, boolean readonly_p, boolean forceLiveUpdate_p)
    {
        super();

        OwPermissionsDocument permissionsDocument = accessRightsView_p.getDocument();
        OwPolicyLevelDocument doc = createPolicyLevelDocument(permissionsDocument);
        if (doc != null)
        {
            setDocument(doc);
        }

        this.m_userInfo = userInfo_p;
        this.m_readonly = readonly_p;
        this.m_forceLiveUpdate = forceLiveUpdate_p;
    }

    /**(overridable)
     * 
     * @param permissionsDocument_p
     * @return the document of this view
     * @since 4.0.0.0 
     */
    protected OwPolicyLevelDocument createPolicyLevelDocument(OwPermissionsDocument permissionsDocument_p)
    {
        return new OwPolicyLevelDocument(permissionsDocument_p);
    }

    public boolean isForceLiveUpdate()
    {
        return this.m_forceLiveUpdate;
    }

    @Override
    public OwPolicyLevelDocument getDocument()
    {
        return (OwPolicyLevelDocument) super.getDocument();
    }

    protected OwPermissionCollection getPermissions()
    {
        OwPolicyLevelDocument document = getDocument();
        return document.getPermissions();
    }

    public boolean canSetPolicies()
    {
        OwPermissionCollection permissions = getPermissions();
        return permissions.canSetPolicies() && !isReadonly();
    }

    public boolean canAddMultiPolicy()
    {
        OwPermissionCollection permissions = getPermissions();
        return permissions.canAddMultiPolicy();
    }

    public List getEffectivePolicyItems()
    {
        OwPermissionCollection permissions = getPermissions();
        Collection effectivePolicies = permissions.getAppliedPolicies();
        return createPolicyItems(effectivePolicies, effectivePolicies);
    }

    public List getAvailablePolicyItems()
    {
        OwPermissionCollection permissions = getPermissions();
        Collection availablePolicies = permissions.getAvailablePolicies(m_userInfo);
        Collection effectivePolicies = permissions.getAppliedPolicies();
        availablePolicies.removeAll(effectivePolicies);
        return createPolicyItems(availablePolicies, effectivePolicies);
    }

    public List getAllPolicyItems()
    {
        OwPermissionCollection permissions = getPermissions();
        Collection availablePolicies = permissions.getAvailablePolicies(m_userInfo);
        Collection effectivePolicies = permissions.getAppliedPolicies();
        return createPolicyItems(availablePolicies, effectivePolicies);
    }

    protected synchronized List createPolicyItems(Collection policies_p, Collection selectedPolicies_p)
    {
        List items = new ArrayList();
        Collection allItems = policies_p;
        if (allItems == null || allItems.isEmpty())
        {
            allItems = selectedPolicies_p;
        }
        for (Iterator i = allItems.iterator(); i.hasNext();)
        {
            OwPolicy policy = (OwPolicy) i.next();
            items.add(createPolicyItem(policy, selectedPolicies_p.contains(policy)));
        }
        return items;
    }

    protected synchronized OwPolicyViewItem createPolicyItem(OwPolicy policy_p, boolean selected_p)
    {
        OwPolicyViewItem item = new OwPolicyViewItem(policy_p, selected_p);
        m_ownedViewItems.put(item.getIntegerId(), item);
        return item;
    }

    /**
     * Clears the HTML policy items this view owns and reinitializes the unique ID generator index {@link #m_lastItemId}  
     */
    public synchronized void clearOwnedItems()
    {
        m_ownedViewItems = new HashMap();
        m_lastItemId = 0;
    }

    public String createAddEventURL()
    {
        return getFormEventFunction("Add", null);

    }

    public String createRemoveEventURL()
    {
        return getFormEventFunction("Remove", null);

    }

    public synchronized void onAdd(HttpServletRequest request_p) throws Exception
    {
        String[] policyIdValues = request_p.getParameterValues(getAvailableListId());
        if (policyIdValues == null)
        {
            policyIdValues = request_p.getParameterValues(getAllListId());
        }
        if (policyIdValues != null)
        {
            OwPermissionCollection permissions = getPermissions();
            for (int i = 0; i < policyIdValues.length; i++)
            {
                OwPolicyViewItem item = (OwPolicyViewItem) m_ownedViewItems.get(Integer.valueOf(policyIdValues[i]));
                if (item != null)
                {
                    OwPolicy policy = item.getPolicy();
                    permissions.addPolicy(policy);
                }
            }

        }
    }

    public synchronized void onRemove(HttpServletRequest request_p) throws Exception
    {
        String[] effectiveValues = request_p.getParameterValues(getEffectiveListId());
        if (effectiveValues != null)
        {
            OwPermissionCollection permissions = getPermissions();
            for (int i = 0; i < effectiveValues.length; i++)
            {
                OwPolicyViewItem item = (OwPolicyViewItem) m_ownedViewItems.get(Integer.valueOf(effectiveValues[i]));
                if (item != null)
                {
                    OwPolicy policy = item.getPolicy();
                    permissions.removePolicy(policy);
                }
            }

        }
    }

    /**
     * 
     * @return the HTML name/id of the effective policies list 
     */
    public String getEffectiveListId()
    {
        return EFFECTIVE_LIST_ID;
    }

    /**
     * 
     * @return the HTML name/id of the available policies list
     */
    public String getAvailableListId()
    {
        return AVAILABLE_LIST_ID;
    }

    /**
     * 
     * @return the HTML name/id of the all policies list
     */
    public String getAllListId()
    {
        return ALL_LIST_ID;
    }

    protected void onRender(Writer w_p) throws Exception
    {
        super.onRender(w_p);
        clearOwnedItems();
        serverSideDesignInclude("OwPolicyLevelView.jsp", w_p);
    }

    /**
     * Renders a HTML select for all available policies using the context defined 
     * combo renderer.  
     * @param w_p
     * @throws Exception
     * @since 3.1.0.0
     */
    public void renderSelectAllPolicies(Writer w_p) throws Exception
    {
        List allItems = getAllPolicyItems();
        List allComboItems = new LinkedList();
        OwDefaultComboItem selectedItem = null;
        for (Iterator i = allItems.iterator(); i.hasNext();)
        {
            OwPolicyViewItem item = (OwPolicyViewItem) i.next();
            OwDefaultComboItem comboItem = new OwDefaultComboItem("" + item.getId(), item.getName());
            allComboItems.add(comboItem);
            if (item.isSelected())
            {
                selectedItem = comboItem;
            }
        }

        OwComboModel model = new OwDefaultComboModel(false, false, selectedItem.getValue(), allComboItems);
        OwMainAppContext context = (OwMainAppContext) getContext();

        String allListId = getAllListId();
        OwString owString = new OwString("owlabel.ACL policies", "ACL policies");
        OwComboboxRenderer renderer = context.createComboboxRenderer(model, allListId, null, null, owString);

        renderer.addEvent("onchange", "policyChanged()");
        renderer.setEnabled(!isReadonly() && canSetPolicies());
        String policyDisplayName = owString.getString(getContext().getLocale());
        OwInsertLabelHelper.insertLabelValue(w_p, policyDisplayName, allListId);
        renderer.renderCombo(w_p);
    }

    public void renderRegion(Writer w_p, int region_p) throws Exception
    {
        switch (region_p)
        {
            case SELECT_ALL_POLICIES_REGION:
                renderSelectAllPolicies(w_p);
                break;
            case MESSAGE_REGION:
                renderMessageRegion(w_p);
                break;

        }
    }

    /**
     * Render the message.
     * @param w_p - the writer
     * @throws Exception
     * @since 3.1.0.0
     */
    protected void renderMessageRegion(Writer w_p) throws Exception
    {
        if (message != null)
        {
            w_p.write(message);
        }
    }

    public final boolean isReadonly()
    {
        return m_readonly;
    }

    /**
     * 
     * @param readOnly_p true if the policy view should be read-only, false otherwise.
     * @since 3.1.0.0
     */
    public final void setReadOnly(boolean readOnly_p)
    {
        this.m_readonly = readOnly_p;
    }

    /**
     * OnSave {@link OwUIGenericAccessRightsModul} delegate for non live updating policy views.
     * @param request_p
     * @param reason_p
     * @throws OwException
     * @since 3.1.0.0
     */
    public synchronized void onSave(HttpServletRequest request_p, Object reason_p) throws OwException
    {
        if (!canAddMultiPolicy())
        {
            String[] policiesIds = request_p.getParameterValues(getAllListId());
            if (policiesIds != null && policiesIds.length > 0)
            {
                OwPermissionCollection permissions = getPermissions();
                OwPolicyViewItem selectedItem = (OwPolicyViewItem) m_ownedViewItems.get(Integer.valueOf(policiesIds[0]));
                OwPolicy policy = selectedItem.getPolicy();
                permissions.addPolicy(policy);
            }
        }
    }

    /**
     * Set the message to be displayed in the message region.
     * @param message_p - the message (can be null - nothing is displayed).
     * @since 3.1.0.0
     */
    public void setMessage(String message_p)
    {
        this.message = message_p;
    }

}