package com.androidtasks.mainactivity.core.toast
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidtasks.mainactivity.R
import com.androidtasks.mainactivity.databinding.FragmentToastSnakeBinding
import com.google.android.material.snackbar.Snackbar

class ToastSnakeFragment : Fragment(R.layout.fragment_toast_snake) {

    private lateinit var binding: FragmentToastSnakeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        binding = FragmentToastSnakeBinding.bind(view)

        binding.toast.setOnClickListener {
            val msg = "msg toast"
            Toast
                .makeText(requireContext(), msg, Toast.LENGTH_SHORT)
                .show()
        }

        // Material design guideline:
        // https://material.io/archive/guidelines/components/snackbars-toasts.html#snackbars-toasts-usage
        binding.snake.setOnClickListener {
            Snackbar.make(view, "msg snakebar", Snackbar.LENGTH_SHORT).show()
        }

        binding.snakeAction.setOnClickListener {
            Snackbar
                .make(view, "msg snake com Action", Snackbar.LENGTH_LONG)
                .setAction("okay?") { Toast
                    .makeText(requireContext(), "msg por: snakeAction", Toast.LENGTH_SHORT)
                    .show()
                 }
                .show()
        }
    }
}