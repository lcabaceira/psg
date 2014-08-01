package com.wewebu.ow.server.fieldctrlimpl;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwFieldManagerControl;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwSettingsProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwHTMLHelper;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Field control implementation for notes.
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
 *@since 2.5.2.0
 */
public class OwFieldManagerControlNote extends OwFieldManagerControl
{
    /** the threshold for shorten m_notes*/
    private static final int SHORTEN_THRESHOLD = 200;
    /** the logger*/
    private static final Logger LOG = OwLogCore.getLogger(OwFieldManagerControlNote.class);
    /**error cause handler*/
    private Map m_errorCauseMap = new HashMap();

    /**
     * Exception for notes management.
     */
    public static class OwNoteException extends OwException
    {
        private static final long serialVersionUID = -2878894039716647164L;

        /**
         * Constructor 
         */
        public OwNoteException(String strMessage_p)
        {
            super(strMessage_p);
        }

        /**
         * @see com.wewebu.ow.server.exceptions.OwException#getModulName()
         */
        public String getModulName()
        {
            return "OECM";
        }
    }

    /**
     *<p>
     * Convenient class for holding a note.
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
    public static class OwNote
    {
        /** the m_user */
        private String m_user;
        /** the time stamp as string */
        private String m_timestamp;
        /** the note m_content */
        private String m_content;
        /** localization for messages */
        private Locale m_locale;
        /** flag for shorten note */
        private boolean m_shouldShortenNote;
        /** the threshold */
        private int m_shortenThreshold_p;

        /**
         * Constructor
         * @param locale_p - the locale parameter.
         * @param user_p - m_user
         * @param timestamp_p - time stamp
         * @param content_p - note m_content.
         */
        public OwNote(Locale locale_p, String user_p, String timestamp_p, String content_p)
        {
            this.m_locale = locale_p;
            if (user_p == null || timestamp_p == null)
            {
                throw new IllegalArgumentException(OwString.localize(m_locale, "app.OwFieldManagerControlNote.cannot_create_note", "Cannot create note. The user and/or the time stamp should not be null."));
            }
            this.m_user = user_p;
            this.m_timestamp = timestamp_p;
            this.m_content = content_p.replaceAll(OwNoteDataModel.NOTE_DELIMITER_REGEXP, "\n");
            this.m_content = this.m_content.replaceAll("\\n+", "\n");
            this.m_content = this.m_content.replaceAll("[\\r\\n]+", "\n");

            if (m_content.endsWith("\n"))
            {
                this.m_content = m_content.substring(0, m_content.length() - 1);
            }
            if (m_content.startsWith("\n"))
            {
                if (m_content.length() > 1)
                {
                    this.m_content = m_content.substring(1);
                }
                else
                {
                    m_content = "";
                }
            }
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object o_p)
        {
            if (o_p instanceof OwNote)
            {
                OwNote convertedNote = (OwNote) o_p;
                return this.m_user.equals(convertedNote.m_user) && this.m_timestamp.equals(convertedNote.m_timestamp);
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return m_user.hashCode() + m_timestamp.hashCode();
        }

        /**
         * Get note m_content.
         * @return note m_content.
         */
        public String getFormatedNote()
        {
            StringBuffer formatedNote = new StringBuffer(m_user);
            formatedNote.append("\n");
            formatedNote.append(m_timestamp);
            formatedNote.append("\n");
            formatedNote.append(m_content);
            formatedNote.append(OwNoteDataModel.NOTE_DELIMITER);
            return formatedNote.toString();
        }

        /**
         * Get the note header 
         * @return - the note header
         */
        public String getHeader()
        {
            return m_user + "\n" + m_timestamp + "\n";
        }

        /**
         * Get the size of this note. 
         */
        public int getSize()
        {
            return getFormatedNote().length();
        }

        /**
         * Append m_content to this note.
         * @param content_p - the m_content to be added.
         */
        public void appendContent(String content_p)
        {
            if (content_p != null)
            {
                m_content = m_content + "\n" + content_p;
            }
        }

        /**
         * Get the note m_content as lines.
         * If necessary, truncate the lines to a given threshold.
         * @return the lines.
         */
        public String[] getContentLines()
        {
            String[] result;
            String content = m_content;
            if (m_shouldShortenNote)
            {
                if (m_shortenThreshold_p >= 1 && m_shortenThreshold_p < m_content.length())
                {
                    String shortenedContent = m_content.substring(0, m_shortenThreshold_p - 1);
                    content = shortenedContent + "\n...";
                }
            }
            result = content.split("\n");
            return result;
        }

        /**
         * Build a note object from a String object.
         * @param locale_p the current locale.
         * @param rawNoteText_p - the m_content.
         * @return the created note.
         * @throws OwNoteException - when a note cannot be created from the given text.
         */
        public static OwNote buildNote(Locale locale_p, String rawNoteText_p) throws OwNoteException
        {
            if (rawNoteText_p == null)
            {
                throw new IllegalArgumentException(OwString.localize(locale_p, "app.OwFieldManagerControlNote.invalid_note_content", "The note content should not be null."));
            }
            rawNoteText_p = rawNoteText_p.replaceAll(OwNoteDataModel.NOTE_DELIMITER_REGEXP, "\n");
            rawNoteText_p = rawNoteText_p.replaceAll("\\n+", "\n");
            rawNoteText_p = rawNoteText_p.replaceAll("[\\r\\n]+", "\n");

            String[] noteItems = rawNoteText_p.split("\n");
            String user = "";
            String date = "";
            String noteContent = "";
            if (noteItems.length > 1)
            {
                if (noteItems.length <= 2)
                {
                    user = noteItems[0];
                    date = noteItems[1];
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("OwFieldManagerControlNote.buildNote: Unable to recongnize the m_user and m_timestamp pattern. We will create a note with empty m_user and m_timestamp! Note Text:" + rawNoteText_p);
                    }
                }
                else
                {
                    user = noteItems[0];
                    date = noteItems[1];
                    String noteHeader = noteItems[0] + "\n" + noteItems[1] + "\n";
                    //check if 
                    if (noteHeader.length() > rawNoteText_p.length())
                    {
                        user = "";
                        date = "";
                        noteContent = rawNoteText_p;
                    }
                    else
                    {
                        noteContent = rawNoteText_p.substring(noteHeader.length());
                    }

                }

            }
            else
            {
                noteContent = rawNoteText_p;
            }
            return new OwNote(locale_p, user, date, noteContent);
        }

