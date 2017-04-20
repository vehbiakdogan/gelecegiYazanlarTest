package firebasetest.vehbiakdogan.com.firebasetest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    private static final int RC_PHOTO_PICKER =  2;
    String TAG = MainActivity.class.getSimpleName();
    String messageImg= ""; // mesaj görseli yok ise boş veri gitsin sunucuya null girdiğimde hata veriyor.
    int kategoriID = 1; // mesajın hangi kategoriye ait olduğunu belli etsin.

    ListView listView;
    ImageView galeriBtn;
    EditText  mesajtv;
    Button gonderBtn;
    private ChatMessageAdapter chatMessageAdapter;

    FirebaseDatabase mDatabase;
    DatabaseReference chatsRef;
    FirebaseUser user;
    ChatMessage msg;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        mesajtv = (EditText) findViewById(R.id.mesaj_tv);
        // login işlemini hallettik.
        FirebaseInitalize();

        // yeni mesaj varmı dinliyoruz.
        chatMessageAdapter = new ChatMessageAdapter(getApplicationContext(),R.id.list_view);
        listView.setAdapter(chatMessageAdapter);

        // yeni mesaj dinleyicisi
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessageAdapter.clear();
                for(DataSnapshot mesajlar : dataSnapshot.getChildren()) {
                    msg = mesajlar.getValue(ChatMessage.class);
                    // mesajı listeye  eklemeden önce kategori id sine göre mesaj buraya ait değilse atla.
                    if(kategoriID != msg.getKategori() ) continue;

                    chatMessageAdapter.add(mesajlar.getValue(ChatMessage.class));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        chatMessageAdapter.notifyDataSetChanged();


    }

    private void FirebaseInitalize() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        chatsRef = mDatabase.getReference("chats");
        storage = FirebaseStorage.getInstance();
        storageRef =  storage.getReference().child("chats_img"); // storagedeki chats_ing klasörüne bağlandım


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Giriş Başarılı", Toast.LENGTH_LONG).show();
                } else {
                    // giriş yapılmamış ise direk giriş yap
                    mAuth.signInWithEmailAndPassword("vehbi@gmail.com","1234").addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Kayıt Başarısız",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        };
    }

   // mesaj gönderme kısmı burada
    public void MesajGonder(View v) {
        // mesaj gönderme işlemleri

        String mesaj = mesajtv.getText().toString();
        if(mesaj.trim().length() == 0) {
            Toast.makeText(getApplicationContext(),"Boş Mesaj Gönderilemez!",Toast.LENGTH_LONG).show();
        }else {
            // sadece mesaj gönderiliyor.
            chatsRef.child(chatsRef.push().getKey()).setValue(new ChatMessage(mesaj,user.getUid(),kategoriID,""));

        }
    }
    public void ResimSec(View v) {
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
                    messageImg =  taskSnapshot.getDownloadUrl().toString();
                    // sadece resim gönderiliyor.
                    chatsRef.child(chatsRef.push().getKey()).setValue(new ChatMessage("",user.getUid(),kategoriID,messageImg));
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
