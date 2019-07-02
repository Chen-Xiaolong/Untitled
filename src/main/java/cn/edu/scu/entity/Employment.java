package cn.edu.scu.entity;

public class Employment {
    private int employmentId;

    private int dutyId;

    private int skillId;

    public int getEmploymentId() {
        return employmentId;
    }

    public void setEmploymentId(int employmentId) {
        this.employmentId = employmentId;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    @Override
    public String toString() {
        return "Employment{" +
                "employmentId=" + employmentId +
                ", dutyId=" + dutyId +
                ", skillId=" + skillId +
                '}';
    }
}
