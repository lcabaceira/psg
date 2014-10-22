package org.alfresco.psgextension;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwStandardFieldManager;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.util.OwHTMLHelper;

public class OwGenerateIDControl extends OwFieldManagerControl
{
	
	StringBuffer m_myNewId = new StringBuffer();
	/** 
	Event handler called when the user clicks on the button 
	*/
	public void onCreateID(final HttpServletRequest request_p) throws Exception
	{
	   if (m_myNewId.length() > 0)
	   m_myNewId.delete(0, m_myNewId.length());

	   OwField field = getFieldManager().getFieldProvider().getField("cmis:document.cmis:name");
	   m_myNewId.append(field.getValue().toString().replaceAll(" ", "_"));
	   m_myNewId.append("-");
	    
	   //create timestamp
	   java.sql.Timestamp now=new java.sql.Timestamp((new java.util.Date()).getTime() );
	   String test = now.toString();
	   test = test.replaceAll(":", "");
	   test = test.replace(".", "-");
	   m_myNewId.append(test.replaceAll(" ", "-"));
	   OwField field2 = getFieldManager().getFieldProvider().getField("D:custom:document.custom:sector");
	   //Don't forget to set the new value of your property
	   field2.setValue(m_myNewId.toString());
	       
	    }
	    
	/**
	Inserts a single edit field whose value can be set from user select dialog
	@see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
	*/
	public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
	    {  
	        // === list value        
	            String strText = "";
	            if (field_p.getValue() != null)
	                strText = field_p.getValue().toString();
	           //check if the value is already set
	            if (strText.length() > 1)
	                    insertReadOnlyField(w_p, fieldDef_p, field_p.getValue());
	            else              
	            {
	            w_p.write("<input size=\"40\" title=\"");
	            OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription( getContext().getLocale()));
	          //render default WeWebU CSS class
	            w_p.write("\" class=\"OwInputControl");
	            boolean isNumber = false;
	         //if is a Number insert type specific CSS class
	            if (isNumber)
	            {
	                w_p.write(" OwInputControlNumber");
	            }
	          //insert property specific CSS class
	            w_p.write(" OwInputControl_");
	            w_p.write(fieldDef_p.getClassName());
	            w_p.write("\" name=\"");
	            OwHTMLHelper.writeSecureHTML(w_p, strID_p);
	            w_p.write("\" type=\"text\" value=\"");
	            OwHTMLHelper.writeSecureHTML(w_p, strText);
	            //onFieldManagerFieldExit is used for custom validation in JavaScript
	            w_p.write("\" onchange='onChange();onFieldManagerFieldExit(\"");
	            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString( fieldDef_p.getClassName()));
	            w_p.write("\",\"");
	            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString( fieldDef_p.getJavaClassName()));
	            w_p.write("\",\"");
	            OwFieldProvider fieldProvider = getFieldManager().getFieldProvider();
	            if (fieldProvider != null)
	            {
	            	OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString( Integer.toString(fieldProvider.getFieldProviderType())));
	          w_p.write("\",\"");
	          OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString( fieldProvider.getFieldProviderName()));
	          w_p.write("\",\"");
	          }
	            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString( strID_p));
	          w_p.write("\",this.value)'/>");

	        // add lookup button
	       String searchDisplayName=fieldDef_p.getDisplayName(getContext().getLocale());

	        if (searchDisplayName == null || "".equals(searchDisplayName))
	        {
	            searchDisplayName = fieldDef_p.getClassName();
	        }

	        //renders the button CreateID and defines the function that should be called when the button is clicked => realized with getFormEventURL(�CreateID�,null) => this calls the �onCreateID� method
	        w_p.write("&nbsp;<input type='button' value='Create ID' name='Create Type ID"  + "' onclick=\"" + getFormEventURL("CreateID", null) + "\">");
	      }
	  }

	@Override
	public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception 
	{
		((OwStandardFieldManager) getFieldManager()).insertSingleReadOnlyFieldInternal(w_p, fieldDef_p, value_p);
	}

	@Override
	public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception 
	{
		return value_p;
	}


}
