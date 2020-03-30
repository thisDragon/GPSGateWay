package com.analog.data.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.util.StringUtil;

public class HttpRequest {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
	private static final int TIMEOUT = 20 * 1000;
	private static final String charset ="UTF-8";
	public static final String ERROR_VALUE = "0";
	
	/**
	 * @Title: authSendPostOfBody   
	 * @Description: post请求(可认证的方式)
	 * @param url :请求地址
	 * @param jsonString :json字符串
	 * @param isAuth :是否开启认证
	 * @param authAccount :账号
	 * @param authPwd :密码
	 * @return 
	 * String      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月3日 下午4:07:53
	 */
	public static String authSendPostOfBody(String url, String jsonString, boolean isAuth, String authAccount, String authPwd) {
	  	String returnValue = ERROR_VALUE;
		CloseableHttpClient httpClient = null;
		//创建httpPost对象
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse httpResonse = null;
		try {
			// 创建HttpClient对象
			httpClient = HttpClients.createDefault();

			// 第三步：给httpPost设置JSON格式的参数
			StringEntity requestEntity = new StringEntity(jsonString.toString(), "utf-8");
			requestEntity.setContentEncoding("UTF-8");
			
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(requestEntity);
			if (isAuth && StringUtil.isNotEmpty(authAccount) && StringUtil.isNotEmpty(authPwd)) {
				httpPost.addHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((authAccount + ":" + authPwd).getBytes()));
			}
			
			// 第四步：发送HttpPost请求，获取返回值
			httpResonse = httpClient.execute(httpPost);
			int statusCode = httpResonse.getStatusLine().getStatusCode();
			//if (statusCode != HttpStatus.SC_OK) {
			if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
				returnValue = org.apache.http.util.EntityUtils.toString(httpResonse.getEntity(), "UTF-8");
			}else{
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
			if (httpResonse != null) {
				try {
					httpResonse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 
	* @Title: HttpClientGet
	* @Description: get请求(针对分区专用)
	* @param @param url
	* @param @param paramMap
	* @param @return
	* @param @throws ClientProtocolException
	* @param @throws IOException    参数
	* @return String    返回类型
	* @throws
	 */
	@SuppressWarnings("resource")
	public static String httpClientGet(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();
        List<NameValuePair> formparams = setHttpParams(paramMap);
        String param = URLEncodedUtils.format(formparams, "UTF-8");
        httpGet.setURI(URI.create(url + "?" + param));
        HttpResponse response = httpClient.execute(httpGet);
        String httpEntityContent = getHttpEntityContent(response);
        httpGet.abort();
        return httpEntityContent;
	}
	
	 /**
     * 设置请求参数
     * @param
     * @return
     */
    private static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formparams;
    }
    
    /**
     * 获得响应HTTP实体内容
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private static String getHttpEntityContent(HttpResponse response) throws IOException, UnsupportedEncodingException {
       //通过HttpResponse 的getEntity()方法获取返回信息
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line + "\n");
                line = br.readLine();
            }
            br.close();
            is.close();
            return sb.toString();
        }
        return "";
    }
	
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static Map<String, String> sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        String exceptionInfo = "";
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
            }
            // 定义 BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            resultMap.put("result", result.toString());
        } catch (Exception e) {
        	exceptionInfo = e.getMessage();
        	resultMap.put("exceptionInfo", exceptionInfo);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (StringUtil.isNotEmpty(exceptionInfo)) {
                	exceptionInfo = exceptionInfo+";"+e2.getMessage();
				}else{
					exceptionInfo = e2.getMessage();
				}
                resultMap.put("exceptionInfo", exceptionInfo);
            }
        }
        return resultMap;
    }
    
    /*public static String sendPostOfBody(String strURL, String params) throws Exception{
    	HttpURLConnection urlConnection = null;
		InputStream is = null;
		byte[] bytes = params.getBytes();
		try {
			URL url = new URL(strURL);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(TIMEOUT);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setInstanceFollowRedirects(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.connect();
			
			if(bytes != null  && bytes.length > 0){
				DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
				dos.write(bytes);
		//		dos.writeUTF(bytes);
				dos.flush();
				dos.close();
			}
			
			int responseCode = urlConnection.getResponseCode();
			if(responseCode != 200) throw new Exception("网络请求出错了，错误代码: " + responseCode);
			is = urlConnection.getInputStream();
			return readContentFromInputStream(is, charset);
		} finally {
			if(is != null) is.close();
			if(urlConnection != null) urlConnection.disconnect();
		}
    }
    private static String readContentFromInputStream(InputStream is, String charset) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while(line != null) {
			sb.append(line);
			line = reader.readLine();
		}
		
		return sb.toString();
	}*/
    
    /**
	 * 
	* @Title: sendPostOfBody
	* @Description: post请求在body里传参
	* @param @param strURL
	* @param @param params
	* @param @return    参数
	* @return String    返回类型
	* @throws
	 */
	public static String sendPostOfBody(String strURL, String params) {
		  OutputStreamWriter out = null;
		  InputStream is = null;
		  String result = "";
		  try { 
		        URL url = new URL(strURL);// 创建连接 
		        HttpURLConnection connection = (HttpURLConnection) 
		        url.openConnection(); 
		        connection.setDoOutput(true); 
		        connection.setDoInput(true); 
		        connection.setUseCaches(false); 
		        connection.setInstanceFollowRedirects(true); 
		        connection.setRequestMethod("POST");// 设置请求方式   
		        connection.setRequestProperty("Accept","application/json");// 设置接收数据的格式   
		        connection.setRequestProperty("Content-Type","application/json");// 设置发送数据的格式   
		        connection.connect(); 
		        out = new OutputStreamWriter( connection.getOutputStream(),"UTF-8");// utf-8编码   
		        out.append(params); 
		        out.flush(); 
		        out.close(); // 读取响应   
		        int length = (int) connection.getContentLength();// 获取长度   
		        is = connection.getInputStream(); 
		        if (length != -1){
		            byte[] data = new byte[length]; 
		            byte[] temp = new byte[512]; 
		            int readLen = 0; int destPos = 0; 
		            while ((readLen = is.read(temp)) > 0){
		                System.arraycopy(temp, 0, data, destPos, readLen); 
		                destPos += readLen; 
		            }
		            result = new String(data, "UTF-8");
		            return result; 
		        } 
		    } catch (Exception e) {
		        e.printStackTrace();
		    }finally {
				try {
					if (out != null) {
						out.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		    return result;
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static boolean saveUrlAs(String fileUrl, String fileName) {
		// 此方法只能用户HTTP协议
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			DataInputStream in = new DataInputStream(connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}