package com.jae464.presentation.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.presentation.databinding.ItemFriendListBinding

class FriendListAdapter : ListAdapter<com.jae464.domain.model.login.User, FriendListAdapter.ItemViewHolder>(diff) {

    inner class ItemViewHolder(
        private val binding: ItemFriendListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: com.jae464.domain.model.login.User) {
            binding.user = user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diff = object: DiffUtil.ItemCallback<com.jae464.domain.model.login.User>() {
            override fun areItemsTheSame(oldItem: com.jae464.domain.model.login.User, newItem: com.jae464.domain.model.login.User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: com.jae464.domain.model.login.User, newItem: com.jae464.domain.model.login.User): Boolean {
                return oldItem.uid == newItem.uid
            }

        }
    }

}