package com.example.dodamdodam.Visit

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dodamdodam.R
import com.github.siyamed.shapeimageview.RoundedImageView
import org.greenrobot.eventbus.EventBus

open class AdminToParentDataAdapter(
    var context: Context,
    var mData: MutableList<ResultVisit>
) : RecyclerView.Adapter<AdminToParentDataAdapter.ViewHolder>(){

    //private var mData : MutableList<ResultNotice>
    private var filterList : MutableList<ResultVisit> = mData


    //Event Bus 클래스
    class ItemClickEvent     //this.id = id;
        (  //public long id;
        var position: Int
    )

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_to_parent, parent, false)
        return ViewHolder(convertView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.user_txt.text = mData[position].parentName + " 보호자님에게"
        viewHolder.date_txt.text = mData[position].writeDate
        viewHolder.contentTxt.text = mData[position].visitNotice

        val boardConfirm = mData[position].boardConfirm
        if(boardConfirm == "Y"){
            viewHolder.boardConfirm.text = "확인"
        }else{
            viewHolder.boardConfirm.text = "미확인"
        }

        val tempYn = mData[position].tempYn
        if(tempYn == "Y"){
            viewHolder.tempTxt.visibility = View.VISIBLE
        }else{
            viewHolder.tempTxt.visibility = View.GONE
        }

        /*if ("0" == mData[position].replyCnt) {
            viewHolder.replyCnt.text = ""
        } else {
            viewHolder.replyCnt.text = " [ " + mData[position].replyCnt.toString() + " ] "
        }*/

        //val path: String = mData[position].path
        val path = ""
        Glide.with(context)
            .load(R.drawable.logo_2)
            .override(600, 300)
            .fitCenter()
            .into(viewHolder.img_view)
        /* if (path != "") {
             Glide.with(context)
                 .load(path)
                 .override(600, 300)
                 .fitCenter()
                 .into(viewHolder.img_view)
         } else {
             Glide.with(context)
                 .load(R.drawable.logo)
                 .override(600, 300)
                 .fitCenter()
                 .into(viewHolder.img_view)
         }
 */

        viewHolder.itemView.setOnClickListener { // MainActivity에 onItemClick이 받음
            EventBus.getDefault().post(ItemClickEvent(position))
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_view: RoundedImageView

        var tempTxt : TextView
        var contentTxt: TextView
        var user_txt: TextView
        var date_txt: TextView
        var replyCnt : TextView
        var boardConfirm : TextView
        init {
            // 레이아웃 들고 오기
            val img_view = itemView.findViewById<RoundedImageView>(R.id.image_view)

            val tempTxt = itemView.findViewById<TextView>(R.id.tempTxt)
            val contentTxt = itemView.findViewById<TextView>(R.id.contentTxt)
            val user_txt = itemView.findViewById<TextView>(R.id.userTxt)
            val date_txt = itemView.findViewById<TextView>(R.id.dateTxt)
            val reply_txt = itemView.findViewById<TextView>(R.id.replyCnt)
            val boardConfirm = itemView.findViewById<TextView>(R.id.boardConfirm)
            this.img_view = img_view
            this.tempTxt = tempTxt
            this.contentTxt = contentTxt
            this.user_txt = user_txt
            this.date_txt = date_txt
            this.replyCnt = reply_txt
            this.boardConfirm = boardConfirm
        }
    }


}