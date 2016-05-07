
public class ExtratorWebsiteSorocredSaraivaProtecaoParser extends
		ExtratorWebsiteStepParser {
	ExtratorWebsiteSorocredSaraiva parent;

	public ExtratorWebsiteSorocredSaraivaProtecaoParser(
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
		// aparentemente eles protegem de robôs colocando um redirecionamento javascript,
		// por este motivo o faremos de maneira manual >:)
		String url = getToken(html, "window.top.location = \"", "\";//]]");
		
		// pega o próximo passo (terceiro passo) para adicionar a url de proteção
		this.parent.getSteps().get(2).setUrl(url);
	}
}
