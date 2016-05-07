package testeEmailReceive;

public class emailBean {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long data) {
		this.date =  data;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	int id;
	Long date;
	String subject ;
	String file; 
	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	String mes;
	String dia;
	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	String categoria;
String parceiro;
public String getParceiro() {
	return parceiro;
}

public void setParceiro(String parceiro) {
	this.parceiro = parceiro;
}


	
}
