package vc.com.avante.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import vc.com.avante.*;
public class demographicModelDAO {
public  demographicModelDAO(){

}
public void addTrainingSet(List<demographicModelBean> listdemo) throws SQLException{
// adiciona uma lista de demographicModelBeans � base de trainingSet	
	 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
		 
	for (demographicModelBean demo : listdemo){
		String sql = "INSERT INTO trainingsetInadimplenciaMicrocredito (id, estadocivil, finalidade, importancianegocio, escolaridade, operacao, setor,sexo,situacaoepresa,valorparcela,adimplencia))VALUES('"+
				demo.getId()+ "', '" + demo.getEstadocivil() + "', '" + demo.getFinalidade()+ "', '"+ demo.getImportancianegocio()+ "', '"+demo.getEscolaridade()+ "', '"+ demo.getOperacao()+ "', '"+demo.getSetor()+ "', '"+demo.getSexo()+ "', '"+demo.getSituacaoempresa()+ "', '"+demo.getValorparcela()+ "', '"+demo.getAdimplencia()+ "')"; 
		List <demographicModelBean> trainingSet = new ArrayList();			
		stmt.executeUpdate(sql);     
		ConexaoSQL.FecharConexao();  
	}
}

public void addTestSet(List<demographicModelBean> listdemo) throws SQLException{
	// adiciona uma lista de demographicModelBeans � base de trainingSet	
		 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
			 
		for (demographicModelBean demo : listdemo){
			String sql = "INSERT INTO testsetInadimplenciaMicrocredito (id, estadocivil, finalidade, importancianegocio, escolaridade, operacao, setor,sexo,situacaoepresa,valorparcela,adimplencia))VALUES('"+
					demo.getId()+ "', '" + demo.getEstadocivil() + "', '" + demo.getFinalidade()+ "', '"+ demo.getImportancianegocio()+ "', '"+demo.getEscolaridade()+ "', '"+ demo.getOperacao()+ "', '"+demo.getSetor()+ "', '"+demo.getSexo()+ "', '"+demo.getSituacaoempresa()+ "', '"+demo.getValorparcela()+ "', '"+demo.getAdimplencia()+ "')"; 
			List <demographicModelBean> trainingSet = new ArrayList();			
			stmt.executeUpdate(sql);     
			ConexaoSQL.FecharConexao();  
		}
	}
public void evaluateCandidates(List<demographicModelBean> listdemo) throws SQLException{
	// adiciona uma lista de demographicModelBeans � base de trainingSet	
		 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
			 
		for (demographicModelBean demo : listdemo){
			String sql = "INSERT INTO scoreDemografico (cpf, score ) VALUES('"+
					demo.getCpf()+ "', '" + demo.getScore() + "')"; 
			List <demographicModelBean> trainingSet = new ArrayList();			
			stmt.executeUpdate(sql);     
			ConexaoSQL.FecharConexao();  
		}
	}

public void evaluateCandidate(demographicModelBean demo) throws SQLException{
	// adiciona uma lista de demographicModelBeans � base de trainingSet	
		 Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
			 
		System.out.println("salvando "+demo.getCpf()+" score "+ demo.getScore());
			String sql = "INSERT INTO scoreDemografico (cpf, score ) VALUES('"+
					demo.getCpf()+ "', '" + demo.getScore() + "')"; 
			stmt.executeUpdate(sql);     
			ConexaoSQL.FecharConexao();  
		}
	


public List<demographicModelBean> getTrainingSet() throws SQLException{
// recupera toda a base de TrainingSet
	Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
	String sql = "SELECT *  FROM trainingsetInadimplenciaMicrocredito";
	List <demographicModelBean> trainingSet = new ArrayList();
	demographicModelBean demo = new demographicModelBean();
	
	ResultSet rs = stmt.executeQuery(sql);
    while(rs.next()){
    	demo = new demographicModelBean();
    	
        //Retrieve by column name
        demo.setId(rs.getInt("id"));
        demo.setAdimplencia(rs.getString("adimplencia"));
        demo.setEscolaridade(rs.getString("escolaridade"));
        demo.setEstadocivil(rs.getString("estadocivil"));
        demo.setFinalidade(rs.getString("finalidade"));
        demo.setImportancianegocio(rs.getString("importancianegocio"));
        demo.setOperacao(rs.getString("operacao"));
        demo.setSetor(rs.getString("setor"));
        demo.setSexo(rs.getString("sexo"));
        demo.setSituacaoempresa(rs.getString("situacaoempresa"));
        demo.setValorcontrato(rs.getString("valorcontrato"));
        demo.setValorparcela(rs.getString("valorparcela"));
        trainingSet.add(demo);
}
    return trainingSet;

}
public List<demographicModelBean> getTestSet() throws SQLException{
// recupera toda a base de testset	
	Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
	String sql = "SELECT *  FROM testInadimplenciaMicrocredito";
	List <demographicModelBean> testSet = new ArrayList();
	demographicModelBean demo = new demographicModelBean();
	
	ResultSet rs = stmt.executeQuery(sql);
    while(rs.next()){
        //Retrieve by column name
        demo.setId(rs.getInt("id"));
        demo.setAdimplencia(rs.getString("adimplencia"));
        demo.setEscolaridade(rs.getString("escolaridade"));
        demo.setEstadocivil(rs.getString("estadocivil"));
        demo.setFinalidade(rs.getString("finalidade"));
        demo.setImportancianegocio(rs.getString("importancianegocio"));
        demo.setOperacao(rs.getString("operacao"));
        demo.setSetor(rs.getString("setor"));
        demo.setSexo(rs.getString("sexo"));
        demo.setSituacaoempresa(rs.getString("situacaoempresa"));
        demo.setValorcontrato(rs.getString("valorcontrato"));
        demo.setValorparcela(rs.getString("valorparcela"));
        demo.setCpf(rs.getString("cpf"));
        demo.setScore(rs.getInt("score"));
        testSet.add(demo);
}
    ConexaoSQL.FecharConexao();
    return testSet;

}
public List<demographicModelBean> getCandidates() throws SQLException{
	// recupera toda a base de testset	
		Statement stmt = ConexaoSQL.getConexaoMySQL().createStatement();
		String sql = "SELECT *  FROM testsetInadimplenciaMicrocredito where cpf not in (select distinct cpf from scoreDemografico)";
		List <demographicModelBean> testSet = new ArrayList();
		demographicModelBean demo = new demographicModelBean();
		
		ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	        //Retrieve by column name
	        demo.setId(rs.getInt("id"));
	        demo.setAdimplencia(rs.getString("adimplencia"));
	        demo.setEscolaridade(rs.getString("escolaridade"));
	        demo.setEstadocivil(rs.getString("estadocivil"));
	        demo.setFinalidade(rs.getString("finalidade"));
	        demo.setImportancianegocio(rs.getString("importancianegocio"));
	        demo.setOperacao(rs.getString("operacao"));
	        demo.setSetor(rs.getString("setor"));
	        demo.setSexo(rs.getString("sexo"));
	        demo.setSituacaoempresa(rs.getString("situacaoempresa"));
	        demo.setValorcontrato(rs.getString("valorcontrato"));
	        demo.setValorparcela(rs.getString("valorparcela"));
	        demo.setCpf(rs.getString("cpf"));
	        testSet.add(demo);
	}
	    ConexaoSQL.FecharConexao();
	    return testSet;

	}


}
