package vc.com.avante.model;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesianLogisticRegression;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;


public class demographicModel {

	
public void EvaluateNewCandidates() throws Exception{
	demographicModelDAO dao = new demographicModelDAO();
	List<demographicModelBean> candidatos = dao.getCandidates();
System.out.println(candidatos.size());
	InstanceQuery query = new InstanceQuery();
	 query.setUsername("avanteods");
	 query.setPassword("All4din2014");
	 //query.setDatabaseURL("jdbc:mysql://ods.chakh74egzj3.sa-east-1.rds.amazonaws.com/microcredito");
	 query.setQuery("select  estadocivil,finalidade,importancianegocio,escolaridade,operacao,setor,sexo,situacaoempresa,valorparcela,adimplencia  from testsetInadimplenciaMicrocredito ");
	 //query.setc
	 	
	 Instances data = query.retrieveInstances();
	 data.setClassIndex(data.numAttributes() - 1);
		
	 System.out.println("testeok");
	 
	 //Classifier cModel = (Classifier)new NaiveBayes();  
	 //cModel.buildClassifier(data);  

	 //weka.core.SerializationHelper.write("/home/ubuntu/pentaho/models/teste.model", cModel);

	 Classifier cls = (Classifier) weka.core.SerializationHelper.read("/home/ubuntu/pentaho/models/teste.model");
List <demographicModelBean> listaAtual = new ArrayList();

	 for (int i = 0; i < data.numInstances(); i++) {
		 demographicModelBean atual = new demographicModelBean();

		 double clsLabel = cls.classifyInstance(data.instance(i));
		double[] clsScore = cls.distributionForInstance(data.instance(i));
		
	   data.instance(i).setClassValue(clsLabel);
	//	   data.instance(i).classValue();
		   //data.instance(i).valueSparse(indexOfIndex);
//	System.out.println(data.instance(i).toString(0)+' '+ Math.round(clsScore[0]*1000));
atual.setScore( (int) Math.round(clsScore[0]*1000));
// estadocivil,finalidade,importancianegocio,escolaridade,operacao,setor,sexo,situacaoempresa,valorparcela,adimplencia
atual.setEstadocivil(data.instance(i).toString(0));
atual.setFinalidade(data.instance(i).toString(1));
atual.setImportancianegocio(data.instance(i).toString(2));
atual.setEscolaridade(data.instance(i).toString(3));
atual.setOperacao(data.instance(i).toString(4));
atual.setSetor(data.instance(i).toString(5));
atual.setSexo(data.instance(i).toString(6));
atual.setSituacaoempresa(data.instance(i).toString(7));
atual.setValorparcela(data.instance(i).toString(8));

listaAtual.add(atual);
System.out.println(atual.getSexo());
	 }
	 List<String> jaColocados = new ArrayList<String>(); 
for(demographicModelBean b : listaAtual){
	for(demographicModelBean a : candidatos){
		//System.out.println(a.getEscolaridade().replaceAll("'", "")+ " comparado com " +b.getEscolaridade());
		if(b.getEscolaridade().replaceAll("'", "").equals(a.getEscolaridade()) && b.getEstadocivil().replaceAll("'", "").equals(a.getEstadocivil()) && b.getFinalidade().replaceAll("'", "").equals( a.getFinalidade()) && b.getImportancianegocio().replaceAll("'", "").equals(a.getImportancianegocio()) && b.getOperacao().replaceAll("'", "").equals(a.getOperacao()) && b.getSetor().replaceAll("'", "").equals(a.getSetor()) && b.getSexo().replaceAll("'", "").equals(a.getSexo())  && b.getSituacaoempresa().replaceAll("'", "").equals(a.getSituacaoempresa()) && b.getValorparcela().replaceAll("'", "").equals(a.getValorparcela())&& !jaColocados.contains(a.getCpf()) ){
		//System.out.println("dentro cpf a"+ a.getCpf() + " b " + b.getCpf() +" score a"+ a.getScore() + " b " + b.getScore());
	
			a.setScore(b.getScore());	
		dao.evaluateCandidate(a);
	jaColocados.add(a.getCpf());	
		}
	}
	
}	 
	 // Test the model
//	 Evaluation eTest = new Evaluation(data);
//	 eTest.evaluateModel(cls, data);
//System.out.println(eTest.correct());	 
	 
/*	
	Classifier cModel = (Classifier)new NaiveBayes();  
	cModel.buildClassifier(isTrainingSet);  

	weka.core.SerializationHelper.write("/some/where/nBayes.model", cModel);

	Classifier cls = (Classifier) weka.core.SerializationHelper.read("/some/where/nBayes.model");

	// Test the model
	Evaluation eTest = new Evaluation(isTrainingSet);
	eTest.evaluateModel(cls, isTrainingSet);
*/
}	
}
