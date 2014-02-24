package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UsersActivity extends ListActivity {
	
	public class codeLeanUser {
		String userName;
		String userRole;
	}
	
	//CodeLearnAdapter userListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		
		//userListAdapter = new CodeLearnAdapter();
	        
	   // setListAdapter(userListAdapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    //	codeLeanUser user = userListAdapter.getCodeLearnUser(position);
	//	Toast.makeText(UsersActivity.this, user.userName,Toast.LENGTH_LONG).show();
		Intent i = new Intent(this, UserInfoActivity.class);
		startActivity(i);
    }
	/*
    public class CodeLearnAdapter extends BaseAdapter {

    	List<codeLeanUser> codeLeanUserList = getDataForListView();
		public int getCount() {
			// TODO Auto-generated method stub
			return codeLeanUserList.size();
		}

		public codeLeanUser getItem(int arg0) {
			// TODO Auto-generated method stub
			return codeLeanUserList.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			if(arg1==null)
			{
				LayoutInflater inflater = (LayoutInflater) UsersActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.row_users, arg2,false);
			}
			
			TextView userName = (TextView)arg1.findViewById(R.id.name);
			//TextView userRole = (TextView)arg1.findViewById(R.id.role);
			
			codeLeanUser user = codeLeanUserList.get(arg0);
			
			userName.setText(user.userName);
			//userRole.setText(user.userRole);
			
			return arg1;
		}
		
		public codeLeanUser getCodeLearnUser(int position)
		{
			return codeLeanUserList.get(position);
		}

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.users, menu);
        return true;
    }
    
    public List<codeLeanUser> getDataForListView()
    {
    	List<codeLeanUser> codeLeanUsersList = new ArrayList<codeLeanUser>();
    	
    	for(int i=0;i<10;i++)
    	{
    		
    		codeLeanUser user = new codeLeanUser();
    		user.userName = "User" + i;
    		user.userRole = "This is" + i;
    		codeLeanUsersList.add(user);
    	}
    	
    	return codeLeanUsersList;
    	
    }*/
}