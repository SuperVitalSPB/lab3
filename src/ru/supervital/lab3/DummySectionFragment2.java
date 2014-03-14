package ru.supervital.lab3;

import ru.supervital.lab3.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

	public class DummySectionFragment2 extends Fragment {
		Button btn;
		LinearLayout ll; 
		GraphDynam gd;
		
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment2() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy2, container, false);
//			btn = (Button) rootView.findViewById(R.id.button1);
			ll = (LinearLayout) rootView.findViewById(R.id.ll);

			gd = new GraphDynam(ll.getContext());
			ll.addView(gd);
	        gd.invalidate();
/*!!!!!!!!!!!!*/	     
	        /*
	    	<NumCode>840</NumCode>
	    	<CharCode>USD</CharCode>
	    	<Nominal>1</Nominal>
	    	<Name>Äמככאנ ÑØÀ</Name>
	    	<Value>33,0006</Value>
	        */
gd.Valute = new Rate("USD", 123, "1", "Äמככאנ ÑØÀ");
gd.Valute.ID = "R01235";
gd.Valute.NumCode = "840";
/*!!!!!!!!!*/
	        gd.LoadDynam();
			
			return rootView;
		}
	}

