package dns.demo.jpa.mapper;

import dns.demo.jpa.dto.ScreenshotDto;
import dns.demo.jpa.entities.Screenshot;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ScreenshotIdMapper.class})
public interface ScreenshotMapper {
    Screenshot toEntity(ScreenshotDto screenshotDto);

    ScreenshotDto toDto(Screenshot screenshot);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Screenshot partialUpdate(ScreenshotDto screenshotDto, @MappingTarget Screenshot screenshot);
}