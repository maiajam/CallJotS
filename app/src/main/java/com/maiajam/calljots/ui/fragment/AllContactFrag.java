package com.maiajam.calljots.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.maiajam.calljots.R;
import com.maiajam.calljots.adapter.AllConAdapter;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.util.NewContactObserver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiAjam on 5/7/2018.
 */

public class AllContactFrag extends Fragment {

    ProgressBar progressBar;
    Cursor Cr_phonesNo;
    public RecyclerView recyclerView;
    public List<AllPhoneContact> phoneList;
    public ArrayList<AllPhoneContact> search_list;
    ArrayList<String> CeckChphoneList;
    ArrayList<AllPhoneContact> phoneContactsList;
    LoadPhone loadPhone = null;
    FloatingActionButton Add_b;
    public AllConAdapter allConAdapter;
    List<AllPhoneContact> allPhoneContact;
    private RoomManger roomManger;
    private Handler handler;


    public void AllContactFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_allcont, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.AllCon_Rec);
        Add_b = (FloatingActionButton) view.findViewById(R.id.addNewContact_fab);

        SharedPreferences sp = getContext().getSharedPreferences("MyFirstVisit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //   editor.putInt("first",1);
        // editor.commit();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (sp.getBoolean("first", false)) {
            NewContactObserver observer = new NewContactObserver(new Handler(), getContext());
            getContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, observer);
          AsyncTask.execute(new Runnable() {
              @Override
              public void run() {
                  roomManger = RoomManger.getInstance(getActivity());
                  RoomDao roomDao = roomManger.roomDao();
                  allPhoneContact = roomDao.getAllPhoneContact();
                  allConAdapter = new AllConAdapter(getActivity(), allPhoneContact, 0);

                    handler.sendEmptyMessage(0);
              }
          });

            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

                    if(allConAdapter != null)
                    {
                        recyclerView.setAdapter(allConAdapter);
                        allConAdapter.notifyDataSetChanged();
                    }
                    super.handleMessage(msg);
                }
            };

        } else {

            editor.putBoolean("first", true);
            editor.commit();
            editor.apply();
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                        10);
            } else {
                Cr_phonesNo = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                loadPhone = new LoadPhone();
            }
            if (Cr_phonesNo != null) {
                loadPhone.execute();
            }
        }

        Add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //    startActivity(new Intent(getActivity(), NewContact.class));
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                roomManger = RoomManger.getInstance(getActivity());
                RoomDao roomDao = roomManger.roomDao();
                allPhoneContact = roomDao.getAllPhoneContact();
                allConAdapter = new AllConAdapter(getActivity(), allPhoneContact, 0);
               handler.sendEmptyMessage(0);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(allConAdapter != null)
                {
                    recyclerView.setAdapter(allConAdapter);
                    allConAdapter.notifyDataSetChanged();
                }
                super.handleMessage(msg);
            }
        };

    }

    class LoadPhone extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            CeckChphoneList = new ArrayList<>();
            phoneList = new ArrayList<>();

            if (Cr_phonesNo.moveToFirst()) {
                do {

                    AllPhoneContact Phonecontact = new AllPhoneContact();
                    int contact_ID = Integer.parseInt(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
                    Phonecontact.setContId(contact_ID);
                    Phonecontact.setContName(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    Phonecontact.setContactPhotoUri(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
                    Phonecontact.setContPhoneNo(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    Bitmap contact_photo = getContactPhoto(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)), contact_ID);

                    if (!CeckChphoneList.contains(Phonecontact.getContName())) {
                        roomManger = RoomManger.getInstance(getActivity());
                        RoomDao roomDao = roomManger.roomDao();
                        roomDao.AddPhoneContacts(Phonecontact);
                        CeckChphoneList.add(Phonecontact.getContName());
                        phoneContactsList.add(Phonecontact);
                    }

                } while (Cr_phonesNo.moveToNext());
                Cr_phonesNo.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //     progressBar.setVisibility(View.GONE);
            allConAdapter = new AllConAdapter(getActivity(), allPhoneContact, 0);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(allConAdapter);
            allConAdapter.notifyDataSetChanged();
        }
    }

    private Bitmap getContactPhoto(String image_Uri, int contact_ID) {

        Bitmap photo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.conphoto);

        if (image_Uri != null) {
            try {
                photo = MediaStore.Images.Media
                        .getBitmap(getContext().getContentResolver(),
                                Uri.parse(image_Uri));

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return photo;


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Cr_phonesNo = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                    LoadPhone loadPhone;
                    loadPhone = new LoadPhone();
                    loadPhone.execute();
                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}

