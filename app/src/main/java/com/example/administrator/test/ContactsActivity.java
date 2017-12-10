package com.example.administrator.test;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * ContactsActivity
 * Created by My on 2017/12/10.
 */

public class ContactsActivity extends AppCompatActivity {
    private static final String TAG = "ContactsActivity";
    private ListView lv;
    private ArrayList<Contact> mContactArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        initUI();
        initData();
        initAdapter();
    }

    private void initAdapter() {
        lv.setAdapter(new ContactsAdapter());
    }

    private void initData() {
        mContactArrayList = new ArrayList<>();
        Contact contact;
        // 获取手机真机联系人信息
        // 1.先查raw_contacts表_id列(不是查contact_id列)
        ContentResolver contentResolver = getContentResolver();
        Uri uri_raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uri_data = Uri.parse("content://com.android.contacts/data");
        Cursor cursor_raw_contacts = contentResolver.query(uri_raw_contacts, new String[]{"_id"},
                null, null, null);
        if (cursor_raw_contacts != null && cursor_raw_contacts.getCount() > 0) {
            while (cursor_raw_contacts.moveToNext()) {
                contact = new Contact();
                int _id = cursor_raw_contacts.getInt(0);
                Log.d(TAG, "initData: _id:" + _id);
                // 2.根据_id(raw_contact_id),查data表data1、mimetype列
                Cursor cursor_data = contentResolver.query(uri_data, new String[]{"mimetype", "data1"},
                        "raw_contact_id=?", new String[]{_id + ""}, null);
                // 3.再根据mimetypes表区分数据
                if (cursor_data != null && cursor_data.getCount() > 0) {
                    Log.d(TAG, "initData: 执行了...");
                    while (cursor_data.moveToNext()) {
                        String mimetype = cursor_data.getString(0);
                        String data1 = cursor_data.getString(1);
                        if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            contact.name = data1;
                        } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            contact.phone = data1;
                        } else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                            contact.email = data1;
                        } else if ("vnd.android.cursor.item/sip_address".equals(mimetype)) {
                            contact.address = data1;
                        }
                    }
                    cursor_data.close();
                }
                mContactArrayList.add(contact);
            }
            cursor_raw_contacts.close();
        }

    }

    private void initUI() {
        lv = (ListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact1 = mContactArrayList.get(position);
                Intent intent = getIntent();
                intent.putExtra("phone", contact1.phone);
                setResult(10, intent);
                finish();
            }
        });
    }

    private class ContactsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mContactArrayList.size();
        }

        @Override
        public Contact getItem(int position) {
            return mContactArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.listview_contacts_item, null);
            }
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            Contact contact = getItem(position);
            tv_name.setText(contact.name);
            tv_phone.setText(contact.phone);
            return convertView;
        }
    }
}
