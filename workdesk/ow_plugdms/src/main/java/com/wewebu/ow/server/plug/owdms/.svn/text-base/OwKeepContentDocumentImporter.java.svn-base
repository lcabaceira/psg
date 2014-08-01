package com.wewebu.ow.server.plug.owdms;

import java.io.InputStream;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDocumentImportItem;
import com.wewebu.ow.server.app.OwDocumentImporter;
import com.wewebu.ow.server.app.OwDocumentImporterCallback;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSubMenuView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.owdms.log.OwLog;
import com.wewebu.ow.server.ui.OwLayout;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Keep the content of a document on checkin a document.
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
public class OwKeepContentDocumentImporter implements OwDocumentImporter
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwKeepContentDocumentImporter.class);
    private OwMainAppContext context;
    /** the configuration of this importer */
    protected OwXMLUtil m_config;

    public String getDisplayName()
    {
        return this.context.localize("plug.dms.OwKeepContentDocumentImporter.DisplayName", "Keep content");
    }

    public String getIconURL()
    {
        try
        {
            return this.context.getDesignURL() + m_config.getSafeTextValue("icon", "/images/docimport/saved_document.png");
        }
        catch (Exception e)
        {
            LOG.error("Can not get design URL", e);
            return null;
        }
    }

    public OwView getView(int context_p, OwDocumentImporterCallback callback_p)
    {
        if (context_p == OwDocumentImporter.IMPORT_CONTEXT_CHECKIN)
        {
            return new OwKeepContentDocumentImporterView(this, callback_p);
        }
        else
        {
            return null;
        }
    }

    public void init(OwMainAppContext context_p, OwXMLUtil config_p) throws OwConfigurationException
    {
        //get the context for further localization
        this.context = context_p;
        m_config = config_p;
    }

    public void releaseAll()
    {
    }

    public void setSingleFileImports(boolean singleFileImports_p)
    {
    }

    public OwView getPostProcessView(int importContext_p, OwObject savedObj_p)
    {
        return null;
    }

    public boolean hasPostProcessView(int importContext_p)
    {
        return false;
    }

    //-------------------INTERNAL-Classes------------------------------------------    
    /**Just simple View to display that content is not overwritten*/
    protected class OwKeepContentDocumentImporterView extends OwLayout
    {
        OwDocumentImporterCallback callback;
        OwDocumentImporter importer;

        public OwKeepContentDocumentImporterView(OwDocumentImporter importer_p, OwDocumentImporterCallback callback_p)
        {
            this.callback = callback_p;
            this.importer = importer_p;
        }

        protected void init() throws Exception
        {
            super.init();
            OwSubMenuView sub = new OwSubMenuView();
            try
            {
                addView(sub, 1, null);
                // add menu button
                int indexOfDefaultButton = sub.addMenuItem(this, OwKeepContentDocumentImporter.this.context.localize("plug.dms.OwKeepContentDocumentImporter.OwKeepContentDocumentImporterView.btn", "Keep content"), "KeepData", null);
                sub.setDefaultMenuItem(indexOfDefaultButton);
            }
            catch (Exception e)
            {
                LOG.warn("OwKeepContentDocumentImporter#OwKeepContentDocumentImporterView.init(): Exception adding/building the MenuItem...", e);
            }
        }

        protected void onRender(Writer w_p) throws Exception
        {
            OwMainAppContext myContext = OwKeepContentDocumentImporter.this.context;
            w_p.write("<div class=\"contentDocumentImporter\">");
            w_p.write(myContext.localize("plug.dms.OwKeepContentDocumentImporter.OwKeepContentDocumentImporterView.label", "Keep the content of the document"));
            this.renderRegion(w_p, 1);
            w_p.write("</div>");
        }

        public void onKeepData(javax.servlet.http.HttpServletRequest request_p, Object obj_p) throws Exception
        {
            this.callback.onDocumentImported(this.importer, new OwKeepContentDocumentImporterItem(OwKeepContentDocumentImporter.this.context.getLocale()));
        }
    }

    /**
     *<p>
     * A simple DocumentImporterItem which do nothing, just returns default values.
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
    protected static class OwKeepContentDocumentImporterItem implements OwDocumentImportItem
    {
        private String lable;

        protected OwKeepContentDocumentImporterItem(Locale locale_p)
        {
            this.lable = OwString.localize(locale_p, "plug.dms.OwKeepContentDocumentImporter.OwKeepContentDocumentImporterItem.label", "Keep content (do not change anything)");
        }

        public String getContentMimeParameter(int i_p)
        {
            return "";
        }

        public String getContentMimeType(int i_p)
        {
            return "";
        }

        public InputStream getContentStream(int i_p) throws Exception
        {
            return null;
        }

        public int getContentStreamCount()
        {
            return 0;
        }

        public String getDisplayName()
        {
            return this.lable;
        }

        public String getPreviewFilePath(int i_p)
        {
            return null;
        }

        public Map getPropertyValueMap()
        {
            return null;
        }

        public String getProposedDocumentName()
        {
            return null;
        }

        public void release() throws Exception
        {
        }

        public Boolean getCheckinAsMajor()
        {
            return null;
        }

        public String getObjectClassName()
        {
            return null;
        }
    }

}