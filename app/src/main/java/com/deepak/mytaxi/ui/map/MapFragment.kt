package com.deepak.mytaxi.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.deepak.mytaxi.MainActivity
import com.deepak.mytaxi.R

class MapFragment : Fragment() {


    companion object {

        const val TAG = "MapFragment"

        fun newInstance(): MapFragment {
            val args = Bundle()
            val fragment = MapFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).setActionBar(getString(R.string.map))

        return inflater.inflate(R.layout.fragment_map, container, false)
    }
}