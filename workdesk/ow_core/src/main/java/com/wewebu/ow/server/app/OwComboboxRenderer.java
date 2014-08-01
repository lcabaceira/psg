package com.wewebu.ow.server.app;

import java.io.Writer;

import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Combo box renderer component interface.
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
 *@since 3.0.0.0
 */
public interface OwComboboxRenderer
{

    /**
     * Render the combobox on a {@link Writer} object. 
     * @param writer_p - the writer object.
     * @throws Exception
     */
    public abstract void renderCombo(Writer writer_p) throws Exception;

    /**
     * Add specified style class.
     * @param styleClass_p - thestyle class.
     */
    public abstract void addStyleClass(String styleClass_p);

    /**
     * Add specific style class. If the parameter 
     * <code>ignoreDefaultStyleClasses_p</code> is <code>true</code> the default style classes are not
     * rendered anymore
     * @param styleClass_p - the name of the style class 
     * @param ignoreDefaultStyleClasses_p - if has <code>true</code> value, the default style classes are not
     * rendered anymore
     */
    public abstract void addStyleClass(String styleClass_p, boolean ignoreDefaultStyleClasses_p);

    /**
     * Add a list of style classes, to be rendered with this component.
     * @param styleClasses_p - the array of {@link String} objects, representing the style classes.
     */
    public abstract void addStyleClasses(String[] styleClasses_p);

    /**
     * Add a list of style classes, to be rendered with this component.
     * @param styleClasses_p - the array of {@link String} objects, representing the style classes.
     * @param ignoreDefaultStyleClasses_p - if has <code>true</code> value, the default style classes are not
     * rendered anymore 
     */
    public abstract void addStyleClasses(String[] styleClasses_p, boolean ignoreDefaultStyleClasses_p);

    /**
     * Add JavaScript code for the given event name
     * @param eventName_p - the event name
     * @param eventHandler_p - the JavaScript event handler. Please use <code>"</code> for JS string delimiter.
     */
    public abstract void addEvent(String eventName_p, String eventHandler_p);

    /**
     * Get all event names for this combo
     * @return an array of {@link String} objects, representing the event names.
     */
    public abstract String[] getAllEventsNames();

    /**
     * Set the context.
     * @param context_p - the {@link OwMainAppContext} object.
     */
    public void setContext(OwMainAppContext context_p);

    /**
     * Set the combo model items.    
     * @param model_p - the {@link OwComboModel} object.
     */
    public void setModel(OwComboModel model_p);

    /**
     * Set the id of the rendered field.
     * @param fieldId_p - the {@link String} object representing the ID to be rendered as <code>htmlid</code> value
     */
    public void setFieldId(String fieldId_p);

    /**
     * Set the description of the rendered field.
     * @param description_p - the {@link OwString} object representing the description of the rendered combo controls (usually as title HTML attribute values) 
     */
    public void setFieldDescription(OwString description_p);

    /**
     * Set the field definition.
     * @param fieldDefinition_p - the {@link OwFieldDefinition} object.
     */
    public void setFieldDefinition(OwFieldDefinition fieldDefinition_p);

    /**
     * Set the field provider.
     * @param fieldProvider_p - the {@link OwFieldProvider} object.
     */
    public void setFieldProvider(OwFieldProvider fieldProvider_p);

    /**
     * Set the enabled status of this combobox.
     * @param enabled_p 
     * @since 3.1.0.0
     */
    public void setEnabled(boolean enabled_p);
}