package x.mvmn.testcon.vp.e2etests;

import lombok.NonNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class VPContainer extends GenericContainer<VPContainer> {

    public VPContainer(@NonNull final String dockerImageName) {
        super(dockerImageName);
    }


    public VPContainer(@NonNull final DockerImageName dockerImageName) {
        super(dockerImageName);
    }

    public VPContainer withPGSqlJDBCUrl(String jdbcUrl) {
        this.addEnv("SPRING_DATASOURCE_URL", jdbcUrl);
        return this;
    }
}
