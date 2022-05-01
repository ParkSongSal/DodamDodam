package com.example.dodamdodam.Visit

import java.io.Serializable

class ResultData(
    var result : String
) : Serializable {

    override fun toString(): String {
        return "ResultData(result='$result')"
    }
}