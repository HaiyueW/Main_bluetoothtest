package com.ece4600.mainapp;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Bluetooth extends Activity{
	
	Button blueon, blueoff, bluevisible, bluepair, bluesearch;
	private BluetoothAdapter BA;
	private Set<BluetoothDevice> pairedDevices;
	private ListView listv;
	private ArrayAdapter<String> adapter_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		blueon = (Button)findViewById(R.id.blueon);
		blueoff = (Button)findViewById(R.id.blueoff);
		bluevisible = (Button)findViewById(R.id.bluevisible);
		bluepair = (Button)findViewById(R.id.bluepair);
		bluesearch = (Button)findViewById(R.id.bluesearch);
		listv = (ListView)findViewById(R.id.bluelist);
		BA = BluetoothAdapter.getDefaultAdapter();
	}

	
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
		switch (v.getId()) {
		case R.id.blueon:
			if(!BA.isEnabled()){
				Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(turnOn, 0);
				Toast.makeText(getApplicationContext(), "Bluetooth Turned ON", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Bluetooth Already ON", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.blueoff:
			BA.disable();
			Toast.makeText(getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
			break;
		case R.id.bluevisible:
			Intent discover = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			startActivityForResult(discover, 0);
			break;
		case R.id.bluepair:
			pairedDevices = BA.getBondedDevices();
			ArrayList<String> list = new ArrayList<String>();
			for(BluetoothDevice bt: pairedDevices){
				list.add(bt.getName());
				Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
				listv.setAdapter(adapter);
			}
			break;
		case R.id.bluesearch:
			//Search.startDiscovery();
			final BroadcastReceiver mReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if(BluetoothDevice.ACTION_FOUND.equals(action)){
						BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						Toast.makeText(getApplicationContext(), "Searching Devices", Toast.LENGTH_SHORT).show();
						adapter_search.add(device.getName() + "\n" +device.getAddress());
						adapter_search.notifyDataSetChanged();
						listv.setAdapter(adapter_search);
					}
					
				}
				
			};
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter);
			break;
		default:
			break;
		}
		
	}
}
