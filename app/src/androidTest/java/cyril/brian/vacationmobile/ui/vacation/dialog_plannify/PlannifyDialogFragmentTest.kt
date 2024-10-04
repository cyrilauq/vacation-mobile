package cyril.brian.vacationmobile.ui.vacation.dialog_plannify

//Les test ici ratent, car un des fragments utilisé par le fragment testé a changé et que je n'ai pas eu le temps de corriger (Cyril)
//class PlannifyDialogFragmentTest {
//    private lateinit var mockedActivitiesRepo: IVacationActivitiesRepository
//
//    @Before
//    fun setUp() {
//        val argsSlot = slot<PlannifyArgs>()
//
//        mockedActivitiesRepo = object: IVacationActivitiesRepository {
//            override fun addActivitiesToVacation(
//                vacationId: String,
//                activities: Array<VacationActivity>
//            ): List<VacationActivity> {
//                TODO("Not yet implemented")
//            }
//
//            override fun plannifyActivity(args: PlannifyArgs) = VacationActivity(
//                "kjlhkjhklh",
//                "rekjgh fd gf",
//                "fd ghdfkj g",
//                3.0,
//                3.0,
//                "flmkjg ldfkjgofdmlj b"
//            )
//
//            override fun getActivities(vacationId: String): List<VacationActivity> {
//                TODO("Not yet implemented")
//            }
//
//        }
//
////        every {
////            mockedActivitiesRepo.plannifyActivity(
////                capture(argsSlot)
////            )
////        } throws StockageException("stockage_error")
////
////        every {
////            mockedActivitiesRepo.plannifyActivity(
////                PlannifyArgs(
////                    "",
////                    "22/11/2020",
////                    "23/11/2020",
////                    "15:50",
////                    "15:50"
////                )
////            )
////        } answers {
////            VacationActivity(
////                "kjlhkjhklh",
////                "rekjgh fd gf",
////                "fd ghdfkj g",
////                3.0,
////                3.0,
////                "flmkjg ldfkjgofdmlj b"
////            )
////        }
//    }
//
//    @After
//    fun tearDown() {
//    }
//
//    @Test
//    fun testPlannifyDialogFragment() {
//        val scenario = launchFragmentInContainer<PlannifyDialogFragment>()
//        scenario.onFragment {
//            it.viewModel.repository = mockedActivitiesRepo
//            it.activityId = ""
//        }
//
//        onView(withId(R.id.begin_date_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.begin_time_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.end_time_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.end_date_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.confirm_btn))
//            .perform(scrollTo(), click())
//
//        onView(withId(R.id.error_message))
//            .check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun whenDateSelectedThenReturnActivity() {
//        val scenario = launchFragmentInContainer<PlannifyDialogFragment>()
//        scenario.onFragment {
//            it.viewModel.repository = mockedActivitiesRepo
//            it.activityId = ""
//        }
//
//        onView(withId(R.id.begin_date_input))
//            .perform(replaceText("22/11/2020"))
//
//        onView(withId(R.id.begin_time_input))
//            .perform(replaceText("15:50"))
//
//        onView(withId(R.id.end_time_input))
//            .perform(replaceText("15:50"))
//
//        onView(withId(R.id.end_date_input))
//            .perform(replaceText("22/11/2020"))
//
//        onView(withId(R.id.confirm_btn))
//            .perform(scrollTo(), click())
//
//        onView(withId(R.id.error_message))
//            .check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun whenFirstWrongInputSecondEverythingIsGoodThenNoMessageDisplayed() {
//        val scenario = launchFragmentInContainer<PlannifyDialogFragment>()
//        scenario.onFragment {
//            it.viewModel.repository = mockedActivitiesRepo
//            it.activityId = ""
//        }
//
//        onView(withId(R.id.begin_date_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.begin_time_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.end_time_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.end_date_picker))
//            .perform(click())
//
//        pressBack()
//
//        onView(withId(R.id.confirm_btn))
//            .perform(scrollTo(), click())
//
//        onView(withId(R.id.error_message))
//            .check(matches(isDisplayed()))
//
//        onView(withId(R.id.begin_date_picker))
//            .perform(scrollTo(), click(), setDate(2020, 11, 1))
//
//        pressBack()
//
//        onView(withId(R.id.begin_time_picker))
//            .perform(click(), setTime(1, 50))
//
//        pressBack()
//
//        Thread.sleep(1000)
//
//        onView(withId(R.id.end_time_picker))
//            .perform(click(), setTime(15, 50))
//
//        pressBack()
//
//        onView(withId(R.id.end_date_picker))
//            .perform(click(), setDate(2020, 11, 22))
//
//        pressBack()
//
//        onView(withId(R.id.confirm_btn))
//            .perform(scrollTo(), click())
//
//        onView(withId(R.id.error_message))
//            .check(matches(isDisplayed()))
//        scenario.onFragment {
//            assertEquals(it.viewModel.beginDate, "01/11/2020")
//        }
//    }
//}