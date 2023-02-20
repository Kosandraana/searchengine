package searchengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "site")
public class Site implements Serializable {
    public static final String INDEXING = "INDEXING";
    public static final String INDEXED = "INDEXED";
    public static final String FAILED = "FAILED";
    public static final String REMOVING = "REMOVING";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

//    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')", nullable = false)
    private StatusType status;

    @Column(name = "status_time", columnDefinition = "DATETIME", nullable = false)
    private long statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "url", nullable = false, columnDefinition = "varchar(255)")
    private String url;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Page> page;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Lemma> lemma;
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Set<Page> pages = new HashSet<>();
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Set<Lemma> lemmas = new HashSet<>();

    public Site(int id, StatusType status, long statusTime, String lastError, String url, String name) {
        this.id = id;
        this.status = status;
        this.statusTime = statusTime;
        this.lastError = lastError;
        this.url = url;
        this.name = name;
    }
}