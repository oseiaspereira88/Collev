package com.empreendapp.collev.util

import android.content.Context
import com.empreendapp.collev.R
import java.util.*


class TimeAgo {
    companion object{
        fun currentDate(): Date {
            val calendar: Calendar = Calendar.getInstance()
            return calendar.getTime()
        }

        fun getTimeAgo(date: Date?, ctx: Context): String? {
            if (date == null) {
                return null
            }
            val time: Long = date.getTime()
            val curDate: Date = currentDate()
            val now: Long = curDate.getTime()
            if (time > now || time <= 0) {
                return null
            }
            val dim = getTimeDistanceInMinutes(time)
            var timeAgo: String? = null
            timeAgo = if (dim == 0) {
                ctx.getResources().getString(R.string.date_util_term_less)
                    .toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_term_a) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_minute)
            } else if (dim == 1) {
                return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute)
            } else if (dim >= 2 && dim <= 44) {
                dim.toString() + " " + ctx.getResources().getString(R.string.date_util_unit_minutes)
            } else if (dim >= 45 && dim <= 89) {
                ctx.getResources().getString(R.string.date_util_prefix_about)
                    .toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_term_an) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_hour)
            } else if (dim >= 90 && dim <= 1439) {
                ctx.getResources().getString(R.string.date_util_prefix_about)
                    .toString() + " " + Math.round((dim / 60).toFloat()) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_hours)
            } else if (dim >= 1440 && dim <= 2519) {
                "1 " + ctx.getResources().getString(R.string.date_util_unit_day)
            } else if (dim >= 2520 && dim <= 43199) {
                Math.round((dim / 1440).toFloat()).toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_days)
            } else if (dim >= 43200 && dim <= 86399) {
                ctx.getResources().getString(R.string.date_util_prefix_about)
                    .toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_term_a) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_month)
            } else if (dim >= 86400 && dim <= 525599) {
                Math.round((dim / 43200).toFloat()).toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_months)
            } else if (dim >= 525600 && dim <= 655199) {
                ctx.getResources().getString(R.string.date_util_prefix_about)
                    .toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_term_a) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_year)
            } else if (dim >= 655200 && dim <= 914399) {
                ctx.getResources().getString(R.string.date_util_prefix_over)
                    .toString() + " " + ctx.getResources()
                    .getString(R.string.date_util_term_a) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_year)
            } else if (dim >= 914400 && dim <= 1051199) {
                ctx.getResources().getString(R.string.date_util_prefix_almost)
                    .toString() + " 2 " + ctx.getResources()
                    .getString(R.string.date_util_unit_years)
            } else {
                ctx.getResources().getString(R.string.date_util_prefix_about)
                    .toString() + " " + Math.round((dim / 525600).toFloat()) + " " + ctx.getResources()
                    .getString(R.string.date_util_unit_years)
            }
            return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix)
        }

        private fun getTimeDistanceInMinutes(time: Long): Int {
            val timeDistance: Long = currentDate().getTime() - time
            return Math.round((Math.abs(timeDistance) / 1000 / 60).toFloat())
        }
    }
}