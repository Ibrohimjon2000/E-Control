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
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.database.AppDatabase
import uz.devapp.e_control.databinding.FragmentSplashBinding
import uz.devapp.e_control.utils.NetworkHelper
import uz.devapp.e_control.utils.PrefUtils

@AndroidEntryPoint
class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    private val viewModel: MainViewModel by viewModels()
    private var networkHelper: NetworkHelper? = null
    val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        PrefUtils.init()
        viewModel.employeeEntityLiveData.observe(requireActivity()) {
            when (it) {
                is DataResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is DataResult.LoadingHide -> {

                }
                is DataResult.LoadingShow -> {

                }
                is DataResult.Success -> {
                    appDatabase.employeeDao().addEmployees(it.result!!)
                    if (PrefUtils.getToken().isEmpty()) {
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_splashFragment_to_deviceFragment)
                    } else {
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_splashFragment_to_homeFragment)
                    }
                }
            }
        }

        viewModel.purposeGetLiveData.observe(requireActivity()) {
            when (it) {
                is DataResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is DataResult.LoadingHide -> {

                }
                is DataResult.LoadingShow -> {

                }
                is DataResult.Success -> {
                    appDatabase.purposeDao().addPurpose(it.result!!)
                }
            }
        }

        networkHelper = NetworkHelper(requireContext())
        if (networkHelper?.isNetworkConnected() == true) {
            viewModel.getEmployees()
            viewModel.getPurpose()
        } else {
            if (appDatabase.employeeDao().getEmployees().isNotEmpty()) {
                binding.root.postDelayed({
                    if (PrefUtils.getToken().isEmpty()) {
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_splashFragment_to_deviceFragment)
                    } else {
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_splashFragment_to_homeFragment)
                    }
                }, 2000)
            } else {
                Toast.makeText(requireContext(), "Internet not connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}