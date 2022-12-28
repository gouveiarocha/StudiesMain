package com.gouveia.studiesmain.gdc.user_interface.activities_intents

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcReplyBinding
import com.gouveia.studiesmain.utils.extensions.navTo

class GdcReplyFragment : Fragment(R.layout.fragment_gdc_reply) {

    companion object {
        const val SEND = "SEND"
    }

    private lateinit var binding: FragmentGdcReplyBinding
    private var send: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcReplyBinding.bind(view)

        binding.btnReply.setOnClickListener {
            val args = Bundle()
            args.putString(GdcSendFragment.REPLY, binding.inputReply.text.toString())
            navTo(R.id.action_gdcReplyFragment_to_gdcSendFragment, args)

            //Startando uma activity usando método proprio com ext ->
            //startActivity(NavigationTestActivity::class.java)

        }

        //Obtém a mensagem enviada pelo fragment ->
        arguments?.let {
            send = it.getString(SEND, null)
        }

        //Exibe a mensagem obtida ->
        send?.let {
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