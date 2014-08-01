package com.wewebu.ow.server.plug.owsearch;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMasterDocument;
import com.wewebu.ow.server.app.OwMaxMinButtonControlView;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewControl;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.plug.owsearch.log.OwLog;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.ui.button.OwUnifyImageButtonView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Result list view, which contains a OwObjectListView to display the search results.
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
public class OwResultListView extends OwLayout implements OwClientRefreshContext
{
    /** name of the max size region */
    public static final int OBJECT_LIST_REGION = 1;

    /** name of the min max control region */
    public static final int MIN_MAX_CONTROL_VIEW = 3;

    /** name of the max object list control region */
    public static final int OBJECT_LIST_CONTROL_REGION = 6;

    /** name of the new search field */
    public static final int NEW_SAVED_SEARCH_REGION = 7;

    /** new search button */
    public static final int NEW_SEARCH_BUTTON_REGION = 8;

    /** query key for the search name input field */
    private static final String SAVE_SEARCH_NAME_KEY = "owsavesearch";

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwResultListView.class);

    /** the buttons for the search form */
    protected OwSubMenuView m_NewSearchMenuView;

    private OwObjectListViewControl m_listcontrol;

    private OwUnifyImageButtonView buttonView;

    /** View Module to display a maximize minimize button and maximize minimize the attached view */
    protected OwMaxMinButtonControlView m_MaxMinButtonControlView;

    /** initialize the target after the context is set.
     */
    protected void init() throws Exception
    {
        super.init();

        m_NewSearchMenuView = new OwSubMenuView();
        addView(m_NewSearchMenuView, NEW_SEARCH_BUTTON_REGION, null);

        // get list of document functions from configuration
        List filteredDocumentFunctions = null;
        OwXMLUtil documentFunctionsNode = ((OwSearchDocument) getDocument()).getDocumentFunctionsNode();

        //documentFunctionsNode is not null and document functions are enabled for this master plugin
        if (documentFunctionsNode != null && documentFunctionsNode.getSafeBooleanAttributeValue(OwSearchDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            // === filter document function plugins if filter is defined
            List docfunctionsList = documentFunctionsNode.getSafeStringList();
            //remove duplicated ID
            Set docfunctions = new LinkedHashSet(docfunctionsList);
            if (docfunctions.size() != 0)
            {
                filteredDocumentFunctions = new LinkedList();
                // === use only defined functions
                Iterator<?> it = docfunctions.iterator();
                while (it.hasNext())
                {
                    String id = (String) it.next();
                    // only add to array if it is an allowed function
                    if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                    {
                        OwFunction func = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                        filteredDocumentFunctions.add(func);
                    }
                }
            }
        }

        m_listcontrol = createObjectListViewControl();
        // === attach to layout use a specific ID for persistence
        addView(m_listcontrol, OBJECT_LIST_CONTROL_REGION, null);

        // === add min max control
        m_MaxMinButtonControlView = createMaxMinButtonControlView(this);

        addView(m_MaxMinButtonControlView, MIN_MAX_CONTROL_VIEW, null);

        // filtered document functions is not null and document functions are enabled for this master plugin
        if (filteredDocumentFunctions != null && documentFunctionsNode.getSafeBooleanAttributeValue(OwSearchDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            m_listcontrol.setDocumentFunctionPluginList(filteredDocumentFunctions);
        }
        // document functions are disabled for this master plugin
        if (documentFunctionsNode != null && !documentFunctionsNode.getSafeBooleanAttributeValue(OwSearchDocument.PLUGIN_LIST_ENABLED_ATTRIBUTE, false))
        {
            m_listcontrol.setDocumentFunctionPluginList(new LinkedList());
        }

        // optional save search button
        int saveSearchButton = m_NewSearchMenuView.addFormMenuItem(this, getContext().localize("owsearch.OwResultListView.savesearch", "Save"), "SaveNewSearch", null);
        m_NewSearchMenuView.setDefaultMenuItem(saveSearchButton);

        // === use button view for rendering in OBJECT_LIST_CONTROL_REGION
        buttonView = createButtonView();
        addView(buttonView, OBJECT_LIST_CONTROL_REGION, null);

        buttonView.addButtonProvider(m_listcontrol);
        buttonView.setHtmlId(m_listcontrol.getHtmlId());
        buttonView.getDesignClasses().addAll(m_listcontrol.getDesignClasses());

        buttonView.addButtonProvider(m_MaxMinButtonControlView);
        buttonView.getDesignClasses().addAll(m_MaxMinButtonControlView.getDesignClasses());

        addViewReference(m_listcontrol.getViewReference(), OBJECT_LIST_REGION);

        // set config node after initialization
        m_listcontrol.setConfigNode(((OwMasterDocument) getDocument()).getConfigNode().getSubNode("ResultListViews"));

        // activate view, select persistent index
        m_listcontrol.activateListView();

        // set the refresh context
        m_listcontrol.getObjectListView().setRefreshContext(this);
        m_listcontrol.getObjectListView().setExternalFormTarget(this);

        m_listcontrol.setExternalFormTarget(this);
    }

    protected String usesFormWithAttributes()
    {
        return "";
    }

    /** event called when user clicked SaveNewSearch
     *   @param request_p  a {@link HttpServletRequest}
     *   @param oReason_p Reason object submitted in the menu item creation
     */
    public void onSaveNewSearch(HttpServletRequest request_p, Object oReason_p) throws Exception
    {
        OwSearchTemplate searchtemplate = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate();
        if (null == searchtemplate)
        {
            return;
        }

        String name = request_p.getParameter(SAVE_SEARCH_NAME_KEY);
        if ((null != name) && (name.length() > 0))
        {
            searchtemplate.saveSearch(name);
            informUserOnSave();

        }
    }

    /** get the reference to the object list view contained in the result list
     * @return OwObjectListView
     */
    public OwObjectListView getObjectListView()
    {
        return m_listcontrol.getObjectListView();
    }

    public boolean isRegion(int region_p)
    {
        switch (region_p)
        {
            case NEW_SEARCH_BUTTON_REGION:
            case NEW_SAVED_SEARCH_REGION:
            {
                try
                {
                    return ((OwSearchDocument) getDocument()).getCurrentSearchTemplate().canSaveSearch();
                }
                catch (Exception e)
                {
                    return false;
                }
            }

            default:
                return super.isRegion(region_p);
        }
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return getContext().localize("plug.owsearch.OwResultListView.title", "Result List");
    }

    /** render only a region in the view, used by derived classes
     *  render the attribute from the result list
     *
     * @param w_p Writer object to write HTML to
     * @param strRegion_p named region to render
     */
    public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
    {
        // === write out the attribute with the region name from the result list
        OwObjectCollection objList = getObjectListView().getObjectList();
        if (null != objList)
        {
            w_p.write(objList.getAttribute(strRegion_p).toString());
        }
    }

    /** get a result list attribute
     *
     * @param attributeName_p String name of attribute as defined with OwObjectCollection.ATTRIBUTE_...
     * @param default_p Object default value if not found
     *
     * @return Object value of attribute, return default_p if no object list available for the view (not initialized yet, not called yet...)
     */
    public Object getSafeListAttribute(String attributeName_p, Object default_p)
    {
        OwObjectCollection objectList = getObjectListView().getObjectList();
        if (objectList != null)
        {
            return objectList.getSafeAttribute(attributeName_p, default_p);
        }
        else
        {
            LOG.debug("OwResultListView.getSafeListAttribute: No object list available for the object list view... returns the default_p value w=" + default_p);
            return default_p;
        }
    }

    /** determine if region exists
     *
     * @param strRegion_p name of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isNamedRegion(String strRegion_p) throws Exception
    {
        // === check if the attribute with the region name from the result list is available
        OwObjectCollection objList = getObjectListView().getObjectList();
        if (null != objList)
        {
            return objList.hasAttribute(strRegion_p);
        }
        else
        {
            return false;
        }
    }

    /** get a URL for the back to template button */
    public String getBackToSearchTemplateURL()
    {
        return getEventURL("BackToTemplate", null);
    }

    /** check if a go back button should be displayed */
    public boolean getCanGoBackToSearchTemplate()
    {
        return ((OwSearchDocument) getDocument()).canGoBackToSearchTemplate();
    }

    /** get the result list count */
    public int getCount()
    {
        return ((Integer) getSafeListAttribute(OwObjectCollection.ATTRIBUTE_SIZE, Integer.valueOf(0))).intValue();
    }

    /** check if result list could be retrieved completely
     *
     * @return boolean true = all items are retrieved, false = there are more items on the server, only some are shown
     */
    public boolean getIsComplete()
    {
        return ((Boolean) getSafeListAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE, Boolean.TRUE)).booleanValue();
    }

    /** called when user clicks the back to template button
     *  the focus will return to the calling search template
     */
    public void onBackToTemplate(HttpServletRequest request_p) throws Exception
    {
        ((OwSearchDocument) getDocument()).goBackToSearchTemplate();
    }

    /**
     * Returns the display name of the calling search template in localized form.<br/>
     * If display name can't retrieved, the method will return an empty string.
     *
     * @return the display name of the calling search template
     */
    public String getTemplateDisplayName()
    {
        String name = "";
        try
        {
            name = ((OwSearchDocument) getDocument()).getCurrentSearchTemplate().getDisplayName(getContext().getLocale());
        }
        catch (Exception e)
        {
            //nothing to do here, an empty string will be returned
            String msg = "OwResultListView.getTemplateDisplayName: Can't retrieve display name of current search template.";
            LOG.warn(msg, e);
        }
        return name;
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     *
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case NEW_SAVED_SEARCH_REGION:
            {
                renderNewSavedSearchRegion(w_p);
            }
                break;

            default:
            {
                // === render registered regions
                super.renderRegion(w_p, iRegion_p);
            }
        }
    }

    /** render the new search field
     *
     * @param w_p
     * @throws Exception
     */
    private void renderNewSavedSearchRegion(Writer w_p) throws Exception
    {
        w_p.write("<div id=\"OwResultListViewSearch\">\n");
        w_p.write("<div>\n");
        w_p.write("<div class='OwPropertyName'>\n");
        w_p.write("<label for='saveTheSearchAsId'>");
        w_p.write(getContext().localize("plug.owsearch.OwResultListView.newsavedsearchlabel", "Save the search as:"));
        w_p.write("</label>");
        w_p.write("</div>\n");
        w_p.write("<input id='saveTheSearchAsId' name='");
        w_p.write(SAVE_SEARCH_NAME_KEY);
        w_p.write("' title='");
        w_p.write(getContext().localize("plug.owsearch.OwResultListView.newsavedsearchtooltip", "Enter a name for the new search"));
        w_p.write("'/>\n");
        w_p.write("</div>\n");
        w_p.write("</div>\n");
    }

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        // === render ObjectList
        serverSideDesignInclude("owsearch/OwResultListView.jsp", w_p);
    }

    // === implement OwClientRefreshContext interface
    public void onClientRefreshContextUpdate(int reason_p, Object param_p) throws Exception
    {
        switch (reason_p)
        {
            case OwUpdateCodes.DELETE_OBJECT:
            {
                // one or more objects have been deleted, we need to remove it from the list.
                // param contains a set of DMSIDs of the deleted objects
                Set deleteddmsids = (Set) param_p;

                OwObjectListView objectListView = getObjectListView();
                OwObjectCollection objectList = objectListView.getObjectList();
                if (null != objectList)
                {
                    Iterator it = objectList.iterator();
                    while (it.hasNext())
                    {
                        OwObject obj = (OwObject) it.next();

                        if (deleteddmsids.contains(obj.getDMSID()))
                        {
                            // found deleted one, remove it from the list
                            it.remove();
                        }
                    }
                }
                objectListView.onClientRefreshContextUpdate(reason_p, param_p);
            }
                break;

            case OwUpdateCodes.UPDATE_PARENT_OBJECT_CHILDS:
            {
                OwSearchDocument searchDocument = ((OwSearchDocument) getDocument());
                searchDocument.repeatLastSearch();
            }
                break;
        }

    }

    /**
     * Post a message for informing user about a successfully save operation.
     */
    protected void informUserOnSave()
    {
        // inform user
        ((OwMainAppContext) getContext()).postMessage(getContext().localize("plug.owsearch.OwResultListView.successfullysave", "Your search was successfully saved."));
    }

    /**
     * Enable/Disable stored searches feature
     * @return true
     * @since 3.1.0.0  
     */
    public boolean isEnabledStoredSearch()
    {
        //true for backward compatibility
        return (((OwSearchDocument) getDocument()).getConfigNode().getSafeBooleanValue("EnableStoredSearches", true));
    }

    /**(overridable)
     * Factory method for list view control.
     * @return OwObjectListViewControl
     * @since 4.1.0.0
     */
    protected OwObjectListViewControl createObjectListViewControl()
    {
        return new OwObjectListViewControl();
    }

    /**(overridable)
     * Factory method for maximize/minimize control view.
     * @return OwMaxMinButtonControlView
     * @since 4.1.0.0
     */
    protected OwMaxMinButtonControlView createMaxMinButtonControlView(OwView view)
    {
        return new OwMaxMinButtonControlView(view);
    }

    /**(overridable)
     * Factory method for button component/view.
     * @return OwUnifyImageButtonView
     * @since 4.1.0.0
     */
    protected OwUnifyImageButtonView createButtonView()
    {
        return new OwUnifyImageButtonView();
    }

    protected OwUnifyImageButtonView getButtonView()
    {
        return buttonView;
    }
}
