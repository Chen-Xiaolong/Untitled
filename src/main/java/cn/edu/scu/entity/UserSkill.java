package cn.edu.scu.entity;

public class UserSkill {

    private int userId;

    private int skillId;

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

    @Override
    public String toString() {
        return "UserSkill{" +
                "userId=" + userId +
                ", skillId=" + skillId +
                '}';
    }
}
