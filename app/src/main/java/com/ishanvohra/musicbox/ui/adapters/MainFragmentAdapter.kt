package com.ishanvohra.musicbox.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ishanvohra.musicbox.models.GetPlaylistResponse
import com.ishanvohra.musicbox.ui.fragments.PlayerFragment

class MainFragmentAdapter(
    fragment: FragmentActivity,
    val dataSet: ArrayList<GetPlaylistResponse.Short>
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun createFragment(position: Int): Fragment {
        return PlayerFragment.newInstance(song = dataSet[position])
    }


}