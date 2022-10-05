package com.psmStudio.dodamdodam.Visit

import java.io.Serializable

class ResultVisit : Serializable {

    var seq: Int = 0
    var result: String? = null
    var userId: String? = null
    var userName: String? = null
    var userPhone: String? = null
    var babyName: String? = null
    var babyNum: String? = null
    var babyBirthDate: String? = null
    var babyBirthTime: String? = null
    var babyRelation: String? = null
    var parentId: String? = null
    var parentName: String? = null
    var visitNotice: String? = null
    var babyWeight: String? = null
    var babyLactation: String? = null
    var babyRequireItem: String? = null
    var babyEtc: String? = null
    var boardConfirm: String? = null
    var writeDate: String = ""
    var tempYn: String? = ""
    var reserveDate: String? = ""
    var replyCnt: String? = null
    var path1: String? = null
    var path2: String? = null
    var path3: String? = null
    var originalPath: String? = null
    var originalPath2: String? = null
    var originalPath3: String? = null
    var insertDate: String? = null

    constructor(
        userId: String?,
        userName: String?,
        userPhone: String?,
        babyName: String?,
        babyNum: String?,
        babyRelation: String?
    ) {
        this.userId = userId
        this.userName = userName
        this.userPhone = userPhone
        this.babyName = babyName
        this.babyNum = babyNum
        this.babyRelation = babyRelation
    }

    constructor(
        seq: Int,
        parentId: String?,
        parentName: String?,
        visitNotice: String?,
        babyWeight: String?,
        babyLactation: String?,
        babyRequireItem: String?,
        babyEtc: String?,
        writeDate: String,
        boardConfirm: String?,
        tempYn: String?,
        reserveDate: String?,
        path1: String?,
        path2: String?,
        path3: String?,
        originalPath: String?,
        originalPath2: String?,
        originalPath3: String?,
        //replyCnt: String?,
        insertDate: String?,
        babyName: String?
    ) {
        this.seq = seq
        this.parentId = parentId
        this.parentName = parentName
        this.visitNotice = visitNotice
        this.babyWeight = babyWeight
        this.babyLactation = babyLactation
        this.babyRequireItem = babyRequireItem
        this.babyEtc = babyEtc
        this.writeDate = writeDate
        this.boardConfirm = boardConfirm
        this.tempYn = tempYn
        this.reserveDate = reserveDate
        this.path1 = path1
        this.path2 = path2
        this.path3 = path3
        this.originalPath = originalPath
        this.originalPath2 = originalPath2
        this.originalPath3 = originalPath3
        //this.replyCnt = replyCnt;
        this.insertDate = insertDate
        this.babyName = babyName
    }

    override fun toString(): String {
        return "ResultVisit(seq=$seq, result=$result, userId=$userId, userName=$userName, userPhone=$userPhone, babyName=$babyName, babyNum=$babyNum, babyBirthDate=$babyBirthDate, babyBirthTime=$babyBirthTime, babyRelation=$babyRelation, parentId=$parentId, parentName=$parentName, visitNotice=$visitNotice, babyWeight=$babyWeight, babyLactation=$babyLactation, babyRequireItem=$babyRequireItem, babyEtc=$babyEtc, boardConfirm=$boardConfirm, writeDate='$writeDate', tempYn=$tempYn, reserveDate=$reserveDate, replyCnt=$replyCnt, path1=$path1, path2=$path2, path3=$path3, originalPath=$originalPath, originalPath2=$originalPath2, originalPath3=$originalPath3, insertDate=$insertDate)"
    }


}