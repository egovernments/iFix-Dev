package org.digit.program.repository;

import org.digit.program.models.ExchangeCode;
import org.digit.program.models.Program;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExchangeCodeRepository extends CrudRepository<ExchangeCode, String> {



}
