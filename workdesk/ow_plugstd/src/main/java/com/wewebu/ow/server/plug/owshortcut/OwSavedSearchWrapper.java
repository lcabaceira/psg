package com.wewebu.ow.server.plug.owshortcut;

/**
 *<p>
 * Save Search Wrapper.<br/>
 * Model saved searches.<br/>
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
 * @since 3.0.0.0
 */
public class OwSavedSearchWrapper
{
    private String m_searchTemplateName;

    private String m_searchTemplateDisplayName;

    /**
     *
     * @return m_searchTemplateDisplayName
     */
    public String getSearchTemplateDisplayName()
    {
        return m_searchTemplateDisplayName;
    }

    /**
     *
     * @param searchTemplateDisplayName_p
     */
    public void setSearchTemplateDisplayName(String searchTemplateDisplayName_p)
    {
        m_searchTemplateDisplayName = searchTemplateDisplayName_p;
    }

    private String m_savedSearch;

    /**
     *
     * @param searchTemplateName_p
     * @param savedSearch_p
     */
    public OwSavedSearchWrapper(String searchTemplateName_p, String searchTemplateDisplayName_p, String savedSearch_p)
    {
        this.m_searchTemplateName = searchTemplateName_p;
        this.m_searchTemplateDisplayName = searchTemplateDisplayName_p;
        this.m_savedSearch = savedSearch_p;
    }

    /**
     *
     * @return Search Template Name <code>String</code>
     */
    public String getSearchTemplateName()
    {
        return m_searchTemplateName;
    }

    public void setSearchTemplateName(String searchTemplateName_p)
    {
        m_searchTemplateName = searchTemplateName_p;
    }

    /**
     *
     * @return Saved Search Name <code>String</code>
     */
    public String getSavedSearch()
    {
        return m_savedSearch;
    }

    /**
     *
     * @param savedSearch_p
     */
    public void setSavedSearch(String savedSearch_p)
    {
        m_savedSearch = savedSearch_p;
    }

    public String toString()
    {
        return " " + m_searchTemplateName + "_" + m_savedSearch;
    }

}
