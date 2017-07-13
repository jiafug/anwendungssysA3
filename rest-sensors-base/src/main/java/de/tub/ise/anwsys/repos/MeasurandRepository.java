package de.tub.ise.anwsys.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.Measurand;
import de.tub.ise.anwsys.models.SmartMeter;

public interface MeasurandRepository extends CrudRepository<Measurand, String> {
	List<Measurand> findBySmartmeter(SmartMeter smartmeter);

	List<Measurand> findAll();

	Measurand findByMetricId(String metricId);
}
