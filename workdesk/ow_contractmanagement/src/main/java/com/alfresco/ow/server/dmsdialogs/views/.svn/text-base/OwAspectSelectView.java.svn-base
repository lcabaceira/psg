package com.alfresco.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.alfresco.ow.server.plug.owaddobject.OwAspectObjectClassProcessor.OwAspectGroup;
import com.alfresco.ow.server.plug.owaddobject.OwAspectObjectClassProcessor.OwAspectGroupSelectionHelper;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * OwAspectSelectView.<br/>
 * String configuration view.
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
public class OwAspectSelectView extends OwLayout
{
    /** Logger for this class */
    private static final Logger LOG = OwLog.getLogger(OwAspectSelectView.class);

    /** stores a collection of all strings available for configuration */
    //    protected List<String> m_AllStrings = new ArrayList<String>();

    protected OwAspectGroupSelectionHelper m_AspectGroupCollection = null;

    /** stores a collection of the user-selected (or previous configured) strings */
    //    protected List<String> m_ConfiguredStrings = new ArrayList<String>();

    /** web key of the available attributes */
    private static final String WEBKEY_AVAILABLE_ATTRIBUTES = "available_";
    /** web key of the selected attributes */
    private static final String WEBKEY_SELECTED_ATTRIBUTES = "selected_";
    /** node name of a combo select item for list properties */
    protected static final String COMBO_SELECT_ITEM_NODE_NAME = "comboselect";

    /** attribute name of a combo select item display name for list properties */
    protected static final String COMBO_SELECT_ITEM_DISPLAY_ATTR_NAME = "displayname";

    /** the combobox size */
    private static final int BOX_SIZE = 20;

    /**
     * sets the collections to process.
     * 
     * @param aspectGroupCollection_p  helper class with the aspect group collection (required param). 
     * @param preselectedAspects_p a collection of strings which must also be contained in allStrings_p and will be pre-selected. May be null also. 
      */
    public void setCollections(OwAspectGroupSelectionHelper aspectGroupCollection_p, List<String> preselectedAspects_p)
    {
        // check required param
        if (null == aspectGroupCollection_p)
        {
            String message = "The parameter 'aspectGroupCollection_p' is required!";
            LOG.error(message);
            throw new NullPointerException(message);
        }
        this.m_AspectGroupCollection = aspectGroupCollection_p;
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";
    }

    /** 
     * Called when the view should create its HTML content to be displayed.
     * 
     * @param w_p Writer object to write HTML to
     */
    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        w_p.write("<TABLE class='OwTaskGeneralText'>"); // table 4 cols, 2 rows
        // render the titles
        w_p.write("<TR>\n");
        String displayName = getDisplayName();
        String availableId = WEBKEY_AVAILABLE_ATTRIBUTES + String.valueOf(this.hashCode());
        String selectedId = WEBKEY_SELECTED_ATTRIBUTES + String.valueOf(this.hashCode());
        w_p.write("<TH class='OwSelectBoxTitle' id='" + availableId + "'>");
        w_p.write("<label for='" + availableId + "'>");
        w_p.write(getContext().localize1("server.dmsdialogs.views.OwAspectSelectView.available.label", "Available &nbsp; %1", displayName));
        w_p.write("</label>");
        w_p.write("</TH>\n");
        w_p.write("<TD>&nbsp;</TD>\n");
        w_p.write("<TH class='OwSelectBoxTitle' id='" + selectedId + "'>");
        w_p.write("<label for='" + selectedId + "'>");
        w_p.write(getContext().localize1("server.dmsdialogs.views.OwAspectSelectView.selected.label", "Selected &nbsp; %1", displayName));
        w_p.write("</label>");
        w_p.write("</TH>\n");
        w_p.write("<TD>&nbsp;</TD>\n");
        w_p.write("</TR>\n");

        // render the select boxes
        w_p.write("<TR>\n");

        // render first select box

        w_p.write("<TD><SELECT class='OwInputfield_long' size='" + BOX_SIZE + "' name='" + availableId + "' id='" + availableId + "'>\n");
        for (int pos = 0; pos < m_AspectGroupCollection.size(); pos++)
        {
            OwAspectGroup aspectGroup = m_AspectGroupCollection.getAspectGroup(pos);
            w_p.write("<OPTGROUP label='" + aspectGroup.getDisplayName() + "'>");
            for (Iterator<String> it = aspectGroup.getAspectTyps().iterator(); it.hasNext();)
            {
                String aspect = it.next();
                if (!m_AspectGroupCollection.isSelectedAspect(aspect))
                {
                    if (!aspectGroup.isMultiSelect() && m_AspectGroupCollection.hasSelectedAspects(aspectGroup))
                    {
                        String notSelectable = getContext().localize("server.dmsdialogs.views.OwAspectSelectView.notSelectable", "You can select only one aspect from this group!");
                        w_p.write("<OPTION title='" + notSelectable + "' value='" + aspect + "' disabled>");
                    }
                    else
                    {
                        w_p.write("<OPTION title='" + getAspectDescription(aspect) + "' value='" + aspect + "'>");
                    }

                    w_p.write(getLocalizedAspectDisplayName(aspect, aspectGroup.getAspectDisplayName(aspect)));
                    w_p.write("</OPTION>\n");
                }
            }
            w_p.write("</OPTGROUP>\n");
        }
        w_p.write("</SELECT>");
        w_p.write("</TD>\n");

