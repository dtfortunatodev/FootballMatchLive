package com.formobile.projectlivestream;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.formobile.projectlivestream.utils.AnalyticsHelper;

/**
 * Created by PTECH on 05-06-2015.
 */
public class VideoPlayerActivity extends BaseActivity {
    private static final String EXTRA_VIDEO_URL = VideoPlayerActivity.class.getName() + ".EXTRA_VIDEO_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_player_layout);

        // Get Video URL
        String videoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);


        if(videoUrl == null){
            Toast.makeText(getBaseContext(), "Video not found...", Toast.LENGTH_LONG).show();
            AnalyticsHelper.sendEvent(getBaseContext(), "ERROR", "VIDEO", "Video not found");
            finish();
        } else{
            // Start video
            startVideo(videoUrl, (VideoView) findViewById(R.id.vvVideoPlayerLayoutVideo));
        }

    }

    /**
     * Start video play
     * @param videoUrl
     * @param videoView
     */
    private void startVideo(String videoUrl, VideoView videoView){
        if(videoView != null){
            videoView.setVideoPath(videoUrl);

            // Init media controller
            MediaController mediaController = new
                    MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.start();

        }
    }


    public static void startVideoPlayer(Context context, String url){
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
