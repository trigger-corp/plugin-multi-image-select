$(document).bind('touchstart', function () {
    forge.internal.call('multi_image_select.getImages', {}, function(results) {
        for (var file in results) {
            forge.file.URL(results[file], function (url) {
                var img = $("<img />").attr({ "src": url, "width": "25%", "height": "25%" });
                img.appendTo("#selection");
            });
        }
    }, function(e) { 
        alert("failed: " + e.message);
    });
});
