Ext.grid.WewebuGridView = function(config){
    Ext.grid.WewebuGridView.superclass.constructor.call(this);
};

Ext.extend(Ext.grid.WewebuGridView, Ext.grid.GridView, {
    getRowClass : function(record,index){
        var rowClassName = record.get("rowClassName");
        return rowClassName ? rowClassName : "";
    },
    render : function(){
        Ext.grid.WewebuGridView.superclass.render.call(this);
        // remove third header-context-menu item
        if(this.hmenu){
            var thm = this.hmenu;
            if(thm.items){
                var thmi = thm.items;
                if(thmi.length > 2){
                    var item = thmi.itemAt(2);
                    this.hmenu.remove(item);
                }
            }
        }
    }
});