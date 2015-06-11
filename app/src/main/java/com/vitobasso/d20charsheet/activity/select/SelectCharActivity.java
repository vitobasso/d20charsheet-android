package com.vitobasso.d20charsheet.activity.select;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity;
import com.vitobasso.d20charsheet.dao.entity.CharDao;
import com.vitobasso.d20charsheet.io.char_io.CharExporterImporter;
import com.vitobasso.d20charsheet.io.char_io.ImportCharException;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_CHAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CREATE;
import static com.vitobasso.d20charsheet.util.font.FontUtil.BOLD_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 28/02/14.
 */
public class SelectCharActivity extends MainNavigationActvity {

    private List<CharBase> list;
    private ListView listView;
    private CharExporterImporter exporterImporter;

    @Override
    protected int getContentLayout() {
        return R.layout.list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exporterImporter = new CharExporterImporter(SelectCharActivity.this);

        CharDao dataSource = new CharDao(this);
        list = dataSource.listAll();
        dataSource.close();

        listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new ClickListener());
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ContextualActionBarListener());

        updateUI();
        setActionbarTitle(this, BOLD_FONT, getTitle());
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
            case BOOKS:
                backToBase();
                goToBooks();
                break;
        }
    }

    private void updateUI() {
        listView.setAdapter(new Adapter(this, list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create, menu);
        getMenuInflater().inflate(R.menu.import_char, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_create:
                goToCreateChar();
                return true;
            case R.id.action_import:
                exporterImporter.importChar();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE) {
            if (resultCode == RESULT_OK) {
                CharBase created = (CharBase) data.getSerializableExtra(EXTRA_CHAR);
                save(created);
            }
        } else if (requestCode == CharExporterImporter.REQUEST_EXPORT) {
            if (resultCode == RESULT_OK) {
                showToast("Export successful");
            } else {
                showToast("Failed to export");
            }
        } else if (requestCode == CharExporterImporter.REQUEST_IMPORT) {
            if (resultCode == RESULT_OK) {
                try {
                    CharBase imported = exporterImporter.handleImportResponse(data);
                    save(imported); //TODO create fromCursorBrief, load after save (to get class & race name)
                    showToast("Import successful");
                } catch (ImportCharException e) {
                    showToast("Failed to import");
                }
            } else {
                String appName = data.getComponent().getPackageName();
                String failMessage = "Failed to get a file from";
                String message = failMessage + " " + appName;
                showToast(message);
            }
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CharBase charToOpen = list.get(i);
            cache.setOpenedChar(charToOpen);
            pref.setLastOpenedCharId(charToOpen.getId());
            goToSummary();
        }
    }

    private class Adapter extends ArrayAdapter<CharBase> {

        public Adapter(Context context, List<CharBase> objects) {
            super(context, R.layout.simple_row, R.id.name, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
            String description = list.get(position).getDescription();
            ActivityUtil.populateTextView(view, R.id.name, description);
            return view;
        }
    }

    private class ContextualActionBarListener implements AbsListView.MultiChoiceModeListener {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.select_char, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_export:
                    exportSelected();
                    return finish(mode);
                case R.id.action_remove:
                    removeSelected();
                    return finish(mode);
                default:
                    return false;
            }
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            invalidateOptionsMenu();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {}

        private boolean finish(ActionMode mode) {
            mode.finish();
            return true;
        }
    }

    private void save(CharBase charBase) {
        CharDao dataSource = new CharDao(this);
        try {
            dataSource.save(charBase);
        }finally {
            dataSource.close();
        }

        list.add(charBase);
        updateUI();
    }

    private void exportSelected() {
        for (CharBase charBase : getSelected()) {
            exporterImporter.exportChar(charBase);
        }
    }

    private void removeSelected() {
        for (CharBase element : getSelected()) {
            remove(element);
        }
    }

    private List<CharBase> getSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        List<CharBase> toRemove = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            toRemove.add(list.get(position));
        }
        return toRemove;
    }

    private void remove(CharBase charBase) {
        CharDao dataSource = new CharDao(SelectCharActivity.this);
        dataSource.remove(charBase);
        dataSource.close();

        list.remove(charBase);
        updateUI();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
