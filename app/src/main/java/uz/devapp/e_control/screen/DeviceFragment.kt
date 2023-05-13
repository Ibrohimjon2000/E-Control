package uz.devapp.e_control.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.devapp.e_control.R
import uz.devapp.e_control.data.model.request.DeviceRequest
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.databinding.FragmentDeviceBinding
import uz.devapp.e_control.utils.NetworkHelper
import uz.devapp.e_control.utils.PrefUtils

@AndroidEntryPoint
class DeviceFragment : Fragment() {
    lateinit var binding: FragmentDeviceBinding
    private val viewModel: MainViewModel by viewModels()
    private var networkHelper: NetworkHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceBinding.inflate(inflater, container, false)
        binding.apply {

            viewModel.deviceLiveData.observe(requireActivity()) {
                when (it) {
                    is DataResult.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataResult.LoadingHide -> {
                        progress.visibility = View.GONE
                    }
                    is DataResult.LoadingShow -> {
                        progress.visibility = View.VISIBLE
                    }
                    is DataResult.Success -> {
                        PrefUtils.setToken(it.result.token)
                        PrefUtils.setId(it.result.id)
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_deviceFragment_to_homeFragment)
                    }
                }
            }

            btnLogin.setOnClickListener {
                if (edDeviceId.text.toString().isNotEmpty() && edPassword.text.toString()
                        .isNotEmpty()
                ) {
                    networkHelper = NetworkHelper(requireContext())
                    if (networkHelper?.isNetworkConnected() == true) {
                        viewModel.getDevice(
                            DeviceRequest(
                                edDeviceId.text.toString().toInt(),
                                edPassword.text.toString()
                            )
                        )
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
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = DeviceFragment()
    }
}