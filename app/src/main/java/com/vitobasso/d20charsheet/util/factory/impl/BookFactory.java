package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.BookParser;
import com.vitobasso.d20charsheet.model.rulebook.Book;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class BookFactory extends EntityToolFactory<Book> {

    public BookFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Book> createDao() {
        return new BookDao(ctx);
    }

    @Override
    public AbstractEntityParser<Book> createParser(File csvFile) {
        return new BookParser(ctx, csvFile);
    }

    @Override
    protected String getCsvFileName() {
        return "books.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.rulebooks;
    }

}
