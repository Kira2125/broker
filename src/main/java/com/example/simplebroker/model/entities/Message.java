package com.example.simplebroker.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "MESSAGE")
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Message {

    private static final long serialVersionUID = 942121424332L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "content", nullable = false)
    @NotBlank
    private String content;

    @Column(name = "source_device_name", nullable = false)
    @NotBlank
//    @CreatedBy -- needs Authentication from SecurityContext
    private String sourceDeviceName;

    @Column(name = "created", nullable = false)
    @NotNull
    @CreatedDate
    private ZonedDateTime created;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MessageDevice> messageDevices;

    //@TODO add isDeleted flag, at this moment log could have reference on message that already deleted

    public Message() {}

}
