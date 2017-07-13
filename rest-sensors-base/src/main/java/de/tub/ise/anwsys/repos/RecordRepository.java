package de.tub.ise.anwsys.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.Record;

public interface RecordRepository extends CrudRepository<Record, String> {
	public List<Record> findAll();
}
