package alibaba.dumy;

import alibaba.dumy.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DumyApplication {

	/*
	This is a dummy application that should contain the logic it is intended to do.
	Each service will contain the netty client and the metrics that will collect the system health info
	and send it to the controller.
	*/
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DumyApplication.class, args);
		System.out.println("Application running on port: " + context.getEnvironment().getProperty("server.port"));
	}
//
//	@Bean
//	public CommandLineRunner sendInitialMessage() {
//		return args -> {
//			while (true) {
//				nettyClient.sendMessage("Hello World");
//				Thread.sleep(2000);
//			}
//		};
//	}

}
