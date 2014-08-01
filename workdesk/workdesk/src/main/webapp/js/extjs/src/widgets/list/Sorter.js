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
 * @class Ext.list.Sorter
 * @extends Ext.util.Observable
 * <p>Supporting Class for Ext.list.ListView</p>
 * @constructor
 * @param {Object} config
 */
Ext.list.Sorter = Ext.extend(Ext.util.Observable, {
    /**
     * @cfg {Array} sortClasses
     * The CSS classes applied to a header when it is sorted. (defaults to <tt>["sort-asc", "sort-desc"]</tt>)
     */
    sortClasses : ["sort-asc", "sort-desc"],

    constructor: function(config){
        Ext.apply(this, config);
        Ext.list.Sorter.superclass.constructor.call(this);
    },

    init : function(listView){
        this.view = listView;
        listView.on('render', this.initEvents, this);
    },

    initEvents : function(view){
        view.mon(view.innerHd, 'click', this.onHdClick, this);
        view.innerHd.setStyle('cursor', 'pointer');
        view.mon(view.store, 'datachanged', this.updateSortState, this);
        this.updateSortState.defer(10, this, [view.store]);
    },

    updateSortState : function(store){
        var state = store.getSortState();
        if(!state){
            return;
        }
        this.sortState = state;
        var cs = this.view.columns, sortColumn = -1;
        for(var i = 0, len = cs.length; i < len; i++){
            if(cs[i].dataIndex == state.field){
                sortColumn = i;
                break;
            }
        }
        if(sortColumn != -1){
            var sortDir = state.direction;
            this.updateSortIcon(sortColumn, sortDir);
        }
    },

    updateSortIcon : function(col, dir){
        var sc = this.sortClasses;
        var hds = this.view.innerHd.select('em').removeClass(sc);
        hds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
    },

    onHdClick : function(e){
        var hd = e.getTarget('em', 3);
        if(hd && !this.view.disableHeaders){
            var index = this.view.findHeaderIndex(hd);
            this.view.store.sort(this.view.columns[index].dataIndex);
        }
    }
});

// Backwards compatibility alias
Ext.ListView.Sorter = Ext.list.Sorter;