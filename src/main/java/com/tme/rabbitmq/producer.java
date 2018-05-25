package com.tme.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class producer {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		com.rabbitmq.client.Connection connection =  factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello world";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println("[x] sent '"+message+"'");
		channel.close();
		connection.close();
	}

}
