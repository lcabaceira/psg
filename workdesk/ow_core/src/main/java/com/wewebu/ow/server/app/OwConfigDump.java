package com.wewebu.ow.server.app;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wewebu.ow.server.conf.OwBaseConfiguration;
import com.wewebu.ow.server.conf.OwBaseConfiguration.OwPluginTypeDefinition;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.ui.OwWebApplication;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Configuration View to display the whole configuration of the application and its plugins. 
 * Used by the administrator and developer only.
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
public class OwConfigDump
{
    //    private static final Logger LOG = OwLogCore.getLogger(OwConfigDump.class);

    /** instance of the application Configuration  */
    private OwConfiguration m_Configuration;

    /** instance of the main application context 
     *  NOTE:   We do not initialize the context here,
     *          since we want the configuration bean to work under all circumstances.
     *          No Network adapter and no plugins are created.
     */
    private OwMainAppContext m_Context;
    /**
     * List of tags to be hidden by htmlWrite
     */
    private Map<String, String> hiddenTags = new HashMap<String, String>();;

    /**
     * Product Name displayed in "configdump.jsp" page
     */
    private String configDumpProduct = "Alfresco Workdesk";

    /** version number class to retrieve version numbers form class objects
     */
    protected static class VersionNumber
    {
        public String m_strReleaseType = "";

        public boolean m_fImplementation;
        /** minor version number or -1 if not set */
        public int m_iMinor = -1;
        /** major version number or -1 if not set */
        public int m_iMajor = -1;
        /** service pack version number or -1 if not set */
        public int m_iServicePack = -1;
        /** hot fix version number or -1 if no set */
        public int m_iHotFix = -1;

        /** construct VersionNumber object
         *  retrieve the version numbers from the static fields of a class object
         *
         * @param class_p the class object with the static version declaration
         * @param fImplementation_p true = retrieve the implementation numbers, false = retrieve the Interface numbers
         */
        public VersionNumber(Class<?> class_p, boolean fImplementation_p)
        {
            m_fImplementation = fImplementation_p;

            try
            {
                //read version number from OwConfiguration
                class_p = Class.forName("com.wewebu.ow.server.app.OwConfiguration");
            }
            catch (ClassNotFoundException e1)
            {
                e1.printStackTrace();
            }

            try
            {
                // === get version numbers from static fields
                if (fImplementation_p)
                {
                    try
                    {
                        m_iMinor = class_p.getField("IMPLEMENTATION_MINOR_VERSION").getInt(null);
                    }
                    catch (NoSuchFieldException ex)
                    {
                    }
                    try
                    {
                        m_iMajor = class_p.getField("IMPLEMENTATION_MAJOR_VERSION").getInt(null);
                    }
                    catch (NoSuchFieldException ex)
                    {
                    }
                    try
                    {
                        m_iServicePack = class_p.getField("IMPLEMENTATION_SERVICEPACK_VERSION").getInt(null);
                    }
                    catch (NoSuchFieldException ex)
                    {
                        m_iServicePack = class_p.getField("IMPLEMENTATION_UPDATE_VERSION").getInt(null);
                    }
                    try
                    {
                        m_iHotFix = class_p.getField("IMPLEMENTATION_HOTFIX_VERSION").getInt(null);
                    }
                    catch (NoSuchFieldException ex)
                    {
                        m_iHotFix = class_p.getField("IMPLEMENTATION_FIXPACK_VERSION").getInt(null);
                    }
                    try
                    {
                        try
                        {
                            m_strReleaseType = " (" + (String) class_p.getField("IMPLEMENTATION_RELEASE_TYPE").get(null) + ")";
                        }
                        catch (NoSuchFieldException ex)
                        {
                            //older Alfresco Workdesk Versions used IMPLEMENTAION_ instead of IMPLEMENTATION_ 
                            m_strReleaseType = " (" + (String) class_p.getField("IMPLEMENTAION_RELEASE_TYPE").get(null) + ")";
                        }
                    }
                    catch (Exception e)
                    {
                        // do nothing 
                    }
                }
                else
                {
                    m_iMinor = class_p.getField("INTERFACE_MINOR_VERSION").getInt(null);
                    m_iMajor = class_p.getField("INTERFACE_MAJOR_VERSION").getInt(null);
                }
            }
            catch (Exception e)
            {
                // do nothing leave versions to -1
            }
        }

