OwPropGroup = function() {
return {
		init : function() {

		},
		toggleGroup : function(groupId, updateURL) {
			group = document.getElementById(groupId);
			statusElement = OwPropGroup.getGroupStatusHolder(groupId);
			if (statusElement) {
				isCollapsed=eval(statusElement.value);
			}
			if (group) {
				if (!isCollapsed) {
					position = group.offsetTop;
					group.style.display = 'none';
				} else {
					if (Ext.isIE7) {
						group.style.display = 'block';
					} else {
						group.style.display = 'table-row-group';
					}
				}
				OwPropGroup.updateGroupStatus(groupId, updateURL, isCollapsed);
			}
		},

		expandGroup : function(groupId, updateURL) {
			group = document.getElementById(groupId);
			if (group) {
				group.style.display = 'table-row-group';
				OwPropGroup.updateGroupStatus(groupId, updateURL, false);
			}
		},
		updateGroupStatus : function(groupId, updateURL, isCollapsed) {
			OwPropGroup.updateGroupHeaderStatus(groupId, isCollapsed);

			var requestConfig = {
				timeout : 30000,
				//disableCaching : false,
				success : function(o) {
				OwSubLayoutEXTJS.updateLayout();		
				},
				failure : function(o) {
				}
			};
			var postparam = Ext.urlEncode( {
				owGroupId : "" + groupId + "",
				isGroupCollapsed : "" + !isCollapsed
			});
			Ext.lib.Ajax.request("POST", updateURL, requestConfig, postparam);
		},
		updateGroupHeaderStatus : function(groupId, isCollapsed) {
			statusHolder = OwPropGroup.getGroupStatusHolder(groupId);
			statusHolder.value = ''+!isCollapsed;
			toBeUpdated = Ext.get('td_' + groupId);
			if (isCollapsed) {
				toBeUpdated.removeClass('OwGroupCollapsed');
				toBeUpdated.addClass('OwGroupExpanded');
			} else {
				toBeUpdated.removeClass('OwGroupExpanded');
				toBeUpdated.addClass('OwGroupCollapsed');
			}
		},
		getGroupStatusHolder:function(groupId) {
			return document.getElementById('isCollapsed_'+groupId);
		}
	};
	
}();