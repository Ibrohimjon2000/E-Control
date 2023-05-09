package uz.devapp.e_control.screen

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.budiyev.android.codescanner.*
import dagger.hilt.android.AndroidEntryPoint
import uz.devapp.e_control.utils.Constants
import uz.devapp.e_control.R
import uz.devapp.e_control.data.model.EmployeeModel
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.databinding.FragmentQrCodeBinding

@AndroidEntryPoint
class QrCodeFragment : Fragment() {
    lateinit var binding: FragmentQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    val MY_CAMERA_PERMISSION_REQUEST = 1111
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.action_qrCodeFragment_to_homeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        viewModel.employeeLiveData.observe(requireActivity()) {
            when (it) {
                is DataResult.Error -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
                is DataResult.LoadingHide -> {

                }
                is DataResult.LoadingShow -> {

                }
                is DataResult.Success -> {
                    if (it.result != null) {
                        val bundle = Bundle()
                        bundle.putSerializable(Constants.EXTRA_DATA, it.result)
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_qrCodeFragment_to_setStatusFragment, bundle)
                    }
                }
            }
        }

        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                viewModel.getEmployeeByPinCode(it.text)
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), "Camera Error: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
                requireActivity().findNavController(R.id.fragmentContainerView).popBackStack()
            }
        }

        checkPermission()

        return binding.root
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                MY_CAMERA_PERMISSION_REQUEST
            )
        } else {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_CAMERA_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            codeScanner.startPreview()
        } else {
            Toast.makeText(
                requireContext(),
                "Can not scan until you give the camera permission",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() = QrCodeFragment()
    }

}