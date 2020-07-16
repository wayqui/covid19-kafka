package com.wayqui.covid19.mapper;

import com.wayqui.covid19.api.ApiStatisticResponse;
import com.wayqui.covid19.dto.Covid19StatDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BeanMapper {

    BeanMapper INSTANCE = Mappers.getMapper( BeanMapper.class );

    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    Covid19StatDto apiResponseToDto(ApiStatisticResponse request);

    List<Covid19StatDto> apiResponsesToDtos(List<ApiStatisticResponse> requests);
}
