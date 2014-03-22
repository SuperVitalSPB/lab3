package ru.supervital.lab3;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import ru.supervital.lab3.R;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	public final static String logTokenKey = "ru.supervital.lab3.resToken";
	public final static String logNameKey = "ru.supervital.lab3.logName";
	
		
	
	/**
	 * The default Login to populate the Login field with.
	 */
	public static final String EXTRA_Login = "com.example.android.authenticatordemo.extra.Login";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */

	// Values for Login and password at the time of the login attempt.
	private String mLogin;
	private String mPassword;

	// UI references.
	private EditText mLoginView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		setupActionBar();

		// Set up the login form.
		mLogin = getIntent().getStringExtra(EXTRA_Login);
		mLoginView = (EditText) findViewById(R.id.login);
		//mLoginView.setText(mLogin);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
						attemptLogin();
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	
	public Boolean aResult = false;
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid Login, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		
		// Reset errors.
		mLoginView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mLogin = mLoginView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		EditText focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid Login address.
		if (TextUtils.isEmpty(mLogin)) {
			mLoginView.setError(getString(R.string.error_field_required));
			focusView = mLoginView;
			cancel = true;
		}

cancel = false;

		if (cancel) {
			focusView.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					EditText eText;
					if (mLoginView.isFocused()) eText = mLoginView;
					else eText = mPasswordView; 
	                 // remove error sign
		             eText.setError(null);
				}

				@Override
				public void afterTextChanged(Editable arg0) {}
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			});
			focusView.requestFocus();
		} else {
			if (mt == null) mt = new LoginSendPost(null, null, aResult); else return;
			
			mt.mActivity = this; 			
			mt.mFormView = mLoginFormView;
			mt.mStatusView = mLoginStatusView;
			mt.Url = "http://www.cbr.ru/scripts/XML_daily.asp";   

			mt.execute(mt.Url);
		}
	}

	public LoginSendPost mt;

	public class LoginSendPost extends SendPost {
		
		public LoginSendPost(String Url, List<NameValuePair> Params, Boolean Result) {
			super(Url, Params, Result);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			super.onPostExecute(success);
			
			String sStr = mt.sMessage;
			mt = null;

			if (success) {
				Intent answerInent = new Intent();
				answerInent.putExtra(logNameKey, mLogin);
				answerInent.putExtra(logTokenKey, (mLogin.length() != 0 ? "result token": ""));
				setResult(RESULT_OK, answerInent);
				finish();
			} else {
				mPasswordView.setError(sStr); // getString(R.string.error_incorrect_password)
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mt = null;
		}
	}
}
