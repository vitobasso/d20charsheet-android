package com.vituel.dndplayer.activity;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.BookDao;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.util.gui.SingleColExpListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 12/04/2015.
 */
public class SelectBooksActivity extends ExpandableListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new Adapter(organizeBooks()));
    }

    private TreeMap<Edition, List<Book>> organizeBooks() {

        // load
        BookDao bookDao = new BookDao(this);
        List<Book> books = bookDao.listAll();
        bookDao.close();

//        CharBookDao charBookDao = new CharBookDao(this);
//        List<Book> enabledBooks = charBookDao.findByParent();
//        charBookDao.close();

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

    private class Adapter extends SingleColExpListAdapter<Edition, Book> {

        public Adapter(TreeMap<Edition, List<Book>> data) {
            super(SelectBooksActivity.this, data, R.layout.expandable_list_group, R.layout.simple_row);
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(groupLayout, null);
            }

            populateTextView(convertView, R.id.text, groups.get(groupPosition));

            setFontRecursively(activity, convertView, MAIN_FONT); //TODO bring to supperclass or to setText
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(childLayout, null);
            }

            Edition edition = groups.get(groupPosition);
            Book book = children.get(edition).get(childPosition);

            populateTextView(convertView, R.id.name, book.getName());

            setFontRecursively(activity, convertView, MAIN_FONT); //TODO bring to supperclass or to setText
            return convertView;
        }
    }

}
