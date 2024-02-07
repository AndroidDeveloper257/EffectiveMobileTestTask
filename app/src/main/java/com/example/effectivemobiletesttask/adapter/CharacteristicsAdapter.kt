package com.example.effectivemobiletesttask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.effectivemobiletesttask.databinding.CharacteristicsItemBinding
import com.example.effectivemobiletesttask.models.Info

class CharacteristicsAdapter(
    private val characteristicsList: List<Info>
) : RecyclerView.Adapter<CharacteristicsAdapter.CharacteristicsViewHolder>() {

    inner class CharacteristicsViewHolder(private val characteristicsItemBinding: CharacteristicsItemBinding) :
        RecyclerView.ViewHolder(characteristicsItemBinding.root) {
        fun onBind(info: Info) {
            characteristicsItemBinding.apply {
                titleTv.text = info.title
                valueTv.text = info.value
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacteristicsViewHolder {
        return CharacteristicsViewHolder(
            CharacteristicsItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = characteristicsList.size

    override fun onBindViewHolder(holder: CharacteristicsViewHolder, position: Int) {
        holder.onBind(characteristicsList[position])
    }

}