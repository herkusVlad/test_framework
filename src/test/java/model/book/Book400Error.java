package model.book;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Book400Error {
    private String type;
    private String title;
    private Integer status;
    private String traceId;
    private JsonNode errors;
}
