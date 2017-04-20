package firebasetest.vehbiakdogan.com.firebasetest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class TimelineActivity extends AppCompatActivity {
    private ListView listView;
    private Timeline timeline;
    private TimelineAdapter timelineAdapter;
    private EditText newPost;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference postRef;
    private FirebaseUser user;


    // kategori id
    int kategoriId = 1;
    String postImg= "";



    private static final int RC_PHOTO_PICKER =  2;
    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        timelineAdapter = new TimelineAdapter(getApplicationContext(),R.id.list_view);
        listView = (ListView) findViewById(R.id.post_listView);
        newPost = (EditText) findViewById(R.id.newPost);

        // deneme Ekleme
        listView.setAdapter(timelineAdapter);
        FirebaseInit();

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                timelineAdapter.clear();
                for( DataSnapshot post: dataSnapshot.getChildren()) {
                    timeline = post.getValue(Timeline.class);
                    timelineAdapter.add(timeline);
                }
                timelineAdapter.setNotifyOnChange(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void FirebaseInit() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        postRef = mDatabase.getReference("posts");
        storage = FirebaseStorage.getInstance();
        storageRef =  storage.getReference().child("posts_img"); // storagedeki chats_ing klasörüne bağlandım


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Giriş Başarılı", Toast.LENGTH_LONG).show();
                } else {
                    // giriş yapılmamış ise direk giriş yap
                    mAuth.signInWithEmailAndPassword("vehbi@gmail.com","1234").addOnCompleteListener(TimelineActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.e(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Giriş Başarısız",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        };
    }

    public void postAt(View v) {
        String postMessage = newPost.getText().toString();
        if(postMessage.trim().length() == 0) {
            Toast.makeText(getApplicationContext(),"Boş Bir Post Atamazsınız!",Toast.LENGTH_LONG).show();
        }else {
            String profileImg = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "http://sariyer.istanbulsaglik.gov.tr/themes/img/default-user-icon-profile.png";
            timeline = new Timeline(postRef.getKey(),kategoriId,0,user.getEmail(),profileImg,postMessage,"");
            postRef.child(postRef.push().getKey()).setValue(timeline);

        }
    }
    public void ResimSec2(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçildi"), RC_PHOTO_PICKER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            Uri resimUri = data.getData();
            StorageReference photoRef = storageRef.child(resimUri.getLastPathSegment());
            photoRef.putFile(resimUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    postImg =  taskSnapshot.getDownloadUrl().toString();
                    // sadece resim gönderiliyor.
                    String profileImg = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "http://sariyer.istanbulsaglik.gov.tr/themes/img/default-user-icon-profile.png";
                    timeline = new Timeline(postRef.push().getKey(),kategoriId,0,user.getEmail(),profileImg,"",postImg);
                    postRef.child(postRef.push().getKey()).setValue(timeline);
                }
            });

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
