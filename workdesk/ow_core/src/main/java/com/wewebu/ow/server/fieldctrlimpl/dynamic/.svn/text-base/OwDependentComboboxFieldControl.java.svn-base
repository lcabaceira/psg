package com.wewebu.ow.server.fieldctrlimpl.dynamic;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.app.OwComboItem;
import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwComboboxRendererFactory;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidValueException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 * 
 *<p>
 * Configurable dependency FieldControl.<br />
 * Can load values dependent on other field values, from different Data provider.
 * <p>
 * There two configuration nodes which are handled by default in the field control:
 * <ul>
 * <li>Active</li>
 * <li>DependentData</li>
 * </ul>
 * <p>
 *  Active is an optional node, and define the context/provider type where  the control should be active.<br />
 *  <code>
 *  &lt;Active search="true|false" edit="true|false" create="true|false" checkin="true|false" /&gt;
 *  </code>
 * </p>
 * Configuration must have a <code>&lt;DependentData&gt;</code> node
 * which provide the information of dependent handling.
 * <pre>
 * <code>&lt;DependentData class="com.wewebu.ow.server.fieldctrlimpl.dynamic.OwDependentComboboxDataHandler"&gt;
 * ...
 * &lt;DependentData&gt;</code>
 * </pre>
 * The <code>&lt;DependentData&gt;</code> can have an attribute <code>class</code> to define corresponding DependentDataHandler.
 * Definitions must implement the <code>{@link OwDependentComboboxDataHandler}</code> interface,  
 * by default <code>com.wewebu.ow.server.fieldctrlimpl.dynamic.OwDBDependentComboboxDataHandler</code> is defined.
 * </p>
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
public class OwDependentComboboxFieldControl extends OwFieldManagerControl
{
    private static final Logger LOG = OwLogCore.getLogger(OwDependentComboboxFieldControl.class);

    public static final String EL_ACTIVE = "Active";
    public static final String EL_DEPENDENT_DATA = "DependentData";

    private OwXMLUtil config;
    private boolean search, create, edit, checkIn;
    private OwDependentComboboxDataHandler dataHandler;

    @Override
    public void init(OwFieldManager fieldmanager_p, Node configNode_p) throws Exception
    {
        super.init(fieldmanager_p, configNode_p);
        config = new OwStandardXMLUtil(configNode_p);
        OwXMLUtil activeContext = config.getSubUtil(EL_ACTIVE);
        if (activeContext != null)
        {
            search = config.getSafeBooleanAttributeValue("search", false);
            create = config.getSafeBooleanAttributeValue("create", true);
            edit = config.getSafeBooleanAttributeValue("edit", true);
            checkIn = config.getSafeBooleanAttributeValue("checkin", true);
        }
        else
        {
            search = false;
            create = true;
            edit = true;
            checkIn = true;
        }
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        OwXMLUtil dataHandlerConfig = config.getSubUtil(EL_DEPENDENT_DATA);
        String dataHandlerClass = dataHandlerConfig.getSafeStringAttributeValue("class", "com.wewebu.ow.server.fieldctrlimpl.dynamic.OwDBDependentComboboxDataHandler");
        try
        {
            Class<OwDependentComboboxDataHandler> clazz = (Class<OwDependentComboboxDataHandler>) Class.forName(dataHandlerClass);
            dataHandler = clazz.newInstance();
        }
        catch (InstantiationException instEx)
        {
            LOG.error("Failed to instantiate DataHandler for class=" + dataHandlerClass);
            throw new OwServerException(getContext().localize1("dynamic.OwDependentChoiceListFieldControl.init.dataHandler", "Failed to instantiate DataHandler for class=%1.", dataHandlerClass), instEx);
        }
        catch (Exception e)
        {
            LOG.error("Failed to instantiate DataHandler for class=" + dataHandlerClass, e);
            throw new OwConfigurationException(getContext().localize1("dynamic.OwDependentChoiceListFieldControl.create.dataHandler", "Could not create DataHandler for class=%1.", dataHandlerClass), e);
        }
        dataHandler.init(getContext(), dataHandlerConfig);
    }

    @Override
    public void insertReadOnlyField(Writer w, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        if (value_p != null)
        {
            w.write(String.valueOf(value_p));
        }
    }

    @Override
    public void insertEditField(Writer w, OwFieldDefinition fieldDef, OwField field, String strID) throws Exception
    {
        if (isActiveContext())
        {
            insertComboField(w, fieldDef, field, strID);
        }
        else
        {
            insertEditTextField(w, fieldDef, field.getValue(), strID);
        }
    }

