/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.example.administrator.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.Utils.Vuforia.VuforiaAppSession;
import com.vuforia.Device;
import com.vuforia.Matrix44F;
import com.vuforia.State;
import com.vuforia.Tool;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.Vuforia;
import com.example.administrator.myapplication.Utils.Vuforia.SampleAppRenderer;
import com.example.administrator.myapplication.Utils.Vuforia.RendererInterface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


// The renderer class for the ImageTargets sample. 
public class ImageTargetRenderer implements GLSurfaceView.Renderer, RendererInterface
{
    private static final String LOGTAG = "ImageTargetRenderer";
    TextView newt;
    private VuforiaAppSession vuforiaAppSession;
    private ImageTargets mActivity;
    private SampleAppRenderer mSampleAppRenderer;
    private boolean mIsActive = false;

    
    public ImageTargetRenderer(ImageTargets activity, VuforiaAppSession session)
    {
        mActivity = activity;
        vuforiaAppSession = session;
        // SampleAppRenderer used to encapsulate the use of RenderingPrimitives setting
        // the device mode AR/VR and stereo mode
        mSampleAppRenderer = new SampleAppRenderer(this, mActivity, Device.MODE.MODE_AR, false, 0.01f , 5f);
        mActivity.newt = mActivity.findViewById(R.id.imagetargettext);
    }


    // Called to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl)
    {

        if (!mIsActive)
            return;

        // Call our function to render content from SampleAppRenderer
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
    
    
    // Function for initializing the renderer.
    private void initRendering()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f
                : 1.0f);
    }

    public void updateConfiguration()
    {
        mSampleAppRenderer.onConfigurationChanged(mIsActive);
    }

    // The render function called from SampleAppRendering by using RenderingPrimitives views.
    // The state is owned by SampleAppRenderer which is controlling it's lifecycle.
    // State should not be cached outside this method.
    public void renderFrame(State state)
    {
        // Renders video background replacing Renderer.DrawVideoBackground()
        mSampleAppRenderer.renderVideoBackground();


       //set the visibility of textview = gone
        if(state.getNumTrackableResults()==0){

            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    newt.setVisibility(View.GONE);
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
                        newt.setVisibility(View.VISIBLE);
                        newt.setText(trackable.getName());
                    }
                });

        }

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

    }

    private void printUserData(Trackable trackable)
    {
        String userData = (String) trackable.getUserData();
        Log.d(LOGTAG, "UserData:Retreived User Data	\"" + userData + "\"");
    }
    
    

    
}
