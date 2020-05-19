package com.wayqui.covid19.conf.mapper;

import com.wayqui.covid19.controller.model.Covid19StatRequest;
import com.wayqui.covid19.dto.Covid19StatDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ObjectMapper {

    ObjectMapper INSTANCE = Mappers.getMapper( ObjectMapper.class );

    Covid19StatDto requestToDto(Covid19StatRequest request);
}
