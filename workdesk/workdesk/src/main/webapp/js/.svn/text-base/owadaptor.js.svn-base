OwAdaptor = function() {
	var adaptorsCombo;
	var baseURL;
	var adaptorsData;
	var selectedAdaptor;
	var mainPanel;
	var southPanel;
	var editableAdaptors;
	var listPanel;
	var listView;
	var dataStore;
	var fieldsDataStore;
	var editNodes;
	var editFieldsPanel;
	var bootstrapLoadingMask;
	var saveMask;
	var selectedFile = 'owbootstrap.xml';
	var hiddenSelectedBootstrap;
	var SavedConfigRecord;//class
	var lastSavedFile = null;
	return {
		config : {},
		init : function() {
			baseURL = this.config.m_baseURL;
			adaptorsData = this.config.m_adaptorsData;
			selectedAdaptor = this.config.m_selectedAdaptor;
			editableAdaptors = this.config.m_editableAdaptors;
			editNodes = this.config.m_editNodes;

			Ext.QuickTips.init();

			selectLabel = new Ext.form.Label( {
				text : 'Select the system you want to connect:',
				cls : 'arrow-label',
				region : 'west'
			});
			selectLabel.setWidth(250);
			adaptorsCombo = new Ext.form.ComboBox( {
				region : 'center',
				typeAhead : true,
				lazyRender : true,
				mode : 'local',
				store : new Ext.data.ArrayStore( {
					//id : 0,
					fields : [ 'adaptorPath', 'displayText' ],

					data : adaptorsData
				}),
				valueField : 'adaptorPath',
				displayField : 'displayText',
				listWidth : 450,
				hiddenName : 'resourceFilePath',
				value : selectedAdaptor,
				triggerAction : 'all'
			});
			adaptorsCombo.on('select', onAdaptorSelected);
			comboPanel = new Ext.Panel( {
				region : 'center',
				layout : 'border',
				border : false,
				items : [ selectLabel, adaptorsCombo ]
			});

			var selectAdaptorButton = new Ext.Button( {
				region : 'east',
				//autoWidth:false,
				minSize : 150,
				maxSize : 160,
				margins : '0 0 0 5',
				text : 'Start Alfresco Workdesk'
			});
			selectAdaptorButton.setWidth(150);
			selectAdaptorButton.on('click', onSelectAdaptorPressed);
			comboPanel.setWidth(550);
			//south components
		listPanel = new Ext.Panel( {
			//title : 'list',
			border : false,
			region : 'north',
			layout : 'border'
		});
		createListPanel(listPanel);
		editFieldsPanel = new Ext.Panel( {
			region : 'center',
			autoScroll : true,
			style : {
				overflow : 'auto'
			},
			layout : 'anchor'
		});
		createFieldsDataStore();
		//createFieldsPanel(editFieldsPanel);
		saveButton = new Ext.Button( {
			text : 'Save'
		});
		saveButton.on('click', onSaveButtonPanelClick);
		saveButtonPanelLayout = {
			type : 'vbox',
			padding : '5',
			align : 'center'
		};
		saveButtonPanel = new Ext.Panel( {
			region : 'south',
			layout : saveButtonPanelLayout,
			layoutConfig : 'center',
			items : [ saveButton ]
		});
		saveButtonPanel.setHeight(35);
		//south region
		southPanel = new Ext.Panel( {
			//title : 'south',
			region : 'south',
			layout : 'border',
			border : false,
			items : [ listPanel, editFieldsPanel, saveButtonPanel ]
		});
		southPanel.setHeight(600);
		southPanel.setVisible(isAdaptorEditable(selectedAdaptor));
		mainPanel = new Ext.Panel( {
			layout : 'border',
			items : [ comboPanel, selectAdaptorButton, southPanel ],
			renderTo : 'mainPanel'
		});
		mainPanel.setWidth(700);
		//		mainPanel.setHeight(26);
		//		mainPanel.doLayout();
		onAdaptorSelected(adaptorsCombo);
	}

	};

	function createFieldsPanel(editFieldsPanel) {
		editFieldsPanel.removeAll();

		for (i = 0; i < fieldsDataStore.getCount(); i++) {
			record = fieldsDataStore.getAt(i);
			labelId = 'fieldLabel' + i;
			label = new Ext.form.Label( {
				id : labelId,
				text : record.get('labelText'),
				margins : '10 5 5 5',
				region : 'west'
			});
			//		toolTip =  new Ext.QuickTip({
			//			target: labelId,
			//			html: record.get('tooltip')
			//		});
			editText = new Ext.form.TextField( {
				id : 'editField' + i,
				value : record.get('nodeValue'),
				margins : '5 5 3 5',
				region : 'center'
			});
			label.setWidth(200);
			panel = new Ext.Panel( {
				id : 'fieldPanel' + i,
				layout : 'border',
				border : false,
				items : [ label, editText ],
				anchor : 0
			});
			panel.setHeight(30);
			editFieldsPanel.add(panel);
		}
		editFieldsPanel.doLayout();
	}
	function createListPanel(listPanel) {
		listPanel.setHeight(200);
		listPanel.removeAll();
		hiddenSelectedBootstrap = new Ext.form.Hidden( {
			name : 'selectedBootstrap'
		});
		listLabel = new Ext.Panel( {
			html : 'Select the configuration you want to edit/load:',
			//cls : 'arrow-label',
			border : false,
			region : 'north'
		});
		listLabel.add(hiddenSelectedBootstrap);
		listLabel.setHeight(20);
		listPanel.add(listLabel);
		listContainer = new Ext.Panel( {
			//title :'listContainer',
			border : false,
			layout : 'fit',
			region : 'center'
		});
		listContainer.removeAll();
		SavedConfigRecord = Ext.data.Record.create( [ {
			name : 'fileName',
			mapping : 1
		}, {
			name : 'identifier',
			mapping : 2
		}, {
			name : 'lastModified',
			mapping : 3
		} ]);
		var reader = new Ext.data.ArrayReader( {
			idIndex : 0
		}, SavedConfigRecord);
		dataStore = new Ext.data.Store( {
			autoDestroy : true,
			reader : reader,
			//data : [[0,'owbootstrap.xml','01.01.2011']]
			url : baseURL
		});
		listView = new Ext.grid.GridPanel( {
			store : dataStore,
			colModel : new Ext.grid.ColumnModel( {
				defaults : {
					width : 120,
					sortable : true
				},
				columns : [ {
					id : 'fileName',
					header : 'File name',
					width : 150,
					sortable : true,
					dataIndex : 'fileName'
				}, {
					header : 'Identifier',
					width : 100,
					sortable : true,
					dataIndex : 'identifier'
				}, {
					header : 'Last Modified',
					sortable : true,
					dataIndex : 'lastModified'
				} ]
			}),
			viewConfig : {
				forceFit : true
			},
			sm : new Ext.grid.RowSelectionModel( {
				singleSelect : true
			}),
			loadMask : new Ext.LoadMask(Ext.getBody(), {
				msg : "Loading saved configuration files..."
			})
		});
		listView.getSelectionModel().on('rowselect', onBootstrapSelect);

		listContainer.add(listView);
		listContainer.doLayout();
		listPanel.add(listContainer);
		listPanel.doLayout();
	}
	function onAdaptorSelected(combo, record, index) {
		if (isAdaptorEditable(combo.getValue())) {
			mainPanel.setHeight(630);
			southPanel.setVisible(true);
			selectedFile = 'owbootstrap.xml';
			loadBootstrapStore(selectedFile);
			mainPanel.doLayout();
		} else {
			mainPanel.setHeight(26);
			southPanel.setVisible(false);
		}
	}
	function loadBootstrapStore(theSelectedFile) {
		loadObject = {
			params : Ext.urlEncode( {
				listSavedFiles : 1,
				currentAdaptor : adaptorsCombo.getValue()
			}),
			selectedFile : theSelectedFile,
			callback : onDataStoreLoad
		};
		dataStore.load(loadObject);
	}
	function onFieldsStoreLoad(record, option, success) {
		createFieldsPanel(editFieldsPanel);
		if (bootstrapLoadingMask != null) {
			bootstrapLoadingMask.hide();
		}
	}

	function onDataStoreLoad(record, option, success) {
		if (option.selectedFile != null) {
			for (i = 0; i < dataStore.getCount(); i++) {
				if (dataStore.getAt(i).get('fileName') == option.selectedFile) {
					listView.getSelectionModel().selectRow(i);
					break;
				}
			}
		} else {
			listView.getSelectionModel().selectFirstRow();
		}

	}

	function onSelectAdaptorPressed(button, eventObject) {
		form = document.getElementById('selectionFormId');
		if (form) {
			if (listView != null) {
				selectedRecord = listView.getSelectionModel().getSelected();
				if (selectedRecord != null) {
					hiddenSelectedBootstrap.setValue(selectedRecord.get('fileName'));
				}
			}
			form.submit();
		}
	}
	function onBootstrapSelect(selectionModel, rowIndex, adaptorRecord) {
		selectedFile = adaptorRecord.get('fileName');
		if (selectedFile == lastSavedFile) { //prevent re-update fields, the config was just saved
			lastSavedFile = null;
			return false;
		}
		fieldsObjectRequest = {
			params : Ext.urlEncode( {
				getEditNodes : 1,
				currentAdaptor : adaptorsCombo.getValue(),
				currentFile : selectedFile
			})
		};
		if (bootstrapLoadingMask == null) {
			bootstrapLoadingMask = new Ext.LoadMask(Ext.getBody(), {
				msg : "Loading editable fields values..."
			});
		}
		bootstrapLoadingMask.show();
		fieldsDataStore.load(fieldsObjectRequest);
	}
	function onSaveButtonPanelClick() {
		modified = false;
		for (i = 0; i < fieldsDataStore.getCount(); i++) {
			record = fieldsDataStore.getAt(i);
			textField = Ext.getCmp('editField' + i);
			if (textField && (record.get('nodeValue') != textField.getValue())) {
				modified = true;
				break;
			}
		}
		if (modified) {
			Ext.MessageBox.prompt('Specify configuration id', 'Please enter the associated Id \nfor your configuration file:', showResultText);
		} else {
			Ext.Msg.alert("Nothing to save", "There are no changes to be saved.");
		}
	}
	function showResultText(btn, identifierValue) {
		//Ext.Msg.alert('Button Click', 'You clicked the {0} button and entered the text "{1}".', btn, text);
		if (btn == "ok") {
			if (saveMask == null) {
				saveMask = new Ext.LoadMask(Ext.getBody(), {
					msg : "Saving editable fields values..."
				});
			}

			allParams = '{saveNodes:"true",currentAdaptor:"' + adaptorsCombo.getValue() + '"';
			allParams = allParams + ',identifier:"' + identifierValue + '"';
			allParams = allParams + ',selectedBootstrap:"' + listView.getSelectionModel().getSelected().get('fileName') + '"';
			modified = false;
			for (i = 0; i < fieldsDataStore.getCount(); i++) {
				record = fieldsDataStore.getAt(i);
				textField = Ext.getCmp('editField' + i);
				if (textField && (record.get('nodeValue') != textField.getValue())) {
					modified = true;
					propId = textField.id;
					allParams = allParams + ',' + propId + ":'" + String.escape(textField.getValue()) + "'";
				}
			}
			allParams = allParams + '}';
			if (modified) {
				saveMask.show();
				allParamsObject = Ext.util.JSON.decode(allParams);
				postParams = Ext.urlEncode(allParamsObject);
				callback = {
					success : function(o) {
						saveMask.hide();
						createdArray = Ext.util.JSON.decode(o.responseText);
						var lastRecord = new SavedConfigRecord( {
							idIndex : createdArray[0],
							fileName : createdArray[1],
							identifier : createdArray[2],
							lastModified : createdArray[3]
						});
						dataStore.add(lastRecord);
						dataStore.commitChanges();
						index = dataStore.indexOf(lastRecord);
						lastSavedFile = lastRecord.get('fileName');
						listView.getSelectionModel().selectRow(index);
						Ext.Msg.alert("Configuration saved", "Saved file: " + lastRecord.get('fileName'));
						return true;
					},
					failure : function(o) {
						saveMask.hide();
						Ext.Msg.alert("Error", "Save error:\n" + o.responseText);
					}
				};

				Ext.lib.Ajax.request("POST", baseURL, callback, postParams);
			} else {
				Ext.Msg.alert("Nothing to save", "There are no changes to be saved.");
			}
		} else {
			Ext.Msg.alert("Changes discarded", "You choose to not save your changes.");
		}

	}

	function isAdaptorEditable(adaptor) {
		return editableAdaptors.indexOf(adaptor) != -1;
	}
	function createFieldsDataStore() {
		var fieldsConfigRecord = Ext.data.Record.create( [ {
			name : 'tooltip',
			mapping : 1
		}, {
			name : 'labelText',
			mapping : 2
		}, {
			name : 'nodeValue',
			mapping : 3
		} ]);
		fieldsReader = new Ext.data.ArrayReader( {
			idIndex : 0
		}, fieldsConfigRecord);
		fieldsDataStore = new Ext.data.Store( {
			autoDestroy : true,
			reader : fieldsReader,
			url : baseURL
		});
		fieldsDataStore.on('load', onFieldsStoreLoad);
		fieldsDataStore.loadData(editNodes);
	}
}();