package com.psmStudio.dodamdodam.Retrofit2;

public class ResultIntroduceImg {
    int seq;
    String result;
    String boardGubun;
    String path;
    String insertId;
    String insertDate;
    String updateId;
    String update_date;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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


    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    @Override
    public String toString() {
        return "ResultIntroduceImg{" +
                "seq=" + seq +
                ", result='" + result + '\'' +
                ", boardGubun='" + boardGubun + '\'' +
                ", path='" + path + '\'' +
                ", insertId='" + insertId + '\'' +
                ", insertDate='" + insertDate + '\'' +
                ", updateId='" + updateId + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
