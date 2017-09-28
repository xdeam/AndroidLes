package com.xdramer.changesize;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public int flag=0;
    public String sex;
    public String name="";
    public String age="";
    public String hobby="";
    public String rating;
    public String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View layout=findViewById(R.id.smsLayout);
        final EditText edSmsContent= (EditText) findViewById(R.id.smsContent);
        final EditText edPhone= (EditText) findViewById(R.id.phone);
        final Button sendSMS=(Button)findViewById(R.id.sendSMS);
        final RatingBar ratingBar= (RatingBar) findViewById(R.id.ratingBar);
         final ImageView img=(ImageView)findViewById(R.id.img);
        final EditText edName=(EditText)findViewById(R.id.xm);
        final EditText edAge=(EditText)findViewById(R.id.nl);
        Button submit=(Button)findViewById(R.id.submit);
        Button cancel=(Button)findViewById(R.id.cancel);
        final RadioGroup radioGroup=(RadioGroup)findViewById(R.id.xbGroup);
        final TextView  result=(TextView)findViewById(R.id.result);
        final DatePicker datePicker= (DatePicker) findViewById(R.id.datepicker);
        Switch switch1= (Switch) findViewById(R.id.switch1);
        final ProgressBar progressBar= (ProgressBar) findViewById(R.id.progressbar1);
        final Spinner spinner= (Spinner) findViewById(R.id.spinner1);
        String a[]={"天津","北京"};
       ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,a);

        spinner.setAdapter(arrayAdapter);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   //progressBar.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }else {
                   // progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                }

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0){
                    img.setImageResource(R.drawable.a);
                    flag=1;
                }
                else {
                    img.setImageResource(R.drawable.z);
                    flag=0;
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId==R.id.male){
                   sex="男";
                }
                else if (checkedId==R.id.female){
                    sex="女";
                }
            }
        });
        final CheckBox art=(CheckBox)findViewById(R.id.art);
        final CheckBox english=(CheckBox)findViewById(R.id.english);
        final CheckBox science=(CheckBox)findViewById(R.id.science);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(0);
                hobby="";
                edName.setText("");
                edAge.setText("");
               radioGroup.clearCheck();
                art.setChecked(false);
                english.setChecked(false);
                science.setChecked(false);
                result.setText("");
            }
        });

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  progressBar.setVisibility(View.VISIBLE);
                  sendSMSMessage(edSmsContent.getText().toString(),edPhone.getText().toString());
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                };
                timer.schedule(task, 3000);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address=spinner.getSelectedItem().toString();
                rating=ratingBar.getNumStars()+"";
                name=edName.getText().toString();
                age=edAge.getText().toString();
                if (art.isChecked()){
                    hobby+="艺术 ";
                }
                if (english.isChecked()){
                    hobby+="英语";
                }
                if (science.isChecked()){
                    hobby+="科学";
                }
                /*findViewById(R.id.qd).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                datePicker.setVisibility(View.GONE);
                            }
                        }
                );*/

               // progressBarH.setVisibility(View.VISIBLE);
                final String year=datePicker.getYear()+"";
                final String month=datePicker.getMonth()+1+"";
                final String date=datePicker.getDayOfMonth()+"";
                rating=ratingBar.getRating()+"";
                final String text="姓名："+name+"\n住址:"+address+"\n日期:"+year+"-"
                        +month+"-"+date+"\n年龄:"+age+"\n性别:"+sex+"\n爱好:"+hobby+"\n评分:"+rating;
                final String smsText="姓名:"+name+" 住址:"+address+" 日期:"+year+"-"
                        +month+"-"+date+" 年龄:"+age+" 性别:"+sex+" 爱好:"+hobby+" 评分:"+rating;
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("提交吗？");
                dialog.setMessage(text);
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             result.setText(text);
                             edSmsContent.setText(smsText);
                    }
                });
                dialog.show();
            }
        });
    }
    public  boolean sendSMSMessage(String sms,String number){
        boolean result=false;

            SmsManager manager = SmsManager.getDefault();
        ArrayList<String> texts = manager.divideMessage(sms);
        for (String text : texts) {
            manager.sendTextMessage(number, null, text, null, null);
        }

    return result;
    }
}
