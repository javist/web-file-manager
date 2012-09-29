$('.wfm-expand').live('click', function() {
	var $this = $(this), href = $this.attr('href');
    $.ajax({
    	url: href,
    	success: function(resp) {
    		var child = $('ul', $(resp));
    		$this.parent().append(child);

    		$this.removeClass('wfm-expand');
    		$this.addClass('wfm-unexpand');
    	}
    });
    return false;
});

$('.wfm-unexpand').live('click', function() {
	var $this = $(this);
	$('ul', $this.parent()).remove();

	$this.removeClass('wfm-unexpand');
	$this.addClass('wfm-expand');
    return false;
});