package com.wewebu.ow.server.app;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwAppContext;

/**
 *<p>
 * Utility that returns different footer based on released edition.
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
public class OwAppEdition
{
    private String footerLocalized = "footerVersion";
    private String setupVersionLocalized = "setupVersionLocalized";

    /** package logger for the class */
    protected static final Logger LOG = OwLogCore.getLogger(OwAppEdition.class);

    /**
     * Returns footer text, if the implementation version contains the word "sprint" the edition is marked as naja warning
     * @param mainContext_p reference to the AppContext
     * @return String application customized footer
     */
    public String getEditionFooter(OwAppContext mainContext_p, HttpServletRequest request_p) throws Exception
    {
        footerLocalized += mainContext_p.getLocale();
        setupVersionLocalized += mainContext_p.getLocale();

        setupEdition(mainContext_p, request_p);

        return (String) request_p.getSession().getAttribute(footerLocalized);
    }

    /**
     * Set page footer based on release edition.
     * @param request_p HttpServletRequest
     * @throws Exception if an error has occurred
     */
    private void setupEdition(OwAppContext mainContext_p, HttpServletRequest request_p) throws Exception
    {
        if (!Boolean.valueOf(((String) request_p.getSession().getAttribute(setupVersionLocalized))).booleanValue())
        {
            //read the versions from manifest files
            String specificationVersion = "undefined";
            String implementationVersion = "undefined";
            java.util.jar.JarFile jar = null;
            try
            {
                String libURL = request_p.getSession().getServletContext().getRealPath("/WEB-INF/lib");
                java.io.File libFolder = new java.io.File(libURL);
                String[] jars = new String[] {};

                // get list of jars and return ow_core to retrieve version 
                if (libFolder.isDirectory())
                {
                    jars = libFolder.list(new java.io.FilenameFilter() {
                        public boolean accept(java.io.File dir_p, String name_p)
                        {
                            return (name_p.startsWith("ow_core_") && name_p.endsWith("jar"));
                        }
                    });
                }
                if (jars.length > 0)
                {
                    String ow_core = jars[0];
                    LOG.debug("Get the path of the ow_core lib: getSession().getServletContext().getRealPath(\"/WEB-INF/lib\")=" + libURL + "/" + ow_core);
                    jar = new java.util.jar.JarFile(libURL + "/" + ow_core);
                    //get version from manifest file
                    java.util.jar.Manifest manifestFile = jar.getManifest();
                    if (null != manifestFile)
                    {
                        java.util.jar.Attributes mainAttributes = manifestFile.getMainAttributes();
                        if (mainAttributes.getValue("Specification-Version") != null)
                        {
                            specificationVersion = mainAttributes.getValue("Specification-Version");
                        }
                        if (mainAttributes.getValue("Implementation-Version") != null)
                        {
                            implementationVersion = mainAttributes.getValue("Implementation-Version");
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                LOG.warn("OwAppEdition.setupEdition: Exception, cannot retrieve the software version of your Workdesk installation - maybe because the Application Server does not support this feature, or the ow_core_xxx.jar file cannot be found...");
            }
            finally
            {
                LOG.debug("OwAppEdition.setupEdition: The version of the application successfully setted....");
                request_p.getSession().setAttribute(setupVersionLocalized, "true");
                if (jar != null)
                {
                    jar.close();
                }
            }

            //Construct the version footer 
            String footerVersionTemp = "";
            specificationVersion = specificationVersion.toUpperCase();
            implementationVersion = implementationVersion.toUpperCase();

            if (specificationVersion.indexOf("ENTERPRISE") != -1)
            {
                if (implementationVersion.indexOf("SPRINT") != -1 || implementationVersion.indexOf("BETA") != -1 || implementationVersion.indexOf("ALPHA") != -1 || implementationVersion.indexOf("RC") != -1)
                {
                    footerVersionTemp = mainContext_p.getContext().localize("default.OwMainLayout.jsp.footer.development", "Development Version - Only for development and testing. Not for production environments");
                }
                else
                {
                    footerVersionTemp = mainContext_p.getContext().localize("default.OwMainLayout.jsp.footer.commercial", "Workdesk - Tested and fully supported by Alfresco Software, Inc.");
                }
            }
            else if (specificationVersion.indexOf("COMMUNITY") != -1)
            {
                footerVersionTemp = mainContext_p
                        .getContext()
                        .localize(
                                "default.OwMainLayout.jsp.footer.community",
                                "Workdesk Community - Supplied free of charge with no support, no certification, no maintenance, no warranty, and no indemnity by Alfresco Software, Inc. or its partners.<br />Looking for a supported and maintained edition? Go to <a href=\"http://www.alfresco.com\">www.alfresco.com</a>.");
            }
            else if (specificationVersion.indexOf("UNDEFINED") != -1)
            {
                footerVersionTemp = mainContext_p.getContext().localize("default.OwMainLayout.jsp.footer.development", "Development Version - Only for development and testing. Not for production environments");
            }

            //Set the version footer as session attribute
            request_p.getSession().setAttribute(footerLocalized, footerVersionTemp);
        }
    }
}