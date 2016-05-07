import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ExtratorInformacoesURL {
	private ExtratorWebsite extratorWebsite;
	private List<String> resultado;

	public void setWebsite(ExtratorWebsite extratorWebsite) {
		this.extratorWebsite = extratorWebsite;
	}

	public List<String> run() throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		int CONNECTION_TIMEOUT = 60 * 1000; // timeout in millis
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(CONNECTION_TIMEOUT)
				.setConnectTimeout(CONNECTION_TIMEOUT)
				.setSocketTimeout(CONNECTION_TIMEOUT).build();

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(requestConfig).build();
		this.resultado = new ArrayList<String>();
		List<ExtratorWebsiteStep> steps = extratorWebsite.getSteps();

		try {
			for (ExtratorWebsiteStep step : steps) {
				// declarando a response
				CloseableHttpResponse response;
				// construindo os params, se houver
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (String strParam : step.getParams().keySet()) {
					String strValor = step.getParams().get(strParam);
					params.add(new BasicNameValuePair(strParam, strValor));
					System.out.println("added " + strParam + " = " + strValor);
				}

				if (step.getHttpMethod().equals("POST")) {
					HttpPost post = new HttpPost(step.getUrl());
					if (params.size() > 0) {
						post.setEntity(new UrlEncodedFormEntity(params));
					}
					response = httpclient.execute(post);
				} else {
					StringBuilder requestUrl = new StringBuilder(step.getUrl());
					if (params.size() > 0) {
						String querystring = URLEncodedUtils.format(params,
								"utf-8");
						requestUrl.append("?");
						requestUrl.append(querystring);
					}
					HttpGet get = new HttpGet(requestUrl.toString());
					response = httpclient.execute(get);
				}

				try {
					System.out.println(response.getStatusLine());
					HttpEntity entity = response.getEntity();
					String entityContents = EntityUtils.toString(entity);
					if (step.isGuardarResultado()) {
						this.resultado.add(entityContents);
					}
					if (step.getParser() != null) {
						step.getParser().parse(entityContents);
					}
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			}
		} finally {
			httpclient.close();
		}

		return this.resultado;
	}

}
