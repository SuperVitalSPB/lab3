package ru.supervital.lab3;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import ru.supervital.lab3.CurrDynam;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;



public class GraphDynam  {
	public MainActivity mActivity;
	public Rate Valute;
	public Date DateRange1;
	public Date DateRange2;
	public View mFormView;
	public View mStatusView;
	public LinearLayout mProgressBar;
	
    public Boolean aResult = false;
    
    public void LoadDynam(int aPeriod){
    	if (Valute == null) return; // || aResult) return;
    	aResult = true;
		mt = new DynamSendPost(null, null, aResult);			
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("VAL_NM_RQ", Valute.ID));
		
		Calendar c = Calendar.getInstance(); 
		c.getTime(); 
		c.add(Calendar.DATE, 1);
		String sCurrDate2 = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
		c.add(Calendar.DATE, -aPeriod);
		String sCurrDate1 = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
		
		nameValuePairs.add(new BasicNameValuePair("date_req1", sCurrDate1));
		nameValuePairs.add(new BasicNameValuePair("date_req2", sCurrDate2));
		
		mt.mActivity = mActivity;
//		http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=02/03/2001&date_req2=14/03/2013&VAL_NM_RQ=R01235		
		mt.Url = "http://www.cbr.ru/scripts/XML_dynamic.asp";
		mt.Params = nameValuePairs;
		mt.mProgressBar = mProgressBar;
		
		mt.execute(mt.Url);
    }

    
    public DynamSendPost mt;
	public class DynamSendPost extends SendPost {
		String sRateList = "";
		
		public DynamSendPost (String Url, List<NameValuePair> Params, Boolean Result) {
			super(Url, Params, Result);
		}

		@Override
		protected void onPostExecute(final Boolean success){
			super.onPostExecute(success);			
			sRateList = mt.sMessage;
			mt.showProgress(true);
			
			float maxValue = 0, minValue = 0;			
			
			if (success) {
				SimpleDateFormat xformat = new SimpleDateFormat("dd.MM.yyyy");

				
				String tmpDate = null;
				float tmpValue = 0;
				String sCourTag = "";
				long longDate;
				int iD = 0;
				
				try {
			      XmlPullParser xpp = prepareXpp(sRateList);
			      
			      Valute.Dynam = new ArrayList<CurrDynam>();
			      Valute.Dates.clear(); 
                  Valute.Rates.clear();
			      
			      while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) { // 1
			    	  
					switch (xpp.getEventType()) {
				        // начало тэга
			        	case XmlPullParser.START_TAG: // 2
				          sCourTag = xpp.getName();
				          try{
					          if (sCourTag.equals("Record")) {
						          for (int i = 0; i < xpp.getAttributeCount(); i++) 
							            if (xpp.getAttributeName(i).equals("Date"))
							            	tmpDate = xpp.getAttributeValue(i);
				            	
					          } if (sCourTag.equals("ValCurs")) {
						          for (int i = 0; i < xpp.getAttributeCount(); i++) {
							            if (xpp.getAttributeName(i).equals("DateRange1"))
							            	DateRange1 = xformat.parse(xpp.getAttributeValue(i).replace('/', '.'));
							            else if (xpp.getAttributeName(i).equals("DateRange2"))
							            	DateRange2 = xformat.parse(xpp.getAttributeValue(i).replace('/', '.'));
							          }
					          }
		            	}catch(ParseException ex){
		            		ex.printStackTrace();
		            	}
				          break;
				        // конец тэга
				        case XmlPullParser.END_TAG: // 3
				          sCourTag = "";
				          break;
				        // содержимое тэга
				        case XmlPullParser.TEXT: // 4
				          if (sCourTag.equals("Value")) {
				        	  tmpValue = Float.valueOf(xpp.getText().replace(',', '.'));
				          } 				          
				          break;
				        default:
				          break;
			        } // case
			        
			        if (tmpDate != null && tmpValue != 0) {
			        	Locale locale = new Locale("ru","RU");
			            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			        	Date date = dateFormat.parse(tmpDate);
			        	
			        	Valute.Dynam.add(new CurrDynam(date, tmpValue));
			        	Valute.Rates.add(tmpValue);
			        	Valute.Dates.add(date.getTime());
			        	
			        	if (tmpValue > maxValue) 
			        			maxValue = tmpValue;
			        	
			        	if (minValue == 0 || tmpValue < minValue) 
			        			minValue = tmpValue;
			        	
			        	tmpDate = null;
			        	tmpValue = 0; 
			        }
			        // следующий элемент
			        xpp.next();
			        
			    } // while
			} catch (Exception exception) {
	    	    Log.e("lab3", "Получено исключение", exception);
	    	}			      
/*				
		    } catch (XmlPullParserException e) {
		    	e.printStackTrace();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
			catch (java.text.ParseException e) {
		            e.printStackTrace();
			}
				*/
		} else { // success
			String sMsg = "Сервер ЦБ, подлец, не вернул данные!!!!\n" + sRateList;
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("Ошибка");
			builder.setMessage(sMsg);
			builder.setCancelable(true);
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        // mActivity.finish();					
			    }
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		Valute.maxRate = maxValue;
		Valute.minRate = minValue;
			
		
		DSF_Graph fragment = ((DSF_Graph) GraphDynam.this.mActivity.mSectionsPagerAdapter.Fragments[1]);
		fragment.setCaptionLabel();
		fragment.DrawDynam();
		mt.showProgress(false);
		mt = null;
	} // onPostExecute

		
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
	
	
	public static long daysBetween(Date startDate, Date endDate) {
		return (long) ((endDate.getTime() - startDate.getTime())/(1000*60*60*24));
	}	
}

/*
  			        	longDate =  System.currentTimeMillis() + iD; //tmpDate.getTime(); // (tmpDate.getTime() % (86400000));
			        	
			        	
			        	iD = iD + 100;
			        	
			        	DateFormat TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        	 
			        	Log.i("MyApp", TIMESTAMP.format(longDate));
			        	
//			        	Valute.Dates.add(Valute.Rates.size());//tmpDate);
//			            Number[] years = {
//			                    978307200,  // 2001
//			                    1009843200, // 2002
//			                    1041379200, // 2003
//			                    1072915200, // 2004
//			                    1104537600  // 2005
//			            };
//			        	Valute.Dates.add(years[Valute.Rates.size() % years.length]);

  
 */