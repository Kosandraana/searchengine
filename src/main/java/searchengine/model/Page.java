package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
//@Table(name = "page", indexes = {@Index(columnList = "path", name = "path_index")})
@Table(name = "page")
public class Page implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name = "site_id", nullable = false, insertable = false, updatable = false)
    private int siteId;
    @Column(name = "path", columnDefinition = "TEXT", nullable = false)
    private String path;
    @Column(name = "code", nullable = false)
    private int code;
    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false, length = 100000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Site site;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Page> page;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "page", fetch = FetchType.LAZY, orphanRemoval = true)
////    @Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
//    private List<Indexx> indexx;

    public Page(int id, int siteId, String path, int code, String content) {
        this.id = id;
        this.siteId = siteId;
        this.path = path;
        this.code = code;
        this.content = content;
    }
    public Page() {}

    @Override
    public String toString() {
        return "id: " + id + ", siteId: " + site.getId() + ", path: " + path;
    }
    public void setContent(String content) {
        this.content = content == null ? "" : content.replaceAll("\\s*\\n\\s*", "");
    }
}
