import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvanteVcPagamentosRealizados extends ExtratorWebsite {
	public List<Integer> pagamentos;
	
	public AvanteVcPagamentosRealizados(String cpf, List<Integer> pagamentos) {
		this.pagamentos = pagamentos;
		// Entra no site para pegar o cookie inicial
		ExtratorWebsiteStep stepInicial = new ExtratorWebsiteStep(
				"https://avante.com/painel/acesso",
				new HashMap<String, String>(), "GET", false);
		stepInicial
				.setParser(new AvanteVcPagamentosRealizadosLoginParser(this));
		steps.add(stepInicial);

		// Adiciona o primeiro Step, que seria a autenticação
		// Como não irá guardar o resultado desta página, passa false como
		// parâmetro
		Map<String, String> paramsLogin = new HashMap<String, String>();
		paramsLogin.put("utf8", "%E2%9C%93");
		paramsLogin.put("user[email]", "eduardo.pacheco@avante.com.vc");
		paramsLogin.put("user[password]", "avante123");
		paramsLogin.put("commit", "Entrar");

		ExtratorWebsiteStep stepLogin = new ExtratorWebsiteStep(
				"https://avante.com/painel/acessar", paramsLogin, "POST", false);
		steps.add(stepLogin);

		// pegando o auth token e a próxima URL
		ExtratorWebsiteStep stepOrders = new ExtratorWebsiteStep(
				"https://avante.com/intranet/orders?utf8=%E2%9C%93&q[order_state_eq_any][]=&q[product_asset_fsp_product_type_asset_id_eq_any][]=&q[product_asset_fsp_product_type_product_type_id_eq_any][]=&q[product_asset_fsp_product_type_product_type_id_eq_any][]=2&q[transactions_payment_type_name_cont_any][]=&q[product_asset_fsp_product_type_fsp_id_eq_any][]=&q[sale_user_id_eq_any][]=&q[created_at_gteq]=&q[created_at_lteq]=&q[closing_date_gteq]=&q[closing_date_lteq]=&q[expiration_date_gteq]=&q[expiration_date_lteq]=&q[user_cpf_or_user_name_or_number_cont]="+ cpf + "&commit=Filtrar",
				new HashMap<String, String>(), "GET", false);
		stepOrders.setParser(new AvanteVcPagamentosRealizadosProtecaoParser(
				this));
		steps.add(stepOrders);

		ExtratorWebsiteStep stepConfirmacaoPagamento = new ExtratorWebsiteStep(
				"MODIFICADO_PELO_STEP_ANTERIOR", new HashMap<String, String>(), "POST_MULTIPART", true);
		steps.add(stepConfirmacaoPagamento);
	}
}
