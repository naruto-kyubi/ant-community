package org.naruto.framework.core.elasticsearch;

import org.elasticsearch.common.text.Text;

public class ElasticSearchUtils {
    public static String concat(Text[] texts) {
        StringBuilder sb = new StringBuilder();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }
}
