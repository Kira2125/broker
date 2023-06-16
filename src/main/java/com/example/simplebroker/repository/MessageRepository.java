package com.example.simplebroker.repository;

import com.example.simplebroker.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

//    @Modifying //not working
//    @Query("update Message message set message.status = :status where message.topic in (select topic from Topic topic where topic.devices in (select device from Device device where device.name = :deviceName))")
//    void modifyStatusToPendingByDeviceName(@Param("deviceName") String deviceName, @Param("status") Status status);

    @Modifying
    @Query(value = "update messagedevice set status = :status \n" +
            "            from topic t\n" +
            "            join device_topic dt on t.id = dt.topic_id\n" +
            "            join device d on dt.device_id = d.id\n" +
            "            where t.id=messagedevice.topic_id and d.name = :deviceName and messagedevice.status = 'PENDING'", nativeQuery = true)
    void modifyStatusByDeviceName(@Param("deviceName") String deviceName, @Param("status") String status);
}
