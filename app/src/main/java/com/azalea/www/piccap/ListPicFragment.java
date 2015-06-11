package com.azalea.www.piccap;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ListPicFragment extends Fragment {

    ListView mListMain = null;
    List<File> mArrPicFiles = new ArrayList<File>();
    ListPicAdapter mPicsAdapter = new ListPicAdapter();

    public ListPicFragment() {
    }


    public ListPicFragment newInstance() {
        return new ListPicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View capView = inflater.inflate(R.layout.fragment_list_pic, container, false);
        mListMain = (ListView)capView.findViewById(R.id.lst_main);

        return capView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListMain.setAdapter(mPicsAdapter);

        new Thread(){
            public void run() {
                mArrPicFiles.clear();
                mArrPicFiles.addAll(Arrays.asList(getActivity().getFilesDir().listFiles()));
                Iterator<File> arrIter = mArrPicFiles.iterator();

                while(arrIter.hasNext()){
                    File ff = arrIter.next();
                    if(!ff.getName().endsWith(".jpg")){
                        arrIter.remove();
                    }
                }
                mPicsAdapter.notifyDataSetChanged();
            }
        }.start();

    }

    private class PicViewHolder{
        TextView txt_Name;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_pic,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_addpic:

                Intent takeIntent = new Intent();
                takeIntent.setClass(getActivity(),CapActivity.class);
                startActivity(takeIntent);

                break;
            case R.id.action_settings:
                break;
        }


        return true;
    }

    private class ListPicAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mArrPicFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrPicFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PicViewHolder viewHolder = null;
            if(convertView==null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.piclist_item,null);
                viewHolder = new PicViewHolder();
                viewHolder.txt_Name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (PicViewHolder) convertView.getTag();
            }

            viewHolder.txt_Name.setText(mArrPicFiles.get(position).getName());

            return convertView;
        }
    }
}
