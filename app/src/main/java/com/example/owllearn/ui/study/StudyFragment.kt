package com.example.owllearn.ui.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.owllearn.databinding.FragmentStudyBinding
import com.example.owllearn.ui.decks.data.viewmodel.DecksViewModel

class StudyFragment : Fragment() {

private var _binding: FragmentStudyBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val studyViewModel =
            ViewModelProvider(this).get(DecksViewModel::class.java)

    _binding = FragmentStudyBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textHome

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}