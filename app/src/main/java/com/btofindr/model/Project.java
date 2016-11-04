package com.btofindr.model;

import java.util.ArrayList;

/**
 * This describes a HDB BTO Project that contains one or more Blocks.
 * A Project is a group of Blocks built together with the same concept.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class Project {

    private String projectId;
    private String projectName;
    private String townName;
    private String ballotDate;
    private String projectImage;
    private ArrayList<Block> blocks;

    public Project(){};

    public Project(String projectId, String projectName, String townName, String ballotDate, String projectImage, ArrayList<Block> blocks) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.townName = townName;
        this.ballotDate = ballotDate;
        this.projectImage = projectImage;
        this.blocks = blocks;
    }

    public Project(String projectId, String townName) {
        this.projectId = projectId;
        this.townName = townName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getBallotDate() {
        return ballotDate;
    }

    public void setBallotDate(String ballotDate) {
        this.ballotDate = ballotDate;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }
}