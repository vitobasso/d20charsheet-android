package com.vituel.dndplayer.model.character;

import com.vituel.dndplayer.model.Skill;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Created by Victor on 26/02/14.
 */
public class CharSkill implements Serializable, Comparable<CharSkill>{

    private Skill skill;
    private int score; //graduation for CharBase or final bonus for Character

    public CharSkill(Skill skill) {
        if(skill == null){
            throw new InvalidParameterException();
        }
        this.setSkill(skill);
    }

    public void addScore(int value){
        setScore(getScore() + value);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        return getSkill().toString() + " " + getScore();
    }

    @Override
    public int compareTo(CharSkill another) {
        return getSkill().getName().compareTo(another.getSkill().getName());
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
