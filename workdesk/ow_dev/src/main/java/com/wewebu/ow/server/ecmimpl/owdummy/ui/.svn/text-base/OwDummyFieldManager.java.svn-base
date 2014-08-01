package com.wewebu.ow.server.ecmimpl.owdummy.ui;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwFieldManagerException;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardFieldManager;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.owdummy.OwDummyFileObject;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Standard Implementation of the Property User Interface.<br/>
 * Displays Property as HTML and creates HTML Form Elements for editing properties,
 * also performs Validation.<br/>
 * NOTE: This class is instantiated once for a block of properties. Is only one static instance of this class in one application.<br/><br/>
 * To be implemented with the specific DMS system.
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
public class OwDummyFieldManager extends OwStandardFieldManager
{
    /** Formats and displays the value attached to the PropertyClass in HTML
     * @param w_p Writer object to write HTML to
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object Value to be displayed
     */
    public void insertReadOnlyFieldInternal(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (value_p != null)
        {
            if (fieldDef_p.getClassName().equals(OwDummyFileObject.OwDummyFileObjectClass.PERSONALNUMMER_PROPERTY))
            {
                // === Personal number formating
                String strFormatedNumber = (String) value_p;

                if (strFormatedNumber.length() == 6)
                {
                    String strPrefix = strFormatedNumber.substring(0, 1);
                    String strNumber = strFormatedNumber.substring(1);

                    strFormatedNumber = "<b>" + strPrefix + "</b> - " + strNumber;
                }

                w_p.write(strFormatedNumber);
            }
            else if (fieldDef_p.getClassName().equals(OwDummyFileObject.OwDummyFileObjectClass.IMAGE_PROPERTY))
            {
                // === image path from dummy adapter
                // the image property is another OwObject, we just need to know the upload URL in order to display it.
                String strImageUploadURL = getContext().getBaseURL() + "/getContent?" + OwMimeManager.DMSID_KEY + "=" + ((OwObject) value_p).getDMSID();
                String toolTip = getContext().localize1("owdummy.ui.OwDummyFieldManager.propertyimagetooltip", "Image of property %1", fieldDef_p.getDisplayName(getContext().getLocale()));
                w_p.write("<img src=\"" + strImageUploadURL + "\" alt=\"" + toolTip + "\" title=\"" + toolTip + "\"/>");
            }
            else if (fieldDef_p.getClassName().equals(OwDummyFileObject.OwDummyFileObjectClass.MALE_PROPERTY))
            {
                // === gender boolean from dummy adapter    
                if (((Boolean) value_p).booleanValue())
                {
                    // === male
                    w_p.write(getContext().localize("owdummy.ui.OwDummyFieldManager.male", "male"));
                }
                else
                {
                    // === female    
                    w_p.write(getContext().localize("owdummy.ui.OwDummyFieldManager.female", "female"));
                }
            }
            else
            {
                // === all other types
                // use standard implementation
                super.insertReadOnlyFieldInternal(w_p, fieldDef_p, value_p);
            }
        }
    }

    // singleton for the PersonalNumberPrefixes
    protected static final PersonalNumberPrefixes m_PersonalNumberPrefixes = new PersonalNumberPrefixes();

    /**
     *<p>
     * PersonalNumberPrefixes.
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
    public static class PersonalNumberPrefixes
    {
        public java.util.Map m_prefixes = new java.util.HashMap();

        public PersonalNumberPrefixes()
        {
            m_prefixes.put("I", "Internal");
            m_prefixes.put("M", "Management");
            m_prefixes.put("F", "Freelancer");
            m_prefixes.put("T", "Trainee");
        }

        public String toString()
        {
            StringBuffer strRet = new StringBuffer();

            java.util.Iterator it = m_prefixes.keySet().iterator();

            boolean fAddDelimiter = false;

            while (it.hasNext())
            {
                if (fAddDelimiter)
                {
                    strRet.append(", ");
                }

                String strKey = (String) it.next();
                //strRet.append(" : ").append(m_prefixes.get(strKey));
                strRet.append(strKey + " (for " + m_prefixes.get(strKey) + ")");

                fAddDelimiter = true;
            }

            return strRet.toString();
        }
    }

    /** error string when a Personal number formatting error occurred */
    private String getPersonalnummerFormatError()
    {
        return getContext().localize("owdummy.ui.OwFNCMDemoFieldManager.personalnummererror", "The personnel number is formatted as follows: CXXXXX. Where X are 5 decimal digits and C is one of the following categories:") + " "
                + m_PersonalNumberPrefixes.toString();
    }

    /** update the property value upon request and validates the new value.
     * Updates the object, which was displayed in a form using the getEditHTML(...) code.
     * Throws Exception if new value could not be validated
     * @param request_p  {@link HttpServletRequest}
     * @param fieldDef_p OwFieldDefinition definition of field
     * @param value_p Object old Value
     * @param strID_p ID of the HTML element
     */
    public Object updateFieldInternal(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        Object ret = super.updateFieldInternal(request_p, fieldDef_p, value_p, strID_p);

        if (fieldDef_p.getClassName().equals(OwDummyFileObject.OwDummyFileObjectClass.PERSONALNUMMER_PROPERTY))
        {
            // Validate Personal number
            if (((String) ret).length() < 6)
            {
                throw new OwFieldManagerException(getPersonalnummerFormatError());
            }

            String strPrefix = ((String) ret).substring(0, 1);
            String strNumber = ((String) ret).substring(1);

            if (((String) ret).charAt(1) == '-')
            {
                strNumber = ((String) ret).substring(2);
            }

            try
            {
                Integer.parseInt(strNumber);
            }
            catch (NumberFormatException e)
            {
                throw new OwFieldManagerException(getPersonalnummerFormatError());
            }

            if (!m_PersonalNumberPrefixes.m_prefixes.containsKey(strPrefix))
            {
                throw new OwFieldManagerException(getPersonalnummerFormatError());
            }

            if (strNumber.length() != 5)
            {
                throw new OwFieldManagerException(getPersonalnummerFormatError());
            }

            return strPrefix + strNumber;
        }

        return ret;
    }
}