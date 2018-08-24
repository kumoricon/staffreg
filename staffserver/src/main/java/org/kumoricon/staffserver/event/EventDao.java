package org.kumoricon.staffserver.event;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class EventDao {
    private ConcurrentLinkedQueue<StaffEventRecord> records = new ConcurrentLinkedQueue<>();

    void save(StaffEventRecord staffEventRecord) {
        records.add(staffEventRecord);
    }

//    List<StaffEventRecord> getRecords() {
//        return new Arrays.asList(records.toArray());
//    }
}
