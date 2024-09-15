package x.mvmn.testcon.vp.e2etests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = ContainersConfig.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public abstract class AbstractTestSuite {
	protected static TestEnvironment sharedTestEnv;
	@Autowired
	private TestEnvironment testEnv;

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
}
