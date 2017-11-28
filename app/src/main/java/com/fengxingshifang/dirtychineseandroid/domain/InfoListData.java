package com.fengxingshifang.dirtychineseandroid.domain;

import java.util.List;

/**
 * Created by git on 2017/11/28.
 */

public class InfoListData {

    private List<InfosBean> infos;

    public List<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(List<InfosBean> infos) {
        this.infos = infos;
    }

    public static class InfosBean {
        /**
         * infoid : 0
         * infoorcomm : 1
         * title : title0
         * digest : digest0
         * content : digest0
         * publisher : publisher0
         * phoneidpublisher :
         * createtime : 2017-11-13 15:52:11
         * lastupdatetime : 2017-11-13 15:52:11
         * infostatus : 01
         * browsecount : 0
         * commcount : 0
         * remember_token : null
         * created_at : 2017-11-13 07:52:11
         * updated_at : 2017-11-13 07:52:11
         */

        private int infoid;
        private String infoorcomm;
        private String title;
        private String digest;
        private String content;
        private String publisher;
        private String phoneidpublisher;
        private String createtime;
        private String lastupdatetime;
        private String infostatus;
        private int browsecount;
        private int commcount;
        private Object remember_token;
        private String created_at;
        private String updated_at;

        public int getInfoid() {
            return infoid;
        }

        public void setInfoid(int infoid) {
            this.infoid = infoid;
        }

        public String getInfoorcomm() {
            return infoorcomm;
        }

        public void setInfoorcomm(String infoorcomm) {
            this.infoorcomm = infoorcomm;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getPhoneidpublisher() {
            return phoneidpublisher;
        }

        public void setPhoneidpublisher(String phoneidpublisher) {
            this.phoneidpublisher = phoneidpublisher;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getLastupdatetime() {
            return lastupdatetime;
        }

        public void setLastupdatetime(String lastupdatetime) {
            this.lastupdatetime = lastupdatetime;
        }

        public String getInfostatus() {
            return infostatus;
        }

        public void setInfostatus(String infostatus) {
            this.infostatus = infostatus;
        }

        public int getBrowsecount() {
            return browsecount;
        }

        public void setBrowsecount(int browsecount) {
            this.browsecount = browsecount;
        }

        public int getCommcount() {
            return commcount;
        }

        public void setCommcount(int commcount) {
            this.commcount = commcount;
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
