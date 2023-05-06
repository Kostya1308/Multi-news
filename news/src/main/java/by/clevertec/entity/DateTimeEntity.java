package by.clevertec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class DateTimeEntity {

    @CreationTimestamp
    @Column(name = "date_time_create", updatable = false)
    private LocalDateTime dateTimeCreate;

    @UpdateTimestamp
    @Column(name = "date_time_update", insertable = false)
    public LocalDateTime dateTimeUpdate;
}
