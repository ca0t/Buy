package com.ms.common;

import org.apache.http.Header;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.message.BasicHeader;

import java.util.Date;
import java.util.List;

public class CookieHelp extends DefaultCookieSpec {
    @Override
    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        String value = header.getValue();
        String prefix = "Expires=";
        if (value.contains(prefix)) {
            String expires = value.substring(value.indexOf(prefix) + prefix.length());
            expires = expires.substring(0, expires.indexOf(";"));
            String date = DateUtils.formatDate(new Date(Long.parseLong(expires) * 1000L),"EEE, dd-MMM-yy HH:mm:ss z");
            value = value.replaceAll(prefix + "\\d{10};", prefix + date + ";");
        }
        header = new BasicHeader(header.getName(), value);
        return super.parse(header, cookieOrigin);
    }
}
