package be.fenego.microspring.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import be.fenego.microspring.entities.StoreHours;

public interface StoreHoursRepository extends CrudRepository<StoreHours, Long> {
	@Query("SELECT id, storeuuid, date, openingtime, closingtime FROM openingtimes WHERE storeuuid IN (:storeUuids) AND date BETWEEN :startDate AND :endDate")
	List<StoreHours> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("storeUuids") List<String> storeUuids);
}
