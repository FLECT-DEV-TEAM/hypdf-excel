if (typeof(flect) == "undefined") flect = {};
if (typeof(flect.app) == "undefined") flect.app = {};


(function ($) {
	flect.app.ExcelToPdf = function() {
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
		}
		function preview(ret) {
			console.log(ret);
			if (ret.error) {
				alert(ret.error);
			} else {
				$excel.excelToCanvas(ret);
				$preview.show();
			}
		}
		var $btnUpload = $("#forms-btnUpload"),
			$file = $("#forms-file"),
			$form = $("#forms-form"),
			$preview = $("#preview"),
			$btnDownload = $("#preview-btnDownload"),
			$excel = $("#preview-excel");
		init();

		$.extend(this, {
			"preview" : preview
		});
	}
})(jQuery);