        // render the toggle buttons
        String submitLeftShiftFunction = this.getFormEventURL("ShiftLeft", null);

        String submitRightShiftFunction = this.getFormEventURL("ShiftRight", null);

        w_p.write("<TD class='shiftButtons' width='60pt'>");
        String shiftLeftTooltip = getContext().localize("server.dmsdialogs.views.OwAspectSelectView.shiftleft", "Shift aspect left");
        String shiftLeftImageUrl = getContext().getDesignURL() + "/images/plug/pointer_left.png";

        w_p.write("<a href=\"" + submitLeftShiftFunction + "\">");
        w_p.write("<span class=\"OwAspect_icon\" style=\"background-image: url(");
        w_p.write(shiftLeftImageUrl);
        w_p.write(")\">");
        w_p.write("<span class='OwAspect_text'>" + shiftLeftTooltip + "</span>");
        w_p.write("</span>");
        w_p.write("</a>");

        w_p.write("<br>");
        w_p.write("<br>");
        String shiftRightTooltip = getContext().localize("server.dmsdialogs.views.OwAspectSelectView.shiftright", "Shift aspect right");
        String shiftRightImageUrl = getContext().getDesignURL() + "/images/plug/pointer_right.png";

        w_p.write("<a href=\"" + submitRightShiftFunction + "\">");

        w_p.write("<span class=\"OwAspect_icon\" style=\"background-image: url(");
        w_p.write(shiftRightImageUrl);
        w_p.write(")\">");
        w_p.write("<span class='OwAspect_text'>" + shiftRightTooltip + "</span>");
        w_p.write("</span>");

        w_p.write("</a>");
        w_p.write("</TD>\n");

        // render the second select box
        w_p.write("<TD><SELECT id='" + selectedId + "' class='OwInputfield_long' size='" + BOX_SIZE + "' name='" + selectedId + "'>\n");

        for (int pos = 0; pos < m_AspectGroupCollection.size(); pos++)
        {
            OwAspectGroup aspectGroup = m_AspectGroupCollection.getAspectGroup(pos);
            w_p.write("<OPTGROUP label='" + aspectGroup.getDisplayName() + "'>");
            for (Iterator<String> it = aspectGroup.getAspectTyps().iterator(); it.hasNext();)
            {
                String aspect = it.next();
                if (m_AspectGroupCollection.isSelectedAspect(aspect))
                {
                    w_p.write("<OPTION title='" + getAspectDescription(aspect) + "' value='" + aspect + "'>");
                    w_p.write(getLocalizedAspectDisplayName(aspect, aspectGroup.getAspectDisplayName(aspect)));
                    w_p.write("</OPTION>\n");
                }
            }
            w_p.write("</OPTGROUP>\n");
        }

        w_p.write("</SELECT>");
        w_p.write("</TD>\n");

        w_p.write("</TR>\n");
        w_p.write("</TABLE>\n");
    }

    /**
     * Event called when user clicked Shift Left Button.
     * 
     * @param request_p HttpServletRequest
     */
    public void onShiftLeft(HttpServletRequest request_p) throws Exception
    {
        String selectedAttribute = request_p.getParameter(WEBKEY_SELECTED_ATTRIBUTES + String.valueOf(this.hashCode()));
        if (selectedAttribute == null)
        {
            return;
        }

        this.getAspectGroupCollection().removeSelectedAspect(selectedAttribute);
    }

    /**
     * Event called when user clicked Shift Right Button
     * 
     * @param request_p  HttpServletRequest
     */
    public void onShiftRight(HttpServletRequest request_p) throws Exception
    {
        String selectedAttribute = request_p.getParameter(WEBKEY_AVAILABLE_ATTRIBUTES + String.valueOf(this.hashCode()));
        if (selectedAttribute == null)
        {
            return;
        }

        this.getAspectGroupCollection().setSelectedAspect(selectedAttribute);
    }

    /**
     * Returns the displayname of the plugin
     * @return the displayname of the plugin
     */
    protected String getDisplayName()
    {
        return getContext().localize("server.dmsdialogs.views.OwAspectSelectView.DisplayName", "Aspects");
    }

    /**
     * Returns the localized display name of the aspect.
     * If no localization available the configured display name will be used.
     * @param aspectTypeId_p
     * @param displayName
     * @return localized display name
     */
    protected String getLocalizedAspectDisplayName(String aspectTypeId_p, String displayName)
    {
        return this.getContext().localize("server.dmsdialogs.views.OwAspectSelectView.AspectDisplayName." + aspectTypeId_p, displayName);
    }

    /**
     * Returns the localized description of the aspect.
     * If no description is available an empty String will be returned.
     * @param aspectTypeId_p
     * @return localized description of the aspect
     */
    protected String getAspectDescription(String aspectTypeId_p)
    {
        return this.getContext().localize("server.dmsdialogs.views.OwAspectSelectView.AspectDescription." + aspectTypeId_p, "");
    }

    /**
     * Returns the collection of all strings. 
     * @return the collection of all strings. 
     */
    public OwAspectGroupSelectionHelper getAspectGroupCollection()
    {
        return this.m_AspectGroupCollection;
    }

    /**
     * returns the selected aspects.
     * @return the selected aspects.
     */
    public List<String> getSelectedAspects()
    {
        return new ArrayList(this.getAspectGroupCollection().getSeletedAspects());
    }
}
