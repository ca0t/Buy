package com.ms;

public class Main {

    public static void main(String[] args) {
        String goodsUrl="https://www.microsoftstore.com.cn/accessories/ms-sculpt-ergonomic-mouse";
        System.out.println(goodsUrl);
	    Center c=new Center();
        c.start(goodsUrl);
    }
}
