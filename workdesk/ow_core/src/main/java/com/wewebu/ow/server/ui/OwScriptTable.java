package com.wewebu.ow.server.ui;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Utility class for Java Script supported HTML tables with selection and context menu.<br/>
 * In addition java script code needs to be included into the page. See uilevel&lt;x&gt;.js.
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
public class OwScriptTable
{
    protected static final String CONTEXT_MENU_LABEL_POSTFIX = "_label";
    protected static final String CONTEXT_MENU_ICON_POSTFIX = "_icon";
    protected static final String ROW_PREFIX_MULTISELECT = "mwrowid_";
    protected static final String ROW_PREFIX_SINGLESELECT = "swrowid_";
    protected static final String CHECKBOX_PREFIX = "owcbid_";

    /** get the name of the checkbox in the selectable table
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @return String check box ID 
     */
    public static String getSelectableListCheckBoxName(String sTableId_p)
    {
        /////////////////////////////////
        // TODO: support multiple tables in one page use given sTableId_p to create prefix

        return "owmcb_" + sTableId_p;
    }

    /** get the ID of the context menu in the table
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @return String check box ID 
     */
    public static String getSelectableListContextMenuID(String sTableId_p)
    {
        /////////////////////////////////
        // TODO: support multiple tables in one page use given sTableId_p to create prefix

        return "owctx_" + sTableId_p;
    }

    /** insert a handler code that will be called if the user clicks a row
     *  Use the following parameters inside your code
     *  fIndex_p		index of selected row
     *  rownode_p		selected TR tag
     *  fSelected_p		true = row was selected
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer
     * @param sJavaScriptCode_p java script code to call
     * @throws IOException
     */
    public static void writeSelectableListHandlerScript(String sTableId_p, Writer w_p, String sJavaScriptCode_p) throws IOException
    {
        //  called when user selects a single row to perform custom actions
        w_p.write("\n\n<script type=\"text/javascript\">\n\nfunction onRowSelectHandler(fIndex_p,rownode_p,fSelected_p)");
        w_p.write("\n{\n\n");
        w_p.write(sJavaScriptCode_p);
        w_p.write("\n\n}\n\n</script>\n\n");
    }

    /** enable the java script driven table
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer
     * @param context_p OwAppContext
     * @throws IOException
     */
    public static void writeSelectableListEnableScript(String sTableId_p, Writer w_p, OwAppContext context_p) throws IOException
    {

        w_p.write("\n\n<script type=\"text/javascript\"> enableScriptTable(");
        if (sTableId_p != null)
        {
            w_p.write(sTableId_p);
        }
        w_p.write(") </script>\n\n");
    }

    /** write a TR table row start tag for each row that supports script driven table
     * @param locale_p current locale 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer
     * @param iIndex_p int row index
     * @param strRowClass_p String class name
     * @param fMultiselect_p boolean true = table will allow multi selection, false = single selection only
     * @throws IOException
     */
    public static void writeSelectableListRowStart(Locale locale_p, String sTableId_p, Writer w_p, int iIndex_p, String strRowClass_p, boolean fMultiselect_p) throws IOException
    {
        writeSelectableListRowStart(locale_p, sTableId_p, w_p, iIndex_p, strRowClass_p, null, fMultiselect_p, false);
    }

    /** write a TR table row start tag for each row that supports script driven table
     * @param locale_p current locale 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer
     * @param iIndex_p int row index
     * @param strRowClass_p String class name
     * @param strSelectedRowClass_p String class name
     * @param fMultiselect_p boolean true = table will allow multi selection, false = single selection only
     * @param fSelected_p true = line is rendered as checked
     * @throws IOException
     */
    public static void writeSelectableListRowStart(Locale locale_p, String sTableId_p, Writer w_p, int iIndex_p, String strRowClass_p, String strSelectedRowClass_p, boolean fMultiselect_p, boolean fSelected_p) throws IOException
    {
        // the row name is used to submit characteristics to the script handler
        String sID = null;

        if (fMultiselect_p)
        {
            sID = ROW_PREFIX_MULTISELECT;
        }
        else
        {
            sID = ROW_PREFIX_SINGLESELECT;
        }

        String sIndex = String.valueOf(iIndex_p);

        w_p.write("\n<tr id=\"");
        w_p.write(sID);
        w_p.write(sTableId_p);
        w_p.write("_");
        w_p.write(sIndex);
        if (fSelected_p)
        {
            w_p.write("\" class=\"");
            w_p.write(strSelectedRowClass_p);
            w_p.write("\" backupClassName=\"");
            w_p.write(strRowClass_p);
        }
        else
        {
            w_p.write("\" class=\"");
            w_p.write(strRowClass_p);
        }
        w_p.write("\">\n");
        w_p.write("\n<td align='center'><input onclick=\"onClickItemCheckBox('");
        w_p.write(sIndex);
        w_p.write("','");
        w_p.write(sTableId_p);
        w_p.write("');\" type=\"checkbox\" id=\"");
        w_p.write(CHECKBOX_PREFIX);
        w_p.write(sTableId_p);
        w_p.write("_");
        w_p.write(sIndex);
        w_p.write("\" title=\"");
        w_p.write(OwString.localize1(locale_p, "app.OwScriptTable.row.checkbox.title", "Row %1", "" + (iIndex_p + 1)));
        w_p.write("\" name=\"");
        w_p.write(getSelectableListCheckBoxName(sTableId_p));
        w_p.write("\" value=\"");
        w_p.write(sIndex);
        w_p.write("\"");
        if (fSelected_p)
        {
            w_p.write(" checked");
        }
        w_p.write("></td>");
    }

