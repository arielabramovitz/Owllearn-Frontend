package com.example.owllearn.ui.create_edit_card

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.owllearn.R

class CreateAndEditCardFragment : Fragment() {
    private lateinit var viewModel: CreateAndEditCardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        return inflater.inflate(R.layout.fragment_create_and_edit_card, container, false)
    }

}