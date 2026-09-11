package com.gtihub.Luythen.MP4_Backend.Player;

import java.time.Instant;

public class PlayerAnswer {

    private String name;
    private String answer;
    private Instant time;

    public PlayerAnswer (String name, String answer, Instant time) {
        this.name = name;
        this.answer = answer;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    public Instant getTime() {
        return time;
    }

}