    /** write a TR table row start tag for the header that supports script driven table 
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer
     * @param strHeaderClass_p String class name
     * @param context_p OwAppContext
     * @throws Exception
     */
    public static void writeSelectableListHeaderStart(String sTableId_p, Writer w_p, String strHeaderClass_p, OwAppContext context_p) throws Exception
    {
        w_p.write("\n<tr class='");
        w_p.write(strHeaderClass_p);
        w_p.write("'>\n");

        // === multi select button
        String strToolTip = context_p.localize("app.OwObjectListView.toggleselectall", "Alles (de)selektieren");
        //        String strURL = "javascript:keyboardSelectAll();";
        String strURL = "javascript:toggleCheckBoxes('" + getSelectableListCheckBoxName(sTableId_p) + "');";

        w_p.write("\n<th align='center'><a href=\"");
        w_p.write(strURL);
        w_p.write("\" id=\"toggleCheckBoxesID\"><img border='0' title=\"");
        w_p.write(strToolTip);
        w_p.write("\" alt=\"");
        w_p.write(strToolTip);
        w_p.write("\" src=\"");
        w_p.write(context_p.getDesignURL());
        w_p.write("/images/ok_btn16.png");
        w_p.write("\"></a></th>");

        context_p.registerKeyEvent(65, OwAppContext.KEYBOARD_CTRLKEY_CTRL, "javascript:keyboardSelectAll();", strToolTip);
        w_p.write("\n\n<script type=\"text/javascript\">activeListCheckBoxId='");
        w_p.write(sTableId_p);
        w_p.write("'");
        w_p.write(";</script>\n\n");
    }

    /** write the start tag enclosing a context menu HTML 
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer 
     * @param strClass_p classname
     * @throws Exception
     */
    public static void writeSelectableListContextMenuStart(String sTableId_p, Writer w_p, String strClass_p) throws Exception
    {
        w_p.write("\n<span id=\"");
        w_p.write(OwScriptTable.getSelectableListContextMenuID(sTableId_p));
        w_p.write("\" class=\"");
        w_p.write(strClass_p);
        w_p.write("\" onMouseout=\"starTimeOut(this)\" onMouseenter=\"clearTimeOut(this)\" onFocusout=\"hideMenu()\" onMouseover=\"clearTimeOut(this)\">");
        w_p.write("<table border='0' cellpadding='0' cellspacing='0'>");
    }

    /** end context menu
     * 
     * @param w_p
     * @throws Exception
     */
    public static void writeSelectableListContextMenuEnd(String sTableId_p, Writer w_p) throws Exception
    {
        w_p.write("</table>");
        w_p.write("\n</span>");
        w_p.write("\n\n<script type=\"text/javascript\">");
        w_p.write("\n\n\tappendMenuId(");
        w_p.write(sTableId_p);
        w_p.write(");");
        w_p.write("\n</script>\n\n");
    }

    /** insert a menu item for the context menu
     * 
     * @param sTableId_p String the ID that identifies the table if more tables are in one page
     * @param w_p Writer
     * @param sClassNamePrefix_p String classname prefix will be extended with __Item, _Icon,_Label
     * @param iIndex_p int index of entry
     * @param sEventURL_p event URL to call
     * @param sFormName_p HTML form name
     * @param sIcon_p icon HTML for entry
     * @param sLabel_p label for entry
     * @throws Exception
     */
    public static void writeSelectableListContextMenuTREntry(String sTableId_p, Writer w_p, String sClassNamePrefix_p, int iIndex_p, String sEventURL_p, String sFormName_p, String sIcon_p, String sLabel_p) throws Exception
    {
        String strID = OwScriptTable.getSelectableListContextMenuID(sTableId_p) + "_" + String.valueOf(iIndex_p);

        w_p.write("\n<tr id=\"");
        w_p.write(strID);
        w_p.write("\" class=\"");
        w_p.write(sClassNamePrefix_p);
        w_p.write("_Item\" onmouseover=\"overctx(this)\" onmouseout=\"outctx(this)\" onclick =\"clickctx(this,'");
        w_p.write(sEventURL_p);
        w_p.write("','");
        w_p.write(sFormName_p);
        w_p.write("');\">");

        w_p.write("<td id=\"");
        w_p.write(strID);
        w_p.write(OwScriptTable.CONTEXT_MENU_ICON_POSTFIX);

        w_p.write("\" class=\"");
        w_p.write(sClassNamePrefix_p);
        w_p.write("_Icon\">");

        w_p.write(sIcon_p);
        w_p.write("</td>");

        w_p.write("<td id=\"");
        w_p.write(strID);
        w_p.write(OwScriptTable.CONTEXT_MENU_LABEL_POSTFIX);

        w_p.write("\" class=\"");
        w_p.write(sClassNamePrefix_p);
        w_p.write("_Label\">");

        w_p.write(sLabel_p);
        w_p.write("</td>");

        w_p.write("</tr>\n");
    }
}
