package com.technokratos.vcs2.repository;

import com.technokratos.vcs2.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
