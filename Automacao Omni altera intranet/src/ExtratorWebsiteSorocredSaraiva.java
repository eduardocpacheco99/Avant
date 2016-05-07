import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtratorWebsiteSorocredSaraiva extends ExtratorWebsite {
	public ExtratorWebsiteSorocredSaraiva(List<String> datas) {
		// Entra no site para pegar o cookie inicial
		ExtratorWebsiteStep stepInicial = new ExtratorWebsiteStep(
				"http://online.sorocred.com.br/sad/Login_novo.aspx",
				new HashMap<String, String>(), "GET", false);
		stepInicial.setParser(new ExtratorWebsiteSorocredSaraivaLoginParser(
				this));
		steps.add(stepInicial);

		// Adiciona o primeiro Step, que seria a autenticação
		// Como não irá guardar o resultado desta página, passa false como
		// parâmetro
		Map<String, String> paramsLogin = new HashMap<String, String>();
		paramsLogin.put("cmbOpcao", "6");
		paramsLogin.put("txtLojistaCodigo", "176974");
		paramsLogin.put("txtLojistaUsuario", "Saraiva");
		paramsLogin.put("txtLojistaSenha", "314866");
		paramsLogin.put("btnLojista.x", "30");
		paramsLogin.put("btnLojista.y", "5");
		paramsLogin.put("__EVENTTARGET", "");
		paramsLogin.put("__EVENTARGUMENT", "");
		paramsLogin.put("__LASTFOCUS", "");

		ExtratorWebsiteStep stepLogin = new ExtratorWebsiteStep(
				"http://online.sorocred.com.br/sad/Login_novo.aspx",
				paramsLogin, "POST", false);
		stepLogin.setParser(new ExtratorWebsiteSorocredSaraivaProtecaoParser(
				this));
		steps.add(stepLogin);

		// Esse Step no site eles usam para proteger de robôs, aparentemente
		ExtratorWebsiteStep stepProtecao = new ExtratorWebsiteStep(
				"VAI-SER-MODIFICADO-NO-PARSE-DO-LOGIN", new HashMap<String, String>(), "GET",
				false);
		steps.add(stepProtecao);

		// Adiciona os steps que buscam o crédito por data
		// Como irá guardar o resultado, passa true como último param
		for (String data : datas) {
			// url que queremos
			Map<String, String> paramsCredWeb = new HashMap<String, String>();
			paramsCredWeb.put("p_valor_script", data);
			ExtratorWebsiteStep stepData = new ExtratorWebsiteStep(
					"http://online.sorocred.com.br/bl/scard_credweb.aspx",
					paramsCredWeb, "GET", true);
			steps.add(stepData);
		}
	}
}
