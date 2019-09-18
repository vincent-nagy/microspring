package be.fenego.microspring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import be.fenego.microspring.dtos.EnrichedStore;
import be.fenego.microspring.services.StoreService;

@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableConfigurationProperties(StoreHoursConfig.class)
@Import(StoreWebApplication.class)
@RestController
public class StoreServer {

	public static final String ISH_STORE_URL = "http://localhost/INTERSHOP/rest/WFS/inSPIRED-inTRONICS-Site/smb-responsive/stores";
	
	@Autowired
	private StoreService storeService;
	
	public static void main(String[] args) {
		System.setProperty("spring.config.name", "store-server");
		
		SpringApplication.run(StoreServer.class, args);
	}

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
    @Bean
    public StoreService storeService() {
        return new StoreService(ISH_STORE_URL);
    }
    
    @RequestMapping("/store")
    public List<EnrichedStore> getAllStores() {
    	return storeService.getAllStores();
    }
}
