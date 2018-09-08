package org.kumoricon.staffserver.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StaffRepository extends JpaRepository< Staff, Long > {
    List<Staff> findByLastModifiedMSAfter(Long modified);
    Staff findByUuid(String uuid);
}
