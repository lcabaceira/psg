Ext.form.EnumEditor = function(_1, _2, enumInfoUrl) {
	Ext.form.EnumEditor.superclass.constructor.call(this, _1, _2);
	this.enumInfoUrl = enumInfoUrl;
};

Ext.extend(Ext.form.EnumEditor, Ext.grid.GridEditor, {
	startEdit : function(el, value) {
	
		//create a new AJAX URL  for the store of this choice control
		theStoreUrl= this.enumInfoUrl + '&row=' + this.row + '&col=' + this.col;

		//replace the store URL
		//this.field.store.proxy.conn.url = theStoreUrl;
		this.field.store.proxy = new Ext.data.HttpProxy( {
			url : theStoreUrl
		});

		//create an entry for the current value 
		//the store will not update until requested
		var evaluatedValue = eval(value);
		var enumValueStore= new Ext.data.ArrayStore({fields:['value','text'],data:evaluatedValue!=null?evaluatedValue:value});
    	
    	if (enumValueStore.data.length>0)
    	{
    		var valueElements=enumValueStore.getAt(0);
    		theText=valueElements.data.value?valueElements.data.text:'';

    		var ComboRecord = Ext.data.Record.create(['value','text']);
    		var theRecord=new ComboRecord({value:value,text:theText});
    		this.field.store.add(theRecord);
    	}
		
		Ext.grid.GridEditor.prototype.startEdit.call(this, el, value);
	}
});