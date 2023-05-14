package uz.devapp.e_control.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import uz.devapp.e_control.R
import uz.devapp.e_control.data.model.request.DeviceRequest
import uz.devapp.e_control.databinding.FragmentSettingsBinding
import uz.devapp.e_control.utils.NetworkHelper
import uz.devapp.e_control.utils.PrefUtils

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.action_settingsFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.tvDeviceName.text = PrefUtils.getDevice().name

        binding.switchOff.isChecked = PrefUtils.getSwitch()

        binding.switchOff.setOnClickListener {
            PrefUtils.setSwitch(binding.switchOff.isChecked)
        }

        binding.exit.setOnClickListener {
            val customDialog = layoutInflater.inflate(R.layout.exit_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(customDialog)
            mBuilder.setCancelable(false)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            customDialog.findViewById<TextView>(R.id.save).setOnClickListener {
                PrefUtils.clear()
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.action_settingsFragment_to_splashFragment)
                mAlertDialog.dismiss()
            }
            customDialog.findViewById<TextView>(R.id.exit).setOnClickListener {
                mAlertDialog.cancel()
            }
        }
        binding.upload.setOnClickListener {
            requireActivity().findNavController(R.id.fragmentContainerView)
                .navigate(R.id.action_settingsFragment_to_uploadFragment)
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}