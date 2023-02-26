package searchengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "`index`")
public class Indexx {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false,
            foreignKey=@ForeignKey(name = "FK_index_page"))
    @OnDelete(action = OnDeleteAction.CASCADE)
//@Column(name = "page_id")
private Page page;
//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id", nullable = false,
            foreignKey=@ForeignKey(name = "FK_index_lemma"))
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @Column(name = "lemma_id")
    private Lemma lemma;
    @Column(name = "`rank`", nullable = false)
    private float rank;
//
//    @Override
//    public String toString() {
//        return "Indexx{" +
//                "pageId=" + pageId +
//                ", lemmaId=" + lemmaId +
//                ", rank=" + rank +
//                '}';
//    }
public Indexx(Page page, Lemma lemma, float rank) {
    this.page = page;
    this.lemma = lemma;
    this.rank = rank;
}
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        Indexx i = (Indexx) obj;
        return id == i.id;
    }
    @Override
    public int hashCode() {
        return id + page.hashCode() + lemma.hashCode();
    }
    @Override
    public String toString() {
        return "id: " + id + "; page: " + page.getPath() + "; lemma: " + lemma.getLemma();
    }
}