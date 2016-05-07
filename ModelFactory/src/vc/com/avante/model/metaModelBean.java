package vc.com.avante.model;

public class metaModelBean {
	public  metaModelBean(){}


	
	private Integer id;	
	private String categoria;
	private Double versao;
	private String descricao;
	private String algoritmo;
	private String created_at;
	private Double countTrainingSet;
	private Double countTestSet;
	private Double areaUnderROC;
	private Double falsePositive;
	private Double falseNegative;
	private Double truePositive;
	private Double trueNegative;
	private Double fMeasure;
	private Double correctTheorical;
	private Double correctActual;
	private Double kappa;
	private Double errorRate;
	private Double meanAbsoluteError;
	private Double rootMeanSquareError;
	private String path;
	public Integer getId() {
	return id;
}
	public String getCategoria() {
		return categoria;
	}
	public Double getVersao() {
		return versao;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getAlgoritmo() {
		return algoritmo;
	}
	public String getCreated_at() {
		return created_at;
	}
	public Double getCountTrainingSet() {
		return countTrainingSet;
	}
	public Double getCountTestSet() {
		return countTestSet;
	}
	public Double getAreaUnderROC() {
		return areaUnderROC;
	}
	public Double getFalsePositive() {
		return falsePositive;
	}
	public Double getFalseNegative() {
		return falseNegative;
	}
	public Double getTruePositive() {
		return truePositive;
	}
	public Double getTrueNegative() {
		return trueNegative;
	}
	public Double getfMeasure() {
		return fMeasure;
	}
	public Double getCorrectTheorical() {
		return correctTheorical;
	}
	public Double getCorrectActual() {
		return correctActual;
	}
	public Double getKappa() {
		return kappa;
	}
	public Double getErrorRate() {
		return errorRate;
	}
	public Double getMeanAbsoluteError() {
		return meanAbsoluteError;
	}
	public Double getRootMeanSquareError() {
		return rootMeanSquareError;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public void setVersao(Double versao) {
		this.versao = versao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public void setCountTrainingSet(Double countTrainingSet) {
		this.countTrainingSet = countTrainingSet;
	}
	public void setCountTestSet(Double countTestSet) {
		this.countTestSet = countTestSet;
	}
	public void setAreaUnderROC(Double areaUnderROC) {
		this.areaUnderROC = areaUnderROC;
	}
	public void setFalsePositive(Double falsePositive) {
		this.falsePositive = falsePositive;
	}
	public void setFalseNegative(Double falseNegative) {
		this.falseNegative = falseNegative;
	}
	public void setTruePositive(Double truePositive) {
		this.truePositive = truePositive;
	}
	public void setTrueNegative(Double trueNegative) {
		this.trueNegative = trueNegative;
	}
	public void setfMeasure(Double fMeasure) {
		this.fMeasure = fMeasure;
	}
	public void setCorrectTheorical(Double correctTheorical) {
		this.correctTheorical = correctTheorical;
	}
	public void setCorrectActual(Double correctActual) {
		this.correctActual = correctActual;
	}
	public void setKappa(Double kappa) {
		this.kappa = kappa;
	}
	public void setErrorRate(Double errorRate) {
		this.errorRate = errorRate;
	}
	public void setMeanAbsoluteError(Double meanAbsoluteError) {
		this.meanAbsoluteError = meanAbsoluteError;
	}
	public void setRootMeanSquareError(Double rootMeanSquareError) {
		this.rootMeanSquareError = rootMeanSquareError;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

}
