package com.cmc.taxyteam.ui.fragments.profile

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cmc.taxyteam.App
import com.cmc.taxyteam.R
import com.cmc.taxyteam.data.local.models.Driver
import com.cmc.taxyteam.data.repository.DriverRepository
import com.cmc.taxyteam.databinding.FragmentEditProfileBinding
import com.cmc.taxyteam.data.viewmodel.ProfileViewModel
import com.cmc.taxyteam.data.viewmodel.ProfileViewModelFactory
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel
    private lateinit var PersonalInfos: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)
        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        driverViewModel.getDriverById(1).observe(viewLifecycleOwner) { driver ->
            driver?.let {
                displayDriverDetails(it)
            }
        }

        binding.goToPosition.setOnClickListener {
            requireActivity().finish()
        }

    }

    fun QRCodegenerator(infos:String){
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(infos, BarcodeFormat.QR_CODE, 400, 400)
            binding.qrcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun displayDriverDetails(driver: Driver) {
        binding.fname.text = driver.firstName
        binding.lname.text = driver.lastName
        binding.age.text = driver.age.toString()
        binding.permiType.text = driver.permiType

        PersonalInfos = """
            Nom: ${driver.firstName}
            Prenom: ${driver.lastName}
            Age: ${driver.age}
            Type De Permie: ${driver.permiType}
        """.trimIndent()

        QRCodegenerator(PersonalInfos)
    }


}