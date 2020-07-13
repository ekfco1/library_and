package com.example.libraryproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText et_mem_id;
    private  EditText et_mem_pw;
    private  EditText et_mem_name;
    private  EditText et_mem_phone;
    private RadioButton mem_member;
    private RadioButton mem_worker;
    private RadioGroup rg;

     String USER_FLAG_VALUE = "-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_mem_id = (EditText) findViewById(R.id.et_mem_id);
        et_mem_pw = (EditText) findViewById(R.id.et_mem_pw);
        et_mem_name = (EditText) findViewById(R.id.et_mem_name);
        et_mem_phone = (EditText)findViewById(R.id.et_mem_phone);
        rg = (RadioGroup)findViewById(R.id.rg);
        mem_member = (RadioButton) findViewById(R.id.mem_member);
        mem_member.setTag("MEMBER"); // 라디오 버튼에 태그를 달아주기
        mem_worker = (RadioButton)findViewById(R.id.mem_worker);
        mem_worker.setTag("WORKER");
        // 태그를 달아주는 이유는 setOnCheckedCahgeListener 에서 어떤 라디어 버튼이 눌러졌는지 판단을 할수 없어서,
        // 태그 값을 붙여주는거야

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton CHECKED_BUTTON = (RadioButton) findViewById(i);
                if (CHECKED_BUTTON.getTag().equals("MEMBER")) {
                    USER_FLAG_VALUE = "0";
                }else{
                    USER_FLAG_VALUE = "1";
                }
            }
        });
    };


    public void btnRegister(View v) {
        //et_mem_id.getText().length(); // 에디트 텍스트 문자의 길이를 가져오는 방법이야, 만약에

        if(et_mem_id.getText().length() == 0){
            et_mem_id.setError("아이디를 입력하세요");
            return; //

        }
        if(et_mem_pw.getText().toString().equals("")){
            et_mem_pw.setError("비밀번호를 입력하세요");
            return;
        }
        if(et_mem_name.getText().toString().equals("")){
            et_mem_name.setError("이름을 입력하세요.");
            return;
        }
        if(et_mem_phone.getText().toString().equals("")){
            et_mem_phone.setError("전화번호를 입력하세요.");
            return;
        }
         if(USER_FLAG_VALUE.equals("-1")){
             // java.lang.NullPointerException: Attempt to invoke virtual method 'boolean java.lang.String.isEmpty()' on a null object reference
             // 2020년 5월 27일 문자열 NullPoint Issue
             // 해결 -> 최상단의 USER_FLAG_VALUE 변수를 정의(define)만 하지않고 초기화 값까지 선언
             Toast.makeText(this.getApplicationContext(),"버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
             return;
        }
            RetrofitService service = new RetrofitCom().retrofit.create(RetrofitService.class);
            Call<MemDTO> call = service.getregister(et_mem_id.getText().toString(), et_mem_pw.getText().toString(), et_mem_name.getText().toString(), Integer.parseInt(et_mem_phone.getText().toString()), USER_FLAG_VALUE);
            Log.d("URL_TEST", call.request().url().toString());
            call.enqueue(new Callback<MemDTO>() {

                @Override
                public void onResponse(Call<MemDTO> call, Response<MemDTO> response) {
                    if (response.isSuccessful()) {
                        Log.d("REGISTER_TEST", response.body().getRESULT_CODE());
                        if(response.body().getRESULT_CODE().equals("Success")){
                            Toast.makeText(getApplicationContext(),"회원가입 성공.",Toast.LENGTH_SHORT).show();
                            Intent Rintent = new Intent(getApplicationContext(),MainActivity.class);
                            Rintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(Rintent);
                        }else if (response.body().getRESULT_CODE().equals("DuplicatedID")) {
                            Toast.makeText(getApplicationContext(),"중복아이디입니다.",Toast.LENGTH_SHORT).show();
//                            Intent Rintent = new Intent(getApplicationContext(),MainActivity.class);
//                            startActivity(Rintent);
                        } else {
                            Toast.makeText(getApplicationContext(),"처리할 수 없는 오류입니다.",Toast.LENGTH_SHORT).show();
                        }
                        MemDTO result = response.body();
                        Log.d("RETROFIT_SUC", response.body().toString());
                    } else {
                        Log.d("RETROFIT_FAIL", response.errorBody().toString());
                    }

                }

                @Override
                public void onFailure(Call<MemDTO> call, Throwable t) {
                    Log.d("RETROFIT_TEST", t.getLocalizedMessage());

                }
            });
    }


    }


