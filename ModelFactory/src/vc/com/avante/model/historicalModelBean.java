package vc.com.avante.model;

public class historicalModelBean {
	public  historicalModelBean(){}
 private Integer id;
 private String cpf;
 private Double valorSomado;
 private Double valorMedio;
 private Double idadeUltimaOcorrencia;
 private Double idadeMediaOcorrencia;
 private Double quantidadeOcorrencias;
 private Integer score;
 private String adimplencia;
public Integer getId() {
	return id;
}
public String getCpf() {
	return cpf;
}
public Double getValorSomado() {
	return valorSomado;
}
public Double getValorMedio() {
	return valorMedio;
}
public Double getIdadeUltimaOcorrencia() {
	return idadeUltimaOcorrencia;
}
public Double getIdadeMediaOcorrencia() {
	return idadeMediaOcorrencia;
}
public Double getQuantidadeOcorrencias() {
	return quantidadeOcorrencias;
}
public String getAdimplencia() {
	return adimplencia;
}
public void setId(Integer id) {
	this.id = id;
}
public void setCpf(String cpf) {
	this.cpf = cpf;
}
public void setValorSomado(Double valorSomado) {
	this.valorSomado = valorSomado;
}
public void setValorMedio(Double valorMedio) {
	this.valorMedio = valorMedio;
}
public void setIdadeUltimaOcorrencia(Double idadeUltimaOcorrencia) {
	this.idadeUltimaOcorrencia = idadeUltimaOcorrencia;
}
public void setIdadeMediaOcorrencia(Double idadeMediaOcorrencia) {
	this.idadeMediaOcorrencia = idadeMediaOcorrencia;
}
public void setQuantidadeOcorrencias(Double quantidadeOcorrencias) {
	this.quantidadeOcorrencias = quantidadeOcorrencias;
}
public void setAdimplencia(String adimplencia) {
	this.adimplencia = adimplencia;
}
public Integer getScore() {
	return score;
}
public void setScore(Integer score) {
	this.score = score;
}

}
