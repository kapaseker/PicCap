package com.azalea.www.piccap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A placeholder fragment containing a simple view.
 */
public class CapFragment extends Fragment {

    Button mBtnCap = null;
    SurfaceView mSfvMain = null;
    Camera mCamera ;
    private FrameLayout mPblayout = null;
    private static final int DEFAULT_CAMERA_DEGREE = 90;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Camera.ShutterCallback mTakePicCallBack = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            if(mPblayout!=null){
                mPblayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private Camera.PictureCallback mJpgCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String fileName = UUID.randomUUID()+"-"+(new Date()).getTime()+".jpg";
            FileOutputStream fos = null;
            boolean isSuccess = false;

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                matrix.setRotate(90);
            }

            try {
                fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.write(data);
                isSuccess = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                isSuccess = false;
            }finally {
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        isSuccess = false;
                    }
                }
            }

            if(isSuccess){
//                Toast.makeText(getActivity(),"Pictrue "+fileName+" Saved",Toast.LENGTH_SHORT).show();
                Intent previewIntent = new Intent(getActivity(),PicPreViewActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString(PicPreViewActivity.BUNDLE_PIC_FILE_PATH, getActivity().getFileStreamPath(fileName).getAbsolutePath());
                previewIntent.putExtras(dataBundle);
                startActivity(previewIntent);

            }else{
                Toast.makeText(getActivity(),"Problem Caughted",Toast.LENGTH_SHORT).show();
            }

            if(mPblayout!=null){
                mPblayout.setVisibility(View.GONE);
            }
        }
    };

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

        File fileDir = getActivity().getFilesDir();
        File[] ff = fileDir.listFiles();
        Toast.makeText(getActivity(),ff.length+" Files in "+fileDir.getAbsolutePath(),Toast.LENGTH_SHORT).show();
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


    private void takePicture(){
        if(mCamera!=null){
            mCamera.takePicture(mTakePicCallBack,null,mJpgCallback);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View capView = inflater.inflate(R.layout.fragment_cap, container, false);

        mBtnCap = (Button) capView.findViewById(R.id.btn_cap);
        mSfvMain = (SurfaceView) capView.findViewById(R.id.sfv_main);
        mPblayout = (FrameLayout) capView.findViewById(R.id.pb_containner);

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

                Camera.Size paramSize = getBestPreviewSize(params.getSupportedPictureSizes(), width, height);
                params.setPreviewSize(paramSize.width, paramSize.height);
                mCamera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });

        mBtnCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                v.setEnabled(false);
            }
        });

        return capView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mBtnCap!=null){
            mBtnCap.setEnabled(true);
        }
    }
}
