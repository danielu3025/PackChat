package shenkar.koruApps.PackChetApp.fragements;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import shenkar.koruApps.PackChetApp.R;
import shenkar.koruApps.PackChetApp.events.ChangeTypeOfStorageEvent;
import shenkar.koruApps.PackChetApp.events.FileEvent;
import shenkar.koruApps.PackChetApp.objects.Model;
import shenkar.koruApps.PackChetApp.objects.Utils;


public class StorageFragment extends Fragment {
    Model model = Model.getInstance();
    Activity activity;

    Utils utils = new Utils();
    ListView storageContainer;
    Button btLect;
    Button btPract;
    Button btFiles;
    DatabaseReference dref;
    StorageReference sref;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    public StorageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        // Inflate the layout for this fragment
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        storageContainer = (ListView) view.findViewById(R.id.storageContainer);
        btLect = (Button) view.findViewById(R.id.btLect);
        btPract = (Button) view.findViewById(R.id.btPract);
        btFiles = (Button) view.findViewById(R.id.btFiles);


        items = new ArrayList<String>();

        dref = utils.getDbRef("db/" + model.currantCourse + "/storage");
        //sref = model.strogRef.child("courses").child(model.currantCourse);
        sref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://packchet-6ce21.appspot.com/courses/All");


        EventBus.getDefault().post(new ChangeTypeOfStorageEvent("lectures"));
        setListeners();


        return view;
    }

    private void updateList() {
        itemsAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, items);
        storageContainer.setAdapter(itemsAdapter);
    }

    @Subscribe
    public void chagneStorage(ChangeTypeOfStorageEvent event) {
        switch (event.getMsg()) {
            case "lectures":
                dref = utils.getDbRef("db/" + model.currantCourse + "/storage").child("lectures");
                break;
            case "practice":
                dref = utils.getDbRef("db/" + model.currantCourse + "/storage").child("practice");
                break;
            case "docs":
                dref = utils.getDbRef("db/" + model.currantCourse + "/storage").child("docs");
                break;
            default:
                dref = utils.getDbRef("db/" + model.currantCourse + "/storage").child("lectures");
        }
        items.removeAll(items);
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    items.add(ds.getValue().toString());
                }
                updateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setListeners() {
        btLect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeTypeOfStorageEvent("lectures"));
            }
        });
        btPract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeTypeOfStorageEvent("practice"));
            }
        });
        btFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeTypeOfStorageEvent("docs"));
            }
        });

        storageContainer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                sref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://packchet-6ce21.appspot.com/courses/All").child(items.get(position));
                final File file = new File(getActivity().getExternalCacheDir(), items.get(position));
                if (!file.exists()) {
                    System.out.println("Downloading file: " + file.getName());
                    EventBus.getDefault().post(new FileEvent(items.get(position),"download",file));
                }
                else {
                    EventBus.getDefault().post(new FileEvent(items.get(position),"open",file));
                }

                return true;
            }
        });
    }

    @Subscribe
    public void fileAction(final FileEvent event) {
        if (Objects.equals(event.getAction(), "download")) {
            sref.getFile(event.getEventFile()).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    System.out.println("Download complete: " + event.getEventFile().getName());
                    EventBus.getDefault().post(new FileEvent(event.getFileFullName(),"open",event.getEventFile()));
                }
            });
        }
        else {
            Intent target = new Intent(Intent.ACTION_VIEW);
            String type = "";
            String[] words = event.getFileFullName().split("\\.", -1);
            if (Objects.equals(words[1], "docx") || Objects.equals(words[1], "doc")) {
                type = "application/msword";
            } else if (Objects.equals(words[1], "pdf")) {
                type = "application/pdf";
            } else if (Objects.equals(words[1], "xls")) {
                type = "application/vnd.ms-excel";
            } else if (Objects.equals(words[1], "ppt")) {
                type = "application/vnd.ms-powerpoint";
            } else if (Objects.equals(words[1], "mp4")) {
                type = "video/mp4";
            } else if (Objects.equals(words[1], "jpeg") || Objects.equals(words[1], "jpg")) {
                type = "image/jpeg";
            } else if (Objects.equals(words[1], "png")) {
                type = "image/png";
            } else if (Objects.equals(words[1], "mp3")) {
                type = "audio/mpeg3";
            }

            target.setDataAndType(Uri.fromFile(event.getEventFile()), type);

            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
    }
}