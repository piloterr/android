package com.piloterr.android.piloterr.ui.fragments.inventory.stable

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.piloterr.android.piloterr.R
import com.piloterr.android.piloterr.components.UserComponent
import com.piloterr.android.piloterr.ui.fragments.BaseMainFragment
import com.piloterr.android.piloterr.ui.helpers.bindView
import com.piloterr.android.piloterr.ui.helpers.resetViews

class StableFragment : BaseMainFragment() {

    private val viewPager: androidx.viewpager.widget.ViewPager? by bindView(R.id.viewPager)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.usesTabLayout = true
        this.hidesToolbar = true
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetViews()
        viewPager?.currentItem = 0

        setViewPagerAdapter()
    }

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    private fun setViewPagerAdapter() {
        val fragmentManager = childFragmentManager

        viewPager?.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(position: Int): androidx.fragment.app.Fragment {

                val fragment = StableRecyclerFragment()

                when (position) {
                    0 -> {
                        fragment.itemType = "pets"
                    }
                    1 -> {
                        fragment.itemType = "mounts"
                    }
                }
                fragment.user = this@StableFragment.user
                fragment.itemTypeText = this.getPageTitle(position).toString()

                return fragment
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence {
                return when (position) {
                    0 -> activity?.getString(R.string.pets)
                    1 -> activity?.getString(R.string.mounts)
                    else -> ""
                } ?:  ""
            }
        }
        tabLayout?.setupWithViewPager(viewPager)
    }


}