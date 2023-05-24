package com.example.isochannelproject;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IsoDeLogRestApplication {

	public static void main(String[] args) {


			SpringApplication app = new SpringApplication(IsoDeLogRestApplication.class);
			app.setBannerMode(Banner.Mode.CONSOLE);
			app.run(args);
	}

}
