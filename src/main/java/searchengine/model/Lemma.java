package searchengine.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
//@Data
@RequiredArgsConstructor
@Table(name = "lemma", indexes = @Index(columnList = "lemma, site_id", name = "KEY_lemma_lemma"))
public class Lemma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private int id;
    @Column(name = "lemma", nullable = false, columnDefinition = "varchar(255)")
    private String lemma;
    @Column(name = "frequency", nullable = false)
    private float frequency;
//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY,
//    targetEntity = Site.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_lemma_site"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Site site;

//    @Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
//    @OneToMany(mappedBy = "lemma", fetch = FetchType.LAZY)
//    private List<Indexx> indexx;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Lemma> lemmas;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "site_lemma", joinColumns = @JoinColumn(name = "site_id"), inverseJoinColumns = @JoinColumn(name = "lemma_id"))
//    private Set<Lemma> lemmaSet;

//    public Lemma(String lemma, int frequency, Site site) {
//        this.lemma = lemma;
//        this.frequency = frequency;
//        this.site = site;
//    }
//
//    @Override
//    public String toString() {
//        return "Lemma{" +
//                "id=" + id +
//                ", lemma='" + lemma + '\'' +
//                ", frequency=" + frequency +
//                ", siteId=" + site +
//                '}';
//    }
@Transient
private float weight;
    @Override
    public boolean equals(Object obj) {
        Lemma l = (Lemma) obj;
        return lemma.equals(l.lemma) && site == l.site;
    }
    @Override
    public int hashCode() {
        return lemma.hashCode() + site.hashCode();
    }
    @Override
    public String toString() {
        return "id: " + id + "; lemma: " + lemma + "; frequency: " + frequency + "; site: " + site.getName();
    }
}
