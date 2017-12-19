package com.fengxingshifang.dirtychineseandroid.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by git on 2017/11/28.
 */

public class InfoListData {

    private List<Info> infos;

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    public static class Info implements Serializable {
        /**
         * infoid : 4
         * infoorcomm : 1
         * title : title4
         * digest : digest4
         * content : digest4
         * publisher : 15323858476
         * phoneid :
         * createtime : 2017-12-12 11:32:59
         * lastupdatetime : 2017-12-12 11:32:59
         * browsecount : 17
         * commentcount : 0
         * supportcount : 0
         * commentlevel : qrqrq
         * isup : qewq
         * infostatus : 01
         * created_at : 2017-12-12 03:32:59
         * updated_at : 2017-12-12 03:32:59
         */

        private String infoid;
        private String infoorcomm;
        private String title;
        private String digest;
        private String content;
        private String publisher;
        private String phoneid;
        private String createtime;
        private String lastupdatetime;
        private int browsecount;
        private int commentcount;
        private int supportcount;
        private String commentlevel;
        private String isup;
        private String infostatus;
        private String created_at;
        private String updated_at;

        public String getInfoid() {
            return infoid;
        }

        public void setInfoid(String infoid) {
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

        public String getPhoneid() {
            return phoneid;
        }

        public void setPhoneid(String phoneid) {
            this.phoneid = phoneid;
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

        public int getBrowsecount() {
            return browsecount;
        }

        public void setBrowsecount(int browsecount) {
            this.browsecount = browsecount;
        }

        public int getCommentcount() {
            return commentcount;
        }

        public void setCommentcount(int commentcount) {
            this.commentcount = commentcount;
        }

        public int getSupportcount() {
            return supportcount;
        }

        public void setSupportcount(int supportcount) {
            this.supportcount = supportcount;
        }

        public String getCommentlevel() {
            return commentlevel;
        }

        public void setCommentlevel(String commentlevel) {
            this.commentlevel = commentlevel;
        }

        public String getIsup() {
            return isup;
        }

        public void setIsup(String isup) {
            this.isup = isup;
        }

        public String getInfostatus() {
            return infostatus;
        }

        public void setInfostatus(String infostatus) {
            this.infostatus = infostatus;
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
