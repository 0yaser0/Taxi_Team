package com.cmc.mytaxi.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmc.mytaxi.R
import com.cmc.mytaxi.data.local.dao.DriverDao
import com.cmc.mytaxi.data.local.models.Driver
import com.cmc.mytaxi.data.repository.DriverRepository
import com.cmc.mytaxi.databinding.ProfileFragmentLayoutBinding

class ProfileFragment : Fragment(R.layout.profile_fragment_layout) {

    private var _binding: ProfileFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var driverViewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileFragmentLayoutBinding.bind(view)

        val driverDao = DriverDao.getDatabase(requireContext()).driverDao()
        val repository = DriverRepository(driverDao)
        val factory = ProfileViewModelFactory(repository)
        driverViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        driverViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding.btnAddDriver.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val permiType = binding.etPermiType.text.toString()
            val age = binding.etAge.text.toString().toInt()

            driverViewModel.addDriver(Driver(id, firstName, lastName, age, permiType))
        }

        binding.btnGetAllDrivers.setOnClickListener {
            driverViewModel.getAllDrivers()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

