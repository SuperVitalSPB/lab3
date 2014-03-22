package com.lab3;

import java.io.IOException;
import java.io.StringReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lab3.LoginActivity.LoginSendPost;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DummySectionFragment extends Fragment {
	TextView lblLogin;
	TextView lblTitle;
	String logToken = "";
	String logName;
	String mLblLogonText;
	ListView lvMain;
	Boolean isDevelop = false;
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	public boolean isOnline;
	
	private RateArrayAdapter adapter;
	private View mFormView;
	private View mStatusView;
	
	ArrayList<Rate> rates = new ArrayList<Rate>();
	
	
	public DummySectionFragment() {
		super();
	}

	public void TabSelected(){
		if (!isOnline || isDevelop) {
			lblLogin.setTextColor(Color.RED);
			lblLogin.setText((isDevelop)? "DEVELOP" : getString(R.string.sNotConnection));
			lblTitle.setText("");
		};
		
		if (logToken == null) return;
		
		if (logToken.length() != 0){
			mLblLogonText = getString(R.string.sOkLogin) + " " + logName;
			lblLogin.setTextColor(Color.GREEN);
		} else{
			mLblLogonText = getString(R.string.sNotLogin);
			lblLogin.setTextColor(Color.RED);
		}
		lblLogin.setText(mLblLogonText);
	}

	View rootView;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
		lblLogin = (TextView) rootView.findViewById(R.id.lblLogin);
//		mFormView = (View) rootView.findViewById(R.id.sec1);
//		mStatusView = (View) getActivity().findViewById(R.id.layout_status);
		lblTitle = (TextView) rootView.findViewById(R.id.lblTitle);
		lblTitle.setText("");
		
		lvMain = (ListView) rootView.findViewById(R.id.lvMain);
		
		
		lvMain.setClickable(true);
		lvMain.setOnItemClickListener(new OnItemClickListener() {
			 @Override
			  public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			    Rate val = (Rate) lvMain.getItemAtPosition(position);
                Toast.makeText(getActivity().getApplicationContext(), val.ValName, Toast.LENGTH_SHORT).show();
			  }			
		});
				
				
		TabSelected();
		
		LoadRate();
			
		return rootView;
	}
	
	public Boolean aResult = false;
	
	public void LoadRate(){
		mt = new CurrSendPost(null, null, aResult);			
		adapter = new RateArrayAdapter(this.getActivity(), rates);		
		lvMain.setAdapter(adapter);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		String sCurrDate = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
		nameValuePairs.add(new BasicNameValuePair("date_req", sCurrDate)); 
		mt.mActivity = this.getActivity() ; 			
//		mt.mFormView = mFormView;
//		mt.mStatusView = mStatusView;
		mt.Url = "http://www.cbr.ru/scripts/XML_daily.asp";   
		mt.Params = nameValuePairs;
		mt.execute(mt.Url);
//		mt.showProgress(true);
		
/*		
		rates.add(adapter.getCount(), new Rate("123", "1", "1", "xxxxxxxxxxxxxxxx"));
		rates.add(adapter.getCount(), new Rate("123", "1", "1", "xxxxxxxxxxxxxxxx"));
		rates.add(adapter.getCount(), new Rate("123", "1", "1", "xxxxxxxxxxxxxxxx"));
*/		
		
		
	}
	
	public CurrSendPost mt;
	
	
	public class CurrSendPost extends SendPost {
		String sRateList = "";
		
		public CurrSendPost(String Url, List<NameValuePair> Params, Boolean Result) {
			super(Url, Params, Result);

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			super.onPostExecute(success);			
			sRateList = mt.sMessage;

			mt.showProgress(true);

			if (success) {

				String LOG_TAG = "MyRate";
				String tmp, tmpCharCode, tmpValue, tmpNominal, sCourTag, tmpName;
				tmpCharCode = tmpValue = tmpNominal = sCourTag = tmpName = "";
				try {
			      XmlPullParser xpp = prepareXpp(sRateList);
			      
			      while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) { // 1
			        switch (xpp.getEventType()) {
			        	// начало документа
			        	case XmlPullParser.START_DOCUMENT: // 0
				          Log.d(LOG_TAG, "START_DOCUMENT");
				          break;
				          
				        // начало тэга
			        	case XmlPullParser.START_TAG: // 2
				          Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName() + ", depth = " + xpp.getDepth() + ", attrCount = " + xpp.getAttributeCount());
				          tmp = "";
				          for (int i = 0; i < xpp.getAttributeCount(); i++) {
				            tmp = tmp + xpp.getAttributeName(i) + " = " + xpp.getAttributeValue(i) + ", ";
				          }
				          if (!TextUtils.isEmpty(tmp))
				            Log.d(LOG_TAG, "Attributes: " + tmp);
				          
				          sCourTag = xpp.getName();
				          
				          if (sCourTag.equals("ValCurs")) {
					          for (int i = 0; i < xpp.getAttributeCount(); i++) {
						            if (xpp.getAttributeName(i).equals("Date"))
						            	lblTitle.setText(String.format(getString(R.string.sTitleSec1), xpp.getAttributeValue(i)));
						          }
				          } 
				          break;
				          
				        // конец тэга
				        case XmlPullParser.END_TAG: // 3
				          Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
				          
				          sCourTag = "";
				          break;
				          
				        // содержимое тэга
				        case XmlPullParser.TEXT: // 4
				          Log.d(LOG_TAG, "text = " + xpp.getText());
				          
				          if (sCourTag.equals("CharCode")) {
				        	  tmpCharCode = xpp.getText();
				          } else if (sCourTag.equals("Value")) {
				        	  tmpValue = xpp.getText();
				          } else if (sCourTag.equals("Nominal")){
				        	  tmpNominal = xpp.getText();
				          } else if (sCourTag.equals("Name")){
				        	  tmpName = xpp.getText();
				          } 				          
				          break;
				          
				        default:
				          break;
			        } // case
			        
			        if (tmpCharCode.length() != 0 && tmpValue.length() != 0 && tmpNominal.length() != 0 && tmpName.length() != 0) {
			        	rates.add(adapter.getCount(), new Rate(tmpCharCode, tmpValue, tmpNominal, tmpName));
			        	tmpCharCode = tmpValue = tmpNominal = tmpName = "";
			        }
			        // следующий элемент
			        xpp.next();
			        
			    } // while
				
		    } catch (XmlPullParserException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		} else { // success
				
		}
		mt.showProgress(false);
		mt = null;
		adapter.notifyDataSetChanged();
		//Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
	}

		
		private XmlPullParser prepareXpp(String sXML) throws XmlPullParserException {
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
		
		@Override
        protected void onPreExecute() {
		    super.onPreExecute();
		    showProgress(true);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mt = null;
		}
	}	
}

