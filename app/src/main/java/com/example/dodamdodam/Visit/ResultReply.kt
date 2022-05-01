package com.example.dodamdodam.Visit

import java.io.Serializable

class ResultReply(
    var seq: String,
    var userId: String,
    var replyContent: String,
    var insertDate: String,
    var parentName : String
) : Serializable {

    override fun toString(): String {
        return "ResultReply(seq=$seq, userId='$userId', replyContent='$replyContent', insertDate='$insertDate', parentName='$parentName')"
    }
}