        /**
         * Build a note object from a String object (old style m_notes).
         * @param locale_p - the current locale
         * @param rawNoteText_p - the m_content.
         * @return the created note.
         * @throws OwNoteException - thrown when the note cannot be created.
         */
        public static OwNote buildNoteFromOldContent(Locale locale_p, String rawNoteText_p) throws OwNoteException
        {
            if (rawNoteText_p == null)
            {
                throw new IllegalArgumentException(OwString.localize(locale_p, "app.OwFieldManagerControlNote.invalid_note_content", "The note content should not be null."));
            }

            rawNoteText_p = rawNoteText_p.replaceAll(OwNoteDataModel.NOTE_DELIMITER_REGEXP, "\n");
            rawNoteText_p = rawNoteText_p.replaceAll("\\n+", "\n");
            rawNoteText_p = rawNoteText_p.replaceAll("[\\r\\n]+", "\n");

            String[] noteItems = rawNoteText_p.split("\n");
            String user = "";
            String timestamp = "";
            String noteContent = "";
            if (noteItems.length < 2)
            {
                noteContent = rawNoteText_p;
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwFieldManagerControlNote.buildNoteFromOldContent: Unable to recongnize the m_user and m_timestamp pattern. We will create a note with empty m_user and m_timestamp! Note Text:" + rawNoteText_p);
                }
            }
            else
            {
                String header = noteItems[0];
                String[] tokens = header.split(",");
                if (tokens.length == 2)
                {
                    user = tokens[0].trim();
                    timestamp = tokens[1].trim().substring(0, tokens[1].lastIndexOf(":") - 1);
                }
                noteContent = rawNoteText_p;
                //check if safely can extract header
                if (rawNoteText_p.indexOf(header) >= 0 && header.length() < rawNoteText_p.length())
                {
                    noteContent = rawNoteText_p.substring(header.length() + 1);
                }
                else
                {
                    user = "";
                    timestamp = "";
                }
            }
            return new OwNote(locale_p, user, timestamp, noteContent);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return getFormatedNote();
        }

        /**
         * Set the shouldShortNote flag.
         * @param shouldShortNote_p - the flag.
         */
        public void setShouldShortenNote(boolean shouldShortNote_p)
        {
            this.m_shouldShortenNote = shouldShortNote_p;
        }

        /** 
         * Set the threshold for shortening note.
         * @param threshold_p - the value of threshold.
         */
        public void setShortenThreshold(int threshold_p)
        {
            this.m_shortenThreshold_p = threshold_p;
        }

    }

    /**
     *<p>
     * Class holding the new note data model.
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
    public static class OwNoteDataModel
    {
        /** delimiter for note */
        public static final String NOTE_DELIMITER = "\n ";
        /** delimiter for note regexp */
        public static final String NOTE_DELIMITER_REGEXP = "\\n +";
        /** delimiter for old note */
        private static final String OLD_NOTE_DELIMITER = "\\n\\n";
        /** constant for no max size set for this note */
        private static final int NO_MAX_SIZE_SET = Integer.MIN_VALUE;
        /** the original string to build a note from it */
        private String m_noteRawContent;
        /** list of m_notes */
        private List m_notes;
        /** Max size for note */
        private int m_maxSize = NO_MAX_SIZE_SET;
        /** the threshold for shorten m_notes feature */
        private int m_shortenThreshold = NO_MAX_SIZE_SET;
        /** locale member */
        private Locale m_locale;
        /** flag indicating that last note was shortened */
        private boolean m_isLastNoteShortened = false;
        private boolean m_showNewNotesAtTheEnd = true;
        private boolean isNullContent = false;

        /**
         * Constructor - create a list of m_notes from note m_content, if it's possible. 
         * @param locale_p - the current locale
         * @param noteContent_p - m_content for the note

         * @throws OwNoteException - thrown when the note model cannot be created from the given string
         */
        public OwNoteDataModel(Locale locale_p, String noteContent_p) throws OwNoteException
        {
            this.m_locale = locale_p;
            this.m_noteRawContent = noteContent_p;
            this.m_notes = new LinkedList();
            this.isNullContent = noteContent_p == null;
            if (m_noteRawContent != null && m_noteRawContent.trim().length() != 0)
            {
                buildNotes();
            }
        }

        /**
         * Constructor - create a list of m_notes from note m_content, if it's possible. 
         * @param locale_p - the current locale
         * @param noteContent_p - m_content for the note
         * @param showNewNotesAtTheEnd_p - if <code>true</code> the new notes are shown at the end of the field.
         * @throws OwNoteException - thrown when the note model cannot be created from the given string
         */
        public OwNoteDataModel(Locale locale_p, String noteContent_p, boolean showNewNotesAtTheEnd_p) throws OwNoteException
        {
            this(locale_p, noteContent_p);
            this.m_showNewNotesAtTheEnd = showNewNotesAtTheEnd_p;
        }

        /**
         * Get the note m_content as was set.
         * @return the original note m_content.
         */
        public String getNoteRawContent()
        {
            return m_noteRawContent;
        }

        /**
         * Create the list of m_notes.
         * @throws OwNoteException
         */
        private void buildNotes() throws OwNoteException
        {
            //try to remove empty comments

            if (m_noteRawContent != null)
            {
                Pattern p = Pattern.compile("[A-Z0-9._%-]+@[A-Z0-9._%-]+\\.[A-Z0-9._%-]+\\, [0-9._%-]", Pattern.CASE_INSENSITIVE);

                Matcher m = p.matcher(m_noteRawContent);
                boolean isOldStyle = m.find();

                if (!isOldStyle)
                {
                    m_noteRawContent = m_noteRawContent.replaceAll("[\\n\\n\\n]+", "\n");
                }
                String rawNotes[] = m_noteRawContent.split(OLD_NOTE_DELIMITER);
                //only one old note
                if (!isOldStyle)
                {
                    String lines[] = m_noteRawContent.split("\\n");
                    if (lines.length > 0)
                    {
                        int atPosition = lines[0].indexOf("@");
                        int commaPosition = lines[0].indexOf(",");
                        int columnPosition = lines[0].indexOf(":");
                        if (atPosition < commaPosition && commaPosition < columnPosition && lines[0].endsWith(":"))
                        {
                            isOldStyle = true;
                        }
                    }
                }
                if (isOldStyle)
                {
                    m_noteRawContent = m_noteRawContent.replaceAll(NOTE_DELIMITER_REGEXP, "\n");
                    rawNotes = m_noteRawContent.split(OLD_NOTE_DELIMITER);
                }
                if (!isOldStyle)
                {
                    //new style m_notes
                    rawNotes = m_noteRawContent.split(NOTE_DELIMITER_REGEXP);
                    for (int i = 0; i < rawNotes.length; i++)
                    {

                        String rtNote = rawNotes[i];

                        if (!rtNote.isEmpty())
                        {
                            OwNote note = OwNote.buildNote(m_locale, rawNotes[i]);
                            m_notes.add(note);
                        }
                    }
                }
                else
                {
                    List mergedNotes = new LinkedList();
                    for (int i = 0; i < rawNotes.length; i++)
                    {
                        if (rawNotes[i].indexOf('@') != -1 && rawNotes[i].indexOf(":") != -1)
                        {
                            mergedNotes.add(rawNotes[i]);
                        }
                        else
                        {
                            if (mergedNotes.isEmpty())
                            {
                                mergedNotes.add(rawNotes[i]);
                            }
                            else
                            {
                                //add to the last correct note
                                String lastNote = (String) mergedNotes.get(mergedNotes.size() - 1);
                                lastNote = lastNote + "\n" + rawNotes[i];
                                mergedNotes.set(mergedNotes.size() - 1, lastNote);
                            }
                        }
                    }
                    // old style m_notes
                    for (int i = 0; i < mergedNotes.size(); i++)
                    {
                        try
                        {
                            OwNote note = OwNote.buildNoteFromOldContent(m_locale, (String) mergedNotes.get(i));
                            m_notes.add(note);
                        }
                        catch (OwNoteException e)
                        {
                            if (!m_notes.isEmpty())
                            {
                                OwNote lastNote = (OwNote) m_notes.get(m_notes.size() - 1);
                                lastNote.appendContent(rawNotes[i]);
                            }
                            else
                            {
                                throw e;
                            }
                        }

                    }
                }
            }
        }

        /**
         * Check if this note model content is null;
         * @return - <code>true</code> if this model content is <code>null</code>.
         * @since 3.1.0.0
         */
        public boolean isNullContent()
        {
            return isNullContent;
        }

        /**
         * Append a note to this model
         * @param note_p
         */
        public void appendNote(OwNote note_p) throws OwNoteException
        {
            boolean shouldAddNote = true;
            if (this.m_maxSize != NO_MAX_SIZE_SET)
            {
                //check if the new note is bigger than max size
                if (note_p.getSize() > this.m_maxSize)
                {
                    shouldAddNote = false;
                }
                if (shouldAddNote)
                {
                    int possibleSize = getSize() + note_p.getSize();
                    if (possibleSize > this.m_maxSize)
                    {
                        //should delete older m_notes
                        do
                        {
                            m_notes.remove(0);
                            possibleSize = getSize() + note_p.getSize();
                        } while (possibleSize > this.m_maxSize);
                    }
                }
            }
            if (shouldAddNote)
            {
                m_notes.add(note_p);
                isNullContent = false;
            }
            else
            {
                throw new OwNoteException(OwString.localize1(m_locale, "app.OwFieldManagerControlNote.note_is_too_big", "The text of the note is too long by %1 characters.", "" + Math.abs(this.m_maxSize - note_p.getSize() - this.getSize())));
            }
        }

        /**
         * Get the size occupied by this note.
         * @return the size
         */
        public int getSize()
        {
            return this.getTrimmedText().length();
        }

        /** 
         * Get the list of m_notes
         * @return - the list of m_notes.
         */
        public List getNotes()
        {
            List result = new LinkedList();
            for (Iterator iterator = m_notes.iterator(); iterator.hasNext();)
            {
                Object object = iterator.next();
                result.add(object);
            }

            if (!m_showNewNotesAtTheEnd)
            {
                Collections.reverse(result);
            }
            return result;
        }

        /** 
         * Get the m_content of this note model, as needed to be saved in m_content manager system.
         * The delimiter between m_notes is "\n " string.
         * @return the m_content of all m_notes related to this model, as described in the specification.  
         */
        public String getTrimmedText()
        {
            StringBuffer buff = new StringBuffer();
            Iterator notesIterator = m_notes.iterator();
            while (notesIterator.hasNext())
            {
                OwNote note = (OwNote) notesIterator.next();
                buff.append(note.toString());
            }
            return buff.toString();
        }

        /**
         * Get the number of m_notes.
         * @return - number of m_notes.
         */
        public int getNumberOfNotes()
        {
            return m_notes.size();
        }

        /**
         * Get the note specified by index, or null.
         * @param index_p
         * @return the note corresponding to the given index, or null.
         */
        public OwNote getNote(int index_p)
        {
            return index_p < 0 || index_p > getNotes().size() - 1 ? null : (OwNote) getNotes().get(index_p);
        }

        /**
         * Returns <code>true</code> in case the last note was shortened.
         * @return - <code>true</code> in case the last note was shortened.
         */
        public boolean isLastNoteShortened()
        {
            return m_isLastNoteShortened;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return getTrimmedText();
        }

        /**
         * Set max size for this note.
         * @param maxSize_p
         */
        public void setMaxSize(int maxSize_p)
        {
            if (maxSize_p < 0)
            {
                throw new IllegalArgumentException(OwString.localize(m_locale, "app.OwFieldManagerControlNote.invalid_note_maxsize", "The note content size is invalid."));
            }
            this.m_maxSize = maxSize_p;
        }

        /**
         * Set the threshold: the note with size over this threshold will be shortened.
         * This apply only for m_notes displayed in Result Lists.
         * @param shortenThreshold_p
         */
        public void setShortenThreshold(int shortenThreshold_p)
        {
            if (shortenThreshold_p < 0)
            {
                throw new IllegalArgumentException(OwString.localize(m_locale, "app.OwFieldManagerControlNote.invalid_note_threshold", "The maximal size of chars a note can have (threshold) is invalid."));
            }
            this.m_shortenThreshold = shortenThreshold_p;
        }

        /**
         * Check if the note need to be shortened.
         * @return <code>true</code> if the note should be shortened.
         */
        public boolean shouldShortenNotes()
        {
            boolean result = false;
            if (this.m_shortenThreshold != NO_MAX_SIZE_SET && this.getSize() > this.m_shortenThreshold)
            {
                result = true;
            }
            return result;
        }

        /**
         * Get a list with shortened m_notes.
         * @return the shortened m_notes lists.
         */
        public List getShortenedNotes()
        {
            List result = new LinkedList();
            List currentNotes = m_notes;
            int size = 0;
            for (int i = currentNotes.size() - 1; i >= 0; i--)
            {
                OwNote note = (OwNote) currentNotes.get(i);
                size += note.getSize();
                if (size <= this.m_shortenThreshold)
                {
                    result.add(note);
                }
                else
                {
                    break;
                }
            }
            if (result.isEmpty() && !currentNotes.isEmpty())
            {
                //shorten last note
                OwNote lastNote = (OwNote) currentNotes.get(currentNotes.size() - 1);
                lastNote.setShouldShortenNote(true);
                lastNote.setShortenThreshold(this.m_shortenThreshold);
                this.m_isLastNoteShortened = true;
                result.add(lastNote);
            }
            if (m_showNewNotesAtTheEnd)
            {
                Collections.reverse(result);
            }
            return result;
        }
    }

    /**
     * Inserts a HTML text area with error and value displayed for insertEditable hook methods.
     * @param w_p
     * @param fieldDef_p
     * @param value_p
     * @param errorCause_p
     * @param strID_p
     * @throws Exception
     * @since 3.2.0.0
     */
    protected void insertTextArea(Writer w_p, OwFieldDefinition fieldDef_p, String value_p, String errorCause_p, String strID_p) throws Exception
    {
        w_p.write("         <textarea ");
        w_p.write("id=\"");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write("\" title=\"");
        OwHTMLHelper.writeSecureHTML(w_p, fieldDef_p.getDescription(getContext().getLocale()));
        /*write the specific CSS classes for this control*/
        w_p.write("\" cols=\"40\" rows=\"5\" class=\"OwInputControl OwInputControlMultiString OwInputControl_");
        w_p.write(fieldDef_p.getClassName());
        w_p.write("\" name=\"");
        OwHTMLHelper.writeSecureHTML(w_p, strID_p);
        w_p.write("\" onblur=\"onFieldManagerFieldExit('");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getClassName()));
        w_p.write("','");
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldDef_p.getJavaClassName()));
        w_p.write("','");
        OwFieldProvider fieldProvider = getFieldManager().getFieldProvider();
        if (fieldProvider != null)
        {
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(Integer.toString(fieldProvider.getFieldProviderType())));
            w_p.write("','");
            OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(fieldProvider.getFieldProviderName()));
            w_p.write("','");
        }
        OwHTMLHelper.writeSecureHTML(w_p, OwHTMLHelper.encodeJavascriptString(strID_p));
        w_p.write("',this.value)\" onkeydown=\"event.cancelBubble=true\">");

        OwHTMLHelper.writeSecureHTML(w_p, errorCause_p);
        if (value_p != null)
        {
            OwHTMLHelper.writeSecureHTML(w_p, value_p);
        }

        w_p.write("</textarea>");

    }

    /**
     * {@link com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(Writer, OwFieldDefinition, OwField, String)} hook 
     * for volatile field instances.
     * A persisted field instance combines multiple note entries into one value. 
     *  
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(Writer, OwFieldDefinition, OwField, String)
     * @since 3.2.0.0
     */
    protected void insertPersistedEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        boolean showNewNotesAtTheEnd = shouldShowNewNotesAtTheEndOfField();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("OwFieldManagerControlNote.insertPersistedEditField: Show new notes at the end of the field: " + showNewNotesAtTheEnd);
        }
        String error = getFieldManager().getSafeFieldError(field_p);
        String errorCause = "";
        if (error != null && error.length() > 0)
        {
            String value = (String) this.m_errorCauseMap.get(strID_p);
            if (value != null)
            {
                errorCause = value;
                this.m_errorCauseMap.remove(strID_p);
            }
        }

        insertTextArea(w_p, fieldDef_p, null, errorCause, strID_p);

        String oldNotes = (String) field_p.getValue();
        OwNoteDataModel noteDataModel = new OwNoteDataModel(getContext().getLocale(), oldNotes, showNewNotesAtTheEnd);

        if (!noteDataModel.isNullContent())
        {
            w_p.write("         <input type=\"hidden\" name=\"" + getHiddenNotesControlId(strID_p) + "\" value=\"");
            OwHTMLHelper.writeSecureHTML(w_p, noteDataModel.getTrimmedText());
            w_p.write("\"/>");

            renderNotesContent(w_p, noteDataModel, showNewNotesAtTheEnd);
        }

    }

    /**
     * {@link com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(Writer, OwFieldDefinition, OwField, String)} hook 
     * for volatile field instances.
     * A volatile field instance only considers the last note entry. 
     *  
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(Writer, OwFieldDefinition, OwField, String)
     * @since 3.2.0.0
     */
    protected void insertVolatileEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        String error = getFieldManager().getSafeFieldError(field_p);
        String errorCause = "";
        if (error != null && error.length() > 0)
        {
            String value = (String) this.m_errorCauseMap.get(strID_p);
            if (value != null)
            {
                errorCause = value;
                this.m_errorCauseMap.remove(strID_p);
            }
        }

        String oldNote = (String) field_p.getValue();
        insertTextArea(w_p, fieldDef_p, oldNote, errorCause, strID_p);
    }

    /**
     * 
     * @return true if this is a volatile note field instance and false for persisted instances  <br/>
     *         A volatile field instance only considers the last note entry.<br/>
     *         A persisted field instance combines multiple note entries into one value at update time.
     * @since 3.2.0.0 
     */
    protected boolean isVolatileNote()
    {
        OwFieldManager fieldManager = getFieldManager();
        return fieldManager.isFieldProviderType(OwFieldProvider.TYPE_SEARCH);
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertEditField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, com.wewebu.ow.server.field.OwField, java.lang.String)
     */
    public void insertEditField(Writer w_p, OwFieldDefinition fieldDef_p, OwField field_p, String strID_p) throws Exception
    {
        if (isVolatileNote())
        {
            insertVolatileEditField(w_p, fieldDef_p, field_p, strID_p);
        }
        else
        {
            insertPersistedEditField(w_p, fieldDef_p, field_p, strID_p);
        }
    }

    /**
     * Read the settings flag.
     * @return <code>true</code> - if the newest notes are shown at the end of the notes field.  
     * @throws Exception
     * @since 3.0.0.0
     */
    private boolean shouldShowNewNotesAtTheEndOfField() throws Exception
    {
        boolean showNewNotesAtTheEnd = true;
        OwSettingsProperty prop = ((OwMainAppContext) getContext()).getConfiguration().getSettings().getProperty("com.wewebu.ow.Workdesk", "DisplayNewNoteAtTheEnd");
        if (prop != null)
        {
            showNewNotesAtTheEnd = ((Boolean) prop.getValue()).booleanValue();
        }
        return showNewNotesAtTheEnd;
    }

    /**
     * Render m_notes m_content.
     * @param showNewNotesAtTheEnd_p - show new notes at the end of the note field.
     */
    private void renderNotesContent(Writer w_p, OwNoteDataModel noteDataModel_p, boolean showNewNotesAtTheEnd_p) throws IOException
    {
        //select styles
        String notesContainerStyle = "notesContainer";
        String noteHeaderStyle = "noteHeader";
        String noteContentStyle = "noteContent";
        String noteResumeeStyle = "noteResumee";
        if (getFieldManager().isFieldProviderType(OwFieldProvider.TYPE_RESULT_LIST))
        {
            notesContainerStyle = "notesContainer_short";
            noteHeaderStyle = "noteHeader_short";
            noteContentStyle = "noteContent_short";
        }
        w_p.write("     <div class=\"" + notesContainerStyle + "\">\n");
        List notes = noteDataModel_p.getNotes();
        if (noteDataModel_p.shouldShortenNotes())
        {
            notes = noteDataModel_p.getShortenedNotes();
            if (showNewNotesAtTheEnd_p)
            {
                appendNoteNotDisplayedSymbol(w_p, noteDataModel_p, noteResumeeStyle);
            }
        }
        for (int i = 0; i < notes.size(); i++)
        {
            w_p.write("     <div class=\"" + noteHeaderStyle + "\"> \n");
            OwNote note = (OwNote) notes.get(i);
            OwHTMLHelper.writeSecureHTML(w_p, note.getHeader(), true);
            w_p.write("     </div>");
            w_p.write("     <div class=\"" + noteContentStyle + "\">\n");
            String[] lines = note.getContentLines();
            for (int j = 0; j < lines.length; j++)
            {
                OwHTMLHelper.writeSecureHTML(w_p, lines[j], true);
                w_p.write("<br/>\n");
            }
            w_p.write("     </div>\n");
        }
        if (!showNewNotesAtTheEnd_p && noteDataModel_p.shouldShortenNotes())
        {
            appendNoteNotDisplayedSymbol(w_p, noteDataModel_p, noteResumeeStyle);
        }
        w_p.write("     </div>\n");
    }

    /**
     * Append <code>...</code> symbol for the notes that are missing
     * @param w_p - writer
     * @param noteDataModel_p - note data model
     * @param noteResumeeStyle_p - the style sheet
     * @throws IOException 
     * @since 3.0.0.0
     */
    private void appendNoteNotDisplayedSymbol(Writer w_p, OwNoteDataModel noteDataModel_p, String noteResumeeStyle_p) throws IOException
    {
        if (!noteDataModel_p.isLastNoteShortened() || noteDataModel_p.getNumberOfNotes() > 1)
        {
            w_p.write("<div class=\"" + noteResumeeStyle_p + "\">");
            w_p.write("...");
            w_p.write("</div>");
        }
    }

    /**
     * Get hidden m_notes control ID.
     * @param strID_p - the field ID.
     * @return the hidden m_notes control ID.
     */
    private String getHiddenNotesControlId(String strID_p)
    {
        return strID_p + "_hidden";
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#insertReadOnlyField(java.io.Writer, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object)
     */
    public void insertReadOnlyField(Writer w_p, OwFieldDefinition fieldDef_p, Object value_p) throws Exception
    {
        String oldNotes = (String) value_p;
        boolean showNewNotesAtTheEnd = shouldShowNewNotesAtTheEndOfField();
        //if (LOG.isDebugEnabled())
        //{
        //    LOG.debug("OwFieldManagerControlNote.insertReadOnlyField: Show new notes at the end of the field: " + showNewNotesAtTheEnd);
        //}

        OwNoteDataModel noteDataModel = new OwNoteDataModel(getContext().getLocale(), oldNotes, showNewNotesAtTheEnd);
        if (!getFieldManager().isFieldProviderType(OwFieldProvider.TYPE_RESULT_LIST))
        {
            renderNotesContent(w_p, noteDataModel, showNewNotesAtTheEnd);
        }
        else
        {
            noteDataModel.setShortenThreshold(SHORTEN_THRESHOLD);
            renderNotesContent(w_p, noteDataModel, showNewNotesAtTheEnd);
        }
    }

    /**
     * {@link com.wewebu.ow.server.app.OwFieldManagerControl#updateField(HttpServletRequest, OwFieldDefinition, Object, String)} hook 
     * for persisted field instances.
     * A persisted field instance combines multiple note entries into one value. 
     *  
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     * @since 3.2.0.0
     */
    protected Object updatePersistedField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        String noteToBeAppended = request_p.getParameter(strID_p);
        String oldNotes = request_p.getParameter(strID_p + "_hidden");
        String resultValue = null;

        if (noteToBeAppended != null && noteToBeAppended.length() != 0)
        {
            OwNoteDataModel noteDataModel = new OwNoteDataModel(getContext().getLocale(), oldNotes);
            if (fieldDef_p != null && fieldDef_p.getMaxValue() != null && fieldDef_p.getMaxValue() instanceof Integer)
            {
                noteDataModel.setMaxSize(((Integer) fieldDef_p.getMaxValue()).intValue());
            }

            if (noteToBeAppended.length() > 0)
            {
                try
                {
                    String currentUser = ((OwMainAppContext) getContext()).getNetwork().getCredentials().getUserInfo().getUserLongName();
                    DateFormat dateFormat = new java.text.SimpleDateFormat(((OwMainAppContext) getContext()).getDateFormatString());
                    String timestamp = dateFormat.format(new Date());
                    OwNote note = new OwNote(getContext().getLocale(), currentUser, timestamp, noteToBeAppended);
                    noteDataModel.appendNote(note);
                }
                catch (Exception e)
                {
                    this.m_errorCauseMap.put(strID_p, noteToBeAppended);
                    throw e;
                }
            }
            resultValue = noteDataModel.getTrimmedText();
        }
        else
        {
            if (oldNotes != null)
            {
                OwNoteDataModel noteDataModel = new OwNoteDataModel(getContext().getLocale(), oldNotes);
                resultValue = noteDataModel.getTrimmedText();
            }
        }

        return resultValue;

    }

    /**
     * {@link com.wewebu.ow.server.app.OwFieldManagerControl#updateField(HttpServletRequest, OwFieldDefinition, Object, String)} hook 
     * for volatile field instances.
     * A volatile field instance only considers the last note entry. 
     *  
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     * @since 3.2.0.0
     */
    protected Object updateVolatileField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        String theNote = request_p.getParameter(strID_p);
        return theNote;
    }

    /**
     * @see com.wewebu.ow.server.app.OwFieldManagerControl#updateField(javax.servlet.http.HttpServletRequest, com.wewebu.ow.server.field.OwFieldDefinition, java.lang.Object, java.lang.String)
     */
    public Object updateField(HttpServletRequest request_p, OwFieldDefinition fieldDef_p, Object value_p, String strID_p) throws Exception
    {
        if (isVolatileNote())
        {
            return updateVolatileField(request_p, fieldDef_p, value_p, strID_p);
        }
        else
        {
            return updatePersistedField(request_p, fieldDef_p, value_p, strID_p);
        }
    }
}
