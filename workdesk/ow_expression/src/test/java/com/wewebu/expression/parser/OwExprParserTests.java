package com.wewebu.expression.parser;

import java.util.Calendar;

import com.wewebu.expression.OwExprTestBase;
import com.wewebu.expression.language.OwExprBooleanValue;
import com.wewebu.expression.language.OwExprDateValue;
import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprExpressionType;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprNullValue;
import com.wewebu.expression.language.OwExprNumericValue;
import com.wewebu.expression.language.OwExprReflectiveScope;
import com.wewebu.expression.language.OwExprStringValue;
import com.wewebu.expression.language.OwExprSymbol;
import com.wewebu.expression.language.OwExprSystem;
import com.wewebu.expression.language.OwExprTime;
import com.wewebu.expression.language.OwExprTimeValue;
import com.wewebu.expression.language.OwExprType;
import com.wewebu.expression.testmodel.OwExprAddress;
import com.wewebu.expression.testmodel.OwExprPerson;
import com.wewebu.expression.testmodel.OwExprPersonService;
import com.wewebu.expression.testmodel.OwExprTask;
import com.wewebu.expression.testmodel.OwExprTaskScope;

/**
*<p>
* OwExprParserTests. 
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
public class OwExprParserTests extends OwExprTestBase
{

    private OwExprPerson m_alenRumburak;
    private OwExprPerson m_johnDoe;

    private OwExprTask m_alensTask1;
    private OwExprTask m_alensTask2;

    private OwExprTask m_johnsTask1;

    private OwExprSystem m_testSystem;

    protected void setUp() throws Exception
    {
        m_testSystem = new OwExprSystem();
        Calendar c17101979 = m_testSystem.date(1979, 10, 17);
        OwExprAddress address1 = new OwExprAddress("221231", "1stStreet", 121, "SomeCity");
        OwExprAddress address2 = new OwExprAddress("881221", "2nd", 8, "TheCity");

        m_alenRumburak = new OwExprPerson("Alen", "Rumburak", new OwExprAddress[] { address1, address2 }, c17101979.getTime(), new String[][] { { "+40256074200012", "+4025607100012" }, { "+41256074200012", "+4125607100012" } });

        m_johnDoe = new OwExprPerson("John", "Doe", new OwExprAddress[] {}, c17101979.getTime(), new String[][] {});

        m_alensTask1 = new OwExprTask();
        m_alensTask1.setProperty("person", m_alenRumburak);
        m_alensTask1.setProperty("startTime", m_testSystem.date(2008, 8, 10));
        m_alensTask1.setProperty("closed", Boolean.FALSE);
        m_alensTask1.setProperty("dueDate", null);

        m_alensTask2 = new OwExprTask();
        m_alensTask2.setProperty("person", m_alenRumburak);
        m_alensTask2.setProperty("startTime", m_testSystem.date(2008, 8, 22));
        m_alensTask2.setProperty("deadLineInDays", new Integer(100));
        m_alensTask2.setProperty("closed", Boolean.FALSE);
        m_alensTask2.setProperty("prerequisite", m_alensTask1);
        m_alensTask2.setProperty("domains", new Integer[] { new Integer(331), new Integer(231), new Integer(33), new Integer(2), new Integer(78) });
        m_alensTask2.setProperty("notes", new String[] { "note1", "note2" });
        m_alensTask2.setProperty("dueDate",  m_testSystem.date(2008, 10, 23));

        m_johnsTask1 = new OwExprTask();
        m_johnsTask1.setProperty("length", new Double(2.6));
        m_johnsTask1.setProperty("length", new Double(2.6));
        
        

    }

    public void testScratchExpression() throws Exception
    {

        System.out.println(value("(today()-date(2009,01,22)).inDays>99"));
    }

    public void testNumericLiterals() throws Exception
    {
        assertEquals(new OwExprNumericValue(0.223), value("0.223"));
        assertEquals(new OwExprNumericValue(0.223E+3), value("0.223E+3"));
        assertEquals(new OwExprNumericValue(.121E+34), value(".121E+34"));
        assertEquals(new OwExprNumericValue(000.121E+34), value("000.121E+34"));
        assertEquals(new OwExprNumericValue(020.E+34), value("020.E+34"));
    }

    public void testStringLiterals() throws Exception
    {
        assertEquals(new OwExprStringValue("ü+öä.-+awq+ü309Ü*ÄÖ ß"), value("'ü+öä.-+awq+ü309Ü*ÄÖ ß'"));
        assertEquals(OwExprBooleanValue.FALSE, value("'abc'.like('efg')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'ab'.like('AB')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'bz'.like('[a-c]z')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'zz'.like('[axzt]z')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'d3z'.like('[a-e]_z')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'D3z'.like('[a-e]_z')"));
        assertEquals(OwExprBooleanValue.FALSE, value("'D3zbla'.like('[a-e]_z')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'D3zbla'.like('[a-e]_z%')"));
        assertEquals(OwExprBooleanValue.FALSE, value("'D3zbla'.like('[a-e]_z!%')"));
        assertEquals(OwExprBooleanValue.TRUE, value("not 'D3zbla'.like('[a-e]_z!%')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'D3z%'.like('[a-e]_z!%')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'mowbobmow'.like('%bob%')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'[]'.like('![!]')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'[]%_!'.like('![!]!%!_!!')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'[](){}.*+?$^|#\\\\'.like('![!](){}.*+?$^|#\\\\')"));
    }

    public void testDateExpression() throws Exception
    {

        assertEquals(new OwExprDateValue(m_testSystem.date(2003, 10, 17, 7, 15, 22, "CET")), value("date('2003-10-17T07:15:22+01:00')"));
        assertEquals(new OwExprDateValue(m_testSystem.date(2003, 10, 17, 7, 15, 22, "EST")), value("date('2003-10-17T07:15:22+05:00')"));
        assertEquals(new OwExprDateValue(m_testSystem.date(2003, 10, 17, 7, 15, 22, "CET")), value("date('2003-10-17T07:15:22EET')"));
        assertEquals(new OwExprDateValue(m_testSystem.date(2003, 10, 17, 7, 15, 22, "PST")), value("date('2003-10-17T07:15:22-0800')"));

        assertEquals(new OwExprNumericValue(7), value("date('2003-10-17T07:15:22+02:00').hour"));
        assertEquals(new OwExprNumericValue(7), value("date('2003-10-17T07:15:22CET').hour"));
        assertEquals(new OwExprNumericValue(22), value("date('2003-10-17T07:15:22+02:00').second"));
        assertEquals(new OwExprNumericValue(15), value("date('2003-10-17T07:15:22+02:00').minute"));
        assertEquals(new OwExprNumericValue(17), value("date('2003-10-17T07:15:22+02:00').day"));
        assertEquals(new OwExprNumericValue(10), value("date('2003-10-17T07:15:22+02:00').month"));
        assertEquals(new OwExprNumericValue(2003), value("date('2003-10-17T07:15:22+02:00').year"));

        assertEquals(new OwExprStringValue("2003-10-17T07:15:22-0800"), value("''+date('2003-10-17T07:15:22-0800')"));
        assertEquals(new OwExprStringValue("2003-10-17T07:15:22+0100"), value("''+date('2003-10-17T07:15:22+0100')"));
        assertEquals(new OwExprStringValue("2003-10-17T07:15:22+0200"), value("''+date('2003-10-17T07:15:22+0200')"));
        assertEquals(new OwExprStringValue("2003-10-17T07:15:22+0000"), value("''+date('2003-10-17T07:15:22GMT')"));

        assertEquals(new OwExprTimeValue(OwExprTime.timeInDays(0.0)), value("date('2003-10-17T07:15:22+01:00')-date('2003-10-17T07:15:22+02:00')"));
        assertEquals("0 days expected! ", new OwExprNumericValue(0), value("(date('2003-10-17T07:15:22+01:00')-date('2003-10-17T07:15:22+02:00')).inDays"));
        assertEquals("1 hour (1/24 days) expected! ", new OwExprNumericValue(1.0 / 24), value("(date('2003-10-17T07:15:22+01:00')-date('2003-10-17T07:15:22+02:00')).inUTCDays"));

        assertEquals("1 month expected! ", new OwExprNumericValue(1), value("(date('2003-11-19T08:15:22+01:00')-date('2003-10-17T07:15:22+02:00')).months"));
        assertEquals("2 days expected! ", new OwExprNumericValue(2), value("(date('2003-11-19T08:15:22+01:00')-date('2003-10-17T07:15:22+0200')).days"));
        assertEquals("1 hour expected! ", new OwExprNumericValue(1), value("(date('2003-11-19T08:15:22+01:00')-date('2003-10-17T07:15:22+02:00')).hours"));

        assertInvalidValue("date('2003-10-17T07:15:2202:00')");
        assertInvalidValue("date('2003-10-17T07:15:22-02:0')");
        assertInvalidValue("date('2003-10-17T07:15:22-0:20')");
        assertInvalidValue("date('2003-10-17T07:15:22-0:0')");
        assertInvalidValue("date('2003-10-17T07:15:220200')");
        assertInvalidValue("date('2003-10-17T07:15:22-200')");
        assertInvalidValue("date('2003-10-17T07:15:22-2')");
        assertInvalidValue("date('2003-10-17T07:15:22')");
        assertInvalidValue("date('2003-10-17T07:15:22ZZZZZ')");
        assertInvalidValue("date('2003-10-17T07:15:22ZZZZ')");
    }

    public void testErrors() throws Exception
    {
        OwExprExpression e;

        e = parse("");
        assertTrue(e.hasErrors());
        assertEquals(1, e.errCount());

        e = parse("a= and b+2=3");
        assertTrue(e.hasErrors());
        assertEquals(1, e.errCount());

        e = parse("a=09'");
        assertTrue(e.hasErrors());
        assertEquals(1, e.errCount());

        e = parse("(true?1:a-b)  and  a");
        assertTrue(e.hasErrors());
        assertEquals(1, e.errCount());

        e = parse(" not a=b  and  a*3=1");
        assertTrue(e.hasErrors());
        assertEquals(3, e.errCount());

        e = parse("object['2']");
        assertTrue(e.hasErrors());
        assertEquals(1, e.errCount());

        e = parse("object[ not a]['3']");
        assertTrue(e.hasErrors());
        assertEquals(2, e.errCount());

        e = parse("(object[a+1]['3'])");
        assertTrue(e.hasErrors());
        assertEquals(1, e.errCount());

        e = parse("(object.a+object.a(2,3)+object.a)");
        assertTrue(e.hasErrors());
        assertEquals(3, e.errCount());

        e = parse("(a or b)  and  object.array[a]=1");
        assertTrue(e.hasErrors());
        assertEquals(3, e.errCount());

    }

    public void testArrays() throws Exception
    {
        assertEquals(OwExprBooleanValue.TRUE, value("{}={}"));
        assertEquals(OwExprBooleanValue.TRUE, value("{3,1}={3,1}"));
        assertEquals(OwExprBooleanValue.FALSE, value("{1,3}={3,1}"));
        assertEquals(OwExprBooleanValue.TRUE, value("task.domains={331,231, 33, 2, 78 }", new OwExprTaskScope("task", m_alensTask2)));
        assertEquals(OwExprBooleanValue.TRUE, value("{}.length=0"));
        assertEquals(OwExprBooleanValue.TRUE, value("{'foo','bar','doo'}[2]='doo'"));
        assertEquals(OwExprBooleanValue.TRUE, value("contains({'foo','bar','doo'},'bar')"));
        assertEquals(new OwExprNumericValue(3), value("{date(17,10,1979),date(1,10,1979),date(17,10,1985)}.length"));
        assertEquals(OwExprBooleanValue.TRUE, value("contains({date(17,10,1979),date(1,10,1979),date(17,10,1985)},date(17,10,1985))"));
        assertEquals(OwExprBooleanValue.FALSE, value("contains({date(17,10,1979),date(1,10,1979),date(17,10,1985)},date(17,10,2010))"));
        assertEquals(OwExprBooleanValue.TRUE, value("contains({331,{2,3},332},{2,3})"));
        assertEquals(OwExprBooleanValue.TRUE, value("contains(task.domains,331)", new OwExprTaskScope("task", m_alensTask2)));
        assertEquals(OwExprBooleanValue.FALSE, value("contains(task.domains,'331')", new OwExprTaskScope("task", m_alensTask2)));
    }

    public void testTables() throws Exception
    {
        OwExprExpression e;

        //Good
        e = parse("1.2>2*1.2  or  true  or  false  or   not true  or  '33'.length>='33'.length");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());

        e = parse("1-object.p");
        OwExprSymbol objectP = e.getSymbol("object").getSymbol("p");
        assertEquals(new OwExprExpressionType(new OwExprType[] { OwExprType.NUMERIC }), objectP.getType());

        e = parse("m+n>a  and  m*0.7>500");
        OwExprSymbol a = e.getSymbol("a");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        assertEquals(new OwExprExpressionType(new OwExprType[] { OwExprType.NUMERIC }), a.getType());

        e = parse("(object.x=object.y)?object.x:'str'");
        OwExprSymbol objectY = e.getSymbol("object").getSymbol("y");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        assertEquals(new OwExprExpressionType(new OwExprType[] { OwExprType.STRING, OwExprType.NUMERIC, OwExprType.DATE, OwExprType.TIME, OwExprType.BOOLEAN, OwExprType.NULL }), objectY.getType());

        e = parse("(object.x=object.y2)?object.x:2.5");
        OwExprSymbol objectY2 = e.getSymbol("object").getSymbol("y2");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        assertEquals(new OwExprExpressionType(new OwExprType[] { OwExprType.NUMERIC, OwExprType.STRING, OwExprType.NULL }), objectY2.getType());

        e = parse("object.x?1:a-b");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol object = e.getSymbol("object");
        OwExprSymbol objectX = object.getSymbol("x");
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), object.getType());
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), objectX.getType());

        e = parse("today()");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());

        e = parse("object.phoneNumbers[0][1]");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());

        e = parse("object.phoneNumbers[0][1]-object.phoneNumbers[0].length");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());

        e = parse("object2.x?1:a-b*object2");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        //Bad
        e = parse("(true?1:a-b)  and  a");
        assertTrue(e.hasErrors());

        e = parse("((object.x>object.y3)?object.x:'str')='str2'");
        assertTrue(e.getErrorTable().toString(), e.hasErrors());

        e = parse(" not a=b  and  a*3=1");
        assertTrue(e.getErrorTable().toString(), e.hasErrors());

        e = parse("object.foo()  and  object.foo=2");
        assertTrue(e.getErrorTable().toString(), e.hasErrors());

        e = parse("object.phoneNumbers[0][1]-object.phoneNumbers[0]");
        OwExprSymbol objectPhoneNumbersA = e.getSymbol("object").getSymbol("phoneNumbers").getSymbol("0$A");
        OwExprSymbol objectPhoneNumbersAA = e.getSymbol("object").getSymbol("phoneNumbers").getSymbol("0$A").getSymbol("0$A");
        assertEquals(new OwExprExpressionType(new OwExprType[] { OwExprType.SCOPE }), objectPhoneNumbersA.getType());
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), objectPhoneNumbersAA.getType());
        assertTrue(e.getErrorTable().toString(), e.hasErrors());
    }

    public void testSymbolsVisibility() throws ParseException, OwExprEvaluationException
    {
        OwExprExpression e;

        e = parse("m+n>a  and  m*0.7>500");
        assertFalse(e.symbolsVisibleInScopes(new OwExprExternalScope[] {}));

        e = parse("task.startTime<today()");
        assertTrue(e.symbolsVisibleInScopes(new OwExprExternalScope[] { new OwExprTaskScope("task", m_alensTask2) }));

        e = parse("task.startTime.minute>30");
        assertTrue(e.symbolsVisibleInScopes(new OwExprExternalScope[] { new OwExprTaskScope("task", m_alensTask2) }));

        e = parse("task.startTime.minute>task[2].unu");
        assertTrue(e.symbolsVisibleInScopes(new OwExprExternalScope[] { new OwExprTaskScope("task", m_alensTask2) }));

        e = parse("task.notes[0].length");
        assertTrue(e.symbolsVisibleInScopes(new OwExprExternalScope[] { new OwExprTaskScope("task", m_alensTask2) }));

        e = parse("(date(2009,3,2)-object.birthDate).years");
        assertTrue(e.symbolsVisibleInScopes(new OwExprExternalScope[] { new OwExprReflectiveScope("object", m_alenRumburak) }));

        e = parse("object.phoneNumbers.length");
        assertTrue(e.symbolsVisibleInScopes(new OwExprExternalScope[] { new OwExprReflectiveScope("object", m_alenRumburak) }));

    }

    public void testLegacyEscapedIdentifiers() throws Exception
    {
        OwExprExpression e;

        e = parse("employee$Sname");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employee$NameSymbol = e.getSymbol("employee$name");
        assertNotNull(employee$NameSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employee$NameSymbol.getType());

        e = parse("employee$Mname");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeMinusNameSymbol = e.getSymbol("employee-name");
        assertNotNull(employeeMinusNameSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeMinusNameSymbol.getType());

        e = parse("employee$Cname");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeColonNameSymbol = e.getSymbol("employee:name");
        assertNotNull(employeeColonNameSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeColonNameSymbol.getType());

        e = parse("employee$SSname");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeDolarColonNameSymbol = e.getSymbol("employee$Sname");
        assertNotNull(employeeDolarColonNameSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeDolarColonNameSymbol.getType());

        e = parse("owd$CEmployee$Dname");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeCollonEmployeeDotNameSymbol = e.getSymbol("owd:Employee.name");
        assertNotNull(employeeCollonEmployeeDotNameSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeCollonEmployeeDotNameSymbol.getType());
    }

    public void testEscapedIdentifiers() throws Exception
    {
        OwExprExpression e;

        e = parse("\\.employee=prop");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol DOTemployeeSymbol = e.getSymbol(".employee");
        assertNotNull(DOTemployeeSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), DOTemployeeSymbol.getType());

        e = parse("\\-employee");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol MINUSemployeeSymbol = e.getSymbol("-employee");
        assertNotNull(MINUSemployeeSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), MINUSemployeeSymbol.getType());

        e = parse("\\:employee");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol COLONemployeeSymbol = e.getSymbol(":employee");
        assertNotNull(COLONemployeeSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), COLONemployeeSymbol.getType());

        e = parse("employee\\:name");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeCOLONSymbol = e.getSymbol("employee:name");
        assertNotNull(employeeCOLONSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeCOLONSymbol.getType());

        e = parse("employee\\-name");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeMINUSSymbol = e.getSymbol("employee-name");
        assertNotNull(employeeMINUSSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeMINUSSymbol.getType());

        e = parse("employee\\.name");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeDOTSymbol = e.getSymbol("employee.name");
        assertNotNull(employeeDOTSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeDOTSymbol.getType());

        e = parse("employee\\.name$Dfull");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeDOTLegacySymbol = e.getSymbol("employee.name.full");
        assertNotNull(employeeDOTLegacySymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeDOTLegacySymbol.getType());

        e = parse("employee\\\\address");
        assertFalse(e.getErrorTable().toString(), e.hasErrors());
        OwExprSymbol employeeBSSymbol = e.getSymbol("employee\\address");
        assertNotNull(employeeBSSymbol);
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), employeeBSSymbol.getType());
    }

    public void testExpressionValue() throws Exception
    {
        //Numeric

        assertEquals(new OwExprNumericValue(32232.33), value("32232.33"));
        assertEquals(new OwExprNumericValue(3), value("1+2"));

        assertEquals(new OwExprNumericValue(132.330 - 2.10), value("132.330-2.10"));
        assertEquals(new OwExprNumericValue(-(132.330 - 2.10)), value("-(132.330-2.10)"));
        assertEquals(new OwExprNumericValue(-12 + (+(132.330 - 2.10))), value("-12+(+(132.330-2.10))"));
        assertEquals(new OwExprNumericValue(132.330 * 2.10), value("132.330*2.10"));
        assertEquals(new OwExprNumericValue(132.330 / 2), value("132.330/2"));
        assertEquals(new OwExprNumericValue(132.330 % 4), value("132.330%4"));

        assertEquals(new OwExprNumericValue((1 + 2 - 3 * (132.330) % 4) / (3.14 * 22)), value("(1+2-3*(132.330)%4)/(3.14*22)"));

        assertEquals(new OwExprNumericValue(3), value("'123'.length"));

        assertEquals(new OwExprNumericValue(0.5), value("1/2"));

        assertEquals(new OwExprNumericValue(10729), value("(date(2009,3,2)-object.birthDate).inDays", "object", m_alenRumburak));
        assertEquals(new OwExprNumericValue(17), value("object.birthDate.day", "object", m_alenRumburak));

        assertEquals(new OwExprNumericValue(29), value("(date(2009,3,2)-object.birthDate).years", "object", m_alenRumburak));

        Calendar currentCalendar = Calendar.getInstance();
        assertEquals(new OwExprNumericValue(currentCalendar.get(Calendar.YEAR)), value("today().year"));

        assertEquals(new OwExprStringValue(m_alenRumburak.getPhoneNumbers()[0][0]), value("object.phoneNumbers[0][0]", "object", m_alenRumburak));

        assertEquals(new OwExprNumericValue(2), value("object.phoneNumbers.length", "object", m_alenRumburak));

        assertEquals(new OwExprNumericValue(3), value("version().length"));
        assertEquals(new OwExprNumericValue(1), value("version()[0]"));
        assertEquals(new OwExprNumericValue(3), value("version()[1]"));
        assertEquals(new OwExprNumericValue(0), value("version()[2]"));

        assertEquals(new OwExprNumericValue(2), value("task.notes.length", new OwExprTaskScope("task", m_alensTask2)));
        assertEquals(new OwExprNumericValue(5), value("task.notes[0].length", new OwExprTaskScope("task", m_alensTask2)));

        assertEquals(new OwExprNumericValue(2.6), value("task.length", new OwExprTaskScope("task", m_johnsTask1)));

        assertEquals(new OwExprNumericValue(0), value("(date(8,3,2009,11,48,1,'EET')-date(8,3,2009,11,48,1,'CET')).inDays"));
        assertEquals(new OwExprNumericValue(1.0 / 24.0), value("(date(8,3,2009,11,48,1,'CET')-date(8,3,2009,11,48,1,'EET')).inUTCDays"));

        //String

        assertEquals(new OwExprStringValue("str"), value("'str'"));
        assertEquals(new OwExprStringValue("12"), value("1+'2'"));
        assertEquals(new OwExprStringValue("12"), value("'1'+2"));
        assertEquals(new OwExprStringValue("222990"), value("'222'+990"));

        assertEquals(new OwExprStringValue("s1s2"), value("'s1'+'s2'"));

        assertEquals(new OwExprStringValue(OwExprSystem.VERSION_STRING), value("versionString"));

        assertEquals(new OwExprStringValue("noaddress"), value("null=object.mainAddress?'noaddress':object.mainAddress.city", "object", m_johnDoe, false));

        assertEquals(new OwExprStringValue(m_alenRumburak.getMainAddress().getCity()), value("null=object.mainAddress?'noaddress':object.mainAddress.city", "object", m_alenRumburak));

        assertEquals(new OwExprStringValue(m_alenRumburak.getAddresses()[0].getCity()), value("object.addresses[0].city", "object", m_alenRumburak));

        assertEquals(new OwExprStringValue(new OwExprPersonService().fullName("fn", "ln")), value("service.fullName('fn','ln')", "service", new OwExprPersonService()));

        assertEquals(new OwExprStringValue("\\'"), value("'\\''"));

        //Boolean

        assertEquals(OwExprBooleanValue.TRUE, value("true"));
        assertEquals(OwExprBooleanValue.FALSE, value("false"));
        assertEquals(OwExprBooleanValue.TRUE, value(" not false"));
        assertEquals(OwExprBooleanValue.FALSE, value(" not true"));
        assertEquals(OwExprBooleanValue.TRUE, value("true  or  false"));
        assertEquals(OwExprBooleanValue.FALSE, value("true  and  false"));
        assertEquals(OwExprBooleanValue.FALSE, value("true  xor  true"));

        assertEquals(OwExprBooleanValue.TRUE, value("1+5=2+4"));
        assertEquals(OwExprBooleanValue.TRUE, value("1+5!=3.14"));
        assertEquals(OwExprBooleanValue.FALSE, value("1+5*10.3<10.2001*5+2-1"));
        assertEquals(OwExprBooleanValue.TRUE, value("1+5*10.3<=10.2001*5+(5*(10.3-10.2001))+2-1"));
        assertEquals(OwExprBooleanValue.TRUE, value("7*8.3>0.56"));

        assertEquals(OwExprBooleanValue.TRUE, value("7*8.3>=0.56"));

        assertEquals(OwExprBooleanValue.TRUE, value("7*8.3='" + (7 * 8.3) + "'"));
        assertEquals(OwExprBooleanValue.TRUE, value("'" + (7 * 8.3) + "'=7*8.3"));

        assertEquals(OwExprBooleanValue.FALSE, value("1+2>3  and  5*0.7>500"));

        assertEquals(OwExprBooleanValue.TRUE, value("date(1979,10,17).year=1979  and  date(1979,10,17).day=17  and  date(1979,10,17).month=10"));

        assertEquals(OwExprBooleanValue.TRUE, value("versionString.length=" + OwExprSystem.VERSION_STRING.length()));

        assertEquals(OwExprBooleanValue.TRUE, value("(today()-(today()-days(2))).days=2"));

        assertEquals(OwExprBooleanValue.FALSE, value("object.mainAddress=null", "object", m_alenRumburak));

        assertEquals(OwExprBooleanValue.TRUE, value("null!=object.mainAddress", "object", m_alenRumburak));

        assertEquals(OwExprBooleanValue.FALSE, value("object.phoneNumbers[0][0]=object.phoneNumbers[0][1]", "object", m_alenRumburak));

        assertEquals(OwExprBooleanValue.TRUE, value("date(17,10,1979)>date(16,10,1979)"));
        assertEquals(OwExprBooleanValue.FALSE, value(" not (date(17,10,1979)>date(16,10,1979))"));

        assertEquals(OwExprBooleanValue.FALSE, value("date(1979,10,17)>date(1979,10,16)+days(2)"));
        assertEquals(OwExprBooleanValue.TRUE, value("date(1979,10,17)<=(date(1979,10,17)+days(2))-days(2)"));
        assertEquals(OwExprBooleanValue.TRUE, value("date(1979,10,17)<=date(1979,10,17)+(days(2)-days(2))"));
        assertEquals(OwExprBooleanValue.TRUE, value("days(1.0+((3.0+(15.0+32.0/60.0)/60.0)/24.0)).seconds=32"));

        assertEquals(OwExprBooleanValue.FALSE, value("(object.birthDate+days(20)).month=10", "object", m_alenRumburak));
        assertEquals(OwExprBooleanValue.TRUE, value("(object.birthDate+days(50)).month=12", "object", m_alenRumburak, false));

        assertEquals(OwExprBooleanValue.TRUE, value(" not task.closed", new OwExprTaskScope("task", m_alensTask2)));

        assertEquals(OwExprBooleanValue.FALSE, value("task.prerequisite!=null?task.prerequisite.closed:true", new OwExprTaskScope("task", m_alensTask2)));
        assertEquals(OwExprBooleanValue.TRUE, value("task.prereqisite!=null?task.prereqisite.closed:true", new OwExprTaskScope("task", m_alensTask1), false));
        assertEquals(OwExprBooleanValue.TRUE, value("task.startTime<today()", new OwExprTaskScope("task", m_alensTask2)));
        assertEquals(OwExprBooleanValue.TRUE, value("today()-(task.startTime+days(task.deadLineInDays))>days(0)", new OwExprTaskScope("task", m_alensTask2)));

        assertEquals(OwExprBooleanValue.TRUE, value("task.notes[0]='note1'", new OwExprTaskScope("task", m_alensTask2)));

        assertEquals(OwExprBooleanValue.TRUE, value("task.notes=null", new OwExprTaskScope("task", m_alensTask1), false));

        //Date

        //Time
        assertEquals(new OwExprTimeValue(new OwExprTime(0, 0, 50, 50, 0, 0, 0)), value("days(10)+days(40)"));
        assertEquals(new OwExprTimeValue(new OwExprTime(0, 0, 1, 1.5, 12, 0, 0)), value("days(1.5)"));
        assertEquals(new OwExprTimeValue(new OwExprTime(0, 0, 1, 1.0 + ((3.0 + 15.0 / 60.0) / 24.0), 3, 15, 0)), value("days(1.0+((3.0+15.0/60.0)/24.0))"));

        //Null
        assertEquals(OwExprNullValue.INSTANCE, value("object.mainAddress", "object", m_johnDoe));
        assertEquals(OwExprNullValue.INSTANCE, value("null"));
    }

    public void testExpressionType() throws Exception
    {
        //Numeric

        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("1.23221"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("-1.23221"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("+2"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("0"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("182"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("1+2"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("1-2"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("122.3*2"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("122.3/2"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("122.3%2"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("object.x?1:a-b"));
        assertEquals(new OwExprExpressionType(OwExprType.NUMERIC), type("(object.x*2)>0?object.x:0"));

        //String

        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("'str'"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("'str1'+'str2'"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("'str1'+2.34"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("2.34+'sswq'"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("true+'sswq'"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("false+'sswq'"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("'sswq'+true"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("'sswq'+false"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("true?''+1:'3'"));
        assertEquals(new OwExprExpressionType(OwExprType.STRING), type("object.x?''+1:'3'"));

        //Boolean

        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("true"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("false"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("1=2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("1!=2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("2121>2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("2121<-0.2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("2121<=-0.2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("2121>=-0.2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("2121>=-0.2  and  2<3  or  4=1  xor  1=1"));

        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("object.prop>=-0.2"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type(" not object.prop"));
        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("object.prop-globalProp=-0.2"));

        assertEquals(new OwExprExpressionType(OwExprType.BOOLEAN), type("date(17,10,1979)<=date(17,10,1979)+(days(2)-days(2))"));

        //Inferred

        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), type("object.prop"));
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), type("object.function()"));
        assertEquals(new OwExprExpressionType(OwExprType.ALL_TYPE_TYPES), type("today()"));
    }

    public void testOperatorsTP() throws Exception
    {
        //Binar Plus
        assertEquals(new OwExprNumericValue(5.5), value("4.3+1.2"));
        assertEquals(new OwExprNumericValue(5.2), value("4+1.2"));
        assertEquals(new OwExprNumericValue(5.1), value("4.1+1"));
        assertEquals(new OwExprNumericValue(9), value("8+1"));
        assertEquals(new OwExprNumericValue(5.5), value("4.3+1.2"));

        assertEquals(new OwExprStringValue("s4"), value("'s'+4"));
        assertEquals(new OwExprStringValue("s4.6"), value("'s'+4.6"));
        assertEquals(new OwExprStringValue("sx"), value("'s'+'x'"));
        assertEquals(new OwExprStringValue("5x"), value("5+'x'"));
        assertEquals(new OwExprStringValue("5.7x"), value("5.7+'x'"));
        assertEquals(new OwExprStringValue("Strue"), value("'S'+true"));
        assertEquals(new OwExprStringValue("trueS"), value("true+'S'"));
        assertEquals(new OwExprStringValue("date1982-11-22T00:00:00+0200"), value("'date'+date(1982,11,22,0,0,0,'EET')"));
        assertEquals(new OwExprStringValue("1982-11-22T00:00:00+0200date"), value("date(1982,11,22,0,0,0,'EET')+'date'"));
        assertEquals(new OwExprStringValue("0:0:10(10.3~10.3):7:12:0time"), value("days(10.3)+'time'"));
        assertEquals(new OwExprStringValue("time0:0:10(10.3~10.3):7:12:0"), value("'time'+days(10.3)"));

        assertEquals(new OwExprDateValue(m_testSystem.date(1982, 12, 2, 12, 0, 0)), value("date(1982,11,22)+days(10.5)"));
        assertEquals(new OwExprDateValue(m_testSystem.date(1982, 12, 2, 12, 0, 0)), value("days(10.5)+date(1982,11,22)"));
        assertEquals(new OwExprTimeValue(OwExprTime.timeInDays(13.6)), value("days(3.1)+days(10.5)"));

        //Binar Minus
        assertEquals(new OwExprNumericValue(1021 - 3412.3), value("1021-3412.3"));
        assertEquals(new OwExprTimeValue(OwExprTime.timeInDays(15323.621493055556)), value("date(1954,7,7,13,15,7)-date(1912,7,23,22,20,10)"));
        assertEquals(new OwExprDateValue(m_testSystem.date(1912, 7, 23, 22, 20, 10)), value("date(1954,7,7,13,15,7)-days(15323.621493055556)"));
        assertEquals(new OwExprTimeValue(OwExprTime.timeInDays(5.5)), value("days(7.6)-days(2.1)"));

        //Mul
        assertEquals(new OwExprNumericValue(1021 * 3412.3), value("1021*3412.3"));

        //Div
        assertEquals(new OwExprNumericValue(1021 / 3412.3), value("1021/3412.3"));

        //AND
        assertEquals(OwExprBooleanValue.FALSE, value("true and false"));
        assertEquals(OwExprBooleanValue.TRUE, value("3>2 and 2>1"));
        assertEquals(OwExprBooleanValue.FALSE, value("false and true"));
        assertEquals(OwExprBooleanValue.FALSE, value("false and false"));

        //OR
        assertEquals(OwExprBooleanValue.TRUE, value("true or false"));
        assertEquals(OwExprBooleanValue.TRUE, value("true or true"));
        assertEquals(OwExprBooleanValue.TRUE, value("false or true"));
        assertEquals(OwExprBooleanValue.FALSE, value("false or false"));

        //XOR
        assertEquals(OwExprBooleanValue.TRUE, value("true xor false"));
        assertEquals(OwExprBooleanValue.FALSE, value("true xor true"));
        assertEquals(OwExprBooleanValue.TRUE, value("false xor true"));
        assertEquals(OwExprBooleanValue.FALSE, value("false xor false"));

        //Equal
        assertEquals(OwExprBooleanValue.FALSE, value("1=2.3"));
        assertEquals(OwExprBooleanValue.TRUE, value("'2.342'=2.342"));
        assertEquals(OwExprBooleanValue.TRUE, value("2.342='2.342'"));
        assertEquals(OwExprBooleanValue.FALSE, value("'2.3420'=2.342"));
        assertEquals(OwExprBooleanValue.FALSE, value("2.342='2.3420'"));
        assertEquals(OwExprBooleanValue.TRUE, value("2.342!='2.3420'"));
        assertEquals(OwExprBooleanValue.TRUE, value("days(2.5)='0:0:2(2.5~2.5):12:0:0'"));
        assertEquals(OwExprBooleanValue.FALSE, value("'0:0:2(2.5~2.5):12:0:0'!=days(2.5)"));

        assertEquals(OwExprBooleanValue.TRUE, value("date(1989,10,2,0,0,0,'EET')='1989-10-02T00:00:00+0200'"));
        assertEquals(OwExprBooleanValue.FALSE, value("'1989-10-02T00:00:00+0200'!=date(1989,10,2,0,0,0,'EET')"));
        assertEquals(OwExprBooleanValue.TRUE, value("'true'=true"));
        assertEquals(OwExprBooleanValue.FALSE, value("false!='false'"));
        assertEquals(OwExprBooleanValue.TRUE, value("'str'='str'"));

        assertEquals(OwExprBooleanValue.TRUE, value("true=true"));
        assertEquals(OwExprBooleanValue.FALSE, value("true!=true"));
        assertEquals(OwExprBooleanValue.TRUE, value("false=false"));
        assertEquals(OwExprBooleanValue.FALSE, value("false!=false"));
        assertEquals(OwExprBooleanValue.FALSE, value("false=true"));
        assertEquals(OwExprBooleanValue.FALSE, value("true=false"));

        assertEquals(OwExprBooleanValue.FALSE, value("date(2003,2,1)=date(2007,4,2)"));

        assertEquals(OwExprBooleanValue.TRUE, value("date(1,2,2003)!=date(2,4,2007)"));
        assertEquals(OwExprBooleanValue.TRUE, value("days(2.5)=date(2007,4,4,22,0,0)-date(2007,4,2,10,0,0)"));
        assertEquals(OwExprBooleanValue.FALSE, value("days(2.5)!=date(2007,4,4,22,0,0)-date(2007,4,2,10,0,0)"));

        assertEquals(OwExprBooleanValue.FALSE, value("days(2.5)!=date(2007,4,4,22,0,0)-date(2007,4,2,10,0,0)"));

        assertEquals(OwExprBooleanValue.TRUE, value("days(2.5)!=null"));
        assertEquals(OwExprBooleanValue.TRUE, value("null!=days(2.3)"));
        assertEquals(OwExprBooleanValue.FALSE, value("null!=null"));
        assertEquals(OwExprBooleanValue.FALSE, value("1=null"));
        assertEquals(OwExprBooleanValue.TRUE, value("null!=2"));
        assertEquals(OwExprBooleanValue.TRUE, value("null!=today()"));
        assertEquals(OwExprBooleanValue.FALSE, value("today()=null"));
        assertEquals(OwExprBooleanValue.FALSE, value("true=null"));
        assertEquals(OwExprBooleanValue.TRUE, value("null!=false"));
        assertEquals(OwExprBooleanValue.TRUE, value("null!='null'"));
        assertEquals(OwExprBooleanValue.FALSE, value("'null'=null"));

        //Less & LessOrEq
        assertEquals(OwExprBooleanValue.TRUE, value("1<2"));
        assertEquals(OwExprBooleanValue.TRUE, value("1/2<=4.5/9"));

        assertEquals(OwExprBooleanValue.TRUE, value("date(2009,3,2,10,20,1)<date(2009,3,1,10,20,2)+days(1)"));
        assertEquals(OwExprBooleanValue.TRUE, value("date(2009,3,2,10,20,1)<=date(2009,3,1,10,20,1)+days(1)"));

        //TODO:Time Less

        //Greater & GreaterOrEq
        assertEquals(OwExprBooleanValue.FALSE, value("2.4>7.9"));
        assertEquals(OwExprBooleanValue.TRUE, value("2.4>=-7.9*2.4/-7.9"));

        assertEquals(OwExprBooleanValue.TRUE, value("date(2010,3,2,10,20,1)>date(2009,3,1,10,20,2)+days(300)"));
        assertEquals(OwExprBooleanValue.TRUE, value("date(2009,3,2,10,20,1)>=date(2009,3,1,10,20,1)+days(1)"));

        //Unary Plus
        assertEquals(new OwExprNumericValue(1.2), value("+1.2"));

        //Unary Minus
        assertEquals(new OwExprNumericValue(-1.2), value("-1.2"));

        //Negate
        assertEquals(OwExprBooleanValue.FALSE, value(" not true"));
        assertEquals(OwExprBooleanValue.TRUE, value(" not false"));
    }

    public void testOperatorsFP() throws Exception
    {
        //Plus
        assertInvalidExpression("4.3+(1*null)");
        assertInvalidExpression("null+(4/1.4)");
        assertInvalidExpression("null+'s'");
        assertInvalidExpression("1+true");
        assertInvalidExpression("false+2");
        assertInvalidExpression("false+true");
        assertInvalidExpression("'s'+null");
        assertInvalidExpression("date(1,1,2010)+null");
        assertInvalidExpression("null+date(1,1,2010)");
        assertInvalidExpression("days(1)+null");
        assertInvalidExpression("null+days(1)");
        assertInvalidExpression("null+true");
        assertInvalidExpression("false+null");

        assertInvalidValue("4.3+days(1.2)");
        assertInvalidValue("4.3+date(1,2,2001)");
        assertInvalidValue("days(1.2)+1");
        assertInvalidValue("date(1,2,2001)+5");

        assertInvalidValue("date(1,2,2001)+date(2,4,1998)");

        //Minus
        assertInvalidExpression("'s'-2");
        assertInvalidExpression("2-'str'");
        assertInvalidExpression("'s'-true");
        assertInvalidExpression("false-'str'");
        assertInvalidExpression("version()[1]-'2'");
        assertInvalidExpression("'2'-version()[1]");
        assertInvalidValue("versionString-2");
        assertInvalidValue("3.2-versionString");

        assertInvalidValue("days(2)-date(2,4,1998)");

        assertInvalidExpression("1-true");
        assertInvalidExpression("false-2");
        assertInvalidExpression("false-null");
        assertInvalidExpression("null-false");
        assertInvalidExpression("1-null");
        assertInvalidExpression("null-2");
        assertInvalidExpression("1-(2>0)");
        assertInvalidExpression("1-(days(2)>days(3))");
        assertInvalidExpression("true-days(2)");
        assertInvalidExpression("days(2)-false");

        //Equal

        assertInvalidValue("1=today()");
        assertInvalidValue("1!=today()");
        assertInvalidValue("today()=198912123");
        assertInvalidValue("today()!=198912123");
        assertInvalidValue("days(2)=2121");
        assertInvalidValue("days(2)!=2121");
        assertInvalidValue("1.5=days(1.5)");
        assertInvalidValue("1.5!=days(1.5)");
        assertInvalidExpression("1=true");
        assertInvalidExpression("1!=true");
        assertInvalidExpression("false=2");
        assertInvalidExpression("false!=2");
        assertInvalidExpression("false!=2");
        assertInvalidValue("false=today()");
        assertInvalidValue("false!=today()");
        assertInvalidValue("today()=true");
        assertInvalidValue("days(2)=true");
        assertInvalidValue("days(2)!=true");
        assertInvalidValue("false=days(232232)");
        assertInvalidValue("today()=days(2)");
        assertInvalidValue("today()!=days(2)");
        assertInvalidValue("days(2)=today()");
        assertInvalidValue("days(2)!=today()");

        //Less & LessOrEq
        assertInvalidExpression("null<1");
        assertInvalidExpression("2<=null");
        assertInvalidExpression("version[0]<=null");
        assertInvalidExpression("null<=version[0]");

        assertInvalidExpression("'str'<1");
        assertInvalidExpression("2<='str'");

        assertInvalidExpression("2<=true");
        assertInvalidExpression("false<=2");

        assertInvalidExpression("'str'<false");
        assertInvalidExpression("true<='str'");

        //TODO:

        //Greater & GreaterOrEq

        assertInvalidExpression("null>1");
        assertInvalidExpression("2>=null");
        assertInvalidExpression("version[0]>=null");
        assertInvalidExpression("null>=version[0]");

        assertInvalidExpression("'str'>1");
        assertInvalidExpression("2>='str'");

        assertInvalidExpression("2>=true");
        assertInvalidExpression("false>=2");

        assertInvalidExpression("'str'>false");
        assertInvalidExpression("true>='str'");
        //TODO:

        //Negate
        assertInvalidExpression(" not 1");
        assertInvalidExpression(" not 'str'");
        assertInvalidValue(" not days(2.2)");
        assertInvalidValue(" not date(1,2,2002)");
        assertInvalidValue(" not versionString");
        assertInvalidValue(" not version()[0]");
        assertInvalidValue(" not version()");
    }
    
    public void testNullConditions() throws Exception
    {
        assertEquals(OwExprBooleanValue.FALSE, value("task.dueDate!=null?task.dueDate<today():false", new OwExprTaskScope("task", m_alensTask1),false));
        assertEquals(OwExprBooleanValue.TRUE, value("task.dueDate!=null?task.dueDate<date(2013,3,2,10,20,1):false",new OwExprTaskScope("task", m_alensTask2),false));
    }
}
