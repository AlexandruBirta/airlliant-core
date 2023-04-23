package ro.unibuc.fmi.airlliantcore.configuration;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
public class WebClientLogger {

    private final ObjectMapper objectMapper;

    public void logHttpBody(Object body, LogType logType) {

        try {

            if (log.isTraceEnabled()) {

                if (LogType.REQUEST.equals(logType)) {
                    log.trace("Request body: {}", objectMapper.writeValueAsString(body));
                } else if (LogType.RESPONSE.equals(logType)) {
                    log.trace("Response body: {}", objectMapper.writeValueAsString(body));
                }

            }

        } catch (JsonProcessingException e) {
            log.error("Error during body JSON serializing.", e);
        }
    }

    public enum LogType {

        REQUEST("request"),
        RESPONSE("response");

        private final String value;

        LogType(String value) {
            this.value = value;
        }

        @JsonCreator
        public static LogType fromValue(String text) {
            for (LogType b : LogType.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

    }

}