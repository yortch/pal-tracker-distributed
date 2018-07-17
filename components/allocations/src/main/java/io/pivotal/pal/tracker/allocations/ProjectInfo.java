package io.pivotal.pal.tracker.allocations;

import java.io.Serializable;

public class ProjectInfo implements Serializable {

    public final boolean active;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    private long projectId;

    private ProjectInfo() {
        this(false);
    }

    public ProjectInfo(boolean active) {
        this.active = active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectInfo that = (ProjectInfo) o;

        return projectId == that.projectId;
    }

    @Override
    public int hashCode() {
        return ((int)projectId);
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
            "active=" + active +
            '}';
    }
}
