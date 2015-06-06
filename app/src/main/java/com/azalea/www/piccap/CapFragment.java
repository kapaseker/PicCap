package com.azalea.www.piccap;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class CapFragment extends Fragment {

    Button mBtnCap = null;
    SurfaceView mSfvMain = null;
    Camera mCamera ;

    private static final int DEFAULT_CAMERA_DEGREE = 90;

    public CapFragment() {
    }

    public static CapFragment newInstance(){
        return new CapFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
            mCamera = Camera.open(0);
        }else{
            mCamera = Camera.open();
        }

        mCamera.setDisplayOrientation(DEFAULT_CAMERA_DEGREE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getBestPreviewSize(List<Camera.Size> szs,int width,int height){
        Camera.Size bestSize = szs.get(0);

        int largeAre = bestSize.width*bestSize.height;

        for(Camera.Size sz:szs){
            int area = sz.width*sz.height;
            if(area>largeAre){
                bestSize=sz;
                largeAre = area;
            }
        }

        return bestSize;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View capView = inflater.inflate(R.layout.fragment_cap, container, false);

        mBtnCap =(Button) capView.findViewById(R.id.btn_cap);
        mSfvMain = (SurfaceView)capView.findViewById(R.id.sfv_main);

        SurfaceHolder sfHolder = mSfvMain.getHolder();
        sfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        sfHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) return;

                Camera.Parameters params = mCamera.getParameters();

                Camera.Size paramSize = getBestPreviewSize(params.getSupportedPictureSizes(),width,height);
                params.setPreviewSize(paramSize.width,paramSize.height);
                mCamera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });

        return capView;
    }

}
