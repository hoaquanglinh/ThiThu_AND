package com.example.thithu_and;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText search;
    APIService apiService;
    AdapterXemay adapter;
    ArrayList<XeMay> list;
    File file;
    EditText edten, edgia, edmau, edmota;
    ImageView edimage;
    XeMay xeMay;
    MultipartBody.Part multipartBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        search = findViewById(R.id.search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        loadData();

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                them(MainActivity.this, 0, xeMay);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editable.toString().trim();
                searchDistributor(keyword);
            }
        });
    }

    void loadData() {
        Call<ArrayList<XeMay>> call = apiService.getXe();

        call.enqueue(new Callback<ArrayList<XeMay>>() {
            @Override
            public void onResponse(Call<ArrayList<XeMay>> call, Response<ArrayList<XeMay>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new AdapterXemay(list, getApplicationContext(), MainActivity.this);

                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<XeMay>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

    public void them(Context context, int type, XeMay xeMay) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_save);

        edten = dialog.findViewById(R.id.edten);
        edgia = dialog.findViewById(R.id.edgia);
        edmota = dialog.findViewById(R.id.edmota);
        edmau = dialog.findViewById(R.id.edmau);
        edimage = dialog.findViewById(R.id.edImage);

        edimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        if (type != 0){
            edten.setText(xeMay.getTen_ph43159());
            edgia.setText(xeMay.getGia_ph43159()+"");
            edmota.setText(xeMay.getMota_ph43159());
            edmau.setText(xeMay.getMau_ph43159());
            Glide.with(context)
                    .load(xeMay.getAnh_ph43159())
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .into(edimage);
            Log.d(TAG, "them: " + xeMay.getAnh_ph43159());
        }

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, RequestBody> mapRequestBody = new HashMap<>();
                String ten = edten.getText().toString();
                String giastr = edgia.getText().toString();
                String mota = edmota.getText().toString();
                String mau = edmau.getText().toString();

                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("anh_ph43159", file.getName(), requestFile);
                } else {
                    multipartBody = null;
                }

                if (ten.length() == 0 || giastr.length() == 0 || mau.length()==0){
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Double gia = Double.parseDouble(giastr);

                    mapRequestBody.put("ten_ph43159", getRequestBody(ten));
                    mapRequestBody.put("mau_ph43159", getRequestBody(mau));
                    mapRequestBody.put("gia_ph43159", getRequestBody(String.valueOf(gia)));
                    mapRequestBody.put("mota_ph43159", getRequestBody(mota));

                    if (gia>0){
                        if (type == 0){
                            Call<XeMay> call = apiService.addXe(mapRequestBody, multipartBody);
                            call.enqueue(new Callback<XeMay>() {
                                @Override
                                public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                                    if (response.isSuccessful()) {
                                        loadData();
                                        Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<XeMay> call, Throwable t) {
                                    Log.e("Home", "Call failed: " + t.toString());
                                    Toast.makeText(MainActivity.this, "Đã xảy ra lỗi khi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            if (file != null){
                                Call<XeMay> call = apiService.updateXe(mapRequestBody, xeMay.get_id(),  multipartBody);
                                call.enqueue(new Callback<XeMay>() {
                                    @Override
                                    public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                                        loadData();
                                        Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<XeMay> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Call<XeMay> call = apiService.updateNoImage(mapRequestBody, xeMay.get_id());
                                call.enqueue(new Callback<XeMay>() {
                                    @Override
                                    public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                                        loadData();
                                        Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<XeMay> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        file = null;

                    }else{
                        Toast.makeText(context, "Gia phai lon hon 0", Toast.LENGTH_SHORT).show();
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(context, "Gia phai la so", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }
    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImage.launch(intent);

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        file = createFileFormUri(imageUri, "anh_ph43159");

                        Glide.with(edimage)
                                .load(imageUri)
                                .skipMemoryCache(true)
                                .into(edimage);
                    }
                }
            });

    private File createFileFormUri(Uri path, String name) {
        File _file = new File(MainActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = MainActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "createFileFormUri: " + "loi anh");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "createFileFormUri: " + "loi anh 2");
        }

        return null;
    }

    public  void xoa(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<XeMay> call = apiService.deleteXe(id);
                call.enqueue(new Callback<XeMay>() {
                    @Override
                    public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                        if (response.isSuccessful()) {
                            loadData();
                            Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<XeMay> call, Throwable t) {
                        Log.e("Home", "Call failed: " + t.toString());
                        Toast.makeText(MainActivity.this, "Đã xảy ra lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    private void searchDistributor(String keyword) {
        Call<ArrayList<XeMay>> call = apiService.searchXe(keyword);
        call.enqueue(new Callback<ArrayList<XeMay>>() {
            @Override
            public void onResponse(Call<ArrayList<XeMay>> call, Response<ArrayList<XeMay>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new AdapterXemay(list, getApplicationContext(), MainActivity.this);

                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<XeMay>> call, Throwable t) {
                Log.e("Search", "Search failed: " + t.toString());
                Toast.makeText(MainActivity.this, "Đã xảy ra lỗi kh tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public void ttct (String id) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_save);

        TextView titlte = dialog.findViewById(R.id.tvtitle);
        titlte.setText("Thong tin chi tiet");

        edten = dialog.findViewById(R.id.edten);
        edgia = dialog.findViewById(R.id.edgia);
        edmota = dialog.findViewById(R.id.edmota);
        edmau = dialog.findViewById(R.id.edmau);
        edimage = dialog.findViewById(R.id.edImage);

        edten.setEnabled(false);
        edgia.setEnabled(false);
        edmota.setEnabled(false);
        edmau.setEnabled(false);
        edimage.setEnabled(false);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setVisibility(View.GONE);
        
        Call<XeMay> call = apiService.getXeId(id);
        call.enqueue(new Callback<XeMay>() {
            @Override
            public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                if(response.isSuccessful()){
                    edten.setText(response.body().getTen_ph43159());
                    edgia.setText(response.body().getGia_ph43159() + "");
                    edmau.setText(response.body().getMau_ph43159());
                    edmota.setText(response.body().getMota_ph43159());

                    Glide.with(MainActivity.this)
                            .load(response.body().getAnh_ph43159())
                            .thumbnail(Glide.with(MainActivity.this).load(R.drawable.loading))
                            .into(edimage);
                }
            }

            @Override
            public void onFailure(Call<XeMay> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Loi ttct", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}