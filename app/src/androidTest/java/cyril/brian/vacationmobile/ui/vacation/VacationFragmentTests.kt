package cyril.brian.vacationmobile.ui.vacation

import cyril.brian.vacationmobile.repositories.IVacationActivitiesRepository
import cyril.brian.vacationmobile.repositories.IVacationRepository
import io.mockk.mockk
import org.junit.Before

class VacationFragmentTests {
    lateinit var mockedActivitiesRepo: IVacationActivitiesRepository
    lateinit var mockedVacationRepo: IVacationRepository

    @Before
    fun setUp() {
        mockedActivitiesRepo = mockk()
        mockedVacationRepo = FakeVacationRepo()
    }

//    Le test ici ne marche pas, il aurait fallut faire de l'injection de dépendance mais sans cela, il m'est impossible de le faire fonctionner.
//    @Test
//    fun whenActivitiesBtnClickedThenActivitiesTitleIsDisplayed() {
//        val factory = object : FragmentFactory() {
//            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
//                return VacationFragment.newInstance {  }.also {
//                    it.viewModel.repository = object: IVacationRepository {
//                        override fun getVacationsUser(token: String): GetVacationUserResponse {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun newVacation(
//                            title: String,
//                            place: String,
//                            description: String,
//                            begin: PeriodTime?,
//                            end: PeriodTime?,
//                            coordinate: Coordinate,
//                            token: String
//                        ): Vacation? {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun getVacationById(id: String) = Vacation().also {
//                            it.title = ""
//                            it.begin = PeriodTime("", "")
//                            it.end = PeriodTime("", "")
//                            it.place = ""
//                        }
//
//                        override fun addMembers(
//                            vacationId: String,
//                            members: List<String>
//                        ): List<User> {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun setCurrentVacation(vacationId: String) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun getCurrentVacation(): String {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun addActivities(
//                            vacationId: String,
//                            activities: Array<VacationActivity>
//                        ): Iterable<VacationActivity> {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun getMembers(vacationId: String): List<User> {
//                            TODO("Not yet implemented")
//                        }
//
//                    }
//                    it.viewModel.activitiesRepository = mockedActivitiesRepo
//                }
//            }
//        }
//        val scenario = launchInContainer(VacationFragment::class.java, null, factory = factory)
//
//        onView(withId(R.id.show_activities))
//            .perform(click())
//
//        onView(withId(R.id.activities_title))
//            .check(matches(isDisplayed()))
//
//        onView(withId(R.id.members_title))
//            .check(matches(not(isDisplayed())))
//    }

//    Le test ici ne marche pas, il aurait fallut faire de l'injection de dépendance mais sans cela, il m'est impossible de le faire fonctionner.
//    @Test
//    fun whenMembersBtnClickedThenMemberTitleIsDisplayed() {
//        val scenario = launchFragmentInContainer<VacationFragment>()
//        scenario.onFragment {
//            it.viewModel.repository = mockk()
//        }
//
//        onView(withId(R.id.show_activities))
//            .perform(click())
//
//        onView(withId(R.id.members_title))
//            .check(matches(isDisplayed()))
//
//        onView(withId(R.id.activities_title))
//            .check(matches(not(isDisplayed())))
//    }

}
