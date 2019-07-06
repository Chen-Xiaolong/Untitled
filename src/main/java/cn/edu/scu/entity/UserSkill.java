package cn.edu.scu.entity;

public class UserSkill {

    private int userId;

    private int skillId;

    private int proficiency;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    @Override
    public String toString() {
        return "UserSkill{" +
                "userId=" + userId +
                ", skillId=" + skillId +
                ", proficiency=" + proficiency +
                '}';
    }
}
