package com.wewebu.ow.server.settingsimpl;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwWindowPositions;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Settings control implementation to save the window and 
 * viewer position as a OwSettingsPropertyWindowPositions.OwWindowPositions value.
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
public class OwSettingsPropertyWindowPositions extends OwSettingsPropertyBaseImpl
{
    /** Persistent node for viewer width
     * @since 3.1.0.0*/
    protected static final String VIEWER_WIDTH = "OwViewerWidth";
    /** Persistent node for viewer height
     * @since 3.1.0.0*/
    protected static final String VIEWER_HEIGHT = "OwViewerHeight";
    /** Persistent node for viewer top X coordinate
     * @since 3.1.0.0*/
    protected static final String VIEWER_TOPX = "OwViewerTopX";
    /** Persistent node for viewer top Y coordinate
     * @since 3.1.0.0*/
    protected static final String VIEWER_TOPY = "OwViewerTopY";
    /** Persistent node for main window width
     * @since 3.1.0.0*/
    protected static final String MAIN_WIDTH = "OwWindowWidth";
    /** Persistent node for main window height
     * @since 3.1.0.0*/
    protected static final String MAIN_HEIGHT = "OwWindowHeight";
    /** Persistent node for main window top X coordinate
     * @since 3.1.0.0*/
    protected static final String MAIN_TOPX = "OwWindowTopX";
    /** Persistent node for main window top Y coordinate
     * @since 3.1.0.0*/
    protected static final String MAIN_TOPY = "OwWindowTopY";
    /** Persistent node for display mode, for values see DISPLAY_MODE_.. 
     * @since 3.1.0.0*/
    protected static final String DISPLAY_MODE = "OwDisplaySettingsMode";
    /** Persistent node for flag to position also the main window
     * @since 3.1.0.0*/
    protected static final String POSITION_MAIN = "OwPositionMainWindow";

    /** tuple that holds window position information */
    public class OwWindowPositionsImpl extends OwWindowPositions
    {
        /**Marker to know if the current position is defined with relative or absolute coordinates
         * @since 3.1.0.0*/
        protected boolean relativePos;

        /** position info for the viewer */
        private int m_iViewerWidth;
        private int m_iViewerHeight;
        private int m_iViewerTopX;
        private int m_iViewerTopY;

        /** true = position window with viewer, false = just position viewer */
        private boolean m_fWindow;

        private String m_stDisplaySettings;

        /** position info for the main window */
        private int m_iWindowWidth;
        private int m_iWindowHeight;
        private int m_iWindowTopX;
        private int m_iWindowTopY;

        /** create default value */
        public OwWindowPositionsImpl()
        {
            relativePos = false;
        }

        /** create from request
        *
        * @param request_p HttpServletRequest with form data to update the property
        * @param strID_p String the HTML form element ID of the requested value
         * @throws OwInvalidOperationException 
        */
        public OwWindowPositionsImpl(HttpServletRequest request_p, String strID_p) throws OwInvalidOperationException
        {
            setFromRequest(request_p, strID_p);
        }

        /** construct value from XML node
         * @param valueNode_p Node
         */
        public OwWindowPositionsImpl(Node valueNode_p)
        {
            OwXMLUtil nodeWrapper = null;
            try
            {
                nodeWrapper = new OwStandardXMLUtil(valueNode_p);
            }
            catch (Exception e)
            {
                return;
            }
            relativePos = nodeWrapper.getSafeTextValue(VIEWER_WIDTH, "").endsWith("%");

            m_iViewerWidth = getSafeIntegerValue(nodeWrapper.getSafeTextValue(VIEWER_WIDTH, null), 0);
            m_iViewerHeight = getSafeIntegerValue(nodeWrapper.getSafeTextValue(VIEWER_HEIGHT, null), 0);
            m_iViewerTopX = getSafeIntegerValue(nodeWrapper.getSafeTextValue(VIEWER_TOPX, null), 0);
            m_iViewerTopY = getSafeIntegerValue(nodeWrapper.getSafeTextValue(VIEWER_TOPY, null), 0);

            m_iWindowWidth = getSafeIntegerValue(nodeWrapper.getSafeTextValue(MAIN_WIDTH, null), 0);
            m_iWindowHeight = getSafeIntegerValue(nodeWrapper.getSafeTextValue(MAIN_HEIGHT, null), 0);
            m_iWindowTopX = getSafeIntegerValue(nodeWrapper.getSafeTextValue(MAIN_TOPX, null), 0);
            m_iWindowTopY = getSafeIntegerValue(nodeWrapper.getSafeTextValue(MAIN_TOPY, null), 0);

            m_fWindow = (nodeWrapper.getSafeIntegerValue(POSITION_MAIN, 0) == 1) ? true : false;
            m_stDisplaySettings = nodeWrapper.getSafeStringAttributeValue(DISPLAY_MODE, "");

        }

