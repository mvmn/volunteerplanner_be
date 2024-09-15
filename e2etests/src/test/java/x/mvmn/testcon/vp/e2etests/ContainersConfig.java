package x.mvmn.testcon.vp.e2etests;

import com.redis.testcontainers.RedisContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
@ComponentScan(basePackages = "x.mvmn.testcon.vp.e2etests")
public class ContainersConfig {
    @Value("${container.image.volplanner:volunteerplanner_be:latest}")
    private String volunteerPlannerImageName;
    @Value("${container.image.pgsql:postgres:16-alpine}")
    private String pgSqlImageName;
    @Value("${container.image.redis:redis:6.2.6}")
    private String redisImageName;
    private Network containerNetwork = Network.SHARED;

    @Bean
    @Scope("prototype")
    public TestEnvironment testEnvironment() {
        PostgreSQLContainer pgSql = configureContainer(new PostgreSQLContainer<>(DockerImageName.parse(pgSqlImageName)), "pgsql").withUsername("postgres").withPassword("postgres123").withDatabaseName("vp");
        RedisContainer redis = configureContainer(new RedisContainer(DockerImageName.parse(redisImageName)), "redis");
        GenericContainer vp = configureContainer(new GenericContainer<>(volunteerPlannerImageName), "vp");
        return TestEnvironment.builder().redis(redis).pgSqlServer(pgSql).volunteerPlanner(vp).network(containerNetwork).build();
    }

    protected <T extends GenericContainer> T configureContainer(T container, String networkAlias) {
        container.withNetwork(containerNetwork).withNetworkAliases(networkAlias).withAccessToHost(true);
        return container;
    }
}
