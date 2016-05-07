import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
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
		// em debug tirar essa linha.
		Logger.getLogger("org.apache.http").setLevel(Level.OFF);
		
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		this.resultado = new ArrayList<String>();
		List<ExtratorWebsiteStep> steps = extratorWebsite.getSteps();

		try {
			for (ExtratorWebsiteStep step : steps) {
				// declarando a response
				CloseableHttpResponse response = null;
				// construindo os params, se houver
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (String strParam : step.getParams().keySet()) {
					String strValor = step.getParams().get(strParam);
					params.add(new BasicNameValuePair(strParam, strValor));
					//System.out.println("added " + strParam + " = " + strValor);
				}

				if (step.getHttpMethod().equals("POST")) {
					HttpPost post = new HttpPost(step.getUrl());
					if (params.size() > 0) {
						post.setEntity(new UrlEncodedFormEntity(params));
					}
					response = httpclient.execute(post);
				} else if (step.getHttpMethod().equals("GET")) {
					StringBuilder requestUrl = new StringBuilder(step.getUrl());
					if (params.size() > 0) {
						String querystring = URLEncodedUtils.format(params,
								"utf-8");
						requestUrl.append("?");
						requestUrl.append(querystring);
					}
					HttpGet get = new HttpGet(requestUrl.toString());
					response = httpclient.execute(get);
				} else if (step.getHttpMethod().equals("POST_MULTIPART")) {
//					System.out.println("multipart");
					try {
						HttpPost post = new HttpPost(step.getUrl());
						MultipartEntityBuilder multiPart = MultipartEntityBuilder
								.create();

						if (params.size() > 0) {
							for (String strParam : step.getParams().keySet()) {
								String strValor = step.getParams()
										.get(strParam);
								StringBody formDataParam = new StringBody(
										strValor,
										ContentType.MULTIPART_FORM_DATA);
								multiPart = multiPart.addPart(strParam,
										formDataParam);
							}
							
							if (step.getParamsMulti() != null) {
								for (String strParam : step.getParamsMulti().keySet()) {
									List<String> lstValor = step.getParamsMulti()
											.get(strParam);
									for (String strValor : lstValor) {
										StringBody formDataParam = new StringBody(
												strValor,
												ContentType.MULTIPART_FORM_DATA);
										multiPart = multiPart.addPart(strParam,
												formDataParam);
									}
								}
							}
						}
						HttpEntity reqEntity = multiPart.build();
						post.setEntity(reqEntity);
						response = httpclient.execute(post);
					} catch (Exception e) {
						// nada por enquanto
					}
				}

				try {
//					System.out.println(response.getStatusLine());
					HttpEntity entity = response.getEntity();
					String entityContents = EntityUtils.toString(entity);
					if (step.isGuardarResultado()) {
						this.resultado.add(entityContents);
					}
					if (step.getParser() != null) {
						step.getParser().parse(entityContents);
					}
					EntityUtils.consume(entity);
				} catch (Exception e) {
					// nada por enquanto
				} finally {
					URL website = new URL("https://avante.com/intranet/microcredits_xls?action=report&controller=intranet%2Forders&format=xls");
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream("information.html");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					
					if (response != null)
						response.close();
				}
			}
		} finally {


			httpclient.close();
		}

		return this.resultado;
	}

}
