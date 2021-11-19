package com.ms.common;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class QRHelp extends Frame {
    boolean loadFinished;
    Image myImage;
    String sMsg;

    public QRHelp(){
        super();
    }

    public void start(String url){
        sMsg="Loading...";
        setSize(500,500);
        setVisible(true);
        loadURLImage(url);
    }

    private void loadURLImage(String sUrl) {
        Toolkit toolkit;
        loadFinished = false;
        toolkit = Toolkit.getDefaultToolkit();
        try {
            URL url = new URL(sUrl);
            myImage = toolkit.getImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Graphics g = this.getGraphics();
        g.drawImage(myImage, 6, 36, this);
    }

    public void paint(Graphics g) {
        //判断是否加载完成
        if (loadFinished == true) {
            g.drawImage(myImage, 6, 36, this);
        } else {
            g.drawString(sMsg, 100, 100);
        }
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (infoflags == ALLBITS) {
            loadFinished = true;
            repaint();
            return false;
        } else {
            return true;
        }
    }
}
