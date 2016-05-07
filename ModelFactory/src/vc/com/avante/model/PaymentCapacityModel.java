package vc.com.avante.model;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesianLogisticRegression;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.*;


public class PaymentCapacityModel {

	 public void trainModel() throws Exception{
		 	PaymentCapacityModelDAO dao = new PaymentCapacityModelDAO();
		 	//List<PaymentCapacityModelBean> candidatos = dao.getCandidates();
		 //System.out.println(candidatos.size());
		 	InstanceQuery query = new InstanceQuery();
		 	 query.setUsername("avanteods");
		 	 query.setPassword("All4din2014");
		 	 //query.setDatabaseURL("jdbc:mysql://ods.chakh74egzj3.sa-east-1.rds.amazonaws.com/microcredito");
			 query.setQuery("select coalesce(endividamentoCurtoPrazo,0),coalesce(endividamentoLongoPrazo,0),coalesce(margemDeConfiabilidade,0),coalesce(valorCreditoSobreCapitalTrabalho,0),coalesce(PMRV,0),coalesce(PMRE,0),coalesce(PMPF,0),coalesce(cicloFinanceiro),coalesce(necessidadeCapitalDeGiro,0), adimplencia from trainingSetInadimplenciaMicrocreditoCapacidadePgto; ");
		 	
		 	 //query.setc
		 	 	
		 	 Instances data = query.retrieveInstances();
		 	 data.setClassIndex(data.numAttributes() - 1);
		 	
		 	 // 
		 	 for(metaModelBean modelo : dao.getAlgoritms()){
		 	 System.out.println("testeok");
		 	Class c = Class.forName(modelo.getPath());
		 	Object o = c.newInstance();

		 	System.out.println( "o = " + o );
		 	 Classifier cModel = (Classifier) o;  
		 	 cModel.buildClassifier(data);  

		 	 metaModelBean meta = new metaModelBean();
		 	meta.setVersao((double) (dao.getLastestVersion()+1)); 
		 	meta.setAlgoritmo(modelo.getAlgoritmo());
		 	meta.setDescricao("Aprendizado semanal");
		 	meta.setCategoria("PaymentCapacity");
		 meta.setPath("/home/ubuntu/pentaho/models/PaymentCapacity/PaymentCapacity_"+meta.getAlgoritmo()+"_"+meta.getVersao().intValue()+".model");	
		 	 weka.core.SerializationHelper.write(meta.getPath(), cModel);
		 	 Evaluation eval = new Evaluation(data);
		 	 eval.crossValidateModel(cModel, data, 10,new Random());
		 	 
		 meta.setCorrectTheorical(eval.pctCorrect());
		 meta.setAreaUnderROC(eval.weightedAreaUnderROC());
		 meta.setCountTrainingSet(eval.numInstances());
		 meta.setErrorRate(eval.errorRate());
		 meta.setFalseNegative(eval.numFalseNegatives(0));
		
		 meta.setFalsePositive(eval.numFalsePositives(0));
		 meta.setTrueNegative(eval.numTrueNegatives(0));
		 meta.setTruePositive(eval.numTruePositives(0));
		 meta.setfMeasure(eval.weightedFMeasure());
		 meta.setKappa(eval.kappa());

		 	 dao.registerModel(meta);
		 System.out.println(" modelo " + meta.getCategoria() +" versao "+ meta.getVersao() +" treinado com sucesso. Instancias : "+ meta.getCountTrainingSet() +" erro: "+ meta.getErrorRate() + " eficiencia " + meta.getCorrectTheorical() );
		 	 }
		 	 }
	
}
