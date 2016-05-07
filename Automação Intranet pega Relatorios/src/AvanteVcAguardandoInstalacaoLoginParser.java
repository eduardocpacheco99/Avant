import java.util.Map;

public class AvanteVcAguardandoInstalacaoLoginParser extends
		ExtratorWebsiteStepParser {
	AvanteVcAguardandoInstalacao parent;

	public AvanteVcAguardandoInstalacaoLoginParser(
			AvanteVcAguardandoInstalacao parent) {
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
		params.put("authenticity_token", getToken(html, "<input type=\"hidden\" name=\"authenticity_token\" value=\"", "\""));
	}
}
