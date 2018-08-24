package org.kumoricon.staffserver.heartbeat;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HeartbeatDao {
    public ConcurrentHashMap<String, HeartbeatRecord> records = new ConcurrentHashMap<>();


    void save(HeartbeatRecord heartbeatRecord) {
        if (heartbeatRecord.getClientID() == null) {
            return;
        }
        records.put(heartbeatRecord.getClientID(), heartbeatRecord);
    }

    List<HeartbeatRecord> getRecords() {
        return new ArrayList<>(records.values());
    }
}