        /** set value from request
        *
        * @param request_p HttpServletRequest with form data to update the property
        * @param strBaseID_p String the HTML form element base ID of the requested value
        * 
         * @throws OwInvalidOperationException 
        */
        public void setFromRequest(HttpServletRequest request_p, String strBaseID_p) throws OwInvalidOperationException
        {
            try
            {
                relativePos = request_p.getParameter(strBaseID_p + VIEWER_WIDTH).endsWith("%");

                //set windows and  viewer positions
                m_iViewerWidth = getIntegerValue(request_p.getParameter(strBaseID_p + VIEWER_WIDTH));
                m_iViewerHeight = getIntegerValue(request_p.getParameter(strBaseID_p + VIEWER_HEIGHT));
                m_iViewerTopX = getIntegerValue(request_p.getParameter(strBaseID_p + VIEWER_TOPX));
                m_iViewerTopY = getIntegerValue(request_p.getParameter(strBaseID_p + VIEWER_TOPY));

                m_iWindowWidth = getIntegerValue(request_p.getParameter(strBaseID_p + MAIN_WIDTH));
                m_iWindowHeight = getIntegerValue(request_p.getParameter(strBaseID_p + MAIN_HEIGHT));
                m_iWindowTopX = getIntegerValue(request_p.getParameter(strBaseID_p + MAIN_TOPX));
                m_iWindowTopY = getIntegerValue(request_p.getParameter(strBaseID_p + MAIN_TOPY));

                m_stDisplaySettings = request_p.getParameter(strBaseID_p + DISPLAY_MODE);

                String sWindow = request_p.getParameter(strBaseID_p + POSITION_MAIN);
                if (null != sWindow)
                {
                    m_fWindow = Boolean.valueOf(sWindow).booleanValue();
                }
            }
            catch (Exception e)
            {
                String msg = OwString.localize(getContext().getLocale(), "settingsimpl.OwSettingsPropertyWindowPositions.validvalues", "Please insert valid values");
                throw new OwInvalidOperationException(msg, e);
            }
        }

