package ru.practicum.explorewithme.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность подборки событий
 */
@NamedEntityGraph(
        name = "compilations-events-graph",
        attributeNodes = @NamedAttributeNode(value = "events", subgraph = "events-subgraph"),
        subgraphs = {
                @NamedSubgraph(
                        name = "events-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("category"),
                                @NamedAttributeNode("initiator")})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Version
    @Column(name = "version")
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "pinned")
    private Boolean pinned;

    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> events = new ArrayList<>();

    public Compilation(Long id, String title, Boolean pinned, List<Event> events) {
        this.id = id;
        this.title = title;
        this.pinned = pinned;
        this.events = events;
    }
}
