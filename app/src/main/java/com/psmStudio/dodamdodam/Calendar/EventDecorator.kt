package com.psmStudio.fastcampusandroid.Calendar

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class EventDecorator(var color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {

    var dates : HashSet<CalendarDay>? = null
    val calendar = Calendar.getInstance()

    init {
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