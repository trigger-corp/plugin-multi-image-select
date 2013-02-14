$(function () {
    function showImage (file, extraCss) {
        extraCss = extraCss === undefined? {}: extraCss;
        forge.file.URL(file, function (url) {
            var img = $("<img />").attr({ "src": url }).css({ "max-width": "30%" }).css(extraCss);
            $('#images').prepend(img);
        });
    }
    function dumpImageData (file, logTag) {
        forge.file.isFile(file, function (isFile) {
            forge.logging.info(logTag + " " + JSON.stringify(file)+" is file: "+isFile);
        });
    }
    $('a.multi').on('click', function () {
        forge.internal.call('multi_image_select.getImages', {}, function(results) {
            for (var idx=0; idx<results.length; idx++) {
                var file = results[idx];
                showImage(file, {'border': '2px dotted red'});
                dumpImageData(file, "multi");
            }
        }, function (e) {
            alert("plugin failed: " + e.message);
        });

    });
    $('a.getImage').on('click', function () {
        forge.file.getImage(function (file) {
            showImage(file, {'border': '2px dotted green'});
            dumpImageData(file, "getImage");

        }, function (e) {
            alert("file.getImage failed: "+e.message);
        });
    });
});
