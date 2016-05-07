import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

public class Main {



	public static void main(String[] args) throws Exception {
	clienteMaquininhaDAO clientedao = new clienteMaquininhaDAO();
		
	avanteVcAguardandoInstalacao(clientedao.getClientes());
	}

	public static void avanteVcPagamentosRealizados() throws Exception {
		System.out.println("Start avanteVcPagamentosRealizados");
		
		// ExtratorInformacoesURL é o 'motor' que fará a extração de dados, bem burro.
		ExtratorInformacoesURL extrator = new ExtratorInformacoesURL();
		// parametros: cpf, pagamentos a serem realizados
		List<Integer> pagamentos = new ArrayList<Integer>();
		pagamentos.add(7);
		pagamentos.add(8);
		extrator.setWebsite(new AvanteVcPagamentosRealizados("03538450439", pagamentos));
		// O método run irá retornar a última página extraída.
		List<String> resultado = extrator.run();
		for (String strResultado : resultado) {
			System.out.println("*----------------------*");
			System.out.println(strResultado);
		}
		
		
		System.out.println("*----------------------*");
		System.out.println("Finish avanteVcPagamentosRealizados");
	}
	
	public static void avanteVcAguardandoInstalacao(List<clienteMaquininhaBean> a) throws Exception {
		System.out.println("Start avanteVcAguardandoInstalacao");
		// ExtratorInformacoesURL é o 'motor' que fará a extração de dados, bem burro.
		ExtratorInformacoesURL extrator = new ExtratorInformacoesURL();
		// parametros: cpf, código serial da máquina
		for(clienteMaquininhaBean b : a){
		extrator.setWebsite(new AvanteVcAguardandoInstalacao(b.getCpf(), b.getStonecode()));
		
		// O método run irá retornar a última página extraída.
		List<String> resultado = extrator.run();
	
		for (String strResultado : resultado) {
			System.out.println("*----------------------*");
			System.out.println(strResultado);
		}
		
		}
		System.out.println("*----------------------*");
		System.out.println("Finish avanteVcAguardandoInstalacao");
	}
		

	@SuppressWarnings("deprecation")
	public static void soroCred() throws Exception {
		System.out.println("Start");

		// ExtratorInformacoesURL é o 'motor' que fará a extração de dados, bem burro.
		ExtratorInformacoesURL extrator = new ExtratorInformacoesURL();

		 List<String> jaColocados = new ArrayList<String>(); 

		 // A implementalão da classe 'ExtratorWebsite' é o que contém as informações da Extração propriamente dita
		// Daria pra incrementar ainda mais a classe Website e especializá-la a ponto de fazer o parse
		List<String> datas = new ArrayList<String>();

	
		for(int i=0; i<=6; i++){
			Date d = new Date(); //data atual
			Date d1 = d; //data atual
		//	System.out.println(d);
			SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );   

			Calendar c = Calendar.getInstance(); 
			c.setTime(d); 
		
			c.add( Calendar.DATE, -i );  
			String convertedDate=dateFormat.format(c.getTime());  
			//d1 = c.getTime();
				System.out.println(convertedDate);
		//	System.out.println(d1);
				datas.add(convertedDate);
		}
		
		extrator.setWebsite(new ExtratorWebsiteSorocredSaraiva(datas));
		List <clienteBean> clientes = new ArrayList();
		List <clienteBean> insert = new ArrayList();
		List <clienteBean> update = new ArrayList();
		
		clienteBean cliente ;
		// O método run irá retornar a última página extraída.
		List<String> resultado = extrator.run();
		for (String strResultado : resultado) {
//			System.out.println("*----------------------*");
	//		System.out.println(strResultado);
		//		<input name="ctl00$ContentPlaceHolderPrincipal$txtInicio" t.spliype="text" value="14/07/2015" maxlength="10" readonly="readonly" id="ctl00_ContentPlaceHolderPrincipal_txtInicio" class="DetalheCabecalho" style="border-width:0px;width:89px;">
			

			try{
	//			System.out.println(	strResultado.split("p_valor_script=")[1].split(" ")[0].replace("%2f", "/").replace("\"", ""));
			String ano = strResultado.split("p_valor_script=")[1].split(" ")[0].replace("%2f", "/").replace("\"", "").split("/")[2];
			String mes = strResultado.split("p_valor_script=")[1].split(" ")[0].replace("%2f", "/").replace("\"", "").split("/")[1];
			String dia = strResultado.split("p_valor_script=")[1].split(" ")[0].replace("%2f", "/").replace("\"", "").split("/")[0];
		
			String dados;
			
			dados = strResultado.split("Ficha</font></th>")[1].split("</table>")[0];
//		System.out.println(dados);
			for(int i = 1; i < dados.split("tr class").length; i++){
				cliente = new clienteBean();			
				cliente.setData(ano+"-"+mes+"-"+dia);		
				cliente.setCpf(dados.split("tr class")[i].split("<font size=\"3\">")[1].split("</font>")[0]);
		cliente.setNome(dados.split("tr class")[i].split("<font size=\"3\">")[2].split("</font>")[0]);
		cliente.setLimite(dados.split("tr class")[i].split("<font size=\"3\">")[5].split("</font>")[0]);
		cliente.setStatus(dados.split("tr class")[i].split("<font size=\"3\">")[7].split("</font>")[0].replace("&#225;","a"));
	//	System.out.println(dados);
		clientes.add(cliente);
	//	System.out.println("Na data "+ cliente.getData()+ " adicionou o cpf "+ cliente.getCpf() );
		//String sql = "INSERT INTO cadastros_sorocred (id, cpf, nome, status,limite, created_at)VALUES(0, '" + cliente.getCpf() + "', '" + cliente.getNome()+ "', '"+ cliente.getStatus()+ "','"+cliente.getLimite()+"', '"+cliente.getData()+ "')"; 
		//System.out.println(sql);
			}
			}catch(Exception e){}
					}
	//aqui
		clienteDAO dao = new clienteDAO();
		List <clienteBean> clientesNaBase =	dao.getClientes();
		
		//2 compara as duas listas
		
		for (clienteBean cliBase : clientesNaBase){
			jaColocados.add(cliBase.getCpf());		
		}

		for(clienteBean cli : clientes){
			if( jaColocados.contains(cli.getCpf().trim())){
//atualiza
				update.add(cli);
			}
			}
	dao.updateClientes(update);

		
		for(clienteBean cli : clientes){
			System.out.println(cli.getCpf());
			if( !jaColocados.contains(cli.getCpf().trim())){
				// adiciona
					insert.add(cli);
					jaColocados.add(cli.getCpf());
				}
				}
		System.out.println("clientes "+clientes.size() + " insert" +insert.size());
//		System.out.println(update.size());
		
		dao.addClientes(insert);
		

		
		

		System.out.println("*----------------------*");
		System.out.println("Finish");
		// 1) pega todos os clientes da base

		
				}
	
}
