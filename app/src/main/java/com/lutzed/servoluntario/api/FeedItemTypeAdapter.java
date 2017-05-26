package com.lutzed.servoluntario.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luizfreitas on 17/07/2016.
 */

public class FeedItemTypeAdapter implements JsonDeserializer<FeedItem> {

    private static Map<String, Class<? extends FeedItem>> feedItemTypeRegistry;

    public static Map<String, Class<? extends FeedItem>> getPostingMuralTypeRegistry() {
        if (feedItemTypeRegistry == null) {
            feedItemTypeRegistry = new HashMap<>();
            feedItemTypeRegistry.put("Opportunity", Opportunity.class);
            feedItemTypeRegistry.put("Organization", Organization.class);
        }
        return feedItemTypeRegistry;
    }

    @Override
    public FeedItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject postObject = json.getAsJsonObject();
        JsonElement postingMuralTypeElement = postObject.get("feedable_type");
        Class<? extends FeedItem> feedItemInstanceClass = getPostingMuralTypeRegistry().get(postingMuralTypeElement.getAsString());

        return context.deserialize(json, feedItemInstanceClass);
    }
}
