package com.vituel.dndplayer.activity.select;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.parser.json.CharJsonParser;

import java.util.List;

import static android.widget.AdapterView.OnItemLongClickListener;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_MODE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CREATE;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.ActivityUtil.internationalize;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 28/02/14.
 */
public class SelectCharActivity extends ListActivity {

    private List<CharBase> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        CharDao dataSource = new CharDao(this);
        list = dataSource.listAll();

        updateUI();

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new ClickListener());
        listView.setOnItemLongClickListener(new LongClickListener());

        dataSource.close();

        setActionbarTitle(this, BOLD_FONT, getTitle());
    }

    private void updateUI() {
        setListAdapter(new Adapter(this, android.R.layout.simple_list_item_1, list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:

                //create new character
                Intent intent = new Intent(this, EditCharActivity.class);
                intent.putExtra(EXTRA_MODE, REQUEST_CREATE);
                startActivityForResult(intent, REQUEST_CREATE);
                return true;

            default:
                return defaultOnOptionsItemSelected(item, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE:
                switch (resultCode) {
                    case RESULT_OK:

                        //save to db
                        CharBase base = (CharBase) data.getSerializableExtra(EXTRA_CHAR);
                        CharDao dataSource = new CharDao(this);
                        dataSource.save(base);
                        dataSource.close();

                        //update ui
                        list.add(base);
                        updateUI();

                }
                break;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CHAR, list.get(i));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class LongClickListener implements OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectCharActivity.this);
            builder.setItems(R.array.select_char_dialog_items, new DialogClickListener(pos));
            Dialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }

    private class DialogClickListener implements DialogInterface.OnClickListener{

        private int pos;

        private DialogClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0: //export
                    CharJsonParser writer = new CharJsonParser(SelectCharActivity.this);
                    writer.exportChar(list.get(pos));
                    break;
                case 1: //remove
                    //delete from db
                    CharDao dataSource = new CharDao(SelectCharActivity.this);
                    dataSource.remove(list.get(pos));
                    dataSource.close();

                    //update ui
                    dialog.dismiss();
                    list.remove(pos);
                    updateUI();
                    break;
            }
        }
    }

    private class Adapter extends ArrayAdapter<CharBase> {

        public Adapter(Context context, int resource, List<CharBase> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            String description = list.get(position).getDescription();
            description = internationalize(description, SelectCharActivity.this);
            view.setText(description);
            return view;
        }
    }
}
