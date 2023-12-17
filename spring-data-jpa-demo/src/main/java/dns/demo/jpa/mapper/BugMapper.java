package dns.demo.jpa.mapper;

import dns.demo.jpa.dto.BugDto;
import dns.demo.jpa.entities.Bug;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {TagMapper.class, AccountMapper.class, ScreenshotMapper.class})
public interface BugMapper {
    Bug toEntity(BugDto bugDto);

    @AfterMapping
    default void linkTags(@MappingTarget Bug bug) {
        bug.getTags().forEach(tag -> tag.setBug(bug));
    }

    @AfterMapping
    default void linkScreenshots(@MappingTarget Bug bug) {
        bug.getScreenshots().forEach(screenshot -> screenshot.setBug(bug));
    }

    BugDto toDto(Bug bug);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Bug partialUpdate(BugDto bugDto, @MappingTarget Bug bug);
}