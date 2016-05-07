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


public class historicalModel {
		
		
	 public void trainModel() throws Exception{
	 	historicalModelDAO dao = new historicalModelDAO();
	 	List<historicalModelBean> candidatos = dao.getCandidates();
	 System.out.println(candidatos.size());
	 	InstanceQuery query = new InstanceQuery();
	 	 query.setUsername("avanteods");
	 	 query.setPassword("All4din2014");
	 	 //query.setDatabaseURL("jdbc:mysql://ods.chakh74egzj3.sa-east-1.rds.amazonaws.com/microcredito");
	 	 query.setQuery("select valorSomado,valorMedio,concat(idadeUltimaOcorrencia,'.0') idadeUltimaOcorrencia,idadeMediaOcorrencia,concat(quantidadeOcorrencias,'.0') quantidadeOcorrencias, adimplencia from trainingSetHistoricoPagamento; ");
	 	
	 	 //query.setc
	 	 	
	 	 Instances data = query.retrieveInstances();
	 	 data.setClassIndex(data.numAttributes() - 1);
	 	
	 	 // 
	 	 
	 	 System.out.println("testeok");
	 	 
	 	 Classifier cModel = (Classifier)new J48();  
	 	 cModel.buildClassifier(data);  

	 	 metaModelBean meta = new metaModelBean();
	 	meta.setVersao((double) (dao.getLastestVersion()+1)); 
	 	meta.setAlgoritmo("J48");
	 	meta.setDescricao("Aprendizado semanal");
	 	meta.setCategoria("historical");
	 meta.setPath("/home/ubuntu/pentaho/models/historical/historical_"+meta.getVersao().intValue()+".model");	
	 	 weka.core.SerializationHelper.write(meta.getPath(), cModel);
	 	 Evaluation eval = new Evaluation(data);
	 	 eval.crossValidateModel(cModel, data, 10,new Random());
	 	 
	 meta.setCorrectTheorical(eval.pctCorrect());
	 meta.setAreaUnderROC(eval.weightedAreaUnderROC());
	 meta.setCountTrainingSet(eval.numInstances());
	 meta.setErrorRate(eval.errorRate());
	 meta.setFalseNegative(eval.weightedFalseNegativeRate());
	 meta.setFalsePositive(eval.weightedFalsePositiveRate());
	 meta.setTrueNegative(eval.weightedTrueNegativeRate());
	 meta.setTruePositive(eval.weightedTruePositiveRate());
	 meta.setfMeasure(eval.weightedFMeasure());
	 meta.setKappa(eval.kappa());

	 	 dao.registerModel(meta);
	 System.out.println(" modelo " + meta.getCategoria() +" versao "+ meta.getVersao() +" treinado com sucesso. Instancias : "+ meta.getCountTrainingSet() +" erro: "+ meta.getErrorRate() + " eficiencia " + meta.getCorrectTheorical() );
	 }	
}
