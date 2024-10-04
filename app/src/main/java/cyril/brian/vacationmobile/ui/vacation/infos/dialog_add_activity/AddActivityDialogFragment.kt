package cyril.brian.vacationmobile.ui.vacation.infos.dialog_add_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.databinding.FragmentAddActivityDialogBinding
import cyril.brian.vacationmobile.domain.Coordinate

class AddActivityDialogFragment : DialogFragment() {
    private var _binding: FragmentAddActivityDialogBinding? = null
    private var _mapFragment: SupportMapFragment? = null
    private val mapFragment get() = _mapFragment!!

    private val binding get() = _binding!!

    companion object {
        fun newInstance(): AddActivityDialogFragment {
            val frag = AddActivityDialogFragment()
            return frag
        }
    }

    val viewModel: AddActivityDialogViewModel by viewModels()
    var coordinate: Coordinate? = null

    private lateinit var mMap: GoogleMap

    /**
     * Callback that will be used when the map is ready
     */
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        // Add a marker to a specific location (e.g., Sydney, Australia)
        if(coordinate != null) {
            googleMap.clear()
            val latlng = LatLng(coordinate!!.lat, coordinate!!.lon)
            googleMap.addMarker(MarkerOptions().position(latlng).title("Your activity"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddActivityDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize the map fragment
        _mapFragment = childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        // We will call the callback when the map will be ready
        mapFragment.getMapAsync(callback)

        setEventListeners()

        return view
    }

    private fun setEventListeners() {
        binding.activityPlaceInput.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus && binding.activityPlaceInput.text.isNotEmpty()) {
                getPlace()
            }
        }

        binding.activityConfirmBtn.setOnClickListener {
            saveData()
        }

        binding.activityConfirmBtnBtn.setOnClickListener {
            binding.activityPlaceInput.clearFocus()
            saveData()
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun getPlace() {
        coordinate = viewModel.getCoordinatesFor(binding.activityPlaceInput.text.toString())
        if(coordinate != null) {
            mapFragment.getMapAsync(callback)
        } else {
            Toast.makeText(requireContext(), viewModel.error, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveData() {
        if(coordinate == null && binding.activityPlaceInput.text.isNotEmpty()) {
            getPlace()
        }
        viewModel.title = binding.activityNameInput.text.toString()
        viewModel.description = binding.activityDescriptionInput.text.toString()
        viewModel.place = binding.activityPlaceInput.text.toString()
        viewModel.lat = coordinate!!.lat
        viewModel.lon = coordinate!!.lon
        if(viewModel.save()) {
            mapFragment.getMapAsync(callback)
            Toast.makeText(requireContext(), "Activity added", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), viewModel.error, Toast.LENGTH_LONG).show()
        }
    }

}