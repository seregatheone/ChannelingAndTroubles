package com.example.a20.ui.first

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import com.example.a20.databinding.FragmentFirstBinding
import com.example.a20.ui.second.SharedFragmentViewModel
import com.example.a20.ui.second.SharedFragmentViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirstFragment : DaggerFragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedFragmentViewModelFactory: SharedFragmentViewModelFactory

    private val sharedFragmentViewModel : SharedFragmentViewModel by viewModels{sharedFragmentViewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textViewFirst.text = sharedFragmentViewModel.currentWork.value.toString()
        if (sharedFragmentViewModel.firstFragmentButtonClicked.value){
            makeObserver()
            launchCoroutineChannel()
        }
        binding.buttonStart.setOnClickListener {
            sharedFragmentViewModel.firstFragmentButtonClicked.value = true
            makeObserver()
            launchCoroutineChannel()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun makeObserver(){
        sharedFragmentViewModel.currentWork.observe(viewLifecycleOwner){
            binding.textViewFirst.text = it.toString()
        }
    }
    private fun launchCoroutineChannel(){
        CoroutineScope(Dispatchers.Default).launch {
            sharedFragmentViewModel.workWithChannel()
        }
    }
}