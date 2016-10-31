package com.example.musicplayer;



import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {
	//�������⣬�����ı���
	TextView title ,author;
	//����/��ͣ��ֹͣ��ť
	ImageButton play,stop;
	//����㲥������
	ActivityReceiver activityReceiver;
	//���Ʋ��š���ͣ
	public static final String CONTROL="android.control";
    //���½�����ʾ
	public static final String UPDATE="android.update";
	//���岥��״̬��0x11:δ���ţ�0x12�����ڲ��ţ�0x13����ͣ
	int status=0x11;
	//������
	String [] titleStrs=new String[]{"Funny Day","Reset","Unity"};
	//�ݳ���
	String [] authorStrs=new String[]{"Nichole Alden","theFatRat","Tiger JK��jinsi"};

	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ָ�������ļ�
		setContentView(R.layout.activity_main);
		//��ȡ��������е�������ť�Լ������ı���ʾ��
		play=(ImageButton)this.findViewById(R.id.play);
		stop=(ImageButton)this.findViewById(R.id.stop);
		title=(TextView)this.findViewById(R.id.title);
		author=(TextView)this.findViewById(R.id.author);
		//Ϊ������ť��Ӽ�����,MainActivityʵ����OnClickListener�ӿ�
		play.setOnClickListener(this);
		stop.setOnClickListener(this);
		//�����㲥�����߶���
		activityReceiver=new ActivityReceiver();
		//����IntentFilter
		IntentFilter filter=new IntentFilter(UPDATE);
		registerReceiver(activityReceiver,filter);
		Intent intent=new Intent(this,MusicService.class);
		//������̨Service
		startService(intent);
	}
		public class ActivityReceiver extends BroadcastReceiver {
        public void onReceiver(Context context,Intent intent){
				//��ȡIntent�е�update��Ϣ
				int update=intent.getIntExtra("update", -1);
				//��ȡ��ǰ�������ֵ����
				int current=intent.getIntExtra("current", -1);
			  //���current ��Ϊ-1������ʾ���ڲ��ŵ����������ݳ���
				if(current>=0){
					title.setText(titleStrs[current]);
					     author.setText(authorStrs[current]);
				}
				//δ����״̬��ʾ���Ű�ť

				switch(update){
				case 0x11:
					play.setImageResource(R.drawable.selector_btn);
					//����״̬������ʹ����ͣͼ��
				   case 0x12:
						play.setImageResource(R.drawable.stop);
					status=0x12;break;
					//��ͣ״̬������ʹ�ò���ͼ��
				   case 0x13:
			 play.setImageResource(R.drawable.stop);
						status=0x13;
						break;
				}
			}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
       }
		
	

	@Override
	public void onClick(View source) {
		// TODO Auto-generated method stub
		//����Intent
		Intent intent=new Intent (CONTROL);
		switch(source.getId()){
			//����"����"/"��ͣ"��ť
			case R.id.play:
				intent.putExtra("control",1);break;
			//����"ֹͣ"��ť
			case R.id.stop:
				intent.putExtra("control",2);break;	
		}
		//���͹㲥������Service�еĹ㲥�������յ�
		sendBroadcast(intent);
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
}

