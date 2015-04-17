package com.vituel.dndplayer.activity;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;

import com.google.common.collect.Ordering;
import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.BookDao;
import com.vituel.dndplayer.dao.EditionDao;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.util.gui.SimpleExpListAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;

/**
 * Created by Victor on 12/04/2015.
 */
public class SelectBooksActivity extends ExpandableListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new Adapter(getSortedEditions(), getMappedBooks()));
    }

    private TreeMap<Edition, List<Book>> getMappedBooks() {

        // load
        BookDao bookDao = new BookDao(this);
        List<Book> books = bookDao.listAll();
        bookDao.close();

        // organize
        TreeMap<Edition, List<Book>> map = new TreeMap<>();
        for (Book book : books) {
            List<Book> list = map.get(book.getEdition());
            if (list == null) {
                list = new ArrayList<>();
                map.put(book.getEdition(), list);
            }
            list.add(book);
        }

        return map;
    }

    private List<Edition> getSortedEditions() {

        // load
        EditionDao editionDao = new EditionDao(this);
        List<Edition> editions = editionDao.listAll();
        editionDao.close();

        // sort
        return Ordering.natural().sortedCopy(editions);
    }

    private class Adapter extends SimpleExpListAdapter<Edition, Book> {

        public Adapter(List<Edition> editions, TreeMap<Edition, List<Book>> bookMap) {
            super(SelectBooksActivity.this, editions, bookMap, R.layout.expandable_list_group, R.layout.simple_row);
        }

        @Override
        public void populateGroup(Edition edition, boolean isExpanded, View convertView) {
            String label = MessageFormat.format("{0} - {1}", edition.getSystem().toLabel(), edition.getName());
            populateTextView(convertView, R.id.text, label);
        }

        @Override
        public void populateChild(Book book, View convertView) {
            populateTextView(convertView, R.id.name, book.getName());
        }

    }

}
