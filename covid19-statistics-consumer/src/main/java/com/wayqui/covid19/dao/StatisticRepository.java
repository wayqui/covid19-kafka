package com.wayqui.covid19.dao;

import com.wayqui.covid19.entity.Covid19Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticRepository extends MongoRepository<Covid19Statistic, String> {
}
