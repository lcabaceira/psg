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
 * @class Ext.Element
 */
Ext.Element.addMethods({
    /**
     * Returns true if this element is scrollable.
     * @return {Boolean}
     */
    isScrollable : function(){
        var dom = this.dom;
        return dom.scrollHeight > dom.clientHeight || dom.scrollWidth > dom.clientWidth;
    },

    /**
     * Scrolls this element the specified scroll point. It does NOT do bounds checking so if you scroll to a weird value it will try to do it. For auto bounds checking, use scroll().
     * @param {String} side Either "left" for scrollLeft values or "top" for scrollTop values.
     * @param {Number} value The new scroll value.
     * @return {Element} this
     */
    scrollTo : function(side, value){
        this.dom["scroll" + (/top/i.test(side) ? "Top" : "Left")] = value;
        return this;
    },

    /**
     * Returns the current scroll position of the element.
     * @return {Object} An object containing the scroll position in the format {left: (scrollLeft), top: (scrollTop)}
     */
    getScroll : function(){
        var d = this.dom, 
            doc = document,
            body = doc.body,
            docElement = doc.documentElement,
            l,
            t,
            ret;

        if(d == doc || d == body){
            if(Ext.isIE && Ext.isStrict){
                l = docElement.scrollLeft; 
                t = docElement.scrollTop;
            }else{
                l = window.pageXOffset;
                t = window.pageYOffset;
            }
            ret = {left: l || (body ? body.scrollLeft : 0), top: t || (body ? body.scrollTop : 0)};
        }else{
            ret = {left: d.scrollLeft, top: d.scrollTop};
        }
        return ret;
    }
});