package com.portifolio.wealthinker.portfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.PortfolioHistory;

@Repository
public interface PortfolioHistoryRepo extends JpaRepository<PortfolioHistory, String> {
    // @Query("SELECT new com.portfolio.wealthinker.portfolio.dto.PortfolioHistoryDTO (" +
    //        "FUNCTION('DATE', ph.snapshotDateTime), " +
    //        "SUM(ph.totalInvestment), " +
    //        "SUM(ph.totalValue)) " +
    //        "FROM PortfolioHistory ph " +
    //        "WHERE ph.portfolio.user.id = :userId " +
    //        "GROUP BY FUNCTION('DATE', ph.snapshotDateTime) " +
    //        "ORDER BY FUNCTION('DATE', ph.snapshotDateTime) ASC")
    // List<PortfolioHistoryDTO> findByUserId(@Param("userId") String userId);

    List<PortfolioHistory> findByPortfolio_User_IdOrderBySnapshotDateTimeAsc(String userId);

    @Query("SELECT ph FROM PortfolioHistory ph WHERE ph.portfolio.id = :portfolioId ORDER BY ph.snapshotDateTime ASC")
    List<PortfolioHistory> findByPortfolioIdOrderBySnapshotDateTimeAsc(@Param("portfolioId") String portfolioId);
}
