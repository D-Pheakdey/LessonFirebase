package com.example.firebase_and;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<User> arraylist;
    private Activity myContext;

    public MyAdapter(Activity context, ArrayList<User> arraylist) {
        this.arraylist = arraylist;
        this.myContext = context;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View single_row = convertView;
        if (single_row == null) {
            LayoutInflater layoutInflater = myContext.getLayoutInflater();
            single_row = layoutInflater.inflate(R.layout.item, null);
        }

        TextView id = single_row.findViewById(R.id.userid);
        TextView name = single_row.findViewById(R.id.username);
        TextView pass = single_row.findViewById(R.id.userpassword);

        User myUser = arraylist.get(position);
        id.setText(myUser.getId());
        name.setText(myUser.getName());
        pass.setText(myUser.getPass());

        return single_row;
    }
    public  void textFilter(ArrayList<User>results){
        arraylist=new ArrayList<>();
        arraylist.addAll(results);
        notifyDataSetChanged();
    }
}
