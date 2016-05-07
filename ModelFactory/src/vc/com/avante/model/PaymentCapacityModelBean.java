package vc.com.avante.model;

public class PaymentCapacityModelBean {
	public  PaymentCapacityModelBean(){}
 private Integer id;
 private Double 
endividamentoCurtoPrazo;
 private Double
 endividamentoLongoPrazo;
 private Double margemDeConfiabilidade;
 private Double valorCreditoSobreCapitalTrabalho;
 private Double PMRV;
 private Double PMRE;
 private Double PMPF;
 private Double cicloFinanceiro;
 private Double capitalTrabalho;
 private Double necessidadeCaoutalDeGiro;
 private String cpf;
 private Double score;
 private String adimplencia;
public Integer getId() {
	return id;
}
public Double getEndividamentoCurtoPrazo() {
	return endividamentoCurtoPrazo;
}
public Double getEndividamentoLongoPrazo() {
	return endividamentoLongoPrazo;
}
public Double getMargemDeConfiabilidade() {
	return margemDeConfiabilidade;
}
public Double getValorCreditoSobreCapitalTrabalho() {
	return valorCreditoSobreCapitalTrabalho;
}
public Double getPMRV() {
	return PMRV;
}
public Double getPMRE() {
	return PMRE;
}
public Double getPMPF() {
	return PMPF;
}
public Double getCicloFinanceiro() {
	return cicloFinanceiro;
}
public Double getCapitalTrabalho() {
	return capitalTrabalho;
}
public Double getNecessidadeCaoutalDeGiro() {
	return necessidadeCaoutalDeGiro;
}
public String getCpf() {
	return cpf;
}
public Double getScore() {
	return score;
}
public String getAdimplencia() {
	return adimplencia;
}
public void setId(Integer id) {
	this.id = id;
}
public void setEndividamentoCurtoPrazo(Double endividamentoCurtoPrazo) {
	this.endividamentoCurtoPrazo = endividamentoCurtoPrazo;
}
public void setEndividamentoLongoPrazo(Double endividamentoLongoPrazo) {
	this.endividamentoLongoPrazo = endividamentoLongoPrazo;
}
public void setMargemDeConfiabilidade(Double margemDeConfiabilidade) {
	this.margemDeConfiabilidade = margemDeConfiabilidade;
}
public void setValorCreditoSobreCapitalTrabalho(
		Double valorCreditoSobreCapitalTrabalho) {
	this.valorCreditoSobreCapitalTrabalho = valorCreditoSobreCapitalTrabalho;
}
public void setPMRV(Double pMRV) {
	PMRV = pMRV;
}
public void setPMRE(Double pMRE) {
	PMRE = pMRE;
}
public void setPMPF(Double pMPF) {
	PMPF = pMPF;
}
public void setCicloFinanceiro(Double cicloFinanceiro) {
	this.cicloFinanceiro = cicloFinanceiro;
}
public void setCapitalTrabalho(Double capitalTrabalho) {
	this.capitalTrabalho = capitalTrabalho;
}
public void setNecessidadeCaoutalDeGiro(Double necessidadeCaoutalDeGiro) {
	this.necessidadeCaoutalDeGiro = necessidadeCaoutalDeGiro;
}
public void setCpf(String cpf) {
	this.cpf = cpf;
}
public void setScore(Double score) {
	this.score = score;
}
public void setAdimplencia(String adimplencia) {
	this.adimplencia = adimplencia;
}
}
