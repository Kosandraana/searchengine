package searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`index`")
public class Indexx implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lemma_id", nullable = false)
    private int lemmaId;
    @Column(name = "search_rank", nullable = false)
    private float rank;
    @ManyToOne(optional = false)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;
}