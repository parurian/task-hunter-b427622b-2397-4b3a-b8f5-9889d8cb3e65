package dev.mher.taskhunter.services;

import dev.mher.taskhunter.models.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: MheR
 * Date: 12/3/19.
 * Time: 01:14 AM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.services.
 */

@Service
public class ProjectService {

    private final ProjectModel projectModel;

    @Autowired
    public ProjectService(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public ProjectModel save(int userId, String name) {
        this.projectModel.setName(name);
        try {
            return this.projectModel.save(userId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ProjectModel> list(int userId, int limit, int offset) {
        try {
            return this.projectModel.list(userId, limit, offset);
        } catch (Exception e) {
            return null;
        }
    }

    public ProjectModel retrieve(Integer projectId, Integer userId) {
        try {
            return this.projectModel.retrieve(projectId, userId);
        } catch (Exception e) {
            return null;
        }
    }

    public ProjectModel update(Integer projectId, Integer userId, String name) {
        try {
            ProjectModel projectModel = this.retrieve(projectId, userId);
            if (projectModel == null) {
                // invalid project
                return null;
            }
            if (!projectModel.isOwner()) {
                // user isn't owner
                return null;
            }
            projectModel.setName(name);
            if (!projectModel.update()) {
                // unknown error
                return null;
            }
            return projectModel;
        } catch (Exception e) {
            return null;
        }
    }


    public Boolean delete(Integer projectId, Integer userId) {
        try {
            ProjectModel projectModel = this.retrieve(projectId, userId);
            if (projectModel == null) {
                // invalid project
                return null;
            }
            if (!projectModel.isOwner()) {
                // user isn't owner
                return null;
            }
            if (!projectModel.delete()) {
                // unknown error
                return null;
            }
            return true;
        } catch (Exception e) {
            return null;
        }
    }


}
