package com.wewebu.ow.server.app;

import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwNavigationView;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Main Navigation UI Module. Creates and displays the TAB plugins.
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
public class OwMainNavigationView extends OwNavigationView
{
    private static Logger LOG = OwLogCore.getLogger(OwMainNavigationView.class);

    public static String NEXT_PLUGIN_ACTION_ID = "main.next.plugin";
    public static String PREVIOUS_PLUGIN_ACTION_ID = "main.previous.plugin";

    /** a navigation item */
    public class OwSubTabInfo extends OwAbstractTabInfo
    {
        public OwSubTabInfo(OwView view_p)
        {
            m_View = view_p;

            m_iIndex = m_TabList.size();
        }

        /** View to add and view to be displayed in case menu item was pressed */
        protected OwView m_View;
        /** target to receive notification in case menu item was pressed*/
        protected OwEventTarget m_Target;
        /** flag to disable the menu item */
        protected boolean m_fDisabled = false;

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
            return false;
        }

        /** get formname */
        public String getFormName()
        {
            return null;
        }

        /** get name */
        public String getName()
        {
            return m_View.getTitle();
        }

        /** get event method */
        public String getEventMethod()
        {
            return null;
        }

        /** get tooltip */
        public String getToolTip()
        {
            return null;
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

        /** get icon URL 
         * @throws Exception */
        public String getIcon() throws Exception
        {
            return m_View.getIcon();
        }

        /** get reason object to be submitted in the event action method */
        public Object getReasonObject()
        {
            return null;
        }

        /** get event target to be notified */
        public OwEventTarget getEventTarget()
        {
            return this.m_Target;
        }

        /** get view to be activated */
        public OwView getView()
        {
            return m_View;
        }

    }

    /** add a View with optional image to the navigation module. 
    *
    * <br>In addition to addMenuItem, the View gets activated and displayed when it is selected in the navigation. The View also becomes child of the navigation module.
    * @param view_p View to add
    * @param strName_p optional Name / ID of the view, can be null.
    * @return index of the tab
    */
    public int addPluginView(OwView view_p, String strName_p) throws Exception
    {
        return super.addView(new OwSubTabInfo(view_p), strName_p);
    }

    /** render the navigation bar
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwMainNavigationView.jsp", w_p);

        // === register keys to directly navigate to the master plugins
        List tabs = getTabList();
        for (int i = 0; i < tabs.size(); i++)
        {
            OwSubTabInfo info = (OwSubTabInfo) tabs.get(i);

            String strEventURL = getNavigateEventURL(i);
            try
            {
                ((OwMainAppContext) getContext()).registerPluginKeyEvent(((OwMasterView) info.getView()).getPluginID(), strEventURL, null, info.getName());
            }
            catch (ClassCastException e)
            {
                // obviously a OwErrorView instead of a OwMasterView, ignore.
            }
        }

        //There are no master plugins configured for current user
        if (getTabList().size() > 0)
        {
            // === register keys to navigate with arrow keys
            boolean areShortcutsDisabled = ((OwMainAppContext) this.getContext()).getSafeBooleanAppSetting("DisableKeyboardShortcutsForPluginsNavigation", false);
            if (!areShortcutsDisabled)
            {
                int iIndex = getNavigationIndex();
                while (iIndex > 0)
                {
                    iIndex--;
                    if (((OwNavigationView.OwTabInfo) getTabList().get(iIndex)).getDelimiter())
                    {
                        continue;
                    }

                    String strUpEvent = getNavigateEventURL(iIndex);
                    getContext().registerKeyAction(PREVIOUS_PLUGIN_ACTION_ID, strUpEvent, ((OwSubTabInfo) getTabList().get(iIndex)).getName());
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
                    getContext().registerKeyAction(NEXT_PLUGIN_ACTION_ID, strUpEvent, ((OwSubTabInfo) getTabList().get(iIndex)).getName());
                    break;
                }
            }
        }
        else
        {
            LOG.info("OwMainNavigationView.onRender:There is/are no masterplugin(s) configured for current user/role");
        }
    }
}