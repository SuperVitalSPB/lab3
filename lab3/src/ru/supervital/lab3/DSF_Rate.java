package ru.supervital.lab3;

import java.io.IOException;
import android.widget.AdapterView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DSF_Rate extends DummySectionFragment {
	
	private RateArrayAdapter adapter;
	ArrayList<Rate> rates = new ArrayList<Rate>();
//--
	TextView lblTitle;
	ListView lvMain;
	private View mFormView;
	private View mStatusView;
	String sDateRate = "";
	final int MENU_DYNAM = 1;		

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		switch (v.getId()) {
			case R.id.lvMain:
				menu.add(0, MENU_DYNAM, 0, getString(R.string.sDynam));
			break;
		}
	}	
		
	 @Override
    public boolean onContextItemSelected(MenuItem item) {
       switch (item.getItemId()) {
       // пункты меню для tvColor
	    case MENU_DYNAM:
	    	AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
	    	LoadGraph(acmi.position);
	    	((MainActivity) getActivity()).mViewPager.setCurrentItem(1);
	    	break;
       }
       return super.onContextItemSelected(item);
    }	
	
	
	public void LoadGraph(int position){
		DSF_Graph fragment = (DSF_Graph) ((MainActivity) getActivity()).mSectionsPagerAdapter.getFragment(1);
		fragment.DynamValute.Valute = (Rate) lvMain.getItemAtPosition(position);
		fragment.DynamValute.aResult = false;
		fragment.DynamValute.mFormView = mFormView;
		fragment.DynamValute.mStatusView = mStatusView;
		fragment.DynamValute.LoadDynam(fragment.getPeriod());
	}

	
	public DSF_Rate() {
		super();
	}

	View rootView;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_dummy_rate, container, false);
		lblTitle = (TextView) rootView.findViewById(R.id.lblTitle);
		lblTitle.setText("");
		
		lvMain = (ListView) rootView.findViewById(R.id.lvMain);
		lvMain.setClickable(true);
		registerForContextMenu(lvMain);
		
 		mFormView = (View) rootView.findViewById(R.id.sec1);
 		mStatusView = (View) rootView.findViewById(R.id.ll_progressbar);
		
		
		TabSelected();
		
		lvMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			    Rate val = (Rate) lvMain.getItemAtPosition(position);
                Toast.makeText(getActivity().getApplicationContext(), val.Name, Toast.LENGTH_SHORT).show();
			}
		});
		
		LoadRate();
			
		
		return rootView;
	}
	

	
	public void TabSelected(){
		if (isOnline) { 
			lblTitle.setTextColor(Color.BLUE);
		} else {
			lblTitle.setText(getString(R.string.sNotLogin));
			lblTitle.setTextColor(Color.RED);
		}

	}
	

	public void setTitle(){
		String sStr = "";
		if (sDateRate.length() != 0) sStr = String.format(getString(R.string.sTitleSec1), sDateRate);
		lblTitle.setText(sStr);
	}
	
	public Boolean aResult = false;
	public void LoadRate(){
		if (rates.size()==0) {
			mt = new CurrSendPost(null, null, aResult);			
			adapter = new RateArrayAdapter(this.getActivity(), rates);		
			lvMain.setAdapter(adapter);
	
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			Calendar c = Calendar.getInstance(); 
			c.getTime(); 
			c.add(Calendar.DATE, 1);
			String sCurrDate = new SimpleDateFormat("dd.MM.yyyy").format(c.getTime());
			nameValuePairs.add(new BasicNameValuePair("date_req", sCurrDate)); 
			mt.mActivity = this.getActivity() ; 			
			mt.mFormView = mFormView;
			mt.mStatusView = mStatusView;
			mt.Url = "http://www.cbr.ru/scripts/XML_daily.asp";   
			mt.Params = nameValuePairs;
			mt.execute(mt.Url);
			mt.showProgress(true);
		} else {
			setTitle();
			lvMain.setAdapter(adapter);
			
		}
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
				
				
				String tmpCharCode, tmpValue, tmpNominal, sCourTag, tmpName, tmpID, tmpNumCode;				
				tmpCharCode = tmpValue = tmpNominal = sCourTag = tmpName = tmpID = tmpNumCode = "";
				try {
			      XmlPullParser xpp = prepareXpp(sRateList);
			      
			      while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) { // 1
			        switch (xpp.getEventType()) {
				        // начало тэга
			        	case XmlPullParser.START_TAG: // 2
				          
				          sCourTag = xpp.getName();
				          if (sCourTag.equals("ValCurs")) {
					          for (int i = 0; i < xpp.getAttributeCount(); i++) {
						            if (xpp.getAttributeName(i).equals("Date")){
						            	sDateRate = xpp.getAttributeValue(i);
						            	setTitle();
						                //lblTitle.setText(String.format(getString(R.string.sTitleSec1), xpp.getAttributeValue(i)));
						            }
						          }
				          } else if (sCourTag.equals("Valute")) {
					          for (int i = 0; i < xpp.getAttributeCount(); i++) {
						            if (xpp.getAttributeName(i).equals("ID"))
						            	tmpID = xpp.getAttributeValue(i);
						          }
				          } 
			          
				          break;
				          
				        // конец тэга
				        case XmlPullParser.END_TAG: // 3
				          sCourTag = "";
				          break;
				          
				        // содержимое тэга
				        case XmlPullParser.TEXT: // 4
				          if (sCourTag.equals("CharCode")) {
				        	  tmpCharCode = xpp.getText();
				          } else if (sCourTag.equals("Value")) {
				        	  tmpValue = xpp.getText();
				          } else if (sCourTag.equals("Nominal")){
				        	  tmpNominal = xpp.getText();
				          } else if (sCourTag.equals("Name")){
				        	  tmpName = xpp.getText();
				          } else if (sCourTag.equals("NumCode")){
				        	  tmpNumCode = xpp.getText();
				          } 				          
				          break;
				          
				        default:
				          break;
			        } // case
			        
			        if (tmpCharCode.length() != 0 && tmpValue.length() != 0 && tmpNominal.length() != 0 && tmpName.length() != 0
			        	&& tmpID.length() != 0 && tmpNumCode.length() != 0) {
			        	
			        	Rate rate = new Rate(tmpCharCode, Double.valueOf(tmpValue.trim().replace(',','.')).doubleValue(), tmpNominal, tmpName);
			        	rate.ID = tmpID;
			        	rate.NumCode = tmpNumCode;
			        	rates.add(adapter.getCount(), rate);
			        	
			        	tmpCharCode = tmpValue = tmpNominal = sCourTag = tmpName = tmpID = tmpNumCode = "";
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
			String sMsg = "Сервер ЦБ, подлец, не вернул данные!!!!\n" + sRateList;
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("Ошибка");
			builder.setMessage(sMsg);
			builder.setCancelable(true);
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        mActivity.finish();					
			    }
			});
			AlertDialog dialog = builder.create();
		//Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
		}
		adapter.notifyDataSetChanged();
		mt.showProgress(false);
		mt = null;		
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
	
	} // class post
	
}
