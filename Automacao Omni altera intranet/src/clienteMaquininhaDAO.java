


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class clienteMaquininhaDAO {
public  clienteMaquininhaDAO(){

}
public void addClientes(List<clienteBean> listdemo) throws SQLException{
// adiciona uma lista de demographicModelBeans à base de trainingSet	
	 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
		 
	for (clienteBean demo : listdemo){
	
		String sql = "INSERT INTO cadastros_sorocred (id, cpf, nome, status,limite, created_at)VALUES(0, '" + demo.getCpf() + "', '" + demo.getNome()+ "', '"+ demo.getStatus()+ "','"+demo.getLimite()+"', '"+demo.getData()+ "')"; 
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
public List<clienteMaquininhaBean> getClientes() throws SQLException{
// recupera toda a base de TrainingSet
	Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
	String sql = "select distinct A.stonecode,cpf from planilha_stone_instalacao_maquininha A inner join leads B on A.stonecode = B.barcode  where A.instalacao = 'Entregue' and B.order_state = 'aguardando_instalacao'";
	List <clienteMaquininhaBean> clientes = new ArrayList();
	clienteMaquininhaBean cliente = new clienteMaquininhaBean();
	
	ResultSet rs = stmt.executeQuery(sql);
    while(rs.next()){
    	cliente = new clienteMaquininhaBean();
    	
        //Retrieve by column name
        cliente.setCpf(rs.getString("cpf"));
        cliente.setStonecode(rs.getString("stonecode"));
        clientes.add(cliente);
}
    return clientes;

}



}
