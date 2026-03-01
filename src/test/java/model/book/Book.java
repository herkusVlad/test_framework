package model.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Book {
    private Long id;
    private String title;
    private String description;
    private Integer pageCount;
    private String excerpt;
    private String publishDate;

    public boolean isAllFieldsNotNull(){
        return !Objects.isNull(this.id)
                && !Objects.isNull(this.title)
                && !Objects.isNull(this.description)
                && !Objects.isNull(this.pageCount)
                && !Objects.isNull(this.excerpt)
                && !Objects.isNull(this.publishDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id)
                && Objects.equals(title, book.title)
                && Objects.equals(description, book.description)
                && Objects.equals(pageCount, book.pageCount)
                && Objects.equals(excerpt, book.excerpt)
                && Objects.equals(publishDate, book.publishDate);
    }
}
