package com.example.effectivemobiletesttask.adapter

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletesttask.R
import com.example.effectivemobiletesttask.databinding.TagItemBinding
import com.example.effectivemobiletesttask.utils.TagsEnum

class TagAdapter(
    private val resources: Resources,
    private val onTagSelected: (String, Boolean, Int) -> Unit,
    private val onTagsCleared: () -> Unit
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private var tagList = TagsEnum.values().map { it.russianTagName }

    private var selectedTagPosition = 0

    private val TAG = "TagAdapter"

    inner class TagViewHolder(private val tagItemBinding: TagItemBinding) :
        RecyclerView.ViewHolder(tagItemBinding.root) {
        fun onBind(position: Int) {
            tagItemBinding.apply {
                if (position == 4) {
                    val marginEndInDp = 10
                    val marginEndInPixels = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        marginEndInDp.toFloat(),
                        resources.displayMetrics
                    ).toInt()

                    val layoutParams = root.layoutParams as ViewGroup.MarginLayoutParams

                    layoutParams.marginEnd = marginEndInPixels

                    root.layoutParams = layoutParams
                }
                tagTitle.text = tagList[position]

                Log.d(TAG, "onBind: ${tagList[position]} position -> $position\t selectedTagPosition -> $selectedTagPosition")
                if (position == selectedTagPosition) {
                    Log.d(TAG, "onBind: ${tagList[position]} selected")
                    root.setCardBackgroundColor(resources.getColor(R.color.element_light_grey))
                    tagTitle.setTextColor(resources.getColor(R.color.white))
                    clear.visibility = View.VISIBLE
                } else {
                    Log.d(TAG, "onBind: ${tagList[position]} unselected")
                    root.setCardBackgroundColor(resources.getColor(R.color.background_light_grey))
                    tagTitle.setTextColor(resources.getColor(R.color.text_grey))
                    clear.visibility = View.GONE
                }

                root.setOnClickListener {
                    if (position != selectedTagPosition) {
                        val previousSelectedTagPosition = selectedTagPosition
                        selectedTagPosition = position
                        notifyItemChanged(previousSelectedTagPosition)
                        notifyItemChanged(selectedTagPosition)
                        onTagSelected.invoke(tagList[position], false, position)
                    } else {
                        onTagSelected.invoke(tagList[position], true, position)
                    }
                }

                clear.setOnClickListener {
                    selectedTagPosition = -1
                    notifyItemChanged(position)
                    onTagsCleared.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = tagList.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.onBind(position)
    }

}