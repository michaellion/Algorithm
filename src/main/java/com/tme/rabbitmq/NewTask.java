package com.tme.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
	
private static final String QUEUE_NAME="task_queue";
	
	
	public static void main(String [] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		
		channel.queueDeclare( QUEUE_NAME, true,false,false,null);
		String message = getMessage(args);
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}
	
	private static String getMessage(String [] strings) {
		// TODO Auto-generated method stub
		if(strings.length<1) {
			return "hello World!";
		}
		return joinStrings(strings,"");
	}

	private static String joinStrings(String[] strings,String delimiter) {
		int length = strings.length;
		if(length==0) {
			return "";
		}
		StringBuilder words = new StringBuilder(strings[0]);
		words.append(delimiter).append(strings[0]);
		return words.toString();
	}
}
