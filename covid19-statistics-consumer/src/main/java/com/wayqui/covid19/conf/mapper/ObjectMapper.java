package com.wayqui.covid19.conf.mapper;

import com.wayqui.covid19.dto.Covid19StatDto;
import com.wayqui.covid19.entity.Covid19Statistic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ObjectMapper {

    ObjectMapper INSTANCE = Mappers.getMapper( ObjectMapper.class );

    Covid19Statistic dtoToEntity(Covid19StatDto statisticDto);
}
