package com.wayqui.covid19.service;

import com.wayqui.covid19.conf.mapper.ObjectMapper;
import com.wayqui.covid19.dao.StatisticRepository;
import com.wayqui.covid19.dto.Covid19StatDto;
import com.wayqui.covid19.entity.Covid19Statistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticRepository statisticsDao;

    @Override
    public void insertStatistic(Covid19StatDto statisticDto) {
        log.info("insertStatistic");

        Covid19Statistic statistic = ObjectMapper.INSTANCE.dtoToEntity(statisticDto);

        Covid19Statistic result = statisticsDao.insert(statistic);

        log.info("insertStatistic: Inserted with id={}", result.getId());
    }
}
