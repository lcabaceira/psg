package com.wewebu.ow.server.app;

import java.io.IOException;
import java.io.Writer;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.ecm.OwClass;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.ui.OwView;

/**
 * Helper class for adding labels.
 *<p>
 * 
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
public class OwInsertLabelHelper
{
    public static void insertLabelValue(Writer w_p, String displayName, String strID_p) throws IOException
    {
        w_p.write("<label class=\"accessibility\" for=\"" + strID_p + "\">");
        w_p.write(displayName + ":");
        w_p.write("</label>");
    }

    public static void insertLabel(Writer w_p, String displayName, String strID_p, String styleClass) throws IOException
    {
        String classStr = (styleClass == null) ? "" : " class=\"" + styleClass + "\"";
        w_p.write("<label " + classStr + " for=\"" + strID_p + "\">");
        w_p.write(displayName);
        w_p.write("</label>");
    }

    public static void insertLabelWithSuffix(Writer w_p, String displayName, String strID_p, String suffix, String styleClass) throws IOException
    {
        String classStr = (styleClass == null) ? "" : " class=\"" + styleClass + "\"";
        w_p.write("<label " + classStr + " for=\"" + strID_p + "\">");
        w_p.write(displayName);
        if (suffix != null)
        {
            w_p.write(suffix);
        }
        w_p.write("</label>");
    }

    public static void insertLabel(Writer w_p, String strPlaceHolder_p, String displayName, OwView viewReference, String styleClass) throws Exception
    {
        if (viewReference instanceof OwObjectPropertyFormularView)
        {
            OwObjectPropertyFormularView view = (OwObjectPropertyFormularView) viewReference;
            view.insertLabel(w_p, strPlaceHolder_p, displayName, styleClass);
            return;
        }
        w_p.write(displayName);
    }

    public static OwClass[] fromFieldValue(Object value_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        try
        {
            OwClass[] classesValues = null;
            if (value_p != null)
            {
                if (fieldDef_p.isArray())
                {
                    Object[] values = (Object[]) value_p;
                    classesValues = new OwClass[values.length];
                    for (int i = 0; i < values.length; i++)
                    {
                        classesValues[i] = (OwClass) values[i];
                    }
                }
                else
                {
                    classesValues = new OwClass[] { (OwClass) value_p };
                }
            }
            return classesValues;
        }
        catch (ClassCastException cce)
        {
            throw new OwConfigurationException("OwFieldManagerControlClass can only handle fields of type OwClass.", cce);
        }
    }
}
