package io.pivotal.pal.tracker.allocations;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public interface ProjectInfoRepository {

    @CachePut(cacheNames = "projectsCache", key = "projectId")
    public ProjectInfo saveProject(long projectId, ProjectInfo projectInfo);

    @Cacheable(cacheNames="projectsCache")
    public ProjectInfo getProjectInfo(long projectId);

}
