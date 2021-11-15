package com.ishanvohra.musicbox.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.ishanvohra.musicbox.R
import com.ishanvohra.musicbox.ui.adapters.MainFragmentAdapter
import com.ishanvohra.musicbox.ui.fragments.PlayerFragment

class MainActivity : AppCompatActivity()
,PlayerFragment.MusicPlayerEventListener{

    private var viewModel = MainViewModel()

    private var viewPager: ViewPager2? = null
    private var animationView: LottieAnimationView? = null

    private var adapter: MainFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        loadMusicList()
        bindViews()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        adapter = MainFragmentAdapter(this, ArrayList(), this)
        viewPager?.adapter = adapter
        viewPager?.orientation = ViewPager2.ORIENTATION_VERTICAL

        viewModel.seriesList.observe(this, {
            if (it != null){
                animationView?.visibility = View.GONE
                viewPager?.visibility = View.VISIBLE

                adapter?.dataSet?.addAll(it.shorts)
                adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun bindViews() {
        viewPager = findViewById(R.id.view_pager)
        animationView = findViewById(R.id.animationView)
    }

    private fun loadMusicList() {
        viewModel.getPlaylist("b9f74279-038b-4590-9f96-7c720261294c")
    }

    override fun moveToNextSong() {
        if(viewPager?.currentItem!! < adapter?.dataSet!!.size - 1){
            viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
        }
    }

    override fun moveToPreviousSong() {
        if(viewPager?.currentItem!! > 0){
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
    }
}