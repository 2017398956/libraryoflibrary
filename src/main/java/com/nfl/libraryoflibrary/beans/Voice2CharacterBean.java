package com.nfl.libraryoflibrary.beans;

import java.util.List;

/**
 * Created by fuli.niu on 2017/3/28.
 */

public class Voice2CharacterBean {


    /**
     * bg : 0
     * ed : 0
     * ls : true
     * sn : 1
     * ws : [{"bg":0,"cw":[{"sc":0,"w":"今天"}]},{"bg":0,"cw":[{"sc":0,"w":"的"}]},{"bg":0,"cw":[{"sc":0,"w":"天气"}]},{"bg":0,"cw":[{"sc":0,"w":"怎么样"}]},{"bg":0,"cw":[{"sc":0,"w":"。"}]}]
     */

    private int bg;// begin 开始
    private int ed;// end 结束
    private boolean ls;// last sentence 是否最后一句
    private int sn; // sentence 第几句
    private List<WsBean> ws;// words 词

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public boolean isLs() {
        return ls;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public List<WsBean> getWs() {
        return ws;
    }

    public void setWs(List<WsBean> ws) {
        this.ws = ws;
    }

    public static class WsBean {
        /**
         * bg : 0
         * cw : [{"sc":0,"w":"今天"}]
         */

        private int bg;
        private List<CwBean> cw;// chinese word 中文分词

        public int getBg() {
            return bg;
        }

        public void setBg(int bg) {
            this.bg = bg;
        }

        public List<CwBean> getCw() {
            return cw;
        }

        public void setCw(List<CwBean> cw) {
            this.cw = cw;
        }

        public static class CwBean {
            /**
             * sc : 0
             * w : 今天
             */

            private int sc;// score 分数
            private String w;// word 单字

            public int getSc() {
                return sc;
            }

            public void setSc(int sc) {
                this.sc = sc;
            }

            public String getW() {
                return w;
            }

            public void setW(String w) {
                this.w = w;
            }
        }
    }
}
