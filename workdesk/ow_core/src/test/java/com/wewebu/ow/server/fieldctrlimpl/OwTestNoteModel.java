package com.wewebu.ow.server.fieldctrlimpl;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.wewebu.ow.server.fieldctrlimpl.OwFieldManagerControlNote.OwNote;
import com.wewebu.ow.server.fieldctrlimpl.OwFieldManagerControlNote.OwNoteDataModel;
import com.wewebu.ow.server.fieldctrlimpl.OwFieldManagerControlNote.OwNoteException;
import com.wewebu.ow.server.util.OwHTMLHelper;

public class OwTestNoteModel extends TestCase
{
    /** delimiter for note regexp */
    public static final String NOTE_DELIMITER_REGEXP = "\\n +";

    private static String OLD_NOTE = "Administrator@WEWEBU-VIRTUAL.LOCAL, 09/24/2009 19.11:\n" + "bubu\n" + "\n" + "Administrator@WEWEBU-VIRTUAL.LOCAL, 10/01/2009 15.10:" + "\ntest note\n\n"
            + "Administrator@WEWEBU-VIRTUAL.LOCAL, 10/01/2009 15.31:\n" + "the brand new note\n" + "with multiple lines\n" + "\ntest";

    private static String NEW_NOTE = "user1\n" + "01.01.2009\n" + "This is a note\n " + "Administrator@WEWEBU-VIRTUAL.LOCAL\n" + "09/24/2009 19.11\n" + "bubu\n " + "Administrator@WEWEBU-VIRTUAL.LOCAL\n" + "10/01/2009 15.10\n" + "test note\n "
            + "Administrator@WEWEBU-VIRTUAL.LOCAL\n" + "10/01/2009 15.31\n" + "the brand new note\n" + "with multiple lines\n" + "test\n ";
    private static String OLD_NOTE_SINGLE = "Administrator@WEWEBU-VIRTUAL.LOCAL, 10/06/2009:\ntest";

    private static String SPACE_NOTE = "user1\n" + "01.01.2009\n" + " \n" + " user2\n" + "01.01.2010\n" + "content\n";
    private static String SPACE_NOTE1 = "admin\n04/07/2014 13.37\n\n\n \n\n\n admin\n04/07/2014 13.46\nuuuuuuu\n";

    private OwNoteDataModel noteDataModel;
    private OwNoteDataModel noteDataModelForOldNote;
    private OwNoteDataModel noteDataModelForNewNote;

    protected void setUp() throws Exception
    {
        noteDataModel = new OwNoteDataModel(Locale.ENGLISH, null, true);
        noteDataModelForOldNote = new OwNoteDataModel(Locale.ENGLISH, OLD_NOTE, true);
        noteDataModelForNewNote = new OwNoteDataModel(Locale.ENGLISH, NEW_NOTE, true);
    }

