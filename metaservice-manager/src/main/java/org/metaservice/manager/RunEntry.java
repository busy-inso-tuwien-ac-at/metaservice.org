package org.metaservice.manager;

import java.io.File;
import java.util.Date;

public class RunEntry{
    public enum Status{
        RUNNING,
        FINISHED
    }
    private int mpid;
    private Process process;
    private File stdout;
    private File stderr;
    private int exitValue;
    private Date startTime;
    private String name;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMpid() {
        return mpid;
    }

    public void setMpid(int mpid) {
        this.mpid = mpid;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public File getStdout() {
        return stdout;
    }

    public void setStdout(File stdout) {
        this.stdout = stdout;
    }

    public File getStderr() {
        return stderr;
    }

    public void setStderr(File stderr) {
        this.stderr = stderr;
    }

    public int getExitValue() {
        return exitValue;
    }

    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}