package org.bigmouth.ticket4j.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AccessSupport {

    protected final Ticket4jHttpClient ticket4jHttpClient;

    public AccessSupport(Ticket4jHttpClient ticket4jHttpClient) {
        Preconditions.checkNotNull(ticket4jHttpClient, "ticket4jHttpClient cannot be null.");
        this.ticket4jHttpClient = ticket4jHttpClient;
    }
    
    protected void addPair(HttpEntityEnclosingRequestBase requestBase, NameValuePair pair) throws UnsupportedEncodingException {
        addPair(requestBase, pair);
    }
    
    protected void addPair(HttpEntityEnclosingRequestBase requestBase, NameValuePair... pairs) throws UnsupportedEncodingException {
        List<NameValuePair> list = Lists.newArrayList(pairs);
        requestBase.setEntity(new UrlEncodedFormEntity(list, Ticket4jDefaults.DEFAULT_CHARSET));
    }
    
    protected <T> T fromJson(String json, Class<T> cls) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, cls);
    }
}
