package com.psmStudio.fastcampusandroid.Calendar

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class EventDecorator : DayViewDecorator {

    var color : Int
    var dates : HashSet<CalendarDay>? = null
    val calendar = Calendar.getInstance()

    constructor(color : Int, dates : Collection<CalendarDay>){
        this.color = color
        this.dates = HashSet(dates)
    }



    override fun shouldDecorate(day: CalendarDay?): Boolean {
        //return dates!!.contains(day)
        return dates!!.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(8F, color))
    }
}