package com.wewebu.ow.server.ui;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.ui.helper.OwInnerViewWrapper;

/**
 *<p>
 * Base Class for all Navigation Views / Menus / Multiple View Navigations.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class OwNavigationView extends OwView
{
    /** query string ID for the tab ID */
    private static final String TAB_ID = "tabID";

    /** flag indicating if panels should be validated */
    protected boolean m_fValidatePanels;

    /** currently selected tab view index */
    protected int m_iCurrentTabIndex = 0;

    /** instance of the view wrapper of the currently selected view */
    private OwInnerViewWrapper m_InnerViewWrapper = new OwInnerViewWrapper();

    /** list of OwTabInfos */
    protected ArrayList m_TabList = new ArrayList();

    /** add a delimiter without function and without text */
    public int addDelimiter()
    {
        // create and set new info instance for the tab
        OwTabInfo Info = new OwDelimiterTabInfo();

        m_TabList.add(Info);

        return m_TabList.indexOf(Info);
    }

    /** add a menu Item to the navigation module.
     *
     * @param info_p item to add
     *
     * @return index of the item
     */
    protected int addMenuItem(OwTabInfo info_p) throws Exception
    {
        m_TabList.add(info_p);

        return m_TabList.indexOf(info_p);
    }

    /** add a View to the navigation module. 
     *
     * <br>In addition to addMenuItem, the View gets activated and displayed when it is selected in the navigation. The View also becomes child of the navigation module.
     *
     * @param strViewName_p optional Name / ID of the view, can be null.
     * @param info_p item to add, getView must return a valid view
     *
     * @return index of the item
     */
    protected int addView(OwTabInfo info_p, String strViewName_p) throws Exception
    {
        registerView(info_p.getView(), strViewName_p);

        m_TabList.add(info_p);
        return m_TabList.indexOf(info_p);
    }

    protected void registerView(OwView view_p, String strViewName_p) throws Exception
    {
        // === set document before setContext, so we have the reference when Init is called in setContext.
        // was document already set?
        if (getDocument() != null && view_p.getDocument() == null)
        {
            view_p.setDocument(getDocument());
        }

        // === set context, init view
        // was view already registered ?
        if (view_p.getContext() == null)
        {
            view_p.attach(getContext(), strViewName_p);
        }

        // set parent
        view_p.setParent(this);

        // === add view to have a consistent iterator
        getViewList().add(view_p);
    }

    /**
     * clears all the added views in which this navigation view is navigating.
     * I.e. clears the OwTabInfo list
     */
    public void clear()
    {
        for (Iterator iter = m_TabList.iterator(); iter.hasNext();)
        {
            OwTabInfo tabInfo = (OwTabInfo) iter.next();
            if (tabInfo.getView() != null)
            {
                tabInfo.getView().detach();
            }
        }
        m_TabList.clear();
    }

    /** enable specified menu items
     * @param iIndex_p index of menu item to enable / disable
     * @param fEnable_p true enables menu item, false disables menu item
     */
    public void enable(int iIndex_p, boolean fEnable_p)
    {
        OwTabInfo Tab = (OwTabInfo) m_TabList.get(iIndex_p);
        Tab.setDisabled(!fEnable_p);
    }

    /**
     *  Mekes this item vizbile or not.
     * @param i index of menu item to enable / disable
     * @param b true renders menu item, false disables rendering for this menu item
     * @since 4.2.0.0
     */
    public void setVisible(int i, boolean b)
    {
        OwTabInfo Tab = (OwTabInfo) m_TabList.get(i);
        Tab.setVisible(b);
    }

    /** enables all menu items
     * @param fEnable_p true enables menu item, false disables menu item
     */
    public void enableAll(boolean fEnable_p)
    {
        for (int i = 0; i < m_TabList.size(); i++)
        {
            OwTabInfo Tab = (OwTabInfo) m_TabList.get(i);
            Tab.setDisabled(!fEnable_p);
        }
    }

    /** event URL to navigate through the tabs
     * NOTE: If a form is used, the EventURL contains '' primes. So you must enclose the URL in quotas "".
     *
     * @param iIndex_p index of the requested tab.
     */
    public String getNavigateEventURL(int iIndex_p)
    {
        OwTabInfo currentTab = (OwTabInfo) m_TabList.get(iIndex_p);
        if (currentTab != null)
        {
            if (currentTab.getFormName() != null)
            {
                StringBuilder build = new StringBuilder(TAB_ID);
                build.append("=");
                build.append(String.valueOf(iIndex_p));
                if (!currentTab.validateForm())
                {
                    build.append("&");
                    build.append(OwFieldManager.FLAG_DISABLE_VALIDATION);
                    build.append("=");
                    build.append(Boolean.TRUE.toString());
                }
                // === button is a submit button
                return getContext().getFormEventURL(this, "Navigate", build.toString(), currentTab.getFormName());
            }
            else
            {
                // === no form was specified so create normal button
                return getEventURL("Navigate", TAB_ID + "=" + String.valueOf(iIndex_p));
            }
        }

        return null;
    }

    /** get the action string to be inserted into a form, which will then submit a request equal to the button index
     * @param iIndex_p int button index of the button to be activated upon form submit
     * 
     * @return String action URL 
     */
    public String getNavigationFormAction(int iIndex_p)
    {
        return getEventURL("Navigate", TAB_ID + "=" + String.valueOf(iIndex_p));
    }

    /** get the currently selected navigation index, form multiple views panel
     * @return int index of current view 
     */
    public int getNavigationIndex()
    {
        return m_iCurrentTabIndex;
    }

    /** get the tab information for each navigation / menu element
     */
    public java.util.List getTabList()
    {
        return m_TabList;
    }

    public OwTabInfo getTab(int index)
    {
        return (OwTabInfo) m_TabList.get(index);
    }

    /** get the reference to the active view */
    public OwBaseView getViewReference()
    {
        return m_InnerViewWrapper;
    }

    /** check if the given index is the last one
     *
     * @return boolean true = last or beyond last one, false = otherwise
     */
    public boolean isLastTabIndex(int i_p)
    {
        return (i_p >= (m_TabList.size() - 1));
    }

    /** check if previous panel is valid
     * @param iIndex_p index of the current panel
     * 
     * @return boolean
     */
    public boolean isPreviousPanelValid(int iIndex_p)
    {
        try
        {
            if (!m_fValidatePanels)
            {
                return true;
            }

            // check if previous view is OwMultipanel and was validated
            if (iIndex_p > 0)
            {
                OwTabInfo PrevActiveInfo = ((OwTabInfo) m_TabList.get(iIndex_p - 1));
                if (PrevActiveInfo.getView() != null)
                {
                    try
                    {
                        if (!((OwMultipanel) PrevActiveInfo.getView()).isValidated())
                        {
                            // can not activate the next view
                            return false;
                        }
                    }
                    catch (ClassCastException e)
                    {
                        // ignore
                    }
                }
            }
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /** navigate to the selected view index. 
     * Only for multiple view navigations, not for menus!
     * @param iIndex_p the index of the view to be navigated to
     */
    public void navigate(int iIndex_p) throws Exception
    {
        navigate(iIndex_p, null);
    }

    /** navigate to the selected view index
     * @param iIndex_p the index of the view to be navigated to
     * @param request_p  HttpServletRequest, can be null if Navigation is used for view navigation (multiple views)
     */
    protected void navigate(int iIndex_p, HttpServletRequest request_p) throws Exception
    {
        if (iIndex_p >= m_TabList.size())
        {
            throw new OwInvalidOperationException("OwNavigationView.navigate: Navigation index is out of bound.");
        }

        OwTabInfo activeInfo = ((OwTabInfo) m_TabList.get(iIndex_p));

        if (activeInfo.getView() != null)
        {
            if (!isPreviousPanelValid(iIndex_p))
            {
                return;
            }

            m_iCurrentTabIndex = iIndex_p;

            // display view if set
            m_InnerViewWrapper.setView(activeInfo.getView());

            activeInfo.getView().onActivate(iIndex_p, activeInfo.getReasonObject());

            ////////////////////////////////////////////////
            // DEBUG: set variable in context for Dump(...) function.
            getContext().m_DEBUG_ActivateViewID = activeInfo.getView().getID();
        }

        if (activeInfo.getEventTarget() != null)
        {
            m_iCurrentTabIndex = iIndex_p;

            if (request_p == null)
            {
                throw new Exception("Request must not be null with handlers.");
            }

            // notify the target
            java.lang.reflect.Method method = activeInfo.getEventTarget().getClass().getMethod("on" + activeInfo.getEventMethod(), new Class[] { HttpServletRequest.class, Object.class });
            method.invoke(activeInfo.getEventTarget(), new Object[] { request_p, activeInfo.getReasonObject() });

            ////////////////////////////////////////////////
            // DEBUG: set variable in context for Dump(...) function.
            getContext().m_DEBUG_NavigationTargetID = activeInfo.getEventTarget().getID();

            ////////////////////////////////////////////////
            // DEBUG: set variable in context for Dump(...) function.
            getContext().m_DEBUG_NavigationEvent = "on" + activeInfo.getEventMethod();
        }
    }

    /** navigate to the selected view with the given target ID. Only for multiview navigations, not for menus
     * @param strTargetID_p the target ID of the view to navigate to
     */
    public void navigate(String strTargetID_p) throws Exception
    {
        // === iterate over the list of contained views
        for (int i = 0; i < m_TabList.size(); i++)
        {
            OwView View = ((OwTabInfo) m_TabList.get(i)).getView();
            if (View == null)
            {
                continue;
            }

            if (View.getID().equals(strTargetID_p))
            {
                // === view found, navigate to it
                navigate(i);
                return;
            }
        }

        throw new Exception("View with Target ID " + strTargetID_p + " not found");
    }

    /** navigate to the first error free view 
     * @throws Exception */
    public void navigateFirst() throws Exception
    {
        // === activate first error free view
        Exception lastException = null;
        for (int i = 0; i < getTabList().size(); i++)
        {
            try
            {
                if (!((OwNavigationView.OwTabInfo) getTabList().get(i)).getDelimiter())
                {
                    navigate(i);
                    return;
                }
            }
            catch (Exception e)
            {
                // try next one
                lastException = e;
            }
        }

        if (null != lastException)
        {
            throw lastException;
        }
    }

    /** called before a form event is caught.
     *  Method gets called before the event handler to inform neighbor controls / views
     *
     * @param request_p HttpServletRequest
     *
     */
    public void onFormEvent(javax.servlet.http.HttpServletRequest request_p) throws Exception
    {
        String strID = request_p.getParameter(TAB_ID);
        if (strID != null)
        {
            int iIndex = Integer.parseInt(strID);
            if (iIndex >= m_TabList.size())
            {
                throw new OwInvalidOperationException("OwNavigationView.onFormEvent: Navigation index is out of bound.");
            }

            OwTabInfo ActiveInfo = ((OwTabInfo) m_TabList.get(iIndex));

            if (ActiveInfo.getEventTarget() != null)
            {
                // dispatch the form event to the requesting target
                ActiveInfo.getEventTarget().onFormEvent(request_p);
            }
        }
    }

    /** event called when user clicked a tab 
     *  @param request_p  HttpServletRequest
     */
    public void onNavigate(HttpServletRequest request_p) throws Exception
    {
        String strID = request_p.getParameter(TAB_ID);
        if (strID != null)
        {
            // Get navigated index and view
            int iOldIndex = m_iCurrentTabIndex;
            int iNewIndex = Integer.parseInt(strID);
            try
            {
                navigate(iNewIndex, request_p);
            }
            catch (Exception e)
            {
                OwTabInfo info = (OwTabInfo) getTabList().get(iNewIndex);

                if (info.getEventMethod() == null)
                {
                    // === view navigation
                    // navigate back to previous view
                    if (iOldIndex != iNewIndex)
                    {
                        navigate(iOldIndex, request_p);
                    }
                }
                throw e;
            }
        }
    }

    /** render the navigation bar
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // === render each tab navigation
        for (int i = 0; i < m_TabList.size(); i++)
        {
            OwTabInfo naviitem = (OwTabInfo) m_TabList.get(i);
            renderNavigationLink(w_p, naviitem, i, m_TabList.size() - 1, getNavigationIndex());
        }
    }

    /** render the currently selected view
     * @param w_p Writer object to write HTML to
     */
    public void renderCurrentView(Writer w_p) throws Exception
    {
        // === render the current view
        OwTabInfo CurrentTab = (OwTabInfo) m_TabList.get(m_iCurrentTabIndex);
        if (CurrentTab != null)
        {
            CurrentTab.getView().render(w_p);
        }
    }

    /** render a single navigation tab 
     * @param w_p Writer object to write HTML to
     * @param tabInfo_p info to the tab (view...)
     * @param index_p zerobased index of the tab 
     * @param last_p last index
     * @param selected_p selected index
     */
    public void renderNavigationLink(Writer w_p, OwTabInfo tabInfo_p, int index_p, int last_p, int selected_p) throws Exception
    {
        // default ignore
    }

    /** flag indicating if panels should be validated */
    public void setValidatePanels(boolean fValidatePanels_p)
    {
        m_fValidatePanels = fValidatePanels_p;
    }

    /** get the number of views to navigate
     *
     */
    public int size()
    {
        return m_TabList.size();
    }

    public String getTitle()
    {
        return ((OwTabInfo) m_TabList.get(getNavigationIndex())).getName();
    }

    /** info class for the tabulator view and name and optional data
     */
    public static class OwDelimiterTabInfo extends OwAbstractTabInfo
    {
        /** return true if item is a delimiter without function */
        public boolean getDelimiter()
        {
            return true;
        }

        /** get disabled flag */
        public boolean getDisabled()
        {
            return false;
        }

        /** event URL to navigate through the tabs
         * NOTE: If a form is used, the EventURL contains '' primes. So you must enclose the URL in quotas "".
         *
         */
        public String getEvent()
        {
            return "";
        }

        /** get event method */
        public String getEventMethod()
        {
            return null;
        }

        /** get event target to be notified */
        public OwEventTarget getEventTarget()
        {
            return null;
        }

        /** get the action string to be inserted into a form, which will then sumit a request equal to the button index
         * @return String action URL 
         */
        public String getFormEventAction()
        {
            return "";
        }

        /** get formname */
        public String getFormName()
        {
            return null;
        }

        /** return true if it is the last item */
        public boolean getLastTab()
        {
            return false;
        }

        /** check if previous item is validated */
        public boolean getPreviousPanelValid()
        {
            return true;
        }

        /** get reason object to be submitted in the event action method */
        public Object getReasonObject()
        {
            return null;
        }

        /** check if item is selected */
        public boolean getSelected()
        {
            return false;
        }

        /** get view to be activated */
        public OwView getView()
        {
            return null;
        }

        /** get disabled flag */
        public void setDisabled(boolean fDisabled_p)
        {
        }

        public String getName()
        {
            return "";
        }
    }

    /**
     *<p>
     * Info class for the tabulator view and name and optional data
     * to be overloaded by the implementing menu.
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
    public interface OwTabInfo
    {

        /** return true if item is a delimiter without function */
        public abstract boolean getDelimiter();

        /** get disabled flag */
        public abstract boolean getDisabled();

        /** event URL to navigate through the tabs
         * NOTE: If a form is used, the EventURL contains '' primes. 
         * So you must enclose the URL in quotas "".
         */
        public abstract String getEvent();

        /** get event method */
        public abstract String getEventMethod();

        /** get event target to be notified */
        public abstract OwEventTarget getEventTarget();

        /** get the action string to be inserted into a form, which will then submit a request equal to the button index
         * @return String action URL 
         */
        public abstract String getFormEventAction();

        /** get formname */
        public abstract String getFormName();

        /** return true if it is the last item */
        public abstract boolean getLastTab();

        /** check if previous item is validated */
        public abstract boolean getPreviousPanelValid();

        /** get reason object to be submitted in the event action method */
        public abstract Object getReasonObject();

        /** check if item is selected */
        public abstract boolean getSelected();

        /** get view to be activated */
        public abstract OwView getView();

        /** get disabled flag */
        public abstract void setDisabled(boolean fDisabled_p);

        /**
         * @since 4.2.0.0
         * @param visible if true then this tab will be shown to the user, otherwise it will be hidden. The default value is true.
         */
        public void setVisible(boolean visible);

        /**
         * @since 4.2.0.0
         * @return true if this tab should be rendered.
         */
        public boolean isVisible();

        public String getName();

        boolean validateForm();

        void setValidateForm(boolean validateForm);
    }

    /**
     *<p>
     * Simple part implemenation of OwTabInfo.
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
     *@since 4.2.0.0
     */
    public static abstract class OwAbstractTabInfo implements OwTabInfo
    {
        private boolean validateForm;
        private boolean visible = true;

        public OwAbstractTabInfo()
        {
            validateForm = true;
        }

        @Override
        public boolean validateForm()
        {
            return validateForm;
        }

        @Override
        public void setValidateForm(boolean validateForm)
        {
            this.validateForm = validateForm;
        }

        /* (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo#setVisible(boolean)
         */
        @Override
        public void setVisible(boolean visible)
        {
            this.visible = visible;
        }

        /* (non-Javadoc)
         * @see com.wewebu.ow.server.ui.OwNavigationView.OwTabInfo#isVisible()
         */
        @Override
        public boolean isVisible()
        {
            return this.visible;
        }
    }

}