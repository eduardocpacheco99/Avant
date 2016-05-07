


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class clienteDAO {
public  clienteDAO(){

}
public void addBF(String a) throws SQLException{
	// adiciona uma lista de demographicModelBeans à base de trainingSet	
		 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
			 
	//	for (clienteBean demo : listdemo){
		
			String sql = "INSERT INTO microcredito.bruteForceS3  (caminho)VALUES('" + a+  "')"; 
//			System.out.println(sql);
		
			stmt.executeUpdate(sql);     
			ConexaoSQL.FecharConexao();  
		//}
	}

public void addClientes(List<clienteBean> listdemo) throws SQLException{
// adiciona uma lista de demographicModelBeans à base de trainingSet	
	 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
		 
	for (clienteBean demo : listdemo){
	
		String sql = "INSERT INTO microcredito.parcelas_pagas_omni_processadas (id, cpf, parcela)VALUES(0, '" + demo.getCpf() + "', '" + demo.getParcela()+  "')"; 
//		System.out.println(sql);
	
		stmt.executeUpdate(sql);     
		ConexaoSQL.FecharConexao();  
	}
}
public void updateClientes(List<clienteBean> listdemo) throws SQLException{
	// adiciona uma lista de demographicModelBeans à base de trainingSet	
		 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
			 
		for (clienteBean demo : listdemo){
			String sql = "UPDATE  cadastros_sorocred set status ='"+demo.getStatus()+"', nome='"+demo.getNome()+"' , limite='"+demo.getLimite() +"'  where cpf = '"+ demo.getCpf()+"';"; 
			stmt.executeUpdate(sql);     
			ConexaoSQL.FecharConexao();  
		}
	}
public List<clienteBean> getClientes() throws SQLException{
// recupera toda a base de TrainingSet
	Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
	String sql = "select distinct cpf, parcela from microcredito.parcelas_pagas_omni where cpf is not null and parcela is not null  "
    +" and concat(cpf,'_',parcela) not in (select concat(cpf,'_',parcela) from microcredito.parcelas_pagas_omni_processadas)";
	List <clienteBean> clientes = new ArrayList();
	clienteBean cliente = new clienteBean();
	
	ResultSet rs = stmt.executeQuery(sql);
    while(rs.next()){
    	cliente = new clienteBean();
    	
        //Retrieve by column name
        cliente.setCpf(rs.getString("cpf"));
        cliente.setParcela(rs.getInt("parcela"));
        clientes.add(cliente);
}
    return clientes;

}



}
