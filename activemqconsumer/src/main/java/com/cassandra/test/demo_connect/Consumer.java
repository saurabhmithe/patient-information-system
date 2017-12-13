package com.cassandra.test.demo_connect;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * The SimpleQueueSender class consists only of a main method,
 * which sends several messages to a queue.
 * <p>
 * Run this program in conjunction with SimpleQueueReceiver.
 * Specify a queue name on the command line when you run the
 * program.  By default, the program sends one message.  Specify
 * a number after the queue name to send that number of messages.
 * <p>
 * The SimpleQueueSender class consists only of a main method,
 * which sends several messages to a queue.
 * <p>
 * Run this program in conjunction with SimpleQueueReceiver.
 * Specify a queue name on the command line when you run the
 * program.  By default, the program sends one message.  Specify
 * a number after the queue name to send that number of messages.
 */

/**
 * The SimpleQueueSender class consists only of a main method,
 * which sends several messages to a queue.
 *
 * Run this program in conjunction with SimpleQueueReceiver.
 * Specify a queue name on the command line when you run the
 * program.  By default, the program sends one message.  Specify
 * a number after the queue name to send that number of messages.
 */


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;


/*
 * HappyPatients Hospital consumer
 * ActiveMQ consumer code snippet source -> activemq.apache.org
 *
 */

public final class Consumer {

    /**
     *
     */
    private Consumer() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {

        String url = "tcp://localhost:61616";
        if (args.length > 0) {
            url = args[0];
        }

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Destination destination = new ActiveMQQueue("HappyPatientsHospital");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(destination);

        for (; ; ) {
            System.out.println("Waiting for message.");
            Message message = consumer.receive();
            if (message == null) {
                break;
            }
            System.out.println("Got message, send to the analytic engine : " + message);
            System.out.println("Got message, send mail to the user : " + ((TextMessage) message).getText());

        }

        connection.close();
    }
}
