package com.example.dodamdodam.Visit

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.R
import com.github.siyamed.shapeimageview.RoundedImageView
import org.greenrobot.eventbus.EventBus

open class VisitUserDataAdapter     //this.saveList = saveList;
    (//private final List<getServerImage> saveList;
    private var context: Context,
    private var mData: MutableList<ResultVisit>,

) :
    RecyclerView.Adapter<VisitUserDataAdapter.ViewHolder>(), Filterable {

    private var filterList : MutableList<ResultVisit> = mData

    //Event Bus 클래스
    class ItemClickEvent     //this.id = id;
        (  //public long id;
        var position: Int
    )

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visit_user, parent, false)
        return ViewHolder(convertView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if("" == mData[position].babyName){
            viewHolder.babyNameTxt?.text = "미등록"
        }else{
            viewHolder.babyNameTxt?.text = mData[position].babyName
        }

        viewHolder.parentNameTxt?.text = "(보호자 : " + mData[position].userName + ")"

        //TODO : 회원가입시 image 추가 해야함

        viewHolder.itemView.setOnClickListener { // MainActivity에 onItemClick이 받음
            EventBus.getDefault().post(ItemClickEvent(position))
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: RoundedImageView? = null
        var babyNameTxt: TextView? = null
        var parentNameTxt: TextView? = null

        init {
            // 레이아웃 들고 오기
            val imageView = itemView.findViewById<RoundedImageView>(R.id.image_view)
            val babyNameTxt = itemView.findViewById<TextView>(R.id.babyNameTxt)
            val parentNameTxt = itemView.findViewById<TextView>(R.id.parentNameTxt)
            this.imageView = imageView
            this.babyNameTxt = babyNameTxt
            this.parentNameTxt = parentNameTxt
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                mData = if (charString.isEmpty()) {
                    filterList
                } else {
                    val filteredList : MutableList<ResultVisit> = arrayListOf()
                    for (item in filterList) {
                        if(item.userName?.contains(charString.toLowerCase()) == true) {
                            filteredList.add(item)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                mData  = results.values as MutableList<ResultVisit>
                notifyDataSetChanged()
            }
        }
    }
}