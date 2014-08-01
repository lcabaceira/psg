package com.wewebu.ow.server.ecmimpl.fncm5.ui;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.RealmSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.security.Group;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;
import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboItem;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.fncm5.conf.OwFNCM5UserInfoFactory;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * P8 5.0 User select module.
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
public class OwFNCM5UIUserSelectModul extends OwUIUserSelectModul
{
    protected static final String SEARCH_CRITERIA_ID = "SearchCrit";
    protected static final String SELECT_INDEX = "itemIdx";
    protected static final String SELECT_REALM = "realmIdx";

    private boolean groups, users, roles;

    private int maxResults;
    private LinkedList<Realm> realms;

    private LinkedList<OwBaseUserInfo> results;

    private String searchString;

    private OwFNCM5UserInfoFactory factory;

    public OwFNCM5UIUserSelectModul(RealmSet realms, int[] types_p, OwFNCM5UserInfoFactory factory_p)
    {
        for (int i = 0; i < types_p.length; i++)
        {
            if (types_p[i] == OwUIUserSelectModul.TYPE_GROUP)
            {
                groups = true;
            }

            if (types_p[i] == OwUIUserSelectModul.TYPE_USER)
            {
                users = true;
            }

            if (types_p[i] == OwUIUserSelectModul.TYPE_ROLE)
            {
                roles = true;
            }
        }
        this.realms = new LinkedList<Realm>();
        Iterator<?> it = realms.iterator();
        while (it.hasNext())
        {
            this.realms.add((Realm) it.next());
        }
        this.factory = factory_p;
        results = new LinkedList<OwBaseUserInfo>();
        setMaxResults(50);
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
    }

