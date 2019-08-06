package com.hilbing.bakingapp.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.model.Recipe;
import com.hilbing.bakingapp.model.Step;
import com.hilbing.bakingapp.widget.WidgetProvider;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class RecipeStepFragment extends Fragment implements Player.EventListener, View.OnClickListener {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    @BindView(R.id.tv_description_step_detail)
    TextView stepDescription;
    @BindView(R.id.iv_video_thumbnail)
    ImageView videoImage;
    @BindView(R.id.tv_no_video)
    TextView noVideo;
    @Nullable
    @BindView(R.id.btn_previous_step)
    Button previousBtn;
    @Nullable
    @BindView(R.id.btn_next_step)
    Button nextBtn;
    @BindView(R.id.player_view)
    PlayerView playerView;

    Recipe recipe;


    private SimpleExoPlayer mSimpleExoPlayer;
    private long playerPosition;
    private int screenOrientation;

    private Context mContext;
    private Step step;
    private boolean isTwoPane;
    private String videoUrl;
    private String thumbnailUrl;

    private static final String EXTRA = "Step";
    private static final String POSITION = "position";
    private static final String WHEN_READY = "play_when_ready";

    public static final String WIDGET_PREF = "recipe_on_widget";
    public static final String ID_PREF = "id";
    public static final String NAME_PREF = "name";

    private int recipeId;
    private String recipeName;

    private boolean playReady = true;

    private OnStepClickListener mListener;

    public interface OnStepClickListener{
        void onPreviousClick(Step step);
        void onNextClick(Step step);
    }

    public RecipeStepFragment(){

    }

    public static RecipeStepFragment newInstance(Step step){
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA, step);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null){
            step = getArguments().getParcelable(EXTRA);

        }

        if (savedInstanceState != null){
            step = savedInstanceState.getParcelable(EXTRA);
            playerPosition = savedInstanceState.getLong(POSITION);
            playReady = savedInstanceState.getBoolean(WHEN_READY);
        } else {
            playerPosition = 0;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_widget, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
               // finish();
                break;
            case R.menu.add_widget:
                addToPrefsWidget();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addToPrefsWidget() {
        SharedPreferences preferences = mContext.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        recipeId = recipe.getId();
        recipeName = recipe.getName();
        editor.putInt(ID_PREF, recipeId);
        editor.putString(NAME_PREF, recipeName);
        editor.apply();

        //Add to widget
        WidgetProvider.updateAllWidgets(mContext, recipe);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_step_details, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "creating view");

        mContext = getActivity();
        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);



        if (step != null) {

            videoUrl = step.getVideoURL();
            thumbnailUrl = step.getThumbnailURL();

            if (null != videoUrl && !videoUrl.isEmpty()){
                if (null != savedInstanceState && savedInstanceState.containsKey(POSITION) && savedInstanceState.containsKey(WHEN_READY)){
                    playerPosition = savedInstanceState.getLong(POSITION);
                    playReady = savedInstanceState.getBoolean(WHEN_READY);
                }

                showVideoViewOnly();
                initExoPlayer();

            } else if (null != thumbnailUrl && !thumbnailUrl.isEmpty()){
                showImageViewOnly();

            } else {
                hideImageAndVideoViews();
            }


            stepDescription.setText(step.getDescription());
            Log.d(TAG, step.getDescription());
            screenOrientation = getResources().getConfiguration().orientation;
            previousBtn.setOnClickListener(this);
            nextBtn.setOnClickListener(this);

            //If the position is landscape, show full screen, else show nav buttons
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                fullScreenPlayer();
                previousBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.GONE);
            } else {
                previousBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                previousBtn.setOnClickListener(this);
                nextBtn.setOnClickListener(this);
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener){
            mListener = (OnStepClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + getString(R.string.error_interface));
        }
    }

    //Exoplayer
    private void initExoPlayer(){
        //check if it is null
        if (step != null) {
            if (mSimpleExoPlayer == null && videoUrl != null) {
                if (!(videoUrl.isEmpty())) {
                    //show the player
                    playerView.setVisibility(View.VISIBLE);
                    //Default trackselector
                    TrackSelector trackSelector = new DefaultTrackSelector();
                    mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
                    playerView.setPlayer(mSimpleExoPlayer);
                    mSimpleExoPlayer.addListener(this);
                    //User agent
                    String userAgent = getResources().getString(R.string.app_name);
                    DataSource.Factory factory = new DefaultDataSourceFactory(mContext, userAgent);
                    MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(videoUrl));
                    mSimpleExoPlayer.prepare(mediaSource);
                    mSimpleExoPlayer.seekTo(playerPosition);
                    mSimpleExoPlayer.setPlayWhenReady(true);
                }
            } else {
                //hide video in landscape
                mSimpleExoPlayer.setPlayWhenReady(false);
                if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    playerView.setVisibility(View.VISIBLE);
                    videoImage.setVisibility(View.VISIBLE);
                    stepDescription.setVisibility(View.VISIBLE);
                    noVideo.setVisibility(View.GONE);
                } else {
                    //portrait
                    playerView.setVisibility(View.VISIBLE);
                    videoImage.setVisibility(View.GONE);
                    noVideo.setVisibility(View.GONE);
                }
            }
        }
    }

    void releasePlayer(){
        if (playerView != null) {
            playerView.setPlayer(null);
        }

        if (mSimpleExoPlayer != null){

            playerPosition = mSimpleExoPlayer.getCurrentPosition();
            playReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    //Method to hide the system UI for full screen mode
/*    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        Objects.requireNonNull(((AppCompatActivity)
                Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        initExoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initExoPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null){
            playerPosition = mSimpleExoPlayer.getCurrentPosition();
            playReady = mSimpleExoPlayer.getPlayWhenReady();
            Log.d(TAG, String.valueOf(playReady));
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSimpleExoPlayer != null){
            playerPosition = mSimpleExoPlayer.getCurrentPosition();
            playReady = mSimpleExoPlayer.getPlayWhenReady();
            Log.d(TAG, String.valueOf(playReady));
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void fullScreenPlayer(){
        if (!videoUrl.isEmpty() && !isTwoPane){
           // hideSystemUI();
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_READY){
            Log.d(TAG, "playing......");
        } else if (playbackState == Player.STATE_READY){
            Log.d(TAG, "paused......");
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION, playerPosition);
        outState.putParcelable(EXTRA, step);
        outState.putBoolean(WHEN_READY, playReady);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_previous_step:
                mListener.onPreviousClick(step);
                break;
            case R.id.btn_next_step:
                mListener.onNextClick(step);
                break;
        }
    }

    private void hideImageAndVideoViews() {
        if (null != playerView) {
            playerView.setVisibility(View.INVISIBLE);
        }
        if (null != videoImage) {
            videoImage.setVisibility(View.INVISIBLE);
            noVideo.setVisibility(View.VISIBLE);
        }
    }

    private void showVideoViewOnly() {
        hideImageAndVideoViews();
        if (null != playerView) {
            playerView.setVisibility(View.VISIBLE);
        }
    }

    private void showImageViewOnly() {
        hideImageAndVideoViews();
        if (null != videoImage) {
            videoImage.setVisibility(View.VISIBLE);
        }
    }

}
