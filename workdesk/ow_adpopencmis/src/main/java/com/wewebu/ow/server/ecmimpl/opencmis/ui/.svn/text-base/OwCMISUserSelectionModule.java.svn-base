package com.wewebu.ow.server.ecmimpl.opencmis.ui;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwInsertLabelHelper;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwGroup;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUser;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUserInfoAdapter;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUserRole;
import com.wewebu.ow.server.ecmimpl.opencmis.users.OwUsersRepository;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * OwCMISUserSelectionModule.<br />
 * A UI component which is used to retrieve and display
 * different groups, user and/or roles.
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
public class OwCMISUserSelectionModule extends OwUIUserSelectModul
{
    protected static final String SEARCH_FIELD_ID = "OwCMISUserSelect";
    protected static final String SEARCH_USER_LST = "OwCMISUserList";

    private static final Logger LOG = OwLog.getLogger(OwCMISUserSelectionModule.class);

    private String enteredPattern;
    private String currentUserID;
    private int[] filter;

    private List<OwCMISInfoItem> infoList;
    private OwUsersRepository usersRepository;

    public OwCMISUserSelectionModule(OwUsersRepository usersRepository)
    {
        this.usersRepository = usersRepository;
    }

    public String getCurrentUserID()
    {
        return currentUserID;
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        infoList = new LinkedList<OwCMISInfoItem>();
    }

    public void setCurrentUserID(String currentUserID_p)
    {
        this.currentUserID = currentUserID_p;
    }

    /**
     * Return an array of int values, which represents
     * filter for the search.
     * <p>If the filter is <code>null</code> or an empty array, no
     * search will be executed.</p>
     * @return array of int, can be null
     * @see OwUIUserSelectModul#TYPE_GROUP
     * @see OwUIUserSelectModul#TYPE_ROLE
     * @see OwUIUserSelectModul#TYPE_USER
     */
    public int[] getFilter()
    {
        return filter;
    }

    /**
     * Set the filter value for the next search.
     * @param filter_p array of int representing the types
     * @see OwUIUserSelectModul#TYPE_GROUP
     * @see OwUIUserSelectModul#TYPE_ROLE
     * @see OwUIUserSelectModul#TYPE_USER
     */
    public void setFilter(int[] filter_p)
    {
        this.filter = filter_p;
    }

    public void onRender(Writer w_p) throws Exception
    {
        w_p.write("<div id=\"OwMainContent\" class=\"OwUIUserSelectModul OwCMISUserSelectionModule\">\n");

        renderSearchCriteriaBox(w_p);

        renderResultList(w_p);

        renderUserSelectButton(w_p);
        w_p.write("</div>");
    }

    /**
     * Render the button for acceptance of User selection.
     * @param w_p Writer to execute rendering
     * @throws IOException 
     */
    protected void renderUserSelectButton(Writer w_p) throws IOException
    {
        w_p.write("<div class=\"block\"><input type=\"button\" onclick=\"");
        w_p.write(getFormEventURL("SelectUser", null));
        w_p.write("\" onkeydown=\"event.cancelBubble=true\" value=\"");
        w_p.write(getContext().localize("opencmis.ui.OwCMISUserSelectionModule.select", "Select"));
        w_p.write("\" name=\"selectUser\"/></div>\n");

    }

