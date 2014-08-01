Ext.grid.WewebuRowSelectionModel = function(config){
	for (param in config) {
		if (this[param] != undefined) {
			this[param] = config[param];
		}
	}
    Ext.grid.WewebuRowSelectionModel.superclass.constructor.call(this);
};

Ext.extend(Ext.grid.WewebuRowSelectionModel, Ext.grid.RowSelectionModel, {
	
	// private
    handleMouseDown : function(g, rowIndex, e){
        if(this.isLocked()){
            return;
        }
        var view = this.grid.getView();
        var macCtrl=e.ctrlKey && ( (Ext.isMac && e.browserEvent.metaKey) || (!Ext.isMac) );
        if(e.shiftKey && !this.singleSelect && this.last !== false){
            var last = this.last;
            this.selectRange(last, rowIndex, e.ctrlKey);
            this.last = last; // reset the last
            view.focusRow(rowIndex);
        }else{
            var isSelected = this.isSelected(rowIndex);
            if(e.button != 0 && isSelected){
                view.focusRow(rowIndex);
            } else if(macCtrl && isSelected ){
                this.deselectRow(rowIndex);
                this.persistCurrentSelection(rowIndex,false);
            }else if (!isSelected || this.getCount() > 1)  {
                this.selectRow(rowIndex,  (e.ctrlKey && isSelected) || (!isSelected && macCtrl) || e.shiftKey );
                this.persistCurrentSelection(rowIndex,true);
                view.focusRow(rowIndex);
            } 
        }
    },
	selectPersistentItems : function(){
    	for (i=0;i<this.grid.store.getCount();i++) {
    	   var record = this.grid.store.getAt(i);
    	   if (record) {
    		   if (record.data["rowSelected"]) {
    			   this.selectRow(i,true);
    		   }
    	   }  	   
    	}
    },
    persistCurrentSelection:function(index, isSelected) {
		//alert('Selected row is: ' + index);
    	var record = this.grid.store.getAt(index);
    	if (record!=null) {
			var postparam = Ext.urlEncode({objid:record.id,status:""+isSelected});
	        var callback = {
	                success: function(o){
	            		return true;
	            	},
	                failure: function(o){
	            		if (trim(o.responseText)=='owsessiontimeout') {
	                		document.location=m_mainUrl;
	                	} else {
	                		if(o.responseText){
	                		Ext.Msg.alert("Update error:\n"+o.responseText);
	                		}
	                	}
	            		return false;
	            	}
	            };
			Ext.lib.Ajax.request("POST",m_url_persistSelection,callback,postparam);
    	}
    },
    persistRange : function(startRow, endRow, keepExisting){
        var i;
        if(this.isLocked()){
            return;
        }
        if(!keepExisting){
            this.clearSelections();
        }
        if(startRow <= endRow){
            for(i = startRow; i <= endRow; i++){
            	isSelected = this.isSelected(i);
                this.persistCurrentSelection(i, isSelected);
            }
        }else{
            for(i = startRow; i >= endRow; i--){
            	isSelected = this.isSelected(i);
                this.persistCurrentSelection(i, isSelected);
            }
        }
    },
    /**
     * Clears all selections.
     */
    clearSelections : function(fast){
        if(this.locked) return;
        var ds = this.grid.store;
        var s = this.selections;
        s.each(function(r){
            this.deselectRow(ds.indexOfId(r.id));
            this.persistCurrentSelection(ds.indexOfId(r.id),false);
        }, this);
        s.clear();
        this.last = false;
    },
    
        
    /**
     * Selects all rows.
     */
    selectAll : function(){
        if(this.locked) return;
        this.selections.clear();
        for(var i = 0, len = this.grid.store.getCount(); i < len; i++){
            this.selectRow(i, true);
            this.persistCurrentSelection(i,true);
        }
    },
    setMainUrl : function(mainUrl) {
    	m_mainUrl = mainUrl;
    },
    /**
     * Selects a range of rows if the selection model
     * {@link Ext.grid.AbstractSelectionModel#isLocked is not locked}.
     * All rows in between startRow and endRow are also selected.
     * @param {Number} startRow The index of the first row in the range
     * @param {Number} endRow The index of the last row in the range
     * @param {Boolean} keepExisting (optional) True to retain existing selections
     */
    selectRange : function(startRow, endRow, keepExisting){
        var i;
        if(this.isLocked()){
            return;
        }
        if(!keepExisting){
            this.clearSelections();
        }
        if(startRow <= endRow){
            for(i = startRow; i <= endRow; i++){
                this.selectRow(i, true);
                this.persistCurrentSelection(i,true);
            }
        }else{
            for(i = startRow; i >= endRow; i--){
                this.selectRow(i, true);
                this.persistCurrentSelection(i,true);
            }
        }
    },
    /**
     * Deselects a range of rows if the selection model
     * {@link Ext.grid.AbstractSelectionModel#isLocked is not locked}.  
     * All rows in between startRow and endRow are also deselected.
     * @param {Number} startRow The index of the first row in the range
     * @param {Number} endRow The index of the last row in the range
     */
    deselectRange : function(startRow, endRow, preventViewNotify){
        if(this.isLocked()){
            return;
        }
        for(var i = startRow; i <= endRow; i++){
            this.deselectRow(i, preventViewNotify);
            this.persistCurrentSelection(i,false);
        }
    },
    /**
     * Selects the first row in the grid.
     */
    selectFirstRow : function(){
        this.selectRow(0);
        this.persistCurrentSelection(0,true);
    },

    /**
     * Select the last row.
     * @param {Boolean} keepExisting (optional) <tt>true</tt> to keep existing selections
     */
    selectLastRow : function(keepExisting){
    	lastIndex = this.grid.store.getCount() - 1;
        this.selectRow(lastIndex, keepExisting);
        this.persistCurrentSelection(lastIndex,true);
    },

    /**
     * Selects the row immediately following the last selected row.
     * @param {Boolean} keepExisting (optional) <tt>true</tt> to keep existing selections
     * @return {Boolean} <tt>true</tt> if there is a next row, else <tt>false</tt>
     */
    selectNext : function(keepExisting){
        if(this.hasNext()){
            this.selectRow(this.last+1, keepExisting);
            this.persistCurrentSelection(this.last,true);
            this.grid.getView().focusRow(this.last);
            return true;
        }
        return false;
    },

    /**
     * Selects the row that precedes the last selected row.
     * @param {Boolean} keepExisting (optional) <tt>true</tt> to keep existing selections
     * @return {Boolean} <tt>true</tt> if there is a previous row, else <tt>false</tt>
     */
    selectPrevious : function(keepExisting){
        if(this.hasPrevious()){
            this.selectRow(this.last-1, keepExisting);
            this.persistCurrentSelection(this.last,true);
            this.grid.getView().focusRow(this.last);
            return true;
        }
        return false;
    }
});