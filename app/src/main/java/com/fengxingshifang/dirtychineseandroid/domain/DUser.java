package com.fengxingshifang.dirtychineseandroid.domain;

/**
 * Created by git on 2017/12/26.
 */

public class DUser {
    /**
     * user : {"userid":6,"registerstyle":"01","phone":"18126004216","thirdpartyid":"","userpassword":"$2y$10$CpUVGNhJQe9gNoz4U0PcIOt/ZGC7Z.gKcsl/j/Gib95C2UZ12Fpu.","phoneidlogin":"","userstatus":"01","registertime":"2017-12-26 11:25:29","lastlogintime":"2017-12-26 11:25:29","logincount":0,"paystatus":"","remember_token":null,"created_at":"2017-12-26 03:25:29","updated_at":"2017-12-26 03:25:29"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * userid : 6
         * registerstyle : 01
         * phone : 18126004216
         * thirdpartyid :
         * userpassword : $2y$10$CpUVGNhJQe9gNoz4U0PcIOt/ZGC7Z.gKcsl/j/Gib95C2UZ12Fpu.
         * phoneidlogin :
         * userstatus : 01
         * registertime : 2017-12-26 11:25:29
         * lastlogintime : 2017-12-26 11:25:29
         * logincount : 0
         * paystatus :
         * remember_token : null
         * created_at : 2017-12-26 03:25:29
         * updated_at : 2017-12-26 03:25:29
         */

        private String userid;
        private String registerstyle;
        private String phone;
        private String thirdpartyid;
        private String userpassword;
        private String phoneidlogin;
        private String userstatus;
        private String registertime;
        private String lastlogintime;
        private int logincount;
        private String paystatus;
        private Object remember_token;
        private String created_at;
        private String updated_at;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getRegisterstyle() {
            return registerstyle;
        }

        public void setRegisterstyle(String registerstyle) {
            this.registerstyle = registerstyle;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getThirdpartyid() {
            return thirdpartyid;
        }

        public void setThirdpartyid(String thirdpartyid) {
            this.thirdpartyid = thirdpartyid;
        }

        public String getUserpassword() {
            return userpassword;
        }

        public void setUserpassword(String userpassword) {
            this.userpassword = userpassword;
        }

        public String getPhoneidlogin() {
            return phoneidlogin;
        }

        public void setPhoneidlogin(String phoneidlogin) {
            this.phoneidlogin = phoneidlogin;
        }

        public String getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(String userstatus) {
            this.userstatus = userstatus;
        }

        public String getRegistertime() {
            return registertime;
        }

        public void setRegistertime(String registertime) {
            this.registertime = registertime;
        }

        public String getLastlogintime() {
            return lastlogintime;
        }

        public void setLastlogintime(String lastlogintime) {
            this.lastlogintime = lastlogintime;
        }

        public int getLogincount() {
            return logincount;
        }

        public void setLogincount(int logincount) {
            this.logincount = logincount;
        }

        public String getPaystatus() {
            return paystatus;
        }

        public void setPaystatus(String paystatus) {
            this.paystatus = paystatus;
        }

        public Object getRemember_token() {
            return remember_token;
        }

        public void setRemember_token(Object remember_token) {
            this.remember_token = remember_token;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
