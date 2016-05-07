import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvanteVcPagamentosRealizadosProtecaoParser extends
		ExtratorWebsiteStepParser {
	AvanteVcPagamentosRealizados parent;

	public AvanteVcPagamentosRealizadosProtecaoParser(
			AvanteVcPagamentosRealizados parent) {
		this.parent = parent;
	}

	public String getToken(String source, String start, String finish) {
		int posStart = source.indexOf(start) + start.length();
		int posFinish = source.substring(posStart).indexOf(finish) + posStart;
		return source.substring(posStart, posFinish);
	}

	@Override
	public void parse(String html) {
		List<Integer> pagamentos = parent.pagamentos;
		String token = getToken(
				html,
				"<meta name=\"csrf-token\" content=\"",
				"\"");
		String url = getToken(
				html,
				"id=\"new_intranet_order_form\" enctype=\"multipart/form-data\" action=\"",
				"\"");
		url = "https://avante.com" + url;
		// pega todas as opções de pagamento da combobox para fazer
		// um parse e saber quais já estavam selecionadas.
		// As opções que já estavam selecionadas são colocadas nos pagamentos
		// que irão para o POST
		String strPagamentosAnteriores = getToken(
				html,
				"multiple=\"multiple\" name=\"intranet_order_form[paid_mc_parcels][]\" id=\"intranet_order_form_paid_mc_parcels\">",
				"</select>");
		String[] pagamentosAnteriores = strPagamentosAnteriores
				.split("</option>");
		for (String opcaoPagamento : pagamentosAnteriores) {
			boolean estavaSelecionada = opcaoPagamento.toLowerCase().contains(
					"selected=\"selected\"");
			String strNumeroPagamento = getToken(opcaoPagamento, "value=\"",
					"_");
			if (estavaSelecionada) {
				try {
					Integer numeroPagamento = Integer
							.parseInt(strNumeroPagamento);
					if (!pagamentos.contains(numeroPagamento))
						pagamentos.add(numeroPagamento);
				} catch (Exception e) {
					// ignora, deveria ir pra um log4j da vida
					System.out.println("numero pagamento invalido: "
							+ strNumeroPagamento);
				}
			}
		}

		// pega o próximo passo (quarto passo) para mudar URL
		this.parent.getSteps().get(3).setUrl(url);
		// pega o próximo passo (quarto passo) para adicionar os parâmetros
		Map<String, String> params = this.parent.getSteps().get(3).getParams();
		Map<String, List<String> > paramsMulti = this.parent.getSteps().get(3).getParamsMulti();
		
		params.put("utf8", "â");
		params.put("authenticity_token", token);
		params.put("commit", "Salvar");
		
		// coloca os pagamentos
		List<String> paramPagamentos = new ArrayList<String>();
		for (Integer numeroPagamento : pagamentos) {
			paramPagamentos.add(numeroPagamento + "_parcel_paid");
		}
		paramsMulti.put("intranet_order_form[paid_mc_parcels][]", paramPagamentos);
		
	}
}
