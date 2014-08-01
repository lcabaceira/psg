package com.wewebu.ow.server.settingsimpl;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwEditablePropertyDate;
import com.wewebu.ow.server.app.OwFieldManagerException;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUserSelectDialog;
import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.ecm.OwCredentials;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecm.ui.OwUIUserSelectModul;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwDialog;
import com.wewebu.ow.server.ui.OwDialog.OwDialogListener;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.util.OwDateTimeUtil;
import com.wewebu.ow.server.util.OwXMLDOMUtil;

/**
 *<p>
 * The proxy settings for BPM.
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
public class OwSettingsPropertyBPMProxy extends OwSettingsPropertyBaseImpl implements OwDialogListener
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwSettingsPropertyBPMProxy.class);

    /** prefix for the ID to distinguish tuple date */
    private static final String PROXY_PERSON_ID_FROM_DATE = "_fromdate";

    /** prefix for the ID to distinguish tuple date */
    private static final String PROXY_PERSON_ID_TO_DATE = "_todate";

    /** ID of absent person where proxies are configured */
    private String m_sAbsentPersonID;

    /** flag is true if user is admin and can edit site settings */
    private boolean m_fAdmin;

    /** set current value of property, to be overridden
    *
    * @param propertyDefinitionNode_p the node which defines the property in the plugin descriptors setting
    * @param valueNode_p the node with the current value
    * @param strSetName_p name of the property set for which the property is created
    */
    public void init(Node propertyDefinitionNode_p, Node valueNode_p, String strSetName_p) throws Exception
    {
        super.init(propertyDefinitionNode_p, valueNode_p, strSetName_p);

        m_fList = OwXMLDOMUtil.getSafeBooleanAttributeValue(propertyDefinitionNode_p, OwBaseConfiguration.PLUGIN_SETATTR_LIST, false);

        // === init value
        Object value;

        if (m_fList)
        {
            // === multiple values
            // set current value list from value node
            value = null;
            // set default value list from description node
            m_defaultValue = new ArrayList();
        }
        else
        {
            // === single value
            // set current value from value node
            value = null;
            // set default value from description node
            m_defaultValue = null;
        }

        // call base class init
        setValue(value);
    }

    protected void init() throws Exception
    {
        super.init();

        OwMainAppContext mainContext = (OwMainAppContext) getContext();

        // is user administrator ? 
        m_fAdmin = mainContext.isAllowed(OwRoleManager.ROLE_CATEGORY_STANDARD_FUNCTION, OwRoleManager.STD_FUNC_CAN_EDIT_SITE_SETTINGS);

        OwCredentials credentials = mainContext.getCredentials();
        OwUserInfo userInfo = credentials.getUserInfo();
        // default absent person is this user
        m_sAbsentPersonID = userInfo.getUserID();
    }

    public Node getValueNode(Document doc_p)
    {
        return doc_p.createElement(ITEM_VALUE_NODE);
    }

    protected OwWorkitemRepository getBPMRepository() throws Exception
    {
        return (OwWorkitemRepository) ((OwMainAppContext) getContext()).getNetwork().getInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository", null);
    }

    /** get current value of property
     * @return Object if isList() is true, Object otherwise
     */
    public Object getValue()
    {
        if ((m_value == null) && (isList()))
        {
            try
            {
                m_value = getBPMRepository().getProxies(getAbsentUserID());
            }
            catch (Exception e)
            {
                LOG.debug("Can not get the value of the properties, possible cause: no DB DataSource configured, property not found... Please analyze the Stacktrace....", e);
            }
        }

        return m_value;
    }

    protected String getAbsentUserID() throws Exception
    {
        return m_sAbsentPersonID;
    }

    /** insert the property into a HTML form for editing
    *
    * @param w_p Writer to write HTML code to
    */
    public void insertFormField(Writer w_p) throws Exception
    {
        // in case of an administrator insert absent person select box
        if (m_fAdmin)
        {
            // display select box for absent person so administrators can edit proxy settings of other users
            w_p.write("<div class='OwPropertyName' style='padding-top:5px;padding-bottom:15px;'>");
            w_p.write("<label for='absentPersonId'>");
            String tooltip = getContext().localize("settingsimpl.OwSettingsPropertyBPMProxy.absentuserselect", "Select absent person");
            w_p.write(tooltip);
            w_p.write("</label>");
            w_p.write(":&nbsp;");

            // === Input field for the absent person
            // Name
            w_p.write("<input size='32' id='absentPersonId' class='OwInputControl' readonly type='text' value='");

            w_p.write(getUserDisplayName(getAbsentUserID()));

            w_p.write("'>");

            w_p.write("<a title='");
            w_p.write(tooltip);
            w_p.write("' href=\"");
            w_p.write(getFormEventURL("SelectAbsentPerson", null));
            w_p.write("\"><img style='vertical-align:middle;border:0px none;' src='");
            w_p.write(getContext().getDesignURL());
            w_p.write("/images/user.png'");
            w_p.write(" alt='");
            w_p.write(tooltip);
            w_p.write("' title='");
            w_p.write(tooltip);
            w_p.write("'/></a>");

            w_p.write("</div>");
        }

        super.insertFormField(w_p);
    }

    /** overridable to get the displayname for a given user object ID 
     * 
     * @param sID_p
     * @return String
     */
    protected String getUserDisplayName(String sID_p)
    {
        try
        {
            OwMainAppContext context = (OwMainAppContext) getContext();
            OwNetwork network = context.getNetwork();
            OwUserInfo userInfo = network.getUserFromID(sID_p);

            return userInfo.getUserDisplayName();
        }
        catch (OwObjectNotFoundException e)
        {
            if (null != sID_p)
            {
                return sID_p;
            }
        }
        catch (Exception e)
        {
            LOG.error("OwSettingsPropertyBPMProxy.getUserDisplayName: Error getting User with Id = " + sID_p, e);
        }

        return "";
    }

    /** overridable to insert a single value into a edit HTML form
    *
    * @param w_p Writer to write HTML code to
    * @param value_p the property value to edit
    * @param strID_p String the ID of the HTML element for use in onApply
    * @param iIndex_p int Index of item if it is a list
    */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        OwWorkitemRepository.OwProxyInfo info = (OwWorkitemRepository.OwProxyInfo) value_p;

        String sProxyPerson = info.getProxyPersonID();
        Date fromdate = info.getStarttime();
        Date todate = info.getEndtime();

        w_p.write("<span class='OwPropertyName'>");
        w_p.write("<table border=0 cellpadding=0 cellspacing=0><tr><td style=\"text-align: left; vertical-align: middle;\">");

        // === Input field for the proxy person
        // Name
        w_p.write("<input size='32' title='" + getContext().localize1("settingsimpl.OwSettingsPropertyBPMProxy.indexed.proxy.title", "Proxy number %1", "" + (iIndex_p + 1)) + "'class='OwInputControl' readonly type='text' value='");
        if (sProxyPerson != null)
        {
            w_p.write(getUserDisplayName(sProxyPerson));
        }
        else
        {
            w_p.write("");
        }
        w_p.write("'>");

        w_p.write("&nbsp;");

        // select button
        w_p.write("<a title='");
        String tooltip = getContext().localize("settingsimpl.OwSettingsPropertyBPMProxy.proxyuserselect", "Select Proxy");
        w_p.write(tooltip);
        w_p.write("' href=\"");
        w_p.write(getFormEventURL("SelectProxyPerson", ITEM_QUERY_KEY + "=" + String.valueOf(iIndex_p)));
        w_p.write("\"><img style='vertical-align:middle;border:0px none;' src='");
        w_p.write(getContext().getDesignURL());
        w_p.write("/images/user.png'");
        w_p.write(" alt='");
        w_p.write(tooltip);
        w_p.write("' title='");
        w_p.write(tooltip);
        w_p.write("'/></a>");
        w_p.write("&nbsp;");

        // === from date
        w_p.write("</td><td style=\"text-align: left; vertical-align: middle;\">");
        w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyBPMProxy.from", "from"));
        w_p.write(":&nbsp;");
        w_p.write("</td><td style=\"text-align: left; vertical-align: middle;\">");

        // delegate to date control, 
        OwEditablePropertyDate.insertEditHTML((OwMainAppContext) getContext(), getContext().getLocale(), w_p, fromdate, strID_p + PROXY_PERSON_ID_FROM_DATE, ((OwMainAppContext) getContext()).useJS_DateControl(), false,
                ((OwMainAppContext) getContext()).getDateFormatStringWithoutTime());

        w_p.write("</td><td style=\"text-align: left; vertical-align: middle;\">");
        w_p.write("&nbsp;");
        w_p.write(getContext().localize("settingsimpl.OwSettingsPropertyBPMProxy.to", "to"));
        w_p.write(":&nbsp;");
        w_p.write("</td><td style=\"text-align: left; vertical-align: middle;\">");

        // === to date
        // delegate to date control, 
        OwEditablePropertyDate.insertEditHTML((OwMainAppContext) getContext(), getContext().getLocale(), w_p, todate, strID_p + PROXY_PERSON_ID_TO_DATE, ((OwMainAppContext) getContext()).useJS_DateControl(), true,
                ((OwMainAppContext) getContext()).getDateFormatStringWithoutTime());
        w_p.write("</td></tr></table>");

        w_p.write("</span>");
    }

    /**
     *<p>
     * OwBPMSettingsUserSelectDialog.
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
    private static class OwBPMSettingsUserSelectDialog extends OwUserSelectDialog
    {

        /** construct user select dialog upon an proxy info item
         * 
         * @param filterType_p
         * @param fMultiselect_p
         * @param info_p OwWorkitemRepository.OwProxyInfo to set
         */
        public OwBPMSettingsUserSelectDialog(int[] filterType_p, boolean fMultiselect_p, OwWorkitemRepository.OwProxyInfo info_p)
        {
            super(filterType_p, fMultiselect_p);

            m_info = info_p;
        }

        /** construct user select dialog
         * 
         * @param filterType_p
         * @param fMultiselect_p
         */
        public OwBPMSettingsUserSelectDialog(int[] filterType_p, boolean fMultiselect_p)
        {
            super(filterType_p, fMultiselect_p);
        }

        /** visually close the Dialog. The behavior depends on usage
         *  If this view is a child of a DialogManager, the View gets removed from it.
         */
        public void closeDialog() throws Exception
        {
            super.closeDialog();

            // assuming there is just one user selected so take the first of the list
            Collection users = getSelectedUsers();
            if (users != null)
            {
                OwUserInfo user = (OwUserInfo) users.iterator().next();
                if (user != null)
                {
                    // set user
                    if (null != m_info)
                    {
                        m_info.setProxyPersonID(user.getUserID());
                    }
                }
            }
        }

        private OwWorkitemRepository.OwProxyInfo m_info;

    }

    /** called when user clicks select button 
     * 
     * @param request_p HttpServletRequest
     */
    public void onSelectAbsentPerson(HttpServletRequest request_p) throws Exception
    {
        // open user select dialog
        OwUserSelectDialog dlgUser = new OwBPMSettingsUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_USER }, false);
        getContext().openDialog(dlgUser, this);
    }

    /** called when user clicks select button 
     * 
     * @param request_p HttpServletRequest
     */
    public void onSelectProxyPerson(HttpServletRequest request_p) throws Exception
    {
        // reset error
        m_strError = "";
        // get item
        int iItem = Integer.parseInt(request_p.getParameter(ITEM_QUERY_KEY));

        OwWorkitemRepository.OwProxyInfo info = null;

        try
        {
            // === list
            info = (OwWorkitemRepository.OwProxyInfo) ((List) getValue()).get(iItem);
        }
        catch (ClassCastException e)
        {
            // === scalar
            info = (OwWorkitemRepository.OwProxyInfo) getValue();
        }

        // open user select dialog
        OwUserSelectDialog dlgUser = new OwBPMSettingsUserSelectDialog(new int[] { OwUIUserSelectModul.TYPE_USER }, false, info);
        getContext().openDialog(dlgUser, null);
    }

    /** update the target after a form event, so it can set its form fields
    *
    * @param request_p HttpServletRequest
    * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
    *
    * @return true = field data was valid, false = field data was invalid
    */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        m_strError = null;

        try
        {
            if (isList())
            {
                // === list value
                List list = (List) getValue();

                for (int i = 0; i < list.size(); i++)
                {
                    OwWorkitemRepository.OwProxyInfo info = (OwWorkitemRepository.OwProxyInfo) list.get(i);
                    setSingleValueFromRequest(request_p, String.valueOf(this.hashCode()) + ITEM_PREFIX + String.valueOf(i), info);
                }
            }
            else
            {
                // === single value
                OwWorkitemRepository.OwProxyInfo info = (OwWorkitemRepository.OwProxyInfo) getValue();
                setSingleValueFromRequest(request_p, String.valueOf(this.hashCode()), info);
            }

            if (fSave_p)
            {
                getBPMRepository().setProxies((Collection) getValue(), getAbsentUserID());
            }
        }
        catch (Exception e)
        {
            m_strError = e.getLocalizedMessage();
        }

        return true;
    }

    /** overridable to apply changes on a submitted form
    *
    * @param request_p HttpServletRequest with form data to update the property
    * @param strID_p String the HTML form element ID of the requested value
    * @param info_p OwWorkitemRepository.OwProxyInfo
    */
    protected void setSingleValueFromRequest(HttpServletRequest request_p, String strID_p, OwWorkitemRepository.OwProxyInfo info_p) throws Exception
    {
        if (info_p.getProxyPersonID() == null)
        {
            throw new OwFieldManagerException(getContext().localize("settingsimpl.OwSettingsPropertyBPMProxy.noproxyuser", "No user selected as proxy, please select a user."));
        }
        Date fromdate = info_p.getStarttime();
        Date todate = info_p.getEndtime();

        // delegate to date control
        String dateFormatterPattern = ((OwMainAppContext) getContext()).getDateFormatStringWithoutTime();
        fromdate = OwEditablePropertyDate.updateField(getContext().getLocale(), request_p, strID_p + PROXY_PERSON_ID_FROM_DATE, ((OwMainAppContext) getContext()).useJS_DateControl(), fromdate, false, dateFormatterPattern);
        info_p.setStarttime(OwDateTimeUtil.setBeginOfDayTime(fromdate));

        // delegate to date control
        todate = OwEditablePropertyDate.updateField(getContext().getLocale(), request_p, strID_p + PROXY_PERSON_ID_TO_DATE, ((OwMainAppContext) getContext()).useJS_DateControl(), todate, true, dateFormatterPattern);
        if (todate != null && todate.compareTo(fromdate) < 0)
        {
            LOG.error("The end date (" + todate + ") is before the begin date (" + fromdate + ").");
            SimpleDateFormat df = new SimpleDateFormat(dateFormatterPattern);

            throw new OwFieldManagerException(getContext().localize2("settingsimpl.OwSettingsPropertyBPMProxy.invaliddate", "The end date (%1) is before the begin date (%2).", df.format(todate), df.format(fromdate)));
        }
        info_p.setEndtime(todate == null ? null : OwDateTimeUtil.setEndOfDayTime(todate));
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return oSingleValue_p;
    }

    /** overridable to create a default value for list properties
     *
     * @return Object with default value for a new list item
     */
    protected Object getDefaultListItemValue()
    {
        try
        {
            return getBPMRepository().createProxy();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** called when user select dialog closes 
     * 
    *  @param dialogView_p the Dialog that have to be closed.
     * */
    public void onDialogClose(OwDialog dialogView_p) throws Exception
    {
        List users = null;
        try
        {
            users = ((OwUserSelectDialog) dialogView_p).getSelectedUsers();
        }
        catch (ClassCastException e)
        {
            String msg = "Casting exception: Try to cast to [OwUserSelectDialog], passed class = [" + dialogView_p.getClass() + "]";
            LOG.error(msg, e);
            throw new OwInvalidOperationException(msg, e);
        }
        if (null != users)
        {
            String suserid = ((OwUserInfo) users.get(0)).getUserID();
            if (null != suserid)
            {
                // set new absent user
                m_sAbsentPersonID = suserid;

                // force update proxy info
                m_value = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int code_p, Object param_p) throws Exception
    {
        // ignore, used for update events from documents
    }
}