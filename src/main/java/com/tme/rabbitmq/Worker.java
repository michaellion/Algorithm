package com.tme.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {
	private static final String task_queue = "task_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(task_queue, true, false, false, null);
		channel.basicQos(1);

		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("[x] Received '" + message + "'");
				try {
					doWork(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					System.out.println("[x] Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}

		};
		channel.basicConsume(task_queue, false, consumer);
	}

	private static void doWork(String task) throws Exception {
		// TODO Auto-generated method stub
		for (char ch : task.toCharArray()) {
			if (ch == '.') {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
					Thread.currentThread().interrupt();
				}

			}
		}
	}
}
