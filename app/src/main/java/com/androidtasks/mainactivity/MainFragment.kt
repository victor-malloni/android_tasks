package com.androidtasks.mainactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.androidtasks.mainactivity.databinding.FragmentMainBinding
import com.androidtasks.mainactivity.util.navTo

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        binding.androidtasksToastSnake.setOnClickListener{navTo(R.id.toastSnakeFragment)}




    }
}