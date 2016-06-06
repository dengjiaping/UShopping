package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AppAction;
import com.brand.ushopping.model.Feedback;
import com.brand.ushopping.model.User;

public class RefActivity extends Activity {
    private AppContext appContext;
    private User user;
    private ImageView backBtn;
    private TextView titleTextView;

    private String content;
    private EditText contentEditText;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ref);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        contentEditText = (EditText) findViewById(R.id.content);
        submitBtn = (Button) findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = contentEditText.getText().toString();
                if(content.isEmpty())
                {
                    Toast.makeText(RefActivity.this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Feedback feedback = new Feedback();
                    feedback.setUserId(user.getUserId());
                    feedback.setSessionid(user.getSessionid());
                    feedback.setCustomername(user.getUserName());
                    feedback.setContact(user.getUserName());
                    feedback.setContent(content);

                    new FeedbackSaveActionTask().execute(feedback);

                }

            }
        });


    }

    //提交反馈
    public class FeedbackSaveActionTask extends AsyncTask<Feedback, Void, Feedback>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Feedback doInBackground(Feedback... feedbacks) {
            return new AppAction().feedbackSaveAction(feedbacks[0]);
        }

        @Override
        protected void onPostExecute(Feedback result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.this);
                    builder.setMessage("反馈已提交,谢谢您的意见");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            submitBtn.setEnabled(false);
                            RefActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(RefActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(RefActivity.this, "反馈提交失败,请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
