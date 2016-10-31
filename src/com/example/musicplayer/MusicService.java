package com.example.musicplayer;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;


public class MusicService extends Service{
	//�����㲥������
	ServiceReceiver serviceReceiver;
	//��Դ������
	AssetManager am;
	//���弸�׸���
	String[] musics =new String[]{"Funny Day.mp3","Reset.mp3","Unity.mp3"};
	MediaPlayer mPlayer;
	//��ǰ��״̬��0x11:δ���ţ�0x12�����ڲ��ţ�0x13����ͣ
	int status=0x11;
	//��¼��ǰ���ڲ��ŵ����ֵ����
	int current=0;
	public IBinder onBind(Intent intent){
		return null;
	}
	public void onCreate(){
		//����Context��ķ���
		am=getAssets();
		//�����㲥�����߶���
		serviceReceiver=new ServiceReceiver();
		//����IntentFilter
		IntentFilter filter=new IntentFilter(MainActivity.CONTROL);
		//ע��㲥������
		registerReceiver(serviceReceiver,filter);
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(new OnCompletionListener(){
			public void onCompletion(MediaPlayer mp){
			current++;
				//�ж��Ƿ񳬳���Χ������������ִӵ�һ�׿�ʼ
				if(current>=3){
					current=0;
			    }
				Intent sendIntent=new Intent(MainActivity.UPDATE);
				sendIntent.putExtra("current",current);
				//���͹㲥������Activity�Ĺ㲥���������յ�
				sendBroadcast(sendIntent);
				//׼������������
				prepareAndPlay(musics [current]);
			 }
		});
		//����ý�岥����
		super.onCreate();
	}
	public class ServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(final Context context, Intent intent) {
			// TODO Auto-generated method stub
			int control=intent.getIntExtra("control", -1);
			switch(control){
			//�����ˡ����š�����ͣ����ť
			case 1:
			//ԭ������û�в���״̬
				if (status==0x11){
					//׼������������
					prepareAndPlay(musics [current]);
					status=0x12;
			}
				//ԭ�����ڲ���״̬
				else if (status==0x12){
					//��ͣ
					mPlayer.pause();
					//�ı�״̬
					status=0x13;
				}
				//ԭ��������ͣ״̬
				else if (status==0x13){
					//����
					mPlayer.start();
					//�ı�״̬
					status=0x12;
			}
				break;
				//ֹͣ����
			case 2:
				//���ԭ�����ڲ��Ż���ͣ
				if (status==0x12||status==0x13){
					//ֹͣ����
					mPlayer.stop();
					status=0x11;
				}
		}
			Intent sendIntent=new Intent(MainActivity.UPDATE);
			sendIntent.putExtra("update", status);
			sendIntent.putExtra("current", current);
			//���͹㲥������Acitvity����еĹ㲥���������յ�
			sendBroadcast(sendIntent);
		}
	}
	private void prepareAndPlay(String music){
		try{
			//��ָ�������ļ�
			AssetFileDescriptor afd=am.openFd(music);

			mPlayer.reset();
			//ʹ��MediaPlayer����ָ���������ļ�
			mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			//׼������
			mPlayer.prepare();
			//����
			mPlayer.start();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


}

