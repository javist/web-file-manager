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
            var regexp = /[\\//]/, hashs = window.location.hash.split(regexp);
            if (hashs.length > 1) {
                this.recurWalk('ul.wfm-tree', hashs, 1);
            }
        },

        recurWalk: function(tree, hashs, i) {
            var name = hashs[i], pathFound = false;
            $('li a', tree).each(function() {
                var $this = $(this);
                if ($.trim($this.text()) === name) {
                    pathFound = true;
                    $this.trigger('click', function(childs) {
                        WFM.recurWalk(childs, hashs, i + 1);
                    });
                }
            });
            if (!pathFound && i < hashs.length) {
                WFM.recurWalk('ul.wfm-tree', hashs, i + 1);
            }
        },

        expand: function(e, callback) {
            var $this = $(this),
                href = $this.attr('href');

            $.ajax({
                url: href,
                success: function(resp) {
                    var childs = $('ul.wfm-tree', $(resp));
                    WFM.appendChilds($this, childs);
                    WFM.appendHash(href);

                    if (callback) {
                        callback(childs);
                    }
                }
            });
            return false;
        },

        unexpand: function() {
            var $this = $(this), childs = $('ul.wfm-tree', $this.parent());

            childs.remove();
            $this.removeClass('wfm-unexpand');
            $this.addClass('wfm-expand');

            WFM.appendParentHash($this);
            return false;
        },

        appendChilds: function(parentAnchor, childs) {
            var container = parentAnchor.parent();
            container.append(childs);

            parentAnchor.removeClass('wfm-expand');
            parentAnchor.addClass('wfm-unexpand');
        },

        appendHash: function(href) {
            window.location.hash = href.split('=')[1];
        },

        appendParentHash: function(anchor) {
            var parentContainer = anchor.parent('li').parent('ul.wfm-tree').parent('li');
            if (parentContainer.length > 0) {
                parentAnchor = $('a', parentContainer);
                this.appendHash(parentAnchor.attr('href'));
            } else {
                window.location.hash = '';
            }
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