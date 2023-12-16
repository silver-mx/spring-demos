package dns.demo.jpa.mapper;

import dns.demo.jpa.dto.TagIdDto;
import dns.demo.jpa.entities.TagId;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagIdMapper {
    TagId toEntity(TagIdDto tagIdDto);

    TagIdDto toDto(TagId tagId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TagId partialUpdate(TagIdDto tagIdDto, @MappingTarget TagId tagId);
}