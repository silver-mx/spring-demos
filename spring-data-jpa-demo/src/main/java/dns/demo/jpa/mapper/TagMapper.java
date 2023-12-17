package dns.demo.jpa.mapper;

import dns.demo.jpa.dto.TagDto;
import dns.demo.jpa.entities.Tag;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BugMapper.class})
public interface TagMapper {
    Tag toEntity(TagDto tagDto);

    default TagDto toDto(Tag tag) {
        return new TagDto(tag.getId().getTag());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(TagDto tagDto, @MappingTarget Tag tag);
}