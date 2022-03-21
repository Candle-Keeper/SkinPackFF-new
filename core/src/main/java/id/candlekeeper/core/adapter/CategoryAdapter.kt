package id.candlekeeper.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.candlekeeper.core.databinding.ItemCategoryBinding
import id.candlekeeper.core.domain.model.dataContent.Category
import id.candlekeeper.core.utils.OnItemClicked
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.loadImageFull


class CategoryAdapter(
    private val onItemClicked: OnItemClicked,
    private val prefManager: PrefManager
) : RecyclerView.Adapter<CategoryAdapter.EventHolder>() {

    var list = ArrayList<Category>()

    fun addList(listData: List<Category>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            DiffCallback(
                list,
                listData
            )
        )
        list.clear()
        list.addAll(listData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(onItemClicked, binding)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bind(onItemClicked, list[position], prefManager)
    }

    override fun getItemCount(): Int = list.size

    class EventHolder(val onItemClicked: OnItemClicked, private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(onItemClicked: OnItemClicked, data: Category, prefManager: PrefManager) {
            with(binding) {
                tvTitle.text = data.name
                ivCategory.loadImageFull(prefManager, data.imageUrl.toString())
            }
            binding.root.setOnClickListener {
                onItemClicked.onEventClick(data)
            }
        }
    }
}
