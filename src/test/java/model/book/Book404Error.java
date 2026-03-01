package model.book;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Book404Error {
    private String type;
    private String title;
    private Integer status;
    private String traceId;
}
