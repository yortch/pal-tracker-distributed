package test.pivotal.pal.tracker.backlog.data;

import io.pivotal.pal.tracker.backlog.data.StoryDataGateway;
import io.pivotal.pal.tracker.backlog.data.StoryFields;
import io.pivotal.pal.tracker.backlog.data.StoryRecord;
import io.pivotal.pal.tracker.testsupport.TestScenarioSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static io.pivotal.pal.tracker.backlog.data.StoryFields.storyFieldsBuilder;
import static io.pivotal.pal.tracker.backlog.data.StoryRecord.storyRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class StoryDataGatewayTest {

    private TestScenarioSupport testScenarioSupport = new TestScenarioSupport("tracker_backlog_test");
    private JdbcTemplate template = testScenarioSupport.template;
    private StoryDataGateway gateway = new StoryDataGateway(testScenarioSupport.dataSource);

    @Before
    public void setUp() throws Exception {
        template.execute("DELETE FROM stories;");
    }

    @Test
    public void testCreate() {
        StoryFields fields = storyFieldsBuilder()
            .projectId(22L)
            .name("aStory")
            .build();


        StoryRecord created = gateway.create(fields);


        assertThat(created.id).isNotNull();
        assertThat(created.name).isEqualTo("aStory");
        assertThat(created.projectId).isEqualTo(22L);

        Map<String, Object> persisted = template.queryForMap("select * from stories where id = ?", created.id);

        assertThat(persisted.get("project_id")).isEqualTo(22L);
        assertThat(persisted.get("name")).isEqualTo("aStory");
    }

    @Test
    public void testFindBy() {
        template.execute("insert into stories (id, project_id, name) values (1346, 22, 'aStory')");


        List<StoryRecord> result = gateway.findAllByProjectId(22L);


        assertThat(result).containsExactly(
            storyRecordBuilder()
                .id(1346L)
                .projectId(22L)
                .name("aStory")
                .build()
        );
    }

    @Test
    public void testFindMostRecentStoryRecords() {
        int id = 1347;
        long projectId = 22L;
        StoryFields field1 = storyFieldsBuilder()
                .projectId(22L)
                .name("aStory")
                .build();
        gateway.create(field1);

        StoryFields field2 = storyFieldsBuilder()
                .projectId(22L)
                .name("aStory")
                .build();
        gateway.create(field2);

        StoryFields field3 = storyFieldsBuilder()
                .projectId(22L)
                .name("Story 3")
                .build();
        gateway.create(field3);

        StoryFields field4 = storyFieldsBuilder()
                .projectId(22L)
                .name("Story 4")
                .build();
        gateway.create(field4);

        List<StoryRecord> list = gateway.findMostRecentStoryRecords(projectId, 2);
        assertThat(list.size()).isEqualTo(2);

        assertThat(list.get(0).name).isEqualTo(field4.name);
        assertThat(list.get(1).name).isEqualTo(field3.name);


    }

}
