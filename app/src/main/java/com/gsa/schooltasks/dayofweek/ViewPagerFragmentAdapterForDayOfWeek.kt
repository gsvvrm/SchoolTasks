package com.gsa.schooltasks.dayofweek

import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class ViewPagerFragmentAdapterForDayOfWeek(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val calendar: Calendar = Calendar.getInstance()
    var dayIndex: Int = 0

    private fun getTomorrowFragment(): Fragment {
        dayIndex = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayIndex){
            1 -> MondayFragment()
            2 -> TuesdayFragment()
            3 -> WednesdayFragment()
            4 -> ThursdayFragment()
            5 -> FridayFragment()
            6 -> SaturdayFragment()
            7 -> SundayFragment()
            else -> TomorrowFragment()
        }

    }

    override fun getItemCount(): Int = 8

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0-> getTomorrowFragment()
            1 -> MondayFragment()
            2 -> TuesdayFragment()
            3 -> WednesdayFragment()
            4 -> ThursdayFragment()
            5 -> FridayFragment()
            6 -> SaturdayFragment()
            7 -> SundayFragment()
            else -> TomorrowFragment()
        }

    }
}