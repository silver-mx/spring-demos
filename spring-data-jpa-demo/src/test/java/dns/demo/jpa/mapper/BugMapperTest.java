package dns.demo.jpa.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import dns.demo.jpa.dto.BugDto;
import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.repositories.BugRepository;
import dns.demo.jpa.repositories.JpaTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureJson
@DataJpaTest
@ContextConfiguration(classes = {JpaTestConfig.class, MapperTestConfig.class})
class BugMapperTest {
    public static final long BUG_ID = 1234L;

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private BugMapper bugMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void toEntity() throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("json/bug-dto.json")) {
            BugDto bugDto = objectMapper.readValue(inputStream, BugDto.class);
            Bug expectedBug = bugRepository.findById(BUG_ID).orElseThrow();
            Bug bug = bugMapper.toEntity(bugDto);
            assertEquals(expectedBug, bug);
        }
    }

    @Test
    void toDto() throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("json/bug-dto.json")) {
            BugDto expectedBugDto = objectMapper.readValue(inputStream, BugDto.class);
            BugDto bugDto = bugMapper.toDto(bugRepository.findById(BUG_ID).orElseThrow());
            assertEquals(expectedBugDto, bugDto);
        }
    }
}