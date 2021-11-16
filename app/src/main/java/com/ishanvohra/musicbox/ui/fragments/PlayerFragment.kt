package com.ishanvohra.musicbox.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.ishanvohra.musicbox.R
import com.ishanvohra.musicbox.models.GetPlaylistResponse
import com.ishanvohra.musicbox.util.ImageUrls

class PlayerFragment : Fragment(R.layout.item_player) {

    //declaring all the view elements
    private var titleTextView: TextView? = null
    private var playerView: StyledPlayerView? = null
    private var thumbnailImageView: ImageView? = null
    private var playPauseBtn: ImageView? = null
    private var replay10Btn: ImageView? = null
    private var forward10Btn: ImageView? = null
    private var skipBackBtn: ImageView? = null
    private var skipForwardBtn: ImageView? = null
    private var progressIndicator: LinearProgressIndicator? = null

    private var songItem: GetPlaylistResponse.Short? = null
    private var simplePlayer: ExoPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isPlaying: Boolean = true
    private var listener: MusicPlayerEventListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting the song item via arguments sent in bundle
        songItem = arguments?.getSerializable("song") as GetPlaylistResponse.Short

        bindViews(view)
        setUpPlayer()

        //setting the text of titleTextView
        titleTextView?.text = songItem?.title

        //implementing pause/play on button click
        //if the song is being played, it will be paused and the drawable on the image view will be updated and vice versa
        playPauseBtn?.setOnClickListener {
            if(isPlaying){
                isPlaying = false
                playPauseBtn?.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                simplePlayer?.pause()
            }
            else{
                isPlaying = true
                playPauseBtn?.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                simplePlayer?.play()
            }
        }

        skipForwardBtn?.setOnClickListener {
            listener?.moveToNextSong()
        }

        skipBackBtn?.setOnClickListener {
            listener?.moveToPreviousSong()
        }

        //Setting the current position of player to 10 seconds back
        replay10Btn?.setOnClickListener {
            try{
                if (simplePlayer?.currentPosition!!.div(1000) > 10) {
                    simplePlayer?.seekTo(simplePlayer?.currentPosition?.minus(10000)!!)
                } else {
                    simplePlayer?.seekTo(0)
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }

        //Setting the current position of player to 10 seconds ahead
        forward10Btn?.setOnClickListener {
            try{
                if (simplePlayer?.currentPosition!! < simplePlayer?.duration!!.minus(10000)) {
                    simplePlayer?.seekTo(simplePlayer?.currentPosition?.plus(10000)!!)
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        //adding a random image as thumbnail from ImageUrls class.
        try{
            Glide
                .with(requireContext())
                .load(ImageUrls.getRandomImage())
                .into(thumbnailImageView!!)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    //setting up the exoplayer
    private fun setUpPlayer() {
        try{
            //building the exoplayer instance
            simplePlayer = ExoPlayer.Builder(requireContext())
                .build()

            //setting up audio attributes
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build()

            playerView?.player = simplePlayer
            playerView?.setShowNextButton(false)
            playerView?.setShowPreviousButton(false)
            simplePlayer?.setAudioAttributes(audioAttributes, true)

            //adding media item to exo player using the url
            val mediaItem: MediaItem = MediaItem.fromUri(songItem?.audioPath!!)
            simplePlayer!!.setMediaItem(mediaItem, true)
            simplePlayer!!.prepare()

            //setting repeat mode off that is song will be played only once and will not be repeated
            simplePlayer!!.repeatMode = Player.REPEAT_MODE_OFF
            simplePlayer!!.playWhenReady = true

            //running a task on a different thread which will keep a track of position of the player and
            //update it on the linear progress indicator
            handler = Handler(Looper.getMainLooper())
            runnable = object : Runnable{
                override fun run() {
                    var progress = simplePlayer!!.currentPosition.times(100)
                    progress = progress.div(simplePlayer!!.duration)
                    progressIndicator?.progress = progress.toInt()
                    Log.d("ReviewStoryProgress", "setUpPlayer: $progress")
                    handler?.postDelayed(this, 200)
                }

            }

            handler?.postDelayed(runnable!!, 500)

            setUpPlayerListener()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    //if the playback state is STATE_ENDED that is if the song is ended. move to the next song.
    private fun setUpPlayerListener() {
        simplePlayer?.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState == Player.STATE_ENDED){
                    listener?.moveToNextSong()
                }
            }
        })
    }

    //binding all the view elements
    private fun bindViews(view: View) {
        titleTextView = view.findViewById(R.id.song_title_tv)
        playerView = view.findViewById(R.id.player_view)
        thumbnailImageView = view.findViewById(R.id.thumbnail_image_view)
        playPauseBtn = view.findViewById(R.id.play_btn)
        replay10Btn = view.findViewById(R.id.replay_10_btn)
        forward10Btn = view.findViewById(R.id.forward_10_btn)
        skipBackBtn = view.findViewById(R.id.go_back_btn)
        skipForwardBtn = view.findViewById(R.id.go_forward_btn)
        progressIndicator = view.findViewById(R.id.progress_indicator)
    }

    //creating an instance of Player Fragment
    companion object{
        fun newInstance(song: GetPlaylistResponse.Short, eventListener: MusicPlayerEventListener) = PlayerFragment()
            .apply {
                arguments = Bundle().apply {
                    putSerializable("song", song)
                    listener = eventListener
                }
            }
    }

    //pausing the player when the fragment is paused
    override fun onPause() {
        super.onPause()
        simplePlayer?.pause()
    }

    //playing the song from the beginning when the fragment is resumed
    override fun onResume() {
        super.onResume()
        simplePlayer?.seekTo(0)
        simplePlayer?.play()
    }

    //when the fragment is stopped, the memory from the player instance is released and the background tasks are closed
    override fun onStop() {
        super.onStop()
        simplePlayer?.stop()
        simplePlayer?.release()
        handler?.removeCallbacks(runnable!!)
    }

    interface MusicPlayerEventListener{
        fun moveToNextSong()
        fun moveToPreviousSong()
    }
}