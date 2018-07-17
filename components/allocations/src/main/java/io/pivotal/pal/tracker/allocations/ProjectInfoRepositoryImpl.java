package io.pivotal.pal.tracker.allocations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProjectInfoRepositoryImpl implements ProjectInfoRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<Long, ProjectInfo>();

    @Override
    public ProjectInfo saveProject(long projectId, ProjectInfo projectInfo) {
        logger.info("> saveProject: "  + projectId);
        projectsCache.put(projectId, projectInfo);
        return projectInfo;
    }

    @Override
    public ProjectInfo getProjectInfo(long projectId) {
        logger.info("> getProjectInfo: "  + projectId);
        return projectsCache.get(projectId);
    }
}
