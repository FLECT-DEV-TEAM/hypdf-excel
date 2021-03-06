if (typeof(flect) == "undefined") flect = {};
if (typeof(flect.app) == "undefined") flect.app = {};


(function ($) {
	flect.app.ExcelToPdf = function(sessionId) {
		function init() {
			$btnUpload.click(function() {
				$file.val(null).click();
			});
			$file.change(function() {
				var filename = $file.val();
				if (filename) {
					$form[0].submit();
				}
			});
			$btnDownload.click(function() {
				window.open("/download");
			});
			$btnFull.click(function() {
				window.open("/topdf/" + sessionId);
			});
		}
		function preview(ret) {
			console.log(ret);
			if (ret.error) {
				alert(ret.error);
			} else {
				$preview.show();
				$excel.empty();
				$excel.excelToCanvas(ret);
			}
		}
		var $btnUpload = $("#forms-btnUpload"),
			$file = $("#forms-file"),
			$form = $("#forms-form"),
			$preview = $("#preview"),
			$btnDownload = $("#preview-btnDownload"),
			$btnFull = $("#preview-btnFull"),
			$excel = $("#preview-excel");
		init();

		$.extend(this, {
			"preview" : preview
		});
	}
})(jQuery);
