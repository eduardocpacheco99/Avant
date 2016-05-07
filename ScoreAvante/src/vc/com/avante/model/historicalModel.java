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


public class historicalModel {

	
public void EvaluateNewCandidates() throws Exception{
	historicalModelDAO dao = new historicalModelDAO();
	List<historicalModelBean> candidatos = dao.getCandidates();
System.out.println(candidatos.size());
	InstanceQuery query = new InstanceQuery();
	 query.setUsername("avanteods");
	 query.setPassword("All4din2014");
	 //query.setDatabaseURL("jdbc:mysql://ods.chakh74egzj3.sa-east-1.rds.amazonaws.com/microcredito");
	 query.setQuery("select valorSomado,valorMedio,concat(idadeUltimaOcorrencia,'.0') idadeUltimaOcorrencia,idadeMediaOcorrencia,concat(quantidadeOcorrencias,'.0') quantidadeOcorrencias, adimplencia from trainingSetHistoricoPagamento; ");
	 //query.setc
	 	System.out.println("fez a query");
	 Instances data = query.retrieveInstances();
	 
	 data.setClassIndex(data.numAttributes() - 1);

	 System.out.println("testeok");
	 
	 //Classifier cModel = (Classifier)new NaiveBayes();  
	 //cModel.buildClassifier(data);  

	 //weka.core.SerializationHelper.write("/home/ubuntu/pentaho/models/teste.model", cModel);

	 Classifier cls = (Classifier) weka.core.SerializationHelper.read("/home/ubuntu/pentaho/models/teste4.model");
System.out.println("teste0");
	 List <historicalModelBean> listaAtual = new ArrayList();
System.out.println("teste");
//System.out.println(cls.);	 


	 for (int i = 0; i < data.numInstances(); i++) {
		 historicalModelBean atual = new historicalModelBean();

		 double clsLabel = cls.classifyInstance(data.instance(i));
		double[] clsScore = cls.distributionForInstance(data.instance(i));
		System.out.println("teste");
	   data.instance(i).setClassValue(clsLabel);
	//	   data.instance(i).classValue();
		   //data.instance(i).valueSparse(indexOfIndex);
//	System.out.println(data.instance(i).toString(0)+' '+ Math.round(clsScore[0]*1000));
atual.setScore( (int) Math.round(clsScore[0]*1000));
//System.out.println("teste2");
// estadocivil,finalidade,importancianegocio,escolaridade,operacao,setor,sexo,situacaoempresa,valorparcela,adimplencia
//  endividamentoCurtoPrazo,endividamentoLongoPrazo,margemDeConfiabilidade,valorCreditoSobreCapitalTrabalho,PMRV,PMRE,PMPF,cicloFinanceiro,capitalTrabalho,necessidadeCapitalDeGiro,cpf, adimplencia
atual.setValorSomado(Double.parseDouble(data.instance(i).toString(0)));
atual.setValorMedio(Double.parseDouble(data.instance(i).toString(1)));
atual.setIdadeUltimaOcorrencia(Double.parseDouble(data.instance(i).toString(2)));
atual.setIdadeMediaOcorrencia(Double.parseDouble(data.instance(i).toString(3)));
atual.setQuantidadeOcorrencias(Double.parseDouble(data.instance(i).toString(4)));
atual.setAdimplencia(data.instance(i).toString(5));
System.out.println("teste22");
listaAtual.add(atual);
	 }
	 List<String> jaColocados = new ArrayList<String>(); 
	// System.out.println("teste33");
	 System.out.println("lista atual :" + listaAtual.size() +" candidatos "+ candidatos.size());
int count=0;
for(historicalModelBean z : candidatos){
	System.out.println(z.getCpf());
}

	 for(historicalModelBean b : listaAtual){
//	 System.out.println("teste333");
	for(historicalModelBean a : candidatos){
count++;
		//	System.out.println("teste4");
		//System.out.println(a.getEscolaridade().replaceAll("'", "")+ " comparado com " +b.getEscolaridade());
	//	System.out.println(a.getCpf());
//		System.out.jaCprintln(	b.getCapitalTrabalho()+ " " + a.getCapitalTrabalho() +" "+ b.getCicloFinanceiro() + " " +a.getCicloFinanceiro() +" "+ b.getEndividamentoCurtoPrazo() + " " +  a.getEndividamentoCurtoPrazo() +" "+ b.getEndividamentoLongoPrazo() + " " +  a.getEndividamentoLongoPrazo() +" "+ b.getMargemDeConfiabilidade() + " " + a.getMargemDeConfiabilidade() +" "+ b.getNecessidadeCaoutalDeGiro() + " " + a.getNecessidadeCaoutalDeGiro() +" "+ b.getPMPF() + " " + a.getPMPF()  +" "+ b.getPMRE() + " " + a.getPMRE() +" "+ b.getPMRV() + " " + a.getPMRV() +" "+ b.getValorCreditoSobreCapitalTrabalho() + " " + a.getValorCreditoSobreCapitalTrabalho() + !jaColocados.contains(a.getCpf() + a.getCpf()));
//System.out.println(count +" "+	b.getEndividamentoCurtoPrazo()+ " " + a.getEndividamentoCurtoPrazo() + !jaColocados.contains(a.getCpf()));
		
if(b.getIdadeMediaOcorrencia().equals( a.getIdadeMediaOcorrencia()) && b.getIdadeUltimaOcorrencia() .equals(a.getIdadeUltimaOcorrencia()) && b.getQuantidadeOcorrencias().equals(  a.getQuantidadeOcorrencias() )&& b.getValorMedio().equals(  a.getValorMedio()) && b.getValorSomado().equals( a.getValorSomado() ) && !jaColocados.contains(a.getCpf()) ){
		//System.out.println("dentro cpf a"+ a.getCpf() + " b " + b.getCpf() +" score a"+ a.getScore() + " b " + b.getScore());
			//	if(b.getEndividamentoCurtoPrazo().equals( a.getEndividamentoCurtoPrazo()) && !jaColocados.contains(a.getCpf()) ){
			//
			//System.out.println("PASSOU");
			a.setScore(b.getScore());	
//	System.out.println(a.getScore()+" " + a.getCpf());
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
