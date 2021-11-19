package com.ms;

import cn.hutool.json.JSONUtil;
import com.ms.common.HttpClientHelp;
import com.ms.model.HttpRes;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.xml.transform.Source;
import java.util.HashMap;
import java.util.Map;

public class Car {
    HttpClientHelp client;

    public Car(HttpClientHelp c){
        client=c;
    }

    public void addCar(String goodsUrl){
        Map<String, String> detail = getGoodsDetail(goodsUrl);
        add2Car(detail);
        checkCar();
    }

    private Map<String,String> getGoodsDetail(String goodsUrl){
        System.out.println("获取商品信息");
        Map<String,String> map=new HashMap<>(5);

        HttpRes httpRes = client.get(goodsUrl);
        Document document = Jsoup.parse(httpRes.Content);
        Elements element = document.getElementsByClass("product-add-form");
        Elements select = element.select("form#product_addtocart_form");
        String action = select.attr("action");
        Document parse = Jsoup.parse(element.toString());
        String product = parse.getElementsByAttributeValue("name", "product").attr("value");
        String item = parse.getElementsByAttributeValue("name", "item").attr("value");
        String formKey = parse.getElementsByAttributeValue("name", "form_key").attr("value");
        String sku = parse.getElementsByAttributeValue("name", "product_sku").attr("value");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(sku+" | "+product+" | "+item+" | "+formKey+" | "+action);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        map.put("product",product);
        map.put("product_sku",sku);
        map.put("item",item);
        map.put("form_key",formKey);
        map.put("action",action);
        return map;
    }

    private void add2Car( Map<String,String> map){
        System.out.println("正在加入购物车");
        HttpRes post = client.post(map.get("action"),null, map);
        System.out.println(post.Content);
    }

    private void checkCar(){
        String url="https://www.microsoftstore.com.cn/checkout/cart/";
        HttpRes httpRes = client.get(url);
        getCarItem(httpRes.Content);
    }

    @SneakyThrows
    private void getCarItem(String html){
        System.out.println("购物车列表");
        Document document = Jsoup.parse(html);
        Elements cart_item = document.getElementsByClass("cart item  ");
        if(cart_item.size()<=0){
            throw  new Exception("添加购物车失败");
        }

        for (org.jsoup.nodes.Element element : cart_item) {
            Elements details = element.getElementsByClass("product-item-details").select("a");
            Elements qty = element.getElementsByClass("field qty").select("input");
            Elements price = element.getElementsByClass("price");
            String itemHref = details.attr("href");
            String itemName = details.text();
            String count = qty.attr("value");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println(itemName + "  |  " + price.text() + "  |  " + count + "  |  " + itemHref);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
