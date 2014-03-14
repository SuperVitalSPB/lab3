package ru.supervital.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;
import javax.xml.transform.Result;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;

public class SendPost extends AsyncTask<String, Void, Boolean> {
  public String Url;
  public List<NameValuePair> Params;
  public Activity mActivity;
  public View mFormView;
  public View mStatusView;
  public String sMessage;
  
  /** 
   * перед выполнением
   */
  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    showProgress(true);
  }

  /** 
   * конструктор
   */
  public SendPost(String Url, List<NameValuePair> Params, Boolean Result){
	  super();
  }

  /** 
   * в отдельном потоке
   */
  @Override
  protected Boolean doInBackground(String... Url) {
  	try {
  		sMessage = postData(Url[0], Params);
  		return true;
	} catch (Exception e) {
		sMessage = e.getMessage();
		e.printStackTrace();
		return false;
	}
	
  }

  /** 
   * после выполнения
   */
  @Override  
  protected void onPostExecute(final Boolean success) {
	  super.onPostExecute(success);
//	  showProgress(false);
  }
	  
  /** 
   * нажали отменить
   */
  @Override
  protected void onCancelled() {
	super.onCancelled();
	showProgress(false);
  }
	  
  /** 
   * моя ф-я выполнение запроса
   */  
	public String postData(String aUrl, List<NameValuePair> aParam ) throws Exception {
	    // Создадим HttpClient и PostHandler
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(aUrl); 
	    String sResp = "";
      
	    try {
			if (aParam != null)
				httppost.setEntity(new UrlEncodedFormEntity(aParam));
			
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
	        // ответ
	        HttpEntity responseEntity = response.getEntity();
	       
	        if(responseEntity != null) {
	        	sResp = EntityUtils.toString(responseEntity, "windows-1251");
	        }
	    } 
	    catch (NetworkOnMainThreadException e) {
	    	throw e;
	    } catch (Exception e) {
	    	throw e;
	    }
	    return sResp;
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		
		if (mFormView == null || mStatusView == null) 
			return;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = mActivity.getResources().getInteger(android.R.integer.config_shortAnimTime);
			if (mStatusView == null) return;
			
			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mFormView.setVisibility(View.VISIBLE);
			mFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
		
	}
	
	protected XmlPullParser prepareXpp(String sXML) throws XmlPullParserException {
	    // получаем фабрику
	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    // включаем поддержку namespace (по умолчанию выключена)
	    factory.setNamespaceAware(true);
	    // создаем парсер
	    XmlPullParser xpp = factory.newPullParser();
	    // даем парсеру на вход Reader
	    xpp.setInput(new StringReader(sXML));
	    return xpp;
	}
	
}	