    public void testisOldType() throws Exception
    {
        Pattern p = Pattern.compile("[A-Z0-9._%-]+@[A-Z0-9._%-]+\\.[A-Z0-9._%-]+\\, [0-9._%-]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(OLD_NOTE_SINGLE);
        assertTrue(m.find());
    }

    public void testSplit() throws Exception
    {
        String rawNotes[];
        List m_notes = new LinkedList();

        String m_noteRawContent = SPACE_NOTE1;
        m_noteRawContent = m_noteRawContent.replaceAll("[\\n\\n\\n]+", "\n");
        rawNotes = m_noteRawContent.split(NOTE_DELIMITER_REGEXP);
        OwNote note = null;

        for (int i = 0; i < rawNotes.length; i++)
        {

            String rtNote = rawNotes[i];

            if (!rtNote.isEmpty())
            {
                note = OwNote.buildNote(Locale.ENGLISH, rawNotes[i]);
                noteDataModel.appendNote(note);
                m_notes.add(note);
            }
        }

        System.out.println("=============================");
        System.out.println("NOTE:\n " + noteDataModel.getNotes());

    }

    public void testAppendNote() throws Exception
    {
        OwNote note_p = new OwNote(Locale.ENGLISH, "user1", "01.01.2009", "\r\nThis is a note line1\n \n\n\n\nThis is a note line 2\n  This is a note line 3\n ");
        noteDataModel.appendNote(note_p);
        assertEquals(1, noteDataModel.getNotes().size());
        int firstIndex = noteDataModel.getNote(0).getFormatedNote().indexOf("\n ");
        int lastIndex = noteDataModel.getNote(0).getFormatedNote().lastIndexOf("\n ");
        assertEquals(firstIndex, lastIndex);
        System.out.println("NOTE: " + noteDataModel);
    }

    public void testAppendBigNote() throws Exception
    {
        noteDataModel.setMaxSize(200);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        for (int i = 0; i < 10; i++)
        {
            OwNote note = new OwNote(Locale.ENGLISH, "user1", dateFormat.format(new Date()), "This is the note number " + (i + 1) + ".");
            noteDataModel.appendNote(note);
        }

        assertEquals(3, noteDataModel.getNotes().size());
        System.out.println("NOTE: " + noteDataModel);
        System.out.println("NOTE SIZE: " + noteDataModel.getSize());
        System.out.println("Last note SIZE: " + noteDataModel.getNote(2).getSize());

        //append a huge note - should fail, and the note content should stay unchanged.
        String lastText = noteDataModel.getTrimmedText();
        StringBuffer buffer = new StringBuffer(lastText);
        buffer.append(lastText);
        try
        {
            noteDataModel.appendNote(new OwNote(Locale.ENGLISH, "user1", "huhu", buffer.toString()));
            fail("Should not be here!");
        }
        catch (OwNoteException e)
        {
            //normal behavior - note content should be unchanged
            assertEquals(lastText, noteDataModel.getTrimmedText());
        }
    }

    public void testGetNotes()
    {
        assertEquals(3, noteDataModelForOldNote.getNotes().size());
        System.out.println(noteDataModelForOldNote);
    }

    public void testConversionFromOld2NewNote() throws Exception
    {
        String rawText = noteDataModelForOldNote.getTrimmedText();
        OwNoteDataModel noteDataModel = new OwNoteDataModel(Locale.ENGLISH, rawText, true);
        assertEquals(3, noteDataModel.getNumberOfNotes());
        String rawText2 = noteDataModel.getTrimmedText();
        assertEquals(rawText, rawText2);
    }

    public void testNumberOfNotes()
    {
        assertEquals(4, noteDataModelForNewNote.getNumberOfNotes());
        System.out.println("==========================================");
        System.out.println(noteDataModelForNewNote.getTrimmedText());
    }

    public void testNoteByIndex()
    {
        OwNote note = noteDataModelForNewNote.getNote(2);
        assertNotNull(note);
        assertEquals(1, note.getContentLines().length);
        for (int i = 0; i < note.getContentLines().length; i++)
        {
            System.out.print("line: ");
            System.out.println(note.getContentLines()[i]);
        }
        System.out.println("==========================================");
        System.out.println(note);
    }

    public void testSingleOldNote() throws OwNoteException
    {
        OwNoteDataModel dataModel = new OwNoteDataModel(Locale.ENGLISH, OLD_NOTE_SINGLE, true);
        assertEquals(1, dataModel.getNumberOfNotes());
        OwNote note = dataModel.getNote(0);
        assertEquals("Administrator@WEWEBU-VIRTUAL.LOCAL\n10/06/2009\n", note.getHeader());
        System.out.println(note);
    }

    public void testOldNoteFromSingleString() throws OwNoteException
    {
        OwNoteDataModel dataModel = new OwNoteDataModel(Locale.ENGLISH, "aaa\n\n", true);
        assertEquals(1, dataModel.getNumberOfNotes());
        System.out.println(dataModel.getNote(0));
        dataModel = new OwNoteDataModel(Locale.ENGLISH, "  \n\naaa\n\n  ", true);
        assertEquals(1, dataModel.getNumberOfNotes());
        System.out.println(dataModel.getNote(0));
        dataModel = new OwNoteDataModel(Locale.ENGLISH, "\n\naaa", true);
        assertEquals(1, dataModel.getNumberOfNotes());
        System.out.println(dataModel.getNote(0));
        dataModel = new OwNoteDataModel(Locale.ENGLISH, "aaa", true);
        assertEquals(1, dataModel.getNumberOfNotes());
        System.out.println(dataModel.getNote(0));
        dataModel = new OwNoteDataModel(Locale.ENGLISH, "aaa", true);
        assertEquals(1, dataModel.getNumberOfNotes());
    }

    public void testOldNoteFromSingleStringTricky() throws OwNoteException
    {
        OwNoteDataModel dataModel = new OwNoteDataModel(Locale.ENGLISH, "administrator@wewebu.local, 10/10/2009:\n\n", true);
        assertEquals(1, dataModel.getNumberOfNotes());
        System.out.println(dataModel.getNote(0));
        dataModel = new OwNoteDataModel(Locale.ENGLISH, "administrator@wewebu.local, 10/10/2009:\nnote1\n\n", true);
        assertEquals(1, dataModel.getNumberOfNotes());
        System.out.println(dataModel.getNote(0));

    }

    public void testReverseNotes() throws OwNoteException
    {
        int numberOfNotes = noteDataModelForNewNote.getNumberOfNotes();
        OwNoteDataModel dataModel = new OwNoteDataModel(Locale.ENGLISH, noteDataModelForNewNote.getNoteRawContent(), false);
        for (int i = 0; i < numberOfNotes; i++)
        {
            OwNote note1 = noteDataModelForNewNote.getNote(i);
            OwNote note2 = dataModel.getNote(numberOfNotes - (i + 1));
            assertEquals(note1, note2);
        }
    }

    public void testNoteEncoded() throws Exception
    {
        OwNoteDataModel model = new OwNoteDataModel(Locale.ENGLISH, "Administrator@WEWEBU-VIRTUAL.LOCAL\n" + "12/02/2009 10.59\n" + "short note 1\n ");
        StringWriter w_p = new StringWriter();
        OwHTMLHelper.writeSecureHTML(w_p, model.getTrimmedText());
        String writtenText = w_p.toString();
        assertEquals(model.getTrimmedText(), writtenText);

    }

}