    /**
     * Render a combo box for value representations. Will retrieve Data for rendering 
     * from {@link #getDataHandler()}, afterwards the retrieved Data is converted into OwComboModel 
     * representation calling {@link #createComboModel(OwField, List)} method.<br />
     * If everything is successful the rendering of combo box will be processed.
     * 
     * @param w Writer
     * @param fieldDef OwFieldDefinition of field to be rendered
     * @param field OwField dependent field
     * @param strID String for the rendered component
     * @throws OwException if rendering cannot be processed.
     * @throws IOException 
     */
    protected void insertComboField(Writer w, OwFieldDefinition fieldDef, OwField field, String strID) throws OwException, IOException
    {
        String eventUrl = getAjaxEventURL("GetComboItems", "fid=" + strID);
        getContext().renderJSInclude("/js/dependentCombobox.js", w);
        String varName = "d" + OwHTMLHelper.encodeJavascriptVariableName(strID);
        w.write("<script type=\"text/javascript\">");
        w.write(varName);
        w.write("= new DependentComboBox(\"");
        w.write(getFieldManager().getFormName());
        w.write("\", \"");
        w.write(eventUrl);
        w.write("\", \"");
        w.write(strID);
        w.write("\", [");
        Iterator<String> it = getDataHandler().getDependentFieldIds().iterator();
        while (it.hasNext())
        {
            w.write("\"");
            w.write(it.next());
            w.write("\"");
            if (it.hasNext())
            {
                w.write(", ");
            }
        }
        w.write("]);\n");
        w.write("OwdJs.addOnChangeListener(");
        w.write(varName);
        w.write(");\n</script>");
        List<OwComboItem> items = getDataHandler().getData(getFieldManager().getFieldProvider());
        OwComboboxRendererFactory comboFactory = getContext().getRegisteredInterface(OwComboboxRendererFactory.class);
        OwComboboxRenderer renderer = comboFactory.createComboboxRenderer();
        renderer.setFieldDefinition(fieldDef);
        renderer.setFieldId(strID);

        renderer.setModel(createComboModel(field, items));

        renderer.addEvent("click", varName + ".onSelect");
        try
        {
            renderer.renderCombo(w);
        }
        catch (OwException owex)
        {
            throw owex;
        }
        catch (Exception e)
        {
            LOG.error("Faild to render combobox", e);
            throw new OwServerException("Could not render value representation", e);
        }
    }

    /**(overridable)
     * Factory method for creation of the combo model, for combo box rendering.
     * @param field OwField
     * @param items List of OwComboItems
     * @return OwComboModel
     * @throws OwException if cannot create model from provided information
     */
    protected OwComboModel createComboModel(OwField field, List<OwComboItem> items) throws OwException
    {
        try
        {
            Object value = field.getValue();
            if (value != null)
            {
                return new OwDefaultComboModel(true, false, "" + value, items);
            }
            else
            {
                return new OwDefaultComboModel(true, false, null, items);
            }
        }
        catch (OwException owex)
        {
            throw owex;
        }
        catch (Exception e1)
        {
            throw new OwServerException("Cannot retrieve current field value", e1);
        }
    }

