package net.qmate.sender.repository;

import net.qmate.sender.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query(value = "SELECT * FROM qmate2.public.messages messages " +
            "WHERE status=:status AND create_date_time >= cast(:from as timestamp) " +
            "AND create_date_time<=cast(:currentTime as timestamp)", nativeQuery = true)
    List<MessageEntity> findByStatusAndTime(String status, LocalDateTime from, LocalDateTime currentTime);

    @Query(value = "SELECT f.value FROM qmate2.public.field f " +
            " WHERE f.entity_id =:ticketId AND f.name =:name AND f.entity_type='TICKET'", nativeQuery = true)
    String getValue(String name, Long ticketId);

    @Query(value = "SELECT t.key FROM qmate2.public.ticket t " +
            "                 WHERE id =:ticketId", nativeQuery = true)
    Long getTicketTitle(Long ticketId);

    @Query(value = "SELECT w.title FROM qmate2.public.workplace w" +
            " LEFT JOIN qmate2.public.ticket t on w.uuid = t.called_workplace_uuid " +
            " WHERE t.id =:ticketId ", nativeQuery = true)
    String getWorkPlaceTitle(Long ticketId);

}
