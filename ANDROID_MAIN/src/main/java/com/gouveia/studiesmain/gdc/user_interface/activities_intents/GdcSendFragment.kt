package com.gouveia.studiesmain.gdc.user_interface.activities_intents

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcSendBinding
import com.gouveia.studiesmain.utils.extensions.navTo

class GdcSendFragment : Fragment(R.layout.fragment_gdc_send) {

    companion object {
        const val REPLY = "REPLY"
    }

    private lateinit var binding: FragmentGdcSendBinding
    private var reply: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcSendBinding.bind(view)

        binding.btnSend.setOnClickListener {
            val args = Bundle()
            args.putString(GdcReplyFragment.SEND, binding.inputSend.text.toString())
            navTo(R.id.action_gdcSendFragment_to_gdcReplyFragment, args)
        }

        //Obtém a mensagem enviada pelo fragment ->
        arguments?.let {
            reply = it.getString(REPLY, null)
        }

        //Exibe a mensagem obtida ->
        reply?.let {
            binding.txtMessage.text = it
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}