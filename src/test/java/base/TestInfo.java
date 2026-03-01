package base;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestInfo {
    private String id;
    private String name;
    private String description;
    private String epic;
    private String issue;
    private String suite;
}
