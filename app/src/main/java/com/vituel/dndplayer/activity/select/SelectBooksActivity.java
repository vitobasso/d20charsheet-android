package com.vituel.dndplayer.activity.select;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.common.collect.Lists;
import com.vituel.dndplayer.MemoryCache;
import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.BookDao;
import com.vituel.dndplayer.dao.CharBookDao;
import com.vituel.dndplayer.dao.EditionDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.util.gui.SimpleExpListAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.ActivityUtil.populateCheckBox;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 12/04/2015.
 */
public class SelectBooksActivity extends ExpandableListActivity {

    private CharBase base;
    private SortedSet<Edition> editions;
    private TreeMap<Edition, List<Book>> booksByEdition;
    private SortedSet<Book> checkedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbarTitle(this, BOLD_FONT, getTitle());

        base = (CharBase) getIntent().getSerializableExtra(EXTRA_EDITED);
        editions = loadSortedEditions();
        booksByEdition = mapBooksByEdition();
        checkedBooks = loadSortedCheckedBooks(base.getId());

        setListAdapter(new Adapter());
    }

    private TreeMap<Edition, List<Book>> mapBooksByEdition() {

        BookDao bookDao = new BookDao(this);
        List<Book> books = bookDao.listAll();
        bookDao.close();

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

    private SortedSet<Edition> loadSortedEditions() {
        EditionDao editionDao = new EditionDao(this);
        List<Edition> editions = editionDao.listAll();
        editionDao.close();
        return new TreeSet<>(editions);
    }

    private SortedSet<Book> loadSortedCheckedBooks(long charId) {
        CharBookDao charBookDao = new CharBookDao(this);
        List<Book> checkedBooks = charBookDao.findByParent(charId);
        charBookDao.close();
        return new TreeSet<>(checkedBooks);
    }

    private class Adapter extends SimpleExpListAdapter<Edition, Book> {

        public Adapter() {
            super(SelectBooksActivity.this, Lists.newArrayList(editions), booksByEdition, R.layout.explist_group_checkbox, R.layout.checkbox_row);
        }

        @Override
        public void populateGroup(final Edition edition, boolean isExpanded, View convertView) {
            String label = MessageFormat.format("{0} - {1}", edition.getSystem().toLabel(), edition.getName());
            populateTextView(convertView, R.id.text, label);

            CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    List<Book> books = booksByEdition.get(edition);
                    for (Book book : books) {
                        updateCheckedState(book, isChecked);
                    }
                    notifyDataSetChanged();
                }
            };
            populateCheckBox(convertView, R.id.checkbox, isAnyBookChecked(edition), listener);
        }

        @Override
        public void populateChild(final Book book, View convertView) {
            populateTextView(convertView, R.id.name, book.getName());

            CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateCheckedState(book, isChecked);
                    notifyDataSetChanged();
                }
            };
            populateCheckBox(convertView, R.id.checkbox, checkedBooks.contains(book), listener);

        }

    }

    private boolean isAnyBookChecked(Edition edition) {
        boolean isAnyChecked = false;
        List<Book> books = booksByEdition.get(edition);
        for (Book book : books) {
            isAnyChecked |= checkedBooks.contains(book);
        }
        return isAnyChecked;
    }

    private void updateCheckedState(Book book, boolean isChecked) {
        if (isChecked) {
            checkedBooks.add(book);
        } else {
            checkedBooks.remove(book);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                // persist
                CharBookDao charBookDao = new CharBookDao(this);
                charBookDao.saveOverwrite(base.getId(), checkedBooks);
                charBookDao.close();

                // update rulebooks in memory
                MemoryCache cache = (MemoryCache) getApplicationContext();
                cache.setActiveRulebooks(checkedBooks);

                finish();
                return true;

            default:
                return defaultOnOptionsItemSelected(item, this);
        }
    }

}
