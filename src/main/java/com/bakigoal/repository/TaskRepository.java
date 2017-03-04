package com.bakigoal.repository;

import com.bakigoal.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Task entity jpa repository
 * Created by ilmir on 03.03.17.
 */
public interface TaskRepository extends CrudRepository<Task, Long> {

  List<Task> findByMonth(String month);

}
