package com.example.dodamdodam.Retrofit2;

public class ResultIntroduce {
    int seq;
    String result;
    String boardGubun;
    String boardContent;
    String insertId;
    String insertDate;
    String updateId;
    String updateDate;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBoardGubun() {
        return boardGubun;
    }

    public void setBoardGubun(String boardGubun) {
        this.boardGubun = boardGubun;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "ResultIntroduce{" +
                "seq=" + seq +
                ", result='" + result + '\'' +
                ", boardGubun='" + boardGubun + '\'' +
                ", boardContent='" + boardContent + '\'' +
                ", insertId='" + insertId + '\'' +
                ", insertDate='" + insertDate + '\'' +
                ", updateId='" + updateId + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
