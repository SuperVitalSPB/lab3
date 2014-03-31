package ru.supervital.lab3;

import java.sql.Date;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DSF_Graph extends DummySectionFragment {

	GraphDynam DynamValute;
	TextView lblCap;
	RadioGroup rgPeriod;
	public XYPlot oneChart;
	RadioButton rbMonth, rbQuarter, rbHY, rbYear, rbAllData;
	private LinearLayout mProgressBar;
	
	public DSF_Graph() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dummy_graph, container, false);
		
		rbMonth = (RadioButton) rootView.findViewById(R.id.rbMonth);
		rbMonth.setChecked(true);

		rbQuarter = (RadioButton) rootView.findViewById(R.id.rbQuarter);
		rbHY = (RadioButton) rootView.findViewById(R.id.rbHY);
		rbYear = (RadioButton) rootView.findViewById(R.id.rbYear);
		rbAllData = (RadioButton) rootView.findViewById(R.id.rbAllData);
		
		rgPeriod = (RadioGroup) rootView.findViewById(R.id.rgPeriod);
		rgPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				DynamValute.LoadDynam(getPeriod());
				DrawDynam();
				oneChart.invalidate();
			}
		});
				
		lblCap = (TextView) rootView.findViewById(R.id.lblCap);
		
		mProgressBar = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);
		
		DynamValute = new GraphDynam();
		DynamValute.mActivity = (MainActivity) getActivity();
		DynamValute.mProgressBar = mProgressBar;

		setCaptionLabel();
	
		DynamValute.LoadDynam(getPeriod());

		oneChart = (XYPlot) rootView.findViewById(R.id.oneChart);

		return rootView;
	}
	


	public void DrawDynam(){
		if (DynamValute.Valute == null) return;
		oneChart.clear();
		
		SimpleDateFormat xformat = new SimpleDateFormat("dd.MM.yyyy");
		                                          // c %s по %s
		String sStr = String.format(getString(R.string.sDatePeriod),
								xformat.format(DynamValute.DateRange1), 
								xformat.format(DynamValute.DateRange2)); 
		
		SimpleXYSeries series = new SimpleXYSeries(DynamValute.Valute.Dates, 
        		                                   DynamValute.Valute.Rates, sStr);

        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.rgb(0, 0, 200), 
        		                                                        Color.rgb(0, 0, 100), null, null);
        oneChart.addSeries(series, series2Format);
       
		//oneChart.setTicksPerRangeLabel(1);
		setCaptionLabel();
		oneChart.setDomainValueFormat(new MyDateFormat());
		oneChart.redraw();
	}
	
	public void setCaptionLabel(){
		if (DynamValute.Valute == null) {
			lblCap.setText(getString(R.string.sSelVal));
			lblCap.setTextColor(Color.RED);
			return;
		} else lblCap.setText("загрузка.....");

		String sStr;			
		if (DynamValute.mt != null && DynamValute.mt.getStatus() != AsyncTask.Status.FINISHED) {
			if (DynamValute.Valute.Dates.size()>0){
				// Изменение курса %s по отношению к RR
				sStr = String.format(getString(R.string.sCapGr), DynamValute.Valute.Nominal + " " + DynamValute.Valute.Code);
				lblCap.setTextColor(Color.BLACK);
			} else {
				// Отсутствуют данные для построения графика
				sStr = getString(R.string.sNoData);
				lblCap.setTextColor(Color.RED);
			}
			lblCap.setText(sStr);
		}
		
	}

	public int getPeriod(){
		int CheckedRBId, SelectedPeriod, rbMonthId, rbQuarterId, rbHYId, rbYearId, rbAllDataId;
		rbMonthId = rbMonth.getId();  
		rbQuarterId = rbQuarter.getId(); 
		rbHYId =  rbHY.getId();
		rbYearId = rbYear.getId();
		rbAllDataId = rbAllData.getId();
		CheckedRBId = rgPeriod.getCheckedRadioButtonId();
		if (CheckedRBId == rbMonthId)  SelectedPeriod = Integer.parseInt(rbMonth.getTag().toString());
			else if (CheckedRBId == rbQuarterId) SelectedPeriod = Integer.parseInt(rbQuarter.getTag().toString());
			else if (CheckedRBId == rbHYId) SelectedPeriod = Integer.parseInt(rbHY.getTag().toString());
			else if (CheckedRBId == rbYearId) SelectedPeriod = Integer.parseInt(rbYear.getTag().toString());
			else if (CheckedRBId == rbAllDataId) SelectedPeriod = Integer.parseInt(rbAllData.getTag().toString());
			else SelectedPeriod = 30;
		
		return SelectedPeriod;
	}
	
	
	private class MyDateFormat extends Format { 

	    private static final long serialVersionUID = 1L;
	    
	    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd");

	    @Override
	    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
	        long timestamp = ((Number) obj).longValue() * 1000000000;
	        Date date = new Date(timestamp);
	        return dateFormat.format(date, toAppendTo, pos);
	    }

	    @Override
	    public Object parseObject(String source, ParsePosition pos) {
	        return null;    
	    }
	}
	
}
