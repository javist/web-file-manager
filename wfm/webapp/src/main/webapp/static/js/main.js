function main($) {

    /**
     * Web File Manager JS Module
     */
    var WFM = {

        init: function() {
            $('.wfm-expand').live('click', this.expand);
            $('.wfm-unexpand').live('click', this.unexpand);
            this.initExpands();
        },

        initExpands: function() {
        	var tree = CookieHandler.read('wfm-tree');
        	if (tree) {
        		var paths = tree.split(':');
        		$.each(paths, function(i, element) {
        			var regexp = /[\\//]/, nodes = element.split(regexp);
        			WFM.recurWalk('ul.wfm-tree', nodes, 0);
        		});
        	}
        },

        recurWalk: function(container, nodes, i) {
            var name = nodes[i], pathFound = false;
            $('li a', container).each(function() {
                var $this = $(this);
                if ($.trim($this.text()) === name) {
                	var parent = $this.parent();
                    pathFound = true;
                    if (!parent.hasClass('wfm-open')) {
                    	$this.trigger('click');
                    }
                    WFM.recurWalk(parent.children('ul'), nodes, i + 1);
                }
            });
            if (!pathFound && i < nodes.length) {
                WFM.recurWalk('ul.wfm-tree', nodes, i + 1);
            }
        },

        expand: function() {
            var $this = $(this),
                parent = $($this.parent()),
                href = $this.attr('href');

            $.ajax({
                url: href,
                async: false,
                success: function(resp) {
                    var childs = $('ul.wfm-tree', $(resp));
                    WFM.appendChilds(parent, $this, childs);

                    parent.addClass('wfm-open');
                    WFM.saveTree();
                }
            });
            return false;
        },

        unexpand: function() {
            var $this = $(this),
                parent = $this.parent(),
                childs = $('ul.wfm-tree', parent);

            childs.remove();
            $this.removeClass('wfm-unexpand');
            $this.addClass('wfm-expand');

            parent.removeClass('wfm-open');
            WFM.saveTree();
            return false;
        },

        appendChilds: function(container, parentAnchor, childs) {
            container.append(childs);
            parentAnchor.removeClass('wfm-expand');
            parentAnchor.addClass('wfm-unexpand');
        },

        saveTree: function(href) {
            var tree = '', openNodes = $('.wfm-open');

            if (openNodes.length == 0) {
            	CookieHandler.create('wfm-tree', '', 365);
            }

            openNodes.each(function() {
            	var $this = $(this),
            	    childs = $this.find('li.wfm-open'),
            	    path = '';

            	if (childs.length == 0) {
            		var parents = $this.parents('li.wfm-open');
            		$(parents.get().reverse()).each(function() {
            			path += $.trim( $(this).children('a').text() ) + '\\';
            		});
            		path += $.trim( $this.children('a').text() );

            		if (tree) {
            			tree += ':';
            		}
            		tree += path;
            		CookieHandler.create('wfm-tree', tree, 365);
            	}
            });
        }
    };

    var CookieHandler = {

        create: function(name, value, days) {
            var expires = "";
            if (days) {
                var date = new Date();
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = "; expires=" + date.toGMTString();
            }
            document.cookie = name + "=" + value + expires + "; path=/";
        },

        read: function(name) {
            var nameEQ = name + "=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i += 1) {
                var c = ca[i];
                while (c.charAt(0) === ' ') { c = c.substring(1, c.length); }
                if (c.indexOf(nameEQ) === 0) { return c.substring(nameEQ.length, c.length); }
            }
            return null;
        },

        erase: function(name) {
            this.createCookie(name, "", -1);
        }
    };

    WFM.init();
};

(function(w) {

    var jq = w.jQuery.noConflict(true);

    /**
     * Start here
     */
    (function() {
        var a = {};
        a.run = function() {
            if (jq('.wfm').length > 0) {
                main(jq);
            } else {
                setTimeout(a.run, 50);
            }
        };
        a.run();
    })();
})(window);