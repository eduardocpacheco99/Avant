import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

public class AvanteVcAguardandoInstalacao extends ExtratorWebsite {
	public String codigoSerialMaquina;

	public AvanteVcAguardandoInstalacao(String cpf, String codigoSerialMaquina) throws IOException {
		this.codigoSerialMaquina = codigoSerialMaquina;
		// Entra no site para pegar o cookie inicial
		ExtratorWebsiteStep stepInicial = new ExtratorWebsiteStep(
				"https://avante.com/painel/acesso",
				new HashMap<String, String>(), "GET", false);
		stepInicial
				.setParser(new AvanteVcAguardandoInstalacaoLoginParser(this));
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
		System.out.println("aqui");
		ExtratorWebsiteStep stepOrders = new ExtratorWebsiteStep(
				"https://avante.com/intranet/microcredits_xls?action=report&controller=intranet%2Forders&format=xls",
				new HashMap<String, String>(), "GET", false);
		stepOrders.setParser(new AvanteVcAguardandoInstalacaoProtecaoParser(
				this));
		steps.add(stepOrders);

		ExtratorWebsiteStep stepConfirmacaoInstalacao = new ExtratorWebsiteStep(
				"MODIFICADO_PELO_STEP_ANTERIOR", new HashMap<String, String>(), "POST_MULTIPART", true);
		steps.add(stepConfirmacaoInstalacao);
	}
}
