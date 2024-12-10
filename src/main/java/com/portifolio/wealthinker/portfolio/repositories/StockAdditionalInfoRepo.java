package com.portifolio.wealthinker.portfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.StockAdditionalInfo;

@Repository
public interface StockAdditionalInfoRepo extends JpaRepository<StockAdditionalInfo, String> {

}
