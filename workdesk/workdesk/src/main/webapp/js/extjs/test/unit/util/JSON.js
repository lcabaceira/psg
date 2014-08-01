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
Ext.test.session.addTest( 'Ext.util', {

    name: 'JSON',

    planned: 4,

    // same as Ext.encode
    // 1
    test_encode: function() {
        Y.Assert.areEqual( '{"foo":"bar"}', Ext.util.JSON.encode( { foo: 'bar' } ), 'Test encode with simple object' );
    },

    // same as Ext.decode
    // 2
    test_decode: function() {
        Y.ObjectAssert.hasKeys({
            foo: 'bar'
        }, Ext.util.JSON.decode( '{"foo":"bar"}' ), 'Test decode with a simple object');
        Y.ObjectAssert.hasKeys({
            foo: ['bar','baz']
        }, Ext.util.JSON.decode( '{"foo":["bar","baz"]}' ), 'Test decode with a hash + array');
    }

    // encodeDate

});
