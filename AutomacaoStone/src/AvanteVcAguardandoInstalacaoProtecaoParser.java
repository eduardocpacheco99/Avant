import java.util.Map;


public class AvanteVcAguardandoInstalacaoProtecaoParser extends
		ExtratorWebsiteStepParser {
	AvanteVcAguardandoInstalacao parent;

	public AvanteVcAguardandoInstalacaoProtecaoParser(
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
		String token = getToken(html, "<meta name=\"csrf-token\" content=\"", "\""); // FAZK/PNQxxqS049tVpGXwUV/fpCWq1aj6nNCPyP6JRE3ov9jMlNMrLDABobyJecRJ1CezMdyR68Ck538nw2P/Q==
		String serial = parent.codigoSerialMaquina;
		String url = getToken(html, "id=\"new_intranet_order_form\" enctype=\"multipart/form-data\" action=\"", "\"");
		url = "https://avante.com" + url;
//		System.out.println(url);
		
		// pega o próximo passo (quarto passo) para mudar URL
		this.parent.getSteps().get(3).setUrl(url);
		// pega o próximo passo (quarto passo) para adicionar os parâmetros
		Map<String, String> params = this.parent.getSteps().get(3).getParams();
		params.put("utf8", "â");
		params.put("authenticity_token", token);
		params.put("intranet_order_form[serial_code]", serial);
		params.put("commit", "Salvar");
	}
}
