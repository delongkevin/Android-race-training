package com.technerds.racelogger.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technerds.racelogger.dataModels.UploadFileModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.UploadFileViewModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class CameraActivity extends AppCompatActivity implements OnCompressListener {
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    RelativeLayout progressLayout;
    ImageView imgCapture, imageFlipCamera;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    int c = 0;
    UploadFileViewModel uploadFileViewModel;
    private CameraX.LensFacing lensFacing = CameraX.LensFacing.BACK;
    ImageCapture imgCap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        textureView = findViewById(R.id.view_finder);
        imgCapture = findViewById(R.id.imgCapture);
        imageFlipCamera = findViewById(R.id.imageFlipCamera);
        progressLayout = findViewById(R.id.progressLayout);
        
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        
        setupViewModels();
        
        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    
    public void setupViewModels() {
        uploadFileViewModel = new ViewModelProvider(this).get(UploadFileViewModel.class);
        uploadFileObserveViewModel(uploadFileViewModel);
        
    }
    
    private void startCamera() {
        
        CameraX.unbindAll();
        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen
        
        
        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);
        
        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);
                        
                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });
        
        
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
    
        imgCap = new ImageCapture(imageCaptureConfig);
        imageFlipCamera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if (CameraX.LensFacing.FRONT == lensFacing) {
                    lensFacing = CameraX.LensFacing.BACK;
                } else {
                    lensFacing = CameraX.LensFacing.FRONT;
                }
                Log.wtf("-Camera", "Lens Facing : " + lensFacing.name());
                try {
                    // Only bind use cases if we can query a camera with this orientation
                    CameraX.getCameraWithLensFacing(lensFacing);
                    bindCameraUseCases();
                } catch (CameraInfoUnavailableException e) {
                    // Do nothing
                }
            }
        });
        
        findViewById(R.id.imgCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   File file = new File(Environment.getExternalStorageDirectory() + "/pictures/RMW/" + System.currentTimeMillis() + ".png");
                //   File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/RMW");
                progressLayout.setVisibility(View.VISIBLE);
                File folder = new File(Environment.getExternalStorageDirectory() + "/ALC_Pics/");
                if (!folder.exists())
                    folder.mkdir();
                String imgName = "test";
                Log.wtf("-this", "ImageName  : " + imgName);
                File file = new File(folder + "/" + imgName + ".jpg");
                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        Handler handler = new Handler();

                        final Runnable r = new Runnable() {
                            public void run() {
                                getCompressedImage(file);
                            }
                        };
                        handler.postDelayed(r, 100);

                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                        Log.wtf("-Camera", "Error : " + msg);
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                });
            }
        });
        
        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner) this, preview, imgCap);
    }
    
    private void bindCameraUseCases() {
        // Make sure that there are no other use cases bound to CameraX
        CameraX.unbindAll();
        
     
        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen
    
    
        PreviewConfig previewConfig = new PreviewConfig.Builder().
                setLensFacing(lensFacing)
                .build();
        Preview preview = new Preview(previewConfig);
    
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setLensFacing(lensFacing)
                .build();
    
        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);
                    
                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });
        
        imgCap = new ImageCapture(imageCaptureConfig);
        
        
        // Apply declared configs to CameraX using the same lifecycle owner
        CameraX.bindToLifecycle((LifecycleOwner) this, preview, imgCap);
    }
    
    private void updateTransform() {
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();
        
        float cX = w / 2f;
        float cY = h / 2f;
        
        int rotationDgr;
        int rotation = (int) textureView.getRotation();
        
        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }
        
        mx.postRotate((float) rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    private boolean allPermissionsGranted() {
        
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    public void uploadPic(File file) {
        
        Log.wtf("-this", " File After Comppressed : " + file.getPath());
      //  String token = mySharedPreference.getString(MyShardPreferences.token, "");
        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("image/jpg"));
        
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        c = 0;
        uploadFileViewModel.uploadFile( body);
    }
    
    private void uploadFileObserveViewModel(final UploadFileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "UploadFile API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "UploadFile response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            UploadFileModel uploadFileModel = null;
                            if (status == 1) {
                                Gson gson = new Gson();
                                uploadFileModel = gson.fromJson(json, UploadFileModel.class);
                                
                            } else {
                                String error = jsonObject.getString("error");
                                uploadFileModel.setStatus(status);
                                uploadFileModel.setError(error);
                                
                            }
                            
                            if (uploadFileModel != null) {
                                if (c == 0) {
                                    c++;
                                    if (uploadFileModel.getStatus() == 1) {
                                        Snackbar.make(textureView, "" + uploadFileModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("fileName", uploadFileModel.getData().getFile_name());
                                        intent.putExtra("path", uploadFileModel.getData().getPath());
                                        setResult(2, intent);
                                        finish();//finishing activity
                                    } else {
                                        Snackbar.make(textureView, "" + uploadFileModel.getError(), Snackbar.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("fileName", "");
                                        intent.putExtra("path", "");
                                        setResult(2, intent);
                                        finish();//finishing activity
                                    }
                                }
                            } else {
                                Log.wtf("-this", "Upload File Camera Response : Null");
                                Intent intent = new Intent();
                                intent.putExtra("fileName", "");
                                intent.putExtra("path", "");
                                setResult(2, intent);
                                finish();//finishing activity
                            }
                            
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "UploadFile API Response : " + apiResponse.error.getMessage());
                }
            }
        });
    }
    
    public void getCompressedImage(File file) {
        Luban.compress(this, file)
                .putGear(Luban.FIRST_GEAR)      // set the compress mode, default is : THIRD_GEAR
                .launch(this);
    }
    
    public void dummyBgClicked(View view) {
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public void onSuccess(File file) {
        uploadPic(file);
    }
    
    @Override
    public void onError(Throwable e) {
    
    }
}
