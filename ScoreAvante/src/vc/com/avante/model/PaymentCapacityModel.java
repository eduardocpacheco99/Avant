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


public class PaymentCapacityModel {

	
public void EvaluateNewCandidates() throws Exception{
	PaymentCapacityModelDAO dao = new PaymentCapacityModelDAO();
	List<PaymentCapacityModelBean> candidatos = dao.getCandidates();
System.out.println(candidatos.size());
	InstanceQuery query = new InstanceQuery();
	 query.setUsername("avanteods");
	 query.setPassword("All4din2014");
	 //query.setDatabaseURL("jdbc:mysql://ods.chakh74egzj3.sa-east-1.rds.amazonaws.com/microcredito");
	 query.setQuery("select  coalesce(endividamentoCurtoPrazo,null,0) endividamentoCurtoPrazo,coalesce(endividamentoLongoPrazo,null,0) endividamentoLongoPrazo,coalesce(margemDeConfiabilidade,null,0) margemDeConfiabilidade,coalesce(valorCreditoSobreCapitalTrabalho,null,0) valorCreditoSobreCapitalTrabalho,coalesce(PMRV,null,0) PMRV,coalesce(PMRE,null,0) PMRE,coalesce(PMPF,null,0) PMPF,coalesce(cicloFinanceiro,null,0) cicloFinanceiro,coalesce(capitalTrabalho,null,0) capitalTrabalho,coalesce(necessidadeCapitalDeGiro,null,0) necessidadeCapitalDeGiro, adimplencia from testSetInadimplenciaMicrocreditoCapacidadePgto where cpf not in (select distinct cpf from microcredito.scoreCapacidadePgto); ");
	 //query.setc

		InstanceQuery query2 = new InstanceQuery();
		 query2.setUsername("avanteods");
		 query2.setPassword("All4din2014");
		 //query.setDatabaseURL("jdbc:mysql://ods.chakh74egzj3.sa-east-1.rds.amazonaws.com/microcredito");
		 query2.setQuery("select   coalesce(endividamentoCurtoPrazo,null,0) endividamentoCurtoPrazo,coalesce(endividamentoLongoPrazo,null,0) endividamentoLongoPrazo,coalesce(margemDeConfiabilidade,null,0) margemDeConfiabilidade,coalesce(valorCreditoSobreCapitalTrabalho,null,0) valorCreditoSobreCapitalTrabalho,coalesce(PMRV,null,0) PMRV,coalesce(PMRE,null,0) PMRE,coalesce(PMPF,null,0) PMPF,coalesce(cicloFinanceiro,null,0) cicloFinanceiro,coalesce(capitalTrabalho,null,0) capitalTrabalho,coalesce(necessidadeCapitalDeGiro,null,0) necessidadeCapitalDeGiro, cpf from testSetInadimplenciaMicrocreditoCapacidadePgto where cpf not in (select distinct cpf from microcredito.scoreCapacidadePgto);");

	 
	 
	 Instances data = query.retrieveInstances();
	 Instances data2 = query2.retrieveInstances();
	 
	 
	// Instances teste = new Instances();
	 data.setClassIndex(data.numAttributes() - 1);
	
	 System.out.println("teste" + data.numAttributes());
	 
	// Classifier cModel = (Classifier)new NaiveBayes();  
//	 cModel.buildClassifier(data);  

//	 cModel.
	 
	 //weka.core.SerializationHelper.write("/home/ubuntu/pentaho/models/teste.model", cModel);

//	 Classifier cls = (Classifier) weka.core.SerializationHelper.read("/home/ubuntu/pentaho/models/InadimplenciaMicrocreditoCapacidadePgtoIBk72.model");
	 Classifier cls = (Classifier) weka.core.SerializationHelper.read("/home/ubuntu/pentaho/models/PaymentCapacity/PaymentCapacity_NaiveBayes_13.model");
//	 Classifier cls = (Classifier) weka.core.SerializationHelper.read("/home/ubuntu/pentaho/models/CapacidadePagamento30diasNaiveBayesFunilNosso2.model");

	 
	 List <PaymentCapacityModelBean> listaAtual = new ArrayList();

//System.out.println(cls.);	 

	 PaymentCapacityModelBean atual;
	 for (int i = 0; i < data.numInstances(); i++) {
		 atual = new PaymentCapacityModelBean();
	//	 data.instance(i).
		// double clsLabel = cls.classifyInstance(data.instance(i));
		double[] clsScore = cls.distributionForInstance(data.instance(i));
		 for (int j = 0; j < data.numInstances(); j++) {
			if(data.instance(i).toString(0).equals(data2.instance(j).toString(0))
				&& data.instance(i).toString(1).equals(data2.instance(j).toString(1))
				&& data.instance(i).toString(2).equals(data2.instance(j).toString(2))
				&& data.instance(i).toString(3).equals(data2.instance(j).toString(3))
				&& data.instance(i).toString(4).equals(data2.instance(j).toString(4))
				&& data.instance(i).toString(5).equals(data2.instance(j).toString(5))
				&& data.instance(i).toString(6).equals(data2.instance(j).toString(6))
				&& data.instance(i).toString(7).equals(data2.instance(j).toString(7))
				&& data.instance(i).toString(8).equals(data2.instance(j).toString(8))
				&& data.instance(i).toString(9).equals(data2.instance(j).toString(9))
				
					){
				atual.setCpf(data2.instance(j).toString(10));
				atual.setScore( (double) Math.round(clsScore[0]*1000));
				atual.setValorCredito(valorCredito);
System.out.println(atual.getCpf() + " " + atual.getScore() + " " + i);
				break;
				
			}
			 
		 }
		 listaAtual.add(atual);
	 }
	 dao.evaluateCandidates(listaAtual);
	 
	 /*
	 for (int i = 0; i < data.numInstances(); i++) {
		 atual = new PaymentCapacityModelBean();

		 double clsLabel = cls.classifyInstance(data.instance(i));
		double[] clsScore = cls.distributionForInstance(data.instance(i));
	//	System.out.println("teste");
	   data.instance(i).setClassValue(clsLabel);
	//	   data.instance(i).classValue();
		   //data.instance(i).valueSparse(indexOfIndex);
//	System.out.println(data.instance(i).toString(0)+' '+ Math.round(clsScore[0]*1000));
atual.setScore( (double) Math.round(clsScore[0]*1000));
//System.out.println("teste2");
// estadocivil,finalidade,importancianegocio,escolaridade,operacao,setor,sexo,situacaoempresa,valorparcela,adimplencia
//  endividamentoCurtoPrazo,endividamentoLongoPrazo,margemDeConfiabilidade,valorCreditoSobreCapitalTrabalho,PMRV,PMRE,PMPF,cicloFinanceiro,capitalTrabalho,necessidadeCapitalDeGiro,cpf, adimplencia
atual.setEndividamentoCurtoPrazo(Double.parseDouble(data.instance(i).toString(0)));
atual.setEndividamentoLongoPrazo(Double.parseDouble(data.instance(i).toString(1)));
atual.setMargemDeConfiabilidade(Double.parseDouble(data.instance(i).toString(2)));
atual.setValorCreditoSobreCapitalTrabalho(Double.parseDouble(data.instance(i).toString(3)));
atual.setPMRV(Double.parseDouble(data.instance(i).toString(4)));
atual.setPMRE(Double.parseDouble(data.instance(i).toString(5)));
//System.out.println("teste22");
atual.setPMPF(Double.parseDouble(data.instance(i).toString(6)));
atual.setCicloFinanceiro(Double.parseDouble(data.instance(i).toString(7)));
//System.out.println("teste222");
atual.setCapitalTrabalho(Double.parseDouble(data.instance(i).toString(8)));
atual.setNecessidadeCaoutalDeGiro(Double.parseDouble(data.instance(i).toString(9)));
//System.out.println("teste2222");
//atual.setAdimplencia(data.instance(i).toString(10));
//System.out.println("teste22222");
//atual.setCpf(data.instance(i).toString(11));
listaAtual.add(atual);
//System.out.println("teste3");
//System.out.println(atual.getSexo());
	 }
	 List<String> jaColocados = new ArrayList<String>(); 
	// System.out.println("teste33");
	 System.out.println("lista atual :" + listaAtual.size() +" candidatos "+ candidatos.size());
int count=0;
//for(PaymentCapacityModelBean z : candidatos){
//	System.out.println(z.getCpf());
//}

	 for(PaymentCapacityModelBean b : listaAtual){ // sem cpf
	// System.out.println("teste333");
	for(PaymentCapacityModelBean a : candidatos){ // com cpf
count++;
		//	System.out.println("teste4");
		//System.out.println(a.getEscolaridade().replaceAll("'", "")+ " comparado com " +b.getEscolaridade());
	//	System.out.println(a.getCpf());
//		System.out.println(	b.getCapitalTrabalho()+ " " + a.getCapitalTrabalho() +" "+ b.getCicloFinanceiro() + " " +a.getCicloFinanceiro() +" "+ b.getEndividamentoCurtoPrazo() + " " +  a.getEndividamentoCurtoPrazo() +" "+ b.getEndividamentoLongoPrazo() + " " +  a.getEndividamentoLongoPrazo() +" "+ b.getMargemDeConfiabilidade() + " " + a.getMargemDeConfiabilidade() +" "+ b.getNecessidadeCaoutalDeGiro() + " " + a.getNecessidadeCaoutalDeGiro() +" "+ b.getPMPF() + " " + a.getPMPF()  +" "+ b.getPMRE() + " " + a.getPMRE() +" "+ b.getPMRV() + " " + a.getPMRV() +" "+ b.getValorCreditoSobreCapitalTrabalho() + " " + a.getValorCreditoSobreCapitalTrabalho() + !jaColocados.contains(a.getCpf() + a.getCpf()));
//System.out.println("A");
		
	if(b.getCapitalTrabalho().equals( a.getCapitalTrabalho()) && b.getCicloFinanceiro() .equals(a.getCicloFinanceiro()) && b.getEndividamentoCurtoPrazo().equals(  a.getEndividamentoCurtoPrazo() )&& b.getEndividamentoLongoPrazo().equals(  a.getEndividamentoLongoPrazo()) && b.getMargemDeConfiabilidade().equals( a.getMargemDeConfiabilidade() ) && b.getNecessidadeCaoutalDeGiro().equals( a.getNecessidadeCaoutalDeGiro()) && b.getPMPF().equals( a.getPMPF())  && b.getPMRE().equals( a.getPMRE()) && b.getPMRV().equals( a.getPMRV() )&& b.getValorCreditoSobreCapitalTrabalho().equals( a.getValorCreditoSobreCapitalTrabalho()) && !jaColocados.contains(a.getCpf()) ){
//if(b.getCpf().equals( a.getCpf()) && !jaColocados.contains(a.getCpf()) ){
		
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
/*
	 Evaluation eTest = new Evaluation(data);
	 eTest.evaluateModel(cls, data);
eTest.correct();
eTest.errorRate();
eTest.areaUnderROC(0);
eTest.falseNegativeRate(0);
eTest.falsePositiveRate(0);
eTest.trueNegativeRate(0);
eTest.truePositiveRate(0);
eTest.correlationCoefficient();
eTest.fMeasure(0);
eTest.SFMeanEntropyGain();
eTest.weightedAreaUnderROC();
eTest.weightedFalseNegativeRate();
eTest.weightedFalsePositiveRate();
eTest.weightedFMeasure();
eTest.weightedRecall();
eTest.totalCost();
eTest.correct();
*/
	 
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
