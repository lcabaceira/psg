package com.wewebu.ow.server.field;

import java.util.Collection;

/**
 *<p>
 * Object Wrapper for XML Search Templates.<br/>
 * Parses Search Objects to create search forms and holds formating information for the search results.<br/><br/>
 * To be implemented by the DMS Adaptor.
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
public interface OwSearchTemplate extends OwFieldProvider
{
    /** type of versions to search for */
    public static final int VERSION_SELECT_DEFAULT = 1;
    /** type of versions to search for the released version */
    public static final int VERSION_SELECT_RELEASED = 2;
    /** type of versions to search for all versions */
    public static final int VERSION_SELECT_ALL = 3;
    /** type of versions to search for the current or latest version */
    public static final int VERSION_SELECT_CURRENT = 4;
    /** type of versions to search for all major versions */
    public static final int VERSION_SELECT_MAJORS = 5;
    /** type of versions to search for all minor versions */
    public static final int VERSION_SELECT_MINORS = 6;
    /** type of versions to search for the in process versions */
    public static final int VERSION_SELECT_IN_PROCESS = 7;
    /** type of versions to search for the checked out version */
    public static final int VERSION_SELECT_CHECKED_OUT = 8;

    /** init the search template so that the specified fields can be resolved
     *
     * @param fieldDefinitionProvider_p OwFieldDefinitionProvider to resolve fields
     */
    public abstract void init(OwFieldDefinitionProvider fieldDefinitionProvider_p) throws Exception;

    /** get the list of the column info tuple, which describe the result view 
     * @return Collection of OwFieldColumnInfo items
     */
    public abstract Collection getColumnInfoList() throws Exception;

    /** get the search tree created out of the template
     * @param fRefresh_p true = reload search from template, false = get cached search
     * @return OwSearchNode the search tree created out of the template
     */
    public abstract OwSearchNode getSearch(boolean fRefresh_p) throws Exception;

    /** get the optional HTML layout associated with this template
     * @return String containing a HTML fragment with place holders for the search parameters of the form {#<searchparametername>#}, or null if not available
     */
    public abstract String getHtmlLayout();

    /** check if the optional HTML layout is available
     * @return true if a HTML layout is available via getHtmlLayout
     */
    public abstract boolean hasHtmlLayout();

    /** get the optional JSP layout page associated with this template
     * @return String containing a JSP page name, or null if it is not available
     */
    public abstract String getJspLayoutPage();

    /** check if the optional JSP layout page is available
     * @return true if a JSP layout page is available via getJspLayoutPage
     */
    public abstract boolean hasJspLayoutPage();

    /** get a optional icon to be displayed with the search template, MUST be available before calling init
     * @return String icon path relative to /<design dir>/, or null if no icon is defined
     */
    public abstract String getIcon();

    /** get the template name, MUST be available before calling init
     * @return name of the template
     */
    public abstract String getName();

    /** get the template name, MUST be available before calling init
     * @param locale_p Locale to use
     * @return displayname of the template
     */
    public abstract String getDisplayName(java.util.Locale locale_p);

    /** get the version selection type
     *
     * @return int types of versions to search for as defined with VERSION_SELECT_...
     */
    public abstract int getVersionSelection();

    /** get the sort to use for the result list
     *
     * @param iMinSortCriteria_p int min number of sort criteria that the returned sort should support
     *
     * @return OwSort the number of max sort criteria can be higher than iMaxSortCriteria_p but not less
     */
    public abstract OwSort getSort(int iMinSortCriteria_p);

    /** get a priority rule for priority 
     * @return OwPriorityRule or null if undefined
     * */
    public abstract OwPriorityRule getPriorityRule();

    /** check if search template is already initialized
     * @return boolean true = template is initialized already, false = init was not called yet. 
     * */
    public abstract boolean isInitalized();

    /** get the default value for the  maximum size of results or 0 if not defined
     * 
     * @return int
     */
    public abstract int getDefaultMaxSize();

    /** check if searches can be saved
     * 
     */
    public abstract boolean canSaveSearch();

    /** check if saved searches can be deleted
     * 
     */
    public abstract boolean canDeleteSearch();

    /** check if saved searches can be updated
     * 
     */
    public abstract boolean canUpdateSearch();

    /** delete the saved search
     * 
     * @param name_p
     */
    public abstract void deleteSavedSearch(String name_p) throws Exception;

    /** get a collection of saved searches names
     * 
     * @return Collection of String names, or null if nothing is available
     * @throws Exception 
     */
    public abstract Collection getSavedSearches() throws Exception;

    /** init the search template with a saved search
     * 
     * @param name_p
     */
    public abstract void setSavedSearch(String name_p) throws Exception;

    /** get the name of the current set search, or null if no saved search is set
     * 
     */
    public abstract String getSavedSearch() throws Exception;

    /** save the current search
     * 
     * @param name_p
     */
    public abstract void saveSearch(String name_p) throws Exception;
}