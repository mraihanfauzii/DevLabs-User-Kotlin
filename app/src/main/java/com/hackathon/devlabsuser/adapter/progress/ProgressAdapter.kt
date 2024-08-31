package com.hackathon.devlabsuser.adapter.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.devlabsuser.databinding.ItemArticleHorizontalBinding
import com.hackathon.devlabsuser.model.Project

class ProgressAdapter: RecyclerView.Adapter<ProgressAdapter.MenungguKonfirmasiViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var projectList = emptyList<Project>()

    inner class MenungguKonfirmasiViewHolder(private val binding: ItemArticleHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {
            binding.apply {
                tvArticleTitle.text = project.name
                tvStoriesDescription.text = project.status

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(project)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgressAdapter.MenungguKonfirmasiViewHolder {
        val data =
            ItemArticleHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenungguKonfirmasiViewHolder(data)
    }

    override fun getItemCount(): Int = projectList.size

    override fun onBindViewHolder(holder: ProgressAdapter.MenungguKonfirmasiViewHolder, position: Int) {
        val latestPortfolios = projectList[position]
        holder.bind(latestPortfolios)
    }

    fun getLatestPortfolios(newProject: List<Project>) {
        projectList = newProject
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(project: Project)
    }
}