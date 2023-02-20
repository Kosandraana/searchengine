package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "lemma", indexes = @Index(columnList = "lemma, site_id"))
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private int id;
    @Column(name = "lemma", nullable = false, columnDefinition = "varchar(255)")
    private String lemma;
    @Column(name = "frequency", nullable = false)
    private int frequency;
//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY,
//    targetEntity = Site.class)
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Site site;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lemma", fetch = FetchType.LAZY, orphanRemoval = true)
////    @Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
//    private List<Indexx> indexx;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Lemma> lemmas;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "site_lemma", joinColumns = @JoinColumn(name = "site_id"), inverseJoinColumns = @JoinColumn(name = "lemma_id"))
//    private Set<Lemma> lemmaSet;

    public Lemma(String lemma, int frequency, Site site) {
        this.lemma = lemma;
        this.frequency = frequency;
        this.site = site;
    }

    @Override
    public String toString() {
        return "Lemma{" +
                "id=" + id +
                ", lemma='" + lemma + '\'' +
                ", frequency=" + frequency +
                ", siteId=" + site +
                '}';
    }
}
