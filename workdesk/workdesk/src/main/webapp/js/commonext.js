

function setFocusToField(){
	var bodyElement = Ext.get('owbody');
	var inputElements = bodyElement.query(".x-form-field");
	
	if(inputElements[0] != null){
		inputElements[0].focus();
	}else{
		inputElements = bodyElement.query("input");
		inputElements[0].focus();
	}
}

Ext.onReady(function() {
	
/* Add popup window for shortcut info */
/* ********************************** */
    extShortcutElement = Ext.get('owkeyinfo');
    var extShortcutButton = Ext.get('OwKeyInfoButton');
    
    
    
    if(extShortcutElement && extShortcutButton)
    {
        extShortcutPanel = new Ext.Panel({
            autoScroll: 'true',
            contentEl: 'owkeyinfo'
        });
        var extShortcutTitle = Ext.getDom(extShortcutElement.first('h1')).innerHTML;
        extShortcutWindow = new Ext.Window({
            title: extShortcutTitle,
            closable: 'true',
            closeAction: 'hide',
            layout:'fit',
            width:350,
            height:360,
            minWidth: 200,
            minHeight: 200,
            items: extShortcutPanel
        });
    
        extShortcutButton.on('click', function(){
            extShortcutElement.removeClass('hidden');
            extShortcutWindow.show();    
        });
    }
    
    return;
    
    //Here the resizable navigation is implemented as an example
    
    var layoutDiv = Ext.get('OwSubLayout_Div'); 
    layoutDiv.setHeight(400);
    var layoutNavigation = Ext.get('OwSubLayout_NAVIGATION');
    var layoutMain = Ext.get('OwSubLayout_MAIN');
    if(layoutDiv && layoutNavigation && layoutMain)
    {
        var height = layoutDiv.getHeight();
        layoutNavigation.setHeight(height);
        layoutMain.setHeight(height);
        var resDiv= new Ext.Resizable('OwSubLayout_NAVIGATION', {
            handles: 'e',
            pinned: true,
            minWidth: 100,
            wrap: true,
            listeners : {
                'resize' : function(resizable, width, height) {
                   //alert(width);
                }
            }
            
        });
    }
});

function toggleShortcutWindow()
{
    if(extShortcutWindow.isVisible())
    {
        extShortcutWindow.hide();
    }else
    {
        extShortcutElement.removeClass('hidden');
        extShortcutWindow.show();
    }
}