package com.hackathon.devlabsuser.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.devlabsuser.databinding.ItemVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class VideoAdapter(private val videoIds: List<String>, private val lifecycle: Lifecycle) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var youTubePlayer: YouTubePlayer
        private var isFullScreen = false

        fun bind(videoId: String) {
            binding.videoId = videoId
            val youtubePlayerView = binding.youtubePlayerView

            val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                    this@VideoViewHolder.youTubePlayer = youTubePlayer
                }
            }

            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build()

            youtubePlayerView.initialize(youtubePlayerListener, iFramePlayerOptions)

            youtubePlayerView.addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    isFullScreen = true
                    (itemView.rootView as? ViewGroup)?.let { rootView ->
                        rootView.removeAllViews()
                        rootView.addView(fullscreenView)
                        (fullscreenView.parent as? ViewGroup)?.removeView(fullscreenView)

                        itemView.context.getActivity()?.toggleFullscreen(true, rootView)
                    }
                }

                override fun onExitFullscreen() {
                    isFullScreen = false
                    (itemView.rootView as? ViewGroup)?.let { rootView ->
                        rootView.removeAllViews()
                        rootView.addView(binding.root)

                        itemView.context.getActivity()?.toggleFullscreen(false, rootView)
                    }
                }
            })
            lifecycle.addObserver(youtubePlayerView)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemVideoBinding.inflate(layoutInflater, parent, false)
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int = videoIds.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoIds[position])
    }
}

