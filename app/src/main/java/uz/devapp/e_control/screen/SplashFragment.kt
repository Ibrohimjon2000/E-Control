package uz.devapp.e_control.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import uz.devapp.e_control.R
import uz.devapp.e_control.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.root.postDelayed({
            requireActivity().findNavController(R.id.fragmentContainerView)
                .navigate(R.id.action_splashFragment_to_homeFragment)
        }, 2000)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}