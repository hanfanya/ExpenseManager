package com.example.samuel.expensemanager.bean;

/**
 * Created by Allen_C on 2015/12/3.
 */
public class Rate {

    /**
     * status : ALREADY
     * scur : USD
     * tcur : EUR
     * ratenm : 美元/欧元
     * rate : 0.9433
     * update : 2015-12-03 10:01:20
     */

    public ResultEntity result;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        public String scur;
        public String tcur;
        public String ratenm;
        public String rate;
        public String update;

        public String getScur() {
            return scur;
        }

        public void setScur(String scur) {
            this.scur = scur;
        }

        public String getTcur() {
            return tcur;
        }

        public void setTcur(String tcur) {
            this.tcur = tcur;
        }

        public String getRatenm() {
            return ratenm;
        }

        public void setRatenm(String ratenm) {
            this.ratenm = ratenm;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }
    }
}
