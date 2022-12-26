package com.gouveia.studiesmain.dca.dialogs.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentTicketSelectionBinding
import com.gouveia.studiesmain.utils.extensions.showToast
import com.gouveia.studiesmain.utils.extensions.viewBinding

// BOTTOM SHEET DIALOG: https://youtu.be/vku9pMNHT9o

class TicketSelectionFragment(private val showAddItem: Boolean) : BottomSheetDialogFragment(),
    OnItemClickListener<TicketElement> {

    private val binding by viewBinding(FragmentTicketSelectionBinding::bind)

    private val bottomSheetElements = ArrayList<TicketElement>()
    private lateinit var ticketAdapter: TicketSelectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mockSomeTickets() //Adiciona dados mockados.
        val recyclerView = binding.tickets
        ticketAdapter = TicketSelectionAdapter(bottomSheetElements, this)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ticketAdapter
    }

    override fun onItemClicked(item: TicketElement) {
        when (item.type) {
            TicketSelectionType.SELECT -> onSelect(item.title)
            TicketSelectionType.ENTER_MANUAL -> onEnterManual()
            TicketSelectionType.ADD -> onAdd()
        }
        dismiss()
    }

    private fun onSelect(ticketNumber: String) {
        showToast("Ticket Selecionado: $ticketNumber")
    }

    private fun onEnterManual() {
        showToast("Adicionar Manualmente")
    }

    private fun onAdd() {
        showToast("Adicionar")
    }

    private fun mockSomeTickets() {
        bottomSheetElements.add(
            TicketElement(
                R.drawable.ic_error,
                "Ticket Expirado",
                true,
                TicketSelectionType.SELECT
            )
        )
        bottomSheetElements.add(
            TicketElement(
                R.drawable.ic_success,
                "Ticket Valido",
                true,
                TicketSelectionType.SELECT
            )
        )
        bottomSheetElements.add(
            TicketElement(
                R.drawable.ic_success,
                "Exemplo de ticket ADD",
                false,
                TicketSelectionType.ADD
            )
        )
        bottomSheetElements.add(
            TicketElement(
                R.drawable.ic_success,
                "Exemplo de ticket ENTER_MANUAL",
                false,
                TicketSelectionType.ENTER_MANUAL
            )
        )
    }

}