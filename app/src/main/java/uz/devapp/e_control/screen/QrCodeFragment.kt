package uz.devapp.e_control.screen

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.budiyev.android.codescanner.*
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import uz.devapp.e_control.R
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.database.AppDatabase
import uz.devapp.e_control.databinding.FragmentQrCodeBinding
import uz.devapp.e_control.utils.Constants
import uz.devapp.e_control.utils.NetworkHelper

@AndroidEntryPoint
class QrCodeFragment : Fragment() {
    lateinit var binding: FragmentQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    private val viewModel: MainViewModel by viewModels()
    private var networkHelper: NetworkHelper? = null
    val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

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
                    codeScanner.startPreview()
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
                networkHelper = NetworkHelper(requireContext())
                if (networkHelper?.isNetworkConnected() == true) {
                    viewModel.getEmployeeByPinCode(it.text)
                } else {
                    if (appDatabase.employeeDao().getEmployees().isNotEmpty()) {
                        val employeeEntityList = appDatabase.employeeDao().getEmployees()
                        employeeEntityList.forEach { employeeEntity ->
                            if (employeeEntity.pinCode == it.text.toInt()) {
                                val bundle = Bundle()
                                bundle.putSerializable(Constants.EXTRA_DATA, employeeEntity)
                                requireActivity().findNavController(R.id.fragmentContainerView)
                                    .navigate(
                                        R.id.action_qrCodeFragment_to_setStatusFragment,
                                        bundle
                                    )
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Internet not connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), "Camera Error: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        PermissionX.init(requireActivity())
            .permissions(
                Manifest.permission.CAMERA
            )
            .request { allGranted, grantedList, deniedList ->
                codeScanner.startPreview()
            }

        return binding.root
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