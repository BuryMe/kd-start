package com.cc.kingdeestart.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author seven up
 * @date 2023年07月11日 2:38 PM
 */
@ConfigurationProperties(prefix = "kingdee")
//@Component
public class KingDeeProperty {

    private String acctId;
    private String appId;
    private String appSec;
    private String userName;
    private String password;
    private String lcId;
    private String connectTimeout;
    private String requestTimeout;
    private String stockTimeout;
    private String serverUrl;

    public KingDeeProperty() {
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSec() {
        return appSec;
    }

    public void setAppSec(String appSec) {
        this.appSec = appSec;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLcId() {
        return lcId;
    }

    public void setLcId(String lcId) {
        this.lcId = lcId;
    }

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(String requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getStockTimeout() {
        return stockTimeout;
    }

    public void setStockTimeout(String stockTimeout) {
        this.stockTimeout = stockTimeout;
    }

    public String getServerUrl() {
        return serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String toString() {
        return "KingDeeProperty{" +
                "acctId='" + acctId + '\'' +
                ", appId='" + appId + '\'' +
                ", appSec='" + appSec + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", lcId='" + lcId + '\'' +
                ", connectTimeout='" + connectTimeout + '\'' +
                ", requestTimeout='" + requestTimeout + '\'' +
                ", stockTimeout='" + stockTimeout + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                '}';
    }

    public String getLoginReqJson() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>(4);
        map.put("acctID", this.acctId);
        map.put("username", this.userName);
        map.put("password", this.password);
        map.put("lcid", this.lcId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }
}
