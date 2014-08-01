package com.wewebu.ow.server.plug.owbpm.plug;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwJspFormConfigurator;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwStandardSequenceDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.plug.owbpm.log.OwLog;

/**
 *<p>
 * Dialog which display a graphical representation of workflow.
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
 * @since 4.2.0.0
 */
public class OwBPMDisplayWorkflowDialog extends OwStandardSequenceDialog
{
    private static final Logger LOG = OwLog.getLogger(OwBPMDisplayWorkflowDialog.class);

    private static final String D_BPM_TASK_BPM_STATUS = "D:bpm:task.bpm:status";
    private static final String D_BPM_TASK_BPM_DUE_DATE = "D:bpm:task.bpm:dueDate";
    private static final String PROP_OW_ASSIGNEE = "OW_ASSIGNEE";

    private Set<OwObject> objects;
    private OwFieldManager m_fieldManager;

    private OwJspFormConfigurator jspFormConfigurator;

    // === multi select functionality
    /** list of items to work on */
    protected List<OwObject> m_items;

    /** current item index */
    protected int m_iIndex = 0;

    /** instance of the MIME manager used to open the objects */
    protected OwMimeManager m_MimeManager;

    /** layout to be used for the dialog */
    private OwSubLayout m_Layout;

    /** the parent of the object that listed the getItem() */
    protected OwObject m_ParentObject;

    /** max number of items to display in the views */
    protected int m_iMaxElementSize;

    private OwBPMDisplayWorkflowDialog createNewDialog(Set<OwObject> objects, OwObject oParent_p)
    {
        return new OwBPMDisplayWorkflowDialogBuilder().items(m_items).index(m_iIndex).parentObject(m_ParentObject).build();
    }

    public OwBPMDisplayWorkflowDialog(OwBPMDisplayWorkflowDialogBuilder builder)
    {
        m_items = new LinkedList();
        m_items.addAll(builder.getItems());
        m_iIndex = builder.getIndex();
        m_ParentObject = builder.getParentObject();
        m_Layout = new OwSubLayout();
        m_Layout.setCustomRegionAttributes(builder.getLayoutRegionAttributes());

    }

    protected void init() throws Exception
    {
        super.init();
        this.m_fieldManager = ((OwMainAppContext) getContext()).createFieldManager();

        OwMainAppContext context = (OwMainAppContext) getContext();

        try
        {
            // === init MIME manager as event target
            m_MimeManager = createMimeManager();
            m_MimeManager.attach(context, null);

        }
        catch (Exception e)
        {
            LOG.debug("Could not create MimeManager  ", e);
        }

    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     */
    public void renderMainRegion(Writer w_p) throws Exception
    {

        this.serverSideDesignInclude("owbpm/OwBPMDisplayWorkflowDialog.jsp", w_p);
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
            case MAIN_REGION:
                renderMainRegion(w_p);
                break;
            case TITLE_REGION:
                renderTitleRegion(w_p);
                break;
            case CLOSE_BTN_REGION:
                renderSequenceNumber(w_p);
                break;
            case HELP_BTN_REGION:
                renderHelpButton(w_p);
                break;

            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    public void detach()
    {
        super.detach();
        if (null != this.m_fieldManager)
        {
            this.m_fieldManager.detach();
        }

        m_MimeManager.reset();
        // detach the field manager as well, this is especially necessary if we use it in a dialog
        m_MimeManager.detach();
    }

    /** Called when the user wants to cancel the workflow selection process 
     *  
     *  @return always true 
     */
    public boolean onCancel(HttpServletRequest request_p) throws Exception
    {
        closeDialog();
        return true;
    }

    /**
     * @return the objects
     */
    public Set<OwObject> getObjects()
    {
        return objects;
    }

    /**
     * Creates a list of lines to be displayed in the tool-tip associated with the diagram for the given workflow.
     * @param object The workflow object to build the tool-tip for.
     * @return A list of tool-tips to be displayed.
     * @since 4.2.0.0
     */
    public List<String> tooltipLinesFor(OwObject object)
    {
        List<String> lines = new ArrayList<String>();
        lines.add(String.format("<b>%s:</b> %s", getContext().localize("owlabel.bpm:taskId", "Task ID"), object.getID()));

        addPropToTooltip(object, lines, D_BPM_TASK_BPM_DUE_DATE);
        addPropToTooltip(object, lines, D_BPM_TASK_BPM_STATUS);
        addPropToTooltip(object, lines, PROP_OW_ASSIGNEE);

        return lines;
    }

    private void addPropToTooltip(OwObject object, List<String> lines, String propName)
    {
        try
        {
            OwProperty property = object.getProperty(propName);
            if (null != property)
            {
                StringWriter stringWriter = new StringWriter();
                m_fieldManager.insertReadOnlyField(stringWriter, property);
                String propertyValString = stringWriter.toString();

                stringWriter = new StringWriter();
                m_fieldManager.insertLabel(stringWriter, true, true, property, "", true);
                String propertyNameString = stringWriter.toString();

                lines.add(String.format("<b>%s: </b>%s", propertyNameString, propertyValString));
            }
        }
        catch (Exception e)
        {
            LOG.debug("Could not retrieve property " + propName, e);
        }
    }

    /** the work item to work on */
    public OwObject getItem()
    {
        return m_items.get(m_iIndex);
    }

    /** called when the Dialog needs to know if there is a next item
    *
    */
    public boolean hasNext() throws Exception
    {
        return (m_iIndex < (m_items.size() - 1));
    }

    /** called when the Dialog needs to know if there is a prev item
     *
     */
    public boolean hasPrev() throws Exception
    {
        return (m_iIndex > 0);
    }

    /** get the number of sequence items in the dialog */
    public int getCount()
    {
        return m_items.size();
    }

    /** move to prev item and roll over, i.e. start at the end one if first one is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the prev item, if this is the last item, closes the dialog
     */
    public void prev(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            throw new OwNotSupportedException("OwEditPropertiesDialog.prev(fRemoveCurrent_p==true) not supported.");
        }

        if (hasPrev())
        {
            m_iIndex--;
        }
        else
        {
            m_iIndex = (m_items.size() - 1);
        }

        // === init the dialog with the current work item
        createNewDialog();
    }

    /** move to next item and roll over, i.e. start at the first one if end is reached
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the next item, if this is the last item, closes the dialog
     */
    public void next(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            // === remove the current item and move to the next
            if (getCount() == 1)
            {
                // === only one item left
                // close dialog
                super.closeDialog();
                return;
            }
            else
            {
                m_items.remove(m_iIndex);
                if (m_iIndex >= m_items.size())
                {
                    m_iIndex = 0;
                }
            }
        }
        else
        {
            // === move to the next item
            if (hasNext())
            {
                m_iIndex++;
            }
            else
            {
                m_iIndex = 0;
            }
        }

        // === init the dialog with the current work item
        createNewDialog();
    }

