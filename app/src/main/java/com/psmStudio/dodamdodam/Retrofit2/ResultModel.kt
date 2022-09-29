package com.psmStudio.dodamdodam.Retrofit2

class ResultModel {
    var seq = 0
    var result: String? = null
    var user_id: String? = null
    var title: String? = null
    var content: String? = null
    var date: String? = null
    var path: String? = null
    var lasted_Date: String? = null
    var nick_Name: String? = null
    var student_Id: String? = null
    var reply_count: String? = null
    override fun toString(): String {
        return "ResultModel{" +
                "seq=" + seq +
                ", result='" + result + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", path='" + path + '\'' +
                ", Lasted_Date='" + lasted_Date + '\'' +
                ", Nick_Name='" + nick_Name + '\'' +
                ", Student_Id='" + student_Id + '\'' +
                ", reply_count='" + reply_count + '\'' +
                '}'
    }
}