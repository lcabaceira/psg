package com.wewebu.ow.server.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Simple helper for localization and conversion of values to string. 
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
public class OwAboutMessageHelper
{

    public static List<String> getAboutInfo(OwAppContext ctx) throws IOException
    {
        List<String> messages = new ArrayList<>();
        String value = OwConfiguration.getEditionString();
        String message_dev_version = "Development Version - Only for development and testing. Not for production environments";
        messages.add(ctx.localize(value, message_dev_version));
        messages.add(ctx.localize("default.OwMainLayout.jsp.version", "Version") + ": " + ctx.localize(OwConfiguration.getVersionString(), OwConfiguration.getVersionString()) + " - " + OwConfiguration.getBuildNumber());
        messages.add(ctx.localize("default.OwMainLayout.jsp.locale", "Language") + ": " + OwHTMLHelper.encodeToSecureHTML(ctx.getLocale().toString()));
        return messages;
    }

    public static String getAboutInfoAsJs(OwAppContext ctx) throws IOException
    {
        StringBuilder message = new StringBuilder();
        List<String> aboutInfo = getAboutInfo(ctx);
        message.append("[");
        for (int i = 0; i < aboutInfo.size(); i++)
        {
            message.append("'").append(aboutInfo.get(i)).append("'");
            if (i < (aboutInfo.size() - 1))
            {
                message.append(",");
            }
        }
        message.append("]");
        return message.toString();
    }

    public static List<String> getContributors(OwAppContext ctx)
    {
        List<String> contributorsList = new ArrayList<String>();
        String contributorsMsg = ctx.localize("About.contributors", "Alfresco Workdesk Contributors...");
        contributorsList.add(contributorsMsg);
        contributorsList.add("Jens Dahl");
        contributorsList.add("Valentin Hemmert");
        contributorsList.add("Alexander Haag");
        contributorsList.add("Dennis Koch");
        contributorsList.add("Bogdan Horje");
        contributorsList.add("Dan Corneanu");
        contributorsList.add("Mihai Cozma");
        contributorsList.add("Silviu Dinuta");
        contributorsList.add("Hema Amara");
        contributorsList.add("...");
        contributorsList.add("Stefan Waldhauser");
        contributorsList.add("Christian Finzel");
        contributorsList.add("Frank Becker");
        contributorsList.add("Dr. Rainer Pausch");
        contributorsList.add("Sabine Otto");
        contributorsList.add("Martin Kappel");
        contributorsList.add("Barbara Lemke");
        contributorsList.add("Tobias Schneider");
        contributorsList.add("Johannes Wernhammer");
        contributorsList.add("Stefan Kopf");
        return contributorsList;
    }

    public static String getCopyright(OwAppContext ctx)
    {
        return ctx.localize("default.OwMainLayout.jsp.copyright", "Copyright &copy; Alfresco Software, Inc.");
    }
}
