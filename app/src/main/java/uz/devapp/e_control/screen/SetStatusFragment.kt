package uz.devapp.e_control.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import uz.devapp.e_control.R
import uz.devapp.e_control.adapters.PurposeAdapter
import uz.devapp.e_control.adapters.PurposeAdapterCallback
import uz.devapp.e_control.data.model.request.PurposeRequest
import uz.devapp.e_control.data.repository.sealed.DataResult
import uz.devapp.e_control.database.AppDatabase
import uz.devapp.e_control.database.entity.AttendsEntity
import uz.devapp.e_control.database.entity.EmployeeEntity
import uz.devapp.e_control.database.entity.PurposeEntity
import uz.devapp.e_control.databinding.FragmentSetStatusBinding
import uz.devapp.e_control.utils.Constants
import uz.devapp.e_control.utils.NetworkHelper
import uz.devapp.e_control.utils.PrefUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val ARG_PARAM1 = Constants.EXTRA_DATA

@AndroidEntryPoint
class SetStatusFragment : Fragment() {
    private var param1: EmployeeEntity? = null
    lateinit var binding: FragmentSetStatusBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val viewModel: MainViewModel by viewModels()
    private var type = ""
    private var networkHelper: NetworkHelper? = null
    val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext())
    }


    private var mTimeLeftInMillis: Long = 2000
    private var mEndTime: Long = 0
    private var mCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (Build.VERSION.SDK_INT < 33) {
                param1 = it.getSerializable(ARG_PARAM1) as EmployeeEntity
            } else {
                param1 = it.getSerializable(ARG_PARAM1, EmployeeEntity::class.java)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetStatusBinding.inflate(inflater, container, false)
        binding.apply {
            outputDirectory = getOutputDirectory()
            cameraExecutor = Executors.newSingleThreadExecutor()

            PermissionX.init(requireActivity())
                .permissions(
                    Manifest.permission.CAMERA
                )
                .request { allGranted, grantedList, deniedList ->
                    startCamera()
                }

            tvName.text = param1!!.name
            tvJob.text = param1!!.position

            binding.btnCome.setOnClickListener {
                startTimer()
                binding.card.visibility = View.GONE
                type = "input"
            }

            binding.btnLeave.setOnClickListener {
                startTimer()
                binding.card.visibility = View.GONE
                type = "output"
            }

            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.action_setStatusFragment_to_homeFragment)
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(callback)

            viewModel.purposeLiveData.observe(requireActivity()) {
                when (it) {
                    is DataResult.Error -> {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataResult.LoadingHide -> {

                    }
                    is DataResult.LoadingShow -> {

                    }
                    is DataResult.Success -> {
                        requireActivity().findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.action_setStatusFragment_to_homeFragment)
                    }
                }
            }

            viewModel.attendsLiveData.observe(requireActivity()) {
                when (it) {
                    is DataResult.Error -> {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is DataResult.LoadingHide -> {
                        progress.visibility = View.GONE
                    }
                    is DataResult.LoadingShow -> {
                        progress.visibility = View.VISIBLE
                    }
                    is DataResult.Success -> {
                        card.visibility = View.GONE
                        cardResult.visibility = View.VISIBLE
                        resultName.text = it.result.employee.name
                        resultTime.text =
                            (if (type == "input") "Keldi: " else "Ketdi: ") + it.result.nowTime
                        status.text = if ((it.result.moment)) "" else "Kech qoldi"
                        if (it.result.moment) {
                            status.visibility = View.GONE
                        } else {
                            status.visibility = View.VISIBLE
                        }

                        if ((it.result.moment)) {
                            rv.visibility = View.GONE
                            root.postDelayed({
                                requireActivity().findNavController(R.id.fragmentContainerView)
                                    .navigate(R.id.action_setStatusFragment_to_homeFragment)
                            }, 2000)
                        } else {
                            rv.visibility = View.VISIBLE
                            rv.adapter =
                                PurposeAdapter(it.result.purposes, object : PurposeAdapterCallback {
                                    override fun onClickListener(item: PurposeEntity) {
                                        viewModel.setPurpose(
                                            PurposeRequest(
                                                it.result.attendanceId,
                                                PrefUtils.getId(),
                                                param1!!.id,
                                                item.id
                                            ), requireContext()
                                        )
                                    }
                                })
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            )
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        val compressedImageFile = Compressor.compress(requireContext(), photoFile)
                        val savedUri = Uri.fromFile(compressedImageFile)
                        networkHelper = NetworkHelper(requireContext())
                        if (networkHelper?.isNetworkConnected() == true) {
                            viewModel.saveAttends(
                                savedUri.path.toString(),
                                type,
                                param1!!.id,
                                PrefUtils.getId()
                            )
                            Glide.with(requireContext())
                                .load(savedUri.path.toString())
                                .into(binding.image)
                        } else {
                            val currentTimeMillis = System.currentTimeMillis()
                            val sdf = SimpleDateFormat("HH:mm")
                            val resultdate = Date(currentTimeMillis)
                            val format = sdf.format(resultdate)
                            val moment = resultdate.hours < 8

                            Glide.with(requireContext())
                                .load(savedUri.path.toString())
                                .into(binding.image)
                            binding.card.visibility = View.GONE
                            binding.cardResult.visibility = View.VISIBLE
                            binding.resultName.text = param1!!.name
                            binding.resultTime.text =
                                (if (type == "input") "Keldi: " else "Ketdi: ") + format
                            if (type == "input") {
                                binding.status.text = if ((moment)) "" else "Kech qoldi"
                                if (moment) {
                                    binding.status.visibility = View.GONE
                                } else {
                                    binding.status.visibility = View.VISIBLE
                                }
                            } else {
                                binding.status.visibility = View.GONE
                            }

                            when (type) {
                                "input" -> {
                                    if ((moment)) {
                                        binding.rv.visibility = View.GONE
                                        binding.root.postDelayed({
                                            requireActivity().findNavController(R.id.fragmentContainerView)
                                                .navigate(R.id.action_setStatusFragment_to_homeFragment)
                                        }, 2000)
                                    } else {
                                        binding.rv.visibility = View.VISIBLE
                                        binding.rv.adapter =
                                            PurposeAdapter(
                                                appDatabase.purposeDao().getPurpose(),
                                                object : PurposeAdapterCallback {
                                                    override fun onClickListener(item: PurposeEntity) {
                                                        appDatabase.attendsDao().addAttends(
                                                            AttendsEntity(
                                                                image = savedUri.path.toString(),
                                                                type = type,
                                                                employeeId = param1!!.id,
                                                                deviceId = PrefUtils.getId(),
                                                                date = currentTimeMillis,
                                                                purposeId = item.id
                                                            )
                                                        )
                                                        requireActivity().findNavController(R.id.fragmentContainerView)
                                                            .navigate(R.id.action_setStatusFragment_to_homeFragment)
                                                    }
                                                })
                                    }
                                }
                                "output" -> {
                                    appDatabase.attendsDao().addAttends(
                                        AttendsEntity(
                                            image = savedUri.path.toString(),
                                            type = type,
                                            employeeId = param1!!.id,
                                            deviceId = PrefUtils.getId(),
                                            date = currentTimeMillis
                                        )
                                    )
                                    binding.rv.visibility = View.GONE
                                    binding.root.postDelayed({
                                        requireActivity().findNavController(R.id.fragmentContainerView)
                                            .navigate(R.id.action_setStatusFragment_to_homeFragment)
                                    }, 2000)
                                }
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )
            } catch (e: Exception) {
                Log.d(Constants.TAG, "startCamera Fail:", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis

        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                binding.counter.visibility = View.VISIBLE
                val seconds = mTimeLeftInMillis / 1000 + 1
                binding.counter.text = seconds.toString()
            }

            override fun onFinish() {
                binding.counter.visibility = View.GONE
                takePhoto()
            }
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: EmployeeEntity) =
            SetStatusFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}