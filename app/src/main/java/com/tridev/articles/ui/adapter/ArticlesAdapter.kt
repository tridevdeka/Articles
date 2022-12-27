package com.tridev.articles.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.makeramen.roundedimageview.RoundedImageView
import com.tridev.articles.R
import com.tridev.articles.databinding.ArticleItemContainerBinding
import com.tridev.articles.model.Article
import com.tridev.articles.utils.Utils.getFormattedDate


class ArticlesAdapter(
    private val context: Context,
    private val clickListener: ClickListener,
    private val deleteListener: DeleteListener? = null,
    private val isSavedArticle: Boolean? = null,
) :
    RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {
    class ArticlesViewHolder(val mBinding: ArticleItemContainerBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(ArticleItemContainerBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val article = differ.currentList[position]
        article.urlToImage?.let {
            holder.mBinding.ivArticle.loadImage(it)
        }
        holder.mBinding.tvArticleTitle.text = article.title
        holder.mBinding.tvArticleAuthor.text = article.author
        article.publishedAt?.let {
            holder.mBinding.tvArticlePublishedAt.text = getFormattedDate(it)
        }
        holder.mBinding.ivComment.setOnClickListener {
            Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show()
        }
        holder.mBinding.ivWhatsappShare.setOnClickListener {
            article.url?.let {
                shareArticle(url = it, implicitIntent = false)
            }
        }
        holder.mBinding.ivMenu.setOnClickListener {
            article.url?.let {
                showBottomSheet(url = it, article)
            }
        }
        holder.itemView.setOnClickListener {
            clickListener.onClick(article)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun showBottomSheet(url: String, article: Article) {
        val view = (LayoutInflater.from(context)).inflate(R.layout.bottom_sheet, null)
        BottomSheetDialog(context).apply {
            view.rootView.findViewById<TextView>(R.id.tv_share).setOnClickListener {
                shareArticle(url, implicitIntent = true)
                this.dismiss()
            }
            view.rootView.findViewById<TextView>(R.id.tv_report).setOnClickListener {
                Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show()
                this.dismiss()
            }

            if (isSavedArticle == true) {
                view.rootView.findViewById<TextView>(R.id.tv_delete).also {
                    it.visibility = VISIBLE
                    it.setOnClickListener {
                        deleteListener?.onDelete(article)
                        this.dismiss()
                    }
                }
            }

            setContentView(view)
        }.show()


    }

    private fun shareArticle(url: String, implicitIntent: Boolean) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            if (!implicitIntent)
                setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, url)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")))
        }
    }

    private fun RoundedImageView.loadImage(url: String) {
        val circularProgressDrawable = CircularProgressDrawable(this.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
        Glide.with(this).load(url).placeholder(circularProgressDrawable)
            .error(com.google.android.material.R.drawable.mtrl_ic_error).into(this)
    }

    interface ClickListener {
        fun onClick(article: Article)
    }

    interface DeleteListener {
        fun onDelete(article: Article)
    }

}