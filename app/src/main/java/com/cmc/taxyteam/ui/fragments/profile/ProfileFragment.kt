package com.cmc.taxyteam.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.R
import com.cmc.mytaxi.databinding.ProfileFragmentLayoutBinding
import com.cmc.taxyteam.App
import com.cmc.taxyteam.data.local.models.Driver
import com.cmc.taxyteam.data.repository.DriverRepository
import com.cmc.taxyteam.data.viewmodel.ProfileViewModel
import com.cmc.taxyteam.data.viewmodel.ProfileViewModelFactory
import com.cmc.taxyteam.ui.activity.HomePage

class ProfileFragment : Fragment(R.layout.profile_fragment_layout) {

    private var _binding: ProfileFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileFragmentLayoutBinding.bind(view)

        val driverRepository = DriverRepository(App.database.driverDao())
        val factory = ProfileViewModelFactory(driverRepository)

        driverViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        binding.btnAddDriver.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val permiType = binding.etPermiType.text.toString()
            val age = binding.etAge.text.toString().toInt()

            val driver = Driver(driverId = 1, firstName = firstName, lastName = lastName, age = age, permiType = permiType ,isCreated = true)
            driverViewModel.addDriver(driver)

            val intent = Intent(requireContext(), HomePage::class.java)
            startActivity(intent)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


