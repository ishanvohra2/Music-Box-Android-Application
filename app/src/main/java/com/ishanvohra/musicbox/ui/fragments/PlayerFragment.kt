package com.ishanvohra.musicbox.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.ishanvohra.musicbox.R
import com.ishanvohra.musicbox.models.GetPlaylistResponse

class PlayerFragment : Fragment(R.layout.item_player) {

    private var titleTextView: TextView? = null
    private var playerView: StyledPlayerView? = null
    private var thumbnailImageView: ImageView? = null
    private var playPauseBtn: ImageView? = null
    private var replay10Btn: ImageView? = null
    private var forward10Btn: ImageView? = null
    private var skipBackBtn: ImageView? = null
    private var forwardBackBtn: ImageView? = null
    private var progressIndicator: LinearProgressIndicator? = null

    private var songItem: GetPlaylistResponse.Short? = null
    private var simplePlayer: ExoPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isPlaying: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songItem = arguments?.getSerializable("song") as GetPlaylistResponse.Short

        bindViews(view)
        setUpPlayer()

        titleTextView?.text = songItem?.title

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
    }

    private fun setUpPlayer() {
        try{
            simplePlayer = ExoPlayer.Builder(requireContext())
                .build()

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build()

            playerView?.player = simplePlayer
            playerView?.setShowNextButton(false)
            playerView?.setShowPreviousButton(false)
            simplePlayer?.setAudioAttributes(audioAttributes, true)

            val mediaItem: MediaItem = MediaItem.fromUri(songItem?.audioPath!!)
            simplePlayer!!.setMediaItem(mediaItem, true)
            simplePlayer!!.prepare()
            simplePlayer!!.repeatMode = Player.REPEAT_MODE_OFF
            simplePlayer!!.playWhenReady = true

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

    private fun setUpPlayerListener() {
        simplePlayer?.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
            }
        })
    }

    private fun bindViews(view: View) {
        titleTextView = view.findViewById(R.id.song_title_tv)
        playerView = view.findViewById(R.id.player_view)
        thumbnailImageView = view.findViewById(R.id.thumbnail_image_view)
        playPauseBtn = view.findViewById(R.id.play_btn)
        replay10Btn = view.findViewById(R.id.replay_10_btn)
        forward10Btn = view.findViewById(R.id.forward_10_btn)
        skipBackBtn = view.findViewById(R.id.go_back_btn)
        forwardBackBtn = view.findViewById(R.id.go_forward_btn)
        progressIndicator = view.findViewById(R.id.progress_indicator)
    }

    companion object{
        fun newInstance(song: GetPlaylistResponse.Short) = PlayerFragment()
            .apply {
                arguments = Bundle().apply {
                    putSerializable("song", song)
                }
            }
    }

    override fun onPause() {
        super.onPause()
        simplePlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        simplePlayer?.play()
    }

    override fun onStop() {
        super.onStop()
        simplePlayer?.stop()
        simplePlayer?.release()
        handler?.removeCallbacks(runnable!!)
    }
}