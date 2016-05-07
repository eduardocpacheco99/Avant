


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
public List<clienteBean> getClientes() throws SQLException{
// recupera toda a base de TrainingSet
	Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
	String sql = "SELECT *  FROM cadastros_sorocred;";
	List <clienteBean> clientes = new ArrayList();
	clienteBean cliente = new clienteBean();
	
	ResultSet rs = stmt.executeQuery(sql);
    while(rs.next()){
    	cliente = new clienteBean();
    	
        //Retrieve by column name
        cliente.setCpf(rs.getString("cpf"));
        clientes.add(cliente);
}
    return clientes;

}



}
