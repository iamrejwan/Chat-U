package com.CodeJokeR.ChatGPT;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import okhttp3.*;
import org.json.*;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> bjj = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> mappy = new ArrayList<>();
	
	private LinearLayout linear4;
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout linear2;
	private LinearLayout linear7;
	private TextView textview1;
	private LinearLayout linear5;
	private RecyclerView recyclerview1;
	private LinearLayout linear6;
	private ImageView imageview1;
	private EditText edittext1;
	private TextView textview2;
	
	private SharedPreferences data;
	private RequestNetwork call;
	private RequestNetwork.RequestListener _call_request_listener;
	private TimerTask t;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear4 = findViewById(R.id.linear4);
		linear1 = findViewById(R.id.linear1);
		linear3 = findViewById(R.id.linear3);
		linear2 = findViewById(R.id.linear2);
		linear7 = findViewById(R.id.linear7);
		textview1 = findViewById(R.id.textview1);
		linear5 = findViewById(R.id.linear5);
		recyclerview1 = findViewById(R.id.recyclerview1);
		linear6 = findViewById(R.id.linear6);
		imageview1 = findViewById(R.id.imageview1);
		edittext1 = findViewById(R.id.edittext1);
		textview2 = findViewById(R.id.textview2);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		call = new RequestNetwork(this);
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext1.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "There must be prompt");
					return;
				}
				sendMessage(edittext1.getText().toString());
				edittext1.setText("");
			}
		});
		
		_call_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		if (!data.getString("chat", "").equals("")) {
			mappy = new Gson().fromJson(data.getString("chat", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setStackFromEnd(true);
		// layoutManager.setReverseLayout(true);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview1.setLayoutManager(layoutManager);
		
		recyclerview1.setAdapter(new Recyclerview1Adapter(mappy));
		linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF444653));
		_navigationBarColor("#353541");
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		if (!(mappy.size() == 0)) {
			data.edit().putString("chat", new Gson().toJson(mappy)).commit();
		}
	}
	public void _extra() {
	}
	
	private void sendMessage(String userMsg) {
			//below line is to pass message to our array list which is entered by the user.
			addResponse(1, userMsg);
			OpenAiClient openAiClient = new OpenAiClient();
			openAiClient.generateResponse(userMsg, new Callback() {
					@Override
					public void onResponse(Call call, Response response) throws IOException {
							String responseBody = response.body().string();
							// Do something with the response body
							addResponse(0, OpenAiClient.parseResponse(responseBody));
							// addResponse(0, responseBody);
					}
					
					@Override
					public void onFailure(Call call, IOException e) {
							// Handle the failure
							addResponse(0, "Something went wrong!");
					}
			});
			
	}
	
	public void addResponse(final int who, final String response) {
			t = new TimerTask() {
					@Override
					public void run() {
							runOnUiThread(new Runnable() {
									@Override
									public void run() {
											bjj = new HashMap<>();
											bjj.put("who", (who == 0) ? "bot" : "you");
											bjj.put("text", response);
											mappy.add(bjj);
											recyclerview1.getAdapter().notifyItemInserted(recyclerview1.getAdapter().getItemCount()-1);
											recyclerview1.scrollToPosition(recyclerview1.getAdapter().getItemCount()-1);
											
									}
							});
					}
			};
			_timer.schedule(t, (int)(0));
	}
	
	
	{
	}
	
	
	public void _navigationBarColor(final String _color) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { getWindow().setNavigationBarColor(Color.parseColor(_color)); }
	}
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.message, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			if (_data.get((int)_position).get("who").toString().equals("you")) {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, Color.TRANSPARENT));
				imageview1.setImageResource(R.drawable.avater_2);
			}
			else {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF444653));
				imageview1.setImageResource(R.drawable.avater_1);
			}
			textview1.setText(_data.get((int)_position).get("text").toString());
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}