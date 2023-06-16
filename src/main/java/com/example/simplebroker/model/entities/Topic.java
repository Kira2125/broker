package com.example.simplebroker.model.entities;

import com.example.simplebroker.enums.TopicType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "TOPIC",
        indexes = {
                @Index(name = "TOPIC_NAME_KEY", columnList = "name", unique = true)
        })
@Builder(toBuilder = true)
public class Topic {

    private static final long serialVersionUID = 212124334332L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "type", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @NotNull
    private TopicType type = TopicType.PUBLIC;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DEVICE_TOPIC",
            joinColumns = @JoinColumn(name = "TOPIC_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEVICE_ID"))
//    @BatchSize(size = 100)
    private List<Device> devices;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Message> messages;
}
