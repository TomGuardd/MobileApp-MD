package com.capstone.tomguard.ui.predict

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.capstone.tomguard.R
import com.capstone.tomguard.databinding.ActivityCameraxBinding
import com.capstone.tomguard.databinding.FragmentPredictBinding
import com.capstone.tomguard.ui.predict.CameraxActivity.Companion.CAMERAX_RESULT

/**
 * A simple [Fragment] subclass.
 * Use the [PredictFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PredictFragment : Fragment() {

    private lateinit var binding: FragmentPredictBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPredictBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        //  binding.layoutButton.galleryButton.setOnClickListener { startGallery() }
        //  binding.layoutButton.cameraButton.setOnClickListener { startCamera() }
        binding.layoutButton.cameraXButton.setOnClickListener { startCameraX() }
        //        binding.btnPredict.setOnClickListener {
        //            currentImageUri?.let {
        //                analyzeImage(it)
        //            } ?: run {
        //                showToast(getString(R.string.empty_image_warning))
        //            }
        //        }
    }


    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraxActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraxActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    //    private fun analyzeImage(uri: Uri) {
    //        val intent = Intent(this, ResultActivity::class.java)
    //        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
    //        startActivity(intent)
    //    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}