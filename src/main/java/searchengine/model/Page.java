package searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Index;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "page", indexes = {@Index(
        name = "path_id", columnList = "path_link", unique = true)})
public class Page implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "path_link", columnDefinition = "TEXT", nullable = false, unique = true)
    private String pathLink;
    private int code;
    @Column(columnDefinition = "MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci",
            nullable = false, length = 16777215)
    private String content;
    @ManyToOne(optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private Set<Indexx> indexes;
}