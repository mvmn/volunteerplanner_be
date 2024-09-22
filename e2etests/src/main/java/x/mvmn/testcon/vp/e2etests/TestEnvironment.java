package x.mvmn.testcon.vp.e2etests;

import com.redis.testcontainers.RedisContainer;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.WaitingConsumer;
import org.testcontainers.lifecycle.Startables;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

@Builder
@Slf4j
public class TestEnvironment {
    public static final String PG_DB_USERNAME = "postgres";
    public static final String PG_DB_PASSWORD = "postgres123";
    public static final String PG_DB_NAME = "vp";
    public static final String REDIS_NETWORK_ALIAS = "redis";
    public static final String PG_SQL_NETWORK_ALIAS = "pgsql";


    @Getter
    protected Network network;

    @Getter
    protected PostgreSQLContainer pgSqlServer;

    @Getter
    protected RedisContainer redis;

    @Getter
    protected GenericContainer volunteerPlanner;

    @Getter
    protected volatile boolean started;

    @Getter
    @Builder.Default
    protected int pgDbPort = 5432;

    @Getter
    @Builder.Default
    protected int redisPort = 6379;

    @Getter
    @Builder.Default
    protected int vpPort = 8080;

    public synchronized void start() {
        if (started) {
            throw new IllegalStateException("Already started");
        }
        log.info("Starting test environment...");

        Startables.deepStart(redis.withExposedPorts(redisPort), pgSqlServer.withExposedPorts(pgDbPort)).join();
        volunteerPlanner.addExposedPort(vpPort);
        volunteerPlanner.addEnv("ENABLE_SMS", "false");
        volunteerPlanner.addEnv("CACHE_TYPE", "redis");
        volunteerPlanner.addEnv("SPRING_REDIS_HOST", REDIS_NETWORK_ALIAS);
        volunteerPlanner.addEnv("SPRING_REDIS_PORT", "" + redisPort);
        volunteerPlanner.addEnv("SPRING_DATASOURCE_URL",
                                "jdbc:postgresql://" + PG_SQL_NETWORK_ALIAS + ":" + pgDbPort + "/" + PG_DB_NAME);
        volunteerPlanner.addEnv("SPRING_DATASOURCE_USERNAME", PG_DB_USERNAME);
        volunteerPlanner.addEnv("SPRING_DATASOURCE_PASSWORD", PG_DB_PASSWORD);
        volunteerPlanner.addEnv("UI_ENABLE", "true");
        Startables.deepStart(volunteerPlanner).join();

        WaitingConsumer consumer = new WaitingConsumer();
        volunteerPlanner.followOutput(consumer, OutputFrame.OutputType.STDOUT);
        try {
            consumer.waitUntil(line -> line.getUtf8String().contains("Started Application in") && line
                    .getUtf8String()
                    .contains("JVM running for"), 1, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            throw new RuntimeException("Didn't get start message in logs from container " + volunteerPlanner.getDockerImageName(),
                                       e);
        }

        log.info("Test environment started.");
        started = true;
    }

    public synchronized void stop() {
        log.info("Stopping test environment...");
        if (!started) {
            throw new IllegalStateException("Already stopped");
        }
        Stream.of(pgSqlServer, redis, volunteerPlanner).forEach(GenericContainer::stop);
        started = false;
        log.info("Test environment stopped.");
    }

    public String getVPInternalUrl() {
        String host = volunteerPlanner.getNetworkAliases().get(0).toString();
        return String.format("http://%s:%s", host, vpPort);
    }

    public String getVPUrl() {
        String host = volunteerPlanner.getHost();
        int port = volunteerPlanner.getMappedPort(vpPort);
        return String.format("http://%s:%s", host, port);
    }

    public String getPGSqlJDBCUrl() {
        String host = pgSqlServer.getHost();
        int port = pgSqlServer.getMappedPort(pgDbPort);
        return String.format("jdbc:postgresql://%s:%s/" + PG_DB_NAME, host, port);
    }
}
