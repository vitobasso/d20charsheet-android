package com.vitobasso.d20charsheet.activity.rules;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.google.common.collect.Lists;
import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity;
import com.vitobasso.d20charsheet.dao.dependant.CharBookDao;
import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.dao.entity.EditionDao;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.rulebook.Book;
import com.vitobasso.d20charsheet.model.rulebook.Edition;
import com.vitobasso.d20charsheet.util.app.AppGlobals;
import com.vitobasso.d20charsheet.util.gui.SimpleExpListAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateCheckBox;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;
import static com.vitobasso.d20charsheet.util.font.FontUtil.BOLD_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 12/04/2015.
 */
public class SelectBooksActivity extends MainNavigationActvity {

    private CharBase charBase;
    private SortedSet<Edition> editions;
    private TreeMap<Edition, List<Book>> booksByEdition;
    private SortedSet<Book> checkedBooks;

    @Override
    protected int getContentLayout() {
        return android.R.layout.expandable_list_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbarTitle(this, BOLD_FONT, getTitle());

        AppGlobals cache = (AppGlobals) getApplicationContext();
        charBase = cache.getChar();
        editions = loadEditions();
        booksByEdition = mapBooksByEdition();
        checkedBooks = loadCheckedBooks();

        ExpandableListView list = findView(this, android.R.id.list);
        list.setAdapter(new Adapter());
    }

    @Override
    protected void navigateTo(NavigationItem nextActivity) {
        switch (nextActivity) {
            case SUMMARY:
                goToSummary();
                break;
            case EDIT:
                goToEditOrCreateChar();
                break;
            case OPEN:
                backToBase();
                goToOpenChar();
                break;
        }
    }

    private TreeMap<Edition, List<Book>> mapBooksByEdition() {
        List<Book> books = listAllBooks();
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

    private List<Book> listAllBooks() {
        BookDao bookDao = new BookDao(this);
        try {
            return bookDao.listAll();
        } finally {
            bookDao.close();
        }
    }

    private SortedSet<Edition> loadEditions() {
        EditionDao editionDao = new EditionDao(this);
        try {
            List<Edition> editions = editionDao.listAll();
            return new TreeSet<>(editions);
        } finally {
            editionDao.close();
        }
    }

    private SortedSet<Book> loadCheckedBooks() {
        Collection<Book> checkedBooks = cache.getChar().getActiveBooks();
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
        getMenuInflater().inflate(R.menu.save, menu);
        getMenuInflater().inflate(R.menu.download, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_save:
                onClickSave();
                return true;
            case R.id.action_download:
                onClickDownload();
                return true;
            default:
                return false;
        }
    }

    private void onClickSave() {
        if(cache.isCharOpened()) {
            saveCharBooks();
        }
        charBase.setActiveBooks(checkedBooks); //updating AppGlobals
        finish();
    }

    private void saveCharBooks() {
        CharBookDao charBookDao = new CharBookDao(this);
        try {
            charBookDao.saveOverwrite(charBase.getId(), checkedBooks);
        } finally {
            charBookDao.close();
        }
    }

    private void onClickDownload() {
        new RulesDownloadDialog(activity).showDialog();
    }

}
