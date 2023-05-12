package uz.devapp.e_control.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.devapp.e_control.R
import uz.devapp.e_control.adapters.AttendsAdapter
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.database.AppDatabase
import uz.devapp.e_control.database.entity.AttendsEntity
import uz.devapp.e_control.databinding.FragmentUploadBinding
import uz.devapp.e_control.utils.NetworkHelper
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UploadFragment : Fragment() {
    lateinit var binding: FragmentUploadBinding
    private val viewModel: MainViewModel by viewModels()
    private var networkHelper: NetworkHelper? = null
    private lateinit var adapter: AttendsAdapter
    val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.action_uploadFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        val attendsEntityList = appDatabase.attendsDao().getAttends() as ArrayList<AttendsEntity>
        val employeeEntityList = appDatabase.employeeDao().getEmployees()
        val purposeEntityList = appDatabase.purposeDao().getPurpose()

        if (attendsEntityList.isEmpty()) {
            binding.lottie.visibility = View.VISIBLE
        } else {
            binding.lottie.visibility = View.GONE
        }

        adapter = AttendsAdapter(attendsEntityList, employeeEntityList, purposeEntityList)
        binding.rv.adapter = adapter

        viewModel.attendsOfflineLiveData.observe(requireActivity()) {
            when (it) {
                is DataResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is DataResult.LoadingHide -> {
                    binding.progress.visibility = View.GONE
                }
                is DataResult.LoadingShow -> {
                    binding.progress.visibility = View.VISIBLE
                }
                is DataResult.Success -> {
                    if (appDatabase.attendsDao().getAttends().isEmpty()) {
                        attendsEntityList.removeAll(attendsEntityList.toSet())
                        adapter.notifyDataSetChanged()
                        if (attendsEntityList.isEmpty()) {
                            binding.lottie.visibility = View.VISIBLE
                        } else {
                            binding.lottie.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.upload.setOnClickListener {
            networkHelper = NetworkHelper(requireContext())
            if (networkHelper?.isNetworkConnected() == true) {
                attendsEntityList.forEach { attendsEntity ->
                    val sdf = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
                    val resultdate = Date(attendsEntity.date)
                    val format = sdf.format(resultdate)
                    binding.root.postDelayed({
                        viewModel.saveAttendsOffline(
                            attendsEntity.image,
                            attendsEntity.type,
                            format,
                            attendsEntity.employeeId,
                            attendsEntity.deviceId,
                            attendsEntity.purposeId
                        )
                    }, 100)
                    appDatabase.attendsDao().deleteAttends(attendsEntity)
                }
            } else {
                Toast.makeText(requireContext(), "Internet not connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = UploadFragment()
    }
}