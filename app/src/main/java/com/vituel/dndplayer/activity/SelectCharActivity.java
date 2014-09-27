package com.vituel.dndplayer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.vituel.dnd_character_sheet.R;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.dao.CharDao;
import com.vituel.dndplayer.model.CharBase;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.*;
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

        setListAdapter(new ArrayAdapter<CharBase>(this, android.R.layout.simple_list_item_1, list));

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SELECTED, list.get(i));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectCharActivity.this);
                builder.setMessage(getString(R.string.char_remove))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //delete from db
                                CharDao dataSource = new CharDao(SelectCharActivity.this);
                                dataSource.remove(list.get(pos));
                                dataSource.close();

                                //update ui
                                dialog.dismiss();
                                list.remove(pos);
                                setListAdapter(new ArrayAdapter<CharBase>(SelectCharActivity.this, android.R.layout.simple_list_item_1, list));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        dataSource.close();

        setActionbarTitle(this, BOLD_FONT, getTitle());
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
                        CharBase base = (CharBase) data.getSerializableExtra(EXTRA_EDITED);
                        CharDao dataSource = new CharDao(this);
                        dataSource.save(base);
                        dataSource.close();

                        //update ui
                        list.add(base);
                        setListAdapter(new ArrayAdapter<CharBase>(SelectCharActivity.this, android.R.layout.simple_list_item_1, list));

                }
                break;
        }
    }

}
