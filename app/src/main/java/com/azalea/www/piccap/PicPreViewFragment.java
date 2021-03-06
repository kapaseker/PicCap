package com.azalea.www.piccap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * A placeholder fragment containing a simple view.
 */
public class PicPreViewFragment extends Fragment {

    private ImageView mImageShow = null;
    private String mImagePath = null;
    private Button mBtnShowList = null;

    public PicPreViewFragment() {
    }

    public static PicPreViewFragment newInstance(){return new PicPreViewFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle dataBd = getActivity().getIntent().getExtras();
        handleData(dataBd);
    }


    private void handleData(Bundle data){
        if(data==null) return;
        mImagePath = data.getString(PicPreViewActivity.BUNDLE_PIC_FILE_PATH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View capView = inflater.inflate(R.layout.fragment_pic_pre_view, container, false);
        mImageShow = (ImageView)capView.findViewById(R.id.img_prev);
        mBtnShowList = (Button)capView.findViewById(R.id.btn_showAll);

        mBtnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent();
                listIntent.setClass(getActivity(),ListPicActivity.class);
                listIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(listIntent);
            }
        });

        return capView;
    }


    @Override
    public void onStart() {
        super.onStart();

        if(mImagePath!=null){
            BitmapDrawable bdPic = PictureUtil.getScaledPictrue(getActivity(),mImagePath);
            if(bdPic!=null){
                mImageShow.setImageDrawable(bdPic);
            }else{
                Toast.makeText(getActivity(),"File Not Found",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtil.cleanImageView(mImageShow);
    }
}
