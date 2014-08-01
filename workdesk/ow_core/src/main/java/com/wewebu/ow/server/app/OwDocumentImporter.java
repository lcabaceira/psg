package com.wewebu.ow.server.app;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.ui.OwView;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Interface for document importers.<br />
 * A document importer can be used by plugins that gather content like the add document,
 * the save or the checkin plugins to receive the content from the user.
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
public interface OwDocumentImporter
{

    /** context used in getView() if the view is used during the creation of new documents */
    public static final int IMPORT_CONTEXT_NEW = 1;

    /** context used in getView() if the view is used to save new content to existing documents */
    public static final int IMPORT_CONTEXT_SAVE = 2;

    /** context used in getView() if the view is used during the checkin of checked-out documents */
    public static final int IMPORT_CONTEXT_CHECKIN = 3;

    /**
     * Initialize this document importer from the XML fragment in the OwXMLUtil.
     * 
     * @param context_p The current OwMainAppContext for the importer
     * @param config_p OwXMLUtil with the XML fragment configuring this document importer
     * 
     * @throws OwConfigurationException if this document importer can not be initialized.
     */
    public void init(OwMainAppContext context_p, OwXMLUtil config_p) throws OwConfigurationException;

    /**
     * Get the URL of the icon representing this document importer in the UI.
     * 
     * @return String containing the URL to the icon
     */
    public String getIconURL();

    /**
     * Get the display name representing this document importer in the UI.
     * 
     * @return String containing the display name
     */
    public String getDisplayName();

    /**
     * Each imported document becomes a document in the back-end ECM system. But documents
     * can have multiple content streams in many ECM systems. This option defines if the
     * document importer should only allow document imports to consist of one single file.
     * 
     * @param singleFileImports_p true if the importer must accept at most one file per document
     */
    public void setSingleFileImports(boolean singleFileImports_p);

    /**
     * Returns the UI view of this document importer. This view has to use this document importer
     * as document in the sense of the MVC design pattern. Multiple calls of this method will
     * always return the same view. The context_p describes where the view should be used (new,
     * checkin or save).<br>
     * The view contains at least a button that invokes the import process and leads to a
     * subsequent invocation of the onDocumentImported() method of the OwDocumentImporterCallback
     * interface.
     * 
     * @param context_p
     * @param callback_p
     * 
     * @return the UI view of this document importer
     */
    public OwView getView(int context_p, OwDocumentImporterCallback callback_p);

    /**
     * Releases all <code>{@link OwDocumentImportItem}</code>s created by this importer during instantiation
     * or the last invokation of this method. Also releases all addition resources like the temporary folder
     * itself.<br>
     * The <code>{@link OwView}</code> returned by <code>{@link #getView(int, OwDocumentImporterCallback)}}</code>
     * might create multiple <code>{@link OwDocumentImportItem}</code>s during its lifetime. Although each
     * item has it's own <code>release()</code> method, this might not be sufficient. Many implementations
     * of this interface create a temporary folder upon instantiation or upon first usage that contains all
     * temporary files associated with the imported documents. So there is a need to remove the containing
     * folder itself and not only all files in it.<br>
     * <b>Please note 1:</b><br>
     * The importer and all it's views must still be able to import documents after this method has been
     * called. In fact, this method gets called multiple times. So it is wise for an importer to create its
     * temporary folder upon first usage and remove it whenever this method is called. So it is re-created
     * as soon as the importer is invoked after an <code>releaseAll()</code> call.<br>
     * <b>Please note 2:</b><br>
     * This method must not rely on the fact that the <code>release()</code> method of all imported items
     * has been called or has not been called. It must be able to deal with any situation.
     */
    public void releaseAll();

    /**
     * Should return a boolean value which define if a post process view should be displayed or not 
     * after saving document into the back-end system. 
     * @param importContext_p int representing one of OwDocumentImporter.IMPORT_CONTEXT_... value
     * @return boolean <code>true</code> only if importer has a post process view for the given context
     */
    public boolean hasPostProcessView(int importContext_p);

    /**
     * Return the view which should be displayed for post processing.
     * <p>Can return null, but should correspond with the {@link #hasPostProcessView(int)}
     * method by returning <code>false</code> for the same importContext_p</p>
     * @param importContext_p int representing one of OwDocumentImporter.IMPORT_CONTEXT_... value
     * @param savedObj_p OwObject which was saved before (non-null reference)
     * @return OwView or null if not exist for given context
     */
    public OwView getPostProcessView(int importContext_p, OwObject savedObj_p);

}