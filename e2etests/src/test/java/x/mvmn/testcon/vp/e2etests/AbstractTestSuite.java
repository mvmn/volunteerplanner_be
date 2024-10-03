package x.mvmn.testcon.vp.e2etests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import x.mvmn.testcon.vp.e2etests.util.UnsafeFunction;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Connection;
import java.sql.Statement;

@ContextConfiguration(classes = ContainersConfig.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public abstract class AbstractTestSuite {
    protected static TestEnvironment sharedTestEnv;
    @Autowired
    private TestEnvironment testEnv;

    private DataSource dbDataSource;

    protected HttpClient http = HttpClient.newHttpClient();

    @BeforeEach
    private void setupAbstractTestSuite() {
        if (sharedTestEnv == null) {
            sharedTestEnv = testEnv;
        } else {
            testEnv = sharedTestEnv;
        }
        if (!testEnv.isStarted()) {
            testEnv.start();
        }
    }

    @AfterAll
    public static void teardownAbstractTestSuite() {
        if (sharedTestEnv.isStarted()) {
            log.info("Shutting down environment");
            sharedTestEnv.stop();
        }
    }

    public HttpResponse<byte[]> callVP(String method, String path, byte[] body) throws URISyntaxException, IOException, InterruptedException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        var reqBuilder = HttpRequest.newBuilder().uri(new URI(testEnv.getVPUrl() + path));
		HttpRequest.BodyPublisher bodyPublisher;
        if (body != null) {
            reqBuilder.header("Content-Type", "application/json");
			bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(body);
        } else {
			bodyPublisher = HttpRequest.BodyPublishers.noBody();
		}
        switch (method) {
            case "GET":
                reqBuilder = reqBuilder.GET();
                break;
            case "POST":
                reqBuilder = reqBuilder.POST(bodyPublisher);
                break;
            case "PUT":
                reqBuilder = reqBuilder.PUT(bodyPublisher);
                break;
            case "DELETE":
                reqBuilder = reqBuilder.DELETE();
                break;
        }
        return http.send(reqBuilder.build(), BodyHandlers.ofByteArray());
    }

    public <T> T queryDb(UnsafeFunction<Statement, T> queryFunction) {
        if (dbDataSource == null) {
            dbDataSource = createDataSource();
        }
        try (Connection conn = dbDataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            return queryFunction.apply(stmt);
        } catch (Exception e) {
            throw new RuntimeException("DB call failure", e);
        }
    }

    protected DataSource createDataSource() {
        PGSimpleDataSource dataSourceBuilder = new PGSimpleDataSource() ;
        dataSourceBuilder.setURL(testEnv.getPGSqlJDBCUrl());
        dataSourceBuilder.setUser(TestEnvironment.PG_DB_USERNAME);
        dataSourceBuilder.setPassword(TestEnvironment.PG_DB_PASSWORD);
        return dataSourceBuilder;
    }
}
