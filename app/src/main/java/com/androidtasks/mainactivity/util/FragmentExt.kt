package com.androidtasks.mainactivity.util

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

fun Fragment.navTo(@IdRes dest: Int) = findNavController().navigate(dest)
fun Fragment.navTo(directions: NavDirections) = findNavController().navigate(directions)
fun Fragment.navTo(@IdRes dest: Int, args: Bundle) = findNavController().navigate(dest, args)
fun Fragment.toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
fun Fragment.snake(view: View, msg: String) = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

fun Fragment.startActivity(clazz: Class<*>, name: String = "", args: Bundle = Bundle()){
    val intent = Intent(requireContext(), clazz).apply {
        if(!(name.isNullOrEmpty() && args.isEmpty)) {
            putExtra(name,args)
        }
    }
    requireActivity().startActivity(intent)
}
