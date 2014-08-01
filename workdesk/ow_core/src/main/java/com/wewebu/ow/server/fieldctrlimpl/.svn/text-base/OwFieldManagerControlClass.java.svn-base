package com.wewebu.ow.server.fieldctrlimpl;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwFieldManagerException;
import com.wewebu.ow.server.ecm.OwClass;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Control to display a Image Property.
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
public class OwFieldManagerControlClass extends OwFieldManagerControl
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwFieldManagerControlClass.class);

    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (fieldDef_p.isArray())
        {
            Object[] values = (Object[]) value_p;
            if (values != null)
            {
                for (int i = 0; i < values.length; i++)
                {
                    insertSingleReadOnlyField(w_p, fieldDef_p, values[i]);
                    w_p.write("<br>");
                }
            }
        }
        else
        {
            insertSingleReadOnlyField(w_p, fieldDef_p, value_p);
        }
    }

    protected void insertSingleReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        // try to cast to OwClass
        OwClass classValue;
        try
        {
            classValue = (OwClass) value_p;
        }
        catch (ClassCastException cce)
        {
            throw new OwConfigurationException("OwFieldManagerControlClass can only handle fields of type OwClass.", cce);
        }
        if (null != classValue)
        {
            OwHTMLHelper.writeSecureHTML(w_p, retrieveDisplayName(classValue));
        }
    }

    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        OwClass[] classValues = fromFieldValue(field_p.getValue(), fieldDef_p);
        if (classValues == null)
        {
            classValues = new OwClass[0];
        }

        if (classValues.length > 0)
        {
            w_p.write("<select class=\"OwFieldControlClass\" size=\"");
            w_p.write("" + classValues.length);
            w_p.write("\"");
            w_p.write(" name=\"");
            w_p.write(strID_p);
            w_p.write("\" id=\"");
            w_p.write(strID_p);
            w_p.write("\" multiple>");
            for (int i = 0; i < classValues.length; i++)
            {
                insertClassOption(w_p, fieldDef_p, classValues[i], i);

            }
            w_p.write("</select>");
        }
    }

    /**
     * Converts the field value to an array of {@link OwClass} objects considering the field definition.
     * If the field is not defined as an array an array with size 1 is returned containing the field value.
     * @param value_p the field value 
     * @param fieldDef_p the field definition
     * @return the field value as an array of {@link OwClass} objects
     * @throws Exception
     * @since 3.0.0.0
     */
    protected OwClass[] fromFieldValue(Object value_p, OwFieldDefinition fieldDef_p) throws Exception
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

    /**
     * Converts an {@link OwClass} array to a field value considering the field definition
     * If the field is not defined as an array the value at index 0 is returned.
     * @param classValues_p
     * @param fieldDef_p
     * @return the field value 
     * @throws Exception
     * @since 3.0.0.0
     */
    protected Object toFieldValue(OwClass[] classValues_p, OwFieldDefinition fieldDef_p) throws Exception
    {
        if (classValues_p != null)
        {
            if (fieldDef_p.isArray())
            {
                return classValues_p;
            }
            else
            {
                if (classValues_p.length > 0)
                {
                    return classValues_p[0];
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Attempts to retrieve the display name of the class defined by the given {@link OwClass} parameter.
     * If the display name can not be retrieve the  system class name is returned.
     * If the {@link OwClass} is <code>null</code> an empty string will be returned.
     * @param owClass_p
     * @return the display name of the class defined by the given {@link OwClass} parameter
     * @since 3.0.0.0 
     */
    protected String retrieveDisplayName(OwClass owClass_p)
    {
        if (null != owClass_p)
        {
            OwAppContext context = getContext();
            String className = owClass_p.getClassName();
            if (context instanceof OwRoleManagerContext)
            {

                try
                {
                    OwRoleManagerContext rmContext = (OwRoleManagerContext) context;
                    OwNetwork network = rmContext.getNetwork();

                    String resourceName = owClass_p.getResourceName();
                    OwResource resource = network.getResource(resourceName);

                    OwObjectClass objectClass = network.getObjectClass(className, resource);
                    return objectClass.getDisplayName(context.getLocale());
                }
                catch (Exception e)
                {
                    LOG.debug("OwFieldManagerControlClass.retrieveDisplayName():Could not find class " + className + " to retrieve display name!Falling back on search template defined name!", e);
                    return className;
                }

            }
            else
            {
                return className;
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * HTML class defining &lt;option&gt; element insertion method 
     * @param w_p
     * @param fieldDef_p
     * @param value_p
     * @param optionIndex_p index at which the class will be rendered in the select control
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void insertClassOption(Writer w_p, OwFieldDefinition fieldDef_p, OwClass value_p, int optionIndex_p) throws Exception
    {
        String option = "";
        boolean selected = false;

        if (null != value_p)
        {
            option = retrieveDisplayName(value_p);
            selected = value_p.isEnabled();
        }
        String optionClass = "OwFieldControlClass_odd";
        if ((Math.abs(optionIndex_p) + 1) % 2 == 0)
        {
            optionClass = "OwFieldControlClass_even";
        }
        w_p.write("<option class=\"");
        w_p.write(optionClass);
        w_p.write("\"  value=\"");
        OwHTMLHelper.writeSecureHTML(w_p, "" + optionIndex_p);
        w_p.write("\"");
        if (selected)
        {
            w_p.write(" selected>");
        }
        else
        {
            w_p.write(">");
        }

        OwHTMLHelper.writeSecureHTML(w_p, option);

        w_p.write("</option>");
    }

    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Object retObject = null;
        String[] classSelections = request_p.getParameterValues(strID_p);
        if (classSelections == null)
        {
            classSelections = new String[0];
        }
        OwClass[] values = fromFieldValue(value_p, fieldDef_p);
        OwClass[] retClasses = new OwClass[values.length];
        for (int i = 0; i < values.length; i++)
        {
            OwClass classValue = values[i];
            retClasses[i] = new OwClass(classValue.getObjectType(), classValue.getClassName(), classValue.getBaseClassName(), classValue.getResourceName(), false, classValue.isIncludeSubclasses());
        }

        boolean noClassSelected = true;

        for (int i = 0; i < classSelections.length; i++)
        {
            int index = 0;
            try
            {
                index = Integer.parseInt(classSelections[i]);

                if (index >= 0 && index < retClasses.length)
                {
                    noClassSelected = false;
                    OwClass classRetValue = retClasses[index];
                    retClasses[index] = new OwClass(classRetValue.getObjectType(), classRetValue.getClassName(), classRetValue.getBaseClassName(), classRetValue.getResourceName(), true, classRetValue.isIncludeSubclasses());
                }
                else
                {
                    LOG.error("OwFieldManagerControlClass.updateField() : Invalid update index : " + index + ". Indexes greater than 0 and less than " + retClasses.length + " expected!");
                }

            }
            catch (NumberFormatException nfe)
            {
                LOG.warn("OwFieldManagerControlClass.updateField(): NumberFormatException", nfe);
            }

        }
        retObject = toFieldValue(retClasses, fieldDef_p);

        // check for required value
        if (fieldDef_p.isRequired() && (noClassSelected || (retObject == null) || (retObject.toString().length() == 0)))
        {
            throw new OwFieldManagerException(getContext().localize("app.OwStandardFieldManager.requiredfield", "Das Feld ist erforderlich und muss einen Wert erhalten."));
        }
        return retObject;
    }

}