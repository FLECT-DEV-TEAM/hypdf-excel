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


public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void preview(File file) {
		String sessionId = session.getId();
		String result = null;
		try {
			ExcelToCanvasBuilder builder = new ExcelToCanvasBuilder();
			builder.getFontManager().setMinchoFont("ipaexm");
			builder.getFontManager().setGothicFont("ipaexg");

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
		hypdf.setTestMode(true);
		String url = request.getBase() + "/topdf/" + id;
		System.out.println(url);
		try {
			PdfResponse res = hypdf.htmlToPdf(url);
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