package com.example.besinkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.besinkitabi.databinding.FragmentBesinDetayBinding
import com.example.besinkitabi.util.gorselIndir
import com.example.besinkitabi.util.placeHolderYap
import com.example.besinkitabi.viewmodel.BesinDetayViewModel


class BesinDetayFragment : Fragment() {

    private var _binding : FragmentBesinDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : BesinDetayViewModel
    var besinId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBesinDetayBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[BesinDetayViewModel::class.java]

        arguments?.let{
            besinId = BesinDetayFragmentArgs.fromBundle(it).besinId
        }

        viewModel.roomVerisiniAl(besinId)
        liveDataObserve()
    }


    private fun liveDataObserve(){
        viewModel.besinLiveData.observe(viewLifecycleOwner){
            binding.besinIsim.text = it.besinIsim
            binding.besinKalori.text = it.besinKalori
            binding.besinKarbonhidrat.text = it.besinKarbonhidrat
            binding.besinProtein.text = it.besinProtein
            binding.besinIYag.text = it.besinYag
            binding.besinImage.gorselIndir(it.besinGorsel, placeHolderYap(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}