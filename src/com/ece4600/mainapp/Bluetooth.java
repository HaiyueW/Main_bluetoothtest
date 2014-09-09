package com.ece4600.mainapp;

import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Bluetooth extends Activity{
	
	Button blueon, blueoff, bluevisible, bluepair, bluesearch;
	private BluetoothAdapter myBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ListView listpaired, listnew;
	private ArrayAdapter<String> adapter, adapter_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		blueon = (Button)findViewById(R.id.blueon);
		blueoff = (Button)findViewById(R.id.blueoff);
		bluevisible = (Button)findViewById(R.id.bluevisible);
		bluepair = (Button)findViewById(R.id.bluepair);
		bluesearch = (Button)findViewById(R.id.bluesearch);
		listpaired = (ListView)findViewById(R.id.bluepairedlist);
		listpaired.setOnItemClickListener(mDeviceClickListener);
		listnew = (ListView)findViewById(R.id.bluenewlist);
		listnew.setOnItemClickListener(mDeviceClickListener);
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        ArrayAdapter<String> adapter_search = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listnew.setAdapter(adapter_search);
		
		bluesearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listpaired.setVisibility(View.GONE);
	            listnew.setVisibility(View.VISIBLE);
                doDiscovery();
			}     
	    });
	}
	
//	 protected void onDestroy() {
//	        super.onDestroy();
//	        // Make sure we're not doing discovery anymore
//	        if (myBluetoothAdapter != null) {
//	            myBluetoothAdapter.cancelDiscovery();
//	        }
//	        // Unregister broadcast listeners
//	        this.unregisterReceiver(mReceiver);
//	    }

	    private void doDiscovery() {
	        if (true)
	            Log.d("DeviceListActivity", "doDiscovery()");
	        // Indicate scanning in the title
	        setProgressBarIndeterminateVisibility(true);
	        if (myBluetoothAdapter.isDiscovering()) {
	        	myBluetoothAdapter.cancelDiscovery();
	        }
	        // Request discover from BluetoothAdapter
	        myBluetoothAdapter.startDiscovery();
	    }
	    
	final BroadcastReceiver mReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Toast.makeText(getApplicationContext(), "Searching Devices", Toast.LENGTH_SHORT).show();						
				adapter_search.add(device.getName() + "\n" +device.getAddress());
				adapter_search.notifyDataSetChanged();
			}else{
				Toast.makeText(getApplicationContext(), "No Devices has been found", Toast.LENGTH_SHORT).show();	
			}
		}		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		listpaired.setAdapter(adapter);
		switch (v.getId()) {
		case R.id.blueon:
			 if(myBluetoothAdapter==null){
				 Toast.makeText(getApplicationContext(), "Bluetooth service not available in the device", Toast.LENGTH_SHORT).show();
             }
			 else{
				 if(!myBluetoothAdapter.isEnabled()){
						Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(turnOn, 0);
						Toast.makeText(getApplicationContext(), "Bluetooth Turned ON", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "Bluetooth Already ON", Toast.LENGTH_SHORT).show();
					}
				 }			
			break;
		case R.id.blueoff:
			myBluetoothAdapter.disable();
			Toast.makeText(getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bluevisible:
			Intent discover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			startActivityForResult(discover, 0);
			break;
		case R.id.bluepair:
			pairedDevices = myBluetoothAdapter.getBondedDevices();
			if (pairedDevices == null || pairedDevices.size() == 0) { 
				Toast.makeText(getApplicationContext(), "No Paired Devices Found", Toast.LENGTH_SHORT).show();
			} else {
				for(BluetoothDevice device: pairedDevices){
					adapter.add(device.getName() + "\n" +device.getAddress());
					}
				Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
//				listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View viewClicked,int position, long id) {
//						Toast.makeText(getApplicationContext(), "Connected to paired device", Toast.LENGTH_SHORT).show();
//						startActivity(new Intent(Bluetooth.this, MainActivity.class));
//						finish();
//					}
//				});
			}
			break;
		default:
			break;
		}		     
	}
	 // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            myBluetoothAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra("device_address", address);
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
			Toast.makeText(getApplicationContext(), "Connected to Bluetooth device", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(Bluetooth.this, MainActivity.class));
			finish();
        }
    };
}
