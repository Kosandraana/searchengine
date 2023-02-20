package searchengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "`index`")
public class Indexx {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
//    @ManyToOne( fetch = FetchType.LAZY)
//    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
@Column(name = "page_id")
private int pageId;
//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "lemma_id", referencedColumnName = "id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "lemma_id")
    private int lemmaId;
    @Column(name = "`rank`", nullable = false)
    private float rank;

    public Indexx(int pageId, int lemmaId, float rank) {
        this.pageId = pageId;
        this.lemmaId = lemmaId;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Indexx{" +
                "pageId=" + pageId +
                ", lemmaId=" + lemmaId +
                ", rank=" + rank +
                '}';
    }
}