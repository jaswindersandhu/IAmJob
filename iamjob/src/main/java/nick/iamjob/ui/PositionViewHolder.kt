package nick.iamjob.ui

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_position.*
import nick.data.model.Position
import nick.iamjob.R
import nick.iamjob.util.OnPositionActionListener
import nick.iamjob.util.PositionAction
import nick.ui.GlideApp

class PositionViewHolder(
    override val containerView: View,
    private val onPositionActionListener: OnPositionActionListener,
    private val showFadedViewedPosition: Boolean
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(position: Position) {
        title.text = position.title
        @SuppressLint("SetTextI18n")
        company.text = position.company
        location.text = position.location
        time_ago.text = DateUtils.getRelativeTimeSpanString(position.createdAt)

        GlideApp.with(containerView)
            .load(position.companyLogo)
            .placeholder(R.drawable.ic_jobs)
            .into(company_logo)

        if (showFadedViewedPosition) {
            setTextColors(position)
        }
        setSaveIcon(position)

        containerView.setOnClickListener {
            onPositionActionListener.onPositionAction(PositionAction.MoreDetails(position))
        }

        save_position.setOnClickListener {
            onPositionActionListener.onPositionAction(PositionAction.SaveOrUnsave(position))
            // To prevent click spamming
            save_position.isEnabled = false
        }
    }

    private fun setTextColors(position: Position) {
        val alpha = if (position.hasViewed) 0.4f else 1.0f

        @ColorInt val titleTextColor = ContextCompat.getColor(
            containerView.context,
            if (position.hasViewed) {
                R.color.darkGrey
            } else {
                android.R.color.black
            }
        )

        title.setTextColor(titleTextColor)
        title.alpha = alpha
        company.alpha = alpha
        location.alpha = alpha
        time_ago.alpha = alpha
    }

    private fun setSaveIcon(position: Position) {
        save_position.isEnabled = true
        save_position.setImageResource(
            if (position.isSaved) {
                R.drawable.ic_saved_filled
            } else {
                R.drawable.ic_saved
            }
        )
    }
}