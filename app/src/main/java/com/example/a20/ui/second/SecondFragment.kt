package com.example.a20.ui.second

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.a20.Status
import com.example.a20.databinding.FragmentSecondBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SecondFragment : DaggerFragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedFragmentViewModelFactory: SharedFragmentViewModelFactory

    private val sharedFragmentViewModel : SharedFragmentViewModel by viewModels{sharedFragmentViewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val statusTextView = binding.showDataTextView
        sharedFragmentViewModel.myStatus.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    Status.SUCCESS -> {
                        statusTextView.text = "SUCCESS"
                    }
                    Status.ERROR -> {
                        statusTextView.text = "ERROR"
                    }
                    Status.LOADING -> {
                        statusTextView.text = "LOADING"
                    }
                    Status.JSON -> {
                        statusTextView.text = "JSON"
                    }
                }
            }
        }

        binding.makeRequest.setOnClickListener {
            sharedFragmentViewModel.getRequest().observe(viewLifecycleOwner){
                Log.i("getRequest",it.toString())
            }
        }

        binding.randomData.setOnClickListener {
            sharedFragmentViewModel.getDataFromJSON()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}