var calAppendCurrentTime = true;
var oldLink = null;
var origDate = null;

// code to change the active stylesheet
function setActiveStyleSheet(link, title) {
  var i, a, main;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
      a.disabled = true;
      if(a.getAttribute("title") == title) a.disabled = false;
    }
  }
  if (oldLink) oldLink.style.fontWeight = 'normal';
  oldLink = link;
  link.style.fontWeight = 'bold';
  return false;
}


function selected(cal, date) {
  cal.sel.value = date;
  if (calAppendCurrentTime == true) {
    var timeNow = new Date();
    var theMinutes = timeNow.getMinutes();
    var theHours = timeNow.getHours();
    if (theMinutes < 10) {
      theMinutes = "0" + theMinutes;
    }
    if (theHours < 10) {
      theHours = "0" + theHours;
    }
    cal.sel.value += " " + theHours + ":" + theMinutes; // just update the date in the input field.
  }
  if (cal.sel.id == "sel1" || cal.sel.id == "sel3")
    // if we add this call we close the calendar on single-click.
    // just to exemplify both cases, we are using this only for the 1st
    // and the 3rd field, while 2nd and 4th will still require double-click.
    cal.callCloseHandler();
}


// And this gets called when the end-user clicks on the _selected_ date,
// or clicks on the "Close" button.  It just hides the calendar without
// destroying it.
function closeHandler(cal) {
  cal.hide();                        // hide the calendar
}

//returns the given format of the ow!
function retOrigFormat() {
	return origDate;
}


// This function shows the calendar under the element having the given ID.
// It takes care of catching "mousedown" signals on document and hiding the
// calendar if the click was outside.
function showCalendar(id, format) 
{
	origDate = format;
 // TODO: convert java dateformat to this calendar dateformat
 // format = 'dd.mm.y';
 // var test = fDate(id,format);
  calAppendCurrentTime = false;
  
  var el = document.getElementById(id);
  if (calendar != null) {
    // we already have some calendar created
    calendar.hide();                 // so we hide it first.
  } else {
    // first-time call, create the calendar.
    var cal = new Calendar(false, null, selected, closeHandler);
    // uncomment the following line to hide the week numbers
    // cal.weekNumbers = false;
    calendar = cal;                  // remember it in the global var
    cal.setRange(1900, 2070);        // min/max year allowed.
    cal.create();
  }
  calendar.setDateFormat(format);    // set the specified date format
  calendar.parseDate(el.value);      // try to parse the text in field
  calendar.sel = el;                 // inform it what input field we use
  calendar.showAtElement(el);        // show the calendar below it

  return false;
}

var MINUTE = 60 * 1000;
var HOUR = 60 * MINUTE;
var DAY = 24 * HOUR;
var WEEK = 7 * DAY;

// If this handler returns true then the "date" given as
// parameter will be disabled.  In this example we enable
// only days within a range of 10 days from the current
// date.
// You can use the functions date.getFullYear() -- returns the year
// as 4 digit number, date.getMonth() -- returns the month as 0..11,
// and date.getDate() -- returns the date of the month as 1..31, to
// make heavy calculations here.  However, beware that this function
// should be very fast, as it is called for each day in a month when
// the calendar is (re)constructed.
function isDisabled(date) {
  var today = new Date();
  return (Math.abs(date.getTime() - today.getTime()) / DAY) > 10;
}
