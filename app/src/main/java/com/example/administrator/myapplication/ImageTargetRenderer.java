/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.example.administrator.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Utils.MyDataObject;
import com.example.administrator.myapplication.Utils.PagerAdapter;
import com.example.administrator.myapplication.Utils.UpdateableFragment;
import com.example.administrator.myapplication.Utils.Vuforia.AppRenderer;
import com.example.administrator.myapplication.Utils.Vuforia.VuforiaAppSession;
import com.example.administrator.myapplication.Utils.utils.LoadingDialogHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vuforia.Device;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.Vuforia;
import com.example.administrator.myapplication.Utils.Vuforia.RendererInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


// The renderer class for the ImageTargets sample. 
public class ImageTargetRenderer implements GLSurfaceView.Renderer, RendererInterface
{
    private static final String LOGTAG = "ImageTargetRenderer";
    private TextView newt,info_details;
    private RelativeLayout info_layout;
    private VuforiaAppSession vuforiaAppSession;
    private ImageTargets mActivity;
    private AppRenderer mSampleAppRenderer;
    private boolean mIsActive = false;
    private ViewPager mImageViewPager;
    private TabLayout tabLayout;
    TextView t1;
    ViewPager viewPager;
    MyDataObject object;
    JSONObject matched_obj ;
     PagerAdapter adapter;
    public ImageTargetRenderer(ImageTargets activity, VuforiaAppSession session)
    {
        mActivity = activity;
        vuforiaAppSession = session;
        // AppRenderer used to encapsulate the use of RenderingPrimitives setting
        // the device mode AR/VR and stereo mode
        mSampleAppRenderer = new AppRenderer(this, mActivity, Device.MODE.MODE_AR, false, 0.01f , 5f);
        info_layout = mActivity.findViewById(R.id.info_layout);
        setpager();
    }



    // Called to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl)
    {

        if (!mIsActive)
            return;

        // Call our function to render content from AppRenderer
        mSampleAppRenderer.render();
    }
    

    public void setActive(boolean active)
    {
        mIsActive = active;

        if(mIsActive)
            mSampleAppRenderer.configureVideoBackground();
    }


    // Called when the surface is created or recreated.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceCreated");
        
        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();
        mSampleAppRenderer.onSurfaceCreated();
    }
    
    
    // Called when the surface changed size.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(LOGTAG, "GLRenderer.onSurfaceChanged");
        
        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);

        // RenderingPrimitives to be updated when some rendering change is done
        mSampleAppRenderer.onConfigurationChanged(mIsActive);

        initRendering();
    }
    
    
    // Function for initializing the renderer. Basically nothing for now.
    private void initRendering()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f
                : 1.0f);

        // Hide the Loading Dialog
        mActivity.loadingDialogHandler
                .sendEmptyMessage(LoadingDialogHandler.HIDE_LOADING_DIALOG);
    }

    public void updateConfiguration()
    {
        mSampleAppRenderer.onConfigurationChanged(mIsActive);
    }

    // The render function called from SampleAppRendering by using RenderingPrimitives views.
    // The state is owned by AppRenderer which is controlling it's lifecycle.
    // State should not be cached outside this method.
    public void renderFrame(State state)
    {
        // Renders video background replacing Renderer.DrawVideoBackground()
        mSampleAppRenderer.renderVideoBackground();

        // handle face culling, we need to detect if we are using reflection
        // to determine the direction of the culling
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);


       //set the visibility of textview = gone
        if(state.getNumTrackableResults()==0){

            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                info_layout.setVisibility(View.GONE);
                }
            });
        }
        // Did we find any trackables in this frame?
        for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++) {
            TrackableResult result = state.getTrackableResult(tIdx);
            final Trackable trackable = result.getTrackable();
            printUserData(trackable);

                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff to updates the UI
                        info_layout.setVisibility(View.VISIBLE);
                        displayinfo(trackable.getName());
                    }
                });

        }

    }

    private void printUserData(Trackable trackable)
    {
        String userData = (String) trackable.getUserData();
        Log.d(LOGTAG, "UserData:Retreived User Data	\"" + userData + "\"");
    }


    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Workstation.Json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("ImageTargetRenderer", "unable to load json");
            return null;
        }
        return json;

    }

    void setpager(){
        TabLayout tabLayout = (TabLayout) mActivity.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("WorksStation"));
        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Operator"));

        object =  new MyDataObject("a", "b");
        viewPager = (ViewPager) mActivity.findViewById(R.id.pager);
        adapter = new PagerAdapter
                (mActivity.getSupportFragmentManager(), tabLayout.getTabCount(), object);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //display the info of the workstation when found
    public void displayinfo(String workstation){
        matched_obj = null;
        try {

            JSONObject obj = new JSONObject(loadJSONFromAsset(mActivity));
            JSONArray  workstations_array= obj.getJSONArray("Dataset");
            for(int i=0;i<workstations_array.length();i++){
                JSONObject currObject = workstations_array.getJSONObject(i);
                String name = currObject.getString("name");

                if(name.equals(workstation))
                {
                    matched_obj = currObject;
                    break;
                }
            }

            if(matched_obj!=null) {

                JsonParser parser = new JsonParser();
                JsonElement mJson =  parser.parse(matched_obj.toString());
                Gson gson = new Gson();
                object = gson.fromJson(mJson,MyDataObject.class);
                adapter.update(object);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IMAGETARGET", "unable to load JSON");
        }



    }

}
