package controllers;

import play.cache.Cache;
import play.mvc.Controller;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import jp.co.flect.excel2canvas.ExcelToCanvasBuilder;
import jp.co.flect.excel2canvas.ExcelToCanvas;
import jp.co.flect.hypdf.HyPDF;
import jp.co.flect.hypdf.model.PdfResponse;
import jp.co.flect.hypdf.model.HyPDFOption;
import jp.co.flect.hypdf.model.LengthUnit;


public class Application extends Controller {

	public static void index() {
		String sessionId = session.getId();
		render(sessionId);
	}

	public static void preview(File file) {
		String sessionId = session.getId();
		String result = null;
		try {
			ExcelToCanvasBuilder builder = new ExcelToCanvasBuilder();
			builder.getFontManager().setMinchoFont("ipaexm");
			builder.getFontManager().setGothicFont("ipaexg");
			builder.setIncludeChart(true);
			builder.setIncludePicture(true);

			ExcelToCanvas canvas = builder.build(file);
			result = canvas.toJson();
			Cache.set(sessionId, result);
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", e.getMessage());
			result = new Gson().toJson(map);
		}
		render(result);
	}

	public static void download() {
		String id = session.getId();
		String json = (String)Cache.get(id);
		if (json == null) {
			notFound();
		}
		String username = System.getenv("HYPDF_USER");
		String password = System.getenv("HYPDF_PASSWORD");

		HyPDF hypdf = new HyPDF(username, password);
		HyPDFOption.HtmlToPdf option = new HyPDFOption.HtmlToPdf();
		option.footer = new HyPDFOption.Footer();
		option.footer.center = request.getBase();
		option.margin_top = LengthUnit.inch(0.25);
		option.margin_bottom = LengthUnit.inch(0.25);

		hypdf.setTestMode(true);
		String url = request.getBase() + "/topdf/" + id;
		try {
			PdfResponse res = hypdf.htmlToPdf(url, option);
			renderBinary(res.getContent(), "sample.pdf");
		} catch (IOException e) {
			error(e.getMessage());
		}
	}

	public static void toPdf(String id) {
		String json = (String)Cache.get(id);
		if (json == null) {
			notFound();
		}
		render(json);
	}

}