package com.example.owllearn.ui.decks

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.owllearn.R
import com.example.owllearn.databinding.FragmentCreateAndEditDeckBinding
import com.example.owllearn.databinding.FragmentOnboardingBinding


class CreateAndEditDeckFragment : Fragment() {
    private var _binding: FragmentCreateAndEditDeckBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_create_and_edit_deck, container, false)
    }


}