import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtratorWebsiteSorocredSaraiva extends ExtratorWebsite {
	public ExtratorWebsiteSorocredSaraiva(List<String> datas) {
		// Entra no site para pegar o cookie inicial
		ExtratorWebsiteStep stepInicial = new ExtratorWebsiteStep(
				"https://avante.com/painel/acesso/",
				new HashMap<String, String>(), "GET", false);
		stepInicial.setParser(new ExtratorWebsiteSorocredSaraivaLoginParser(
				this));
		steps.add(stepInicial);

		// Adiciona o primeiro Step, que seria a autenticação
		// Como não irá guardar o resultado desta página, passa false como
		// parâmetro
		Map<String, String> paramsLogin = new HashMap<String, String>();
		paramsLogin.put("user_email", "eduardo.pacheco@avante.com.vc");
		paramsLogin.put("user_password", "avante123");
		paramsLogin.put("autencity_token", "6pLJF5QR3zr/kkgRivZLh6AD9xc3whEmoqkSiWEojAjY7x0lAcsUOEN10t8GxH7r9ZsDzhDbHavMH5lNaaR9MA==");
	
		ExtratorWebsiteStep stepLogin = new ExtratorWebsiteStep(
				"https://avante.com/painel/acesso/",
				paramsLogin, "GET", false);
	//	stepLogin.setParser(new ExtratorWebsiteSorocredSaraivaProtecaoParser(
	//			this));
		stepInicial.setParser(new ExtratorWebsiteSorocredSaraivaLoginParser(
				this));

		steps.add(stepLogin);
System.out.println("aqui");
		// Esse Step no site eles usam para proteger de robôs, aparentemente//
	//	ExtratorWebsiteStep stepProtecao = new ExtratorWebsiteStep(
		//		"VAI-SER-MODIFICADO-NO-PARSE-DO-LOGIN", new HashMap<String, String>(), "GET",
			//	false);
	//	steps.add(stepProtecao);

		// Adiciona os steps que buscam o crédito por data
		// Como irá guardar o resultado, passa true como último param
		for (String data : datas) {
			// url que queremos
			Map<String, String> paramsCredWeb = new HashMap<String, String>();
			paramsCredWeb.put("q_user_cpf_or_user_name_or_number_cont", "58445447491");
			ExtratorWebsiteStep stepData = new ExtratorWebsiteStep(
					"https://avante.com/intranet/orders/",
					paramsCredWeb, "GET", true);
			steps.add(stepData);
		}
	}
}
