package cn.edu.scu.entity;

public class Duty {
    private int dutyId;

    private String dutyName;

    private String description;

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Duty{" +
                "dutyId=" + dutyId +
                ", dutyName='" + dutyName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
