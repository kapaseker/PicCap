package com.azalea.www.piccap;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ListPicFragment extends Fragment {

    ListView mListMain = null;
    List<File> mArrPicFiles = null;
    ListPicAdapter mPicsAdapter = null;

    public ListPicFragment() {
    }


    public ListPicFragment newInstance() {
        return new ListPicFragment();
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

        mArrPicFiles = Arrays.asList(getActivity().getFilesDir().listFiles());
        mPicsAdapter = new ListPicAdapter();
        Toast.makeText(getActivity(),mArrPicFiles.size()+" ",Toast.LENGTH_LONG).show();
        mListMain.setAdapter(mPicsAdapter);

//        new Thread(){
//            public void run() {
//                mArrPicFiles = Arrays.asList(getActivity().getFilesDir().listFiles());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(),mArrPicFiles.size()+" ",Toast.LENGTH_LONG).show();
//                        mPicsAdapter.notifyDataSetChanged();
//                    }
//                });
//
//            }
//        }.start();

    }

    private class PicViewHolder{
        TextView txt_Name;
    }

    private class ListPicAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            PicViewHolder viewHolder = null;
//            if(convertView==null){
//                viewHolder = new PicViewHolder();
//                viewHolder.txt_Name = new TextView(getActivity());
//                convertView = viewHolder.txt_Name;
//                convertView.setTag(viewHolder);
//            }else{
//                viewHolder = (PicViewHolder) convertView.getTag();
//            }
//
//            viewHolder.txt_Name.setText("This is Good");
            TextView tt = new TextView(getActivity());
            tt.setText("This is Good");
            return tt;
        }
    }
}
