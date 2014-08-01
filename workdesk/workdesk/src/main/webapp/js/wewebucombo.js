Ext.override(Ext.form.ComboBox, {
	initList : function() {
		if (!this.list) {
			var cls = 'x-combo-list';

			this.list = new Ext.Layer({
				parentEl : this.getListParent(),
				shadow : this.shadow,
				cls : [ cls, this.listClass ].join(' '),
				constrain : false
			});

			this.innerList = this.list.createChild({
				cls : cls + '-inner'
			});
			this.mon(this.innerList, 'mouseover', this.onViewOver, this);
			this.mon(this.innerList, 'mousemove', this.onViewMove, this);

			if (this.pageSize) {
				this.footer = this.list.createChild({
					cls : cls + '-ft'
				});
				this.pageTb = new Ext.PagingToolbar({
					store : this.store,
					pageSize : this.pageSize,
					renderTo : this.footer
				});
				this.assetHeight += this.footer.getHeight();
			}

			if (!this.tpl) {
				this.tpl = '<tpl for="."><div class="' + cls + '-item">{'
						+ this.displayField + '}</div></tpl>';
			}
			this.view = new Ext.DataView({
				applyTo : this.innerList,
				tpl : this.tpl,
				singleSelect : true,
				selectedClass : this.selectedClass,
				itemSelector : this.itemSelector || '.' + cls + '-item',
				emptyText : this.listEmptyText
			});

			this.mon(this.view, 'click', this.onViewClick, this);

			//Only after the store is loaded will we know the real width of items in the drop down list
			this.bindStore(this.store, true);

			var lw;
			if (Ext.isDefined(this.resizable) || this.listWidth === 'auto') {
				var rect = 0;
				if (Ext.isIE) {
					//list-auto-width-IE class contains one line --> display:inline;
					//This will allow us to get the actual width of innerList div
					this.innerList.addClass('list-auto-width-IE');
					rect = this.list.getBox();
					//Remove this class once we got the width
					this.innerList.removeClass('list-auto-width-IE');
				} else {
					rect = this.list.getBox();
				}
				lw = Math.max(this.wrap.getWidth(), rect.width);
				this.list.setSize(lw, 0);
			} else if (this.listWidth) {
				lw = this.listWidth
						|| Math.max(this.wrap.getWidth(), this.minListWidth);
				this.list.setSize(lw, 0);
			}
			this.list.swallowEvent('mousewheel'); // if choking proceed to Heimlich()
			this.assetHeight = 0;
			if (this.syncFont !== false) {
				this.list.setStyle('font-size', this.el.getStyle('font-size'));
			}
			if (this.title) {
				this.header = this.list.createChild({
					cls : cls + '-hd',
					html : this.title
				});
				this.assetHeight += this.header.getHeight();
			}
			this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));

			if (this.resizable) {
				this.resizer = new Ext.Resizable(this.list, {
					pinned : true,
					handles : 'se'
				});
				this.mon(this.resizer, 'resize', function(r, w, h) {
					this.maxHeight = h - this.handleHeight
							- this.list.getFrameWidth('tb') - this.assetHeight;
					this.listWidth = w;
					this.innerList.setWidth(w - this.list.getFrameWidth('lr'));
					this.restrictHeight();
				}, this);

				this[this.pageSize ? 'footer' : 'innerList'].setStyle(
						'margin-bottom', this.handleHeight + 'px');
			}
		}
	}
});