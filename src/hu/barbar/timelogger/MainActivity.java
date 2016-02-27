package hu.barbar.timelogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import hu.barbar.timelogger.util.FileHandler;

public class MainActivity extends Activity {

	public static String DATEFORMAT = "MM-dd HH:mm:ss - ";	//"yyyy-MM-dd HH:mm:ss"
	
	private TextView textViewTime = null;
	private Button btnTest = null;
	private EditText logArea = null;
	protected ImageButton btnDrive = null;
	private ImageButton btnParking = null;
	private ImageButton btnCheckIn = null;
	private ImageButton btnCheckOut = null;
	private ImageButton btnWork = null;
	private ImageButton btnWorkEnd = null;
	private ImageButton btnInfo = null;
	
	public static final int DRIVE = 0,
							PARKING = 1,
							CHECK_IN = 2,
							CHECK_OUT = 3,
							WORK = 4,
							WORK_END = 5,
							INFO = 6;
	
	private OnClickListener myOnClickListener = null;
	
	
	private FileHandler myFileHandler = null; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myFileHandler = new FileHandler(MainActivity.this);
		
		initGUI();
	}
	
	private void initGUI(){
		
		textViewTime = (TextView) findViewById(R.id.tv_time);
		
		myOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(v == btnDrive){
					onButtonPressed(MainActivity.DRIVE);
				}else
				if(v == btnParking){
					onButtonPressed(MainActivity.PARKING);
				}else
				if(v == btnCheckIn){
					onButtonPressed(MainActivity.CHECK_IN);
				}else
				if(v == btnCheckOut){
					onButtonPressed(MainActivity.CHECK_OUT);
				}else
				if(v == btnWork){
					onButtonPressed(MainActivity.WORK);
				}else
				if(v == btnWorkEnd){
					onButtonPressed(MainActivity.WORK_END);
				}else
				if(v == btnInfo){
					onButtonPressed(MainActivity.INFO);
				}
					
			}
		};
		
		btnDrive = (ImageButton) findViewById(R.id.btn_drive);
		btnParking = (ImageButton) findViewById(R.id.btn_parking);
		btnCheckIn = (ImageButton) findViewById(R.id.btn_checkin);
		btnCheckOut = (ImageButton) findViewById(R.id.btn_checkout);
		btnWork = (ImageButton) findViewById(R.id.btn_work);
		btnWorkEnd = (ImageButton) findViewById(R.id.btn_go_home);
		btnInfo = (ImageButton) findViewById(R.id.btn_info);
		
		btnDrive.setOnClickListener(myOnClickListener);
		btnParking.setOnClickListener(myOnClickListener);
		btnCheckIn.setOnClickListener(myOnClickListener);
		btnCheckOut.setOnClickListener(myOnClickListener);
		btnWork.setOnClickListener(myOnClickListener);
		btnWorkEnd.setOnClickListener(myOnClickListener);
		btnInfo.setOnClickListener(myOnClickListener);
		
		logArea = (EditText) findViewById(R.id.log_area);
		
	}
	

	
	public void onButtonPressed(int btnId){
		//TODO
		
		switch (btnId) {
		case MainActivity.DRIVE:
			logArea.append(getCurrentTimeStamp() + "Driving\n");
			break;
		case MainActivity.PARKING:
			logArea.append(getCurrentTimeStamp() + "Stop driving\n");
			break;
		case MainActivity.CHECK_IN:
			logArea.append(getCurrentTimeStamp() + "Check in\n");
			break;
		case MainActivity.CHECK_OUT:
			logArea.append(getCurrentTimeStamp() + "Check out\n");
			break;	
		case MainActivity.WORK:
			logArea.append(getCurrentTimeStamp() + "Start worktime\n");
			break;
		case MainActivity.WORK_END:
			logArea.append(getCurrentTimeStamp() + "End worktime\n");
			break;
		case MainActivity.INFO: 
			logArea.append(getCurrentTimeStamp() + "Info\n");
			break;
			
		default:
			break;
		}
		
	}
	
	
	@Override
	protected void onPause() {
		
		// save log content without timestamp
		int res = myFileHandler.saveFile(getLinesFromLogArea(), false);
		if(res != FileHandler.RESULT_OK)
			show("Result: " + res);
		
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		//Load data from file
		ArrayList<String> lines = myFileHandler.loadFile();
		
		if(lines == null){
		
			Toast.makeText(getApplicationContext(), "Error accourse while try to load file..", Toast.LENGTH_SHORT).show();
			
		}else{
			
			String buffer = "";
			for(int i=0; i<lines.size(); i++){
				buffer += lines.get(i) + "\n";
			}
			
			//show loaded data
			logArea.setText(buffer);
			
		}
			
			
	}
	
	
	private void createNewLog(){
		// save log content with timestamp
		int res = myFileHandler.saveFile(getLinesFromLogArea(), true);
		if(res != FileHandler.RESULT_OK){
			show("Result: " + res);
		}else{
			logArea.setText("");
		}
		
	}
	
	
	private ArrayList<String> getLinesFromLogArea() {

		ArrayList<String> data = new ArrayList<String>();
		
		String[] lines = logArea.getText().toString().split("\n");
		for(int i=0; i<lines.length; i++){
			data.add(lines[i]);
		}
		
		return data;
	}

	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat(MainActivity.DATEFORMAT);//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	
	public void show(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			Toast.makeText(getApplicationContext(), "NEW", 0).show();
			createNewLog();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
