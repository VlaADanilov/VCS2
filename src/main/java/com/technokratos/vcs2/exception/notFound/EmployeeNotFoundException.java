package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException(UUID uuid) {
        super("Employee with id %s not found".formatted(uuid), "/employee");
    }
}
