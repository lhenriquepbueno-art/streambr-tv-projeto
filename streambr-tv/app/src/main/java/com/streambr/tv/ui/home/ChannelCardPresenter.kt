package com.streambr.tv.ui.home

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.streambr.tv.R
import com.streambr.tv.data.api.Channel

/**
 * Presenter de canal ao vivo para o BrowseFragment.
 *
 * Um Presenter no Leanback é o equivalente a um ViewHolder + Adapter
 * do RecyclerView — ele infla o layout do card, preenche os dados
 * e gerencia o ciclo de vida (bind/unbind).
 *
 * O sistema de foco do D-pad é gerenciado automaticamente pelo
 * BrowseFragment — o Presenter só precisa definir como o card
 * responde visualmente ao ganhar/perder foco (escala + elevação).
 */
class ChannelCardPresenter : Presenter() {

    class ViewHolder(view: View) : Presenter.ViewHolder(view) {
        val logo: ImageView      = view.findViewById(R.id.iv_channel_logo)
        val name: TextView       = view.findViewById(R.id.tv_channel_name)
        val group: TextView      = view.findViewById(R.id.tv_channel_group)
        val badgeQuality: TextView  = view.findViewById(R.id.badge_quality)
        val badgeLive: LinearLayout = view.findViewById(R.id.badge_live)
    }

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_channel, parent, false)

        // Foco: escala o card para 1.07x (efeito Netflix/Globoplay)
        view.isFocusable = true
        view.isFocusableInTouchMode = true

        view.setOnFocusChangeListener { v, hasFocus ->
            val scale = if (hasFocus) 1.07f else 1.0f
            val elevation = if (hasFocus) 8f else 2f

            ObjectAnimator.ofFloat(v, "scaleX", scale).apply {
                duration = 150; start()
            }
            ObjectAnimator.ofFloat(v, "scaleY", scale).apply {
                duration = 150; start()
            }
            ObjectAnimator.ofFloat(v, "translationZ", elevation).apply {
                duration = 150; start()
            }
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val channel = item as Channel
        val vh = viewHolder as ViewHolder

        // Nome do canal
        vh.name.text = channel.name

        // Grupo + qualidade na linha secundária
        vh.group.text = channel.group

        // Badge de qualidade (HD, FHD, 4K)
        vh.badgeQuality.text = channel.quality
        vh.badgeQuality.visibility = View.VISIBLE

        // Badge LIVE — sempre visível em canais ao vivo
        vh.badgeLive.visibility = View.VISIBLE

        // Logo via Glide — fallback para inicial do nome se sem logo
        if (channel.logo.isNotBlank()) {
            Glide.with(vh.logo.context)
                .load(channel.logo)
                .transition(DrawableTransitionOptions.withCrossFade(150))
                .placeholder(R.drawable.ic_channel_placeholder)
                .error(R.drawable.ic_channel_placeholder)
                .into(vh.logo)
        } else {
            // Sem logo: exibe inicial do nome como placeholder
            vh.logo.setImageResource(R.drawable.ic_channel_placeholder)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val vh = viewHolder as ViewHolder
        // Cancela carregamento de imagem ao reciclar o card
        Glide.with(vh.logo.context).clear(vh.logo)
        vh.logo.setImageDrawable(null)
    }
}