    /**(overridable)
     * Default rendering in case combo box should not be rendered.
     * @param w Writer
     * @param fieldDef OwFieldDefinition 
     * @param currentValue Object 
     * @param id String for rendered representation item
     * @throws OwException
     * @throws IOException
     */
    protected void insertEditTextField(Writer w, OwFieldDefinition fieldDef, Object currentValue, String id) throws OwException, IOException
    {
        String strText = "";
        if (currentValue != null)
        {
            strText = currentValue.toString();
        }

        // get the max string length from definition max value
        int iMaxSize = 64; // default
        try
        {
            Object value = fieldDef.getMaxValue();
            if (value != null)
            {
                iMaxSize = ((Integer) value).intValue();
            }
        }
        catch (Exception e)
        { /* ignore */
        }

        int iVisibleSize = (iMaxSize > 32) ? 32 : iMaxSize;

        w.write("<input id=\"");
        OwHTMLHelper.writeSecureHTML(w, id);
        w.write("\" maxlength=\"");
        OwHTMLHelper.writeSecureHTML(w, String.valueOf(iMaxSize));
        w.write("\" size=\"");
        OwHTMLHelper.writeSecureHTML(w, String.valueOf(iVisibleSize));
        w.write("\" title=\"");
        OwHTMLHelper.writeSecureHTML(w, fieldDef.getDescription(getContext().getLocale()));
        /*create the CSS classes for this input control*/
        w.write("\" class=\"OwInputControl OwInputControlString OwInputControl_");
        w.write(fieldDef.getClassName());
        w.write("\" name=\"");
        OwHTMLHelper.writeSecureHTML(w, id);
        w.write("\" type=\"text\" value=\"");
        OwHTMLHelper.writeSecureHTML(w, strText);
        w.write("\" onblur='onFieldManagerFieldExit(\"");
        OwHTMLHelper.writeSecureHTML(w, OwHTMLHelper.encodeJavascriptString(fieldDef.getClassName()));
        w.write("\",\"");
        OwHTMLHelper.writeSecureHTML(w, OwHTMLHelper.encodeJavascriptString(fieldDef.getJavaClassName()));
        w.write("\",\"");
        if (getFieldManager().getFieldProvider() != null)
        {
            OwHTMLHelper.writeSecureHTML(w, OwHTMLHelper.encodeJavascriptString(Integer.toString(getFieldManager().getFieldProvider().getFieldProviderType())));
            w.write("\",\"");
            OwHTMLHelper.writeSecureHTML(w, OwHTMLHelper.encodeJavascriptString(getFieldManager().getFieldProvider().getFieldProviderName()));
            w.write("\",\"");
        }
        OwHTMLHelper.writeSecureHTML(w, OwHTMLHelper.encodeJavascriptString(id));
        w.write("\",this.value)'>");
    }

    @Override
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        String strValue = request_p.getParameter(strID_p);
        if (strValue != null && isActiveContext())
        {
            List<OwComboItem> allowedValues = getDataHandler().getData(getFieldManager().getFieldProvider());
            boolean validValue = true;
            for (OwComboItem item : allowedValues)
            {
                if (strValue.equals(item.getValue()))
                {
                    validValue = true;
                    break;
                }
            }

            if (!validValue)
            {
                throw new OwInvalidValueException(getContext().localize("dynamic.OwDependentComboboxFieldControl.updateField", "The current value is not within the allowed range."));
            }
        }

        return getFieldManager().convertValue(request_p, fieldDef_p, value_p, strID_p);
    }

    protected OwXMLUtil getConfig()
    {
        return this.config;
    }

    protected OwDependentComboboxDataHandler getDataHandler()
    {
        return this.dataHandler;
    }

    public void onAjaxGetComboItems(HttpServletRequest req, HttpServletResponse resp) throws OwException, IOException
    {
        try
        {
            getFieldManager().updateExternalFormTarget(req, false);
        }
        catch (OwException owex)
        {
            throw owex;
        }
        catch (Exception e)
        {
            LOG.error("Cannot update fields from request", e);
            throw new OwServerException("Cannot update fields from request", e);
        }
        List<OwComboItem> items = getDataHandler().getData(getFieldManager().getFieldProvider());
        processDataResponse(req, resp, items);
    }

    protected void processDataResponse(HttpServletRequest req, HttpServletResponse resp, List<OwComboItem> items) throws IOException
    {
        StringBuilder answer = new StringBuilder("{\"data\":[");
        if (items.isEmpty())
        {
            answer.append("[\"\", \"-\"]");
        }
        else
        {
            Iterator<OwComboItem> itItems = items.iterator();
            while (itItems.hasNext())
            {
                OwComboItem item = itItems.next();
                answer.append("[\"");
                answer.append(OwHTMLHelper.encodeToSecureHTML(item.getValue()));
                answer.append("\",\"");
                answer.append(OwHTMLHelper.encodeToSecureHTML(item.getDisplayName()));
                answer.append("\"]");
                if (itItems.hasNext())
                {
                    answer.append(",");
                }
            }
        }
        answer.append("]}");
        resp.getWriter().append(answer);
    }

    /**(overridable)
     * Verify if this instance should be active in current context/ field provider type. 
     * @return boolean true only if matching configuration, else false
     */
    protected boolean isActiveContext()
    {
        OwFieldManager fieldManager = getFieldManager();
        return ((fieldManager.isFieldProviderType(OwFieldProvider.TYPE_META_OBJECT) && edit) || (fieldManager.isFieldProviderType(OwFieldProvider.TYPE_CREATE_OBJECT) && create)
                || (fieldManager.isFieldProviderType(OwFieldProvider.TYPE_SEARCH) && search) || (fieldManager.isFieldProviderType(OwFieldProvider.TYPE_CHECKIN_OBJECT) && checkIn));
    }
}
