import java.util.Map;

public class ExtratorWebsiteSorocredSaraivaLoginParser extends
		ExtratorWebsiteStepParser {
	ExtratorWebsiteSorocredSaraiva parent;

	public ExtratorWebsiteSorocredSaraivaLoginParser(
			ExtratorWebsiteSorocredSaraiva parent) {
		this.parent = parent;
	}
	
	public String getToken(String source, String start, String finish) {
		int posStart = source.indexOf(start) + start.length();
		int posFinish = source.substring(posStart).indexOf(finish) + posStart;
		return source.substring(posStart, posFinish);
	}
	
	@Override
	public void parse(String html) {
		// pega o próximo passo (segundo passo) para adicionar os parâmetros
		Map<String, String> params = this.parent.getSteps().get(1).getParams();
		params.put("__VIEWSTATE", getToken(html, "id=\"__VIEWSTATE\" value=\"", "\""));
		params.put("__EVENTVALIDATION", getToken(html, "id=\"__EVENTVALIDATION\" value=\"", "\""));
	}
}
