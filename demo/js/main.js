$(document).bind('touchend', function () {
    forge.internal.call('multi_image_select.getImages', {}, function(results) {
        for (var file in results) {
            forge.file.URL(results[file], function (url) {
                var img = $("<img />").attr({ "src": url }).css({ "max-width": "80%" });
                img.appendTo("#selection");
            });
        }
    }, function(e) { 
        alert("failed: " + e.message);
    });
});
