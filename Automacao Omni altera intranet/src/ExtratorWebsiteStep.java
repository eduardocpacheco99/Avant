import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtratorWebsiteStep {
	private String url;
	private Map<String, String> params;
	private Map<String, List<String>> paramsMulti = null;
	private String httpMethod;
	private boolean guardarResultado;
	private ExtratorWebsiteStepParser parser;

	public ExtratorWebsiteStep(String url, Map<String, String> params,
			String httpMethod, boolean guardarResultado) {
		super();
		this.url = url;
		this.params = params;
		this.httpMethod = httpMethod;
		this.guardarResultado = guardarResultado;
	}

	public ExtratorWebsiteStep(String url, Map<String, String> params, Map<String, List<String>> paramsMulti,
			String httpMethod, boolean guardarResultado) {
		super();
		this.url = url;
		this.paramsMulti = paramsMulti;
		this.httpMethod = httpMethod;
		this.guardarResultado = guardarResultado;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Map<String, List<String>> getParamsMulti() {
		if (paramsMulti == null) 
			this.paramsMulti = new HashMap<String, List<String>>();
		return paramsMulti;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public boolean isGuardarResultado() {
		return guardarResultado;
	}

	public void setGuardarResultado(boolean guardarResultado) {
		this.guardarResultado = guardarResultado;
	}

	public ExtratorWebsiteStepParser getParser() {
		return parser;
	}

	public void setParser(ExtratorWebsiteStepParser parser) {
		this.parser = parser;
	}
}