    /**
     * Create a builder instance for OwEditPropertiesDialog creation.
     * @return instance of OwEditPropertiesDialogBuilder
     * @since 4.2.0.0
     */
    protected OwBPMDisplayWorkflowDialogBuilder createDialogBuilder()
    {
        return new OwBPMDisplayWorkflowDialogBuilder();
    }

    /** init the dialog with the current item
    * @throws Exception
    */
    protected void createNewDialog() throws Exception
    {

        OwBPMDisplayWorkflowDialog dialog = new OwBPMDisplayWorkflowDialogBuilder().items(m_items).index(m_iIndex).parentObject(m_ParentObject).maxElementSize(m_iMaxElementSize).layoutRegionAttributes(m_Layout.getCustomRegionAttributes()).build();
        dialog.setJspConfigurator(getJspConfigurator());
        dialog.setHelp(m_strHelpPath);
        dialog.setTitle(getTitle());

        // set info icon from this dialog
        dialog.setInfoIcon(m_strInfoIconURL);

        // close this dialog
        super.closeDialog();
        // open new dialog
        getContext().openDialog(dialog, m_Listener);

    }

    public OwJspFormConfigurator getJspConfigurator()
    {
        return this.jspFormConfigurator;
    }

    public void setJspConfigurator(OwJspFormConfigurator jspFormConfigurator_p)
    {
        this.jspFormConfigurator = jspFormConfigurator_p;
    }

    private void renderTitleRegion(Writer w_p) throws Exception
    {
        m_MimeManager.reset();
        serverSideDesignInclude("owbpm/OwBPMDisplayWorkflowDialogTitle.jsp", w_p);
    }

    /** get the MIME manager
    *
    * @return OwMimeManager
    */
    public OwMimeManager getMimeManager()
    {
        return m_MimeManager;
    }

    protected OwMimeManager createMimeManager()
    {
        return new OwMimeManager();
    }

    /** determine if region contains a view
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case TITLE_REGION:
                return true;

            case MENU_REGION:
                return true;

            default:
                return super.isRegion(iRegion_p);
        }
    }

    /** render the no. of elements (x from y)
     * @param w_p Writer object to write HTML to
     */
    public void renderSequenceNumber(Writer w_p) throws Exception
    {
        w_p.write("<div class=\"floatleft\">");
        w_p.write("     <div class=\"floatleft\">");
        renderCloseButton(w_p);
        w_p.write("     </div>");
        w_p.write("     <div class=\"floatleft\">");
        w_p.write("         <div id=\"OwStandardDialog_SEQUENCEBUTTONS\">");
        renderNavigationButtons(w_p);
        w_p.write("         </div>");
        w_p.write("         <div  style=\"text-align:center;\" id=\"OwStandardDialog_PAGENR\">");
        w_p.write("<span class=\"OwEditProperties_Versiontext\"> ");
        int curentItem = m_iIndex + 1;
        if (getCount() > 1)
        {
            w_p.write(curentItem + " " + getContext().localize("owdocprops.OwEditPropertiesDialog.pageoffrom", "from") + " " + getCount());
        }
        w_p.write("</span>");
        w_p.write("&nbsp;");
        w_p.write("         </div>");
        w_p.write("     </div>");
        w_p.write("</div>");
    }

}