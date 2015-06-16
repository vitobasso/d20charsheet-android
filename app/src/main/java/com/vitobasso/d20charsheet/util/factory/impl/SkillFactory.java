package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.SkillDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.SkillParser;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class SkillFactory extends EntityToolFactory<Skill> {

    public SkillFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Skill> createDao() {
        return new SkillDao(ctx);
    }

    @Override
    public AbstractEntityParser<Skill> createParser(File csvFile) {
        return new SkillParser(ctx, csvFile);
    }

    @Override
    protected String getCsvFileName() {
        return "skills.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.skills;
    }

}
