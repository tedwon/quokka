package sara.won.quokka.models;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Objects;

/**
 * See https://quarkus.io/guides/hibernate-orm
 */
@Entity
@Table(name = "Memo")
@NamedQuery(name = "Memos.findAll", query = "SELECT m FROM Memo m ORDER BY m.pin DESC, m.date DESC", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@NamedQuery(name = "Memos.findByTag", query = "SELECT m FROM Memo m WHERE m.tags LIKE :tagSearch ORDER BY m.pin DESC, m.date DESC")
@NamedQuery(name = "Memos.findPinned", query = "SELECT m FROM Memo m WHERE m.pin = true ORDER BY m.date DESC", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@NamedQuery(name = "Memos.findByKeyword", query = "SELECT m FROM Memo m WHERE (upper(m.title) LIKE upper(:keyword)) OR (upper(m.body) LIKE upper(:keyword)) OR (upper(m.tags) LIKE upper(:keyword)) ORDER BY m.pin DESC, m.date DESC")
@NamedQuery(name = "Memos.findPinnedByKeyword", query = "SELECT m FROM Memo m WHERE m.pin = true AND ((upper(m.title) LIKE upper(:keyword)) OR (upper(m.body) LIKE upper(:keyword)) OR (upper(m.tags) LIKE upper(:keyword))) ORDER BY m.pin DESC, m.date DESC")
@Cacheable
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String title;

    @Column(length = 10485760, unique = true)
    private String body;

    @Column(length = 100)
    private String tags;
    private Boolean pin;
    private Date date;

    public Memo() {
    }

    public Memo(Integer id, String title, String body, String tags, Boolean pin, Date date) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.pin = pin;
        this.date = date;
    }

    public Memo(String title, String body, String tags, Boolean pin, Date date) {
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.pin = pin;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getPin() {
        return pin;
    }

    public void setPin(Boolean pin) {
        this.pin = pin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memo memo1 = (Memo) o;
        return Objects.equals(id, memo1.id) && Objects.equals(title, memo1.title) && Objects.equals(body, memo1.body) && Objects.equals(tags, memo1.tags) && Objects.equals(pin, memo1.pin) && Objects.equals(date, memo1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body, tags, pin, date);
    }

    public String exportToString() {
        final StringBuilder sb = new StringBuilder();
        if (title != null && !title.trim().equals("")) {
            sb.append("Title: " + title);
            sb.append("\n\n");
        }
        sb.append(body);
        if (tags != null && !tags.trim().equals("")) {
            sb.append("\n\n");
            sb.append("Tags: " + tags);
        }
        sb.append("\n\n----------------------------\n\n");

        return sb.toString();

    }

    public String exportToStringForEBook() {
        final StringBuilder sb = new StringBuilder();
        if (title != null && !title.trim().equals("")) {
            sb.append(title);
            sb.append("\n\n");
        }
        sb.append(body);
        return sb.toString();

    }

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", tags='" + tags + '\'' +
                ", pin=" + pin +
                ", date=" + date +
                '}';
    }
}
