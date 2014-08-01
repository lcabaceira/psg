package com.wewebu.ow.server.ui;

import java.io.Writer;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Base class for  menus for the Workdesk.<br/>
 * The Menu Items can consist of Icons or Text or both.
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
public abstract class OwMenu extends OwNavigationView
{
    /** a navigation item
     */
    public class OwMenuTab extends OwAbstractTabInfo
    {
        public OwMenuTab(OwEventTarget target_p, String strTitle_p, String strImage_p, Object oReason_p, String strEvent_p, String strToolTip_p)
        {
            m_Target = target_p;
            m_strName = strTitle_p;
            m_Image = strImage_p;
            m_ReasonObject = oReason_p;
            m_strEventMethod = strEvent_p;
            m_strToolTip = strToolTip_p;
            m_iIndex = m_TabList.size();
        }

        /** target to receive notification in case menu item was pressed*/
        protected OwEventTarget m_Target;
        /** name of the tab to be displayed */
        protected String m_strName;
        /** optional object to be submitted to the renderNavigationLink method. e.g. an image path. */
        protected Object m_Image;
        /** optional object to be submitted to the event handler / view. */
        protected Object m_ReasonObject;
        /** name of the event method to be called in the event target. */
        protected String m_strEventMethod;
        /** flag to disable the menu item */
        protected boolean m_fDisabled = false;
        /** tooltip text for the item */
        protected String m_strToolTip;
        /** Name of the form if button is a form submit button. null if it is a normal menu button */
        public String m_strFormName;

        private int m_iIndex = 0;

        /** check if previous item is validated */
        public boolean getPreviousPanelValid()
        {
            return isPreviousPanelValid(m_iIndex);
        }

        /** check if item is selected */
        public boolean getSelected()
        {
            return getNavigationIndex() == m_iIndex;
        }

        /** event URL to navigate through the tabs
         * NOTE: If a form is used, the EventURL contains '' primes. So you must enclose the URL in quotas "".
         *
         */
        public String getEvent()
        {
            return getNavigateEventURL(m_iIndex);
        }

        /** get the action string to be inserted into a form, which will then submit a request equal to the button index
         * @return String action URL 
         */
        public String getFormEventAction()
        {
            return getNavigationFormAction(m_iIndex);
        }

        /** return true if it is the last item */
        public boolean getLastTab()
        {
            return isLastTabIndex(m_iIndex);
        }

        /** return true if item is a delimiter without function */
        public boolean getDelimiter()
        {
            return false;
        }

        /** get form name */
        public String getFormName()
        {
            return m_strFormName;
        }

        /** get name */
        public String getName()
        {
            return m_strName;
        }

        /** get event method */
        public String getEventMethod()
        {
            return m_strEventMethod;
        }

        /** get tooltip */
        public String getToolTip()
        {
            return m_strToolTip;
        }

        /** get disabled flag */
        public boolean getDisabled()
        {
            return m_fDisabled;
        }

        /** get disabled flag */
        public void setDisabled(boolean fDisabled_p)
        {
            m_fDisabled = fDisabled_p;
        }

        /** get user object */
        public Object getImage()
        {
            return m_Image;
        }

        /** get reason object to be submitted in the event action method */
        public Object getReasonObject()
        {
            return m_ReasonObject;
        }

        /** get event target to be notified */
        public OwEventTarget getEventTarget()
        {
            return m_Target;
        }

        /** get view to be activated */
        public OwView getView()
        {
            return null;
        }
    }

    /**constant for situation when no default button was considered for this menu*/
    protected static final int NO_DEFAULT_BUTTON = Integer.MIN_VALUE;
    /**the default button index*/
    protected int defaultButtonIndex = NO_DEFAULT_BUTTON;
    private static Logger LOG = OwLogCore.getLogger(OwMenu.class);