        /** overridable to insert a single value into a edit HTML form
        *
        * @param w_p Writer to write HTML code to
        * @param strID_p String the ID of the HTML element for use in onApply
        * @param locale_p Locale to use
        */
        protected void insertFormValue(Writer w_p, String strID_p, Locale locale_p) throws Exception
        {
            w_p.write("<div class=\"owInlineControls\" style=\"float:left;\">");
            w_p.write("<table>");
            w_p.write("<tr><td>");
            w_p.write("<script>");
            w_p.write(" var strID='" + strID_p + "';");
            w_p.write("</script>");
            w_p.write("<div style=\"clear:both;\" ><div class=\"OwPropertyName\">");
            w_p.write(OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.viewerpos", "Viewer Position"));
            w_p.write("</div></div></td></tr><tr><td><div style=\"clear:both;\">");
            writeStringMember(w_p, relativePos ? String.valueOf(m_iViewerWidth) + "%" : String.valueOf(m_iViewerWidth), strID_p, VIEWER_WIDTH, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.width", "Width"), locale_p);
            writeStringMember(w_p, relativePos ? String.valueOf(m_iViewerHeight) + "%" : String.valueOf(m_iViewerHeight), strID_p, VIEWER_HEIGHT, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.height", "Height"),
                    locale_p);
            writeStringMember(w_p, relativePos ? String.valueOf(m_iViewerTopX) + "%" : String.valueOf(m_iViewerTopX), strID_p, VIEWER_TOPX, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.topx", "X"), locale_p);
            writeStringMember(w_p, relativePos ? String.valueOf(m_iViewerTopY) + "%" : String.valueOf(m_iViewerTopY), strID_p, VIEWER_TOPY, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.topy", "Y"), locale_p);
            w_p.write("</div>");//closed first owInlineControls
            w_p.write("</td></tr><tr><td>");
            w_p.write("<div class=\"owInlineControls\" style=\"clear:both;\"> ");
            w_p.write("<div class=\"OwPropertyName\" style=\"float:left;\">");
            String booleanMemberDisplayName = OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.mainwindowpos", "Position main window as well");
            insertLabelValue(w_p, booleanMemberDisplayName, strID_p + POSITION_MAIN, 0);
            w_p.write("&nbsp;</div><div style=\"float:left;\">");
            writeBooleanMember(w_p, m_fWindow, strID_p, POSITION_MAIN, locale_p);
            w_p.write("</div></div>");
            w_p.write("</td></tr><tr><td>");
            w_p.write("<div style=\"clear:left;\">");
            writeStringMember(w_p, relativePos ? String.valueOf(m_iWindowWidth) + "%" : String.valueOf(m_iWindowWidth), strID_p, MAIN_WIDTH, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.width", "Width"), locale_p);
            writeStringMember(w_p, relativePos ? String.valueOf(m_iWindowHeight) + "%" : String.valueOf(m_iWindowHeight), strID_p, MAIN_HEIGHT, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.height", "Height"), locale_p);
            writeStringMember(w_p, relativePos ? String.valueOf(m_iWindowTopX) + "%" : String.valueOf(m_iWindowTopX), strID_p, MAIN_TOPX, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.topx", "X"), locale_p);
            writeStringMember(w_p, relativePos ? String.valueOf(m_iWindowTopY) + "%" : String.valueOf(m_iWindowTopY), strID_p, MAIN_TOPY, OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyWindowPositions.topy", "Y"), locale_p);
            w_p.write("</div></td></tr></table></div>");//closed second owInlineControls
        }

        private void writeStringMember(Writer w_p, String sValue_p, String sBaseID_p, String sName_p, String sDisplayName_p, Locale locale_p) throws IOException
        {
            // member
            w_p.write("<div class=\"OwPropertyName\">");
            insertLabelValue(w_p, sDisplayName_p, sBaseID_p + sName_p, 0);
            w_p.write("&nbsp;</div><input class=\"OwInputControl\" size=\"3\" name=\"");
            w_p.write(sBaseID_p);
            w_p.write(sName_p);
            w_p.write("\" id=\"");
            w_p.write(sBaseID_p);
            w_p.write(sName_p);
            w_p.write("\" type=\"text\" value=\"");
            w_p.write(sValue_p);
            w_p.write("\">");
        }

        protected void writeBooleanMember(Writer w_p, boolean fValue_p, String strID_p, String sName_p, Locale locale_p) throws Exception
        {
            String[] values = { Boolean.TRUE.toString(), Boolean.FALSE.toString() };
            String[] displayValues = { OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyBoolean.yes", "Yes"), OwString.localize(locale_p, "settingsimpl.OwSettingsPropertyBoolean.no", "No") };
            OwComboModel comboModel = new OwDefaultComboModel(false, false, Boolean.toString(fValue_p), values, displayValues);
            OwComboboxRenderer renderer = ((OwMainAppContext) OwSettingsPropertyWindowPositions.this.getContext()).createComboboxRenderer(comboModel, strID_p + sName_p, null, null, null);
            renderer.renderCombo(w_p);
        }

        /** clone value
         * 
         * @return OwWindowPositions
         */
        public OwWindowPositionsImpl createClone()
        {
            return this;
        }

        /**
         * Handle the String values which represent either relative or non-relative
         * integer values. <p>Relative values ends with '%' percentage sign.</p>
         * @param valueToParse_p String to parse as integer
         * @param defaultValue_p int to return if parsing fail.
         * @return int representing given String or defaultVaue if parsing fails.
         * @since 3.1.0.0 
         */
        private int getSafeIntegerValue(String valueToParse_p, int defaultValue_p)
        {
            if (valueToParse_p != null)
            {
                try
                {
                    return getIntegerValue(valueToParse_p);
                }
                catch (NumberFormatException numEx)
                {
                }
            }
            return defaultValue_p;
        }

        /**
         * Will return the String as int representation, parsing is
         * handled different for relative and non-relative values.
         * <p>Relative values ends with '%' percentage sign.</p>
         * @param valueToParse_p String to parse
         * @return int if possible
         * @throws NumberFormatException if parsing fails
         * @since 3.1.0.0
         */
        private int getIntegerValue(String valueToParse_p) throws NumberFormatException
        {
            if (valueToParse_p.endsWith("%"))
            {
                return Integer.parseInt(valueToParse_p.substring(0, valueToParse_p.length() - 1));
            }
            else
            {
                return Integer.parseInt(valueToParse_p);
            }
        }

        /** append the value as a XML node to the given node
         * 
         * @param valueRootNode_p Node
         */
        public void appendNode(Node valueRootNode_p)
        {
            appendMemberRelativeHandling(valueRootNode_p, m_iViewerWidth, VIEWER_WIDTH);
            appendMemberRelativeHandling(valueRootNode_p, m_iViewerHeight, VIEWER_HEIGHT);
            appendMemberRelativeHandling(valueRootNode_p, m_iViewerTopX, VIEWER_TOPX);
            appendMemberRelativeHandling(valueRootNode_p, m_iViewerTopY, VIEWER_TOPY);

            appendMemberRelativeHandling(valueRootNode_p, m_iWindowWidth, MAIN_WIDTH);
            appendMemberRelativeHandling(valueRootNode_p, m_iWindowHeight, MAIN_HEIGHT);
            appendMemberRelativeHandling(valueRootNode_p, m_iWindowTopX, MAIN_TOPX);
            appendMemberRelativeHandling(valueRootNode_p, m_iWindowTopY, MAIN_TOPY);

            appendMember(valueRootNode_p, m_fWindow ? 1 : 0, POSITION_MAIN);
        }

        private void appendMember(Node valueRootNode_p, int value_p, String sName_p)
        {
            appendMember(valueRootNode_p, Integer.toString(value_p), sName_p);
        }

        /**
         * Converts the given Integer value to a relative String representation, using
         * the percentage sign '%' as suffix of to the given number.
         * @param valueRootNode_p Node to attache to
         * @param value_p int value to be attached
         * @param sName_p String name of the subnode to create
         * @since 3.1.0.0
         */
        private void appendMemberRelativeHandling(Node valueRootNode_p, int value_p, String sName_p)
        {
            if (relativePos)
            {
                appendMember(valueRootNode_p, Integer.toString(value_p) + "%", sName_p);
            }
            else
            {
                appendMember(valueRootNode_p, Integer.toString(value_p), sName_p);
            }
        }

        private void appendMember(Node valueRootNode_p, String value_p, String sName_p)
        {
            Node n = valueRootNode_p.getOwnerDocument().createElement(sName_p);
            Node v = valueRootNode_p.getOwnerDocument().createTextNode(value_p);
            n.appendChild(v);
            valueRootNode_p.appendChild(n);
        }

        public int getViewerWidth()
        {
            return m_iViewerWidth;
        }

        public int getViewerHeight()
        {
            return m_iViewerHeight;
        }

        public int getViewerTopX()
        {
            return m_iViewerTopX;
        }

        public int getViewerTopY()
        {
            return m_iViewerTopY;
        }

        public int getWindowWidth()
        {
            return m_iWindowWidth;
        }

        public int getWindowHeight()
        {
            return m_iWindowHeight;
        }

        public int getWindowTopX()
        {
            return m_iWindowTopX;
        }

        public int getWindowTopY()
        {
            return m_iWindowTopY;
        }

        public boolean getPositionMainWindow()
        {
            return m_fWindow;
        }

        public int getUnits()
        {
            return relativePos ? UNITS_PERCENT : UNITS_PIXEL;
        }
    }

    /** overridable to create a default value for list properties
    *
    * @return Object with default value for a new list item
    */
    protected Object getDefaultListItemValue()
    {
        // default returns zero Integer
        return new OwWindowPositionsImpl();
    }

    /** overridable to apply changes on a submitted form
     *
     * @param request_p HttpServletRequest with form data to update the property
     * @param strID_p String the HTML form element ID of the requested value
    * @throws OwInvalidOperationException 
     */
    protected Object getSingleValueFromRequest(HttpServletRequest request_p, String strID_p) throws OwInvalidOperationException
    {
        return new OwWindowPositionsImpl(request_p, strID_p);
    }

    /** overridable, return the given value as a DOM Node for serialization
    *
    * @param valueRootNode_p root Node of the property
    * @param value_p Object to append as DOM Node
    *
    */
    protected void appendSingleValueNode(Node valueRootNode_p, Object value_p)
    {
        ((OwWindowPositionsImpl) value_p).appendNode(valueRootNode_p);
    }

    /** create a clone out of the given single property value
     *
     * @param oSingleValue_p single Object value
     * @return Object
     */
    protected Object createSingleClonedValue(Object oSingleValue_p)
    {
        return ((OwWindowPositionsImpl) oSingleValue_p).createClone();
    }

    /** overridable to create a single value for the given node
     * @return Object with value
     */
    protected Object getSingleValue(Node valueNode_p)
    {
        return new OwWindowPositionsImpl(valueNode_p);
    }

    /** overridable to insert a single value into a edit HTML form
    *
    * @param w_p Writer to write HTML code to
    * @param value_p the property value to edit
    * @param strID_p String the ID of the HTML element for use in onApply
     * @param iIndex_p int Index of item if it is a list
     */
    protected void insertFormValue(Writer w_p, Object value_p, String strID_p, int iIndex_p) throws Exception
    {
        ((OwWindowPositionsImpl) value_p).insertFormValue(w_p, strID_p, getContext().getLocale());
    }

    @Override
    public void insertLabel(Writer w_p) throws Exception
    {
        w_p.write(getDisplayName() + ":");
    }

    @Override
    protected void insertLabelValue(Writer w_p, String displayName, String strID_p, int iIndex_p) throws IOException
    {
        w_p.write("<label for=\"" + strID_p + "\">");
        w_p.write(displayName + ":");
        w_p.write("</label>");
    }
}