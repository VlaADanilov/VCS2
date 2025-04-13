package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.Auto;
import com.technokratos.vcs2.repository.AutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AutoService {

    private final AutoRepository autoRepository;

    public Auto findAutoById(UUID id) {
        return autoRepository
                .findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Auto with id %s not found".formatted(id.toString()))
                );
    }

    //работает ли
    public List<Auto> findAllAutos(int pageNumber, int size) {
        return autoRepository.findAll(PageRequest.of(pageNumber,size)).toList();
    }
    
}
