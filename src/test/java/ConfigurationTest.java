import org.junit.Before;
import org.junit.Test;

import orm.configuration.ProjectConfig;

import static org.junit.Assert.*;

public class ConfigurationTest {

    private ProjectConfig projectConfig;

    @Before
    public void before() {
        this.projectConfig = new ProjectConfig();
    }

    @Test
    public void canReadProjectConfigurationFile() {
        assertNotNull(projectConfig);
        assertNotNull(projectConfig.getDatabaseDriver());
        assertNotNull(projectConfig.getDatabasePassword());
        assertNotNull(projectConfig.getDatabaseUrl());
        assertNotNull(projectConfig.getDatabaseUsername());
        assertNotNull(projectConfig.getDatabaseName());
        assertEquals(true | false, projectConfig.isSqlTraceEnable());
    }
}
