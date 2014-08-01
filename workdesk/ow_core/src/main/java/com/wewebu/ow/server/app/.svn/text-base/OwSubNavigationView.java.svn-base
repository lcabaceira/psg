package com.wewebu.ow.server.app;

import java.io.Writer;

import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwNavigationView;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Sub View Navigation UI Module.
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
public class OwSubNavigationView extends OwNavigationView
{
    public static String KEYBOARD_UP = "OwAppContext.KEYBOARD_KEY_UP";
    public static String KEYBOARD_DOWN = "OwAppContext.KEYBOARD_KEY_DN";

    /** a navigation item
     */
    public class OwSubTabInfo extends OwAbstractTabInfo
    {
        public OwSubTabInfo(OwSubNavigationView navView_p, OwView view_p, String strTitle_p, String strImage_p, Object oReason_p, String strToolTip_p)
        {
            m_navView = navView_p;
            m_View = view_p;
            m_strName = strTitle_p;
            m_Image = strImage_p;
            m_ReasonObject = oReason_p;
            m_strToolTip = strToolTip_p;
            m_iIndex = m_TabList.size();
        }

        protected OwSubNavigationView m_navView;

        /** View to add and view to be displayed in case menu item was pressed */
        protected OwView m_View;
        /** target to receive notification in case menu item was pressed*/
        protected OwEventTarget m_Target;
        /** name of the tab to be displayed */
        protected String m_strName;
        /** optional object to be submitted to the renderNavigationLink method. e.g. an image path. */
        protected Object m_Image;
        /** optional object to be submitted to the event handler / view. */
        protected Object m_ReasonObject;
        /** flag to disable the menu item */
        protected boolean m_fDisabled = false;
        /** tooltip text for the item */
        protected String m_strToolTip;

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

        /** get the action string to be inserted into a form, which will then sumit a request equal to the button index
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
            return ((m_View == null) && (m_Target == null));
        }

        /** get formname */
        public String getFormName()
        {
            if (m_navView.m_iUseFormUpdate != OwSubNavigationView.FORM_UPDATE_NONE)
            {
                OwNavigationView.OwTabInfo tab = (OwNavigationView.OwTabInfo) m_navView.getTabList().get(m_navView.getNavigationIndex());

                // get the form name of the currently selected view
                return tab.getView().getFormName();
            }
            else
            {
                return null;
            }
        }

        /** get name */
        public String getName()
        {
            return m_strName;
        }

        /** get event method */
        public String getEventMethod()
        {
            return null;
        }

        /** get tooltip */
        public String getToolTip()
        {
            if (m_strToolTip == null)
            {
                return "";
            }
            else
            {
                return m_strToolTip;
            }
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

        /** get userobject */
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
            return m_View;
        }
    }

    /** update enum used in activateFormUpdate: do not perform an update */
    public static final int FORM_UPDATE_NONE = 0;
    /** update enum used in activateFormUpdate: do update the selected formview */
    public static final int FORM_UPDATE_NORMAL = 1;
    /** update enum used in activateFormUpdate: do update and save the selected formview */
    public static final int FORM_UPDATE_SAVE = 2;

    /** true = form of selected view gets updated on navigate, false = no update is performed */
    protected int m_iUseFormUpdate = FORM_UPDATE_NONE;

    /** set the form activate flat
     * true = form of selected view gets updated on navigate, false = no update is performed 
     *
     * @param iUseFormUpdate_p int flag as defined with FORM_UPDATE_...
     */
    protected void activateFormUpdate(int iUseFormUpdate_p)
    {
        m_iUseFormUpdate = iUseFormUpdate_p;
    }

