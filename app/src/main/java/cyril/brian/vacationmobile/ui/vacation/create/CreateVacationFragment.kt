package cyril.brian.vacationmobile.ui.vacation.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.databinding.FragmentCreateVacationBinding
import cyril.brian.vacationmobile.domain.Coordinate
import cyril.brian.vacationmobile.ui.components.PeriodPickerFragment

class CreateVacationFragment : Fragment() {
    private var _binding: FragmentCreateVacationBinding? = null

    val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    lateinit var consumer: () -> Unit

    companion object {
        fun newInstance(consumer: () -> Unit): CreateVacationFragment {
            val frag = CreateVacationFragment()
            frag.consumer = consumer
            return frag
        }
    }

    val viewModel: CreateVacationViewModel by viewModels()
    var step = 1
    var coordinate: Coordinate? = null

    /**
     * Callback that will be used when the map is ready
     */
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        if(coordinate != null) {
            googleMap.clear()
            val latlng = LatLng(coordinate!!.lat, coordinate!!.lon)
            googleMap.addMarker(MarkerOptions().position(latlng).title("Your vacation"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateVacationBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize the map fragment
        mapFragment = childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        // We will call the callback when the map will be ready
        mapFragment.getMapAsync(callback)

        binding.vacationConfirmButton.setOnClickListener {
            viewModel.save()
        }
        binding.vacationTitleField.doOnTextChanged { text, _, _, _ -> viewModel.title = String(text!!.toList().toCharArray())}
        binding.vacationPlaceField.doOnTextChanged { text, _, _, _ -> viewModel.place = String(text!!.toList().toCharArray())}
        binding.vacationDescriptionField.doOnTextChanged { text, _, _, _ -> viewModel.description = String(text!!.toList().toCharArray())}
        binding.vacationPlaceField.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus && binding.vacationPlaceField.text.isNotEmpty()) {
                getPlace()
            }
        }

        binding.vacationConfirmButton.setOnClickListener {
            val periodPicker = binding.periodPicker.getFragment<PeriodPickerFragment>()
            viewModel.date_begin = periodPicker.selectedBeginDate
            viewModel.date_end = periodPicker.selectedEndDate
            viewModel.hour_begin = periodPicker.selectedBeginTime
            viewModel.hour_end = periodPicker.selectedEndTime

            if(coordinate == null && binding.vacationPlaceField.text.isNotEmpty()) {
                getPlace()
            }
            binding.addVacationError.visibility = View.GONE
            binding.addVacationError.text = ""
            if(!viewModel.isValid() || !viewModel.save()) {
                binding.addVacationError.visibility = View.VISIBLE
                binding.addVacationError.text = viewModel.error
                Log.d("CreateVacationFragment", viewModel.error)
            } else {
                Toast.makeText(requireContext(), "Vacation successfully created", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    private fun getPlace() {
        coordinate = viewModel.getCoordinatesFor(viewModel.place)
        if(coordinate != null) {
            mapFragment.getMapAsync(callback)
        } else {
            Toast.makeText(requireContext(), viewModel.error, Toast.LENGTH_LONG).show()
        }
    }

}