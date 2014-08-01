package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;

/**
 *<p>
 * Object list view. Displays the results of searches.
 * In addition displays filter boxes to created filter search nodes. <br/><br/>
 * Use setObjectList and setColumnInfo to set the objects and columns to display.
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
public class OwObjectListViewFilterRow extends OwObjectListViewRow
{
    /** query key for the filter property */
    protected static final String FILTER_PROPERTY_KEY = "fprop";
    public static final String FILTER_OPERATOR_SELECT_ID = "filterop";

    /** construct a object list view
     * 
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListViewFilterRow(int iViewMask_p)
    {
        super(iViewMask_p);
    }

    /** construct a object list view
     */
    public OwObjectListViewFilterRow()
    {
        super();
    }

    /** (overridable) render the property sort column
     *  @param w_p java.io.Writer
     *  @param colInfo_p OwFieldColumnInfo
     * */
    protected void renderPropertyColumnHeader(java.io.Writer w_p, OwFieldColumnInfo colInfo_p) throws Exception
    {
        if (getFilter() != null)
        {
            // === render the filter boxes
            try
            {
                boolean fAktive = getFilter().getFilterEntry(colInfo_p.getPropertyName()).isActive();

                w_p.write("<a href=\"");
                w_p.write(getEventURL("EditFilter", FILTER_PROPERTY_KEY + "=" + colInfo_p.getPropertyName()));
                w_p.write("\" title=\"");
                String tooltipMessage = getContext().localize1("app.dmsdialogs.OwObjectListViewFilterRow.filtersettingstootlip", "Filter settings for %1", colInfo_p.getDisplayName(getContext().getLocale()));
                w_p.write(tooltipMessage);
                w_p.write("\">");

                w_p.write("<img  class=\"OwGeneralList_sortimg\" border='0' src=\"");
                w_p.write(getContext().getDesignURL());
                w_p.write("/images/OwObjectListView/");

                if (fAktive)
                {
                    w_p.write("filteractive.png\"");
                }
                else
                {
                    w_p.write("filter.png\"");
                }
                w_p.write(" alt=\"");
                w_p.write(tooltipMessage);
                w_p.write("\" title=\"");
                w_p.write(tooltipMessage);
                w_p.write("\" /></a>&nbsp;");
            }
            catch (OwObjectNotFoundException e)
            {
                // no filter for this property
            }
        }
        // === render default property sort header
        super.renderPropertyColumnHeader(w_p, colInfo_p);
    }

    /** set the filter to be used
     * @param filter_p an {@link com.wewebu.ow.server.dmsdialogs.views.OwObjectListView.OwFilter}
     */
    public void setFilter(OwFilter filter_p)
    {
        super.setFilter(filter_p);

        m_currentfilter = null;
        activateNextFilter();
    }

    /** currently editable filter */
    protected OwFilterEntry m_currentfilter;

    /** called when user clicks on a filter icon
     * */
    public void onEditFilter(HttpServletRequest request_p) throws Exception
    {
        OwFilterEntry filter = getFilter().getFilterEntry(request_p.getParameter(FILTER_PROPERTY_KEY));

        m_currentfilter = filter;
    }

    /** called when user clicks on a filter value button
     * */
    public void onSetFilterValue(HttpServletRequest request_p) throws Exception
    {
        getFieldManager().update(request_p, null, null);
        m_currentfilter.setActive(true);
        // persist changes
        getFilter().save(getDocument().getPersistentAttributeBagWriteable());

        // update child list
        getEventListner().onObjectListViewFilterChange(getFilterSearch(), getParentObject());
    }

    /** called when user changes a filter operator
     */
    public void onChangeFilterOperator(HttpServletRequest request_p) throws Exception
    {
        // set new operator
        int iOperator = Integer.parseInt(request_p.getParameter(FILTER_OPERATOR_SELECT_ID));

        m_currentfilter.setOperator(iOperator);
    }

    /** called when user clicks on a filter enable button
     * */
    public void onEnableFilter(HttpServletRequest request_p) throws Exception
    {
        m_currentfilter.toggle();
        getFieldManager().update(request_p, null, null);

        // persist changes
        getFilter().save(getDocument().getPersistentAttributeBagWriteable());

        // update child list
        getEventListner().onObjectListViewFilterChange(getFilterSearch(), getParentObject());

        activateNextFilter();
    }

    /** activate (show) the next active filter */
    protected void activateNextFilter()
    {
        if ((null == m_currentfilter) || (!m_currentfilter.isActive()))
        {
            m_currentfilter = null;

            Iterator<?> it = getFilter().values().iterator();
            while (it.hasNext())
            {
                OwFilterEntry filterentry = (OwFilterEntry) it.next();

                if (filterentry.isActive())
                {
                    m_currentfilter = filterentry;
                    break;
                }
            }
        }
    }

    /** render the view JSP 
     * @param w_p Writer object to write HTML to
     */
    protected void renderMainRegion(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwObjectListViewFilterRow.jsp", w_p);
    }

    public OwFilterEntry getCurrentFilter()
    {
        return m_currentfilter;
    }

    public String getEnableFilterURL()
    {
        return getFormEventURL("EnableFilter", null);
    }

    public String getSetFilterValueURL()
    {
        return getFormEventURL("SetFilterValue", null);
    }

    public String getChangeFilterOperatorURL()
    {
        return getFormEventURL("ChangeFilterOperator", null);
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return this.getContext().localize("dmsdialogs.views.OwObjectListViewFilterRow.title", "Filterable List");
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon URL, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return "/images/OwObjectListView/OwObjectListViewFilterRow.png";
    }
}