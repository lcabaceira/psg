package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSearchCriteria;

/**
 *<p>
 * ExtJS Object list view. Displays the results of searches with Ajax technologies.
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
public class OwObjectListViewFilterEXTJSGrid extends OwObjectListViewEXTJSGrid
{
    /** query key for the filter property */
    private static final String FILTER_PROPERTY_KEY = "fprop";

    public static final String FILTER_OPERATOR_SELECT_ID = "filterop";

    /** construct a object list view
     * 
     * @param iViewMask_p int combination of VIEW_MASK_... defined flags
     */
    public OwObjectListViewFilterEXTJSGrid(int iViewMask_p)
    {
        super(iViewMask_p);
    }

    /** construct a object list view
     */
    public OwObjectListViewFilterEXTJSGrid()
    {
        super();
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
        // get parameter
        String sParamFilter = request_p.getParameter(FILTER_PROPERTY_KEY);

        // lookup column, property and set filter
        OwFieldColumnInfo colinfo = ((OwAjaxColumnEntry) m_AjaxColnameToPropertyMap.get(sParamFilter)).getFieldColumnInfo();
        if (colinfo != null)
        {
            String strFilterProperty = colinfo.getPropertyName();
            if (strFilterProperty != null)
            {
                OwFilterEntry filter = getFilter().getFilterEntry(strFilterProperty);
                m_currentfilter = filter;
            }
        }
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

        OwSearchCriteria crit = m_currentfilter.getSearchNode().getCriteria();

        crit.setOperator(iOperator);
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
            if (null != getFilter())
            {
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
    }

    /** (overridable) render the JSP page
     * 
     * @param w_p Writer to write to
     */
    protected void renderMainRegion(Writer w_p) throws Exception
    {
        // redirect to render page
        serverSideDesignInclude("OwObjectListViewFilterEXTJSGrid.jsp", w_p);
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
        return this.getContext().localize("dmsdialogs.views.OwObjectListViewFilterEXTJSGrid.title", "Adaptable Filterable List");
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon URL, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return "/images/OwObjectListView/OwObjectListViewFilterEXTJSGrid.png";
    }

    /** to get additional form attributes used for the form
     *  override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        return "";
    }

}