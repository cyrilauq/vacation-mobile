package cyril.brian.vacationmobile.domain

import cyril.brian.vacationmobile.domain.exception.CreateVacationException

class Vacation {
    var id: String? = null
        get() = if(field == null) "" else field
        private set(value) {
            field = value
        }
    lateinit var title: String
    lateinit var place: String
    lateinit var description: String
    lateinit var begin: PeriodTime
    lateinit var end: PeriodTime
    lateinit var coordinate: Coordinate
    var isPublished: Boolean = false

    var members: MutableList<User> = mutableListOf()

    companion object {
        @Throws(CreateVacationException::class)
        fun from(id: String, title: String, description: String, place: String, begin: PeriodTime?, end: PeriodTime?, coordinate: Coordinate, isPublished: Boolean = false): Vacation {
            verify(title, place)
            val vacation = Vacation()
            vacation.id = id
            vacation.title = title
            vacation.description = description
            vacation.place = place
            vacation.begin = begin!!
            vacation.end = end!!
            vacation.coordinate = coordinate
            vacation.isPublished = isPublished
            return vacation
        }

        @Throws(CreateVacationException::class)
        fun newVacation(title: String, place: String, description: String, begin: PeriodTime?, end: PeriodTime?, coordinate: Coordinate): Vacation {
            verify(title, place)
            val vacation = Vacation()
            vacation.title = title
            vacation.description = description
            vacation.place = place
            vacation.begin = begin!!
            vacation.end = end!!
            vacation.coordinate = coordinate
            return vacation
        }

        private fun verify(title: String, place: String) {
            if (title.isEmpty()) {
                throw CreateVacationException("The title shouldn't be empty.")
            }
            if (place.isEmpty()) {
                throw CreateVacationException("The place shouldn't be empty.")
            }
        }
    }
}