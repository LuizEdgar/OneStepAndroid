package com.lutzed.servoluntario.models;

import com.lutzed.servoluntario.R;

/**
 * Created by luizfreitas on 26/05/2017.
 */

public interface FeedItem {

    public Type getFeedableTypeAsEnum();

    enum Type {
        ALL("All", 0),
        OPPORTUNITY("Opportunity", R.id.feed_item_adapter_opportunity_id),
        ORGANIZATION("Organization", R.id.feed_item_adapter_organization_id);

        private final String value;
        private int feedItemAdapterType;

        Type(String value, int feedItemAdapterType) {
            this.value = value;
            this.feedItemAdapterType = feedItemAdapterType;
        }

        public String getValue() {
            return value;
        }

        public int getFeedItemAdapterType() {
            return feedItemAdapterType;
        }

        public static Type fromString(String value) {
            switch (value) {
                case "Opportunity":
                    return OPPORTUNITY;
                case "Organization":
                    return ORGANIZATION;
                default:
                    return ALL;
            }
        }
    }

}