    public void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div id=\"OwMainContent\" class=\"OwFNCM5UIUserSelectModul\">\n");

        renderRealmsSelectBox(w_p);
        renderSearchCriteriaBox(w_p);

        renderResultList(w_p);

        if (results.size() > getMaxResults())
        {
            w_p.write("     <div>\n");
            w_p.write("         <span class=\"OwWarningText\">");
            w_p.write(getContext().localize1("OwFNCM5UIUserSelectModul.incomplete", "The result is incomplete, only the first %1 are shown", String.valueOf(getMaxResults())));
            w_p.write("         </span>");
            w_p.write("     </div>\n");
        }

        renderUserSelectButton(w_p);
        w_p.write("</div>");
    }

    protected void renderRealmsSelectBox(Writer w_p) throws Exception
    {
        // === render combobox with available realms
        w_p.write("<div class=\"block blockLabel\">\n<label>\n");
        w_p.write(getContext().localize("OwFNCM5UIUserSelectModul.domains", "Select domain:"));
        w_p.write("</label>\n");

        if (!getRealms().isEmpty())
        {
            Iterator<Realm> it = getRealms().iterator();
            LinkedList<OwComboItem> renderItems = new LinkedList<OwComboItem>();
            int idx = 0;
            while (it.hasNext())
            {
                Realm realm = it.next();
                String id = Integer.toString(idx++);
                if (isRealmAllowed(realm))
                {
                    OwComboItem item = new OwDefaultComboItem(id, realm.get_Name());
                    renderItems.add(item);
                }
            }
            OwComboModel model = new OwDefaultComboModel(false, false, null, renderItems);
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(model, SELECT_REALM, null, null, null);
            renderer.renderCombo(w_p);
        }

        w_p.write("</div>\n");

    }

    protected void renderSearchCriteriaBox(Writer w_p) throws Exception
    { // === render search criteria box
        w_p.write("<div class=\"block\">\n");
        w_p.write("     <input type=\"text\" value=\"");

        if (searchString != null)
        {
            w_p.write(searchString);
        }

        w_p.write("\" name=\"");
        w_p.write(SEARCH_CRITERIA_ID);
        w_p.write("\" id=\")");
        w_p.write(SEARCH_CRITERIA_ID);
        w_p.write("\" />&nbsp;&nbsp;");

        String sSearchEventURL = getFormEventURL("Search", null);
        String desc = getContext().localize("OwFNCM5UIUserSelectModul.btn.search", "Search");

        w_p.write("<input type=\"button\" name=\"Search\" onClick=\"");
        w_p.write(sSearchEventURL);
        w_p.write("\" value=\"");
        w_p.write(desc);
        w_p.write("\" />");

        w_p.write("</div>\n");
        // set focus
        getContext().setFocusControlID(SEARCH_CRITERIA_ID);

        // register return key to submit search
        getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, sSearchEventURL, desc);
    }

    protected void renderResultList(Writer w_p) throws Exception
    {
        String selectUserEvent = getFormEventURL("SelectUser", null);
        w_p.write("<div class=\"block\" ondblclick=\"");
        w_p.write(selectUserEvent);
        w_p.write("\">");

        // === render result list
        w_p.write("<select class=\"OwInputfield_minWidth\" size=\"10\"");
        w_p.write("onkeydown=\"onKey(event,13,true,function(event){");
        w_p.write(selectUserEvent);
        w_p.write("});\" name=\"");
        w_p.write(SELECT_INDEX);
        w_p.write("\"");

        if (getMultiselect())
        {
            w_p.write(" multiple>");
        }

        w_p.write(">");

        if (!results.isEmpty())
        {
            int max = Math.min(getMaxResults(), results.size());
            for (int i = 0; i < max; i++)
            {
                OwBaseUserInfo item = results.get(i);

                w_p.write("<option value=\"");
                w_p.write(Integer.toString(i));
                w_p.write("\">");
                w_p.write(item.getUserDisplayName());
                w_p.write("</option>");
            }
        }

        w_p.write("</select>");
        w_p.write("</div>");

    }

    protected void renderUserSelectButton(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"block\"><input type=\"button\" onclick=\"");
        w_p.write(getFormEventURL("SelectUser", null));
        w_p.write("\" onKeyDown=\"event.cancelBubble=true\" value=\"");
        w_p.write(getContext().localize("OwFNCM5UIUserSelectModul.btn.select", "Select"));
        w_p.write("\" name='selectUser'/></div>\n");
    }

    /**
     * Get max result amount to retrieve.
     * @return the maxResults
     */
    public int getMaxResults()
    {
        return maxResults;
    }

    /**
     * Set the maximum result size.
     * @param maxResults the max. results to set
     */
    public void setMaxResults(int maxResults)
    {
        this.maxResults = maxResults;
    }

    /**
     * Search for roles during search trigger.
     * @return boolean
     */
    public boolean checkRoles()
    {
        return this.roles;
    }

    /**
     * Search/check for groups
     * @return boolean
     */
    public boolean checkGroups()
    {
        return this.groups;
    }

    /**
     * Search/check for users objects.
     * @return boolean
     */
    public boolean checkUsers()
    {
        return this.users;
    }

    /**
     * Return the list of realms which will be handled.
     * @return List of Realm
     */
    public List<Realm> getRealms()
    {
        return this.realms;
    }

    /** called when user presses search button
     * @param request HttpServletRequest
     */
    public void onSearch(HttpServletRequest request) throws Exception
    {
        // === clear result list
        results.clear();

        searchString = request.getParameter(SEARCH_CRITERIA_ID);
        String realmIndex = request.getParameter(SELECT_REALM);

        if (realmIndex == null || realmIndex.length() == 0 || (searchString == null) || (searchString.length() == 0))
        {
            return;
        }
        // === get the realm
        Realm realm = getRealms().get(Integer.parseInt(realmIndex));

        // === search the realm
        if (checkGroups() || checkRoles())
        {
            GroupSet groups = realm.findGroups(searchString, PrincipalSearchType.CUSTOM, PrincipalSearchAttribute.SHORT_NAME, PrincipalSearchSortType.ASCENDING, Integer.valueOf(getMaxResults() + 1), null);

            if (!groups.isEmpty())
            {
                Iterator<?> it = groups.iterator();
                while (it.hasNext())
                {
                    Group group = (Group) it.next();
                    results.add(factory.createGroupInfo(group));
                }
            }

            List<?> defaultRoles = searchDefaultRoleNames(searchString);
            for (Iterator<?> i = defaultRoles.iterator(); i.hasNext();)
            {
                String defaultRole = (String) i.next();
                results.add(factory.createRoleInfo(defaultRole));
            }
        }

        if (checkUsers())
        {
            UserSet users = realm.findUsers(searchString, PrincipalSearchType.CUSTOM, PrincipalSearchAttribute.DISPLAY_NAME, PrincipalSearchSortType.ASCENDING, Integer.valueOf(getMaxResults() + 1), null);
            if (!users.isEmpty())
            {
                Iterator<?> it = users.iterator();
                while (it.hasNext())
                {
                    User user = (User) it.next();
                    results.add(factory.createUserInfo(user));
                }

            }
        }

    }

    public void onSelectUser(HttpServletRequest request_p) throws Exception
    {
        String[] strItems = request_p.getParameterValues(SELECT_INDEX);
        if (null == strItems)
        {
            return;
        }

        if (checkRoles())
        {
            // === when selecting roles, return the role-names only
            OwMandator mandator = getNetwork().getContext().getMandator();

            LinkedList<String> roles = new LinkedList<String>();
            for (int i = 0; i < strItems.length; i++)
            {
                OwBaseUserInfo item = results.get(Integer.parseInt(strItems[i]));

                // convert role name according to mandator specification
                String rolename = item.getUserDisplayName();
                if (mandator != null)
                {
                    rolename = mandator.filterRoleName(rolename);
                }
                roles.add(rolename);
            }

            getEventListner().onSelectRoles(roles);
        }
        else
        {
            // === when selecting users / groups, return the user object
            LinkedList<OwBaseUserInfo> users = new LinkedList<OwBaseUserInfo>();
            for (int i = 0; i < strItems.length; i++)
            {
                OwBaseUserInfo item = results.get(Integer.parseInt(strItems[i]));
                users.add(item);
            }

            getEventListner().onSelectUsers(users);
        }
    }

    protected String usesFormWithAttributes()
    {
        return "";
    }

    /**
     * Helper to pre-filter realm's to be used for selection. 
     * @param realm Realm to check if allowed or not
     * @return boolean
     */
    protected boolean isRealmAllowed(Realm realm)
    {
        return true;
    }
}