        /** construct VersionNumber object
         *  retrieve the version numbers from the static fields of a class object
         *
         * @param sVersion_p the version string
         */
        public VersionNumber(String sVersion_p)
        {
            try
            {
                // === get version numbers from string
                String[] VersionNumbers;
                VersionNumbers = sVersion_p.split("\\.");

                if (4 == VersionNumbers.length)
                {
                    m_fImplementation = true;

                    m_iMajor = Integer.parseInt(VersionNumbers[0]);
                    m_iMinor = Integer.parseInt(VersionNumbers[1]);
                    m_iServicePack = Integer.parseInt(VersionNumbers[2]);
                    m_iHotFix = Integer.parseInt(VersionNumbers[3]);
                }
                else if (3 == VersionNumbers.length)
                {
                    m_fImplementation = true;

                    m_iMajor = Integer.parseInt(VersionNumbers[0]);
                    m_iMinor = Integer.parseInt(VersionNumbers[1]);
                    m_iServicePack = Integer.parseInt(VersionNumbers[2]);
                }
                else if (2 == VersionNumbers.length)
                {
                    m_fImplementation = false;

                    m_iMajor = Integer.parseInt(VersionNumbers[0]);
                    m_iMinor = Integer.parseInt(VersionNumbers[1]);
                }
            }
            catch (Exception e)
            {
                // do nothing leave versions to -1
            }
        }

        /** create version string
         */
        public String toString()
        {
            if (m_fImplementation)
            {
                return String.valueOf(m_iMajor) + "." + String.valueOf(m_iMinor) + "." + String.valueOf(m_iServicePack) + "." + String.valueOf(m_iHotFix) + m_strReleaseType;
            }
            else
            {
                return String.valueOf(m_iMajor) + "." + String.valueOf(m_iMinor) + m_strReleaseType;
            }
        }

    }

    /** write a dump of the installed plugins in the plugin descriptor
     * @param htmlWriter_p Writer object to write HTML to
     * @param def_p type of the requested plugin ow_main, ow_docfunction...
     * @param strTitle_p title to be displayed
     */
    private void dumpPlugins(Writer htmlWriter_p, OwPluginTypeDefinition def_p, String strTitle_p) throws Exception
    {
        htmlWriter_p.write("<tr bgcolor='#999999'><td class='title' colspan='2'>Plugins: ");
        htmlWriter_p.write(strTitle_p);
        htmlWriter_p.write("</td><tr>");

        // Dump interface version and implementation version of derived adapter
        List<?> plugIns = m_Configuration.getAllowedPlugins(def_p.getType());
        if (null != plugIns)
        {
            // Iterate over area plugins
            for (int i = 0; i < plugIns.size(); i++)
            {
                // === create node wrapper to access the values
                OwXMLUtil nodeWrapper = (OwXMLUtil) plugIns.get(i);
                String strClassName = nodeWrapper.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_CLASSNAME, "undef. classname");

                String strPluginName = nodeWrapper.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_NAME, "no Plugin name defined");

                String version = nodeWrapper.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_VERSION, "no version defined");

                htmlWriter_p.write("<tr>");

                try
                {
                    if (!OwBaseConfiguration.PLUGINTYPE_APPLICATION.equals(def_p.getType()))
                    {
                        Class.forName(strClassName);
                    }
                    htmlWriter_p.write("<td valign=\"top\"><b>");
                    htmlWriter_p.write(strPluginName);
                    htmlWriter_p.write("</b><br />");
                    htmlWriter_p.write(strClassName);
                    htmlWriter_p.write(" - ");
                    htmlWriter_p.write(version);
                }
                catch (Exception e)
                {
                    htmlWriter_p.write("<td valign=\"top\" bgcolor=\"#ff0000\">");
                    htmlWriter_p.write(strPluginName);
                    htmlWriter_p.write("<br />ERROR: ");
                    htmlWriter_p.write("<a href=\"javascript:toggleElementDisplay('");
                    htmlWriter_p.write(strPluginName == null ? "null" : strPluginName);
                    htmlWriter_p.write(String.valueOf(i));
                    htmlWriter_p.write("');\">");
                    htmlWriter_p.write(e.getLocalizedMessage());
                    htmlWriter_p.write("</a><br /><span style=\"display: none;\" id=\"");
                    htmlWriter_p.write(strPluginName == null ? "null" : strPluginName);
                    htmlWriter_p.write(String.valueOf(i));
                    htmlWriter_p.write("\">");
                    htmlWriter_p.write(e.toString());
                    htmlWriter_p.write("</span>");
                }
                htmlWriter_p.write("</td>");

