package com.example.dodamdodam.Visit

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R

open class BoardReplyDataAdapter(
    var context: Context,
    var mData: MutableList<ResultReply>
) : RecyclerView.Adapter<BoardReplyDataAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reply, parent, false)
        return ViewHolder(convertView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name_txt.text = mData[position].userId
        viewHolder.date_txt.text = mData[position].insertDate
        viewHolder.reply_txt.text = mData[position].replyContent
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name_txt: TextView
        var date_txt : TextView
        var reply_txt : TextView
        init {
            // 레이아웃 들고 오기
            val nameTxt = itemView.findViewById<TextView>(R.id.nameTxt)
            val dateTxt = itemView.findViewById<TextView>(R.id.dateTxt)
            val replyTxt = itemView.findViewById<TextView>(R.id.replyTxt)

            this.name_txt = nameTxt
            this.date_txt = dateTxt
            this.reply_txt = replyTxt
        }
    }
}