    protected void renderSearchCriteriaBox(Writer w_p) throws Exception
    {
        // === render search criteria box
        w_p.write("<div class=\"block\">\n");
        String searchCriteriaDisplayName = getContext().localize("OwCMISUserSelectionModule.searchCriteriaDisplayName", "Search Criteria");
        OwInsertLabelHelper.insertLabelValue(w_p, searchCriteriaDisplayName, SEARCH_FIELD_ID);
        w_p.write("     <input type=\"text\" value=\"");

        if (getEnteredPattern() != null)
        {
            OwHTMLHelper.writeSecureHTML(w_p, getEnteredPattern());
        }

        w_p.write("\" name=\"");
        w_p.write(SEARCH_FIELD_ID);
        w_p.write("\" id=\"");
        w_p.write(SEARCH_FIELD_ID);
        w_p.write("\"/>&nbsp;&nbsp;");

        String sSearchEventURL = getFormEventURL("Search", null);

        w_p.write("<input type=\"button\" name=\"Search\" onClick=\"");
        w_p.write(sSearchEventURL);
        w_p.write("\" value=\"");
        String searchLabel = getContext().localize("opencmis.ui.OwCMISUserSelectionModule.search", "Search");
        w_p.write(searchLabel);
        w_p.write("\" /></div>\n");
        // set focus
        getContext().setFocusControlID(SEARCH_FIELD_ID);

        // register return key to submit search
        getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, sSearchEventURL, searchLabel);
    }

    protected void renderResultList(Writer w_p) throws OwException
    {
        String selectUserEvent = getFormEventURL("SelectUser", null);
        try
        {
            w_p.write("<div class=\"block\" ondblclick=\"");
            w_p.write(selectUserEvent);
            w_p.write("\">");

            // === render result list
            String selectUsersDisplayName = getContext().localize("OwCMISUserSelectionModule.selectUsersDisplayName", "Select Users");
            OwInsertLabelHelper.insertLabelValue(w_p, selectUsersDisplayName, SEARCH_USER_LST);
            w_p.write("<select class=\"OwInputfield_minWidth\" size=\"10\" ");
            w_p.write("onkeydown=\"onKey(event,13,true,function(event){");
            w_p.write(selectUserEvent);
            w_p.write("});\" name=\"");
            w_p.write(SEARCH_USER_LST);
            w_p.write("\" id=\"");
            w_p.write(SEARCH_USER_LST);
            w_p.write("\"");
            if (getMultiselect())
            {
                w_p.write(" multiple>");
            }

            w_p.write(">");

            List<OwCMISInfoItem> lst = getInfoList();
            if (lst != null)
            {
                for (int i = 0; i < lst.size(); i++)
                {
                    OwCMISInfoItem item = lst.get(i);

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
        catch (IOException e)
        {
            LOG.fatal("Cannot write to outputstream.", e);
            throw new OwServerException(getContext().localize("opencmis.ui.OwCMISUserSelectionModule.err.io", "Cannot write to outputstream."), e);
        }
        catch (Exception e)
        {
            LOG.error("Cannot get displayname for rendering.", e);
            throw new OwServerException(getContext().localize("opencmis.ui.OwCMISUserSelectionModule.err.render", "Cannot get displayname for rendering."), e);
        }
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";//render new form
    }

    /**
     * List of OwBaseUserInfo containing the 
     * users which were retrieved with last
     * search, and filtered by defined {@link #getFilter()}.
     * @return List of OwBaseUserInfo
     */
    protected List<OwCMISInfoItem> getInfoList()
    {
        return this.infoList;
    }

    /**
     * Handling method for events when search should be executed.
     * @param request_p HttpServletRequest
     * @throws OwException
     */
    public void onSearch(HttpServletRequest request_p) throws OwException
    {
        // === clear result list
        getInfoList().clear();
        String searchPattern = request_p.getParameter(SEARCH_FIELD_ID);
        setEnteredPattern(searchPattern);
        if (searchPattern != null)
        {
            int[] filter = getFilter();
            if (filter != null && filter.length > 0)
            {
                for (int i = 0; i < filter.length; i++)
                {
                    switch (filter[i])
                    {
                        case OwUIUserSelectModul.TYPE_USER:
                            getInfoList().addAll(getTypeUser(searchPattern));
                            break;
                        case OwUIUserSelectModul.TYPE_ROLE:
                            getInfoList().addAll(getTypeRole(searchPattern));
                            break;
                        case OwUIUserSelectModul.TYPE_GROUP:
                            getInfoList().addAll(getTypeGroup(searchPattern));
                            break;
                        default:
                            getInfoList().addAll(getExtendedType(filter[i], searchPattern));
                    }
                }
            }
        }
    }

    /**
     * Handling method for events when user entry is selected
     * @param request_p HttpServletRequest
     * @throws Exception
     */
    public void onSelectUser(HttpServletRequest request_p) throws Exception
    {
        String[] selection = request_p.getParameterValues(SEARCH_USER_LST);
        if (selection == null || selection.length == 0)
        {
            return;
        }

        LinkedList<String> roles = new LinkedList<String>();
        LinkedList<OwBaseUserInfo> users = new LinkedList<OwBaseUserInfo>();

        for (String selectedItemId : selection)
        {
            OwCMISInfoItem info = getInfoList().get(Integer.parseInt(selectedItemId));
            if (OwCMISInfoItem.GROUP == info.getType() || OwCMISInfoItem.ROLE == info.getType())
            {
                // === when selecting roles or groups, return the names only
                roles.add(info.getUserName());
            }
            else
            {
                // === when selecting users / groups, return the user object
                //OwUserInfo selectedUser = this.getNetwork().getUserFromID(info.getUserName());
                final OwUser user = info.getUser();
                OwUserInfo selectedUser = new OwUserInfoAdapter(user);
                users.add(selectedUser);
            }
        }

        if (!roles.isEmpty())
        {
            getEventListner().onSelectRoles(roles);
        }
        else
        {
            getEventListner().onSelectUsers(users);
        }
    }

    /**
     * Method to return a list of users matching the pattern,
     * which will be called if {@link #getFilter()} contains  {@link OwUIUserSelectModul#TYPE_USER}.
     * <p>Must return an empty list if no matching entries could be found!</p>
     * @param pattern String
     * @return List of OwBaseUserInfo
     * @throws OwException 
     * @see OwUIUserSelectModul#TYPE_USER
     */
    public List<OwCMISInfoItem> getTypeUser(String pattern) throws OwException
    {
        OwObjectCollection users = this.usersRepository.findUsersMatching(pattern);
        LinkedList<OwCMISInfoItem> lst = new LinkedList<OwCMISInfoItem>();
        for (Object record : users)
        {
            OwUser user = (OwUser) record;
            lst.add(new OwCMISInfoItem(user, OwCMISInfoItem.USER));
        }

        try
        {
            if (!users.isComplete())
            {
                OwMainAppContext ctx = (OwMainAppContext) this.getContext();
                String msg = ctx.localize1("owsearch.OwResultListView.notcomplete", "The result list does not contain all results. Only the first %1 are displayed.", String.valueOf(users.size()));
                ctx.postMessage(msg);
            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            throw new OwServerException("Fatal error!", e);
        }
        return lst;
    }

    /**
     * Method to return a list of user matching the pattern,
     * which will be called if {@link #getFilter()} contains  {@link OwUIUserSelectModul#TYPE_GROUP}.
     * <p>Must return an empty list if no matching entries could be found!</p>
     * @param pattern String
     * @return List of OwBaseUserInfo
     * @throws OwException 
     * @see OwUIUserSelectModul#TYPE_GROUP
     */
    public List<OwCMISInfoItem> getTypeGroup(String pattern) throws OwException
    {
        Set<OwGroup> groups = this.usersRepository.findGroupsMatching(pattern);
        LinkedList<OwCMISInfoItem> lst = new LinkedList<OwCMISInfoItem>();
        for (OwGroup group : groups)
        {
            lst.add(new OwCMISInfoItem(group.getId(), group.getName(), OwCMISInfoItem.GROUP));
        }
        return lst;
    }

    /**
     * Method to return a list of user matching the pattern,
     * which will be called if {@link #getFilter()} contains  {@link OwUIUserSelectModul#TYPE_ROLE}.
     * <p>Must return an empty list if no matching entries could be found!</p>
     * @param pattern String
     * @return List of OwBaseUserInfo
     * @throws OwException 
     * @see OwUIUserSelectModul#TYPE_ROLE
     */
    @SuppressWarnings("unchecked")
    public List<OwCMISInfoItem> getTypeRole(String pattern) throws OwException
    {
        Set<OwUserRole> roles = this.usersRepository.findRolesMatching(pattern);
        LinkedList<OwCMISInfoItem> lst = new LinkedList<OwCMISInfoItem>();

        //get last entered string and replace wildcard character
        String last = pattern.replaceAll("[:*:]", ".+");
        //always return include role names
        for (String val : getDefaultRoleNames())
        {
            if (val.matches(last))
            {
                lst.add(createRoleInfo(val));
            }
        }
        for (OwUserRole role : roles)
        {
            OwCMISInfoItem group = new OwCMISInfoItem(role.getId(), role.getName(), OwCMISInfoItem.GROUP);
            if (!lst.contains(group))
            {
                lst.add(group);
            }
        }

        return lst;
    }

    public boolean isRoleSelection()
    {
        int[] filter = getFilter();
        for (int i = 0; i < filter.length; i++)
        {
            if (filter[i] == OwUIUserSelectModul.TYPE_ROLE)
            {
                return true;
            }
        }
        return false;
    }

    protected String getEnteredPattern()
    {
        return this.enteredPattern;
    }

    protected void setEnteredPattern(String pattern)
    {
        this.enteredPattern = pattern;
    }

    /**
     * (overridable)
     * Method for additional type search which should be added
     * to the info list ({@link #getInfoList()}).
     * <p>This method will return by default an empty list.</p>
     * @param extendedType_p int extended type search
     * @param pattern_p String
     * @return List of OwBaseUserInfo
     */
    public List<OwCMISInfoItem> getExtendedType(int extendedType_p, String pattern_p)
    {
        return new LinkedList<OwCMISInfoItem>();
    }

    /**
     * Helper to return a OwCMISNetwork,
     * which cast the {@link #getNetwork()} return 
     * value to OwCMISNetwork.
     * @return OwCMISNetwork
     */
    protected OwCMISNetwork getCurrentNetwork()
    {
        return (OwCMISNetwork) getNetwork();
    }

    /**
     * Factory method to create a instance representing
     * a user with given name.
     * @param name_p String name of group
     * @return OwBaseUserInfo
     */
    protected OwCMISInfoItem createUserInfo(String name_p)
    {
        return new OwCMISInfoItem(name_p, OwCMISInfoItem.USER);
    }

    /**
     * Factory method to create a instance representing
     * a role with given name.
     * @param name_p String name of group
     * @return OwBaseUserInfo
     */
    protected OwCMISInfoItem createRoleInfo(String name_p)
    {
        return new OwCMISInfoItem(name_p, OwCMISInfoItem.ROLE);
    }

    /**
     *<p>
     * OwCMISInfoItem.<br />
     * Used for rendering the list.
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
    protected static class OwCMISInfoItem
    {
        public static final int ROLE = 1;
        public static final int USER = 2;
        public static final int GROUP = 3;

        private int type;
        private String name;
        private String displayName;
        private OwUser user;

        public OwCMISInfoItem(String name_p, int type)
        {
            this(name_p, name_p, type);
        }

        public OwCMISInfoItem(String name_p, String displayName, int type)
        {
            this.name = name_p;
            this.displayName = displayName;
            this.type = type;
        }

        public OwCMISInfoItem(OwUser user, int type)
        {
            this.name = user.getId();
            this.displayName = user.getName();
            this.type = type;
            this.user = user;
        }

        public OwUser getUser()
        {
            return user;
        }

        public String getUserDisplayName() throws Exception
        {
            return this.displayName;
        }

        public String getUserName() throws Exception
        {
            return this.name;
        }

        public boolean isGroup() throws Exception
        {
            return this.type == GROUP;
        }

        public int getType()
        {
            return this.type;
        }

        public int hashCode()
        {
            return 31 * (31 + ((name == null) ? 0 : name.hashCode())) + type;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null || getClass() != obj.getClass())
            {
                return false;
            }
            OwCMISInfoItem other = (OwCMISInfoItem) obj;
            if (getType() != other.getType())
            {
                return false;
            }
            if (name == null)
            {
                return other.name == null;
            }
            else
            {
                return name.equals(other.name);
            }
        }

        @Override
        public String toString()
        {
            return this.displayName;
        }
    }
}