package com.lab3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

	public class DummySectionFragment2 extends Fragment {
		TextView dummyTextView;
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment2() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy2, container, false);
			dummyTextView = (TextView) rootView.findViewById(R.id.lblSection);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)) + "");
			return rootView;
		}
	}

