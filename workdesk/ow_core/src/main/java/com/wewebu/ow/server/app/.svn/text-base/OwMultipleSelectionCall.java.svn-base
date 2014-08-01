package com.wewebu.ow.server.app;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.util.OwHTMLHelper;

/**
 *<p>
 * Multiple selection document function call wrapper.
 * Objects of this class perform additional validation on a multiple selection
 * document function call :
 * <LI>if all selected objects are enabled (see {@link OwDocumentFunction#isEnabled(OwObject, OwObject, int)})
 * the document function is called</LI>
 * <LI>if there are disabled objects the user is asked to validate the ongoing call</LI>
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
public class OwMultipleSelectionCall extends OwDocumentFunctionCall
{
    /** Maximum number of  objects to display in the validator message box */
    private int m_totalElementsToDisplay = 10;

    /** YES/NO validating message box implementation **/
    public class OwValidatorMessageBox extends OwMessageBox
    {

        /**
         * Constructor.<br>
         * Creates a yes/no question validation message box.
         * 
         * @param title_p
         * @param strText_p
         */
        public OwValidatorMessageBox(String title_p, String strText_p)
        {
            this(OwMessageBox.TYPE_YES_NO, OwMessageBox.ICON_TYPE_QUESTION, title_p, strText_p);
        }

        /**
         * Constructor
         * @param title_p
         * @param type_p message box type as in {@link OwMessageBox#OwMessageBox(int, int, String, String)}
         * @param iconType_p message box icon type as in {@link OwMessageBox#OwMessageBox(int, int, String, String)}
         * @param strText_p
         * @since 2.5.2.0
         */
        public OwValidatorMessageBox(int type_p, int iconType_p, String title_p, String strText_p)
        {
            super(type_p, OwMessageBox.ACTION_STYLE_BUTTON, iconType_p, title_p, strText_p);
        }

        /**
         * YES  handler.
         * The user chooses to go on with the function call.
         * The dialog is closed than the document function is invoked.
         * @param request_p
         * @throws Exception
         */
        public void onYes(HttpServletRequest request_p) throws Exception
        {
            closeDialog();
            invokeOnMultiselectClickEvent();
        }

        /**
         * NO handler.
         * The dialog is closed.
         * @param request_p
         * @throws Exception
         */
        public void onNo(HttpServletRequest request_p) throws Exception
        {
            closeDialog();
        }

        /**
         * NO handler.
         * The dialog is closed.
         * @param request_p
         * @throws Exception
         */
        public void onCancel(HttpServletRequest request_p) throws Exception
        {
            closeDialog();
        }
    }

    /** 
     * The selected objects.
     * @see OwDocumentFunction#onMultiselectClickEvent(Collection, OwObject, OwClientRefreshContext) 
     * */
    private Collection m_objects;
    /**
    * Call context parent.
    * @see OwDocumentFunction#onMultiselectClickEvent(Collection, OwObject, OwClientRefreshContext) 
    */
    private OwObject m_parent;

    /**
     * Refresh context
     * @see OwDocumentFunction#onMultiselectClickEvent(Collection, OwObject, OwClientRefreshContext) 
     */
    private OwClientRefreshContext m_refreshCtx;

    /** App context needed for validator dialog */
    private OwMainAppContext m_context;

    /**
     * Constructor. 
     * Document function call parameters are passed 
     * 
     * @param documentFunction_p
     * @param objects_p
     * @param parent_p
     * @param refreshCtx_p
     * @param context_p
     * @see OwDocumentFunction#onMultiselectClickEvent(Collection, OwObject, OwClientRefreshContext)
     */
    public OwMultipleSelectionCall(OwDocumentFunction documentFunction_p, Collection objects_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p, OwMainAppContext context_p)
    {
        super(documentFunction_p);
        this.m_objects = objects_p;
        this.m_parent = parent_p;
        this.m_context = context_p;
        this.m_refreshCtx = refreshCtx_p;
    }

    /**
     * If there are disabled objects in the selected collection a the given
     * StringBuffer will be filled with their HTML description list.
     * @param namesBuffer_p a StringBuffer to be filled with the disabled objects HTML list 
     * @return the number of disabled objects
     * @throws Exception
     */
    protected final int createDisabledObjectNamesString(StringBuffer namesBuffer_p) throws Exception
    {
        StringBuffer disabledObjectNames = namesBuffer_p;

        disabledObjectNames.append("<ul class=\"OwMessageBoxText\">");

        int disabledObjectsCount = 0;
        for (Iterator i = m_objects.iterator(); i.hasNext();)
        {
            OwObject owObject = (OwObject) i.next();
            if (!m_documentFunction.isEnabled(owObject, m_parent, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                disabledObjectsCount++;
                disabledObjectNames.append("<li>");
                disabledObjectNames.append(OwHTMLHelper.encodeToSecureHTML(owObject.getName()));
                disabledObjectNames.append("</li>");

                if ((disabledObjectsCount >= m_totalElementsToDisplay) && i.hasNext())
                {
                    // there are more than TOTAL_ELEMENTS_DISPLAY objects, add ellipses and break
                    disabledObjectNames.append("<li>");
                    disabledObjectNames.append("...");
                    disabledObjectNames.append("</li>");

                    break;
                }
            }
        }
        disabledObjectNames.append("</ul>");

        return disabledObjectsCount;
    }

    /**
     * Multiple selection document function call invocation method.
     * If disabled objects are found the user is asked to validate the ongoing call
     * @throws Exception if the invocation method fails (either wrapping 
     *                    code or document function call code)
     */
    public void invokeFunction() throws Exception
    {
        if (!m_documentFunction.isEnabled(m_objects, m_parent, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
        {
            StringBuffer namesBuffer = new StringBuffer();
            int disabledObjectsCount = createDisabledObjectNamesString(namesBuffer);
            if (disabledObjectsCount == 0)
            {
                invokeOnMultiselectClickEvent();
            }
            else if (disabledObjectsCount == m_objects.size())
            {
                StringBuffer validatorText = new StringBuffer();
                String message = m_context.localize("owdocfun.OwMultipleSelectionCall.validatorMessageBox.allobjectsdisabled", "The operation cannot be executed on the selected objects!");

                validatorText.append(message);
                validatorText.append("<BR>");

                OwValidatorMessageBox validatorDialog = new OwValidatorMessageBox(OwMessageBox.TYPE_OK, OwMessageBox.ICON_TYPE_EXCLAMATION, m_documentFunction.getDefaultLabel(), validatorText.toString());
                m_context.openDialog(validatorDialog, null);
            }
            else
            {
                StringBuffer validatorText = new StringBuffer();
                String objectsMessage = m_context.localize("owdocfun.OwMultipleSelectionCall.validatorMessageBox.objects", "Warning!<br><br>The operation cannot be executed on following objects:");
                String executeAnywayMessage = m_context.localize("owdocfun.OwMultipleSelectionCall.validatorMessageBox.executeAnyway", "Execute anyway?");

                validatorText.append(objectsMessage);
                validatorText.append("<BR>");
                validatorText.append(namesBuffer);
                validatorText.append("<BR>");
                validatorText.append(executeAnywayMessage);

                OwValidatorMessageBox validatorDialog = new OwValidatorMessageBox(m_documentFunction.getDefaultLabel(), validatorText.toString());
                m_context.openDialog(validatorDialog, null);
            }
        }
        else
        {
            invokeOnMultiselectClickEvent();
        }

    }

    /**
     * Document function invocation helper method 
     * Delegates to {@link OwDocumentFunction#onMultiselectClickEvent(Collection, OwObject, OwClientRefreshContext)}
     * @throws Exception
     */
    protected final void invokeOnMultiselectClickEvent() throws Exception
    {
        m_documentFunction.onMultiselectClickEvent(m_objects, m_parent, m_refreshCtx);
    }
}