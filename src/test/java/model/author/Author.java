package model.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Author {
    private Long id;
    private Integer idBook;
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id)
                && Objects.equals(idBook, author.idBook)
                && Objects.equals(firstName, author.firstName)
                && Objects.equals(lastName, author.lastName);
    }
}
