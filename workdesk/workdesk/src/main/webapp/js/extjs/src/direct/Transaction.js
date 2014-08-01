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
 * @class Ext.Direct.Transaction
 * @extends Object
 * <p>Supporting Class for Ext.Direct (not intended to be used directly).</p>
 * @constructor
 * @param {Object} config
 */
Ext.Direct.Transaction = function(config){
    Ext.apply(this, config);
    this.tid = ++Ext.Direct.TID;
    this.retryCount = 0;
};
Ext.Direct.Transaction.prototype = {
    send: function(){
        this.provider.queueTransaction(this);
    },

    retry: function(){
        this.retryCount++;
        this.send();
    },

    getProvider: function(){
        return this.provider;
    }
};