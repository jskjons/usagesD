package com.github.jeffskj;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.Map;

import com.google.gson.Gson;

public class UsageDHttpRequestHandler implements HttpHandler {
    private static final String JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String PATH = "/usages";
    private static final int DEFAULT_LIMIT = 500;
    private Gson gson = new Gson();
    private UsageStore usageStore;
    
    public UsageDHttpRequestHandler(UsageStore usageStore) {
        this.usageStore = usageStore;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.getRelativePath().equals(PATH)) {
            exchange.getResponseHeaders().put(HttpString.tryFromString(CONTENT_TYPE), JSON);
            String response = handleGetUsages(exchange.getQueryParameters());
            exchange.getResponseSender().send(response);
        }
    }

    private String handleGetUsages(Map<String, Deque<String>> params) {
        return gson.toJson(getUsages(params));
    }

    private Collection<Usage> getUsages(Map<String, Deque<String>> params) {
        if (params.containsKey("category") && params.containsKey("cutoff")) {
            Instant cutoff = new Date(Long.parseLong(params.get("cutoff").getFirst())).toInstant();
            return usageStore.findUsages(params.get("category").getFirst(), cutoff, limit(params));
        } else if (params.containsKey("category")) {
            return usageStore.findUsages(params.get("category").getFirst(), limit(params));
        } else {
            return usageStore.findUsages(limit(params));
        }
    }
    
    private int limit(Map<String, Deque<String>> params) {
        return params.containsKey("limit") ? Integer.parseInt(params.get("limit").getFirst()) : DEFAULT_LIMIT;
    }
}
