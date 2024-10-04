package cyril.brian.vacationmobile.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import cyril.brian.vacationmobile.databinding.FragmentPeriodPickerBinding
import cyril.brian.vacationmobile.ui.utils.getStringDate
import cyril.brian.vacationmobile.ui.utils.getStringTime
import java.util.Calendar

class PeriodPickerFragment : Fragment() {
    private var _binding: FragmentPeriodPickerBinding? = null
    private val binding get() = _binding!!

    private var isBegin = true

    val selectedBeginDate get() = binding.beginDateInput.text.toString()
    val selectedEndDate get() = binding.endDateInput.text.toString()
    val selectedBeginTime get() = binding.beginTimeInput.text.toString()
    val selectedEndTime get() = binding.endTimeInput.text.toString()

    companion object {
        fun newInstance() = PeriodPickerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeriodPickerBinding.inflate(inflater, container, false)
        val view = binding.root

        setInputType()
        setListeners()

        return view
    }

    private fun setInputType() {
        binding.beginDateInput.inputType = InputType.TYPE_NULL
        binding.endDateInput.inputType = InputType.TYPE_NULL
        binding.endTimeInput.inputType = InputType.TYPE_NULL
        binding.beginTimeInput.inputType = InputType.TYPE_NULL
    }

    private fun setListeners() {
        binding.beginDateInput.setOnFocusChangeListener { it, hasFocus ->
            if(hasFocus) {
                it.performClick()
            }
        }
        binding.beginDateInput.setOnClickListener {
            dateInputSelected(true)
        }
        binding.endDateInput.setOnFocusChangeListener { it, hasFocus ->
            if(hasFocus) {
                it.performClick()
            }
        }
        binding.endDateInput.setOnClickListener {
            dateInputSelected(false)
        }
        binding.beginTimeInput.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                timeInputSelected(true)
            }
        }
        binding.beginTimeInput.setOnClickListener {
            timeInputSelected(true)
        }
        binding.endTimeInput.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                timeInputSelected(false)
            }
        }
        binding.endTimeInput.setOnClickListener {
            timeInputSelected(false)
        }
    }

    private fun dateInputSelected(isBegin: Boolean) {
        showDatePicker()
        this.isBegin = isBegin
    }

    private fun timeInputSelected(isBegin: Boolean) {
        showTimePicker()
        this.isBegin = isBegin
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(requireContext(),
            { picker: DatePicker, selectedYear: Int, selectedMonth: Int, day: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate[selectedYear, selectedMonth] = day
                updateDateTimeText(picker)
            }, year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { picker, selectedHour, selectedMinute ->
                // Handle the selected time
                val selectedTime = Calendar.getInstance()
                selectedTime[Calendar.HOUR_OF_DAY] = selectedHour
                selectedTime[Calendar.MINUTE] = selectedMinute
                updateTimeText(picker)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun updateDateTimeText(date: DatePicker) {
        if(isBegin) {
            binding.beginDateInput.text.clear()
            binding.beginDateInput.text.append(date.getStringDate())
        } else {
            binding.endDateInput.text.clear()
            binding.endDateInput.text.append(date.getStringDate())
        }
    }

    private fun updateTimeText(picker: TimePicker) {
        if(isBegin) {
            binding.beginTimeInput.text.clear()
            binding.beginTimeInput.text.append(picker.getStringTime())
        } else {
            binding.endTimeInput.text.clear()
            binding.endTimeInput.text.append(picker.getStringTime())
        }
    }

}