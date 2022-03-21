package id.candlekeeper.core.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import id.candlekeeper.core.R
import id.candlekeeper.core.databinding.ItemEndorseListBinding
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.*


@SuppressLint("SetTextI18n")
class EndorseViewHolderList(
    private val context: Context,
    private val binding: ItemEndorseListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(onItemClicked: OnItemClicked, data: Endorse, prefManager: PrefManager) {
        with(binding) {

            if (!data.isEnable!!) {
                root.hide()
            } else {

                if (data.youtubeUrl != "") {
                    imgBackground.setOnClickListener {
                        intentToYoutube(context, data.youtubeUrl.toString())
                    }
                } else {
                    ivPlay.hide()
                }

                if (data.packageApp != "") {
                    btnInstall.setOnClickListener {
                        AppAnalytics.trackClick(AppAnalytics.Const.ENDORSE_HEROLIST_I)
                        intentToPlaystore(context, data.packageApp.toString())
                    }
                } else {
                    btnInstall.text = context.resources.getString(R.string.lihat)
                    btnInstall.setOnClickListener {
                        if (data.webUrl != "") {
                            intentToWeb(context, data.webUrl.toString())
                        } else {
                            onItemClicked.onEventClick(data)
                        }
                    }
                }

                root.setOnClickListener {
                    AppAnalytics.trackClick(AppAnalytics.Const.ENDORSE_HEROLIST_C)
                    if (data.webUrl != "") {
                        intentToWeb(context, data.webUrl.toString())
                    } else {
                        onItemClicked.onEventClick(data)
                    }
                }

                if (data.webUrl == "" && data.activityUrl == "") {
                    root.isEnabled = false
                }

                tvName.text = data.title
                tvDescription.text = data.description
                imgBackground.loadImageFull(prefManager, data.imageUrl.toString())
            }
        }
    }
}