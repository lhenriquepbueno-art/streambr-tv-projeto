package com.streambr.tv.ui.home

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.streambr.tv.R
import com.streambr.tv.data.api.VodItem
import java.util.Locale

/**
 * Presenter para cards de VOD (filmes e séries).
 * Layout vertical em proporção de pôster (2:3).
 */
class VodCardPresenter : Presenter() {

    class ViewHolder(view: View) : Presenter.ViewHolder(view) {
        val poster: ImageView       = view.findViewById(R.id.iv_poster)
        val title: TextView         = view.findViewById(R.id.tv_title)
        val rating: TextView        = view.findViewById(R.id.tv_rating)
        val year: TextView          = view.findViewById(R.id.tv_year)
        val badgeQuality: TextView  = view.findViewById(R.id.badge_quality)
    }

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_vod, parent, false)

        view.isFocusable = true
        view.isFocusableInTouchMode = true

        view.setOnFocusChangeListener { v, hasFocus ->
            val scale = if (hasFocus) 1.06f else 1.0f
            val elevation = if (hasFocus) 10f else 2f

            ObjectAnimator.ofFloat(v, "scaleX", scale).apply { duration = 150; start() }
            ObjectAnimator.ofFloat(v, "scaleY", scale).apply { duration = 150; start() }
            ObjectAnimator.ofFloat(v, "translationZ", elevation).apply { duration = 150; start() }
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val vod = item as VodItem
        val vh  = viewHolder as ViewHolder

        vh.title.text  = vod.title
        vh.year.text   = vod.year.toString()
        vh.rating.text = String.format(Locale.getDefault(), "%.1f", vod.rating)
        vh.badgeQuality.text = vod.quality

        if (vod.posterUrl.isNotBlank()) {
            Glide.with(vh.poster.context)
                .load(vod.posterUrl)
                .transition(DrawableTransitionOptions.withCrossFade(150))
                .placeholder(R.drawable.ic_vod_placeholder)
                .error(R.drawable.ic_vod_placeholder)
                .into(vh.poster)
        } else {
            vh.poster.setImageResource(R.drawable.ic_vod_placeholder)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val vh = viewHolder as ViewHolder
        Glide.with(vh.poster.context).clear(vh.poster)
        vh.poster.setImageDrawable(null)
    }
}
