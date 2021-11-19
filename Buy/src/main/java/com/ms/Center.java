package com.ms;

import com.ms.common.HttpClientHelp;
import com.ms.model.Account;

public class Center {
    public static Account account;
    private Login l;
    private Car c;

    public Center() {
        HttpClientHelp client=new HttpClientHelp();

        l=new Login(client);
        c=new Car(client);
    }

    public void start(String goodsUrl) {
        try {
            account = l.login();
            c.addCar(goodsUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
