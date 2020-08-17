package com.piloterr.android.piloterr.models.responses;

import com.piloterr.android.piloterr.models.tasks.Task;
import com.piloterr.android.piloterr.models.user.User;

import java.util.List;

public class SkillResponse {

    public User user;
    public List<User> partyMembers;
    public Task task;

    public double expDiff;
    public double hpDiff;
    public double goldDiff;

}