    /** called before a form event is caught.
     *  Method gets called before the event handler to inform neighbor controls / views
     *
     * @param request_p HttpServletRequest
     *
     */
    public void onFormEvent(javax.servlet.http.HttpServletRequest request_p) throws Exception
    {
        super.onFormEvent(request_p);

        OwTabInfo lastActiveInfo = ((OwTabInfo) m_TabList.get(getNavigationIndex()));

        if (lastActiveInfo.getView() != null)
        {
            switch (m_iUseFormUpdate)
            {
                case FORM_UPDATE_NORMAL:
                {
                    // dispatch the form event to the last selected view
                    lastActiveInfo.getView().updateExternalFormTarget(request_p, false);
                }
                    break;

                case FORM_UPDATE_SAVE:
                {
                    // dispatch the form event to the last selected view
                    lastActiveInfo.getView().updateExternalFormTarget(request_p, true);
                }
                    break;
            }
        }
    }

    /** change the title of the menu item 
     *
     * @param strNewTitle_p String new title
     * @param iIndex_p int index of the item to set the title for
     *
     */
    public void setTitle(String strNewTitle_p, int iIndex_p)
    {
        ((OwSubTabInfo) m_TabList.get(iIndex_p)).m_strName = strNewTitle_p;
    }

    /** get the title of the menu item 
     *
     * @param iIndex_p int index of the item
     *
     */
    public String getTitle(int iIndex_p)
    {
        return ((OwSubTabInfo) m_TabList.get(iIndex_p)).getName();
    }

    /** add a View with optional image to the navigation module. 
     *
     * <br>In addition to addMenuItem, the View gets activated and displayed when it is selected in the navigation. The View also becomes child of the navigation module.
     * @param view_p View to add
     * @param strTitle_p name of the tab to be displayed
     * @param strName_p optional Name / ID of the view, can be null.
     * @param strImage_p optional image
     * @param oReason_p optional object to be submitted to the event handler / view. Can be null.
     * @return index of the tab
     */
    public int addView(OwView view_p, String strTitle_p, String strName_p, String strImage_p, Object oReason_p, String strToolTip_p) throws Exception
    {
        // === create and set new info instance for the tab
        return super.addView(new OwSubTabInfo(this, view_p, strTitle_p, strImage_p, oReason_p, strToolTip_p), strName_p);
    }

    /**
     * (overridable) JSP inclusion getter. 
     * By overriding this method subclasses can specify custom JSP renderers for a navigation object. 
     * @return the sub navigation JSP inclusion 
     * @since 2.5.2.0
     */
    protected String getIncludeJsp()
    {
        return "OwSubNavigationView.jsp";
    }

    /** render the navigation bar
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude(getIncludeJsp(), w_p);

        // === register keys to navigate
        int iIndex = getNavigationIndex();
        while (iIndex > 0)
        {
            iIndex--;
            if (((OwNavigationView.OwTabInfo) getTabList().get(iIndex)).getDelimiter())
            {
                continue;
            }

            String strUpEvent = getNavigateEventURL(iIndex);

            getContext().registerKeyAction(KEYBOARD_UP, strUpEvent, ((OwSubTabInfo) getTabList().get(iIndex)).getName());
            // getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_UP, OwAppContext.KEYBOARD_CTRLKEY_CTRL, strUpEvent, ((OwSubTabInfo) getTabList().get(iIndex)).getName());
            break;
        }

        iIndex = getNavigationIndex();
        while (!isLastTabIndex(iIndex))
        {
            iIndex++;
            if (((OwNavigationView.OwTabInfo) getTabList().get(iIndex)).getDelimiter())
            {
                continue;
            }

            String strUpEvent = getNavigateEventURL(iIndex);
            getContext().registerKeyAction(KEYBOARD_DOWN, strUpEvent, ((OwSubTabInfo) getTabList().get(iIndex)).getName());
            //  getContext().registerKeyEvent(OwAppContext.KEYBOARD_KEY_DN, OwAppContext.KEYBOARD_CTRLKEY_CTRL, strUpEvent, ((OwSubTabInfo) getTabList().get(iIndex)).getName());
            break;
        }
    }

}