    /** determine if the button is checked via check function
     */
    public boolean isChecked(int iIndex_p)
    {
        try
        {
            return getCheckedBoxIcon().equals(getImage(iIndex_p));
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private String getCheckedBoxIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/checked.png";
    }

    private String getUnCheckedBoxIcon() throws Exception
    {
        return getContext().getDesignURL() + "/images/unchecked.png";
    }

    /** display a checkbox
     * @param fEnable_p true = check the box, false = uncheck
     * @param iIndex_p button index
     */
    public void check(boolean fEnable_p, int iIndex_p) throws Exception
    {
        if (fEnable_p)
        {
            setImage(getCheckedBoxIcon(), iIndex_p);
        }
        else
        {
            setImage(getUnCheckedBoxIcon(), iIndex_p);
        }
    }

    /** toggle the checked state
     */
    public void toggleCheck(int iIndex_p) throws Exception
    {
        check(!isChecked(iIndex_p), iIndex_p);
    }

    /** specify a button in the navigation view to act as a form submit button
     * @param strFormName_p Name of the form where the button is used
     * @param iButtonIndex_p Index of the menu button to act as submit button
     */
    protected void setFormSubmitButton(String strFormName_p, int iButtonIndex_p) throws Exception
    {
        // set form name
        OwMenuTab CurrentTab = (OwMenuTab) m_TabList.get(iButtonIndex_p);
        if (CurrentTab != null)
        {
            CurrentTab.m_strFormName = strFormName_p;
        }
        else
        {
            throw new Exception("Button index not defined");
        }
    }

    /** change the user object of the menu item 
        *
        * @param strImage_p image
        * @param iIndex_p int index of the item to set the object for
        *
        */
    public void setImage(String strImage_p, int iIndex_p)
    {
        ((OwMenuTab) m_TabList.get(iIndex_p)).m_Image = strImage_p;
    }

    /** get the user object of the menu item 
     *
     * @param iIndex_p int index of the item to set the object for
     *
     */
    protected Object getImage(int iIndex_p)
    {
        return ((OwMenuTab) m_TabList.get(iIndex_p)).m_Image;
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strEvent_p name of the event method to be called in the event target.
     * @param strToolTip_p Tooltip text
     */
    public int addMenuItem(OwEventTarget target_p, String strTitle_p, String strEvent_p, String strToolTip_p) throws Exception
    {
        return addMenuItem(target_p, strTitle_p, null, strEvent_p, null, strToolTip_p);
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strEvent_p name of the event method to be called in the event target.
     * @param strToolTip_p Tooltip text
     * @param validateForm boolean
     * @return index of menu item
     * @since 4.2.0.0
     */
    public int addMenuItem(OwEventTarget target_p, String strTitle_p, String strEvent_p, String strToolTip_p, boolean validateForm) throws Exception
    {
        return addMenuItem(target_p, strTitle_p, null, strEvent_p, null, strToolTip_p, validateForm);
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strImage_p optional Image
     * @param strEvent_p name of the event method to be called in the event target.
     * @param oReason_p optional object to be submitted to the event handler. Can be null.
     * @param strToolTip_p Tooltip text
     */
    public int addMenuItem(OwEventTarget target_p, String strTitle_p, String strImage_p, String strEvent_p, Object oReason_p, String strToolTip_p) throws Exception
    {
        return super.addMenuItem(createTabInfo(target_p, strTitle_p, strImage_p, oReason_p, strEvent_p, strToolTip_p));
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strImage_p optional Image
     * @param strEvent_p name of the event method to be called in the event target.
     * @param oReason_p optional object to be submitted to the event handler. Can be null.
     * @param strToolTip_p Tooltip text
     * @param validateForm boolean control Form validation
     * @since 4.2.0.0
     */
    public int addMenuItem(OwEventTarget target_p, String strTitle_p, String strImage_p, String strEvent_p, Object oReason_p, String strToolTip_p, boolean validateForm) throws Exception
    {
        OwTabInfo tabInfo = createTabInfo(target_p, strTitle_p, strImage_p, oReason_p, strEvent_p, strToolTip_p);
        tabInfo.setValidateForm(validateForm);
        return super.addMenuItem(tabInfo);
    }

    /** overridable to create a tab info
     * 
     * @param target_p
     * @param strTitle_p
     * @param strImage_p
     * @param oReason_p
     * @param strEvent_p
     * @param strToolTip_p
     * 
     * @return OwMenuTab
     */
    protected OwMenuTab createTabInfo(OwEventTarget target_p, String strTitle_p, String strImage_p, Object oReason_p, String strEvent_p, String strToolTip_p)
    {
        return new OwMenuTab(target_p, strTitle_p, strImage_p, oReason_p, strEvent_p, strToolTip_p);
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strEvent_p name of the event method to be called in the event target.
     * @param strToolTip_p Tooltip text
     * @param strFormName_p Form Name
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strEvent_p, String strToolTip_p, String strFormName_p) throws Exception
    {
        return addFormMenuItem(target_p, strTitle_p, null, strEvent_p, null, strToolTip_p, strFormName_p);
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strEvent_p name of the event method to be called in the event target.
     * @param strToolTip_p Tooltip text
     * @param strFormName_p Form Name
     * @param validateForm boolean
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strEvent_p, String strToolTip_p, String strFormName_p, boolean validateForm) throws Exception
    {
        return addFormMenuItem(target_p, strTitle_p, null, strEvent_p, null, strToolTip_p, strFormName_p, validateForm);
    }

    /** add a menu Item to the navigation module. Uses the form name of the target
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strEvent_p name of the event method to be called in the event target.
     * @param strToolTip_p Tooltip text
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strEvent_p, String strToolTip_p) throws Exception
    {
        return addFormMenuItem(target_p, strTitle_p, null, strEvent_p, null, strToolTip_p);
    }

    /** add a menu Item to the navigation module. Uses the form name of the target
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strEvent_p name of the event method to be called in the event target.
     * @param strToolTip_p Tooltip text
     * @param validateForm boolean
     * @return index of menu item
     * @since 4.2.0.0
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strEvent_p, String strToolTip_p, boolean validateForm) throws Exception
    {
        return addFormMenuItem(target_p, strTitle_p, null, strEvent_p, null, strToolTip_p, validateForm);
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strImage_p optional image
     * @param strEvent_p name of the event method to be called in the event target.
     * @param oReason_p optional object to be submitted to the event handler. Can be null.
     * @param strToolTip_p Tooltip text
     * @param strFormName_p Form Name
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strImage_p, String strEvent_p, Object oReason_p, String strToolTip_p, String strFormName_p) throws Exception
    {
        int iIndex = addMenuItem(target_p, strTitle_p, strImage_p, strEvent_p, oReason_p, strToolTip_p);
        setFormSubmitButton(strFormName_p, iIndex);

        return iIndex;
    }

    /** add a menu Item to the navigation module.
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strImage_p optional image
     * @param strEvent_p name of the event method to be called in the event target.
     * @param oReason_p optional object to be submitted to the event handler. Can be null.
     * @param strToolTip_p Tooltip text
     * @param strFormName_p Form Name
     * @param validateForm boolean
     * @return index of item in menu
     * @since 4.2.0.0
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strImage_p, String strEvent_p, Object oReason_p, String strToolTip_p, String strFormName_p, boolean validateForm) throws Exception
    {
        int iIndex = addMenuItem(target_p, strTitle_p, strImage_p, strEvent_p, oReason_p, strToolTip_p, validateForm);
        setFormSubmitButton(strFormName_p, iIndex);

        return iIndex;
    }

    /** add a menu Item to the navigation module. Uses the form name of the target
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strImage_p optional image
     * @param strEvent_p name of the event method to be called in the event target.
     * @param oReason_p optional object to be submitted to the event handler. Can be null.
     * @param strToolTip_p Tooltip text
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strImage_p, String strEvent_p, Object oReason_p, String strToolTip_p) throws Exception
    {
        int iIndex = addMenuItem(target_p, strTitle_p, strImage_p, strEvent_p, oReason_p, strToolTip_p);
        setFormSubmitButton(target_p.getFormName(), iIndex);

        return iIndex;
    }

    /** add a menu Item to the navigation module. Uses the form name of the target
     *
     * @param target_p View to be notified in case menu item was pressed.
     * @param strTitle_p name of the tab to be displayed
     * @param strImage_p optional image
     * @param strEvent_p name of the event method to be called in the event target.
     * @param oReason_p optional object to be submitted to the event handler. Can be null.
     * @param strToolTip_p Tooltip text
     * @param validateForm boolean
     * @return index of item in menu
     * @since 4.2.0.0
     */
    public int addFormMenuItem(OwEventTarget target_p, String strTitle_p, String strImage_p, String strEvent_p, Object oReason_p, String strToolTip_p, boolean validateForm) throws Exception
    {
        int iIndex = addMenuItem(target_p, strTitle_p, strImage_p, strEvent_p, oReason_p, strToolTip_p, validateForm);
        setFormSubmitButton(target_p.getFormName(), iIndex);

        return iIndex;
    }

    /**
     * Specify what index from existing entries should be considered as default for ENTER key at a specific moment.
     * @param defaultMenuItemIndex_p - the index
     * @since 2.5.2.0
     */
    public void setDefaultMenuItem(int defaultMenuItemIndex_p)
    {
        defaultButtonIndex = defaultMenuItemIndex_p;
    }

    /**
     * Add code for register menu event to ENTER keyboard event. The event is registered only when the menu item is enabled.
     * @see com.wewebu.ow.server.ui.OwNavigationView#onRender(java.io.Writer)
     */
    protected void onRender(Writer w_p) throws Exception
    {
        super.onRender(w_p);
        if (defaultButtonIndex != NO_DEFAULT_BUTTON)
        {
            if (defaultButtonIndex < getTabList().size())
            {
                OwTabInfo tabInfo = (OwTabInfo) getTabList().get(defaultButtonIndex);

                if (!tabInfo.getDisabled())
                {
                    getContext().registerKeyFormEvent(OwAppContext.KEYBOARD_KEY_RETURN, OwAppContext.KEYBOARD_CTRLKEY_NONE, OwAppContext.FULLJS_MARKER + "javascript:navigateToMenu();", null, tabInfo.getName());
                    serverSideDesignInclude("/OwMenu.jsp", w_p);
                }
                else
                {
                    LOG.warn("OwMenu.onRender: The default menu item event with index: [" + defaultButtonIndex + "] cannot be registred for ENTER key because the menu item is disabled.");
                }
            }
            else
            {
                LOG.warn("OwMenu.onRender: The default menu item event with index: [" + defaultButtonIndex + "] cannot be registred for ENTER key.");
            }
        }
    }

    /**
     * Method to be called from renderer,
     * which is called if there is a default button defined.
     * @return String event URL
     * @since 4.0.0.0
     */
    public String getMenuEventURL()
    {
        return getNavigateEventURL(defaultButtonIndex);
    }
}