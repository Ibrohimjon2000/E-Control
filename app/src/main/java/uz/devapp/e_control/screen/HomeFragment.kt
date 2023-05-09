package uz.devapp.e_control.screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.devapp.e_control.utils.Constants
import uz.devapp.e_control.R
import uz.devapp.e_control.data.model.EmployeeModel
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.databinding.FragmentHomeBinding
import uz.devapp.e_control.utils.Constants.TAG

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var inputTextBuilder: StringBuilder
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            inputTextBuilder = StringBuilder()
            binding.btnQr.setOnClickListener {
                requireActivity().findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.action_homeFragment_to_qrCodeFragment)
            }

            btn1.setOnClickListener {
                inputTextBuilder.append(btn1.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn2.setOnClickListener {
                inputTextBuilder.append(btn2.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn3.setOnClickListener {
                inputTextBuilder.append(btn3.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn4.setOnClickListener {
                inputTextBuilder.append(btn4.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn5.setOnClickListener {
                inputTextBuilder.append(btn5.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn6.setOnClickListener {
                inputTextBuilder.append(btn6.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn7.setOnClickListener {
                inputTextBuilder.append(btn7.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn8.setOnClickListener {
                inputTextBuilder.append(btn8.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn9.setOnClickListener {
                inputTextBuilder.append(btn9.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btn0.setOnClickListener {
                inputTextBuilder.append(btn0.text.toString())
                inputText.setText(inputTextBuilder.toString())
            }

            btnBack.setOnClickListener {
                if (inputTextBuilder.length >= 1) {
                    inputTextBuilder.setLength(inputTextBuilder.length - 1)
                    inputText.setText(inputTextBuilder.toString())
                }
            }

            inputText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                    when (p0!!.length) {
                        0 -> {
                            input1.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                            input2.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                            input3.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                            input4.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                        }
                        1 -> {
                            input1.setBackgroundResource(R.drawable.pincode_circle_background)
                            input2.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                            input3.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                            input4.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                        }
                        2 -> {
                            input1.setBackgroundResource(R.drawable.pincode_circle_background)
                            input2.setBackgroundResource(R.drawable.pincode_circle_background)
                            input3.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                            input4.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                        }
                        3 -> {
                            input1.setBackgroundResource(R.drawable.pincode_circle_background)
                            input2.setBackgroundResource(R.drawable.pincode_circle_background)
                            input3.setBackgroundResource(R.drawable.pincode_circle_background)
                            input4.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                        }
                        4 -> {
                            input1.setBackgroundResource(R.drawable.pincode_circle_background)
                            input2.setBackgroundResource(R.drawable.pincode_circle_background)
                            input3.setBackgroundResource(R.drawable.pincode_circle_background)
                            input4.setBackgroundResource(R.drawable.pincode_circle_background)
                            viewModel.getEmployeeByPinCode(p0.toString())
                            viewModel.employeeLiveData.observe(requireActivity()) {
                                when (it) {
                                    is DataResult.Error -> {
                                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                                        input1.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                                        input2.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                                        input3.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                                        input4.setBackgroundResource(R.drawable.pincode_circle_empty_background)
                                        inputText.setText("")
                                        inputTextBuilder = StringBuilder()
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
                                                .navigate(R.id.action_homeFragment_to_setStatusFragment, bundle)
                                            inputText.setText("")
                                            inputTextBuilder = StringBuilder()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}