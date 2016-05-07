package vc.com.avante;

import java.sql.SQLException;

import vc.com.avante.model.PaymentCapacityModel;
import vc.com.avante.model.demographicModel;
import vc.com.avante.model.demographicModelBean;
import vc.com.avante.model.demographicModelDAO;
import vc.com.avante.model.historicalModel;

public class Main {
public static void main(String args[]) throws Exception{
//demographicModelDAO dao = new demographicModelDAO();
//for(demographicModelBean bean : dao.getTrainingSet() ){
//	System.out.println(bean.getEscolaridade());
//}
PaymentCapacityModel m = new PaymentCapacityModel();
m.trainModel();
//PaymentCapacityModel p = new PaymentCapacityModel();
//p.EvaluateNewCandidates();

}
}
