package com.daemon.kafka.producer;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

public class KafkaDaemonProducer {
	
	
	public static void main(String [] args) throws IOException {
		S3Function s3function = new S3Function(); 
		//Amazon S3에 연결하기 위한 객체 선언
		
		Future future = null;
		//Kafka Producer가 정상적으로 broker에게 정보를 전달 하였느지 확인하기 위한 객체. producer.send의 반환형이 Future형이다.
		
		Properties properties = new Properties();
		//Kakfa Producer의 property를 담기위한 객체 선언
		
		properties.setProperty("bootstrap.servers", "<YOUR_BROKER_SERVER>:<BROKER_PORT>");
		//Kafka Cluster location setting, 복수개라면 여러개 명시가 가능하다.
		
		properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		//kafka key-value serializer lib 설정
		
		properties.put("acks", "0 | 1 | -1(all)");
		//defalut = 1
		//0 -> 프로듀서는 서버로부터 ack응답을 받지 않음. 
		//1 -> Leader(클러스터에 있는 데이터의 원본)은 로그를 로컬에 지속적으로 남기지만, follower(데이터 복제본)의 응답을 기다리지 않는다.
		//-1(all)-> leader는 follower의 모든 follower의 복제 싱크에 대한 ack를 모두 대기하는 통신방식이다.
		
		properties.setProperty("max.request.size", "YOUR_MAX_REQUEST_MESSAGE_SIZE");
		//요청의 최대 크기에 대한 설정
		
		properties.setProperty("send.buffer.bytes", "1999999999");
		//TCP 전송 버퍼 용량에 대한 설정
		
		properties.setProperty("buffer.memory", "1999999999");
		//Kafka가 서버로 전송하기 전 대기중인 레코드를 버퍼링 하는데 쓸 수 있는 메모리 용량
		
		
		properties.put("batch.size", "<YOUR_CUSTOMIZED_BATCH_SIZE>");
		//Kafka producer는 batch 작업 핸들링을 지원한다. 이 배치 작업에 대한 단위를 설정해 주는 파라미터이다.
		
		properties.put("linger.ms", "<YOUR_CUSTOMIZED_LINGER_MS>");
		//주로 batch작업에 많이 쓰이며, 배치 작업 간 인위적인 딜레이를 추가하는 파라미터이다.

		
		KafkaProducer <String, String> producer = new KafkaProducer <String, String>(properties);
		
		        
		
		String result = s3function.getObject();

		try {
			future = producer.send(new ProducerRecord<String, String>("test3", result));
			//test3이라는 토픽으로 레코드를 전송한다.
		}catch (KafkaException e) {
		     e.printStackTrace();
		 }
		
			producer.close();
			System.out.println(future.isDone());
	}
}
