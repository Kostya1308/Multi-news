package by.clevertec.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Access(AccessType.FIELD)
@OptimisticLocking(type = OptimisticLockType.VERSION)
@Table(name = "NEWS")
@NamedEntityGraph(name = "News.comments", attributeNodes = @NamedAttributeNode("comments"))
public class News extends DateTimeEntity implements Content, Serializable {
    @Version
    private static long serialVersionUID;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_generator")
    @SequenceGenerator(name = "news_generator", sequenceName = "news_seq", allocationSize = 1)
    @Column(name = "news_id")
    private long id;

    @Column
    private String title;

    @Lob
    @Column
    private String text;

    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private Set<Comment> comments;
}