                htmlWriter_p.write("<td valign=\"top\">");
                htmlWriter_p.write("<a href=\"javascript:toggleElementDisplay('");
                htmlWriter_p.write(strClassName == null ? "null" : strClassName);
                htmlWriter_p.write(String.valueOf(i));
                htmlWriter_p.write("');\">XML-Config</a><br/>");
                htmlWriter_p.write("<span style=\"display: none;\" id=\"");
                htmlWriter_p.write(strClassName == null ? "null" : strClassName);
                htmlWriter_p.write(String.valueOf(i));
                htmlWriter_p.write("\">");
                nodeWrapper.writeHtmlDumpFiltered(htmlWriter_p, getHiddenTags());
                htmlWriter_p.write("</span>");
                htmlWriter_p.write("</td></tr>");
            }
        }
    }

    private void dumpManager(Writer htmlWriter_p, String strName_p, String strInterfaceClassName_p, OwXMLUtil configNode_p) throws Exception
    {
        String className = configNode_p.getSafeTextValue(OwBaseConfiguration.PLUGIN_NODE_CLASSNAME, null);

        // Dump interface version of adapter base
        htmlWriter_p.write("<tr bgcolor='#999999'><td colspan=\"2\">");
        htmlWriter_p.write(strName_p);
        htmlWriter_p.write("</td><tr>");

        if (className != null)
        {
            try
            {
                Class.forName(className);
                htmlWriter_p.write("<td valign=\"top\">");
                htmlWriter_p.write(className);
            }
            catch (Exception e)
            {
                htmlWriter_p.write("<td valign=\"top\" bgcolor=\"#ff0000\">");
                htmlWriter_p.write("<br />ERROR: ");
                htmlWriter_p.write(e.getLocalizedMessage());
            }
        }

        htmlWriter_p.write("</td><td valign=\"top\">");
        htmlWriter_p.write("<a href=\"javascript:toggleElementDisplay('" + className + "');\">XML-Config</a><br/>");
        htmlWriter_p.write("<span style=\"display: none;\" id=\"" + className + "\">");
        configNode_p.writeHtmlDumpFiltered(htmlWriter_p, this.getHiddenTags());
        htmlWriter_p.write("</span>");

        htmlWriter_p.write("</td></tr>");
    }

    /** print the optional history stack */
    /*public void DumpHistory(Writer HTMLWriter,String[] strHistory_p) throws Exception
    {
        HTMLWriter.write("<div style='margin-left: 10pt;font-size: 8pt;'><ul>");
        if ( strHistory_p != null )
        {
            for (int i=0;i <strHistory_p.length;i++)
            {
                HTMLWriter.write("<li>" + strHistory_p[i] + "</li>");
            }
        }
        HTMLWriter.write("</ul></div>");
    }*/

    @SuppressWarnings("rawtypes")
    private void dumpEnvironment(Writer htmlWriter_p) throws Exception
    {
        htmlWriter_p.write("<table border='1' cellpadding='4' cellspacing='0' bordercolor='#000000'>");
        Iterator it = System.getProperties().entrySet().iterator();
        while (it.hasNext())
        {
            htmlWriter_p.write("<tr>");

            Map.Entry entry = (Map.Entry) it.next();
            Object k = entry.getKey();
            Object v = entry.getValue();

            htmlWriter_p.write("<td>");
            htmlWriter_p.write(k.toString());
            htmlWriter_p.write("</td>");
            htmlWriter_p.write("<td>");
            htmlWriter_p.write(v.toString());
            htmlWriter_p.write("</td>");

            htmlWriter_p.write("</tr>");
        }

        htmlWriter_p.write("</table>");
    }

    /** called by the JSP page when the view should create its HTML content to be displayed.
     * 
     * optionally set a prefix to distinguish several different applications.
     * The role manager will filter the allowed plugins, MIME settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     *
     *   @param context_p ServletContext
     *   @param request_p  HttpServletRequest
     *   @param response_p HttpServletResponse
     */
    public void handleRequest(ServletContext context_p, HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // === Init session
        PrintWriter htmlWriter = response_p.getWriter();

        HttpSession Session = request_p.getSession();
        m_Context = (OwMainAppContext) Session.getAttribute(OwWebApplication.CONTEXT_KEY);

        try
        {
            if (m_Context == null)
            {
                // === not logged on yet, create session
                m_Context = new OwMainAppContext();
                m_Context.setRequest(context_p, request_p, response_p);
                m_Configuration = m_Context.getConfiguration();
                m_Configuration.init(m_Context);
            }
            else
            {
                m_Configuration = m_Context.getConfiguration();
            }
        }
        catch (Exception e)
        {
            htmlWriter.write("System initialization failure... <br><small>" + e.getMessage() + "</small>");
        }

        // === Dump Workdesk configuration
        htmlWriter.write("<h3> " + this.configDumpProduct + " - Configuration Dump</h3>");

        try
        {
            String message_dev_version = "Development Version - Only for development and testing. Not for production environments";
            htmlWriter.write(m_Context.localize(OwConfiguration.getEditionString(), message_dev_version));
        }
        catch (Exception e)
        {
            htmlWriter.write("System initialization failure... Cannot read the Workdesk version");
        }

        // === display the configuration
        htmlWriter.write("<p><table border='1' cellpadding='4' cellspacing='0' bordercolor='#000000'>");

        // Dump implementation version of core system
        VersionNumber coreSystemImplementationVersion = new VersionNumber(Class.forName("com.wewebu.ow.server.app.OwConfiguration"), true);
        htmlWriter.write("<tr bgcolor='#999999'><td>CORE System (OwConfiguration)<BR><small>for detailed information perform:<BR>Check all required Java Packages (jar files & libraries)</td><td>");
        htmlWriter.write(coreSystemImplementationVersion.toString());
        htmlWriter.write("</td><tr>");

        htmlWriter.write("<tr><td>LOCALE:</td><td>");

        htmlWriter.write(m_Context.getLocale() != null ? m_Context.getLocale().toString() : "");

        htmlWriter.write("</td></tr>");

        try
        {
            dumpManager(htmlWriter, "EcmAdapter", "com.wewebu.ow.server.ecm.OwNetwork", m_Configuration.getNetworkAdaptorConfiguration());
            dumpManager(htmlWriter, "RoleManager", "com.wewebu.ow.server.role.OwRoleManager", m_Configuration.getRoleManagerConfiguration());
            dumpManager(htmlWriter, "FieldManager", "com.wewebu.ow.server.app.OwFieldManager", m_Configuration.getFieldManagerConfiguration());
            dumpManager(htmlWriter, "HistoryManager", "com.wewebu.ow.server.history.OwHistoryManager", m_Configuration.getHistoryManagerConfiguration());
            dumpManager(htmlWriter, "MandatorManager", "com.wewebu.ow.server.mandator.OwMandatorManager", m_Configuration.getMandatorManagerConfiguration());
        }
        catch (Exception e)
        {
            htmlWriter.write("<tr><td colspan='2'>System initialization failure... Cannot print out the EcmAdapter & Managers</td></tr>");
        }

        // === Plugins
        try
        {
            for (int i = 0; i < OwBaseConfiguration.getPluginTypeDefinitions().length; i++)
            {
                dumpPlugins(htmlWriter, OwBaseConfiguration.getPluginTypeDefinitions()[i], OwBaseConfiguration.getPluginTypeDefinitions()[i].getDisplayName(m_Context.getLocale()));
            }
        }
        catch (Exception e)
        {
            htmlWriter.write("<tr bgcolor='#ff0000'><td colspan='2'>");

            htmlWriter.write("To see the plugin descriptions, you need to log on first...");

            htmlWriter.write("</tr>");
        }

        htmlWriter.write("</table>");

        // === dump role manager
        dumpRoleData(htmlWriter);

        // === Dump Workdesk configuration
        htmlWriter.write("<h3>Java Runtime Dump</h3><p>");
        dumpEnvironment(htmlWriter);
    }

    /** dump current role manager data
     * 
     * @param htmlWriter_p
     * @throws Exception 
     */
    private void dumpRoleData(PrintWriter htmlWriter_p) throws Exception
    {
        // === display the configuration
        htmlWriter_p.write("<br><table border='1' cellpadding='4' cellspacing='0' bordercolor='#000000'>");

        if (m_Context.getCurrentUser() != null)
        {

            htmlWriter_p.write("<tr bgcolor='#999999'><td colspan='2'>Roles</td></tr>");

            // === dump user info
            try
            {
                htmlWriter_p.write("<tr><td>Userinfo</td>");

                htmlWriter_p.write("<td><ul>");
                htmlWriter_p.write("<li>UserID: ");
                String value = m_Context.getCurrentUser().getUserID();
                htmlWriter_p.write(value != null ? value : "Error: Null value for id is invalid!");

                htmlWriter_p.write("</li><li>UserLongName: ");
                value = m_Context.getCurrentUser().getUserLongName();
                htmlWriter_p.write(value != null ? value : "Error: Null value for long name is invalid!");

                htmlWriter_p.write("</li><li>UserName: ");
                value = m_Context.getCurrentUser().getUserName();
                htmlWriter_p.write(value != null ? value : "Error: Null value for user name is invalid!");

                htmlWriter_p.write("</li><li>UserEmailAddress: ");
                String userEmailAdress = m_Context.getCurrentUser().getUserEmailAdress();
                if (userEmailAdress != null && !userEmailAdress.equals(""))
                {
                    htmlWriter_p.write(userEmailAdress);
                }
                else
                {
                    htmlWriter_p.write("N/A");
                }
                htmlWriter_p.write("</li></ul></td></tr>");
            }
            catch (Exception e)
            {
                htmlWriter_p.write("<tr><td>Exception:</td><td>Could not get userinfo; ");
                htmlWriter_p.write(e.getMessage());
                htmlWriter_p.write("</td></tr>");
            }

            // === dump role info
            try
            {
                htmlWriter_p.write("<tr><td>User Roles:</td>");

                htmlWriter_p.write("<td><ul>");
                @SuppressWarnings("unchecked")
                Collection<String> roleNames = m_Context.getCurrentUser().getRoleNames();
                if (roleNames != null)
                {
                    Iterator<String> roles = roleNames.iterator();
                    while (roles.hasNext())
                    {
                        htmlWriter_p.write("<li>");
                        String value = roles.next();
                        if (value != null)
                        {
                            htmlWriter_p.write(value);
                        }
                        else
                        {
                            htmlWriter_p.write("<b>Error: Invalid null value contained as Role Name</b>");
                        }
                        htmlWriter_p.write("</li>");
                    }
                }
                htmlWriter_p.write("</ul></td></tr>");
            }
            catch (Exception e)
            {
                htmlWriter_p.write("<tr><td>Exception:</td><td>Could not get roleinfo; ");
                htmlWriter_p.write(e.getMessage());
                htmlWriter_p.write("</td></tr>");
            }

            // === dump resource info
            try
            {
                for (int iCats = 0; iCats < OwRoleManager.m_predefinedcategories.length; iCats++)
                {
                    int iCategorie = OwRoleManager.m_predefinedcategories[iCats];
                    String sCategorieName = m_Context.getRoleManager().getCategoryDisplayName(m_Context.getLocale(), iCategorie);

                    htmlWriter_p.write("<tr><td>Categorie: ");
                    htmlWriter_p.write(sCategorieName == null ? "Error: DisplayName not available for" + Integer.toString(iCategorie) : sCategorieName);
                    htmlWriter_p.write("</td><td><ul>");

                    Iterator<?> catIT = m_Context.getRoleManager().getAllowedResources(iCategorie).iterator();
                    while (catIT.hasNext())
                    {
                        htmlWriter_p.write("<li>");
                        String value = (String) catIT.next();
                        htmlWriter_p.write(value == null ? "Error: Null value provided as allowed resource" : value);
                        htmlWriter_p.write("</li>");
                    }

                    htmlWriter_p.write("</ul></td></tr>");
                }
            }
            catch (Exception e)
            {
                htmlWriter_p.write("<tr><td>Exception:</td><td>Could not get role resources; ");
                htmlWriter_p.write(e.getMessage());
                htmlWriter_p.write("</td></tr>");
            }
        }
        else
        {
            htmlWriter_p.write("<tr><td>User not logged in, skipping listing of User information</td></tr>");
        }
        htmlWriter_p.write("</table>");
    }

    /**
     * Return values and tag to hide
     */
    public Map<String, String> getHiddenTags()
    {
        return hiddenTags;
    }

    /**
     * 
     */
    public void setHiddenTags(Map<String, String> hiddenTags)
    {
        this.hiddenTags = hiddenTags;
    }

    /**
     * 
     */
    public void setConfigDumpProduct(String product)
    {
        this.configDumpProduct = product;
    }
}