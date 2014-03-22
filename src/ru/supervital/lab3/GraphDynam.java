package ru.supervital.lab3;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import ru.supervital.lab3.CurrDynam;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;



public class GraphDynam extends DrawView {
	public Activity mActivity;
	public Rate Valute;
	Date DateRange1;
	Date DateRange2;
	
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // Valute.Dynam
        if (Valute.Dynam == null || DateRange1 == null|| DateRange2 == null) return;
        

        convertCoord(Valute.Dynam, height); // конвертнули его

        float gX, gY;
        float nX = 0, nY = 0;
        int xScale = 1, yScale = 1;
        
        paint.setStrokeWidth(2);
        
        double days = daysBetween(DateRange1, DateRange2);
        
        double i = 1;
        double p = height / days;
        int rec = 0;
        float deltaY = 0;
        try 
        {
	        while  (i < p * days && rec < Valute.Dynam.size()-1) {
	        	gX = (float) i * xScale; 
	        	gY = (float) Valute.Dynam.get(rec).Rate * yScale + deltaY * 2;
	        	
	        	if (nX != 0 && nY != 0){
	        		paint.setColor(Color.BLUE);
	        		canvas.drawLine(nX, nY, gX, gY, paint);
	        		deltaY = nY-gY;
	        	}
	        	
	        	paint.setColor(Color.RED);
	        	canvas.drawPoint(gX, gY, paint);
	
	        	paint.setColor(Color.GREEN);
	        	paint.setTextSize(height/10);
	//        	canvas.drawText(String.valueOf(z[i-1]), gX, gY-3, paint);
	        	
	        	nX = gX;
	        	nY = gY;
	        	
	        	rec++;
	        	i = i + p;
	        } // while
        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
        
    }
	
    Boolean aResult;
    
    public void LoadDynam(){
		mt = new DynamSendPost(null, null, aResult);			

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("VAL_NM_RQ", Valute.ID));
		
		Calendar c = Calendar.getInstance(); 
		c.getTime(); 
		c.add(Calendar.DATE, 1);
		String sCurrDate2 = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
		c.add(Calendar.DATE, -30);
		String sCurrDate1 = new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
		
		nameValuePairs.add(new BasicNameValuePair("date_req1", sCurrDate1));
		nameValuePairs.add(new BasicNameValuePair("date_req2", sCurrDate2));
		
		
		mt.mActivity = mActivity;
//		http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=02/03/2001&date_req2=14/03/2013&VAL_NM_RQ=R01235		
		mt.Url = "http://www.cbr.ru/scripts/XML_dynamic.asp";
		mt.Params = nameValuePairs;
		mt.execute(mt.Url);
    }

    public GraphDynam(Context context) {
        super(context);
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
			
			
			if (success) {
				SimpleDateFormat xformat = new SimpleDateFormat("dd.MM.yyyy");
				
				Date tmpDate = null;
				float tmpValue = 0;

				String sCourTag = "";
		        
				try {
			      XmlPullParser xpp = prepareXpp(sRateList);
			      
			      Valute.Dynam = new ArrayList<CurrDynam>();
			      
			      while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) { // 1
			    	  
					switch (xpp.getEventType()) {
				        // начало тэга
			        	case XmlPullParser.START_TAG: // 2
				          sCourTag = xpp.getName();
				          try{
					          if (sCourTag.equals("Record")) {
						          for (int i = 0; i < xpp.getAttributeCount(); i++) 
							            if (xpp.getAttributeName(i).equals("Date"))
							            	tmpDate = xformat.parse(xpp.getAttributeValue(i));
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
			        	Valute.Dynam.add(new CurrDynam(tmpDate, tmpValue));
			        	tmpDate = null;
			        	tmpValue = 0; 
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

	} // onPostExecute

		
		@Override
        protected void onPreExecute() {
		    super.onPreExecute();
		    showProgress(true);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
//			mt = null;
		}
	}	
	
	
	public static long daysBetween(Date startDate, Date endDate) {
		return (long) ((endDate.getTime() - startDate.getTime())/(1000*60*60*24));
	}	
}
