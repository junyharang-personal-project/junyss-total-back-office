package com.giggalpeople.backoffice.chatops.logback.appender.discord.object;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <h2><b>Discord JSON Object</b>/h2>
 */
public class JsonObject {

    /**
     * <b>Discord에 보낼 Message를 Map 형식으로 담아 JSON 형태로 보내기 위한 Map</b>
     */
    private final HashMap<String, Object> sendDiscordMessageMap = new HashMap<>();

    /**
     * <b>Discord에 보낼 Message를 JSON Type으로 보내기 위해 Map에 저장하기 위한 Method</b>
     * @param key Message Key
     * @param value Message Value
     */
    public void put(String key, Object value) {
        if (value != null) {
            sendDiscordMessageMap.put(key, value);
        }
    }

    /**
     * <b>Discord에 보낼 Log Back Message에 불 필요한 내용 등을 정리하기 위한 toString Method</b>
     * @return Discord에 보낼 Log Back Message에 불 필요한 내용 등을 정리한 문자열 값
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, Object>> entrySet = sendDiscordMessageMap.entrySet();
        builder.append("{");

        int iter = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            Object val = entry.getValue();
            builder.append(quote(entry.getKey())).append(":");

            if (val instanceof String) {
                builder.append(quote(String.valueOf(val)));
            } else if (val instanceof Integer) {
                builder.append(Integer.valueOf(String.valueOf(val)));
            } else if (val instanceof Boolean) {
                builder.append(val);
            } else if (val instanceof JsonObject) {
                builder.append(val);
            } else if (val.getClass().isArray()) {
                builder.append("[");
                int len = Array.getLength(val);
                for (int j = 0; j < len; j++) {
                    builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                }
                builder.append("]");
            }

            builder.append(++iter == entrySet.size() ? "}" : ",");
        }

        return builder.toString();
    }

    private String quote(String string) {
        return "\"" + string + "\"";
    }
}
