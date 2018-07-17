package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String registrationServerEndpoint;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<Long, ProjectInfo>();

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations= restOperations;
        this.registrationServerEndpoint = registrationServerEndpoint;
    }


    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    //@Cacheable("projectsCache")
    public ProjectInfo getProject(long projectId) {
        logger.info("Get Project: " + projectId);
        ProjectInfo project = restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class);
        //projectsCache.put(projectId, project);
        //updateCache(projectId, project);
        projectInfoRepository.saveProject(projectId, project);

        return project;
    }

    //@Cacheable("projectsCache")
//    public ProjectInfo updateCache(long projectId, ProjectInfo project) {
//        logger.info("Adding to cache: " + projectId);
//        return project;
//    }

    //@Cacheable("projectsCache")
    public ProjectInfo getProjectFromCache(long projectId)  {
        logger.info("get project from cache:"  + projectId);
        //return projectsCache.get(projectId);
        return projectInfoRepository.getProjectInfo(projectId);
    }
}
