/*
This file is part of Ext JS 3.4

Copyright (c) 2011-2013 Sencha Inc

Contact:  http://www.sencha.com/contact

Commercial Usage
Licensees holding valid commercial licenses may use this file in accordance with the Commercial
Software License Agreement provided with the Software or, alternatively, in accordance with the
terms contained in a written agreement between you and Sencha.

If you are unsure which license is appropriate for your use, please contact the sales department
at http://www.sencha.com/contact.

Build date: 2013-04-03 15:07:25
*/
/**
 * @class Ext.direct.JsonProvider
 * @extends Ext.direct.Provider
 */
Ext.direct.JsonProvider = Ext.extend(Ext.direct.Provider, {
    parseResponse: function(xhr){
        if(!Ext.isEmpty(xhr.responseText)){
            if(typeof xhr.responseText == 'object'){
                return xhr.responseText;
            }
            return Ext.decode(xhr.responseText);
        }
        return null;
    },

    getEvents: function(xhr){
        var data = null;
        try{
            data = this.parseResponse(xhr);
        }catch(e){
            var event = new Ext.Direct.ExceptionEvent({
                data: e,
                xhr: xhr,
                code: Ext.Direct.exceptions.PARSE,
                message: 'Error parsing json response: \n\n ' + data
            });
            return [event];
        }
        var events = [];
        if(Ext.isArray(data)){
            for(var i = 0, len = data.length; i < len; i++){
                events.push(Ext.Direct.createEvent(data[i]));
            }
        }else{
            events.push(Ext.Direct.createEvent(data));
        }
        return events;
    }
});