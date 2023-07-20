package com.cc.kingdeestart.core;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author seven up
 * @date 2023年07月11日 10:07 AM
 */
public class RequestResolver {

    private final static Log log = LogFactory.getLog(RequestResolver.class);

    private final static String HEADER = "kdservice-sessionId";

    private final static String SESSION_KEY = "KDSVCSessionId";

    private final static Cache<String, String> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1024)
            .build();

    private final KingDeeProperty kingDeeProperty;

    public RequestResolver(KingDeeProperty kingDeeProperty) {
        this.kingDeeProperty = kingDeeProperty;
    }

    private String getSessionId() {
        String sessionId = CACHE.getIfPresent(SESSION_KEY);
        if (StringUtils.hasText(sessionId)) {
            return sessionId;
        }

        String loginResult;
        try {
            String serverUrl = this.kingDeeProperty.getServerUrl();
            String loginReqJson = this.kingDeeProperty.getLoginReqJson();
            loginResult = http(serverUrl, loginReqJson, null);
            JsonParser jsonParser = JsonParserFactory.getJsonParser();
            Map<String, Object> stringObjectMap = jsonParser.parseMap(loginResult);
            Object sessionIdObj = stringObjectMap.get("KDSVCSessionId");
            Assert.notNull(sessionIdObj, "KDSVCSessionId is null");
            sessionId = (String) sessionIdObj;
            CACHE.put(SESSION_KEY, sessionId);
        } catch (Exception e) {
            log.error("exception in getting sessionId. error:" + e.getMessage());
            throw new RuntimeException("exception in getting kingDee sessionId");
        }

        return sessionId;
    }


    private static final Map<String, String> METHOD_AND_URI = new HashMap<>();

    static {
        METHOD_AND_URI.put("login", "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc");
        METHOD_AND_URI.put("saveGroup", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.GroupSave.common.kdsvc");
        METHOD_AND_URI.put("deleteGroup", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.GroupDelete.common.kdsvc");
        METHOD_AND_URI.put("save", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc");
        METHOD_AND_URI.put("delete", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Delete.common.kdsvc");
        METHOD_AND_URI.put("unAudit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc");
        METHOD_AND_URI.put("submit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc");
        METHOD_AND_URI.put("audit", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc");
        METHOD_AND_URI.put("queryBill", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc");
        METHOD_AND_URI.put("queryGroupInfo", "Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.QueryGroupInfo.common.kdsvc");
    }

    public String invoke(String api, Object reqJson) {
        Assert.notNull(reqJson, "kingDee api reqJson is null");
        String uri = METHOD_AND_URI.get(api);
        Assert.hasText(uri, "kingDee api type is error." + api);
        String serverUrl = this.kingDeeProperty.getServerUrl();
        String sessionId = getSessionId();
        return http(serverUrl + uri, reqJson.toString(), sessionId);
    }


    private String http(String url, String reqJson, String sessionId) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, reqJson);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (StringUtils.hasText(sessionId)) {
            builder.header(HEADER, sessionId);
        }
        builder.post(body);
        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.error("exception in http request", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
