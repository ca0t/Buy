package com.ms.model;

import lombok.Data;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;

@Data
public class HttpRes {
    public String Content;
    public List<Cookie> cookieList;
    public Header[] headers;
}
