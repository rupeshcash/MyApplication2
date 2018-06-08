/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.example.administrator.myapplication;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.Utils.Vuforia.AppRenderer;
import com.example.administrator.myapplication.Utils.Vuforia.VuforiaAppSession;
import com.example.administrator.myapplication.Utils.utils.LoadingDialogHandler;
import com.vuforia.Device;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.Vuforia;
import com.example.administrator.myapplication.Utils.Vuforia.RendererInterface;

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

    public ImageTargetRenderer(ImageTargets activity, VuforiaAppSession session)
    {
        mActivity = activity;
        vuforiaAppSession = session;
        // AppRenderer used to encapsulate the use of RenderingPrimitives setting
        // the device mode AR/VR and stereo mode
        mSampleAppRenderer = new AppRenderer(this, mActivity, Device.MODE.MODE_AR, false, 0.01f , 5f);
        info_layout = mActivity.findViewById(R.id.info_layout);
        newt = mActivity.findViewById(R.id.target_info);
       // info_details = mActivity.findViewById(R.id.info_details);
        mImageViewPager = (ViewPager) mActivity.findViewById(R.id.pager);
        tabLayout = (TabLayout) mActivity.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mImageViewPager, true);
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
                        if(trackable.getName().equals("stones"))
                        {

                           newt.setText("Stones");
                          //  info_details.setText("Stones and pebbles on a sea shore");
                        }
                        else if(trackable.getName().equals("chips"))
                        {
                            newt.setText("Chips");
                            //info_details.setText(" crumbled chips  of wood");

                        }

                    }
                });

        }

    }

    private void printUserData(Trackable trackable)
    {
        String userData = (String) trackable.getUserData();
        Log.d(LOGTAG, "UserData:Retreived User Data	\"" + userData + "\"");
    }
    
    

    
}
