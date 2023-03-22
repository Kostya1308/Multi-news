package by.clevertec.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Access(AccessType.FIELD)
@OptimisticLocking(type = OptimisticLockType.VERSION)
@NamedEntityGraph(name = "Comment.news", attributeNodes = @NamedAttributeNode("news"))
public class Comment extends DateTimeEntity implements Reaction, Serializable {
    @Version
    private static long serialVersionUID;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_generator")
    @SequenceGenerator(name = "comment_generator", sequenceName = "comment_seq", allocationSize = 1)
    @Column(name = "comment_id")
    private long id;

    @Lob
    @Column
    private String text;

    @Column
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    @ToString.Exclude
    @JsonIgnore
    private News news;

}
