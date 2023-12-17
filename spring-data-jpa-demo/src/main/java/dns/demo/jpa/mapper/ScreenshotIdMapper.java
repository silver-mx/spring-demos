package dns.demo.jpa.mapper;

import dns.demo.jpa.dto.ScreenshotIdDto;
import dns.demo.jpa.entities.ScreenshotId;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScreenshotIdMapper {
    ScreenshotId toEntity(ScreenshotIdDto screenshotIdDto);

    ScreenshotIdDto toDto(ScreenshotId screenshotId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ScreenshotId partialUpdate(ScreenshotIdDto screenshotIdDto, @MappingTarget ScreenshotId screenshotId);
}