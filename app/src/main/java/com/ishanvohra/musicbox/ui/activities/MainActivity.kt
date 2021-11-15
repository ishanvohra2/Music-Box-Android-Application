package com.ishanvohra.musicbox.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ishanvohra.musicbox.R
import com.ishanvohra.musicbox.ui.adapters.MainFragmentAdapter

class MainActivity : AppCompatActivity() {

    private var viewModel = MainViewModel()

    private var viewPager: ViewPager2? = null

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
        adapter = MainFragmentAdapter(this, ArrayList())
        viewPager?.adapter = adapter
        viewPager?.orientation = ViewPager2.ORIENTATION_VERTICAL

        viewModel.seriesList.observe(this, {
            if (it != null){
                adapter?.dataSet?.addAll(it.shorts)
                adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun bindViews() {
        viewPager = findViewById(R.id.view_pager)
    }

    private fun loadMusicList() {
        viewModel.getPlaylist("b9f74279-038b-4590-9f96-7c720261294c")
    }
}