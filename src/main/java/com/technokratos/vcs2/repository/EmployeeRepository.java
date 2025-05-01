package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Employee;
import com.technokratos.vcs2.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("SELECT image FROM Employee employee " +
            "JOIN employee.image image WHERE employee.id = :employeeId AND image.id = :imageId")
    Optional<Image> getImageWhereEmployeeId(@Param("employeeId") UUID employeeId,
                                            @Param("imageId") UUID imageId);
}
