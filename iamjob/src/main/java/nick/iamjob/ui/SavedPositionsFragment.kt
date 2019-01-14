package nick.iamjob.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_saved_positions.*
import nick.core.util.visibleOrGone
import nick.iamjob.PositionsViewModel
import nick.iamjob.R
import nick.iamjob.util.OnPositionClicked
import nick.iamjob.util.PositionAction
import nick.iamjob.util.PositionsQuery
import nick.ui.BaseFragment

class SavedPositionsFragment
    : BaseFragment()
    , OnPositionClicked {

    private val viewModel: PositionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }

    private val adapter = PositionsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_saved_positions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.positions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            progress_bar.visibleOrGone(false)
        })

        viewModel.queryPositions(PositionsQuery.SavedPositions)
    }

    override fun handleAction(positionAction: PositionAction) {
        with (positionAction) {
            when (this) {
                is PositionAction.SaveOrUnsave -> viewModel.saveOrUnsavePosition(position)
                is PositionAction.MoreDetails -> findNavController().navigate(
                    SavedPositionsFragmentDirections.toPosition(position)
                )
            }
        }
    }
}