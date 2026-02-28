package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Author {
    private Long id;
    private Integer idBook;
    private String firstName;
    private String lastName;
}
